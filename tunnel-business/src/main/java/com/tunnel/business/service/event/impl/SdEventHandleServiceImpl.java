package com.tunnel.business.service.event.impl;

import java.util.Arrays;
import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdEventFlow;
import com.tunnel.business.domain.event.SdEventHandle;
import com.tunnel.business.mapper.event.SdEventFlowMapper;
import com.tunnel.business.mapper.event.SdEventHandleMapper;
import com.tunnel.business.service.event.ISdEventHandleService;
import com.tunnel.business.utils.json.JSONObject;
import com.zc.common.core.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 事件处置信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-15
 */
@Service
public class SdEventHandleServiceImpl implements ISdEventHandleService
{
    @Autowired
    private SdEventHandleMapper sdEventHandleMapper;

    /**
     * 查询事件处置信息
     * 
     * @param id 事件处置信息主键
     * @return 事件处置信息
     */
    @Override
    public SdEventHandle selectSdEventHandleById(Long id)
    {
        return sdEventHandleMapper.selectSdEventHandleById(id);
    }

    /**
     * 查询事件处置信息列表
     * 
     * @param sdEventHandle 事件处置信息
     * @return 事件处置信息
     */
    @Override
    public List<SdEventHandle> selectSdEventHandleList(SdEventHandle sdEventHandle)
    {
        return sdEventHandleMapper.selectSdEventHandleList(sdEventHandle);
    }

    /**
     * 新增事件处置信息
     * 
     * @param sdEventHandle 事件处置信息
     * @return 结果
     */
    @Override
    public int insertSdEventHandle(SdEventHandle sdEventHandle)
    {
        sdEventHandle.setCreateTime(DateUtils.getNowDate());
        return sdEventHandleMapper.insertSdEventHandle(sdEventHandle);
    }

    /**
     * 修改事件处置信息
     * 
     * @param sdEvent 事件处置信息
     * @return 结果
     */
    @Override
    public AjaxResult updateSdEventHandle(SdEvent sdEvent)
    {
        List<String> list = Arrays.asList(sdEvent.getIds().split(","));
        int count = 0;
        for(String id : list){
            SdEventHandle sdEventHandle = new SdEventHandle();
            sdEventHandle.setId(Long.valueOf(id));
            //0:未完成 1:已完成'
            sdEventHandle.setEventState("1");
            sdEventHandle.setUpdateTime(DateUtils.getNowDate());
            count = sdEventHandleMapper.updateSdEventHandle(sdEventHandle);
            SdEventHandle sdEventHandle1 = sdEventHandleMapper.selectSdEventHandleById(Long.valueOf(id));
            //保存事件处理记录
            SdEventFlow flow = new SdEventFlow();
            flow.setFlowDescription(sdEventHandle1.getFlowContent() + "执行下发操作");
            flow.setEventId(sdEvent.getId().toString());
            flow.setFlowTime(DateUtils.getNowDate());
            flow.setFlowHandler(SecurityUtils.getUsername());
            SpringUtils.getBean(SdEventFlowMapper.class).insertSdEventFlow(flow);
        }
        if(count == 0){
            return AjaxResult.error("事件处置更新失败");
        }else {
            return AjaxResult.success();
        }

    }

    /**
     * 批量删除事件处置信息
     * 
     * @param ids 需要删除的事件处置信息主键
     * @return 结果
     */
    @Override
    public int deleteSdEventHandleByIds(Long[] ids)
    {
        return sdEventHandleMapper.deleteSdEventHandleByIds(ids);
    }

    /**
     * 删除事件处置信息信息
     * 
     * @param id 事件处置信息主键
     * @return 结果
     */
    @Override
    public int deleteSdEventHandleById(Long id)
    {
        return sdEventHandleMapper.deleteSdEventHandleById(id);
    }
}
