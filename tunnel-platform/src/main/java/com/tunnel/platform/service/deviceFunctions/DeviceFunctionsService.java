package com.tunnel.platform.service.deviceFunctions;

import com.ruoyi.quartz.task.OmronTask;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdEquipmentState;
import com.tunnel.business.domain.protocol.SdDevicePoint;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdEquipmentStateService;
import com.tunnel.business.service.protocol.ISdDevicePointService;
import com.tunnel.deal.plc.omron.OmronConnectProperties;
import com.tunnel.deal.plc.omron.finsTCP.OmronTcpClient;
import com.tunnel.deal.plc.omron.util.ByteUtil;
import io.netty.channel.ChannelFuture;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    private ISdDevicesService sdDevicesService;
    @Autowired
    private ISdDevicePointService devicePointService;

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
        return resultData;
    }



    public boolean getDataByDataStatusAndEqType(String data,List<SdDevicePoint> sdDevicePointList,OmronTcpClient client){
        boolean resultStatus = false;
        if(sdDevicePointList.size() == 1){
            //根据statusType 解析当前推送数据
            SdDevicePoint sdDevicePointInfo = sdDevicePointList.get(0);
            switch (sdDevicePointInfo.getDataStatus()){
                case "sbBoolean":
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
}
