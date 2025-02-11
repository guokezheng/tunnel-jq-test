package com.tunnel.business.service.digitalmodel.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.*;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.digitalmodel.*;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdRadarDetectData;
import com.tunnel.business.domain.informationBoard.SdIotDevice;
import com.tunnel.business.domain.wulian.KafkaRadar;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.digitalmodel.RadarEventMapper;
import com.tunnel.business.mapper.informationBoard.IotBoardReleaseLogMapper;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.digitalmodel.RadarEventService;
import com.tunnel.business.service.event.ISdEventFlowService;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.event.ISdEventTypeService;
import com.tunnel.business.service.informationBoard.ISdIotDeviceService;
import com.tunnel.business.utils.constant.RadarEventConstants;
import com.tunnel.business.utils.util.DataUtils;
import com.zc.common.core.kafka.kafkaTool;
import com.zc.common.core.websocket.WebSocketService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.DictUtils.getCacheEventKey;
import static com.ruoyi.common.utils.DictUtils.getCacheKey;

/**
 * @author dzy 万集接口
 * @date 2022/9/4 16:51
 */
@Service
public class RadarEventServiceImpl implements RadarEventService {

    private static final Logger log = LoggerFactory.getLogger(RadarEventServiceImpl.class);
    /**
     * 时间戳格式
     */
    private static final String sdf_pattern = "yyyy-MM-dd HH:mm:ss.SSS";

    @Autowired
    private ISdEventService sdEventService;
    @Autowired
    private RadarEventMapper wjMapper;
    @Autowired
    private SdDevicesMapper devicesMapper;
    @Autowired
    private SdDeviceDataMapper sdDeviceDataMapper;

    @Autowired
    private ISdEventFlowService eventFlowService;

    @Autowired
    private ISdEventTypeService eventTypeService;

    @Autowired
    private ISdTunnelsService tunnelsService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    @Qualifier("kafkaOneTemplate")
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    @Qualifier("kafkaTwoTemplate")
    private KafkaTemplate<String, String> kafkaTwoTemplate;

    @Value("${wj.imagePath}")
    private String picUrl;
    @Value("${wj.url}")
    private String prefix;
    /*@Value("${eventTopic}")
    private String eventTopic;*/
    @Value("${authorize.name}")
    private String authorizeName;
    /*@Value("${devStatusTopic}")
    private static String devStatusTopic;*/

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SdDeviceDataMapper deviceDataMapper;

    @Autowired
    private IotBoardReleaseLogMapper iotBoardReleaseLogMapper;

    @Autowired
    private ISdIotDeviceService sdIotDeviceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult insertWjEvent(Map<String, Object> map) {

        //获取异常事件列表
        String tunnelId = (String) map.get("tunnelId");
        JSON parse = JSONUtil.parse(map.get("b5eventList"));
        List<WjEvent> list = JSONUtil.toList(parse.toString(), WjEvent.class);
        List<SdEvent> eventList = new ArrayList<>();
        List<Long> eventIdList = new ArrayList<>();
        //所有事件类型Map
        Map<Long,String> eventTypeMap = eventTypeService.getEventTypeMap();
        //所有隧道Map
        Map<String,String> tunnelMap = tunnelsService.getTunnelNameMap();
        list.forEach(f -> {
            SdEvent sdEvent = new SdEvent();
            Integer integer = wjMapper.selectID(f.getEventId());
            if (integer != 0) {
                String eventType = WJEnum.getValue(f.getEventType());
                sdEvent.setEventTypeId(Long.parseLong(eventType));
                sdEvent.setTunnelId(tunnelId);
                sdEvent.setStationId(f.getStationId() + "");
                sdEvent.setStakeNum(f.getStakeNum());
                if(20L == Long.valueOf(eventType)){
                    sdEvent.setLaneNo("1,2,3");
                }else {
                    sdEvent.setLaneNo(f.getLaneNo() + "");
                }
                sdEvent.setEventLongitude(f.getEventLongitude() + "");
                sdEvent.setEventLatitude(f.getEventLatitude() + "");
                sdEvent.setStartTime(dateZh(f.getEventTimeStampStart()) == null ? null : DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,dateZh(f.getEventTimeStampStart())));
                sdEvent.setEndTime(dateZh(f.getEventTimeStampEnd()) == null ? null : DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,dateZh(f.getEventTimeStampEnd())));
                sdEvent.setEventTime(dateZh(f.getEventTimeStampStart()));
                sdEvent.setId(f.getEventId());
                sdEvent.setUpdateTime(DateUtils.getNowDate());
                //方向
                if(!StringUtils.isEmpty(f.getDirection())){
//                    sdEvent.setDirection(f.getDirection() + "");
                    String direction = EventDirectionMap.DIRECTION_MAP.get(String.valueOf(f.getDirection()));
                    sdEvent.setDirection(direction);
                }
                wjMapper.updateEvent(sdEvent);
                //推送事件数据到物联中台kafka
                //sendDataToOtherSystem(null, sdEvent);
            } else {
                sdEvent.setId(f.getEventId());
                String eventType = WJEnum.getValue(f.getEventType());
                sdEvent.setEventTypeId(Long.parseLong(eventType));
                sdEvent.setTunnelId(tunnelId);
                sdEvent.setStationId(f.getStationId() + "");
                //事件桩号
                sdEvent.setStakeNum(f.getStakeNum());
                //车道号
                if(20L == Long.valueOf(eventType)){
                    sdEvent.setLaneNo("1,2,3");
                }else {
                    sdEvent.setLaneNo(f.getLaneNo() + "");
                }
                //经度
                sdEvent.setEventLongitude(f.getEventLongitude() + "");
                //维度
                sdEvent.setEventLatitude(f.getEventLatitude() + "");
                sdEvent.setStartTime(dateZh(f.getEventTimeStampStart()) == null ? null : DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,dateZh(f.getEventTimeStampStart())));
                sdEvent.setEndTime(dateZh(f.getEventTimeStampEnd()) == null ? null : DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,dateZh(f.getEventTimeStampEnd())));
                sdEvent.setEventTime(dateZh(f.getEventTimeStampStart()));
                //事件等级默认为一般
                sdEvent.setEventGrade("1");
                //接收到的事件状态设置为未处理
                sdEvent.setEventState(EventStateEnum.unprocessed.getCode());
                //事件来源：雷达
                sdEvent.setEventSource(EventDescEnum.event_source_radar_video.getCode());
                //事件方向--将万集定义的隧道方向映射为平台的隧道方向
                if(!StringUtils.isEmpty(f.getDirection())){
                    String direction = EventDirectionMap.DIRECTION_MAP.get(String.valueOf(f.getDirection()));
                    sdEvent.setDirection(direction);
                    //sdEvent.setDirection(String.valueOf(f.getDirection()));
                }
                //拼接获取默认的事件标题
                String eventTitle = sdEventService.getDefaultEventTitle(sdEvent,tunnelMap,eventTypeMap);
                sdEvent.setEventTitle(eventTitle);
                sdEvent.setCreateTime(DateUtils.getNowDate());
                eventList.add(sdEvent);
                eventIdList.add(sdEvent.getId());
                List<WjConfidence> targetList = f.getTargetList();
                targetList.forEach(s -> s.setEventIds(f.getEventId()));
                wjMapper.insertEventConfidence(targetList);

