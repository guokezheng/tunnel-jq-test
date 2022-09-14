package com.tunnel.platform.service.digitalmodel.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ImageUtil;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.domain.digitalmodel.*;
import com.tunnel.platform.domain.event.SdEvent;
import com.tunnel.platform.domain.event.SdRadarDetectData;
import com.tunnel.platform.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.platform.mapper.digitalmodel.RadarEventMapper;
import com.tunnel.platform.service.digitalmodel.RadarEventService;
import com.tunnel.platform.utils.constant.RadarEventConstants;
import com.zc.common.core.websocket.WebSocketService;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

/**
 * @author dzy
 * @date 2022/9/4 16:51
 */
@Service
public class RadarEventServiceImpl implements RadarEventService {

    @Autowired
    private RadarEventMapper wjMapper;

    @Autowired
    private SdDevicesMapper devicesMapper;

    @Autowired
    private RedisCache redisCache;

    private static final Logger log = LoggerFactory.getLogger(RadarEventServiceImpl.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${wj.imagePath}")
    private String picUrl;
    @Value("${wj.url}")
    private String prefix;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult insertWjEvent(Map<String, Object> map) {

        //获取异常事件列表
        String tunnelId = (String) map.get("tunnelId");
        JSON parse = JSONUtil.parse(map.get("b5eventList"));
        List<WjEvent> list = JSONUtil.toList(parse.toString(), WjEvent.class);
        List<SdEvent> eventList=new ArrayList<>();
        list.forEach(f->{
            SdEvent sdEvent=new SdEvent();
            Integer integer=wjMapper.selectID(f.getEventId());
            if (integer!=0){
                String eventType = WJEnum.getValue(f.getEventType());
                sdEvent.setEventTypeId(Long.parseLong(eventType));
                sdEvent.setTunnelId(tunnelId);
                sdEvent.setStationId(f.getStationId()+"");
                sdEvent.setStakeNum(f.getStakeNum());
                sdEvent.setLaneNo(f.getLaneNo()+"");
                sdEvent.setEventLongitude(f.getEventLongitude()+"");
                sdEvent.setEventLatitude(f.getEventLatitude()+"");
                sdEvent.setStartTime(f.getEventTimeStampStart());
                sdEvent.setEndTime(f.getEventTimeStampEnd());
                sdEvent.setId(f.getEventId());
                wjMapper.updateEvent(sdEvent);
                return;
            }else {
            sdEvent.setId(f.getEventId());
            String eventType = WJEnum.getValue(f.getEventType());
            sdEvent.setEventTypeId(Long.parseLong(eventType));
            sdEvent.setTunnelId(tunnelId);
            sdEvent.setStationId(f.getStationId()+"");
            sdEvent.setStakeNum(f.getStakeNum());
            sdEvent.setLaneNo(f.getLaneNo()+"");
            sdEvent.setEventLongitude(f.getEventLongitude()+"");
            sdEvent.setEventLatitude(f.getEventLatitude()+"");
            sdEvent.setStartTime(f.getEventTimeStampStart());
            sdEvent.setEndTime(f.getEventTimeStampEnd());
            eventList.add(sdEvent);
            List<WjConfidence> targetList = f.getTargetList();
            targetList.forEach(s->s.setEventIds(f.getEventId()));
            wjMapper.insertEventConfidence(targetList);
            }
        });
        if (CollectionUtils.isNotEmpty(eventList)){
            wjMapper.insertWjEvent(eventList);
            WebSocketService.broadcast("WjEvent",eventList);
        }
        return AjaxResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult uploadPic(Map<String, Object> map) {
        String eventId =map.get("eventId")+"";
        String videoImage = (String) map.get("videoImage");
        String secondVideoImage = (String) map.get("secondVideoImage");
        String thirdVideoImage = (String) map.get("thirdVideoImage");
        if (StringUtils.isNotBlank(eventId)){
            // 从缓存中获取文件存储路径
//            String fileServerPath = RuoYiConfig.getUploadPath();
            //新路径
//            String url = fileServerPath + picUrl+"/";
            if (StringUtils.isNotBlank(videoImage)) {
                String e1 ="事件前";
//                String imgUrl = ImageUtil.generateImage(videoImage, url,e1);
//                String s1 = this.picName(imgUrl);
                wjMapper.insertPic(eventId,prefix+videoImage,e1);
            }
            if (StringUtils.isNotEmpty(secondVideoImage)) {
                String e2 ="事件中";
//                String imgUrl = ImageUtil.generateImage(secondVideoImage, url,e2);
//                String s2 = this.picName(imgUrl);
                wjMapper.insertPic(eventId,prefix+secondVideoImage,e2);
            }
            if (StringUtils.isNotEmpty(thirdVideoImage)) {
                String e3 ="事件后";
//                String imgUrl = ImageUtil.generateImage(thirdVideoImage, url,e3);
//                String s3 = this.picName(imgUrl);
                wjMapper.insertPic(eventId,prefix+thirdVideoImage,e3);
            }
        }
        return AjaxResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult eventVideo(Map<String,Object> map) {
        String eventId =map.get("eventId")+"";
        String eventVideoUrl = (String) map.get("eventVideoUrl");
        if (StringUtils.isNotBlank(eventVideoUrl)){
            wjMapper.updateVideoById(Long.parseLong(eventId),prefix+eventVideoUrl);
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
        String id = map.get("id")+"";
        //隧道ID
        String tunelId = (String) map.get("tunelId");
        //车辆类型
        String originalType = map.get("originalType")+"";
        //车辆颜色
        String originalColor = map.get("originalColor")+"";
        //车牌号
        String picLicense = (String) map.get("picLicense");
        //车牌颜色
        String vehicleColor = map.get("vehicleColor")+"";
        wjMapper.insertVhc(recordSerialNumber,id,tunelId,originalType,originalColor,picLicense,vehicleColor);
        return AjaxResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertRadarDetect(Map<String, Object> map) throws ParseException{
        //检测时间，yyyy-MM-dd HH:mm:ss:SSS（可读性考虑）
        String timeStamp = (String) map.get("timeStamp");
        //隧道ID
        String tunnelId = (String) map.get("tunnelId");
        Date date = DateUtils.parseDate(timeStamp, "yyyy-MM-dd HH:mm:ss:SSS");
        JSON parse = JSONUtil.parse(map.get("participants"));
        List<WjParticipants> list = JSONUtil.toList(parse.toString(), WjParticipants.class);
        List<SdRadarDetectData> dataList=new ArrayList<>();
        list.forEach(
                f->{
                    SdRadarDetectData sdRadarDetectData=new SdRadarDetectData();
                    sdRadarDetectData.setTunnelId(tunnelId);
                    sdRadarDetectData.setDetectTime(date);
                    sdRadarDetectData.setRecordSerialNumber(f.getRecordSerialNumber());
                    sdRadarDetectData.setVehicleId(f.getId()+"");
                    sdRadarDetectData.setVehicleType(f.getOriginalType()+"");
                    sdRadarDetectData.setVehicleColor(f.getOriginalColor()+"");
                    sdRadarDetectData.setLongitude(f.getLongitude()+"");
                    sdRadarDetectData.setLatitude(f.getLatitude()+"");
                    sdRadarDetectData.setSpeed(f.getSpeed()+"");
                    sdRadarDetectData.setLaneNum(f.getLaneNum()+"");
                    sdRadarDetectData.setCourseAngle(f.getCourseAngle()+"");
                    sdRadarDetectData.setVehicleLicense(f.getPicLicense());
                    sdRadarDetectData.setLicenseColor(f.getVehicleColor()+"");
                    sdRadarDetectData.setStakeNum(f.getStakeNum());
                    dataList.add(sdRadarDetectData);
                });
        wjMapper.insertRadarDetect(dataList);
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
            wjDevicelidar->{
        List<SdDevices> devicesList=new ArrayList<>();
        //将相机的隧道id设备类型id放入集合
        wjDeviceCameraList.forEach(
            f->{
                SdDevices devices=new SdDevices();
                devices.setIp(f.getIp());
                devicesList.add(devices);
            }
        );
        //将雷达的隧道id设备类型id放入集合
        SdDevices devices = new SdDevices();
        devices.setIp(wjDevicelidar.getIp());
        devicesList.add(devices);
        //雷达设备类型
        Integer lidarType = wjDevicelidar.getDeviceType();
        //相机设备类型
        Integer cameraType = wjDeviceCameraList.get(0).getDeviceType();
        //遍历集合查出雷达、枪机设备ID 数据库
        List<SdDevices> list = devicesMapper.selectDeviceByTidEqtp(devicesList,tunnelId,lidarType,cameraType);
        //将雷达的状态放入相机集合
        WjDeviceCamera wjDeviceCamera=new WjDeviceCamera();
        wjDeviceCamera.setIp(wjDevicelidar.getIp());
        wjDeviceCamera.setDeviceType(wjDevicelidar.getDeviceType());
        wjDeviceCamera.setStatus(wjDevicelidar.getStatus());
        wjDeviceCameraList.add(wjDeviceCamera);
        for (int i = 0; i < list.size(); i++) {
            for (int t = 0; t < wjDeviceCameraList.size(); t++) {
                String eqType = list.get(i).getEqType()+"";
                if (list.get(i).getIp().equals(wjDeviceCameraList.get(t).getIp()) && eqType.equals(wjDeviceCameraList.get(t).getDeviceType()+"")){
                    //状态赋最新值
                    String s = wjDeviceCameraList.get(t).getStatus() + "";
                    list.get(i).setEqStatus(s);
                    //给传入数据赋eqId
                    wjDeviceCameraList.get(t).setEqId(list.get(i).getEqId());
                }
            }
        }
        //将设备状态保存到设备表
        devicesMapper.updateSdDevicesBatch(list);
        //赋值雷达eqId
        wjDevicelidar.setEqId(wjDeviceCameraList.get(wjDeviceCameraList.size()-1).getEqId());
        //存储后将雷达数据从相机数据剔除
        wjDeviceCameraList.remove(wjDeviceCameraList.size()-1);
        //雷达数据存redis
        redisCache.setCacheMapValue(RadarEventConstants.WJ_LIDAR_INFO_KEY, RadarEventConstants.WJ_LIDAR_INFO_KEY+wjDevicelidar.getEqId(),parse);
        //相机数据存redis
        wjDeviceCameraList.forEach(
            v->{
                JSON parse1 = JSONUtil.parse(v);
                redisCache.setCacheMapValue(RadarEventConstants.WJ_CAMERA_INFO_KEY, RadarEventConstants.WJ_CAMERA_INFO_KEY+v.getEqId(),parse1);
            }
        );
        });
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
        String deviceStatus= devicesMapper.selectEqStatus(deviceId);
        String tunnelId = devicesMapper.selecTunnelId(deviceId);
        if ("1".equals(deviceType) || "2".equals(deviceType) || "3".equals(deviceType) || "4".equals(deviceType)
                || "10".equals(deviceType) || "12".equals(deviceType) || "13".equals(deviceType) || "34".equals(deviceType)) {
            String runStatus = wjDeviceData.getRunStatus() + "";
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("tunnelId", tunnelId);
            jsonObject.put("deviceType", Integer.parseInt(deviceType));
            jsonObject.put("deviceStatus", Integer.parseInt(deviceStatus));
            jsonObject.put("runStatus", runStatus);
        }else if ("5".equals(deviceType)||"15".equals(deviceType)||"28".equals(deviceType)){
            Integer runDate = wjDeviceData.getRunDate();
            String unit = wjDeviceData.getUnit();
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("tunnelId", tunnelId);
            jsonObject.put("deviceType", Integer.parseInt(deviceType));
            jsonObject.put("deviceStatus", Integer.parseInt(deviceStatus));
            jsonObject.put("runDate",runDate);
            jsonObject.put("unit",unit);
        }else if ("6".equals(deviceType)||"7".equals(deviceType)||"8".equals(deviceType)||"9".equals(deviceType)){
            Integer runStatus = wjDeviceData.getRunStatus();
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("tunnelId", tunnelId);
            jsonObject.put("deviceType", Integer.parseInt(deviceType));
            jsonObject.put("deviceStatus", Integer.parseInt(deviceStatus));
            jsonObject.put("runStatus", runStatus);
        }else if ("31".equals(deviceType)){
            Integer runStatus = wjDeviceData.getRunStatus();
            Integer runMode = wjDeviceData.getRunMode();
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("tunnelId", tunnelId);
            jsonObject.put("deviceType", Integer.parseInt(deviceType));
            jsonObject.put("deviceStatus", Integer.parseInt(deviceStatus));
            jsonObject.put("runStatus",runStatus);
            jsonObject.put("runMode",runMode);
        }else if ("17".equals(deviceType)){
            Double windSpeed = wjDeviceData.getWindSpeed();
            String windDirection = wjDeviceData.getWindDirection();
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("tunnelId", tunnelId);
            jsonObject.put("deviceType", Integer.parseInt(deviceType));
            jsonObject.put("deviceStatus", Integer.parseInt(deviceStatus));
            jsonObject.put("windSpeed",windSpeed);
            jsonObject.put("windDirection",windDirection);
        }else if ("19".equals(deviceType)){
            Double co = wjDeviceData.getCO();
            Double vi = wjDeviceData.getVI();
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("tunnelId", tunnelId);
            jsonObject.put("deviceType", Integer.parseInt(deviceType));
            jsonObject.put("deviceStatus", Integer.parseInt(deviceStatus));
            jsonObject.put("co",co);
            jsonObject.put("vi",vi);
        }else if ("16".equals(deviceType)){
            String message = wjDeviceData.getMessage();
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("tunnelId", tunnelId);
            jsonObject.put("deviceType", Integer.parseInt(deviceType));
            jsonObject.put("deviceStatus", Integer.parseInt(deviceStatus));
            jsonObject.put("message",message);
        }
        log.info("-----测试测试测试----{}",jsonObject);
        kafkaTemplate.send(RadarEventConstants.BASEDEVICESTATUS, jsonObject.toString());
    }

    @Override
    public Object selectDevice(String tunnelId) {
        List<SdDevices> devices = devicesMapper.selectDevice(tunnelId);
        List<SdRadarDevice> list = new ArrayList<>();
        /*
        //数据结构为hash，使用Pipeline(管道)，组合命令，批量操作redis
        RedisTemplate redisTemplate = redisCache.redisTemplate;
        //返回储存的集合-json
        List<Object> redisResult = redisTemplate.executePipelined(
            new RedisCallback<String>() {
                // 自定义序列化
                RedisSerializer keyS = redisTemplate.getKeySerializer();
                @Override
                public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    for (SdDevices e : devices) {
                        redisConnection.hGet(keyS.serialize(RadarEventConstants.DEVICE_DATA), keyS.serialize(RadarEventConstants.DEVICE_DATA+e.getEqId()));
                    }
                    return null;
                }
            }, redisTemplate.getValueSerializer());
            */
        devices.forEach(
            f -> {
                SdRadarDevice sdRadarDevice = new SdRadarDevice();
                sdRadarDevice.setDeviceId(f.getEqId());
                sdRadarDevice.setDeviceType(f.getEqType() + "");
                sdRadarDevice.setDeviceName(f.getEqName());
                if (StringUtils.isNotEmpty(f.getEqStatus())){
                    sdRadarDevice.setDeviceStatus(Integer.parseInt(f.getEqStatus()));
                }
                if (StringUtils.isNotEmpty(f.getLng())){
                    sdRadarDevice.setLongitude(Double.parseDouble(f.getLng()));
                }
                if (StringUtils.isNotEmpty(f.getLat())){
                    sdRadarDevice.setLatitude(Double.parseDouble(f.getLat()));
                }
                sdRadarDevice.setDirection(f.getEqDirection());
                sdRadarDevice.setStakeNum(f.getPile());
                sdRadarDevice.setTransform(f.getRemark());
                WjDeviceData deviceData = new WjDeviceData();
                if ("1".equals(sdRadarDevice.getDeviceType()) || "2".equals(sdRadarDevice.getDeviceType()) || "3".equals(sdRadarDevice.getDeviceType()) || "4".equals(sdRadarDevice.getDeviceType())
                        || "10".equals(sdRadarDevice.getDeviceType()) || "12".equals(sdRadarDevice.getDeviceType()) || "13".equals(sdRadarDevice.getDeviceType()) || "34".equals(sdRadarDevice.getDeviceType())) {
                    /*redisResult.forEach(
                        v->{
                            if (v!=null){
                                if (v.toString().contains(sdRadarDevice.getDeviceId())){
                                    JSON parse = JSONUtil.parse(v);
                                    WjDeviceData wjDeviceData = JSONUtil.toBean(parse.toString(), WjDeviceData.class);
                                    deviceData.setRunStatus(wjDeviceData.getRunStatus());
                                }
                            }
                        }
                    );*/
                    /**
                     * 从sd_state_storage表中查询deviceType=* 字段独有的runStatus
                     */
                    List<SdDevices> devicesList = devicesMapper.selectStateStorage();
                    devicesList.forEach(
                            v->{
                                if (f.getEqId().equals(v.getEqId())){
                                    deviceData.setRunStatus(Integer.parseInt(v.getEqStatus()));
                                }
                            }
                    );
                }else if ("5".equals(sdRadarDevice.getDeviceType())||"15".equals(sdRadarDevice.getDeviceType())||"28".equals(sdRadarDevice.getDeviceType())){
                    /*redisResult.forEach(
                            v->{
                                if (v!=null){
                                    if (v.toString().contains(sdRadarDevice.getDeviceId())){
                                        JSON parse = JSONUtil.parse(v);
                                        WjDeviceData wjDeviceData = JSONUtil.toBean(parse.toString(), WjDeviceData.class);
                                        deviceData.setRunDate(wjDeviceData.getRunDate());
                                        deviceData.setUnit(wjDeviceData.getUnit());
                                    }
                                }
                            }
                    );*/
                    SdDeviceDataItem sdDeviceDataItem = devicesMapper.selectDataUnit(f.getEqId());

                }else if ("6".equals(sdRadarDevice.getDeviceType())||"7".equals(sdRadarDevice.getDeviceType())||"8".equals(sdRadarDevice.getDeviceType())||"9".equals(sdRadarDevice.getDeviceType())){
                   /* redisResult.forEach(
                        v->{
                            if (v!=null){
                                if (v.toString().contains(sdRadarDevice.getDeviceId())){
                                    JSON parse = JSONUtil.parse(v);
                                    WjDeviceData wjDeviceData = JSONUtil.toBean(parse.toString(), WjDeviceData.class);
                                    deviceData.setRunStatus(wjDeviceData.getRunStatus());
                                }
                            }
                        }
                    );*/
                    /**
                     * 从sd_state_storage表中查询deviceType=* 字段独有的runStatus
                     */
                    List<SdDevices> devicesList = devicesMapper.selectStateStorage();
                    devicesList.forEach(
                            v->{
                                if (f.getEqId().equals(v.getEqId())){
                                    deviceData.setRunStatus(Integer.parseInt(v.getEqStatus()));
                                }
                            }
                    );
                }else if ("31".equals(sdRadarDevice.getDeviceType())){
                   /* redisResult.forEach(
                            v->{
                                if (v!=null){
                                    if (v.toString().contains(sdRadarDevice.getDeviceId())){
                                        JSON parse = JSONUtil.parse(v);
                                        WjDeviceData wjDeviceData = JSONUtil.toBean(parse.toString(), WjDeviceData.class);
                                        deviceData.setRunStatus(wjDeviceData.getRunStatus());
                                        deviceData.setRunMode(wjDeviceData.getRunMode());
                                    }
                                }
                            }
                    );*/
                }else if ("17".equals(sdRadarDevice.getDeviceType())){
                    /*redisResult.forEach(
                        v->{
                            if (v!=null){
                                if (v.toString().contains(sdRadarDevice.getDeviceId())){
                                    JSON parse = JSONUtil.parse(v);
                                    WjDeviceData wjDeviceData = JSONUtil.toBean(parse.toString(), WjDeviceData.class);
                                    deviceData.setWindSpeed(wjDeviceData.getWindSpeed());
                                    deviceData.setWindDirection(wjDeviceData.getWindDirection());
                                }
                            }
                        }
                    );*/
                }else if ("19".equals(sdRadarDevice.getDeviceType())){
                    /*redisResult.forEach(
                        v->{
                            if (v!=null){
                                if (v.toString().contains(sdRadarDevice.getDeviceId())){
                                    JSON parse = JSONUtil.parse(v);
                                    WjDeviceData wjDeviceData = JSONUtil.toBean(parse.toString(), WjDeviceData.class);
                                    deviceData.setCO(wjDeviceData.getCO());
                                    deviceData.setVI(wjDeviceData.getCO());
                                }
                            }
                        }
                    );*/
                }else if ("16".equals(sdRadarDevice.getDeviceType())){
                    /*redisResult.forEach(
                        v->{
                            if (v!=null){
                                if (v.toString().contains(sdRadarDevice.getDeviceId())){
                                    JSON parse = JSONUtil.parse(v);
                                    WjDeviceData wjDeviceData = JSONUtil.toBean(parse.toString(), WjDeviceData.class);
                                    deviceData.setMessage(wjDeviceData.getMessage());
                                }
                            }
                        }
                    );*/
                }
                sdRadarDevice.setDeviceData(deviceData);
                list.add(sdRadarDevice);
            }
        );
        //在线
        int normal = 0;
        //离线
        int errorNum = 0;
        //故障
        int offlineNum = 0;
        for (SdRadarDevice sdRadarDevice : list) {
            if (sdRadarDevice.getDeviceStatus()!=null){
                if (sdRadarDevice.getDeviceStatus()==1){
                    normal+=1;
                }else if (sdRadarDevice.getDeviceStatus()==2){
                    errorNum+=1;
                }else if (sdRadarDevice.getDeviceStatus()==3){
                    offlineNum+=1;
                }
            }
        }
        JSONObject object=new JSONObject();
        object.put("normal",normal);
        object.put("errorNum",errorNum);
        object.put("offlineNum",offlineNum);
        object.put("deviceList",list);
        return JSONUtil.parse(object);
    }


    private String picName(String urlName){
        String[] split = urlName.split("/");
        String s = split[split.length - 1];
        return s;
    }
}
