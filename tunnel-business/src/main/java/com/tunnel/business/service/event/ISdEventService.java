package com.tunnel.business.service.event;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdEventHandle;
import com.tunnel.business.domain.event.SdReservePlan;
import com.tunnel.business.domain.event.SdReserveProcess;

import javax.servlet.http.HttpServletResponse;
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
     * 查询事件管理列表(不做额外处理，单纯查询数据库SQL)
     *
     * @param sdEvent 事件管理
     * @return 事件管理集合
     */
    List<SdEvent> querySdEventList(SdEvent sdEvent);

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

    //List<Map> eventPopAll(String subIndex);

    /**
     * 交通事件-复核-处置获取预案流程
     * @param sdEvent
     * @return
     */
    List<SdEventHandle> getHandle(SdEvent sdEvent);

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

    /**
     * 查询预案id
     * @param sdReservePlan
     * @return
     */
    AjaxResult getReserveId(SdReservePlan sdReservePlan);

    /**
     * 查询应急调度出入口视频
     * @param sdEvent
     * @return
     */
    AjaxResult getEntranceExitVideo(SdEvent sdEvent);

    /**
     * 查看事件详情
     *
     * @param sdEvent
     * @return
     */
    AjaxResult getEventDetail(SdEvent sdEvent);

    /**
     * 华为修改事件接口
     * @param sdEvent
     * @return
     */
    int updateSdEventHw(SdEvent sdEvent);

    /**
     * 事件详情导出
     * @param sdEvent
     */
    void detailExport(HttpServletResponse response, SdEvent sdEvent);

    /**
     * 警情升级返现
     * @param sdEvent
     * @return
     */
    AjaxResult getSituationUpgrade(SdEvent sdEvent);

    /**
     * 应急调度-处置设备详情（单条）
     * @param sdReserveProcess
     * @return
     */
    AjaxResult getManagementDevice(SdReserveProcess sdReserveProcess);

    /**
     * 应急调度-处置设备详情（阶段）
     * @param sdEventHandle
     * @return
     */
    AjaxResult getAllManagementDevices(SdEventHandle sdEventHandle);

    /**
     * 修改警情升级
     * @param sdEvent
     * @return
     */
    int updateSituationUpgrade(SdEvent sdEvent);

    /**
     * 查询事件等级以及预案名称
     * @param sdEvent
     * @return
     */
    AjaxResult getEventInif(SdEvent sdEvent);

    /**
     * 查看所选预案或策略的设备详情
     * @param sdEvent
     * @return
     */
    AjaxResult examineDeviceDetail(SdEvent sdEvent);

    /**
     * 右上角事件数据
     * @param sdEvent
     * @return
     */
    List<Map<String, Object>> eventPopData(SdEvent sdEvent);

    /**
     * 批量处理事件
     * @param sdEvent
     * @return
     */
    AjaxResult batchHandleEvent(SdEvent sdEvent);

    /**
     * 查看历史录像
     * @param sdEvent
     * @return
     */
    AjaxResult vedioData(SdEvent sdEvent);

    /**
     * 关闭相机录像视频流
     * @param camId
     * @param playId
     * @return
     */
    AjaxResult closeVedio(String camId, String playId);

    /**
     * 下载视频录像
     * @param camId
     * @param downLoadTime
     * @return
     */
    void downLoadVedio(String camId, String downLoadTime,HttpServletResponse response);

    void eventDemonstrate(String hyData);

    /**
     * 接收万集事件
     * @param eventJson
     */
    void upload(String eventJson);
}
