package com.tunnel.platform.service.event;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.platform.domain.event.SdEvent;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

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

    /**
     * 新增万集事件
     * @param map
     */
    AjaxResult insertWjEvent(Map<String, Object> map);

    AjaxResult uploadPic(Map<String, Object> map);

    AjaxResult eventVideo(Map<String,Object> map);

    AjaxResult specialCar(Map<String, Object> map);

    /**
     * 根据id查询事件 ---视频
     * @param id
     * @return
     */
    public SdEvent getById(Long id);

    void insertRadarDetect(Map<String, Object> map) throws ParseException;
}