package com.tunnel.deal.tcp.plc.omron;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.datacenter.domain.enumeration.DevicePointControlTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DeviceStateTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.OperationLogEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.protocol.SdDevicePoint;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.protocol.ISdDevicePointService;
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.tcp.client.config.ChannelKey;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralBean;
import com.tunnel.deal.tcp.client.netty.TcpNettySocketClient;
import com.tunnel.deal.tcp.plc.omron.fins.FinsCmdResolver;
import com.tunnel.deal.tcp.plc.omron.task.OmronFinsTask;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;

/**
 * describe: 欧姆龙fins PLC控制
 *
 * @author zs
 * @date 2023/8/17
 */
@Component
public class OmronFinsControl implements GeneralControlBean, TcpClientGeneralBean {

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private CommonControlService commonControlService;

    @Autowired
    private ISdDevicePointService devicePointService;

    @Autowired
    private FinsCmd finsCmd;

    @Resource(name = "scheduledExecutorService")
    private ScheduledExecutorService executor;


    /**
     * 设备控制--工作台单个设备控制
     *
     * @param map
     * @param sdDevices
     * @return
     */
    @Override
    public AjaxResult control(Map<String, Object> map, SdDevices sdDevices) {
        //设备ID
        String devId = Optional.ofNullable(map.get("devId")).orElse("").toString();
        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();

        Integer controlState = 0;

        boolean isopen = commonControlService.queryAnalogControlConfig();
        if (isopen) {
            //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
            controlState = commonControlService.analogControl(map,sdDevices);
            return AjaxResult.success(controlState);
        }

        //控制设备之前获取设备状态
        String beforeState = commonControlService.selectBeforeState(sdDevices);


        AjaxResult ajaxResult = control(sdDevices,state);
        Integer code = Integer.valueOf(String.valueOf(ajaxResult.get("code")));
        if( code == HttpStatus.SUCCESS){
            controlState = Integer.valueOf(OperationLogEnum.STATE_SUCCESS.getCode());
        }
        //操作日志
        commonControlService.addOperationLog(map,sdDevices,beforeState,controlState);
        return AjaxResult.success(controlState);
    }

    /**
     * 设备控制+模拟控制--其他模块调用的统一控制方法
     *
     * @param map
     * @return
     */
    @Override
    public Integer controlDevices(Map<String, Object> map) {
        //设备ID
        String devId = Optional.ofNullable(map.get("devId")).orElse("").toString();
        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);

        Integer controlState = 0;

        boolean isopen = commonControlService.queryAnalogControlConfig();
        if (isopen) {
            //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
            controlState = commonControlService.analogControl(map,sdDevices);
            return controlState;
        }

        //控制设备之前获取设备状态
        String beforeState = commonControlService.selectBeforeState(sdDevices);


