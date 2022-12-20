package com.tunnel.business.service.event;


import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdReservePlan;

import java.util.List;
import java.util.Map;

/**
 * 事件管理Service接口
 *
 * @author gongfanfei
 * @date 2020-08-24
 */
public interface ISdEventService {
    /**
     * 查询事件管理
     *
     * @param id 事件管理ID
     * @return 事件管理
     */
    SdEvent selectSdEventById(Long id);

    /**
     * 查询事件管理列表
     *
     * @param sdEvent 事件管理
     * @return 事件管理集合
     */
    List<SdEvent> selectSdEventList(SdEvent sdEvent);

    /**
     * 新增事件管理
     *
     * @param sdEvent 事件管理
     * @return 结果
     */
    int insertSdEvent(SdEvent sdEvent);

    /**
     * 修改事件管理
     *
     * @param sdEvent 事件管理
     * @return 结果
     */
    int updateSdEvent(SdEvent sdEvent);

    /**
     * 批量删除事件管理
     *
     * @param ids 需要删除的事件管理ID
     * @return 结果
     */
    int deleteSdEventByIds(Long[] ids);

    /**
     * 删除事件管理信息
     *
     * @param id 事件管理ID
     * @return 结果
     */
    int deleteSdEventById(Long id);


    /**
     * 根据id查询事件 ---视频
     *
     * @param id
     * @return
     */
    SdEvent getById(Long id);

    /**
     * 预警事件查询全部
     *
     * @return
     */
    List<SdEvent> getEvent(SdEvent sdEvent);

    /**
     * 统计今日事件
     * @return
     */
    Map getTodayEventCount();


    /**
     * 根据事件id列表查询事件信息
     *
     * @param eventIdList 事件id列表
     * @return
     */
    List<SdEvent> getEventList(List<Long> eventIdList);

    /**
     * 拼接得到默认的事件标题
     * @param sdEvent 事件信息
     * @param tunnelMap 隧道名称Map
     * @param eventTypeMap 事件类型Map
     * @return
     */
    String getDefaultEventTitle(SdEvent sdEvent,Map<String,String> tunnelMap,Map<Long,String> eventTypeMap);

    /**
     * 根据事件桩号 获取分区Id
     * @param stakeNum
     * @return
     */
    Long getSubareaByStakeNum(String tunnelId,String stakeNum,String direction);

    List<SdDevices> getEventCamera(String tunnelId,String stakeNum,String direction);

    List<Map> eventPopAll(String subIndex);

    /**
     * 主动安全-复核-处置获取预案流程
     * @param sdEvent
     * @return
     */
    AjaxResult getHandle(SdEvent sdEvent);

    /**
     * 应急调度关联策略
     * @param sdReservePlan
     * @return
     */
    AjaxResult getRelation(SdReservePlan sdReservePlan);

    /**
     * 计算事故点
     * @param sdEvent
     * @return
     */
    AjaxResult getAccidentPoint(SdEvent sdEvent);
}
