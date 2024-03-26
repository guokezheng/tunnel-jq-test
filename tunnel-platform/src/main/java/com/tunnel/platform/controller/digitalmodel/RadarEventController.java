package com.tunnel.platform.controller.digitalmodel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.event.SdRadarDetectData;
import com.tunnel.business.service.digitalmodel.RadarEventService;
import com.tunnel.business.utils.constant.RadarEventConstants;
import com.zc.common.core.websocket.WebSocketService;
import io.swagger.annotations.ApiOperation;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author dzy
 * @date 2022/9/2 9:41
 * 万集事件传入接口
 */
@RestController
@RequestMapping("/")
public class RadarEventController {

    @Autowired
    private RadarEventService service;


    public static final Logger log = LoggerFactory.getLogger(RadarEventController.class);

    /**
     * 事件数据雷达的  event_source=0
     * @param map
     * @return
     */
    @ApiOperation("事件数据雷达的")
    @PostMapping("wjData/eventData")
    public AjaxResult eventData(@RequestBody Map<String,Object> map){
        return AjaxResult.success(service.insertWjEvent(map));
    }

    /**
     * 事件图片
     * @param map
     */
    @ApiOperation("事件图片")
    @PostMapping("wjData/eventImage")
    public AjaxResult eventImage(@RequestBody Map<String,Object> map){
        return AjaxResult.success(service.uploadPic(map));
    }

    /**
     * 事件视频
     */
    @ApiOperation("事件视频")
    @PostMapping("wjData/eventVideo")
    public AjaxResult eventVideo(@RequestBody Map<String,Object> map){
        return AjaxResult.success(service.eventVideo(map));
    }

    /**
     * 重点车辆
     */
    @ApiOperation("重点车辆")
    @PostMapping("wjData/specialCar")
    public AjaxResult specialCar(@RequestBody Map<String,Object> map){
        return AjaxResult.success(service.specialCar(map));
    }

    /**
     * 感知数据
     * kafka的监听器，topic为"matchResultData"，消费者组为"TestGroup"
     * @param record
     * @param item
     */
//    @KafkaListener(id = "matchResultData",containerFactory = "myKafkaContainerFactory", topicPartitions = {@TopicPartition(topic = RadarEventConstants.MATCHRESULTDATA, partitions = "0")}, groupId = "TestGroup")
    @KafkaListener(topics = {RadarEventConstants.MATCHRESULTDATA}, containerFactory = "kafkaOneContainerFactory")
    public void topicMatchResultData(ConsumerRecord<String, String> record, Acknowledgment item, Consumer<?,?> consumer) throws Exception {
        String value = record.value();
        Map<String,Object> map = (Map<String, Object>) JSON.parse(value);
        String participantNum = map.get("participantNum")+"";
        if (Integer.parseInt(participantNum)>0){
            service.insertRadarDetect(map);
        } else {
            List<SdRadarDetectData> dataList = new ArrayList<>();
            JSONObject object = new JSONObject();
            object.put("radarDataList", dataList);
            WebSocketService.broadcast("radarDataList",object.toString());
        }
        //手动提交
//        item.acknowledge();
        consumer.commitSync();
    }

    /**
     * 生产者测试
     */
    @ApiOperation("生产者测试")
    @PostMapping("wjData/send")
    public void send(@RequestBody Map<String,Object> map) throws ParseException {
//        kafkaTemplate.send("matchResultData",  "key", "测试kafka消息");
//        log.info("发送成功");
        //todo 万集数据推送
        //service.sendBaseDeviceStatus(map);
    }

    /**
     * 雷达-设备运行数据
     * topic wjDeviceRunningInfo
     */
//    @KafkaListener(id = "wjDeviceRunningInfo",containerFactory = "myKafkaContainerFactory", topicPartitions = {@TopicPartition(topic = RadarEventConstants.WJDEVICERUNNINGINFO, partitions = "0")}, groupId = "TestGroup")
    @KafkaListener(topics = {RadarEventConstants.WJDEVICERUNNINGINFO}, containerFactory = "kafkaOneContainerFactory")
    public void topicWjDeviceRunningInfo(ConsumerRecord<String, String> record, Acknowledgment item, Consumer<?,?> consumer) throws ParseException {
        String value = record.value();
        Map<String,Object> map = (Map<String, Object>) JSON.parse(value);
        service.saveRedis(map);
        //手动提交
//        item.acknowledge();
        consumer.commitSync();
    }

    /**
     * 基础设施-万集设备运行状态数据 发送
     * 这个接口谁调谁传值  然后发送kafka
     * 1.4.1
     * topic	baseDeviceStatus
     * 正晨调这个接口传值
     */
    @ApiOperation("基础设施-万集设备运行状态数据 发送")
    @PostMapping("wjData/sendBaseDeviceStatus")
    public String sendBaseDeviceStatus(@RequestBody Map<String,Object> map){
        //todo 万集数据推送
        //service.sendBaseDeviceStatus(map);
        return "向主题发送数据信息";
    }

    /**
     * 2.2.5
     * 设备状态数据查询
     * 万集调用
     * 设备实时数据从缓存取
     * key为 device_data:eqid
     * value 按照设备类型既定格式存储
     * 包含设备id  设备类型 还有类型独特字段
     */
    @ApiOperation("设备状态数据查询")
    @PostMapping("zcData/baseDeviceStatus")
    public AjaxResult baseDeviceStatus(@RequestBody Map<String,Object> map){
        String tunnelId = (String) map.get("tunnelId");
        Object sdRadarDevices = service.selectDevice(tunnelId);
        return AjaxResult.success(sdRadarDevices);
    }
}
