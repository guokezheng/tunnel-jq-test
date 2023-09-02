package com.tunnel.deal.mqtt.config;

import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.deal.mqtt.service.HongMengMqttCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.integration.mqtt.event.MqttConnectionFailedEvent;
import org.springframework.integration.mqtt.event.MqttMessageDeliveredEvent;
import org.springframework.integration.mqtt.event.MqttMessageSentEvent;
import org.springframework.integration.mqtt.event.MqttSubscribedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * describe: mqtt监听器
 *
 * @author zs
 * @date 2023/8/15
 */
@Component
public class MqttListener {
    private static final Logger log = LoggerFactory.getLogger(MqttListener.class);

    @Autowired
    private HongMengMqttCommonService hongMengMqttCommonService;

    /**
     * 连接失败的事件通知
     * @param mqttConnectionFailedEvent
     */
    @EventListener(classes = MqttConnectionFailedEvent.class)
    public void listenerAction(MqttConnectionFailedEvent mqttConnectionFailedEvent) {
        log.error("连接失败的事件通知,time="+new Date());
//        //将胡山隧道鸿蒙控制器以及子设备全部设置为离线
//        hongMengMqttCommonService.updateDeviceStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
    }

    /**
     * 已发送的事件通知
     * @param mqttMessageSentEvent
     */
    @EventListener(classes = MqttMessageSentEvent.class)
    public void listenerAction(MqttMessageSentEvent mqttMessageSentEvent) {
        log.info("已发送的事件通知");
    }

    /**
     * 已传输完成的事件通知
     * 1.QOS == 0,发送消息后会即可进行此事件回调，因为不需要等待回执
     * 2.QOS == 1，发送消息后会等待ACK回执，ACK回执后会进行此事件通知
     * 3.QOS == 2，发送消息后会等待PubRECV回执，知道收到PubCOMP后会进行此事件通知
     * @param mqttMessageDeliveredEvent
     */
    @EventListener(classes = MqttMessageDeliveredEvent.class)
    public void listenerAction(MqttMessageDeliveredEvent mqttMessageDeliveredEvent) {
        log.info("已传输完成的事件通知");
    }

    /**
     * 消息订阅的事件通知
     * @param mqttSubscribedEvent
     */
    @EventListener(classes = MqttSubscribedEvent.class)
    public void listenerAction(MqttSubscribedEvent mqttSubscribedEvent) {
        log.error("消息订阅的事件通知,time="+new Date());
//        //将胡山隧道鸿蒙控制器以及子设备全部设置为在线
//        hongMengMqttCommonService.updateDeviceStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
    }
}
