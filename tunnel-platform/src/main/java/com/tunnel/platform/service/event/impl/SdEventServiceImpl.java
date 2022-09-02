package com.tunnel.platform.service.event.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.AjaxResult;
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
import org.springframework.beans.factory.annotation.Autowired;
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
        JSON parse = JSONUtil.parse(map.get("b5eventList"));
        List<WjEvent> list = JSONUtil.toList(parse.toString(), WjEvent.class);
        List<SdEvent> eventList=new ArrayList<>();
        list.forEach(f->{
            SdEvent sdEvent=new SdEvent();
            if (StringUtils.isNotBlank(f.getEventId()+"")){
                sdEvent.setId(f.getEventId());
            }
            if (StringUtils.isNotBlank(f.getEventType()+"")){
                Byte eventType = f.getEventType();
                sdEvent.setEventTypeId(eventType.longValue());
            }
            if (StringUtils.isNotBlank(f.getStationId()+"")){
                sdEvent.setTunnelId(f.getStationId()+"");
            }
            if (StringUtils.isNotBlank(f.getLaneNo()+"")){
                sdEvent.setLaneNo(f.getLaneNo()+"");
            }
            if (StringUtils.isNotBlank(f.getEventLongitude()+"")){
                sdEvent.setEventLongitude(f.getEventLongitude()+"");
            }
            if (StringUtils.isNotBlank(f.getEventLatitude()+"")){
                sdEvent.setEventLatitude(f.getEventLatitude()+"");
            }
            if (StringUtils.isNotBlank(f.getEventTimeStampStart())){
                sdEvent.setStartTime(f.getEventTimeStampStart());
            }
            if (StringUtils.isNotBlank(f.getEventTimeStampEnd())){
                sdEvent.setEndTime(f.getEventTimeStampEnd());
            }
            eventList.add(sdEvent);
            List<WjConfidence> targetList = f.getTargetList();
            targetList.forEach(s->s.setEventIds(f.getEventId()));
            sdEventMapper.insertEventConfidence(targetList);
        });
        sdEventMapper.insertWjEvent(eventList);
        return AjaxResult.success();
    }
}