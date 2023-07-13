package com.tunnel.deal.mqtt.config;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.deal.mqtt.config.MqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * describe: MQTT控制类--测试
 *
 * @author zs
 * @date 2023/3/28
 */
//@RestController
//@RequestMapping("/mqtt")
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
    public String sendMqtt(String sendData,String topic){
        //System.out.println(sendData+"   "+topic);
        //System.out.println("进入inbound发送："+sendData);
        mqttGateway.sendToMqtt(topic,(String) sendData);
        return "Test is OK";
    }

    @RequestMapping("/sendTopic")
    public String testMqtt(String externalDeviceId){
        //System.out.println(sendData+"   "+topic);
        //System.out.println("进入inbound发送："+sendData);
        String externalDeviceId2 = "JQ-WeiFang-JiuLongYu-JJL-JF-100-1-external";
        JSONObject sendData = new JSONObject();
        sendData.put("sn",externalDeviceId2);
//        sendData.put("sn",externalDeviceId);
        sendData.put("fanRunStatus","00");
//        externalDeviceId2 = externalDeviceId2.replace("+","/+");
        mqttGateway.sendToMqtt("rhy/iot/receive/fan/alterRunStatus/{"+externalDeviceId2+"}",sendData.toString());
        return "Test is OK";
    }

    @RequestMapping("/testMqttTopic")
    public String testMqttTopic(@RequestBody JSONObject jsonObject){
        String topic = String.valueOf(jsonObject.get("topic"));
        JSONObject sendData = jsonObject.getJSONObject("sendData");

        mqttGateway.sendToMqtt(topic,sendData.toString());
        return "Test is OK";
    }


}
