package com.tunnel.platform.service.event;

import com.tunnel.platform.domain.event.SdEventFlow;

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
}