        AjaxResult ajaxResult = control(sdDevices,state);
        Integer code = Integer.valueOf(String.valueOf(ajaxResult.get("code")));
        if( code == HttpStatus.SUCCESS){
            controlState = Integer.valueOf(OperationLogEnum.STATE_SUCCESS.getCode());
        }
        //操作日志
        commonControlService.addOperationLog(map,sdDevices,beforeState,controlState);
        return controlState;
    }

    /**
     * 解析读取的数据
     *
     * @param ip       网关设备IP
     * @param deviceId 网关设备ID
     * @param msg      读取的数据
     */
    @Override
    public void handleReadData(String ip, String deviceId, String msg) {

//        System.out.println(msg);
        JSONObject jsonObject = FinsCmdResolver.commandParse(deviceId,msg);
        Boolean flag = jsonObject.getBoolean("flag");
        String value = jsonObject.getString("value");
        if(value == null || "".equals(value)){
            return;
        }
        List<String> idList = new ArrayList<>();
        idList.add(deviceId);

        //只读点位
        Long pointType = DevicePointControlTypeEnum.only_read.getCode();


       SdDevices sdDevices = sdDevicesService.selectSdDevicesById(deviceId);
       String port = sdDevices.getPort();
        if(flag){
            //读取信息成功，关闭通道
            Channel channel = TcpNettySocketClient.channels.get(ChannelKey.getChannelKey(ip,Integer.valueOf(port)));
            channel.close();
            //握手信息置为false
            OmronFinsTask.deviceMap.get(deviceId).put("handshake",false);
        }

    }


    /**
     * 控制设备（取协议配置信息，下发命令）
     * @param sdDevices
     * @param state
     * @return
     */
    public AjaxResult control(SdDevices sdDevices, String state) {
        //点位类型：控制点位
        Long pointType = DevicePointControlTypeEnum.control_enable.getCode();
        //数据状态
        String runState = DeviceStateTypeEnum.data_status.getCode();
        String eqId = sdDevices.getEqId();

//        //设备信息
//        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(deviceId);

        //查询设备点位
        SdDevicePoint devicePoint = new SdDevicePoint();
        devicePoint.setEqId(eqId);
        devicePoint.setIsReserved(pointType);
        List<SdDevicePoint> devicePointList = devicePointService.selectSdDevicePointList(devicePoint);
        if(devicePointList == null || devicePointList.size() == 0){
            return AjaxResult.error("未配置设备点位");
        }
        devicePoint = devicePointList.get(0);

        String pointConfig = devicePoint.getPointConfig();
        JSONObject jsonConfig = JSONObject.parseObject(pointConfig);
        //点位状态
        String stateStr = jsonConfig.getString("stateConfig");
        JSONArray jsonArray = JSONArray.parseArray(stateStr);
        //状态匹配
        JSONObject stateJson = new JSONObject();
        for(Object obj : jsonArray){
            JSONObject jsonObject = (JSONObject) obj;
            String stateConfig = jsonObject.getString("state");
            if(state.equals(stateConfig)){
                stateJson = jsonObject;
                break;
            }
        }

        String controlState = stateJson.getString("value");
        if(controlState == null){
            return AjaxResult.error("设备点位配置不完整");
        }
        //源地址
        String sourceAddress = jsonConfig.getString("sourceAddress");
        //目的地址
        String destinationAddress = jsonConfig.getString("destinationAddress");
        //存储分区代码
        String area = jsonConfig.getString("area");
        //写入地址
        String address = jsonConfig.getString("address");
        String bitAddress = jsonConfig.getString("bitAddress");
        String writeLength = jsonConfig.getString("writeLength");
//        String value = jsonConfig.getString("value");


//        if(writeLength == null){
//            return AjaxResult.error("设备点位配置不完整");
//        }

        //通过控制父设备PLC控制设备
        String fEqId = sdDevices.getfEqId();
        if(fEqId == null || "".equals(fEqId)){
            return AjaxResult.error("未配置设备关联的父设备");
        }

        //默认操作失败
        AjaxResult ajaxResult = AjaxResult.success(0);
        SdDevices fDevice = sdDevicesService.selectSdDevicesById(fEqId);
        String ip = fDevice.getIp();
        String port = fDevice.getPort();
        Integer portNum = Integer.valueOf(port);
        //获取连接通道
        TcpNettySocketClient.getInstance().connect(ip,portNum);

        int count = 50;
        int i = 0;
        boolean flag = false;
        //发送指令
        while(i < count){
            Channel channel = TcpNettySocketClient.channels.get(ChannelKey.getChannelKey(ip,portNum));
            if (channel != null && channel.isActive()) {
                if(!flag){
                    //建立连接后，发送握手指令,只发送一次
                    finsCmd.sendHandshakeCommand(OmronFinsTask.deviceMap,fEqId,sourceAddress);
                    flag = true;
                }
                //判断握手是否成功
                Object isHandshakeObj = OmronFinsTask.deviceMap.get(fEqId).get("handshake");
                Boolean isHandshake = Boolean.valueOf(String.valueOf(isHandshakeObj));
                if(isHandshake){
                    //握手成功
                    //发送指令
                    ajaxResult = finsCmd.sendControlCommand(OmronFinsTask.deviceMap,fEqId,sourceAddress,destinationAddress,
                            area,address,bitAddress,writeLength,controlState);
                    break;
                }
            }
            i++;
            sleep(20);
        }


//        //判断握手是否成功
//        Map itemMap = OmronFinsTask.deviceMap.get(fEqId);
//        if(itemMap == null){
//            return AjaxResult.error("父设备配置信息不完整");
//        }
//        Object isHandshakeObj = itemMap.get("handshake");
//        Boolean isHandshake = Boolean.valueOf(String.valueOf(isHandshakeObj));
//        if(!isHandshake){
//            //未握手成功，发送握手指令
//            finsCmd.sendHandshakeCommand(OmronFinsTask.deviceMap,fEqId,sourceAddress);
//            try {
//                //握手命令发送后等待500毫秒
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }


        return ajaxResult;
    }


    /**
     * 线程休眠固定时间
     * @param ms 毫秒
     */
    public void sleep(int ms){
        //间隔固定时间（毫秒）发送指令，避免同一个设备连续多次发送指令无回复
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
