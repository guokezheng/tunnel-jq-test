package com.tunnel.deal.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * describe: MQTT控制类--测试
 *
 * @author zs
 * @date 2023/3/28
 */
@RestController
@RequestMapping("/mqtt")
public class MessageController {

    @Autowired
    MqttGateway mqttGateway;

    @RequestMapping("/sendMqttMessage")
    public String sendMqttMessage(String message, String topic) {
        mqttGateway.sendToMqtt(message, topic);
        return "ok";
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello!";
    }

    @RequestMapping("/sendMqtt")
    public String sendMqtt(String  sendData){
        System.out.println(sendData);
        System.out.println("进入sendMqtt-------"+sendData);
        mqttGateway.sendToMqtt("topic01",(String) sendData);
        return "Test is OK";
    }



    @RequestMapping("/sendMqttTopic")
    public String sendMqtt(String  sendData,String topic){
        //System.out.println(sendData+"   "+topic);
        //System.out.println("进入inbound发送："+sendData);
        mqttGateway.sendToMqtt(topic,(String) sendData);
        return "Test is OK";
    }


}
