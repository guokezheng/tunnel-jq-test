package com.tunnel.webthings.kafka.consumer;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.service.event.ISdEventService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 读取隧道wq_tunnelEvent主题信息
 */
@Component
public class KafkaReadListenToTunnelEvent {

    private static final Logger log = LoggerFactory.getLogger(KafkaReadListenToTunnelEvent.class);

    @Autowired
    private SdEventMapper sdEventMapper;

    @Value("${authorize.name}")
    private String authorizeName;

    /**
     * 读取隧道事件数据
     *
     * @param record
     * @param acknowledgment
     * @param consumer
     */

    @KafkaListener(topics = {"wq_tunnelEvent"})
    public void tunnelEventData(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
        /**
         * {"timeStamp":"2022-10-27 14:08:53.243",
         * "devNo":"S00063700001980001",
         * "event":{"eventLatitude":"36.4750181","eventLongitude":"117.8171659","eventSource":"0","eventState":"1",
         * "eventType":{"id":143768,"params":{}},"eventTypeId":16,"id":143768,"laneNo":"2","params":{},"passengerCarNum":0,
         * "slightInjured":0,"smallCarNum":0,"stakeNum":"K-1544+-47","startTime":"2022-09-23 09:06:51:858","tankerNum":0,
         * "truckNum":0,"tunnelId":"WLJD-JiNan-YanJiuYuan-FHS",
         * "tunnels":{"params":{},"percentage":{"params":{}},"tunnelId":"WLJD-JiNan-YanJiuYuan-FHS"},
         * "updateTime":1664421865000}}
         */
        System.err.println("authorizeName:" + authorizeName);
        log.info("{}, {}, {}, {}", record.topic(), record.partition(), record.offset(), record.value());
        if (authorizeName != null && !authorizeName.equals("") && authorizeName.equals("GSY")) {
            if (record.value() != null || !record.value().toString().equals("")) {
                System.out.println(record.value());
                JSONObject jsonObject = JSONObject.parseObject(record.value().toString());
                Object o = jsonObject.get("event");
                SdEvent sdEvent = JSONUtil.toBean(o.toString(), SdEvent.class);
                SdEvent event = sdEventMapper.selectSdEventById(sdEvent.getId());
                if (event != null) {
                    sdEvent.setUpdateTime(new Date());
                    sdEventMapper.updateSdEvent(sdEvent);
                } else {
                    sdEventMapper.insertSdEvent(sdEvent);
                }
            }
        }
        consumer.commitSync();
    }

}
