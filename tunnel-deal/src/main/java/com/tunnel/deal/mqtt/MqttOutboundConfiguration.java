package com.tunnel.deal.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;


/**
 * describe: 出站通道配置
 *
 * @author zs
 * @date 2023/3/28
 */
@Configuration
@IntegrationComponentScan
public class MqttOutboundConfiguration {


    @Autowired
    private MqttProperties mqttProperties;
    @Bean
    public MqttConnectOptions getMqttConnectOptions(){
        String[] array = mqttProperties.getUrl().split(",");
        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);

        //qoslevel todo
        mqttConnectOptions.setConnectionTimeout(Integer.valueOf(mqttProperties.getTimeout()));
        mqttConnectOptions.setKeepAliveInterval(Integer.valueOf(mqttProperties.getKeepalive()));
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setUserName(mqttProperties.getUsername());
        mqttConnectOptions.setPassword(mqttProperties.getPassword().toCharArray());
        mqttConnectOptions.setServerURIs(array);
        return mqttConnectOptions;
    }
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
//        MqttPahoMessageHandler messageHandler =  new MqttPahoMessageHandler(mqttProperties.getClientId(), mqttClientFactory());
        MqttPahoMessageHandler messageHandler =  new MqttPahoMessageHandler(mqttProperties.getClientOutId(), mqttClientFactory());
        messageHandler.setAsync(true);
//        messageHandler.setDefaultTopic(defaultTopic);
        return messageHandler;
    }
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }
}
