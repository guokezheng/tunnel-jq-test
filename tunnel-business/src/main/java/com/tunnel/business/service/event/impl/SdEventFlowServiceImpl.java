package com.tunnel.business.service.event.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DictTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.EventDescEnum;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.event.*;
import com.tunnel.business.mapper.event.SdEventFlowMapper;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.event.ISdEventFlowService;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.event.ISdEventTypeService;
import com.tunnel.business.service.event.ISdReservePlanService;
import com.tunnel.business.utils.json.JSONObject;
import com.zc.common.core.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 事件处理流程Service业务层处理
 *
 * @author gongfanfei
 * @date 2020-09-17
 */
@Service
public class SdEventFlowServiceImpl implements ISdEventFlowService {
    @Autowired
    private SdEventFlowMapper sdEventFlowMapper;

    @Autowired
    private ISdTunnelsService tunnelsService;

    @Autowired
    private ISdEventTypeService eventTypeService;

    @Autowired
    private ISdReservePlanService reservePlanService;

    @Autowired
    private ISdEventService eventService;

    /**
     * 查询事件处理流程
     *
     * @param id 事件处理流程ID
     * @return 事件处理流程
     */
    @Override
    public SdEventFlow selectSdEventFlowById(Long id) {
        return sdEventFlowMapper.selectSdEventFlowById(id);
    }

    /**
     * 查询事件处理流程列表
     *
     * @param sdEventFlow 事件处理流程
     * @return 事件处理流程
     */
    @Override
    public List<SdEventFlow> selectSdEventFlowList(SdEventFlow sdEventFlow) {
        return sdEventFlowMapper.selectSdEventFlowList(sdEventFlow);
    }

    /**
     * 执行预案保存事件处理流程记录
     * @param eventId
     * @param data
     * @return
     */
    @Override
    public int execPlanSaveEventFlow(String eventId, Map data) {
        List<String> strategyList = (List)data.get("strategy");
        StringBuffer buffer = new StringBuffer();
        String strategyNames = strategyList.stream().collect(Collectors.joining("、"));
        buffer.append("执行了").append(data.get("planName")).append("：").append(strategyNames);
        SdEventFlow flow = new SdEventFlow();
        flow.setFlowDescription(buffer.toString());
        flow.setEventId(eventId);
        flow.setFlowTime(DateUtils.getNowDate());
        flow.setFlowHandler(SecurityUtils.getUsername());
        JSONObject json = new JSONObject();
        json.put("eventFlow",flow);
        WebSocketService.broadcast("eventFlow",json);
        return sdEventFlowMapper.insertSdEventFlow(flow);
    }
    /**
     * 确认事件
     * @param eventId
     * @return
     */
    @Override
    public int saveUserConfirmFlow(String eventId) {
//        StringBuffer buffer = new StringBuffer();
//        buffer.append("用户").append(SecurityUtils.getUsername()).append("确认了该事件");
        SdEventFlow flow = new SdEventFlow();
        flow.setFlowDescription("用户确认了该事件");
        flow.setEventId(eventId);
        flow.setFlowTime(DateUtils.getNowDate());
        flow.setFlowHandler(SecurityUtils.getUsername());
        JSONObject json = new JSONObject();
        json.put("eventFlow",flow);
        WebSocketService.broadcast("eventFlow",json);
        return sdEventFlowMapper.insertSdEventFlow(flow);
    }

    /**
     * 新增事件处理流程
     *
     * @param sdEventFlow 事件处理流程
     * @return 结果
     */
    @Override
    public int insertSdEventFlow(SdEventFlow sdEventFlow) {

        sdEventFlow.setFlowHandler(SecurityUtils.getUsername());
        sdEventFlow.setFlowTime(DateUtils.getNowDate());
        return sdEventFlowMapper.insertSdEventFlow(sdEventFlow);
    }

    /**
     * 修改事件处理流程
     *
     * @param sdEventFlow 事件处理流程
     * @return 结果
     */
    @Override
    public int updateSdEventFlow(SdEventFlow sdEventFlow) {
        return sdEventFlowMapper.updateSdEventFlow(sdEventFlow);
    }

    /**
     * 批量删除事件处理流程
     *
     * @param ids 需要删除的事件处理流程ID
     * @return 结果
     */
    @Override
    public int deleteSdEventFlowByIds(Long[] ids) {
        return sdEventFlowMapper.deleteSdEventFlowByIds(ids);
    }

    /**
     * 删除事件处理流程信息
     *
     * @param id 事件处理流程ID
     * @return 结果
     */
    @Override
    public int deleteSdEventFlowById(Long id) {
        return sdEventFlowMapper.deleteSdEventFlowById(id);
    }

    /**
     * 添加事件流程记录--批量
     *
     * @param list 事件列表信息

     * @return
     */
    @Override
    public void addEventFlowBatch(List<SdEvent> list) {
        for(SdEvent sdEvent : list){
            addEventStartFlow(sdEvent);
        }
    }

