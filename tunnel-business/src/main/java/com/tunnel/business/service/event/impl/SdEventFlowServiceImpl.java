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
import com.tunnel.business.service.event.ISdEventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * 添加事件开始-事件流程记录
     *
     * @param sdEvent 事件信息
     * @return
     */
    @Override
    public int addEventStartFlow(SdEvent sdEvent, String source) {
        SdTunnels tunnel = tunnelsService.selectSdTunnelsById(sdEvent.getTunnelId());
        //查询方向字典值
        String direction = DictUtils.getDictLabel(DictTypeEnum.sd_direction.getCode(), sdEvent.getDirection());
        //查询事件类型
        SdEventType sdEventType = eventTypeService.selectSdEventTypeById(sdEvent.getEventTypeId());
        String eventTypeName = sdEventType.getEventType();
        SdEventFlow eventFlow = new SdEventFlow();
        eventFlow.setEventId(sdEvent.getFlowId());
        eventFlow.setFlowTime(sdEvent.getEventTime());
        //拼接事件流程描述
        StringBuffer desc = new StringBuffer();
        if (!StringUtils.isEmpty(source)) {
            desc.append(source);
        }
        desc.append(tunnel.getTunnelName()).append(sdEvent.getStakeNum()).append(direction)
                .append("发生").append(eventTypeName).append("事件.");
        //###隧道桩号##方向发生###事件
        eventFlow.setFlowDescription(desc.toString());
        eventFlow.setFlowHandler(SecurityUtils.getUsername());
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
