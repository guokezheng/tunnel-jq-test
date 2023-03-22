package com.ruoyi.quartz.task;

import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceDataRecord;
import com.tunnel.business.domain.protocol.SdDevicePoint;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataRecordMapper;
import com.tunnel.business.mapper.protocol.SdDevicePointMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.deal.plc.omron.OmronConnectProperties;
import com.tunnel.deal.plc.omron.exception.PlcException;
import com.tunnel.deal.plc.omron.finsTCP.OmronMessageHeader;
import com.tunnel.deal.plc.omron.finsTCP.OmronTcpClient;
import com.tunnel.deal.plc.omron.util.ByteUtil;
import com.zc.common.core.ThreadPool.ThreadPool;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * 欧姆龙定时任务调度
 */
@Component("omronTask")
public class OmronTask {


    private static final Logger log = LoggerFactory.getLogger(OmronTask.class);


//    @Value("${omron.service.ip}")
//    private String serviceIp;
//    @Value("${omron.service.prot}")
//    private Integer serviceProt;
//    @Value("${omron.host.ip}")
//    private String hostIp;


    private String serviceIp = "10.7.187.87";
    private Integer serviceProt = 9600;
    private String hostIp = "127.0.0.1";

    public static OmronTcpClient omronTcpClient;

    @Autowired
    private SdDevicePointMapper sdDevicePointMapper;
    @Autowired
    private ISdDeviceDataService sdDeviceDataService;
    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private SdDeviceDataRecordMapper sdDeviceDataRecordMapper;

    public void runTask(){
            if(omronTcpClient == null){
                try {
                    omronTcpClient = new OmronTcpClient(getOmronConnectProperties());
                    //初始化链接     服务器地址
                    omronTcpClient.init(serviceIp,serviceProt);
                    //获取ChannelFuture
                    ChannelFuture channelFuture = omronTcpClient.channelFuture;
                    //推送握手协议
                    omronTcpClient.send(omronTcpClient.successCallback(channelFuture).array());
                } catch (Exception e) {
                    log.error("omron链接异常。请联系管理员");
                    omronTcpClient = null;
                }
            }
            if(omronTcpClient!=null){
                //获取当前所有设备以及点位信息。       根据条件筛选 当前 项目下 欧姆龙FINS协议的点位信息 下所有点位信息
                SdDevicePoint sdDevicePoint = new SdDevicePoint();
                //获取可读点位信息   状态为 1
                sdDevicePoint.setIsReserved(1L);
                //筛选条件。   (当前为测试  默认筛选所有。后期根据设备类型  以及  协议ID进行筛选)
                List<SdDevicePoint> sdDevicePointList = sdDevicePointMapper.selectSdDevicePointList(sdDevicePoint);

                for (SdDevicePoint sdd:sdDevicePointList) {
                    byte[] data;
                    try {
                        //查看当前点位信息是否为   位信息。
                        boolean isBit = sdd.getAddress().contains(".")?true:false;
                        //读取数据
                        byte[] dataInfo = omronTcpClient.buildReadRequestBody(sdd.getAddress(),isBit);

                        OmronMessageHeader  omronMessageHeader = new OmronMessageHeader();
                        byte[] dataBody = omronTcpClient.getMessageBody(omronMessageHeader,dataInfo.length);
                        data = omronTcpClient.send(ByteUtil.byteMerger(dataBody,dataInfo));
                        //返回数据校验        true  成功   false  失败
                        if(omronTcpClient.doBuildResponseMessage(data)){
                            //解析当前数据  根据设备点位信息解析
                            if(isBit){
                                //获取结尾2字节  结果集
                                Integer number = ByteUtil.bytesToIntOfReverse2Byte(data,data.length-2);
                                ThreadPool.executor.execute(() ->{
                                    //数据持久化
                                    createDate(sdd,number.toString());
                                });
                            }else{
                                //获取结尾4字节  结果集
                                String number = omronTcpClient.getCodeByDataType(data,sdd.getDataType());
                                ThreadPool.executor.execute(() ->{
                                    //数据持久化
                                    createDate(sdd,number);
                                });
                            }
                        }
                    } catch (PlcException e) {
                        e.printStackTrace();
                    }
                }
            }
    }


    /**
     * 初始化欧姆龙配置信息  (当前为测试数据  后期整理为线上链接)
     * @return
     */
    public OmronConnectProperties getOmronConnectProperties(){
        OmronConnectProperties conf = new OmronConnectProperties();
        //SA1地址：电脑 ip
        conf.setLocalHost(hostIp);
        //DA1地址： 服务器 ip
        conf.setHost(serviceIp);
        //服务器port
        conf.setPort(serviceProt);
        return conf;
    }




    /**
     * 持久化操作
     * @param sdDevicePoint
     * @param data
     */
    public void createDate(SdDevicePoint sdDevicePoint,String data){
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevicePoint.getEqId());
        sdDeviceData.setItemId(sdDevicePoint.getItemId());
        List<SdDeviceData> deviceData = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
        if (deviceData.size() > 0) {
            SdDeviceData oldSdDeviceData = deviceData.get(0);
            oldSdDeviceData.setData(getReadDataByStatusType(data,sdDevicePoint));
            oldSdDeviceData.setUpdateTime(new Date());
            sdDeviceDataService.updateSdDeviceData(oldSdDeviceData);
        } else {
            sdDeviceData.setData(getReadDataByStatusType(data,sdDevicePoint));
            sdDeviceData.setCreateTime(new Date());
            sdDeviceDataService.insertSdDeviceData(sdDeviceData);
        }
        //存入数据记录表中
        SdDeviceDataRecord sdDeviceDataRecord = new SdDeviceDataRecord();
        sdDeviceDataRecord.setDeviceId(sdDevicePoint.getEqId());
        sdDeviceDataRecord.setItemId(Long.valueOf(sdDevicePoint.getItemId()));
        sdDeviceDataRecord.setData(data);
        sdDeviceDataRecord.setCreateTime(new Date());
        sdDeviceDataRecordMapper.insertSdDeviceDataRecord(sdDeviceDataRecord);
    }


    /**
     * 设备状态解析
     */
    public String getReadDataByStatusType(String data,SdDevicePoint dataStatus){
        String resultData = "";
        if(data==null || "".equals(data)){
            return resultData;
        }else{
            switch (dataStatus.getDataStatus()){
                case "status":
                    resultData = data;
                    break;
                case "xfsStatus":
                    //手动D502=184;自动D502=377
                    if(184 == Integer.parseInt(data)){
                        resultData = "手动";
                    }else if(377 == Integer.parseInt(data)){
                        resultData = "自动";
                    }
                    break;
                case "xfsbStatus":
                    //停止D512=184;运行D512=377 ;工作D512=569
                    //item:  停止 0;运行 1;工作 2
                    if(184 == Integer.parseInt(data)){
                        resultData = "0";
                    }else if(377 == Integer.parseInt(data)){
                        resultData = "1";
                    } if(569 == Integer.parseInt(data)){
                        resultData = "2";
                     }
                    break;
                case "sbBoolean":
                    //置1启动；置0停止
                    if(Integer.parseInt(data)==1){
                        resultData = "启动";
                    }else{
                        resultData = "停止";
                    }
                    break;
                case "ssBoolean":
                    //关闭D0=0；开启D0=1
                    resultData = Integer.parseInt(data)==0?"关闭":"开启";
                    break;
            }
        }
        return resultData;
    }
}
