package com.tunnel.business.service.event.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DictTypeEnum;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdEventFlow;
import com.tunnel.business.mapper.event.SdEventFlowMapper;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.utils.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 事件管理Service业务层处理
 *
 * @author gongfanfei
 * @date 2020-08-24
 */
@Service
public class SdEventServiceImpl implements ISdEventService {
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
    public SdEvent selectSdEventById(Long id) {
        return sdEventMapper.selectSdEventById(id);
    }

    /**
     * 查询事件管理列表
     *
     * @param sdEvent 事件管理
     * @return 事件管理
     */
    @Override
    public List<SdEvent> selectSdEventList(SdEvent sdEvent) {
        if (SecurityUtils.getDeptId() != null && SecurityUtils.getDeptId() != 0L) {
            Long deptId = SecurityUtils.getDeptId();
            sdEvent.getParams().put("deptId", deptId);
        }
        return sdEventMapper.selectSdEventList(sdEvent);
    }

    /**
     * 新增事件管理
     *
     * @param sdEvent 事件管理
     * @return 结果
     */
    @Override
    public int insertSdEvent(SdEvent sdEvent) {
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
        if (result > -1) {
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
    public int updateSdEvent(SdEvent sdEvent) {
        if ("1".equals(sdEvent.getEventState())) {
            SdEventFlow eventFlow = new SdEventFlow();
            eventFlow.setEventId(sdEvent.getFlowId());
            eventFlow.setFlowTime(sdEvent.getEventTime());
            eventFlow.setFlowDescription("问题解决");
            eventFlow.setFlowHandler(SecurityUtils.getUsername());
            sdEventFlowMapper.insertSdEventFlow(eventFlow);
        }
        if ("2".equals(sdEvent.getEventState())) {
            SdEventFlow eventFlow = new SdEventFlow();
            eventFlow.setEventId(sdEvent.getFlowId());
            eventFlow.setFlowTime(sdEvent.getEventTime());
            eventFlow.setFlowDescription("问题忽略");
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
    public int deleteSdEventByIds(Long[] ids) {
        return sdEventMapper.deleteSdEventByIds(ids);
    }

    /**
     * 删除事件管理信息
     *
     * @param id 事件管理ID
     * @return 结果
     */
    @Override
    public int deleteSdEventById(Long id) {
        return sdEventMapper.deleteSdEventById(id);
    }


    /**
     * 根据id查询事件 ---视频
     *
     * @param id
     * @return
     */
    @Override
    public SdEvent getById(Long id) {
        return sdEventMapper.selectSdEventById(id);
    }

    /**
     * 预警事件查询全部
     *
     * @return
     */
    @Override
    public List<SdEvent> getEvent(SdEvent sdEvent) {
        Long deptId = SecurityUtils.getDeptId();
        sdEvent.getParams().put("deptId", deptId);
        return sdEventMapper.getEvent(sdEvent);
    }

    @Override
    public Map getTodayEventCount() {
        return sdEventMapper.getTodayEventCount();
    }

    /**
     * 根据事件id列表查询事件信息
     *
     * @param eventIdList 事件id列表
     * @return
     */
    @Override
    public List<SdEvent> getEventList(List<Long> eventIdList) {
        return sdEventMapper.getEventList(eventIdList);
    }

    /**
     * 拼接得到默认的事件标题
     * @param sdEvent 事件信息
     * @param tunnelMap 隧道名称Map
     * @param eventTypeMap 事件类型Map
     * @return
     */
    @Override
    public String getDefaultEventTitle(SdEvent sdEvent,Map<String,String> tunnelMap,Map<Long,String> eventTypeMap) {
        //拼接事件标题,格式：##隧道##方向桩号##发生##类型事件
        StringBuffer titleDesc = new StringBuffer();

        //隧道名称
        String tunnelName = tunnelMap.get(sdEvent.getTunnelId());
        titleDesc.append(tunnelName);

        //查询方向字典值
        String directionDict = "";
        if(!StringUtils.isEmpty(sdEvent.getDirection())){
            directionDict = DictUtils.getDictLabel(DictTypeEnum.sd_direction.getCode(), sdEvent.getDirection());
        }
        if(!StringUtils.isEmpty(directionDict)){
            titleDesc.append(directionDict);
        }
        if(!StringUtils.isEmpty(sdEvent.getStakeNum())){
            titleDesc.append("桩号").append(sdEvent.getStakeNum());
        }
        //事件类型名称
        String eventTypeName = eventTypeMap.get(sdEvent.getEventTypeId());
        titleDesc.append("发生").append(eventTypeName).append("事件");

        return titleDesc.toString();
    }
}
