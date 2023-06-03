package com.tunnel.business.mapper.bigScreenApiN;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SdTunnelsDataMapper {

    /* 获取今日重大事故总数 */
    Integer getMajorEventDataCount();

    /* 获取今日安全预警总数 */
    Integer getMajorEventDataWarnCount();

    /* 获取今日未处理安全预警总数 */
    Integer getMajorEventDataHandleWarnCount();

    /* 获取今日交通事件总数 */
    Integer getMajorEventDataEventCount();

    /* 获取今日未处理交通事件总数*/
    Integer getMajorEventDataHandleEventCount();

    /* 获取今日事件列表 */
    List<Map<String,Object>> getMajorEventDataList();

    /* 获取所有事件类型 */
    List<Map<String, Object>> getListByPrevControlTypeId(@Param("prevControlTypeId") String prevControlTypeId);

    /* 获取每个事件类型的总数 */
    Integer[] getEventStatMonthDataByEventTypeId(@Param("eventTypeId") Integer eventTypeId);

    /* 获取每个事件类型 周的数据集 */
    Integer[] getEventStatWeekDataByEventTypeId(@Param("eventTypeId")Integer eventTypeId);

    /* 获取当年车流量总数 */
    Integer getCarFlowDataThisYearCount();

    /* 获取去年车流量总数 */
    Integer getCarFlowDataLastYearCount();

    /* 获取TOP 10 隧道流量排名 */
    List<Map<String, Object>> getCarFlowDataTopList();

    /* 获取近七日车流量列表 */
    List<Map<String, Object>> getCarFlowDataWeekList();

    /* 获取去年同期流量列表 */
    List<Map<String, Object>> getCarFlowDataLastYearList();

    /* 获取每个4小时流量列表 */
    List<Map<String, Object>> getCarFlowDataFourHoursList();

    /* 获取全部隧道 */
    String [] getAllTunnelsList();

    /* 获取车辆类型 */
    List<Map<String, Object>> getVehicleTypeList();

    /* 获取指定车辆类型车流量 */
    Integer [] getRealTimeCarFlowByVehicleTypeId(@Param("vehicleTypeId") Integer vehicleTypeId);

    /* 获取24小时内事件总数 */
    Integer getIndexData24HoursEventCount();

    /* 获取事件总数 */
    Integer getIndexDataEventCount();

    /* 获取事件来源列表*/
    List<Map<String, Object>> getIndexDataEventSourceList();

    /* 获取在途车流量 */
    Integer getIndexDataTravelVehicleFlowCount();

    /* 获取在途车流量列表 */
    List<Map<String, Object>> getIndexDataTravelVehicleFlowList();

    /* 获取在途车重点车辆总数 */
    Integer getIndexDataImportantVehicleCount();

    /* 获取在途车重点车辆列表*/
    List<Map<String, Object>> getIndexDataImportantVehicleList();
}