                //事故只存 车祸 和 火灾  通过redis 加 定时任务 推送前端  展示爆炸
                if("16".equals(sdEvent.getEventType())||"20".equals(sdEvent.getEventType())){
                    redisCache.setCacheObject(getCacheEventKey(sdEvent.getId().toString()),sdEvent);
                }
            }
        });
        if (CollectionUtils.isNotEmpty(eventList)) {
            wjMapper.insertWjEvent(eventList);
            //推送新添加的事件数据到物联中台
            //sendDataToOtherSystem(eventList, null);
            log.info("---插入数据list---{}", eventList);
            for(SdEvent sdEvent : eventList){
                SdEvent sdEvent1 = new SdEvent();
                sdEvent1.setId(sdEvent.getId());
                List<SdEvent> sdEvents = sdEventService.querySdEventList(sdEvent1);
                sdEvents.stream().forEach(item -> item.setIds(item.getId().toString()));
                List<SdTunnels> sdTunnelsList = tunnelsService.selectTunnels(SecurityUtils.getDeptId());
                List<SdTunnels> collect = sdTunnelsList.stream().filter(item -> item.getTunnelId().equals(sdEvents.get(0).getTunnelId())).collect(Collectors.toList());
                if(collect.size() > 0) {
                    //新增事件后推送前端  弹出视频
                    JSONObject object = new JSONObject();
                    object.put("sdEventList", sdEvents);
                    WebSocketService.broadcast("sdEventList", object.toString());
                }
                // 添加事件流程记录
                eventFlowService.addEventFlowBatch(sdEvents);
            }
            //List<SdEvent> sdEventList = sdEventService.getEventList(eventIdList);

//            WebSocketServer.sendMessage(object.toString());
        }
        return AjaxResult.success();
    }

    //管理站推送事件数据到物联中台kafka
    public void sendDataToOtherSystem(List<SdEvent> eventList, SdEvent sdEvent) {
        if (authorizeName != null && !authorizeName.equals("") && authorizeName.equals("GLZ")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("devNo", "S00063700001980001");
            jsonObject.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern));
            if (eventList != null && eventList.size() > 0) {
                for (int i = 0;i < eventList.size();i++) {
                    SdEvent event = eventList.get(i);
                    //event.setEventState("1");
                    event.setStakeNum(event.getStakeNum().replaceAll("-",""));
                    jsonObject.put("event", event);
//                    kafkaTwoTemplate.send(eventTopic, jsonObject.toString());
                }
            } else if (sdEvent != null) {
                //sdEvent.setEventState("1");
                if(sdEvent.getStakeNum()!=null){
                    sdEvent.setStakeNum(sdEvent.getStakeNum().replaceAll("-",""));
                }
                jsonObject.put("event", sdEvent);
                kafkaTwoTemplate.send(TopicEnum.EVENT_TOPIC.getCode(), jsonObject.toString());
            }
        }
    }

    //重写管理站推送事件数据到物联中台kafka
    public void sendDataToOtherSystem(Map<String,Object> sdEventDataMap) {
        if (authorizeName != null && !authorizeName.equals("") && authorizeName.equals("GLZ")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("devNo", "S00063700001980001");
            jsonObject.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern));
            if(sdEventDataMap.get("stakeNum") != null){
                sdEventDataMap.put("stakeNum",sdEventDataMap.get("stakeNum").toString().replaceAll("-",""));
            }
            jsonObject.put("event", sdEventDataMap);
            JSONObject jsonObject1 = JSONObject.parseObject(jsonObject.toString());
            Object o = jsonObject1.get("event");
            SdEvent sdEvent = JSONUtil.toBean(o.toString(), SdEvent.class);
            kafkaTwoTemplate.send(TopicEnum.EVENT_TOPIC.getCode(), jsonObject.toString());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult uploadPic(Map<String, Object> map) {
        String eventId = map.get("eventId") + "";
        String videoImage = (String) map.get("videoImage");
        String secondVideoImage = (String) map.get("secondVideoImage");
        String thirdVideoImage = (String) map.get("thirdVideoImage");
        if (StringUtils.isNotBlank(eventId)) {
            // 从缓存中获取文件存储路径
//            String fileServerPath = RuoYiConfig.getUploadPath();
            //新路径
//            String url = fileServerPath + picUrl+"/";
            if (StringUtils.isNotBlank(videoImage)) {
                String e1 = "事件前";
//                String imgUrl = ImageUtil.generateImage(videoImage, url,e1);
//                String s1 = this.picName(imgUrl);
                wjMapper.insertPic(eventId, prefix + videoImage,"0", e1);
            }
            if (StringUtils.isNotEmpty(secondVideoImage)) {
                String e2 = "事件中";
//                String imgUrl = ImageUtil.generateImage(secondVideoImage, url,e2);
//                String s2 = this.picName(imgUrl);
                wjMapper.insertPic(eventId, prefix + secondVideoImage, "0", e2);
            }
            if (StringUtils.isNotEmpty(thirdVideoImage)) {
                String e3 = "事件后";
//                String imgUrl = ImageUtil.generateImage(thirdVideoImage, url,e3);
//                String s3 = this.picName(imgUrl);
                wjMapper.insertPic(eventId, prefix + thirdVideoImage, "0", e3 );
            }
        }
        return AjaxResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult eventVideo(Map<String, Object> map) {
        String eventId = map.get("eventId") + "";
        String eventVideoUrl = (String) map.get("eventVideoUrl");
        if (StringUtils.isNotBlank(eventVideoUrl)) {
            //wjMapper.updateVideoById(Long.parseLong(eventId), prefix + eventVideoUrl);
            wjMapper.insertPic(eventId, prefix + eventVideoUrl, "1", "事件短视频" );
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult specialCar(Map<String, Object> map) {
        //数据流水号
        String recordSerialNumber = (String) map.get("recordSerialNumber");
        //参与者全域ID
        String id = map.get("id") + "";
        //隧道ID
        String tunelId = (String) map.get("tunelId");
        //车辆类型
        String originalType = map.get("originalType") + "";
        //车辆颜色
        String originalColor = map.get("originalColor") + "";
        //车牌号
        String picLicense = (String) map.get("picLicense");
        //车牌颜色
        String vehicleColor = map.get("vehicleColor") + "";
        wjMapper.insertVhc(recordSerialNumber, id, tunelId, originalType, originalColor, picLicense, vehicleColor);
        return AjaxResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertRadarDetect(Map<String, Object> map) throws Exception {
        //检测时间，yyyy-MM-dd HH:mm:ss:SSS（可读性考虑）
        String timeStamp = (String) map.get("timeStamp");
        //隧道ID
        String tunnelId = (String) map.get("tunnelId");
        Date date = DateUtils.parseDate(timeStamp, "yyyy-MM-dd HH:mm:ss:SSS");
        JSON parse = JSONUtil.parse(map.get("participants"));
        if (parse == null) {
            log.error("万集推送的感知数据中，交通参与者集合为空");
            return;
        }
        List<WjParticipants> list = JSONUtil.toList(parse.toString(), WjParticipants.class);
        List<SdRadarDetectData> dataList = new ArrayList<>();
        //存入websocket集合
        List<Map> webSocketList = new ArrayList<>();
        list.forEach(
                f -> {
                    SdRadarDetectData sdRadarDetectData = new SdRadarDetectData();
                    sdRadarDetectData.setTunnelId(tunnelId);
                    sdRadarDetectData.setDetectTime(date);
                    sdRadarDetectData.setRecordSerialNumber(f.getRecordSerialNumber());
                    sdRadarDetectData.setVehicleId(f.getId() + "");
                    sdRadarDetectData.setVehicleType(f.getOriginalType() + "");
                    sdRadarDetectData.setVehicleColor(f.getOriginalColor() + "");
                    sdRadarDetectData.setLongitude(f.getLongitude() + "");
                    sdRadarDetectData.setLatitude(f.getLatitude() + "");
                    sdRadarDetectData.setSpeed(f.getSpeed() + "");
                    sdRadarDetectData.setLaneNum(f.getLaneNum() + "");
                    sdRadarDetectData.setCourseAngle(f.getCourseAngle() + "");
                    sdRadarDetectData.setVehicleLicense(f.getPicLicense());
                    sdRadarDetectData.setLicenseColor(f.getVehicleColor() + "");
                    sdRadarDetectData.setStakeNum(f.getStakeNum());
                    sdRadarDetectData.setDistance(f.getDistanceEntry());
                    //将数据推送至物联
                    sendKafka(sdRadarDetectData);
                    //感知数据转换map
                    Map map1 = setCarsnapRedis(sdRadarDetectData);
                    //判断不是空并且启示数据不是0米
                    if(StringUtils.isNotEmpty(map1)&&!"0".equals(map1.get("distance"))){
                        webSocketList.add(map1);
                    }
//                    System.out.println("ID："+sdRadarDetectData.getVehicleId()+",车牌："+sdRadarDetectData.getVehicleLicense()+",速度："+sdRadarDetectData.getSpeed()+",距离："+f.getDistanceEntry());
                    dataList.add(sdRadarDetectData);
                });
        wjMapper.insertRadarDetect(dataList);
        //传到前端实时展示
        JSONObject object = new JSONObject();
        object.put("radarDataList", webSocketList);
        //给指定的客户端发送websocket请求
        kafkaTool.sendAssignWebSocket(null ,webSocketList,object);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRedis(Map<String, Object> map) {
        //隧道ID
        String tunnelId = (String) map.get("tunnelId");
        //转集合接收
        JSON json = JSONUtil.parse(map.get("cameraInfoList"));
        List<WjDeviceCamera> wjDeviceCameraList = JSONUtil.toList(json.toString(), WjDeviceCamera.class);
        //转实体接收
        JSON parse = JSONUtil.parse(map.get("lidarInfo"));
        List<WjDevicelidar> devicelidar = JSONUtil.toList(parse.toString(), WjDevicelidar.class);
        devicelidar.forEach(
                wjDevicelidar -> {
                    List<SdDevices> devicesList = new ArrayList<>();
                    List<Map<String, String>> devicesErrorContentList = new ArrayList<>();
                    //将相机的隧道id设备类型id放入集合
                    wjDeviceCameraList.forEach(
                            f -> {
                                SdDevices devices = new SdDevices();
                                devices.setIp(f.getIp());
                                devicesList.add(devices);
                                Map<String, String> hashMap = new HashMap<>();
                                hashMap.put("errorContent", f.getErrorContent());
                                hashMap.put("ip", f.getIp());
                                devicesErrorContentList.add(hashMap);
                            }
                    );
                    //将雷达的隧道id设备类型id放入集合
                    SdDevices devices = new SdDevices();
                    devices.setIp(wjDevicelidar.getIp());
                    devicesList.add(devices);
                    Map<String, String> hashMap = new HashMap<>();
                    hashMap.put("errorContent", wjDevicelidar.getErrorContent());
                    hashMap.put("ip", wjDevicelidar.getIp());
                    devicesErrorContentList.add(hashMap);
                    //雷达设备类型
                    Integer lidarType = wjDevicelidar.getDeviceType();
                    //相机设备类型
                    Integer cameraType = wjDeviceCameraList.get(0).getDeviceType();
                    //遍历集合查出雷达、枪机设备ID 数据库
                    List<SdDevices> list = devicesMapper.selectDeviceByTidEqtp(devicesList, tunnelId, lidarType, cameraType);
                    //将雷达的状态放入相机集合
                    WjDeviceCamera wjDeviceCamera = new WjDeviceCamera();
                    wjDeviceCamera.setIp(wjDevicelidar.getIp());
                    wjDeviceCamera.setDeviceType(wjDevicelidar.getDeviceType());
                    wjDeviceCamera.setStatus(wjDevicelidar.getStatus());
                    wjDeviceCameraList.add(wjDeviceCamera);
                    for (int i = 0; i < list.size(); i++) {
                        for (int t = 0; t < wjDeviceCameraList.size(); t++) {
                            String eqType = list.get(i).getEqType() + "";
                            if (list.get(i).getIp().equals(wjDeviceCameraList.get(t).getIp()) && eqType.equals(wjDeviceCameraList.get(t).getDeviceType() + "")) {
                                //状态赋最新值
                                String s = wjDeviceCameraList.get(t).getStatus() + "";
                                list.get(i).setEqStatus(s);
                                //给传入数据赋eqId
                                wjDeviceCameraList.get(t).setEqId(list.get(i).getEqId());
                            }
                        }
                    }
                    //将设备状态保存到设备表
                    if (CollectionUtils.isNotEmpty(list)) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("devNo", "S00063700001980001");
                        jsonObject.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern));
                        list.forEach(
                                t -> {
                                    //当前t为单个设备信息，需要把异常事件增加device_data
                                    for (int z = 0;z < devicesErrorContentList.size();z++) {
                                        if (devicesErrorContentList.get(z).get("ip").toString().equals(t.getIp())) {
                                            String value = devicesErrorContentList.get(z).get("errorContent");
                                            SdDeviceData sdDeviceData = new SdDeviceData();
                                            sdDeviceData.setDeviceId(t.getEqId());
                                            if (t.getEqType().longValue() == DevicesTypeEnum.CAMERA_BOX.getCode().longValue()) {
                                                sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.CAMERA_ERROR_CONTETN.getCode()));
                                            } else if (t.getEqType().longValue() == DevicesTypeEnum.LIDAR.getCode().longValue()) {
                                                sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.RADAR_ERROR_CONTETN.getCode()));
                                            }
                                            List<SdDeviceData> deviceData = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
                                            if (deviceData.size() > 0) {
                                                SdDeviceData data = deviceData.get(0);
                                                data.setData(value);
                                                data.setUpdateTime(new Date());
                                                sdDeviceDataMapper.updateSdDeviceData(data);
                                                jsonObject.put("deviceData", data);
                                                kafkaTemplate.send(TopicEnum.DEV_STATUS_TOPIC.getCode(), jsonObject.toString());
                                                log.info("推送物联中台kafka内容：" + jsonObject);
                                            } else {
                                                sdDeviceData.setData(value);
                                                sdDeviceData.setCreateTime(new Date());
                                                sdDeviceDataMapper.insertSdDeviceData(sdDeviceData);
                                                jsonObject.put("deviceData", sdDeviceData);
                                                kafkaTemplate.send(TopicEnum.DEV_STATUS_TOPIC.getCode(), jsonObject.toString());
                                                log.info("推送物联中台kafka内容：" + jsonObject);
                                            }
                                            //接收到摄像机和雷达的数据，直接回传给万集
                                            sendDataToWanJi(t, value);
                                        }
                                    }
                                    devicesMapper.updateSdDevicesBatch(t.getEqId(), t.getEqStatus());
                                    jsonObject.put("deviceStatus", t);
                                    kafkaTemplate.send(TopicEnum.DEV_STATUS_TOPIC.getCode(), jsonObject.toString());
                                    log.info("推送物联中台kafka内容：" + jsonObject);
                                }
                        );
                    }
                    //赋值雷达eqId
                    wjDevicelidar.setEqId(wjDeviceCameraList.get(wjDeviceCameraList.size() - 1).getEqId());
                    //存储后将雷达数据从相机数据剔除
                    wjDeviceCameraList.remove(wjDeviceCameraList.size() - 1);
//                    //雷达数据存redis
//                    redisCache.setCacheMapValue(RadarEventConstants.WJ_LIDAR_INFO_KEY, RadarEventConstants.WJ_LIDAR_INFO_KEY + wjDevicelidar.getEqId(), parse);
//                    //相机数据存redis
//                    wjDeviceCameraList.forEach(
//                            v -> {
//                                JSON parse1 = JSONUtil.parse(v);
//                                redisCache.setCacheMapValue(RadarEventConstants.WJ_CAMERA_INFO_KEY, RadarEventConstants.WJ_CAMERA_INFO_KEY + v.getEqId(), parse1);
//                            }
//                    );
                });
    }

    private void sendDataToWanJi(SdDevices sdDevices, String runDate) {
        Map<String, Object> map = new HashMap<>();
        map.put("deviceId", sdDevices.getEqId());
        map.put("deviceType", sdDevices.getEqType());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("runDate", runDate);
        map.put("deviceData", jsonObject);
        //todo 万集数据推送
        //sendBaseDeviceStatus(map);
    }

    @Override
    public void sendBaseDeviceStatus(Map<String, Object> map) {
        //设备ID
        String deviceId = (String) map.get("deviceId");
        //设备类型 后转Integer
        String deviceType = map.get("deviceType") + "";
        //设备数据：包括设备实时数据、实时状态，根据deviceType区分
        JSON parse = JSONUtil.parse(map.get("deviceData"));
        WjDeviceData wjDeviceData = JSONUtil.toBean(parse.toString(), WjDeviceData.class);
        JSONObject jsonObject = new JSONObject();
        //设备监测状态 后转Integer
        String deviceStatus = devicesMapper.selectEqStatus(deviceId);
        if (deviceStatus == null || deviceStatus.equals("")) {
            deviceStatus = "2";
        }
        String tunnelId = devicesMapper.selecTunnelId(deviceId);
        if ("1".equals(deviceType) || "2".equals(deviceType) || "3".equals(deviceType) || "4".equals(deviceType)
                || "10".equals(deviceType) || "12".equals(deviceType) || "13".equals(deviceType)) {
//            String runStatus = wjDeviceData.getRunStatus() + "";
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("tunnelId", tunnelId);
            jsonObject.put("deviceType", Integer.parseInt(deviceType));
            jsonObject.put("deviceStatus", Integer.parseInt(deviceStatus));
            jsonObject.put("deviceData", parse);
//            jsonObject.put("runStatus", runStatus);
        } else if ("5".equals(deviceType) || "15".equals(deviceType) || "28".equals(deviceType) || "18".equals(deviceType)) {
//            String runDate = wjDeviceData.getRunDate();
//            String unit = wjDeviceData.getUnit();
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("tunnelId", tunnelId);
            jsonObject.put("deviceType", Integer.parseInt(deviceType));
            jsonObject.put("deviceStatus", Integer.parseInt(deviceStatus));
            jsonObject.put("deviceData", parse);
//            jsonObject.put("runDate",runDate);
//            jsonObject.put("unit",unit);
        } else if ("6".equals(deviceType) || "7".equals(deviceType) || "8".equals(deviceType) || "9".equals(deviceType)) {
//            Integer runStatus = wjDeviceData.getRunStatus();
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("tunnelId", tunnelId);
            jsonObject.put("deviceType", Integer.parseInt(deviceType));
            jsonObject.put("deviceStatus", Integer.parseInt(deviceStatus));
            jsonObject.put("deviceData", parse);
//            jsonObject.put("runStatus", runStatus);
        } else if ("31".equals(deviceType) || "30".equals(deviceType)) {
//            Integer runStatus = wjDeviceData.getRunStatus();
//            Integer runMode = wjDeviceData.getRunMode();
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("tunnelId", tunnelId);
            jsonObject.put("deviceType", Integer.parseInt(deviceType));
            jsonObject.put("deviceStatus", Integer.parseInt(deviceStatus));
            jsonObject.put("deviceData", parse);
//            jsonObject.put("runStatus",runStatus);
//            jsonObject.put("runMode",runMode);
        } else if ("17".equals(deviceType)) {
//            Double windSpeed = wjDeviceData.getWindSpeed();
//            String windDirection = wjDeviceData.getWindDirection();
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("tunnelId", tunnelId);
            jsonObject.put("deviceType", Integer.parseInt(deviceType));
            jsonObject.put("deviceStatus", Integer.parseInt(deviceStatus));
            jsonObject.put("deviceData", parse);
//            jsonObject.put("windSpeed",windSpeed);
//            jsonObject.put("windDirection",windDirection);
        } else if ("19".equals(deviceType)) {
//            Double co = wjDeviceData.getCO();
//            Double vi = wjDeviceData.getVI();
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("tunnelId", tunnelId);
            jsonObject.put("deviceType", Integer.parseInt(deviceType));
            jsonObject.put("deviceStatus", Integer.parseInt(deviceStatus));
            jsonObject.put("deviceData", parse);
//            jsonObject.put("co",co);
//            jsonObject.put("vi",vi);
        } else if ("16".equals(deviceType)) {
//            String message = wjDeviceData.getMessage();
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("tunnelId", tunnelId);
            jsonObject.put("deviceType", Integer.parseInt(deviceType));
            jsonObject.put("deviceStatus", Integer.parseInt(deviceStatus));
            jsonObject.put("deviceData", parse);
//            jsonObject.put("message",message);
        } else if ("32".equals(deviceType)) {
//            Double windSpeed = wjDeviceData.getWindSpeed();
//            String windDirection = wjDeviceData.getWindDirection();
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("tunnelId", tunnelId);
            jsonObject.put("deviceType", Integer.parseInt(deviceType));
            jsonObject.put("deviceStatus", Integer.parseInt(deviceStatus));
            jsonObject.put("deviceData", parse);
//            jsonObject.put("windSpeed",windSpeed);
//            jsonObject.put("windDirection",windDirection);
        } else if ("23".equals(deviceType) || "26".equals(deviceType)) {
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("tunnelId", tunnelId);
            jsonObject.put("deviceType", Integer.parseInt(deviceType));
            jsonObject.put("deviceStatus", Integer.parseInt(deviceStatus));
            jsonObject.put("deviceData", parse);
        }
        log.info("-----测试测试测试----{}", jsonObject);
        if (jsonObject.equals(null) || jsonObject.isEmpty()) {
            return;
        }
        kafkaTemplate.send(RadarEventConstants.BASEDEVICESTATUS, jsonObject.toString());
    }

    @Override
    public Object selectDevice(String tunnelId) {
        List<SdDevices> devices = devicesMapper.selectDevice(tunnelId);
        //List<SdRadarDevice> list = new ArrayList<>();
        /*int shouBaoAlarmCode = DevicesTypeItemEnum.SHOU_BAO_ALARM.getCode();
        int flameDetectorAlarmCode = DevicesTypeItemEnum.FLAME_DETECTOR_ALARM.getCode();*/
        /*for (SdDevices f : devices) {
            //设备类型34对应的是消防主机，没有必要提供给万集
            if (f.getEqType().longValue() == 34L) {
                continue;
            }
            SdRadarDevice sdRadarDevice = setRadarDevice(f);
            com.alibaba.fastjson.JSONObject deviceData = new com.alibaba.fastjson.JSONObject();
            if ("1".equals(sdRadarDevice.getDeviceType()) || "2".equals(sdRadarDevice.getDeviceType()) || "3".equals(sdRadarDevice.getDeviceType()) || "4".equals(sdRadarDevice.getDeviceType())
                || "10".equals(sdRadarDevice.getDeviceType()) || "12".equals(sdRadarDevice.getDeviceType()) || "13".equals(sdRadarDevice.getDeviceType())
                || "6".equals(sdRadarDevice.getDeviceType()) || "7".equals(sdRadarDevice.getDeviceType()) || "8".equals(sdRadarDevice.getDeviceType()) || "9".equals(sdRadarDevice.getDeviceType())
                || "30".equals(sdRadarDevice.getDeviceType()) || "31".equals(sdRadarDevice.getDeviceType())) {
                List<Map<String, Object>> maps = devicesMapper.selectDeviceDataAndUnit(f.getEqId());
                for (int i = 0;i < maps.size();i++) {
                    Map<String, Object> map = maps.get(i);
                    if (map.get("data") != null) {
                        deviceData.put("runStatus", Integer.parseInt(map.get("data").toString()));
                    } else {
                        deviceData.put("runStatus", 0);
                    }
                }
            } else if ("5".equals(sdRadarDevice.getDeviceType()) || "15".equals(sdRadarDevice.getDeviceType()) || "28".equals(sdRadarDevice.getDeviceType()) || "18".equals(sdRadarDevice.getDeviceType())) {
                List<Map<String, Object>> maps = devicesMapper.selectDeviceDataAndUnit(f.getEqId());
                for (int i = 0;i < maps.size();i++) {
                    Map<String, Object> map = maps.get(i);
                    if (map.get("data") != null) {
                        deviceData.put("runDate", Double.parseDouble(map.get("data").toString()));
                    }
                    if (map.get("unit") != null) {
                        deviceData.put("unit", map.get("unit").toString());
                    }
                }
            }else if ("17".equals(sdRadarDevice.getDeviceType())) {
                List<Map<String, Object>> maps = devicesMapper.selectDeviceDataAndUnit(f.getEqId());
                for (int i = 0;i < maps.size();i++) {
                    Map<String, Object> map = maps.get(i);
                    if (map.get("data") != null && map.get("itemId") != null
                            && Long.valueOf(map.get("itemId").toString()).longValue() == Long.valueOf(DevicesTypeItemEnum.FENG_SU.getCode()).longValue()) {
                        deviceData.put("windSpeed", Double.parseDouble(map.get("data").toString()));
                    }
                    if (map.get("data") != null && map.get("itemId") != null
                            && Long.valueOf(map.get("itemId").toString()).longValue() == Long.valueOf(DevicesTypeItemEnum.FENG_XIANG.getCode()).longValue()) {
                        deviceData.put("windDirection", map.get("data").toString());
                    }
                }
            } else if ("19".equals(sdRadarDevice.getDeviceType())) {
                List<Map<String, Object>> maps = devicesMapper.selectDeviceDataAndUnit(f.getEqId());
                for (int i = 0;i < maps.size();i++) {
                    Map<String, Object> map = maps.get(i);
                    if (map.get("data") != null && map.get("itemId") != null
                            && Long.valueOf(map.get("itemId").toString()).longValue() == Long.valueOf(DevicesTypeItemEnum.CO.getCode()).longValue()) {
                        deviceData.put("CO", Double.parseDouble(map.get("data").toString()));
                    }
                    if (map.get("data") != null && map.get("itemId") != null
                            && Long.valueOf(map.get("itemId").toString()).longValue() == Long.valueOf(DevicesTypeItemEnum.VI.getCode()).longValue()) {
                        deviceData.put("VI", Double.parseDouble(map.get("data").toString()));
                    }
                }
            }else if ("23".equals(sdRadarDevice.getDeviceType()) || "26".equals(sdRadarDevice.getDeviceType())) {
                List<Map<String, Object>> maps = devicesMapper.selectDeviceDataAndUnit(f.getEqId());
                for (int i = 0;i < maps.size();i++) {
                    Map<String, Object> map = maps.get(i);
                    if (map.get("data") != null) {
                        deviceData.put("runDate", map.get("data").toString());
                    }
                }
            } else if ("16".equals(sdRadarDevice.getDeviceType()) || "36".equals(sdRadarDevice.getDeviceType())) {
                //隧道内情报板
                List<Map<String, Object>> vmsData = deviceDataMapper.getVmsData(f.getEqId());
                for (int i = 0;i < vmsData.size();i++) {
                    Map<String, Object> map = vmsData.get(i);
                    //设备id
                    String deviceId = map.get("deviceId").toString();
                    SdDevices sdDevices = devicesMapper.selectSdDevicesById(deviceId);
                    JSONObject devMap = new JSONObject();
                    //情报板编号
                    devMap.put("boardNo",sdDevices.getAssociatedDeviceId());
                    //隧道id
                    devMap.put("tunnelId",sdDevices.getEqTunnelId());
                    //设备id
                    devMap.put("deviceCode",sdDevices.getEqId());
                    //情报板数据
                    String data = map.get("data").toString();
                    JSONArray objects = JSONObject.parseArray(data);
                    devMap.put("list",objects);
                    sdRadarDevice.setDeviceData(devMap);
                    list.add(sdRadarDevice);
                }
            }
            if(!"16".equals(sdRadarDevice.getDeviceType()) && !"36".equals(sdRadarDevice.getDeviceType())){
                sdRadarDevice.setDeviceData(deviceData);
                list.add(sdRadarDevice);
            }
        }
        //获取万集雷达相机数据
        Collection<String> wanJiDevice = redisCache.keys("wanJiDevice");
        List<Map<String, Object>> deviceDataList = (List<Map<String, Object>>)redisTemplate.opsForValue().multiGet(wanJiDevice);
        for(Map<String, Object> map : deviceDataList){
            JSONObject deviceData = new JSONObject();
            String deviceType = map.get("deviceType").toString();
            SdDevices sdDevices = new SdDevices();
            sdDevices.setEqType(Long.valueOf(deviceType));
            sdDevices.setIp(map.get("ip").toString());
            List<SdDevices> devicesList = devicesMapper.selectSdDevicesList(sdDevices);
            SdRadarDevice sdRadarDevice = setRadarDevice(devicesList.get(0));
            if("26".equals(deviceType)){
                deviceData.put("line",Integer.valueOf(map.get("line").toString()));
            }else {
                deviceData.put("rate",Integer.valueOf(map.get("rate").toString()));
            }
            deviceData.put("errorContent",map.get("errorContent").toString());
            sdRadarDevice.setDeviceData(deviceData);
            list.add(sdRadarDevice);
        }*/
        //获取实时信息
        List<SdRadarDevice> list = getDeviceRadar(devices);

        //在线
        int normal = 0;
        //离线
        int errorNum = 0;
        //故障
        int offlineNum = 0;
        for (SdRadarDevice sdRadarDevice : list) {
            if (sdRadarDevice.getDeviceStatus() != null) {
                if (sdRadarDevice.getDeviceStatus() == 1) {
                    normal += 1;
                } else if (sdRadarDevice.getDeviceStatus() == 3) {
                    errorNum += 1;
                } else if (sdRadarDevice.getDeviceStatus() == 2) {
                    offlineNum += 1;
                }
            }
        }
        JSONObject object = new JSONObject();
        object.put("onlineNum", normal);
        object.put("errorNum", errorNum);
        object.put("offlineNum", offlineNum);
        object.put("deviceList", list);
        return JSONUtil.parse(object);
    }

    /**
     * 实时数据信息
     * @param f
     * @return
     */
    public SdRadarDevice setRadarDevice(SdDevices f){
        SdRadarDevice sdRadarDevice = new SdRadarDevice();
        sdRadarDevice.setTunnelId(f.getEqTunnelId());
        sdRadarDevice.setDeviceId(f.getEqId());
        sdRadarDevice.setIp(f.getIp());
        sdRadarDevice.setDeviceType(f.getEqType() + "");
        if (f.getEqName().contains("k")) {
            sdRadarDevice.setDeviceName(f.getEqName().substring(0, f.getEqName().indexOf("k") - 1));
        } else {
            sdRadarDevice.setDeviceName(f.getEqName());
        }
        sdRadarDevice.setDeviceStatus((f.getEqStatus() == null || "".equals(f.getEqStatus())) ? 2 : Integer.parseInt(f.getEqStatus()));
        if (StringUtils.isNotEmpty(f.getLng())) {
            sdRadarDevice.setLongitude(Double.parseDouble(f.getLng()));
        }
        if (StringUtils.isNotEmpty(f.getLat())) {
            sdRadarDevice.setLatitude(Double.parseDouble(f.getLat()));
        }
        sdRadarDevice.setDirection(f.getEqDirection());
        sdRadarDevice.setStakeNum(f.getPile());
        sdRadarDevice.setTransform(f.getRemark());
        return sdRadarDevice;
    }


    private String picName(String urlName) {
        String[] split = urlName.split("/");
        String s = split[split.length - 1];
        return s;
    }

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
     * 将车辆快照转map
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
//        redisCache.setCacheObject("vehicleSnap:" + sdRadarDetectData.getTunnelId() + ":" + sdRadarDetectData.getVehicleLicense(),map,5, TimeUnit.SECONDS);
        return  map;
    }

    /**
     * 将数据推送至物联
     * @param sdRadarDetectData
     */
    public void sendKafka(SdRadarDetectData sdRadarDetectData){
        Map<String, Object> map = new HashMap<>();
        map.put("tunnelId",sdRadarDetectData.getTunnelId());
        map.put("direction",sdRadarDetectData.getRoadDir());
        map.put("speed",sdRadarDetectData.getSpeed());
        map.put("laneNo",sdRadarDetectData.getLaneNum());
        map.put("vehicleType",sdRadarDetectData.getVehicleType());
        map.put("lat",sdRadarDetectData.getLatitude());
        map.put("lng",sdRadarDetectData.getLongitude());
        map.put("distance",sdRadarDetectData.getDistance());
        map.put("vehicleLicense",sdRadarDetectData.getVehicleLicense());
        JSONObject jsonObject = new JSONObject(map);
        kafkaTwoTemplate.send(TopicEnum.TUNNEL_RADAR_TOPIC.getCode(),jsonObject.toString());
    }

    /**
     * 查询设备实时状态
     * @param devices
     * @return
     */
    public List<SdRadarDevice> getDeviceRadar(List<SdDevices> devices){
        List<SdRadarDevice> list = new ArrayList<>();
        for (SdDevices f : devices) {
            //设备类型34对应的是消防主机，没有必要提供给万集
            if (f.getEqType().longValue() == 34L) {
                continue;
            }
            SdRadarDevice sdRadarDevice = setRadarDevice(f);
            com.alibaba.fastjson.JSONObject deviceData = new com.alibaba.fastjson.JSONObject();
            if ("1".equals(sdRadarDevice.getDeviceType()) || "2".equals(sdRadarDevice.getDeviceType()) || "3".equals(sdRadarDevice.getDeviceType()) || "4".equals(sdRadarDevice.getDeviceType())
                    || "10".equals(sdRadarDevice.getDeviceType()) || "12".equals(sdRadarDevice.getDeviceType()) || "13".equals(sdRadarDevice.getDeviceType())
                    || "30".equals(sdRadarDevice.getDeviceType()) || "31".equals(sdRadarDevice.getDeviceType()) || "45".equals(sdRadarDevice.getDeviceType())) {
                List<Map<String, Object>> maps = devicesMapper.selectDeviceDataAndUnit(f.getEqId());
                for (int i = 0;i < maps.size();i++) {
                    Map<String, Object> map = maps.get(i);
                    if("30".equals(f.getEqType().toString())){
                        if("19".equals(map.get("itemId").toString())){
                            deviceData.put("runStatus", Integer.parseInt(map.get("data").toString()));
                        }
                    }else if (map.get("data") != null || !"".equals(map.get("data"))) {
                        if("远程".equals(map.get("data").toString()) || "就地".equals(map.get("data").toString()) || "".equals(map.get("data"))){
                            continue;
                        }
                        deviceData.put("runStatus", Integer.parseInt(map.get("data").toString()));
                    } else {
                        deviceData.put("runStatus", 0);
                    }
                }
            }else if("6".equals(sdRadarDevice.getDeviceType()) || "7".equals(sdRadarDevice.getDeviceType()) || "8".equals(sdRadarDevice.getDeviceType()) || "9".equals(sdRadarDevice.getDeviceType())){
                List<Map<String, Object>> maps = devicesMapper.selectDeviceDataAndUnit(f.getEqId());
                for (int i = 0;i < maps.size();i++) {
                    Map<String, Object> map = maps.get(i);
                    if (map.get("data") != null || !"".equals(map.get("data"))) {
                        if(map.get("itemId") != null && !"".equals(map.get("itemId"))){
                            Integer itemId = Integer.valueOf(map.get("itemId").toString());
                            if(itemId != DevicesTypeItemEnum.JQ_LIGHT_OPENCLOSE.getCode() && itemId != DevicesTypeItemEnum.JI_BEN_ZHAO_MING_OPENCLOSE.getCode()){
                                continue;
                            }
                        }
                        deviceData.put("runStatus", Integer.parseInt(map.get("data").toString()));
                    } else {
                        continue;
                    }
                }
            } else if ("5".equals(sdRadarDevice.getDeviceType()) || "15".equals(sdRadarDevice.getDeviceType()) || "28".equals(sdRadarDevice.getDeviceType()) || "18".equals(sdRadarDevice.getDeviceType())) {
                List<Map<String, Object>> maps = devicesMapper.selectDeviceDataAndUnit(f.getEqId());
                for (int i = 0;i < maps.size();i++) {
                    Map<String, Object> map = maps.get(i);
                    if (map.get("data") != null || !"".equals(map.get("data"))) {
                        deviceData.put("runDate", Double.parseDouble(map.get("data").toString()));
                    }
                    if (map.get("unit") != null) {
                        deviceData.put("unit", map.get("unit").toString());
                    }
                }
            }else if ("17".equals(sdRadarDevice.getDeviceType())) {
                List<Map<String, Object>> maps = devicesMapper.selectDeviceDataAndUnit(f.getEqId());
                for (int i = 0;i < maps.size();i++) {
                    Map<String, Object> map = maps.get(i);
                    if (map.get("data") != null && map.get("itemId") != null
                            && Long.valueOf(map.get("itemId").toString()).longValue() == Long.valueOf(DevicesTypeItemEnum.FENG_SU.getCode()).longValue()) {
                        deviceData.put("windSpeed", Double.parseDouble(map.get("data").toString()));
                    }
                    if (map.get("data") != null && map.get("itemId") != null
                            && Long.valueOf(map.get("itemId").toString()).longValue() == Long.valueOf(DevicesTypeItemEnum.FENG_XIANG.getCode()).longValue()) {
                        deviceData.put("windDirection", map.get("data").toString());
                    }
                }
            } else if ("19".equals(sdRadarDevice.getDeviceType())) {
                List<Map<String, Object>> maps = devicesMapper.selectDeviceDataAndUnit(f.getEqId());
                for (int i = 0;i < maps.size();i++) {
                    Map<String, Object> map = maps.get(i);
                    if (map.get("data") != null && map.get("itemId") != null
                            && Long.valueOf(map.get("itemId").toString()).longValue() == Long.valueOf(DevicesTypeItemEnum.CO.getCode()).longValue()) {
                        deviceData.put("CO", Double.parseDouble(map.get("data").toString()));
                    }
                    if (map.get("data") != null && map.get("itemId") != null
                            && Long.valueOf(map.get("itemId").toString()).longValue() == Long.valueOf(DevicesTypeItemEnum.VI.getCode()).longValue()) {
                        deviceData.put("VI", Double.parseDouble(map.get("data").toString()));
                    }
                }
            }else if ("23".equals(sdRadarDevice.getDeviceType()) || "26".equals(sdRadarDevice.getDeviceType())) {
                List<Map<String, Object>> maps = devicesMapper.selectDeviceDataAndUnit(f.getEqId());
                for (int i = 0;i < maps.size();i++) {
                    Map<String, Object> map = maps.get(i);
                    if (map.get("data") != null) {
                        deviceData.put("runDate", map.get("data").toString());
                    }
                }
            } else if ("16".equals(sdRadarDevice.getDeviceType())) {
                //隧道内情报板
                /*List<Map<String, Object>> vmsData = deviceDataMapper.getVmsData(f.getEqId());
                for (int i = 0;i < vmsData.size();i++) {

                }*/
                //Map<String, Object> map = vmsData.get(i);
                //设备id
                String deviceId = sdRadarDevice.getDeviceId();
                SdDevices sdDevices = devicesMapper.selectSdDevicesById(deviceId);
                JSONObject devMap = new JSONObject();
                //情报板编号
                devMap.put("boardNo",sdDevices.getAssociatedDeviceId());
                //隧道id
                devMap.put("tunnelId",sdDevices.getEqTunnelId());
                //设备id
                devMap.put("deviceCode",sdDevices.getEqId());
                //情报板数据
                JSONObject board = getBoard(deviceId);
                devMap.put("list",board.getJSONArray("parameters"));
                sdRadarDevice.setDeviceData(devMap);
                list.add(sdRadarDevice);
            }
            if(!"16".equals(sdRadarDevice.getDeviceType()) && !"36".equals(sdRadarDevice.getDeviceType())){
                sdRadarDevice.setDeviceData(deviceData);
                list.add(sdRadarDevice);
            }
        }
        //获取万集雷达相机数据
        Collection<String> wanJiDevice = redisCache.keys("wanJiDevice");
        List<Map<String, Object>> deviceDataList = (List<Map<String, Object>>)redisTemplate.opsForValue().multiGet(wanJiDevice);
        for(Map<String, Object> map : deviceDataList){
            JSONObject deviceData = new JSONObject();
            String deviceType = map.get("deviceType").toString();
            SdDevices sdDevices = new SdDevices();
            sdDevices.setEqType(Long.valueOf(deviceType));
            sdDevices.setIp(map.get("ip").toString());
            List<SdDevices> devicesList = devicesMapper.selectSdDevicesList(sdDevices);
            SdRadarDevice sdRadarDevice = setRadarDevice(devicesList.get(0));
            if("26".equals(deviceType)){
                deviceData.put("line",Integer.valueOf(map.get("line").toString()));
            }else {
                deviceData.put("rate",Integer.valueOf(map.get("rate").toString()));
            }
            deviceData.put("errorContent",map.get("errorContent").toString());
            sdRadarDevice.setDeviceData(deviceData);
            list.add(sdRadarDevice);
        }
        return list;
    }


    /**
     * 获取情报板数据信息
     * @param deviceId
     * @return
     */
    public JSONObject getBoard(String deviceId){
        SdDevices sdDevices = devicesMapper.selectBoardSdDevicesById(deviceId);
        if(sdDevices == null){
            return null;
        }
        Map<String, Object> boardContentData = iotBoardReleaseLogMapper.getBoardContentData(sdDevices.getAssociatedDeviceId().toString());
        SdIotDevice sdIotDevice = sdIotDeviceService.selectIotDeviceById(sdDevices.getAssociatedDeviceId());
        String protocolType = sdIotDevice.getProtocolName();
        if(boardContentData != null){
            Object content2 = boardContentData.get("content");
            if(content2 == null || "".equals(content2)){
                return null;
            }
            String content = boardContentData.get("content").toString();
            String substring = "";
            substring = content.substring(content.indexOf("ITEM0"), content.length());
                /*if(!protocolType.startsWith(IDeviceProtocol.SANSI)){
                    substring = content.substring(31,content.length());
                }else {
                    substring = content.substring(27,content.length());
                }*/
            String boardCon = DataUtils.itemContentToJson(substring, protocolType);
            JSONArray objects = new JSONArray();
            if(boardCon != null && !"".equals(boardCon) && boardCon.contains("[")){
                objects = JSONObject.parseArray(boardCon);
            }
            String o = "";
            List<JSONObject> list = new ArrayList<>();
            for(int i = 0; i < objects.size(); i++){
                JSONObject jsonObject1 = JSONObject.parseObject(objects.get(i).toString());
                if(protocolType.startsWith("GUANGDIAN") || protocolType.startsWith("DINGEN") || protocolType.startsWith("TONGZHOU") || protocolType.startsWith("DIANMING") || protocolType.startsWith("SANSI")){
                    JSONArray jsonArray = JSONObject.parseArray(jsonObject1.get("ITEM" + String.format("%03d", i)).toString());
                    for(int a = 0; a < jsonArray.size(); a++){
                        JSONObject jsonObject2 = JSONObject.parseObject(jsonArray.get(a).toString());
                            /*o = jsonObject2.get("ITEM" + String.format("%03d", i)).toString();
                            String s = o.replaceAll("\\[", "").replaceAll("]", "");
                            JSONObject jsonObject = JSONObject.parseObject(s);*/
                        if(protocolType.startsWith("DINGEN")){
                            String content1 = jsonObject2.getString("CONTENT");
                            if(content1.contains("W")){
                                content1 = content1.substring(1, jsonObject2.getString("CONTENT").length());
                            }
                            content1 = content1.replaceAll("<n>", "<br>");
                            content1 = content1.replaceAll("<r>", "\r");
                            jsonObject2.put("CONTENT",content1);
                        }else {
                            String content1 = jsonObject2.getString("CONTENT");
                            content1 = content1.replaceAll("<n>", "<br>");
                            content1 = content1.replaceAll("<r>", "\r");
                            jsonObject2.put("CONTENT",content1);
                        }
                        list.add(jsonObject2);
                    }

                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceIds",sdDevices.getAssociatedDeviceId());
            jsonObject.put("parameters", com.alibaba.fastjson.JSON.toJSON(list));
            return jsonObject;
        }else {
            return null;
        }
    }
}
