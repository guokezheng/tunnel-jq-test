package com.tunnel.platform.service.digitalmodel.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ImageUtil;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.platform.domain.digitalmodel.WjConfidence;
import com.tunnel.platform.domain.digitalmodel.WjEvent;
import com.tunnel.platform.domain.digitalmodel.WjParticipants;
import com.tunnel.platform.domain.event.SdEvent;
import com.tunnel.platform.domain.event.SdRadarDetectData;
import com.tunnel.platform.mapper.digitalmodel.WjMapper;
import com.tunnel.platform.service.digitalmodel.WjService;
import com.zc.common.core.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author dzy
 * @date 2022/9/4 16:51
 */
public class WjServiceImpl implements WjService {

    @Autowired
    private WjMapper wjMapper;

    @Value("${wj.imagePath}")
    private String picUrl;

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
            sdEvent.setId(f.getEventId());
            Byte eventType = f.getEventType();
            sdEvent.setEventTypeId(eventType.longValue());
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
        });
        wjMapper.insertWjEvent(eventList);
        WebSocketService.broadcast("WjEvent",eventList);
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
            String fileServerPath = RuoYiConfig.getUploadPath();
            //新路径
            String url = fileServerPath + picUrl+"/";
            if (StringUtils.isNotBlank(videoImage)) {
                String e1 ="事件前";
                String imgUrl = ImageUtil.generateImage(videoImage, url,e1);
                String s1 = this.picName(imgUrl);
                wjMapper.insertPic(eventId,imgUrl,s1);
            }
            if (StringUtils.isNotEmpty(secondVideoImage)) {
                String e2 ="事件中";
                String imgUrl = ImageUtil.generateImage(secondVideoImage, url,e2);
                String s2 = this.picName(imgUrl);
                wjMapper.insertPic(eventId,imgUrl,s2);
            }
            if (StringUtils.isNotEmpty(thirdVideoImage)) {
                String e3 ="事件后";
                String imgUrl = ImageUtil.generateImage(thirdVideoImage, url,e3);
                String s3 = this.picName(imgUrl);
                wjMapper.insertPic(eventId,imgUrl,s3);
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
            wjMapper.updateVideoById(Long.parseLong(eventId),eventVideoUrl);
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
        Date date = DateUtils.parseDate(timeStamp, "yyyy-MM-dd HH:mm:ss.SSS");
        JSON parse = JSONUtil.parse(map.get("participants"));
        List<WjParticipants> list = JSONUtil.toList(parse.toString(), WjParticipants.class);
        List<SdRadarDetectData> dataList=new ArrayList<>();
        list.forEach(
                f->{
                    SdRadarDetectData sdRadarDetectData=new SdRadarDetectData();
                    sdRadarDetectData.setTunnelId(tunnelId);
                    sdRadarDetectData.setDetectTime(date);
                    sdRadarDetectData.setId(f.getRecordSerialNumber());
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

        Object lidarInfo = map.get("lidarInfo");
    }

    private String picName(String urlName){
        String[] split = urlName.split("/");
        String s = split[split.length - 1];
        return s;
    }
}
