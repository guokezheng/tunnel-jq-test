package com.tunnel.business.service.event.impl;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.event.SdJoinTypeFlow;
import com.tunnel.business.domain.event.SdPlanFlow;
import com.tunnel.business.mapper.event.SdJoinTypeFlowMapper;
import com.tunnel.business.service.event.ISdJoinTypeFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事件类型预案流程关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-10
 */
@Service
public class SdJoinTypeFlowServiceImpl implements ISdJoinTypeFlowService
{
    @Autowired
    private SdJoinTypeFlowMapper sdJoinTypeFlowMapper;

    /**
     * 查询事件类型预案流程关联
     * 
     * @param id 事件类型预案流程关联主键
     * @return 事件类型预案流程关联
     */
    @Override
    public List<SdJoinTypeFlow> selectSdJoinTypeFlowById(Long id)
    {
        return sdJoinTypeFlowMapper.selectSdJoinTypeFlowById(id);
    }

    /**
     * 查询事件类型预案流程关联列表
     * 
     * @param sdJoinTypeFlow 事件类型预案流程关联
     * @return 事件类型预案流程关联
     */
    @Override
    public List<SdJoinTypeFlow> selectSdJoinTypeFlowList(SdJoinTypeFlow sdJoinTypeFlow)
    {
        return sdJoinTypeFlowMapper.selectSdJoinTypeFlowList(sdJoinTypeFlow);
    }

    /**
     * 新增事件类型预案流程关联
     * 
     * @param sdJoinTypeFlow 事件类型预案流程关联
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public int insertSdJoinTypeFlow(SdJoinTypeFlow sdJoinTypeFlow)
    {
        List<SdPlanFlow> planFlowList = sdJoinTypeFlow.getPlanFlowList();
        int count = 0;
        int sort = 0;
        for(SdPlanFlow item : planFlowList){
            sort = sort + 1;
            SdJoinTypeFlow joinTypeFlow = setJoinTypeFlowData(item, sdJoinTypeFlow, sort);
            joinTypeFlow.setCreateTime(DateUtils.getNowDate());
            count = sdJoinTypeFlowMapper.insertSdJoinTypeFlow(joinTypeFlow);
            if(item.getChildren() != null && item.getChildren().size() > 0){
                sort = 0;
                for(SdPlanFlow children : item.getChildren()){
                    sort = sort + 1;
                    SdJoinTypeFlow joinTypeFlow1 = setJoinTypeFlowData(children, sdJoinTypeFlow, sort);
                    joinTypeFlow1.setCreateTime(DateUtils.getNowDate());
                    count = sdJoinTypeFlowMapper.insertSdJoinTypeFlow(joinTypeFlow1);
                }
            }
        }
        return count;
    }

    /**
     * 数据赋值
     * @return
     */
    public SdJoinTypeFlow setJoinTypeFlowData(SdPlanFlow sdPlanFlow, SdJoinTypeFlow sdJoinTypeFlow, int sort){
        SdJoinTypeFlow joinTypeFlow = new SdJoinTypeFlow();
        joinTypeFlow.setFlowPid(sdPlanFlow.getPid());
        joinTypeFlow.setFlowId(sdPlanFlow.getId());
        joinTypeFlow.setFlowName(sdPlanFlow.getLabel());
        joinTypeFlow.setFlowSort(sort+"");
        joinTypeFlow.setEventTypeId(sdJoinTypeFlow.getEventTypeId());
        return joinTypeFlow;
    }

    /**
     * 修改事件类型预案流程关联
     * 
     * @param sdJoinTypeFlow 事件类型预案流程关联
     * @return 结果
     */
    @Override
    public int updateSdJoinTypeFlow(SdJoinTypeFlow sdJoinTypeFlow)
    {
        sdJoinTypeFlow.setUpdateTime(DateUtils.getNowDate());
        return sdJoinTypeFlowMapper.updateSdJoinTypeFlow(sdJoinTypeFlow);
    }

    /**
     * 批量删除事件类型预案流程关联
     * 
     * @param ids 需要删除的事件类型预案流程关联主键
     * @return 结果
     */
    @Override
    public int deleteSdJoinTypeFlowByIds(Long[] ids)
    {
        return sdJoinTypeFlowMapper.deleteSdJoinTypeFlowByIds(ids);
    }

    /**
     * 删除事件类型预案流程关联信息
     * 
     * @param id 事件类型预案流程关联主键
     * @return 结果
     */
    @Override
    public int deleteSdJoinTypeFlowById(Long id)
    {
        return sdJoinTypeFlowMapper.deleteSdJoinTypeFlowById(id);
    }

    @Override
    public List<SdPlanFlow> getTypeFlowList() {
        return sdJoinTypeFlowMapper.getTypeFlowList();
    }

    @Override
    public AjaxResult checkData(String eventTypeId) {
        int count = sdJoinTypeFlowMapper.checkData(Long.valueOf(eventTypeId));
        if(count > 0){
            return AjaxResult.error("事件类型预案流程已存在！");
        }
        return AjaxResult.success();
    }
}
