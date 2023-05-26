package com.tunnel.deal.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * describe: 入站通道配置
 *
 * @author zs
 * @date 2023/5/25
 */
@Configuration
@IntegrationComponentScan
public class MqttInboundConfiguration {


    @Autowired
    private MqttProperties mqttProperties;

    @Autowired
    private MqttPahoClientFactory mqttClientFactory;


    /**
     * 配置client,监听的topic
     * @return
     */
    @Bean
    public MessageProducer inbound() {
        String[] inboundTopics = mqttProperties.getTopics().split(",");
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                mqttProperties.getClientInId(),mqttClientFactory, inboundTopics);
        //对inboundTopics主题进行监听
        adapter.setCompletionTimeout(5000);
        // todo
        adapter.setQos(1);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }


    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }


    /**
     * 通过通道获取数据
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
//				System.out.println("message:"+message);
            System.out.println("----------------------");
            System.out.println("message:"+message.getPayload());
            System.out.println("PacketId:"+message.getHeaders().getId());
            System.out.println("Qos:"+message.getHeaders().get(MqttHeaders.QOS));

            String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
            System.out.println("topic:"+topic);
        };
    }


}
