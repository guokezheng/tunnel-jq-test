package com.tunnel.deal.mqtt.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.digitalmodel.SdRadarDevice;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.digitalmodel.impl.RadarEventServiceImpl;
import com.tunnel.deal.mqtt.service.HongMengMqttService;
import com.tunnel.deal.mqtt.service.impl.HongMengMqttCommonServiceImpl;
import com.tunnel.deal.mqtt.strategy.HongMengMqttStrategyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * describe: 入站通道配置
 *
 * @author zs
 * @date 2023/5/25
 */
@Configuration
@IntegrationComponentScan
public class MqttInboundConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MqttInboundConfiguration.class);


    @Autowired
    private MqttProperties mqttProperties;

    @Autowired
    private MqttPahoClientFactory mqttClientFactory;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private HongMengMqttStrategyFactory hongMengMqttStrategyFactory;

    @Autowired
    private ISdDevicesService devicesService;


    @Autowired
    private RadarEventServiceImpl radarEventServiceImpl;


    @Autowired
    @Qualifier("kafkaOneTemplate")
    private KafkaTemplate<String, String> kafkaOneTemplate;


    public Logger mcaLogger = LoggerFactory.getLogger("mca");

    //定义先进先出队列
    private static Queue<SdDevices> queue = new LinkedBlockingQueue<>();


    MqttInboundConfiguration(){
        mcaLogger.info("HongMengMqttCommonServiceImpl构造函数进来了");
        mqttHandler mqttHandler = new mqttHandler();
        Thread thread = new Thread(mqttHandler);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    class mqttHandler implements Runnable{
        @Override
        public void run() {
            while (true) {
                if (queue.isEmpty()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }
                //lock.lock();
                SdDevices data = queue.poll();
                mcaLogger.info("万集队列总数：{}，当前数据：{}",queue.size(),data.toString());
                //lock.unlock();
                pushWanJi(data);
                mcaLogger.info("万集队列数据已执行完毕");
            }
        }

        public void pushWanJi(SdDevices sdDevices)
        {
            List<SdDevices> sdDevicesList =  new ArrayList<>();
            sdDevicesList.add(sdDevices);
            List<SdRadarDevice> deviceRadar = radarEventServiceImpl.getDeviceRadar(sdDevicesList);
            kafkaOneTemplate.send("baseDeviceStatus", JSON.toJSONString(deviceRadar));
        }
    }


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
//            System.out.println("----------------------");
//            System.out.println("message:"+message.getPayload());
//            System.out.println("PacketId:"+message.getHeaders().getId());
//            System.out.println("Qos:"+message.getHeaders().get(MqttHeaders.QOS));

            String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
            String payload = String.valueOf(message.getPayload());
            log.info("入站监听数据：message="+message);
            try {

//            System.out.println("topic:"+topic);
                if(topic != null){
//                if(topic.contains("clientConnectedStatus")){
//                    System.err.println("监听到啦："+topic);
//                }
//                if(topic.contains("disconnected") || topic.contains("connected")){
//                    System.out.println("监听到啦："+topic);
//                }
//                if(!topic.contains("{")){
//                    return;
//                }
//                int start = topic.indexOf("{");
//                int end = topic.indexOf("}");
//                String externalId = topic.substring(start+1,end);
//                if("null".equals(externalId)){
//                    return;
//                }
                    if(!topic.contains("$SYS/brokers")){
                        JSONObject jsonObject ;
                        try {
                            jsonObject = JSONObject.parseObject(payload);
                        } catch (Exception e) {
                            log.error("入站监听数据异常：监听到的数据JSON格式异常：payload="+payload);
                            return;
                        }

                        String externalId = String.valueOf(jsonObject.get("sn"));
                        SdDevices sdDevices = sdDevicesService.getDevicesListByExternalId(externalId);
                        if(sdDevices != null){
//                    String eqId = sdDevices.getEqId();
                            String eqType = String.valueOf(sdDevices.getEqType());
                            HongMengMqttService hongMengMqttService = hongMengMqttStrategyFactory.strategy(eqType);
                            //解析数据
                            hongMengMqttService.handleReceiveData(topic,sdDevices,payload);

                            //添加万集推送队列
                            queue.add(sdDevices);

                        }else{
                            log.error("入站监听数据异常：监听到的数据没有匹配到设备：externalId="+externalId);
                        }
                    }
                    else{//系统topic判断设备上线下线
                        // 将字符串转换为JSONObject
                        JSONObject json = JSONObject.parseObject(payload);
                        String clientId = json.get("clientid").toString();
                        //设备上线
                        /*if(topic.contains("connected")){
                            mcaLogger.info("MQTT clientId："+clientId+" 在线");
                            devicesService.updateOnlineStatus(clientId,true);
                        }*/
                        //设备下线
                        /*if(topic.contains("disconnected")){
                            mcaLogger.info("MQTT clientId："+clientId+" 离线");
                            devicesService.updateOfflineStatus(clientId,true);
                        }*/
                    }

                }
            }catch (Exception e){
                log.error("JSON Format Parsing Exception for MqttInboundConfiguration: {}", topic);
            }

        };
    }


}
