package com.tunnel.deal.mqtt.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.SdDevices;

import java.util.Map;

/**
 * describe: 鸿蒙控制器MQTT通信业务类
 *
 * @author zs
 * @date 2023/5/25
 */
public interface HongMengMqttService {

    /**
     * 设备控制方法
     * @param map
     * @param sdDevices
     */
    AjaxResult deviceControl(Map<String, Object> map, SdDevices sdDevices);


    /**
     * 查询设备运行状态或业务属性
     * @param sdDevices
     */
   void queryDeviceData(SdDevices sdDevices);


    /**
     * 处理上报的数据
     * @param topic 主题
     * @param sdDevices 设备信息
     * @param payload 消息
     */
   void handleReceiveData(String topic,SdDevices sdDevices,String payload);

}