    /**
     * 添加事件开始-事件流程记录
     *
     * @param sdEvent 事件信息
     * @return
     */
    @Override
    public int addEventStartFlow(SdEvent sdEvent) {
//        //隧道名称
//        String tunnelName = tunnelMap.get(sdEvent.getTunnelId());
//        String direction = sdEvent.getDirection();
//        //查询方向字典值
//        String directionDict = "";
//        if(!StringUtils.isEmpty(direction)){
//            directionDict = DictUtils.getDictLabel(DictTypeEnum.sd_direction.getCode(), sdEvent.getDirection());
//        }
//
//        //事件类型名称
//        String eventTypeName = eventTypeMap.get(sdEvent.getEventTypeId());
        SdEventFlow eventFlow = new SdEventFlow();
        eventFlow.setEventId(String.valueOf(sdEvent.getId()));
        eventFlow.setFlowTime(DateUtils.getNowDate());

        String eventSource = sdEvent.getEventSource();
        String eventSourceDesc = EventDescEnum.getName(eventSource);

        //拼接事件流程描述
        StringBuffer desc = new StringBuffer();
        if (!StringUtils.isEmpty(eventSourceDesc)) {
            desc.append(eventSourceDesc);
        }
        if(!StringUtils.isEmpty(sdEvent.getEventTitle())){
            desc.append(sdEvent.getEventTitle());
        }

//        if(!StringUtils.isEmpty(directionDict)){
//            desc.append(directionDict);
//        }
//        if(!StringUtils.isEmpty(sdEvent.getStakeNum())){
//            desc.append("桩号").append(sdEvent.getStakeNum());
//        }
//        desc.append("发生").append(eventTypeName).append("事件");
        //###隧道##方向桩号##发生###事件
        eventFlow.setFlowDescription(desc.toString());

        int row = sdEventFlowMapper.insertSdEventFlow(eventFlow);
        return row;
    }

    /**
     * 添加事件结束-事件流程记录
     *
     * @param sdEvent 事件信息
     * @return
     */
    @Override
    public int addEventEndFlow(SdEvent sdEvent) {
        SdEventFlow eventFlow = new SdEventFlow();
        eventFlow.setEventId(sdEvent.getFlowId());
        eventFlow.setFlowDescription(EventDescEnum.event_end.getName());
        eventFlow.setFlowTime(DateUtils.getNowDate());
        eventFlow.setFlowHandler(SecurityUtils.getUsername());
        int row = sdEventFlowMapper.insertSdEventFlow(eventFlow);
        return row;
    }

    /**
     * 事件忽略-事件流程记录
     *
     * @param sdEvent 事件信息
     * @return
     */
    @Override
    public int addEventIgnoreFlow(SdEvent sdEvent) {
        SdEventFlow eventFlow = new SdEventFlow();
        eventFlow.setEventId(sdEvent.getFlowId());
        eventFlow.setFlowDescription(EventDescEnum.event_ignore.getName());
        eventFlow.setFlowTime(DateUtils.getNowDate());
        eventFlow.setFlowHandler(SecurityUtils.getUsername());
        int row = sdEventFlowMapper.insertSdEventFlow(eventFlow);
        return row;
    }

    /**
     * 事件处理-事件流程记录
     *
     * @param sdEvent 事件信息
     * @return
     */
    @Override
    public int addEventHandleFlow(SdEvent sdEvent) {
        SdEventFlow eventFlow = new SdEventFlow();
        eventFlow.setEventId(sdEvent.getFlowId());
        eventFlow.setFlowDescription(EventDescEnum.event_handle.getName());
        eventFlow.setFlowTime(DateUtils.getNowDate());
        eventFlow.setFlowHandler(SecurityUtils.getUsername());
        int row = sdEventFlowMapper.insertSdEventFlow(eventFlow);
        return row;
    }

    /**
     * 更新事件信息/事件修改-事件流程记录
     *
     * @param sdEvent 事件信息
     * @return
     */
    @Override
    public int addEventUpdateFlow(SdEvent sdEvent) {
        SdEventFlow eventFlow = new SdEventFlow();
        eventFlow.setEventId(sdEvent.getFlowId());
        eventFlow.setFlowDescription(EventDescEnum.event_update.getName());
        eventFlow.setFlowTime(DateUtils.getNowDate());
        eventFlow.setFlowHandler(SecurityUtils.getUsername());
        int row = sdEventFlowMapper.insertSdEventFlow(eventFlow);
        return row;
    }

    /**
     * 拨打电话-事件流程记录
     *
     * @param sdEvent        事件信息
     * @param sdEmergencyPer 应急人员
     * @return
     */
    @Override
    public int addEventPhoneFlow(SdEvent sdEvent, SdEmergencyPer sdEmergencyPer) {
        //拼接事件流程描述
        StringBuffer desc = new StringBuffer();
        desc.append(EventDescEnum.event_phone.getName()).append(sdEmergencyPer.getUserName()).append(" ").append(sdEmergencyPer.getPhone());
        SdEventFlow eventFlow = new SdEventFlow();
        eventFlow.setEventId(sdEvent.getFlowId());
        eventFlow.setFlowDescription(desc.toString());
        eventFlow.setFlowTime(DateUtils.getNowDate());
        eventFlow.setFlowHandler(SecurityUtils.getUsername());
        int row = sdEventFlowMapper.insertSdEventFlow(eventFlow);
        return row;
    }

    /**
     * 执行预案-事件流程记录
     *
     * @param sdEvent     事件信息
     * @param reservePlan 预案信息
     * @return
     */
    @Override
    public int addEventPlanFlow(SdEvent sdEvent, SdReservePlan reservePlan) {
        //拼接事件流程描述
        StringBuffer desc = new StringBuffer();
        desc.append(EventDescEnum.event_plan.getName()).append(reservePlan.getPlanName());
        SdEventFlow eventFlow = new SdEventFlow();
        eventFlow.setEventId(sdEvent.getFlowId());
        eventFlow.setFlowDescription(desc.toString());
        eventFlow.setFlowTime(DateUtils.getNowDate());
        eventFlow.setFlowHandler(SecurityUtils.getUsername());
        int row = sdEventFlowMapper.insertSdEventFlow(eventFlow);
        return row;
    }

}
