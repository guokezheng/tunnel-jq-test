package com.tunnel.deal.mqtt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.digitalmodel.SdRadarDevice;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.digitalmodel.impl.RadarEventServiceImpl;
import com.tunnel.deal.enums.DeviceProtocolCodeEnum;
import com.tunnel.deal.mqtt.config.MqttGateway;
import com.tunnel.deal.mqtt.service.HongMengMqttCommonService;
import com.tunnel.deal.mqtt.task.HongMengMqttTask;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralService;
import com.zc.common.constant.RedisKeyConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * describe: 鸿蒙控制器MQTT对接公共方法实现类
 *
 * @author zs
 * @date 2023/5/25
 */
@Service
public class HongMengMqttCommonServiceImpl implements HongMengMqttCommonService
{

    @Autowired
    MqttGateway mqttGateway;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private TcpClientGeneralService tcpClientGeneralService;

    @Autowired
    private RadarEventServiceImpl radarEventServiceImpl;

    @Autowired
    @Qualifier("kafkaOneTemplate")
    private KafkaTemplate<String, String> kafkaOneTemplate;

    /**
     * Redis缓存工具类
     **/
    private RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    private static final Logger log = LoggerFactory.getLogger(HongMengMqttCommonServiceImpl.class);

    /**
     * 获取actionId公共方法
     *
     * @return
     */
    @Override
    public String getActionId() {
        long time = System.currentTimeMillis();
        return String.valueOf(time);
    }

    /**
     * 查询设备运行状态或业务属性
     *
     * @param sdDevices
     * @param topicPrefix
     */
    @Override
    public void queryDeviceData(SdDevices sdDevices,String topicPrefix) {
        String ctrlSn = sdDevices.getFEqId();
        String externalDeviceId = sdDevices.getExternalDeviceId();
        JSONObject jsonObject = new JSONObject();
        //映射设备Id
        jsonObject.put("sn",externalDeviceId);
        //与回复指令对应，使用时间戳
        jsonObject.put("actionId", getActionId());
        mqttGateway.sendToMqtt(topicPrefix+ctrlSn,jsonObject.toJSONString());
    }

    /**
     * 处理设备状态上报数据
     *
     * @param sdDevices 设备信息
     * @param payload   消息
     */
    @Override
    public void handleDeviceStateReceiveData(SdDevices sdDevices, String payload) {
        String deviceId = sdDevices.getEqId();

        //{
        //    "sn": "1",
        //    "devStatus": "01",
        //    "isFault": "00",
        //    "errCodeList": ["1A"]
        //}

        //     devStatus   00：离线，01：在线
//      isFault  00：无故障，01：故障

        JSONObject jsonObject = JSONObject.parseObject(payload);
        String devStatus = String.valueOf(jsonObject.get("devStatus"));
        String isFault = String.valueOf(jsonObject.get("isFault"));
        String errCodeList = String.valueOf(jsonObject.get("errCodeList"));
        if("00".equals(isFault)){
            if("00".equals(devStatus)){
                //离线
                sdDevicesService.updateOfflineStatus(deviceId,false);
                sdDevicesService.updateFDeviceStatusByEqId(deviceId, DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
            }
            if("01".equals(devStatus)){
                //在线
                sdDevicesService.updateOnlineStatus(deviceId,false);
                sdDevicesService.updateFDeviceStatusByEqId(deviceId, DevicesStatusEnum.DEVICE_ON_LINE.getCode());
            }
        }
        if("01".equals(isFault)){
            //故障
            sdDevicesService.updateFaultStatus(deviceId,false);
            log.error("鸿蒙MQTT设备状态上报，设备ID="+deviceId+",设备故障码="+errCodeList);
        }
    }

    /**
     * 处理指令执行情况上报数据
     *
     * @param sdDevices 设备信息
     * @param payload   消息
     */
    @Override
    public void handleExecStateReceiveData(SdDevices sdDevices, String payload,String topic) {
        String deviceId = sdDevices.getEqId();

//        {
//            "sn": "1",
//                "actionId": "1",
//                "error": "00",
//                "timeStamp": ""
//        }
//        error
//        00：无错误
//        01：指令执行错误
//        02：命令设备与控制器挂在设备不符合
//        03：错误的参数
        JSONObject jsonObject = JSONObject.parseObject(payload);
        String actionId = String.valueOf(jsonObject.get("actionId"));
        String error = String.valueOf(jsonObject.get("error"));
        String timeStamp = String.valueOf(jsonObject.get("timeStamp"));
        //todo 如何处理
        if(!"00".equals(error)){
            log.error("鸿蒙MQTT指令上报，设备ID="+deviceId+",设备故障码="+error);
        }else{

            //TODO 死循环，厂家流程问题
          // queryDeviceData(sdDevices,topic);
        }
    }

    /**
     * 修改设备状态
     * @param status 设备状态
     */
    @Override
    public void updateDeviceStatus(String status) {
        String protocolCode = DeviceProtocolCodeEnum.HONGMENG_MQTT_PROTOCOL_CODE.getCode();
        List<SdDevices> list = tcpClientGeneralService.getDevicesList(protocolCode, null);
        List<String> idList = list.stream().map(SdDevices::getEqId).collect(Collectors.toList());
        sdDevicesService.updateDeviceStatusBatch(idList,status);
    }


    /**
     * 设备掉线检测数据缓存
     * @param deviceId 设备ID
     */
    @Override
    public void setRedisCacheDeviceStatus(String deviceId){
        //定时任务5秒钟请求一次状态，如果获取返回信息，判定设备离线

        //在线时间检测控制时间，定时任务传入方便修改
        Integer expireTime = HongMengMqttTask.onlineSecondInterval;
        if(expireTime == null){
            //默认调整10分钟
            expireTime = 600;
        }
        redisCache.setCacheObject(RedisKeyConstants.HONG_MENG_MQTT_STATUS + ":" + deviceId,"online",expireTime, TimeUnit.SECONDS);
    }

}
