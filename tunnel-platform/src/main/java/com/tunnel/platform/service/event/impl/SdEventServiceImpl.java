package com.tunnel.platform.service.event.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.ImageUtil;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.platform.domain.digitalmodel.WjConfidence;
import com.tunnel.platform.domain.digitalmodel.WjEvent;
import com.tunnel.platform.domain.event.SdEvent;
import com.tunnel.platform.domain.event.SdEventFlow;
import com.tunnel.platform.mapper.event.SdEventFlowMapper;
import com.tunnel.platform.mapper.event.SdEventMapper;
import com.tunnel.platform.service.event.ISdEventService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.platform.utils.util.UUIDUtil;
import com.zc.common.core.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 事件管理Service业务层处理
 * 
 * @author gongfanfei
 * @date 2020-08-24
 */
@Service
public class SdEventServiceImpl implements ISdEventService 
{
    @Autowired
    private SdEventMapper sdEventMapper;

    @Autowired
    private SdEventFlowMapper sdEventFlowMapper;

    @Value("${wj.imagePath}")
    private String picUrl;

    /**
     * 查询事件管理
     * 
     * @param id 事件管理ID
     * @return 事件管理
     */
    @Override
    public SdEvent selectSdEventById(Long id)
    {
        return sdEventMapper.selectSdEventById(id);
    }

    /**
     * 查询事件管理列表
     * 
     * @param sdEvent 事件管理
     * @return 事件管理
     */
    @Override
    public List<SdEvent> selectSdEventList(SdEvent sdEvent)
    {
        return sdEventMapper.selectSdEventList(sdEvent);
    }

    /**
     * 新增事件管理
     * 
     * @param sdEvent 事件管理
     * @return 结果
     */
    @Override
    public int insertSdEvent(SdEvent sdEvent)
    {
    	String guid = UUIDUtil.getRandom32BeginTimePK();
        sdEvent.setCreateTime(DateUtils.getNowDate());
        sdEvent.setFlowId(guid);
        //添加事件处理流程 ：开始
        SdEventFlow eventFlow = new SdEventFlow();
        eventFlow.setEventId(guid);
        eventFlow.setFlowTime(sdEvent.getEventTime());
        eventFlow.setFlowDescription(sdEvent.getEventDescription());
        eventFlow.setFlowHandler(SecurityUtils.getUsername());
        int result = sdEventFlowMapper.insertSdEventFlow(eventFlow);
        if(result > -1){
        	result = sdEventMapper.insertSdEvent(sdEvent);
        }
        return result;
    }

    /**
     * 修改事件管理
     * 
     * @param sdEvent 事件管理
     * @return 结果
     */
    @Override
    public int updateSdEvent(SdEvent sdEvent)
    {
    	
    	if("1".equals(sdEvent.getEventState())){
    		SdEventFlow eventFlow = new SdEventFlow();
            eventFlow.setEventId(sdEvent.getFlowId());
            eventFlow.setFlowTime(sdEvent.getEventTime());
            eventFlow.setFlowDescription("问题解决");
            eventFlow.setFlowHandler(SecurityUtils.getUsername());
            sdEventFlowMapper.insertSdEventFlow(eventFlow);
    	}
        sdEvent.setUpdateTime(DateUtils.getNowDate());
        return sdEventMapper.updateSdEvent(sdEvent);
    }

    /**
     * 批量删除事件管理
     * 
     * @param ids 需要删除的事件管理ID
     * @return 结果
     */
    @Override
    public int deleteSdEventByIds(Long[] ids)
    {
        return sdEventMapper.deleteSdEventByIds(ids);
    }

    /**
     * 删除事件管理信息
     * 
     * @param id 事件管理ID
     * @return 结果
     */
    @Override
    public int deleteSdEventById(Long id)
    {
        return sdEventMapper.deleteSdEventById(id);
    }

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
            sdEventMapper.insertEventConfidence(targetList);
        });
        sdEventMapper.insertWjEvent(eventList);
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
                sdEventMapper.insertPic(eventId,imgUrl,s1);
            }
            if (StringUtils.isNotEmpty(secondVideoImage)) {
                String e2 ="事件中";
                String imgUrl = ImageUtil.generateImage(secondVideoImage, url,e2);
                String s2 = this.picName(imgUrl);
                sdEventMapper.insertPic(eventId,imgUrl,s2);
            }
            if (StringUtils.isNotEmpty(thirdVideoImage)) {
                String e3 ="事件后";
                String imgUrl = ImageUtil.generateImage(thirdVideoImage, url,e3);
                String s3 = this.picName(imgUrl);
                sdEventMapper.insertPic(eventId,imgUrl,s3);
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
            sdEventMapper.updateVideoById(Long.parseLong(eventId),eventVideoUrl);
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
        sdEventMapper.insertVhc(recordSerialNumber,id,tunelId,originalType,originalColor,picLicense,vehicleColor);
        return AjaxResult.success();
    }

    private String picName(String urlName){
        String[] split = urlName.split("/");
        String s = split[split.length - 1];
        return s;
    }
}