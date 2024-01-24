package com.tunnel.webthings.kafka.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.*;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdRadarDetectData;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import com.tunnel.business.domain.vehicle.SdKvcar;
import com.tunnel.business.domain.vehicle.SdNoSlowDownTraffic;
import com.tunnel.business.mapper.digitalmodel.SdRadarDetectDataMapper;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficImageMapper;
import com.tunnel.business.mapper.vehicle.SdKvcarMapper;
import com.tunnel.business.mapper.vehicle.SdNoSlowDownTrafficMapper;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.digitalmodel.impl.RadarEventServiceImpl;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.event.ISdEventTypeService;
import com.zc.common.core.kafka.kafkaTool;
import com.zc.common.core.websocket.WebSocketService;
import com.zc.websocket.bo.ChannelProperty;
import com.zc.websocket.constant.AttributeKeyConst;
import com.zc.websocket.util.MsgUtil;
import io.netty.channel.Channel;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import static com.ruoyi.common.utils.DictUtils.getCacheEventKey;

/**
 * @author zhai
 * @date 2023/8/12
 */
@Component
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

    @Autowired
    private RadarEventServiceImpl radarEventServiceImpl;

    /**
     * 线程池
     */
    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    @Qualifier("kafkaTwoTemplate")
    private KafkaTemplate<String, String> kafkaTwoTemplate;

    @Autowired
    @Qualifier("kafkaOneTemplate")
    private KafkaTemplate<String, String> kafkaOneTemplate;

    @Autowired
    private SdNoSlowDownTrafficMapper trafficMapper;

    @Autowired
    private SdKvcarMapper kvcarMapper;

    private static final Logger log = LoggerFactory.getLogger(KafkaReadListenToWanJiTopic.class);

    /**
     * 获取事件
     * @param record
     * @param item
     * @param consumer
     * @throws Exception
     */
    @KafkaListener(topics = {"wj_events"}, containerFactory = "kafkaOneContainerFactory")
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
    @KafkaListener(topics = {"wj_events_evi"}, containerFactory = "kafkaOneContainerFactory")
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
     * 获取车道统计数据
     * @param record
     * @param item
     * @param consumer
     * @throws Exception
     */
    @KafkaListener(topics = {"wj_lane_statistic_data"}, containerFactory = "kafkaOneContainerFactory")
    public void topicLaneStatisticData(ConsumerRecord<String, String> record, Acknowledgment item, Consumer<?,?> consumer) throws Exception {
        log.info("监听到万集车道统计数据： --> {}",record.value());
        if(record.value() != null & record.value() != ""){
            //解析事件数据
            JSONObject objects = JSONObject.parseObject(record.value());
            saveLaneData(objects);
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
    @KafkaListener(topics = {"wj_device"}, containerFactory = "kafkaOneContainerFactory")
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
     * 获取感知数据
     * @param record
     * @param item
     * @param consumer
     * @throws Exception
     */
    @KafkaListener(topics = {"wj_participants"}, containerFactory = "kafkaOneContainerFactory")
    public void topicParticipants(ConsumerRecord<String, String> record, Acknowledgment item, Consumer<?,?> consumer) throws Exception {
        log.info("监听到万集感知数据： --> {}",record.value());
        if(record.value() != null & record.value() != ""){
            //解析感知数据
            JSONObject objects = JSONObject.parseObject(record.value());
            //String data = "{\"direction\":2,\"frameNum\":12175,\"participantList\":[{\"color\":0,\"courseAngle\":256.3,\"enGap\":323,\"id\":1128,\"laneNum\":2,\"latitude\":36.6283242,\"longitude\":117.5767885,\"plate\":\"默A00000\",\"speed\":50.0,\"type\":1}],\"participantNum\":1,\"timeStamp\":1693029615943,\"tunnelId\":\"JQ-JiNan-WenZuBei-MJY\"}";
            //JSONObject objects = JSONObject.parseObject(data);
            //储存感知数据
            saveRadarData(objects);
        }
        //手动提交
        consumer.commitSync();
    }

    /**
     * 获取感知数据
     *
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    @KafkaListener(topics = {"wj_participants"}, containerFactory = "kafkaOneContainerFactoryTwo")
    public void topicParticipantsTwo(List<ConsumerRecord<String,Object>> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info(record.size()+"-----------------");
        for(ConsumerRecord<String,Object> item : record){
            if(item.value() != null & item.value() != ""){
                //解析车辆快照数据
                JSONObject jsonObject = JSONObject.parseObject(item.value().toString());
                //隧道id
                String tunnelId = jsonObject.getString("tunnelId");
                //监测时间
                Date timeStamp = dateZh(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,new Date(jsonObject.getLongValue("timeStamp"))));
                //隧道方向
                String direction = jsonObject.getString("direction");
                JSONArray track = JSONObject.parseArray(jsonObject.getString("participantList"));
                long time = jsonObject.getLongValue("timeStamp");
                Date date = new Date();
                long time1 = date.getTime();
                long time2 = time1 - time;
                //转分钟
                Long minutes = (time2 / 1000) / 60;
                if(minutes>3||track==null){
                    break;
                }
                //新增车辆快照数据
                saveAnalysisCarsnapTwo(track,time,tunnelId,direction,timeStamp);
            }
        }
        consumer.commitSync();
    }

    /**
     * 获取不降速通行数据
     * @param record
     * @param item
     * @param consumer
     * @throws Exception
     */
    @KafkaListener(topics = {"wj_no_slow_down_traffic"}, containerFactory = "kafkaOneContainerFactory")
    public void topicNoSlowDown(ConsumerRecord<String, String> record, Acknowledgment item, Consumer<?,?> consumer) throws Exception {
        log.info("监听到万集感知数据： --> {}",record.value());
        if(record.value() != null & record.value() != ""){
            //解析感知数据
            JSONObject objects = JSONObject.parseObject(record.value());
            saveNoSlowDownData(objects);
        }
        //手动提交
        consumer.commitSync();
    }

    /**
     * 获取两客一危车辆数据
     * @param record
     * @param item
     * @param consumer
     * @throws Exception
     */
    @KafkaListener(topics = {"wj_2k1vCar"}, containerFactory = "kafkaOneContainerFactory")
    public void topicTwokOnevCar(ConsumerRecord<String, String> record, Acknowledgment item, Consumer<?,?> consumer) throws Exception {
        log.info("监听到万集感知数据： --> {}",record.value());
        if(record.value() != null & record.value() != ""){
            //解析感知数据
            JSONArray objects = JSONObject.parseArray(record.value());
            saveKvCarData(objects);
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
        //隧道方向
        String direction = jsonObject.getString("direction");
        //事件列表
        JSONArray eventList = JSONObject.parseArray(jsonObject.get("eventList").toString());

        for(int i = 0; i < eventList.size(); i++){
            JSONObject eventData = JSONObject.parseObject(eventList.get(i).toString());
            SdEvent data = sdEventMapper.selectSdEventById(eventData.getLongValue("eventId"));
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
            sdEvent.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,new Date(Long.parseLong(eventData.getString("startTime")))));
            //sdEvent.setEndTime("0".equals(eventData.getString("endTime")) ? null : DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,new Date(Long.parseLong(eventData.getString("endTime")))));
            sdEvent.setEventTime(DateUtils.parseDate(sdEvent.getStartTime()));
            sdEvent.setLaneNo(eventData.getString("laneNo"));
            sdEvent.setEventSource("1");
            sdEvent.setEventState(EventStateEnum.unprocessed.getCode());
            sdEvent.setStakeNum(eventData.getString("stakeNum"));
            sdEvent.setDirection(direction);
            sdEvent.setConfidenceList(eventData.getString("plate"));
            if(data != null){
                sdEvent.setEventState(data.getEventState());
                sdEventMapper.updateSdEvent(sdEvent);
                if(sdEvent.getConfidenceList() != null && !"".equals(sdEvent.getConfidenceList())){
                    sdEventMapper.updateEventConfidence(sdEvent);
                }
            }else {
                sdEventService.insertSdEvent(sdEvent);
                if(sdEvent.getConfidenceList() != null && !"".equals(sdEvent.getConfidenceList())){
                    sdEventMapper.insertEventConfidence(sdEvent);
                }
                //将事件推送到前端展示
                /*eventSendWeb(jsonObject);*/
                /*Long typeId = sdEvent.getEventTypeId();
                if(typeId == 1L || typeId == 2L || typeId == 11L || typeId == 12L || typeId == 13L || typeId == 14L
                        || typeId == 18L || typeId == 19L || typeId == 20L){
                    //如果是未处理状态改为处理中
                    if(sdEvent.getEventState().equals(EventStateEnum.unprocessed.getCode())){
                        sdEvent.setEventState(EventStateEnum.processing.getCode());
                        sdEvent.setUpdateTime(DateUtils.getNowDate());
                    }
                    String[] split = sdEvent.getEventTitle().split(",");
                    String eventTitle = getEventTitle(sdEvent);
                    sdEvent.setEventTitle(split[0] + "," + eventTitle);
                    noNullStringAttr(sdEvent);
                    Map<String, Object> map = setEventData(sdEvent);
                    //向物联推送事件
                    threadPoolTaskExecutor.execute(()->{
                        sendWlEvent(map);;
                    });
                    //radarEventServiceImpl.sendDataToOtherSystem(map);
                }*/
            }
        }
    }

    /**
     * 储存事件图片视频
     * @param jsonObject
     */
    public void saveEventImage(JSONObject jsonObject){
        //事件数据
        //图片数据
        //事件id
        Long eventId = jsonObject.getLongValue("eventId");
        //查询视频图片
        List<SdTrafficImage> list = imageMapper.selectImageByBusinessId(eventId.toString());
        //事件图片-视频
        String[] imgList = new String[3];
        String[] vedioList = new String[2];
        String imagePath1 = jsonObject.getString("imagePath1");
        String imagePath2 = jsonObject.getString("imagePath2");
        String imagePath3 = jsonObject.getString("imagePath3");
        String vedioPath = jsonObject.getString("videoPath");
        String vedioPath2 = jsonObject.getString("videoPath2");
        imgList[0] = imagePath1;
        imgList[1] = imagePath2;
        imgList[2] = imagePath3;
        vedioList[0] = vedioPath;
        vedioList[1] = vedioPath2;
        List<SdTrafficImage> imageList = new ArrayList<>();
        for(String img:imgList){
            if(img == null || "".equals(img)){
                continue;
            }
            int count = checkImageData(list, img);
            if(count == 1){
                continue;
            }
            SdTrafficImage image = new SdTrafficImage();
            image.setImgUrl(img);
            image.setBusinessId(eventId.toString());
            image.setImgType("0");
            image.setCreateTime(DateUtils.getNowDate());
            imageList.add(image);
        }
        for(String vedio:vedioList){
            if(vedio == null || "".equals(vedio)){
                continue;
            }
            int count = checkImageData(list, vedio);
            if(count == 1){
                continue;
            }
            SdTrafficImage image = new SdTrafficImage();
            image.setImgUrl(vedio);
            image.setBusinessId(eventId.toString());
            image.setImgType("1");
            image.setCreateTime(DateUtils.getNowDate());
            imageList.add(image);
        }
        if(imageList.size() == 0){
            return;
        }
        //将图片视频存入
        imageMapper.brachInsertFaultIconFile(imageList);
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
     * 储存感知数据
     * @param jsonObject
     */
    public void saveRadarData(JSONObject jsonObject){
        //隧道id
        String tunnelId = jsonObject.getString("tunnelId");
        //监测时间
        Date timeStamp = dateZh(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,new Date(jsonObject.getLongValue("timeStamp"))));
        //隧道方向
        String direction = jsonObject.getString("direction");
        //感知数据集合
        JSONArray participantList = jsonObject.getJSONArray("participantList");
        //车辆置信度
        /*JSONObject eventInfo = jsonObject.getJSONObject("eventInfo");
        WjConfidence wjConfidence = new WjConfidence();
        wjConfidence.setEventIds(eventInfo.getLongValue("eventId"));*/
        if(participantList == null){
            return;
        }
        for(int i = 0; i < participantList.size(); i++){
            //单条感知数据
            JSONObject radar = JSONObject.parseObject(participantList.get(i).toString());
            SdRadarDetectData radarDetectData = setRadarData(radar,tunnelId,direction,timeStamp);
            SdRadarDetectDataMapper detectDataMapper = SpringUtils.getBean(SdRadarDetectDataMapper.class);
            //TODO 数据量过大暂时不存入数据库
            detectDataMapper.insertSdRadarDetectData(radarDetectData);
            if(StringUtils.isNotEmpty(radarDetectData.getVehicleLicense()) && StringUtils.isNotNull(radarDetectData.getVehicleLicense())){
                //将数据推送至物联
                sendKafka(radarDetectData);
                setCarsnapRedis(radarDetectData);
            }
        }
    }

    /**
     * 将车道统计数据存入redis
     * @param jsonObject
     */
    public void saveLaneData(JSONObject jsonObject){
        //方向
        String direction = jsonObject.getString("direction");
        //隧道id
        String tunnelId = jsonObject.getString("tunnelId");
        List<Map<String, Object>> mapList = (List<Map<String, Object>>)jsonObject.get("dataList");
        //redisKey 命名规则 carVolume : 隧道id : 方向
        String redisKey = "carVolume:" + tunnelId + ":" + direction;
        Integer carNumber = 0;
        if(mapList != null){
            for(Map<String,Object> item : mapList){
                carNumber = carNumber + Integer.valueOf(item.get("cars").toString());
            }
        }
        redisCache.setCacheObject(redisKey,carNumber,30,TimeUnit.SECONDS);
        //查询隧道信息
        SdTunnels sdTunnels = sdTunnelsService.selectSdTunnelsById(tunnelId);
        //结束桩号
        Integer endpile = Integer.valueOf(sdTunnels.getEndPileNum());
        //开始桩号
        Integer startPile = Integer.valueOf(sdTunnels.getStartPileNum());
        //隧道长度
        BigDecimal tunnelNum = new BigDecimal(endpile - startPile);
        //瞬时隧道最大承受车流量(上行/下行) 公式: 隧道长度/100*3个车道+应急停车带数量
        //(上行/下行) 交通负荷公式: 隧道内车辆数/瞬时隧道最大承受车流量 (上行/下行)
        BigDecimal maxCarNum = tunnelNum.divide(BigDecimal.valueOf(100),0,BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(3)).add(BigDecimal.valueOf(getParkingStripNum(tunnelId)));
        BigDecimal divide = new BigDecimal(carNumber).divide(maxCarNum,2,BigDecimal.ROUND_HALF_UP);
        JSONObject carData = new JSONObject();
        carData.put("tunnelId",tunnelId);
        carData.put("direction",direction);
        carData.put("load",divide.multiply(new BigDecimal(100)));
        kafkaOneTemplate.send(TopicEnum.TUNNEL_TRAFFIC_LOAD_TOPIC.getCode(),carData.toString());
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
        //新增事件后推送前端  弹出视频
        JSONObject object = new JSONObject();
        object.put("sdEventList", sdEventList);
        WebSocketService.broadcast("sdEventList",object.toString());
    }

    /**
     * 时间转换
     * @param timeData
     * @return
     */
    public Date dateZh(String timeData){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换
        Date time = null;
        try {
            if(timeData != null && !"".equals(timeData)){
                time = sdf.parse(timeData);
                return time;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 实时车辆数据
     * @param jsonObject
     * @return
     */
    public SdRadarDetectData setRadarData(JSONObject jsonObject, String tunnelId, String direction, Date timeStamp){
        SdRadarDetectData sdRadarDetectData = new SdRadarDetectData();
        sdRadarDetectData.setTunnelId(tunnelId);
        sdRadarDetectData.setVehicleType(getVehicleType(jsonObject.getString("type")));
        sdRadarDetectData.setVehicleColor(jsonObject.getString("color"));
        sdRadarDetectData.setVehicleLicense(jsonObject.getString("plate"));
        sdRadarDetectData.setLatitude(jsonObject.getString("latitude"));
        sdRadarDetectData.setLongitude(jsonObject.getString("longitude"));
        sdRadarDetectData.setCourseAngle(jsonObject.getString("courseAngle"));
        sdRadarDetectData.setSpeed(jsonObject.getString("speed"));
        sdRadarDetectData.setDistance(jsonObject.getString("enGap"));
        sdRadarDetectData.setLaneNum(jsonObject.getString("laneNum"));
        sdRadarDetectData.setDetectTime(timeStamp);
        sdRadarDetectData.setRoadDir(direction);
        sdRadarDetectData.setVehicleId(jsonObject.getString("id"));
        return sdRadarDetectData;
    }

    /**
     * 对应车辆类型
     *
     * @param vehicleType
     * @return
     */
    public String getVehicleType(String vehicleType){
        String type = null;
        type = WjVehicleTypeEnum.getValue(vehicleType);
        if(type == null){
            type = vehicleType;
        }
        return type;
    }

    /**
     * 将车辆快照存入redis
     *
     * @param sdRadarDetectData
     */
    public Map setCarsnapRedis(SdRadarDetectData sdRadarDetectData){
        Map<String, Object> map = new HashMap<>();
        map.put("tunnelId",sdRadarDetectData.getTunnelId());
        map.put("roadDir",sdRadarDetectData.getRoadDir());
        map.put("speed",sdRadarDetectData.getSpeed());
        map.put("laneNo",sdRadarDetectData.getLaneNum());
        map.put("vehicleType",sdRadarDetectData.getVehicleType());
        map.put("lat",sdRadarDetectData.getLatitude());
        map.put("lng",sdRadarDetectData.getLongitude());
        map.put("distance",sdRadarDetectData.getDistance());
        map.put("vehicleLicense",sdRadarDetectData.getVehicleLicense());
        map.put("detectTime",sdRadarDetectData.getDetectTime());
        map.put("vehicleId",sdRadarDetectData.getVehicleId());
        //redis-key命名规则，固定字段vehicleSnap:隧道id:隧道方向：+
        redisCache.setCacheObject("vehicleSnap:" + sdRadarDetectData.getTunnelId() + ":" + sdRadarDetectData.getRoadDir() + ":" + sdRadarDetectData.getVehicleLicense(),map,30, TimeUnit.SECONDS);
        return  map;
    }

    public Map setCarsnapRedisTwo(SdRadarDetectData sdRadarDetectData){
        Map<String, Object> map = new HashMap<>();
        map.put("tunnelId",sdRadarDetectData.getTunnelId());
        map.put("roadDir",sdRadarDetectData.getRoadDir());
        map.put("speed",sdRadarDetectData.getSpeed());
        map.put("laneNo",sdRadarDetectData.getLaneNum());
        map.put("vehicleType",sdRadarDetectData.getVehicleType());
        map.put("lat",sdRadarDetectData.getLatitude());
        map.put("lng",sdRadarDetectData.getLongitude());
        map.put("distance",sdRadarDetectData.getDistance());
        map.put("vehicleLicense",sdRadarDetectData.getVehicleLicense());
        map.put("detectTime",sdRadarDetectData.getDetectTime());
        map.put("vehicleId",sdRadarDetectData.getVehicleId());
        return  map;
    }

    public void saveAnalysisCarsnapTwo(JSONArray jsonArray,Long time, String tunnelId, String direction, Date timeStamp){
        List<Map> list = new ArrayList<>();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(i).toString());
            jsonObject.put("time",time);
            //保存车辆快照数据
            Map map = saveRadarDataTwo(jsonObject,tunnelId,direction,timeStamp);
            if(StringUtils.isNotEmpty(map)&&!"0".equals(map.get("distance"))&&!map.get("vehicleLicense").equals("默A00000")){
                list.add(map);
            }
        }
        //传到前端实时展示
        JSONObject object = new JSONObject();
        object.put("radarDataList", list);
        //caKokenList 判断小车是否运行的key
        List<String> scanKey = redisCache.getCacheList("caKokenList");
        List<Object> caKokenList = new ArrayList<>();

        for (String key :scanKey) {
            //获取隧道以及是否推送 map可能会有多个
            Map<String, Object> cacheMap = redisCache.getCacheMap(key);
            //推送的token
            String s = key.replaceAll(Constants.CAR_TOKEN, "");

            //后台存在的token集合
            for (Channel channel : MsgUtil.channels){
                ChannelProperty channelProperty = channel.attr(AttributeKeyConst.CHANNEL_PROPERTY_KEY).get();
                //第一步判断 token是否存活
                if(s.equalsIgnoreCase(channelProperty.getTokenSN())){
                    //便利map存放 隧道以及是否推送标志
                    for(String keys : cacheMap.keySet()){
                        //判断是否可以推送  && 前端可以推送的隧道是否和当前隧道匹配
                        if("0".equals(cacheMap.get(keys))&&tunnelId.equals(keys)){
                            // 给指定客户端发送消息
                            kafkaTool.sendAssignWebSocket(s,list,object);
                        }
                    }
                }
            }
        }

    }

    public Map saveRadarDataTwo(JSONObject jsonObject, String tunnelId, String direction, Date timeStamp){
        //单条感知数据
        SdRadarDetectData radarDetectData = setRadarData(jsonObject,tunnelId,direction,timeStamp);
        if(StringUtils.isNotEmpty(radarDetectData.getVehicleLicense()) && StringUtils.isNotNull(radarDetectData.getVehicleLicense())){
            //将数据推送至物联
            //sendKafka(sdRadarDetectData);
            return setCarsnapRedisTwo(radarDetectData);
        }
        return null;
    }

    //将类对象里面的null数据以及date等进行转换
    public static <T> T noNullStringAttr(T cls) {
        Field[] fields = cls.getClass().getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return cls;
        }
        for (Field field : fields) {
            if ("String".equals(field.getType().getSimpleName())) {
                field.setAccessible(true);
                try {
                    Object value = field.get(cls);
                    if (value == null) {
                        field.set(cls, "");
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return cls;
    }

    //组装event数据
    public Map<String, Object> setEventData(SdEvent sdEvent){
        String number = TunnelEnum.getNumber(sdEvent.getTunnelId());
        String keyId = number + String.format("%011d",sdEvent.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("endTime",sdEvent.getEndTime());
        map.put("id",keyId);
        map.put("eventSource",sdEvent.getEventSource());
        map.put("eventState",sdEvent.getEventState());
        map.put("eventLongitude",sdEvent.getEventLongitude());
        map.put("eventLatitude",sdEvent.getEventLatitude());
        map.put("eventTypeId",sdEvent.getEventTypeId());
        map.put("laneNo",sdEvent.getLaneNo());
        map.put("passengerCarNum",sdEvent.getPassengerCarNum());
        map.put("slightInjured",sdEvent.getSlightInjured());
        map.put("smallCarNum",sdEvent.getSmallCarNum());
        map.put("stakeNum",sdEvent.getStakeNum().replaceAll("Z","").replaceAll("Y",""));
        map.put("startTime",sdEvent.getStartTime());
        map.put("tankerNum",sdEvent.getTankerNum());
        map.put("truckNum",sdEvent.getTankerNum());
        map.put("tunnelId",sdEvent.getTunnelId());
        map.put("eventTitle",sdEvent.getEventTitle());
        map.put("eventTime",sdEvent.getEventTime());
        map.put("eventGrade",sdEvent.getEventGrade());
        map.put("direction",sdEvent.getDirection());
        map.put("eventDescription",sdEvent.getEventDescription());
        map.put("currencyId",sdEvent.getCurrencyId());
        map.put("flowId",sdEvent.getFlowId());
        map.put("stakeEndNum",sdEvent.getStakeEndNum());
        map.put("videoUrl",sdEvent.getVideoUrl());
        map.put("createTime",sdEvent.getCreateTime());
        map.put("updateBy",sdEvent.getUpdateBy());
        map.put("updateTime",sdEvent.getUpdateTime());
        map.put("remark",sdEvent.getRemark());
        map.put("eventImgUrl",sdEvent.getEventImgUrl());
        map.put("reviewRemark",sdEvent.getReviewRemark());
        map.put("simplifyName",sdEvent.getSimplifyName());
        map.put("tunnelName",sdEvent.getTunnelName());
        return map;
    }

    /**
     * 大脑标题
     * @param sdEvent
     * @return
     */
    public String getEventTitle(SdEvent sdEvent){
        String title = "";
        switch (sdEvent.getEventTypeId().toString()){
            case "18" :
                title = "行人/非机动车";
            case "19" :
                title = "行人/非机动车";
            case "11" :
                title = "其他";
            case "1" :
                title = "倒车";
            case "14" :
                title = "其他";
            case "13" :
                title = "其他";
            case "2" :
                title = "交通拥堵";
            case "12" :
                title = "交通事故";
            case "20" :
                title = "烟火";
            default: title = sdEvent.getEventTitle().split(",")[1];
        }
        return title;
    }

    /**
     * 推送物联事件
     * @param map
     */
    public void sendWlEvent(Map<String, Object> map){
        Executor executor = Executors.newSingleThreadExecutor();
        CompletionService<Object> completionService = new ExecutorCompletionService<>(executor);
        Future<?> future = completionService.submit(new Callable<Object>() {
            public Object call() throws Exception {
                // 这里是要执行的方法
                radarEventServiceImpl.sendDataToOtherSystem(map);
                return 1;
            }
        });

        // 获取执行结果
        try {
            Object result = completionService.poll(3000L, TimeUnit.MILLISECONDS);
            if (result == null) {
                //System.out.println("超时了，结束该方法的执行");
                log.error("推送物联事件超时{}",JSON.toJSONString(map));
                future.cancel(true);
            } else {
                // 方法执行完毕，处理执行结果
                //System.out.println("方法执行完毕，结果：" + result.toString());
                log.info("推送物联事件成功{}",JSON.toJSONString(map));
            }
        } catch (InterruptedException e) {
            //System.out.println("出现异常，结束该方法的执行");
            log.error("推送物联事件出现异常{}",JSON.toJSONString(map));
            future.cancel(true);
        }
    }

    /**
     * 将数据推送至物联
     * @param sdRadarDetectData
     */
    public void sendKafka(SdRadarDetectData sdRadarDetectData){
        Map<String, Object> map = new HashMap<>();
        map.put("tunnelId",sdRadarDetectData.getTunnelId());
        map.put("roadDir",sdRadarDetectData.getRoadDir());
        map.put("speed",sdRadarDetectData.getSpeed());
        map.put("laneNo",sdRadarDetectData.getLaneNum());
        map.put("vehicleType",sdRadarDetectData.getVehicleType());
        map.put("lat",sdRadarDetectData.getLatitude());
        map.put("lng",sdRadarDetectData.getLongitude());
        map.put("distance",sdRadarDetectData.getDistance());
        map.put("vehicleLicense",sdRadarDetectData.getVehicleLicense());
        map.put("detectTime",sdRadarDetectData.getDetectTime());
        map.put("vehicleId",sdRadarDetectData.getVehicleId());
        JSONObject jsonObject = new JSONObject(map);
        kafkaTwoTemplate.send(TopicEnum.TUNNEL_RADAR_TOPIC.getCode(),jsonObject.toString());
    }

    /**
     * 获取应急停车带数量
     * @param tunnelId
     * @return
     */
    public int getParkingStripNum(String tunnelId){

        switch (tunnelId){
            case "JQ-JiNan-WenZuBei-MJY" : return 3;
            case "JQ-ZiBo-TaiHe-PDS" : return 5;
            case "JQ-ZiBo-TaiHe-QFL" : return 2;
            case "JQ-WeiFang-MiaoZi-BJY" : return 2;
            case "JQ-WeiFang-MiaoZi-WCL" : return 3;
            case "JQ-WeiFang-YangTianShan-SZS" : return 2;
            case "JQ-WeiFang-YangTianShan-YTS" : return 1;
            case "JQ-WeiFang-JiuLongYu-MAS" : return 2;
            case "JQ-WeiFang-JiuLongYu-JJL" : return 1;
            case "JQ-WeiFang-JiuLongYu-HSD" : return 1;
            default: return 0;
        }
    }

    /**
     * 查询是否存在相同视频图片
     * @param list
     * @param data
     * @return
     */
    public int checkImageData(List<SdTrafficImage> list, String data){
        for(SdTrafficImage imageData : list){
            if(data.equals(imageData.getImgUrl())){
                return 1;
            }
        }
        return 0;
    }

    /**
     * 储存不降速通行数据
     * @param object
     */
    public void saveNoSlowDownData(JSONObject object){
        SdNoSlowDownTraffic noSlowDownTraffic = new SdNoSlowDownTraffic();
        noSlowDownTraffic.setTunnelId(object.getString("tunnelId"));
        noSlowDownTraffic.setInAvgSpeed(object.getInteger("inAvgSpeed"));
        noSlowDownTraffic.setOutAvgSpeed(object.getInteger("outAvgSpeed"));
        noSlowDownTraffic.setDirection(object.getString("direction"));
        trafficMapper.insertSdNoSlowDownTraffic(noSlowDownTraffic);
    }

    /**
     * 保存2客1危数据
     * @param jsonArray
     */
    public void saveKvCarData(JSONArray jsonArray){
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(i).toString());
            SdKvcar kvcar = new SdKvcar();
            kvcar.setTunnelId(jsonObject.getString("tunnelId"));
            kvcar.setDirection(jsonObject.getString("direction"));
            kvcar.setCarColor(jsonObject.getString("color2k1v"));
            kvcar.setCarType(getCarColor(jsonObject.getString("type2k1v")));
            kvcar.setPlate(jsonObject.getString("plate"));
            kvcar.setInTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,dateZh(jsonObject.getString("frameFirstTime"))));
            kvcar.setOutTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,dateZh(jsonObject.getString("frameLastTime"))));
            kvcarMapper.insertSdKvcar(kvcar);
        }
    }

    public String getCarColor(String data){
        switch (data){
            case "2":
                return "17";
            case "3":
                return "16";
            case "8":
                return "40";
            default:return data;
        }
    }
}
