package com.tunnel.webthings.kafka.consumer;

import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.datacenter.domain.enumeration.PlatformAuthEnum;
import com.tunnel.platform.controller.platformAuthApi.PlatformApiController;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * 读取设备、隧道基础数据
 *
 * @author zhai
 * @date 2022/11/1
 */
public class KafkaReadListenToBasicDataTopic {

    private static final Logger log = LoggerFactory.getLogger(KafkaReadListenToBasicDataTopic.class);

    /**
     * 获取当前平台名称
     */
    @Value("${authorize.name}")
    private String authorizeName;

    @Autowired
    private PlatformApiController platformApiController;

    /**
     * 监听设备基础数据
     *
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"tunnelDeviceBaseData"}, containerFactory = "kafkaTwoContainerFactory")
    public void devicesAccept(ConsumerRecord<String,Object> record, Consumer<?,?> consumer){
        if(PlatformAuthEnum.GSY.getCode().equals(authorizeName)){
            String deviceData = "";
            if(StringUtils.isNotNull(record.value()) && StringUtils.isNotEmpty(record.value().toString())){
                deviceData = record.value().toString();
            }
            platformApiController.devicesAccept(deviceData);
        }
        consumer.commitSync();
    }

    /**
     * 监听设备基础数据
     *
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"tunnelBaseData"}, containerFactory = "kafkaTwoContainerFactory")
    public void tunnelsAccept(ConsumerRecord<String,Object> record, Consumer<?,?> consumer){
        if(PlatformAuthEnum.GSY.getCode().equals(authorizeName)){
            String tunnelData = "";
            if(StringUtils.isNotNull(record.value()) && StringUtils.isNotEmpty(record.value().toString())){
                tunnelData = record.value().toString();
            }
            platformApiController.tunnelsAccept(tunnelData);
        }
        consumer.commitSync();
    }
}
