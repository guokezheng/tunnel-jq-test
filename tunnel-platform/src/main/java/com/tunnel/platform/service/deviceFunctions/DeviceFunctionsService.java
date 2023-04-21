package com.tunnel.platform.service.deviceFunctions;

import cn.hutool.system.SystemUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.quartz.task.OmronTask;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.domain.protocol.SdDevicePoint;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdEquipmentStateService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.business.service.protocol.ISdDevicePointService;
import com.tunnel.deal.guidancelamp.protocol.StringUtil;
import com.tunnel.deal.plc.omron.finsTCP.OmronTcpClient;
import com.tunnel.deal.plc.omron.util.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

@Service
public class DeviceFunctionsService {


//    @Value("${omron.service.ip}")
//    private String serviceIp;
//    @Value("${omron.service.prot}")
//    private Integer serviceProt;
//    @Value("${omron.host.ip}")
//    private String hostIp;
    private String serviceIp = "10.7.187.87";
    private Integer serviceProt = 9600;
    private String hostIp = "127.0.0.1";

    @Autowired
    private ISdEquipmentStateService sdEquipmentStateService;
    @Autowired
    private ISdDevicePointService devicePointService;
    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private ISdDeviceDataService sdDeviceDataService;
    @Autowired
    private ISdOperationLogService sdOperationLogService;





    /**
     * 设备控制指令接口
     * @param type
     * @param eqId
     * @param data
     */
    public boolean deviceControlByParam(String type, String eqId, String data){
        boolean resultData = false;
        switch (type){
            case "omron":
                OmronTcpClient omronTcpClient = OmronTask.omronTcpClient;
//                if(omronTcpClient == null){
//                    try {
//                        //欧姆龙  设备新消息配置   后期优化
//                        OmronConnectProperties conf = new OmronConnectProperties();
//                        //SA1地址：电脑 ip
//                        conf.setLocalHost(hostIp);
//                        //DA1地址： 服务器 ip
//                        conf.setHost(serviceIp);
//                        //服务器port
//                        conf.setPort(serviceProt);
//
//                        omronTcpClient = new OmronTcpClient(conf);
//                        //初始化链接     服务器地址
//                        omronTcpClient.init(serviceIp,serviceProt);
//                        //获取ChannelFuture
//                        ChannelFuture channelFuture = omronTcpClient.channelFuture;
//                        //推送握手协议
//                        omronTcpClient.send(omronTcpClient.successCallback(channelFuture).array());
//                    } catch (Exception e) {
//                        omronTcpClient = null;
//                        //链接异常超时，记录当前日志。(后期优化记录)
//                    }
//                }
                if(omronTcpClient!=null){
                    SdDevicePoint sdDevicePoint = new SdDevicePoint();
                    sdDevicePoint.setEqId(eqId);
                    sdDevicePoint.setIsReserved(2L);
                    List<SdDevicePoint> sdDevicePointList =  devicePointService.selectSdDevicePointList(sdDevicePoint);
                    resultData = getDataByDataStatusAndEqType(data,sdDevicePointList,omronTcpClient);
                }
                break;
        }
        if(resultData){
            SdDevices device = sdDevicesService.selectSdDevicesById(eqId);
            //添加操作日志
            SdOperationLog sdOperationLog = new SdOperationLog();
            sdOperationLog.setEqTypeId(device.getEqType());
            sdOperationLog.setTunnelId(device.getEqTunnelId());
            sdOperationLog.setEqId(device.getEqId());
            sdOperationLog.setOperationState(data);
            //控制方式   0：手动 1：时间控制 2：光强控制
            sdOperationLog.setControlType("0");
            sdOperationLog.setCreateTime(new Date());
            try {
                sdOperationLog.setOperIp(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            //操作是否成功 0 不成功；1成功
            sdOperationLog.setState("1");
            // 获取设备之前状态
            SdDeviceData sdDeviceData = new SdDeviceData();
            sdDeviceData.setDeviceId(eqId);
            sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.XIAO_FANG_SHUI_BENG.getCode()));
            List<SdDeviceData> sdDeviceDataList = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
            if (null != sdDeviceDataList && sdDeviceDataList.size() > 0) {
                sdOperationLog.setBeforeState(sdDeviceDataList.get(0).getData());
            }
            sdOperationLogService.insertSdOperationLog(sdOperationLog);
//            //更新设备data状态
//            sdDeviceData.setData(data);
//            sdDeviceData.setUpdateTime(new Date());
//            sdDeviceDataService.updateSdDeviceData(sdDeviceData);
        }else{
            SdDevices device = sdDevicesService.selectSdDevicesById(eqId);
            //添加操作日志
            SdOperationLog sdOperationLog = new SdOperationLog();
            sdOperationLog.setEqTypeId(device.getEqType());
            sdOperationLog.setTunnelId(device.getEqTunnelId());
            sdOperationLog.setEqId(device.getEqId());
            sdOperationLog.setOperationState(data);
            //控制方式   0：手动 1：时间控制 2：光强控制
            sdOperationLog.setControlType("0");
            sdOperationLog.setCreateTime(new Date());
            try {
                sdOperationLog.setOperIp(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            //操作是否成功 0 不成功；1成功
            sdOperationLog.setState("0");
            // 获取设备之前状态
            SdDeviceData sdDeviceData = new SdDeviceData();
            sdDeviceData.setDeviceId(eqId);
            sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.XIAO_FANG_SHUI_BENG.getCode()));
            List<SdDeviceData> sdDeviceDataList = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
            if (null != sdDeviceDataList && sdDeviceDataList.size() > 0) {
                sdOperationLog.setBeforeState(sdDeviceDataList.get(0).getData());
            }
            sdOperationLogService.insertSdOperationLog(sdOperationLog);
        }
        return resultData;
    }



