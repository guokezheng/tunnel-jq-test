package com.tunnel.platform.service.event.impl;

import com.tunnel.platform.domain.event.SdEventFlow;
import com.tunnel.platform.mapper.event.SdEventFlowMapper;
import com.tunnel.platform.service.event.ISdEventFlowService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
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
public class SdEventFlowServiceImpl implements ISdEventFlowService 
{
    @Autowired
    private SdEventFlowMapper sdEventFlowMapper;

    /**
     * 查询事件处理流程
     * 
     * @param id 事件处理流程ID
     * @return 事件处理流程
     */
    @Override
    public SdEventFlow selectSdEventFlowById(Long id)
    {
        return sdEventFlowMapper.selectSdEventFlowById(id);
    }

    /**
     * 查询事件处理流程列表
     * 
     * @param sdEventFlow 事件处理流程
     * @return 事件处理流程
     */
    @Override
    public List<SdEventFlow> selectSdEventFlowList(SdEventFlow sdEventFlow)
    {
        return sdEventFlowMapper.selectSdEventFlowList(sdEventFlow);
    }

    /**
     * 新增事件处理流程
     * 
     * @param sdEventFlow 事件处理流程
     * @return 结果
     */
    @Override
    public int insertSdEventFlow(SdEventFlow sdEventFlow)
    {
    	
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
    public int updateSdEventFlow(SdEventFlow sdEventFlow)
    {
        return sdEventFlowMapper.updateSdEventFlow(sdEventFlow);
    }

    /**
     * 批量删除事件处理流程
     * 
     * @param ids 需要删除的事件处理流程ID
     * @return 结果
     */
    @Override
    public int deleteSdEventFlowByIds(Long[] ids)
    {
        return sdEventFlowMapper.deleteSdEventFlowByIds(ids);
    }

    /**
     * 删除事件处理流程信息
     * 
     * @param id 事件处理流程ID
     * @return 结果
     */
    @Override
    public int deleteSdEventFlowById(Long id)
    {
        return sdEventFlowMapper.deleteSdEventFlowById(id);
    }
}