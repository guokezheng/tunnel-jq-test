package com.tunnel.deal.tcp.plc.omron;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.datacenter.domain.enumeration.DevicePointControlTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DeviceStateTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.OperationLogEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.protocol.SdDevicePointPlc;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.protocol.ISdDevicePointPlcService;
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.mqtt.config.MqttInboundConfiguration;
import com.tunnel.deal.tcp.client.config.ChannelKey;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralBean;
import com.tunnel.deal.tcp.client.netty.TcpNettySocketClient;
import com.tunnel.deal.tcp.plc.omron.fins.FinsCmdResolver;
import com.tunnel.deal.tcp.plc.omron.task.OmronFinsTask;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

import static com.tunnel.deal.tcp.plc.omron.task.OmronFinsTask.commandLock;

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
    private ISdDevicePointPlcService devicePointPlcService;

    @Autowired
    private FinsCmd finsCmd;

    @Resource(name = "scheduledExecutorService")
    private ScheduledExecutorService executor;

    @Value("${plc.server}")
    private String plcServer;


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
        return AjaxResult.success(1);
        //return AjaxResult.success("下发成功");
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
        boolean isopen = commonControlService.queryAnalogControlConfig();
        Integer controlState = 0;
        if (isopen) {
            //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
            controlState = commonControlService.analogControl(map,sdDevices);
            return controlState;
        }
        Map<String, Object> map1 = new HashMap<>();
        map1.put("sdDevices",sdDevices);
        map1.put("state",state);
        map1.put("type","2");
        OmronFinsControlProcession.queue.add(map1);
        //控制设备之前获取设备状态
        String beforeState = commonControlService.selectBeforeState(sdDevices);
        //操作日志
        commonControlService.addOperationLog(map,sdDevices,beforeState,1);
        return 1;
        /*Integer controlState = 0;

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
        return controlState;*/
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
        Map<String, Object> map = new HashMap<>();
        map.put("sdDevices",sdDevices);
        map.put("state",state);
        map.put("type","2");
        OmronFinsControlProcession.queue.add(map);
       /* //控制指令下发，将下发指令锁置为false
        OmronFinsTask.commandLock = false;
        System.out.println("关闭指令锁：commandLock="+commandLock);

        //通过控制父设备PLC控制设备
        String fEqId = sdDevices.getfEqId();
        if(fEqId == null || "".equals(fEqId)){
            return AjaxResult.error("未配置设备关联的父设备");
        }
        SdDevices fDevice = sdDevicesService.selectSdDevicesById(fEqId);
        String ip = fDevice.getIp();
        String port = fDevice.getPort();
        Integer portNum = Integer.valueOf(port);
        //先关闭通道，解除查询指令占用的通道
        Channel cmdChannel = TcpNettySocketClient.channels.get(ChannelKey.getChannelKey(ip,Integer.valueOf(port)));
        if(cmdChannel != null){
            cmdChannel.close();
        }



        //点位类型：控制点位
        Long pointType = DevicePointControlTypeEnum.control_enable.getCode();
        //数据状态
        String runState = DeviceStateTypeEnum.data_status.getCode();
        String eqId = sdDevices.getEqId();

//        //设备信息
//        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(deviceId);

        //查询设备点位
        SdDevicePointPlc devicePointPlc = new SdDevicePointPlc();
        devicePointPlc.setEqId(eqId);
        devicePointPlc.setIsReserved(pointType);
        List<SdDevicePointPlc> devicePointList = devicePointPlcService.selectSdDevicePointPlcList(devicePointPlc);
        if(devicePointList == null || devicePointList.size() == 0){
            return AjaxResult.error("未配置设备点位");
        }
        devicePointPlc = devicePointList.get(0);

        String pointConfig = devicePointPlc.getPointConfig();
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



        //默认操作失败
        AjaxResult ajaxResult = AjaxResult.error();


        //源地址
        String sourceAddress = plcServer;
        //目的地址
        String destinationAddress = ip;

        //获取连接通道
        TcpNettySocketClient.getInstance().connect(ip,portNum);

        int count = 50;
        int i = 0;
        boolean flag = false;
        long shakeTime = 0L;
        long controlTime = 0L;
        //发送指令
        while(i < count){
            Channel channel = TcpNettySocketClient.channels.get(ChannelKey.getChannelKey(ip,portNum));
            if (channel != null && channel.isActive()) {
                if(!flag){
                    //建立连接后，发送握手指令,只发送一次
                    finsCmd.sendHandshakeCommand(OmronFinsTask.deviceMap,fEqId,sourceAddress);
                    shakeTime = System.currentTimeMillis();
                    System.out.println("ip="+ip+",握手指令发送时间："+ shakeTime);
                    flag = true;
                }
                //判断握手是否成功
                Object isHandshakeObj = OmronFinsTask.deviceMap.get(fEqId).get("handshake");
                Boolean isHandshake = Boolean.valueOf(String.valueOf(isHandshakeObj));
                if(isHandshake){
                    //握手成功
                    //发送指令
                    sleep(100);
                    ajaxResult = finsCmd.sendControlCommand(OmronFinsTask.deviceMap,fEqId,sourceAddress,destinationAddress,
                            area,address,bitAddress,writeLength,controlState);
                    controlTime = System.currentTimeMillis();
                    System.out.println("ip="+ip+",控制指令发送时间："+ controlTime);
                    break;
                }
            }
            i++;
            sleep(20);
        }

        System.out.println("ip="+ip+",控制指令发送时间-握手指令发送时间="+ (controlTime-shakeTime));
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


        sleep(50);
        OmronFinsTask.commandLock = true;
        System.out.println("打开指令锁：commandLock="+commandLock);*/
        return AjaxResult.success();
    }

    public void readCommitData(String fEqId, String eqId){
        //查询设备点位
        SdDevicePointPlc devicePointPlc = new SdDevicePointPlc();
        devicePointPlc.setEqId(eqId);
        devicePointPlc.setIsReserved(DevicePointControlTypeEnum.only_read.getCode());
        List<SdDevicePointPlc> devicePointList = devicePointPlcService.selectSdDevicePointPlcList(devicePointPlc);
        if(devicePointList == null || devicePointList.size() == 0){
            return;
        }
        devicePointPlc = devicePointList.get(0);

        String pointConfig = devicePointPlc.getPointConfig();
        JSONObject jsonConfig = JSONObject.parseObject(pointConfig);
        String area = jsonConfig.getString("area");
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
