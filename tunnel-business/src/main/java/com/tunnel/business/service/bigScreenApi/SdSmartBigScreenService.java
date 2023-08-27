package com.tunnel.business.service.bigScreenApi;

import com.ruoyi.common.core.domain.AjaxResult;

import java.util.Map;

/**
 * @author zhai
 * @date 2022/11/7
 */
public interface SdSmartBigScreenService {

    /**
     * 查询本月预警事件
     * @return
     */
    Map<String,Object> getEventWarning(String tunnelId);

    /**
     * 查询当天报警事件
     * @param tunnelId
     * @return
     */
    AjaxResult getToDayEventWarning(String tunnelId);

    /**
     * 查询本周报警事件
     * @param tunnelId
     * @return
     */
    AjaxResult getToWeekEventWarning(String tunnelId);

    /**
     * 查询本月报警事件
     * @param tunnelId
     * @return
     */
    AjaxResult getSameMonthEventWarning(String tunnelId);

    /**
     * 累计预警分析
     * @param tunnelId
     * @return
     */
    AjaxResult getCumulativeAlarm(String tunnelId);

    /**
     * 查询交通事件信息列表
     * @param tunnelId
     * @return
     */
    AjaxResult getTrafficIncident(String tunnelId);

    /**
     * 查询主动安全信息列表
     * @param tunnelId
     * @return
     */
    AjaxResult getActiveSafety(String tunnelId);

    /**
     * 查询设备故障信息列表
     * @param tunnelId
     * @return
     */
    AjaxResult getequipmentFailure(String tunnelId);

    /**
     * 查询预案列表
     * @return
     */
    AjaxResult getReservePlan(String tunnelId);

    /**
     * 查询周边物资
     * @return
     */
    AjaxResult getEmergencyMaterials(String tunnelId);

    /**
     * 查询值班人员
     * @param tunnelId
     * @return
     */
    AjaxResult getEmergencyPer(String tunnelId);

    /**
     * 查询应急车辆
     * @param tunnelId
     * @return
     */
    AjaxResult getEmergencyVehicle(String tunnelId);

    /**
     * 查询报警信息
     * @param tunnelId
     * @return
     */
    AjaxResult getAlarmInformation(String tunnelId);

    /**
     * 查询风险指标
     *
     * @param tunnelId
     * @return
     */
    AjaxResult getRiskIndicators(String tunnelId);

    /**
     * 查询隧道内车辆数、车辆均速
     *
     * @param tunnelId
     * @param roadDir
     * @return
     */
    AjaxResult getTunnelVehicles(String tunnelId, String roadDir);

    /**
     * 查询实时车辆
     *
     * @param tunnelId
     * @param vehicleLicense
     * @return
     */
    AjaxResult getRealCars(String tunnelId, String vehicleLicense);

    /**
     * 查询当日累计车辆
     * @param tunnelId
     * @return
     */
    AjaxResult getCumulativeCar(String tunnelId);

    /**
     * 查询车辆在途数
     * @param tunnelId
     * @return
     */
    AjaxResult getTransitCar(String tunnelId);

    /**
     * 查询24小时车流量
     * @param tunnelId
     * @return
     */
    AjaxResult getHoursTrafficVolume(String tunnelId);

    /**
     * 统计设备状态
     * @param tunnelId
     * @return
     */
    AjaxResult getStatisticalDevice(String tunnelId);

    /**
     * 隧道平均车速以及车流
     * @return
     */
    AjaxResult getAllTunnelVehicleSpeed();

    /**
     * 隧道平均车速
     * @return
     */
    AjaxResult getTunnelSpeed();

    /**
     * 获取各隧道数据
     * @return
     */
    AjaxResult getTunnelStatis();
}
