package com.tunnel.business.mapper.bigScreenApi;

import com.tunnel.business.domain.bigScreenApi.SdEventWarning;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zhai
 * @date 2022/11/7
 */
public interface SdSmartBigScreenMapper {

    /**
     * 查询本月预警信息
     * @return
     */
    List<SdEventWarning> seleteEventWarning(@Param("tunnelId") String tunnelId);

    /**
     * 查询事件比例
     * @return
     */
    List<Map<String, Object>> getEventProportion(@Param("tunnelId") String tunnelId);

    /**
     * 查询今日报警事件
     * @param tunnelId
     * @return
     */
    List<Map<String, Object>> getToDayEventWarning(@Param("tunnelId") String tunnelId);

    //查询今日报警事件(故障)
    List<Map<String, Object>> getToDayFaultWarning(@Param("tunnelId") String tunnelId);

    /**
     * 查询本周报警事件
     * @param tunnelId
     * @return
     */
    List<Map<String, Object>> getToWeekEventWarning(@Param("tunnelId") String tunnelId);

    /**
     * 查询本周报警事件(故障)
     * @param tunnelId
     * @return
     */
    List<Map<String, Object>> getToWeekFaultWarning(@Param("tunnelId") String tunnelId);

    /**
     * 查询本月报警趋势(交通事件)
     * @param tunnelId
     * @return
     */
    List<Integer> getEventList(@Param("tunnelId") String tunnelId,
                                           @Param("prevControlType") String prevControlType,
                                           @Param("dictValue") String dictValue);

    /**
     * 查询本月报警趋势(设备故障)
     * @param tunnelId
     * @return
     */
    List<Integer> getFaultList(@Param("tunnelId") String tunnelId);

    /**
     * 查询累计报警分析(交通事件)
     * @param tunnelId
     * @return
     */
    int getCumulativeEvent(@Param("tunnelId") String tunnelId,
                           @Param("prevControlType") String prevControlType,
                           @Param("dictValue") String dictValue);

    /**
     * 查询累计报警分析(主动安全)
     * @param tunnelId
     * @return
     */
    int getCumulativeWarning(@Param("tunnelId") String tunnelId,
                             @Param("prevControlType") String prevControlType,
                             @Param("dictValue") String dictValue);

    /**
     * 查询累计报警分析(设备故障)
     * @param tunnelId
     * @return
     */
    int getCumulativeFault(@Param("tunnelId") String tunnelId);

    /**
     * 查询累计分析列表
     * @param tunnelId
     * @return
     */
    List<Map<String, Object>> getCumulativeAlarmList(@Param("tunnelId") String tunnelId,
                                                     @Param("prevControlType") String prevControlType,
                                                     @Param("sdEventState") String sdEventState,
                                                     @Param("faultType") String faultType,
                                                     @Param("faultRemoveStatue") String faultRemoveStatue,
                                                     @Param("dictValue1") String dictValue1,
                                                     @Param("dictValue2") String dictValue2);

    /**
     * 查询交通事件信息列表
     * @param tunnelId
     * @return
     */
    List<Map<String, Object>> getTrafficIncident(@Param("tunnelId") String tunnelId,
                                                 @Param("prevControlType") String prevControlType,
                                                 @Param("dictValue") String dictValue);

    /**
     * 查询主动安全信息列表
     * @param tunnelId
     * @return
     */
    List<Map<String, Object>> getActiveSafety(@Param("tunnelId") String tunnelId,
                                              @Param("prevControlType") String prevControlType,
                                              @Param("dictValue") String dictValue);

    /**
     * 查询设备故障信息列表
     * @param tunnelId
     * @return
     */
    List<Map<String, Object>> getequipmentFailure(@Param("tunnelId") String tunnelId,
                                                  @Param("sdDirection") String sdDirection,
                                                  @Param("faultType") String faultType);

    /**
     * 查询预案列表
     * @return
     */
    List<Map<String, Object>> getReservePlan(@Param("tunnelId") String tunnelId);

    /**
     * 查询周边物资
     * @return
     */
    List<Map<String, Object>> getEmergencyMaterials(@Param("tunnelId") String tunnelId);

    /**
     * 查询值班人员
     * @return
     */
    List<Map<String, Object>> getEmergencyPer(@Param("tunnelId") String tunnelId);

    /**
     * 查询应急车辆
     * @return
     */
    List<Map<String, Object>> getEmergencyVehicle(@Param("tunnelId") String tunnelId);

    /**
     * 查询报警信息
     * @return
     */
    List<Map<String, Object>> getAlarmInformation(@Param("tunnelId") String tunnelId);

    /**
     * 查询风险指标
     *
     * @param tunnelId
     * @return
     */
    List<Map<String, Object>> getRiskIndicators(@Param("tunnelId") String tunnelId);

    /**
     * 查询当日累计车辆
     * @param tunnelId
     * @return
     */
    Map<String, Object> getCumulativeCar(@Param("tunnelId") String tunnelId,
                                         @Param("model") String model);

    /**
     * 查询车辆在途数
     * @param tunnelId
     * @param model
     * @return
     */
    Map<String, Object> getTransitCar(@Param("tunnelId") String tunnelId,
                                      @Param("model") String model);

    /**
     * 查询24小时车流量
     * @param tunnelId
     * @return
     */
    List<Map<String, Object>> getHoursTrafficVolume(@Param("tunnelId") String tunnelId,
                                              @Param("model") String model);

    /**
     * 统计设备状态
     * @param tunnelId
     * @return
     */
    List<Map<String, Object>> getStatisticalDevice(@Param("tunnelId") String tunnelId);
}
