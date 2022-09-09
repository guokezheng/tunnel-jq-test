package com.tunnel.platform.service.event;

import com.tunnel.platform.domain.event.SdEmergencyPer;
import com.tunnel.platform.domain.event.SdEvent;
import com.tunnel.platform.domain.event.SdEventFlow;
import com.tunnel.platform.domain.event.SdReservePlan;

import java.util.List;

/**
 * 事件处理流程Service接口
 *
 * @author gongfanfei
 * @date 2020-09-17
 */
public interface ISdEventFlowService
{
    /**
     * 查询事件处理流程
     *
     * @param id 事件处理流程ID
     * @return 事件处理流程
     */
    public SdEventFlow selectSdEventFlowById(Long id);

    /**
     * 查询事件处理流程列表
     *
     * @param sdEventFlow 事件处理流程
     * @return 事件处理流程集合
     */
    public List<SdEventFlow> selectSdEventFlowList(SdEventFlow sdEventFlow);

    /**
     * 新增事件处理流程
     *
     * @param sdEventFlow 事件处理流程
     * @return 结果
     */
    public int insertSdEventFlow(SdEventFlow sdEventFlow);

    /**
     * 修改事件处理流程
     *
     * @param sdEventFlow 事件处理流程
     * @return 结果
     */
    public int updateSdEventFlow(SdEventFlow sdEventFlow);

    /**
     * 批量删除事件处理流程
     *
     * @param ids 需要删除的事件处理流程ID
     * @return 结果
     */
    public int deleteSdEventFlowByIds(Long[] ids);

    /**
     * 删除事件处理流程信息
     *
     * @param id 事件处理流程ID
     * @return 结果
     */
    public int deleteSdEventFlowById(Long id);


    /**
     * 事件开始/事件新增-事件流程记录
     * @param sdEvent 事件信息
     * @param source 事件来源
     * @return
     */
    int addEventStartFlow(SdEvent sdEvent,String source);

    /**
     * 事件结束-事件流程记录
     * @param sdEvent 事件信息
     * @return
     */
    int addEventEndFlow(SdEvent sdEvent);


    /**
     * 事件忽略-事件流程记录
     * @param sdEvent 事件信息
     * @return
     */
    int addEventIgnoreFlow(SdEvent sdEvent);

    /**
     * 事件处理-事件流程记录
     * @param sdEvent 事件信息
     * @return
     */
    int addEventHandleFlow(SdEvent sdEvent);


    /**
     * 更新事件信息/事件修改-事件流程记录
     * @param sdEvent 事件信息
     * @return
     */
    int addEventUpdateFlow(SdEvent sdEvent);


    /**
     * 拨打电话-事件流程记录
     * @param sdEvent 事件信息
     * @param sdEmergencyPer 应急人员
     * @return
     */
    int addEventPhoneFlow(SdEvent sdEvent, SdEmergencyPer sdEmergencyPer);


    /**
     * 执行预案-事件流程记录
     * @param sdEvent 事件信息
     * @param reservePlan 预案信息
     * @return
     */
    int addEventPlanFlow(SdEvent sdEvent, SdReservePlan reservePlan);
}