    public boolean getDataByDataStatusAndEqType(String data,List<SdDevicePoint> sdDevicePointList,OmronTcpClient client){
        boolean resultStatus = false;
        if(sdDevicePointList.size() == 1){
            //根据statusType 解析当前推送数据
            SdDevicePoint sdDevicePointInfo = sdDevicePointList.get(0);
            switch (sdDevicePointInfo.getDataStatus()){
                case "sbBoolean":
                    //data 消防栓数据转化
                    data = getStatusByData(data, Math.toIntExact(sdDevicePointInfo.getStateId()));
                    //二进制解析  将数据转化为byte[]
                    if("bin".equals(sdDevicePointInfo.getDataType())){
                        //获取对应数据   解析为对应状态值
                        byte[] dataInfo = ByteUtil.getBytes(Integer.parseInt(data));

                        boolean isBit = sdDevicePointInfo.getAddress().contains(".")?true:false;

                        resultStatus = client.writeBin(dataInfo,isBit,sdDevicePointInfo.getAddress(),client);
                    }
                    break;
                //预留后期增加配置类型
            }
        }else{
            for (SdDevicePoint sdDevicePointInfo :sdDevicePointList) {
                //根据statusType 解析当前推送数据
                switch (sdDevicePointInfo.getDataStatus()){
                    //预留后期增加配置类型
                    default:
                        //控制状态  与 点位状态一致  以当前点位数据推送
                        if(data.equals(sdDevicePointInfo.getDataStatus())){
                            //获取对应数据   解析为对应状态值
                            byte[] dataInfo = ByteUtil.getBytes(1);

                            boolean isBit = sdDevicePointInfo.getAddress().contains(".")?true:false;

                            resultStatus = client.writeBin(dataInfo,isBit,sdDevicePointInfo.getAddress(),client);
                        }
                        break;
                }
            }
        }
        return resultStatus;
    }


    /**
     * 控制状态解析
     * @param data
     * @param statusId
     * @return
     */
    public String getStatusByData(String data, int statusId){
        String resultData = "";
        //消防水泵
        if(DevicesTypeEnum.SHUI_BENG.getCode() == statusId){
            //置1启动；置0停止
            switch (data){
                case "1":
                    //开启
                    resultData = "1";
                    break;
                case "2":
                    //关闭
                    resultData = "0";
                    break;
            }
        }
        return resultData;
    }
}
