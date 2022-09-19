package com.tunnel.webthings.kafka.consumer;

import cn.hutool.json.JSONUtil;
import com.tunnel.business.service.dataInfo.ISdStateStorageService;
import com.tunnel.webthings.service.TunnelIotDeviceService;
import com.tunnel.webthings.vo.SdStateStorageVO;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;

/**
 * @author ZHC
 * @date 2022/7/16 9:16
 * 读取隧道TunnelEvent主题信息
 */
//@Component
public class KafkaReadListenToTunnelEvent {

    private static final Logger log = LoggerFactory.getLogger(KafkaReadListenToTunnelEvent.class);

    @Autowired
    private ISdStateStorageService sdStateStorageService;

    @Autowired
    private TunnelIotDeviceService service;

    /**
     * 读取隧道事件数据 分区0
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    //@KafkaListener(topics = "tunnelEvent",groupId = "zcTestGroup")
    @KafkaListener(id = "tunnelEvent0", topicPartitions = {@TopicPartition(topic = "tunnelEvent", partitions = "0") }, groupId = "zcTestGroup")
    public void tunnelEventData(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        Object value = record.value();
        String string = value.toString();
        SdStateStorageVO sdStateStorageVO = JSONUtil.toBean(string, SdStateStorageVO.class);
        int i = sdStateStorageService.updateSdStateStorage(sdStateStorageVO.getExpands());
        int add = service.addStateStorage(sdStateStorageVO);
        //log.info("--SdStateStorageVO--> {} -- {}",i,add);
        consumer.commitSync();
    }

    /**
     * 读取隧道事件数据 分区1
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    //@KafkaListener(topics = "tunnelEvent",groupId = "zcTestGroup")
    @KafkaListener(id = "tunnelEvent1", topicPartitions = {@TopicPartition(topic = "tunnelEvent", partitions = "1") }, groupId = "zcTestGroup")
    public void tunnelEventData1(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        Object value = record.value();
        String string = value.toString();
        SdStateStorageVO sdStateStorageVO = JSONUtil.toBean(string, SdStateStorageVO.class);
        int i = sdStateStorageService.updateSdStateStorage(sdStateStorageVO.getExpands());
        int add = service.addStateStorage(sdStateStorageVO);
        //log.info("--SdStateStorageVO--> {} -- {}",i,add);
        consumer.commitSync();
    }

    /**
     * 读取隧道事件数据 分区2
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    //@KafkaListener(topics = "tunnelEvent",groupId = "zcTestGroup")
    @KafkaListener(id = "tunnelEvent2", topicPartitions = {@TopicPartition(topic = "tunnelEvent", partitions = "2") }, groupId = "zcTestGroup")
    public void tunnelEventData2(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        Object value = record.value();
        String string = value.toString();
        SdStateStorageVO sdStateStorageVO = JSONUtil.toBean(string, SdStateStorageVO.class);
        int i = sdStateStorageService.updateSdStateStorage(sdStateStorageVO.getExpands());
        int add = service.addStateStorage(sdStateStorageVO);
        //log.info("--SdStateStorageVO--> {} -- {}",i,add);
        consumer.commitSync();
    }

}
