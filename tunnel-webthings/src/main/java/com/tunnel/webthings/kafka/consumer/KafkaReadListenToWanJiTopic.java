package com.tunnel.webthings.kafka.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.datacenter.domain.enumeration.EventStateEnum;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdRadarDetectData;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficImageMapper;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.event.ISdEventTypeService;
import com.tunnel.business.utils.constant.RadarEventConstants;
import com.zc.common.core.websocket.WebSocketService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.DictUtils.getCacheEventKey;

/**
 * @author zhai
 * @date 2023/8/12
 */
public class KafkaReadListenToWanJiTopic {

    @Autowired
    private ISdEventTypeService sdEventTypeService;

    @Autowired
    private ISdTunnelsService sdTunnelsService;

    @Autowired
    private ISdEventService sdEventService;

    @Autowired
    private SdEventMapper sdEventMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SdTrafficImageMapper imageMapper;

    private static final Logger log = LoggerFactory.getLogger(KafkaReadListenToWanJiTopic.class);

    /**
     * 获取事件
     * @param record
     * @param item
     * @param consumer
     * @throws Exception
     */
    @KafkaListener(topics = "wj_events", containerFactory = "kafkaOneContainerFactory")
    public void topicEventData(ConsumerRecord<String, String> record, Acknowledgment item, Consumer<?,?> consumer) throws Exception {
        log.info("监听到万集事件数据： --> {}",record.value());
        if(record.value() != null & record.value() != ""){
            //解析事件数据
            JSONObject objects = JSONObject.parseObject(record.value());
            //储存事件数据
            saveEvent(objects);
        }
        //手动提交
        consumer.commitSync();
    }

    /**
     * 获取事件图片视频
     * @param record
     * @param item
     * @param consumer
     * @throws Exception
     */
    @KafkaListener(topics = "wj_events_evi", containerFactory = "kafkaOneContainerFactory")
    public void topicEventImageData(ConsumerRecord<String, String> record, Acknowledgment item, Consumer<?,?> consumer) throws Exception {
        log.info("监听到万集事件图片视频数据： --> {}",record.value());
        if(record.value() != null & record.value() != ""){
            //解析事件数据
            JSONObject objects = JSONObject.parseObject(record.value());
            //储存事件图片视频数据
            saveEventImage(objects);
        }
        //手动提交
        consumer.commitSync();
    }

    /**
     * 获取设备状态
     * @param record
     * @param item
     * @param consumer
     * @throws Exception
     */
    @KafkaListener(topics = "wj_device", containerFactory = "kafkaOneContainerFactory")
    public void topicDeviceStatus(ConsumerRecord<String, String> record, Acknowledgment item, Consumer<?,?> consumer) throws Exception {
        log.info("监听到万集设备状态数据： --> {}",record.value());
        if(record.value() != null & record.value() != ""){
            //解析设备数据
            JSONObject objects = JSONObject.parseObject(record.value());
            //储存设备状态数据
            saveDeviceStatus(objects);
        }
        //手动提交
        consumer.commitSync();
    }

    /**
     * 储存事件数据
     * @param jsonObject
     */
    public void saveEvent(JSONObject jsonObject){
        //隧道id
        String tunnelId = jsonObject.getString("tunnelId");
        //事件列表
        JSONArray eventList = JSONObject.parseArray(jsonObject.get("eventList").toString());

        for(int i = 0; i < eventList.size(); i++){
            JSONObject eventData = JSONObject.parseObject(eventList.get(i).toString());
            SdEvent data = sdEventService.selectSdEventById(eventData.getLongValue("eventId"));
            SdEvent sdEvent = new SdEvent();
            sdEvent.setId(eventData.getLongValue("eventId"));
            sdEvent.setTunnelId(tunnelId);
            sdEvent.setEventGrade("1");
            sdEvent.setEventTypeId(eventTypeCompare(eventData.getString("eventType")));
            sdEvent.setEventLatitude(eventData.getString("lat"));
            sdEvent.setEventLongitude(eventData.getString("lon"));
            //所有事件类型Map
            Map<Long,String> eventTypeMap = sdEventTypeService.getEventTypeMap();
            //所有隧道Map
            Map<String,String> tunnelMap = sdTunnelsService.getTunnelNameMap();
            sdEvent.setEventTitle(sdEventService.getDefaultEventTitle(sdEvent,tunnelMap,eventTypeMap));
            sdEvent.setStartTime(eventData.getString("startTime"));
            sdEvent.setEndTime(eventData.getString("endTime("));
            sdEvent.setEventTime(DateUtils.parseDate(sdEvent.getStartTime()));
            sdEvent.setLaneNo(eventData.getString("laneNo"));
            sdEvent.setEventSource("1");
            sdEvent.setEventState(EventStateEnum.unprocessed.getCode());
            //TODO 缺少方向桩号
            if(data != null){
                sdEvent.setEventState(data.getEventState());
                sdEventMapper.updateSdEvent(sdEvent);
            }else {
                sdEventService.insertSdEvent(sdEvent);
                //将事件推送到前端展示
                eventSendWeb(jsonObject);
            }

        }
    }

