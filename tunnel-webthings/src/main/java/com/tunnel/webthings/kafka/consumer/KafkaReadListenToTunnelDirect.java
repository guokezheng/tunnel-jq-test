package com.tunnel.webthings.kafka.consumer;

import cn.hutool.json.JSONUtil;
import com.tunnel.webthings.service.TunnelIotDeviceService;
import com.tunnel.webthings.vo.SdActiveLuminousSignsVO;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;


/**
 * 接收tunnelDirect主题的信息
 * @author ZHC
 * @date 2022/7/16 9:16
 */
//@Component
public class KafkaReadListenToTunnelDirect {

    private static final Logger log = LoggerFactory.getLogger(KafkaReadListenToTunnelDirect.class);

    @Autowired
    private TunnelIotDeviceService iotDeviceService;

    /**
     * 读取隧道指令 分区0
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    //@KafkaListener(topics = "tunnelDirect",groupId = "zcTestGroup")
    @KafkaListener(id = "tunnelDirect0", topicPartitions = {@TopicPartition(topic = "tunnelDirect", partitions = "0") }, groupId = "zcTestGroup")
    public void tunnelDirectData(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("===================");
        log.info("{}",record.value());
        Object value = record.value();
        String activeLuminousSigns = value.toString();
        SdActiveLuminousSignsVO sdActiveLuminousSigns = JSONUtil.toBean(activeLuminousSigns, SdActiveLuminousSignsVO.class);
        sdActiveLuminousSigns.setEx(sdActiveLuminousSigns.getExpands());
        //log.info("===================");
        //String iotDeviceType = iotDeviceService.selectIotDeviceType(sdActiveLuminousSigns.getDevType());
        iotDeviceService.addActiveLuminousSigns(sdActiveLuminousSigns);
        String user = sdActiveLuminousSigns.getUser();
        log.info("---> {}",user);
        consumer.commitSync();
    }

    /**
     * 读取隧道指令 分区1
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    //@KafkaListener(topics = "tunnelDirect",groupId = "zcTestGroup")
    @KafkaListener(id = "tunnelDirect1", topicPartitions = {@TopicPartition(topic = "tunnelDirect", partitions = "1")}, groupId = "zcTestGroup")
    public void tunnelDirectData1(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer) {
        log.info("=========*==========");
        log.info("{}", record.value());
        Object value = record.value();
        String activeLuminousSigns = value.toString();
        SdActiveLuminousSignsVO sdActiveLuminousSigns = JSONUtil.toBean(activeLuminousSigns, SdActiveLuminousSignsVO.class);
        sdActiveLuminousSigns.setEx(sdActiveLuminousSigns.getExpands());
        //log.info("===================");
        //String iotDeviceType = iotDeviceService.selectIotDeviceType(sdActiveLuminousSigns.getDevType());
        iotDeviceService.addActiveLuminousSigns(sdActiveLuminousSigns);
        String user = sdActiveLuminousSigns.getUser();
        log.info("---> {}", user);
        consumer.commitSync();
    }

    /**
     * 读取隧道指令 分区2
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    //@KafkaListener(topics = "tunnelDirect",groupId = "zcTestGroup")
    @KafkaListener(id = "tunnelDirect2", topicPartitions = { @TopicPartition(topic = "tunnelDirect", partitions = "2") }, groupId = "zcTestGroup")
    public void tunnelDirectData2(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer) {
        log.info("=========**==========");
        log.info("{}", record.value());
        Object value = record.value();
        String activeLuminousSigns = value.toString();
        SdActiveLuminousSignsVO sdActiveLuminousSigns = JSONUtil.toBean(activeLuminousSigns, SdActiveLuminousSignsVO.class);
        sdActiveLuminousSigns.setEx(sdActiveLuminousSigns.getExpands());
        //log.info("===================");
        //String iotDeviceType = iotDeviceService.selectIotDeviceType(sdActiveLuminousSigns.getDevType());
        iotDeviceService.addActiveLuminousSigns(sdActiveLuminousSigns);
        String user = sdActiveLuminousSigns.getUser();
        log.info("---> {}", user);
        consumer.commitSync();
    }

}
