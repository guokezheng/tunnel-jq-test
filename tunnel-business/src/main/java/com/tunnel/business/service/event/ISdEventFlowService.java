package com.tunnel.business.service.event;

import com.tunnel.business.domain.event.SdEmergencyPer;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdEventFlow;
import com.tunnel.business.domain.event.SdReservePlan;

import java.util.List;
import java.util.Map;

/**
 * 事件处理流程Service接口
 *
 * @author gongfanfei
 * @date 2020-09-17
 */
public interface ISdEventFlowService {
    /**
     * 查询事件处理流程
     *
     * @param id 事件处理流程ID
     * @return 事件处理流程
     */
    SdEventFlow selectSdEventFlowById(Long id);

    /**
     * 查询事件处理流程列表
     *
     * @param sdEventFlow 事件处理流程
     * @return 事件处理流程集合
     */
    List<SdEventFlow> selectSdEventFlowList(SdEventFlow sdEventFlow);

    /**
     * 执行预案保存流程记录
     * @return
     */
    int execPlanSaveEventFlow(String eventId, Map data);
    /**
     * 新增事件处理流程
     *
     * @param sdEventFlow 事件处理流程
     * @return 结果
     */
    int insertSdEventFlow(SdEventFlow sdEventFlow);

    /**
     * 修改事件处理流程
     *
     * @param sdEventFlow 事件处理流程
     * @return 结果
     */
    int updateSdEventFlow(SdEventFlow sdEventFlow);

    /**
     * 批量删除事件处理流程
     *
     * @param ids 需要删除的事件处理流程ID
     * @return 结果
     */
    int deleteSdEventFlowByIds(Long[] ids);

    /**
     * 删除事件处理流程信息
     *
     * @param id 事件处理流程ID
     * @return 结果
     */
    int deleteSdEventFlowById(Long id);


    /**
     * 添加事件流程记录--批量
     * @param list 事件列表信息
     * @return
     */
    void addEventFlowBatch(List<SdEvent> list);

    /**
     * 事件开始/事件新增-事件流程记录
     *
     * @param sdEvent 事件信息
     * @return
     */
    int addEventStartFlow(SdEvent sdEvent);

    /**
     * 事件结束-事件流程记录
     *
     * @param sdEvent 事件信息
     * @return
     */
    int addEventEndFlow(SdEvent sdEvent);


    /**
     * 事件忽略-事件流程记录
     *
     * @param sdEvent 事件信息
     * @return
     */
    int addEventIgnoreFlow(SdEvent sdEvent);

    /**
     * 事件处理-事件流程记录
     *
     * @param sdEvent 事件信息
     * @return
     */
    int addEventHandleFlow(SdEvent sdEvent);


    /**
     * 更新事件信息/事件修改-事件流程记录
     *
     * @param sdEvent 事件信息
     * @return
     */
    int addEventUpdateFlow(SdEvent sdEvent);


    /**
     * 拨打电话-事件流程记录
     *
     * @param sdEvent        事件信息
     * @param sdEmergencyPer 应急人员
     * @return
     */
    int addEventPhoneFlow(SdEvent sdEvent, SdEmergencyPer sdEmergencyPer);


    /**
     * 执行预案-事件流程记录
     *
     * @param sdEvent     事件信息
     * @param reservePlan 预案信息
     * @return
     */
    int addEventPlanFlow(SdEvent sdEvent, SdReservePlan reservePlan);
}
