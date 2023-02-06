package com.tunnel.business.service.event;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.event.SdJoinTypeFlow;
import com.tunnel.business.domain.event.SdPlanFlow;

import java.util.List;

/**
 * 事件类型预案流程关联Service接口
 * 
 * @author ruoyi
 * @date 2022-12-10
 */
public interface ISdJoinTypeFlowService 
{
    /**
     * 查询事件类型预案流程关联
     * 
     * @param id 事件类型预案流程关联主键
     * @return 事件类型预案流程关联
     */
    public List<SdJoinTypeFlow> selectSdJoinTypeFlowById(Long id);

    /**
     * 查询事件类型预案流程关联列表
     * 
     * @param sdJoinTypeFlow 事件类型预案流程关联
     * @return 事件类型预案流程关联集合
     */
    public List<SdJoinTypeFlow> selectSdJoinTypeFlowList(SdJoinTypeFlow sdJoinTypeFlow);

    /**
     * 新增事件类型预案流程关联
     * 
     * @param sdJoinTypeFlow 事件类型预案流程关联
     * @return 结果
     */
    public int insertSdJoinTypeFlow(SdJoinTypeFlow sdJoinTypeFlow);

    /**
     * 修改事件类型预案流程关联
     * 
     * @param sdJoinTypeFlow 事件类型预案流程关联
     * @return 结果
     */
    public int updateSdJoinTypeFlow(SdJoinTypeFlow sdJoinTypeFlow);

    /**
     * 批量删除事件类型预案流程关联
     * 
     * @param ids 需要删除的事件类型预案流程关联主键集合
     * @return 结果
     */
    public int deleteSdJoinTypeFlowByIds(Long[] ids);

    /**
     * 删除事件类型预案流程关联信息
     * 
     * @param id 事件类型预案流程关联主键
     * @return 结果
     */
    public int deleteSdJoinTypeFlowById(Long id);

    /**
     * 获取预案流程树
     * @return
     */
    List<SdPlanFlow> getTypeFlowList();

    /**
     * 校验是否已存在当前事件类型预案流程
     * @param eventTypeId
     * @return
     */
    AjaxResult checkData(String eventTypeId);
}