    /**
     * 储存事件图片视频
     * @param jsonObject
     */
    public void saveEventImage(JSONObject jsonObject){
        //事件数据
        JSONArray eventList = JSONObject.parseArray(jsonObject.get("eventList").toString());
        for(int i =0; i < eventList.size(); i++){
            //图片数据
            JSONObject imageData = JSONObject.parseObject(eventList.get(i).toString());
            //事件id
            Long eventId = imageData.getLongValue("eventId");
            //删除历史图片视频
            imageMapper.delImageByBusinessIds(new Long[]{eventId});
            //事件图片-视频
            String[] imgList = new String[3];
            String[] vedioList = new String[2];
            String imagePath1 = imageData.getString("imagePath1");
            String imagePath2 = imageData.getString("imagePath2");
            String imagePath3 = imageData.getString("imagePath3");
            String vedioPath = imageData.getString("vedioPath");
            String vedioPath2 = imageData.getString("vedioPath2");
            imgList[0] = imagePath1;
            imgList[1] = imagePath2;
            imgList[2] = imagePath3;
            vedioList[0] = vedioPath;
            vedioList[1] = vedioPath2;
            List<SdTrafficImage> imageList = new ArrayList<>();
            for(String img:imgList){
                SdTrafficImage image = new SdTrafficImage();
                image.setImgUrl(img);
                image.setBusinessId(eventId.toString());
                image.setImgType("0");
                imageList.add(image);
            }
            for(String vedio:vedioList){
                SdTrafficImage image = new SdTrafficImage();
                image.setImgUrl(vedio);
                image.setBusinessId(eventId.toString());
                image.setImgType("1");
                imageList.add(image);
            }
            //将图片视频存入
            imageMapper.brachInsertFaultIconFile(imageList);
        }
    }

    /**
     * 储存设备状态
     * @param jsonObject
     */
    public void saveDeviceStatus(JSONObject jsonObject){
        //隧道id
        String tunnelId = jsonObject.getString("tunnelId");
        //雷达数据
        JSONArray lidarInfoList = jsonObject.getJSONArray("lidarInfo");
        //相机数据
        JSONArray cameraInfoList = jsonObject.getJSONArray("cameraInfoList");
        for(int i = 0; i < lidarInfoList.size(); i++){
            JSONObject lidarInfoData = JSONObject.parseObject(lidarInfoList.get(i).toString());
            Map<String, Object> map = new HashMap<>();
            map.put("deviceType",lidarInfoData.getLongValue("deviceType"));
            map.put("ip",lidarInfoData.getString("ip"));
            map.put("line",lidarInfoData.getLongValue("line"));
            map.put("errorContent",lidarInfoData.getString("errorContent"));
            map.put("status",lidarInfoData.getLongValue("status"));
            //固定格式WanJiDevice:lidar:隧道id:ip
            redisCache.setCacheObject("wanJiDevice:lidar:"+tunnelId+":"+map.get("ip").toString(),map);
        }
        for(int i = 0; i < cameraInfoList.size(); i++){
            JSONObject cameraInfoData = JSONObject.parseObject(cameraInfoList.get(i).toString());
            Map<String, Object> map = new HashMap<>();
            map.put("deviceType",cameraInfoData.getLongValue("deviceType"));
            map.put("ip",cameraInfoData.getString("ip"));
            map.put("rate",cameraInfoData.getLongValue("rate"));
            map.put("errorContent",cameraInfoData.getString("errorContent"));
            map.put("status",cameraInfoData.getLongValue("status"));
            //固定格式WanJiDevice:camera:隧道id:ip
            redisCache.setCacheObject("wanJiDevice:camera:"+tunnelId+":"+map.get("ip").toString(),map);
        }
    }

    /**
     * 事件类型对应
     * @param type
     * @return
     */
    public Long eventTypeCompare(String type){
        Long eventType = 0L;
        switch (type){
            case "1" :
                eventType = 4L;
                break;
            case "3" :
                eventType = 1L;
                break;
            case "6" :
                eventType = 14L;
                break;
            case "8" :
                eventType = 11L;
                break;
            case "16" :
                eventType = 13L;
                break;
            case "19" :
                eventType = 15L;
                break;
            default:
                eventType = Long.valueOf(type);
                break;
        }
        return eventType;
    }

    /**
     * 将事件推送到前端
     *  @param jsonObject
     */
    public void eventSendWeb(JSONObject jsonObject){
        //新增后再查询数据库，返回给前端事件图标等字段
        SdEvent sdEvent = new SdEvent();
        sdEvent.setId(jsonObject.getLong("eventId"));
        List<SdEvent> sdEventList = sdEventService.querySdEventList(sdEvent);

        List<Map> SdEventMaps = new ArrayList<>();
        sdEventList.stream().forEach( (sdEventItem)->{
            sdEventItem.setIds(sdEventItem.getId().toString());
            //设置视频地址
            sdEventItem.setVideoUrl(
                    StringUtils.isNotEmpty(jsonObject.getString("eventVideoUrl"))&&jsonObject.getString("eventVideoUrl").indexOf(";")!=-1
                            ? jsonObject.getString("eventVideoUrl").split(";")[0]
                            : jsonObject.getString("eventVideoUrl"));
            //事故只存 车祸 和 火灾  通过redis 加 定时任务 推送前端  展示爆炸
            if("16".equals(sdEventItem.getEventType())||"20".equals(sdEventItem.getEventType())){
                redisCache.setCacheObject(getCacheEventKey(sdEventItem.getId().toString()),sdEventItem);
            }
        });
        List<SdTunnels> sdTunnelsList = sdTunnelsService.selectTunnels(SecurityUtils.getDeptId());
        List<SdTunnels> collect = sdTunnelsList.stream().filter(item -> item.getTunnelId().equals(sdEventList.get(0).getTunnelId())).collect(Collectors.toList());
        if(collect.size() > 0){
            //新增事件后推送前端  弹出视频
            JSONObject object = new JSONObject();
            object.put("sdEventList", sdEventList);
            WebSocketService.broadcast("sdEventList",object.toString());
        }
    }
}
