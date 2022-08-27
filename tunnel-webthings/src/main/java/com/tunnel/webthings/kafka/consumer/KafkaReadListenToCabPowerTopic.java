package com.tunnel.webthings.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 *  读取Topic内容实例
 *  读取智能配电柜数据总数 按照topic - 设备 - 小时 存入redis
 * @author XCT
 */
//@Component
public class KafkaReadListenToCabPowerTopic {

    public static final Logger log = LoggerFactory.getLogger(KafkaReadListenToCabPowerTopic.class);

    /**
     * 获取智能配电柜的数据
     * @param record
     * @param acknowledgment
     * @param consumer
     */
//    @KafkaListener(topics = "devStatusTopic",groupId = "zcTestGroup")
    public void devStatusData(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("===================");
        log.info("{}",record.value());
        consumer.commitSync();
    }
}
