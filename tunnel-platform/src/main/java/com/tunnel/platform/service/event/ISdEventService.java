package com.tunnel.platform.service.event;

import com.tunnel.platform.domain.event.SdEvent;

import java.util.List;

/**
 * 事件管理Service接口
 * 
 * @author gongfanfei
 * @date 2020-08-24
 */
public interface ISdEventService 
{
    /**
     * 查询事件管理
     * 
     * @param id 事件管理ID
     * @return 事件管理
     */
    public SdEvent selectSdEventById(Long id);

    /**
     * 查询事件管理列表
     * 
     * @param sdEvent 事件管理
     * @return 事件管理集合
     */
    public List<SdEvent> selectSdEventList(SdEvent sdEvent);

    /**
     * 新增事件管理
     * 
     * @param sdEvent 事件管理
     * @return 结果
     */
    public int insertSdEvent(SdEvent sdEvent);

    /**
     * 修改事件管理
     * 
     * @param sdEvent 事件管理
     * @return 结果
     */
    public int updateSdEvent(SdEvent sdEvent);

    /**
     * 批量删除事件管理
     * 
     * @param ids 需要删除的事件管理ID
     * @return 结果
     */
    public int deleteSdEventByIds(Long[] ids);

    /**
     * 删除事件管理信息
     * 
     * @param id 事件管理ID
     * @return 结果
     */
    public int deleteSdEventById(Long id);
}