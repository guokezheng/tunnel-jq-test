package com.tunnel.webthings.kafka.consumer;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.tunnel.webthings.service.TunnelIotDeviceService;
import com.tunnel.webthings.vo.SdDevStatusVO;
import com.tunnel.webthings.vo.SdRadarMsgTopicVO;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author ZHC
 * @date 2022/7/16 9:16
 * 读取DevStatus主题内容
 */
//@Component
public class KafkaReadListenToDevStatusTopic {

    private static final Logger log = LoggerFactory.getLogger(KafkaReadListenToDevStatusTopic.class);

    @Autowired
    private TunnelIotDeviceService service;

    private SdRadarMsgTopicVO sdRadarMsgTopicVO;

    private SdDevStatusVO sdDevStatusVO;

    /**
     * 读取设备状态数据
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    @KafkaListener(topics = "devStatusTopic",groupId = "zcTestGroup")
    public void devStatusData(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
//        log.info("===================");
//        log.info("------>{}",record.value());
        Object value = record.value();
        String devStatus = value.toString();
        if (StrUtil.contains(devStatus,"G00030001A1990001") || StrUtil.contains(devStatus,"G00030001A1990002")) {
//            log.info("---RadarMsg--->{}",record.value());
            sdRadarMsgTopicVO = JSONUtil.toBean(devStatus, SdRadarMsgTopicVO.class);
            boolean notEmpty = ObjectUtil.isNotEmpty(sdRadarMsgTopicVO);
            if (notEmpty) {
                int i = service.addRadarMag(sdRadarMsgTopicVO);
//                log.info("----addNumber------>  {}",i);
            }
        } else {
            sdDevStatusVO = JSONUtil.toBean(devStatus, SdDevStatusVO.class);
        }
//        log.info("---Radar--> {}", sdRadarMsgTopicVO);
//        log.info("---DevStatus--> {}",sdDevStatusVO);
        consumer.commitSync();
    }

    /**
     * 图片存储路径修改数据库
     */
    @PostMapping("/aaa")
    public void method(){
        service.method();
    }
}
