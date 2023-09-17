package com.tunnel.deal.mqtt.service;

import com.tunnel.business.domain.dataInfo.SdDevices;

/**
 * describe: 鸿蒙控制器MQTT对接公共方法
 *
 * @author zs
 * @date 2023/5/26
 */
public interface HongMengMqttCommonService {

    /**
     * 获取actionId公共方法
     * @return
     */
    String getActionId();

    /**
     * 查询设备运行状态或业务属性
     * @param sdDevices
     * @param topicPrefix
     */
    void queryDeviceData(SdDevices sdDevices,String topicPrefix);


    /**
     * 处理设备状态上报数据
     * @param sdDevices 设备信息
     * @param payload    消息
     */
    void handleDeviceStateReceiveData(SdDevices sdDevices, String payload);


    /**
     * 处理指令执行情况上报数据
     * @param sdDevices 设备信息
     * @param payload    消息
     */
     void handleExecStateReceiveData(SdDevices sdDevices, String payload,String topic);

    /**
     * 修改设备状态
     * @param status 设备状态
     */
    void updateDeviceStatus(String status);


    /**
     * 设备掉线检测数据缓存
     * @param deviceId 设备ID
     */
    void setRedisCacheDeviceStatus(String deviceId);

}
