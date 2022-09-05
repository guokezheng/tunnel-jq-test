package com.tunnel.platform.controller.digitalmodel;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.platform.service.digitalmodel.WjService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Map;

/**
 * @author dzy
 * @date 2022/9/2 9:41
 * 万集事件传入接口
 */
@RestController
@RequestMapping("/wjData")
public class WjEventController {

    @Autowired
    private WjService wjService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public static final Logger log = LoggerFactory.getLogger(WjEventController.class);

    /**
     * 事件数据
     * @param map
     * @return
     */
    @PostMapping("/eventData")
    public AjaxResult eventData(@RequestBody Map<String,Object> map){
        return AjaxResult.success(wjService.insertWjEvent(map));
    }

    /**
     * 事件图片
     * @param map
     */
    @PostMapping("/eventImage")
    public AjaxResult eventImage(@RequestBody Map<String,Object> map){
        return AjaxResult.success(wjService.uploadPic(map));
    }

    /**
     * 事件视频
     */
    @PostMapping("/eventVideo")
    public AjaxResult eventVideo(@RequestBody Map<String,Object> map){
        return AjaxResult.success(wjService.eventVideo(map));
    }

    /**
     * 重点车辆
     */
    @PostMapping("/specialCar")
    public AjaxResult specialCar(@RequestBody Map<String,Object> map){
        return AjaxResult.success(wjService.specialCar(map));
    }

    /**
     * 感知数据
     * kafka的监听器，topic为"matchResultData"，消费者组为"TestGroup"
     * @param record
     * @param item
     */
//    @KafkaListener(topics = "matchResultData", groupId = "TestGroup")
    public void topicMatchResultData(ConsumerRecord<String, String> record, Acknowledgment item) throws ParseException {
        String value = record.value();
        Map<String,Object> map = (Map<String, Object>) JSON.parse(value);
        wjService.insertRadarDetect(map);
        System.out.println(value);
        System.out.println(record);
        log.info("-------------->>>>>>>>>>>>>>>");
        //手动提交
        item.acknowledge();
    }

    /**
     * 生产者测试
     */
    @PostMapping("/send")
    public void send(@RequestBody Map<String,Object> map) throws ParseException {
//        kafkaTemplate.send("matchResultData",  "key", "测试kafka消息");
//        log.info("发送成功");
    }

    /**
     * 雷达-设备运行数据
     * topic wjDeviceRunningInfo
     */
//    @KafkaListener(topics = "wjDeviceRunningInfo", groupId = "TestGroup")
    public void topicWjDeviceRunningInfo(ConsumerRecord<String, String> record, Acknowledgment item) throws ParseException {
        String value = record.value();
        Map<String,Object> map = (Map<String, Object>) JSON.parse(value);
        wjService.saveRedis(map);
        //手动提交
        item.acknowledge();
    }
}
