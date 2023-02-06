package com.tunnel.business.service.event;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdEventHandle;

import java.util.List;

/**
 * 事件处置信息Service接口
 * 
 * @author ruoyi
 * @date 2022-12-15
 */
public interface ISdEventHandleService 
{
    /**
     * 查询事件处置信息
     * 
     * @param id 事件处置信息主键
     * @return 事件处置信息
     */
    public SdEventHandle selectSdEventHandleById(Long id);

    /**
     * 查询事件处置信息列表
     * 
     * @param sdEventHandle 事件处置信息
     * @return 事件处置信息集合
     */
    public List<SdEventHandle> selectSdEventHandleList(SdEventHandle sdEventHandle);

    /**
     * 新增事件处置信息
     * 
     * @param sdEventHandle 事件处置信息
     * @return 结果
     */
    public int insertSdEventHandle(SdEventHandle sdEventHandle);

    /**
     * 修改事件处置信息
     * 
     * @param sdEvent 事件处置信息
     * @return 结果
     */
    public AjaxResult updateSdEventHandle(SdEvent sdEvent);

    /**
     * 批量删除事件处置信息
     * 
     * @param ids 需要删除的事件处置信息主键集合
     * @return 结果
     */
    public int deleteSdEventHandleByIds(Long[] ids);

    /**
     * 删除事件处置信息信息
     * 
     * @param id 事件处置信息主键
     * @return 结果
     */
    public int deleteSdEventHandleById(Long id);
}
