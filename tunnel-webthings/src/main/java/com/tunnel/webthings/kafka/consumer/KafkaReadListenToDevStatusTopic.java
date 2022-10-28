package com.tunnel.webthings.kafka.consumer;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.webthings.service.TunnelIotDeviceService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

/**
 * 读取DevStatus主题内容
 */
@Component
public class KafkaReadListenToDevStatusTopic {

    private static final Logger log = LoggerFactory.getLogger(KafkaReadListenToDevStatusTopic.class);

    @Autowired
    private TunnelIotDeviceService service;

    @Value("${authorize.name}")
    private String authorizeName;

    /**
     * 读取设备状态数据
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    @KafkaListener(topics = {"wq_devStatusTopic"})
    public void devStatusData(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        System.err.println("wq_devStatusTopic:::authorizeName:" + authorizeName);
        log.info("{}", record.value());
//        if (authorizeName != null && !authorizeName.equals("") && authorizeName.equals("GSY")) {
//            if (record.value() != null || !record.value().toString().equals("")) {
//                System.out.println(record.value());
//                JSONObject jsonObject = JSONObject.parseObject(record.value().toString());
//                Object o = jsonObject.get("event");
//                SdEvent sdEvent = JSONUtil.toBean(o.toString(), SdEvent.class);
//                SdEvent event = sdEventMapper.selectSdEventById(sdEvent.getId());
//                if (event != null) {
//                    sdEvent.setUpdateTime(new Date());
//                    sdEventMapper.updateSdEvent(sdEvent);
//                } else {
//                    sdEventMapper.insertSdEvent(sdEvent);
//                }
//            }
//        }
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
