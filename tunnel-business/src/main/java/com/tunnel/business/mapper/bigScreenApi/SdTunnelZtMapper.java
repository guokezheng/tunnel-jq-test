package com.tunnel.business.mapper.bigScreenApi;



import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SdTunnelZtMapper {

    /**
     * 获取当天的预警事件
     * @param createTimeBegin
     * @param createTimeEnd
     * @return
     */
    List<Map> getTunnelEventNumber(@Param("createTimeBegin") String createTimeBegin, @Param("createTimeEnd") String createTimeEnd,@Param("tunnelId") String tunnelId);

    /**
     * 大屏-环境监测风力风向
     * @param tunnelId
     * @return
     */
    List<Map> getWindPowerDirection(@Param("tunnelId")String tunnelId);

    /**
     * 大屏-环境监测CO浓度
     * @param tunnelId
     * @return
     */
    List<Map> getCOPotency(@Param("tunnelId")String tunnelId);

    /**
     * 大屏-环境监测能见度
     * @param tunnelId
     * @return
     */
    List<Map> getVisibility(@Param("tunnelId")String tunnelId);

    /**
     * 大屏-环境监测液位
     * @param tunnelId
     * @return
     */
    List<Map> getLiquidLevel(@Param("tunnelId")String tunnelId);

    /**
     * 大屏-环境监测光照监测
     * @param tunnelId
     * @return
     */
    List<Map> getLightDetection(@Param("tunnelId")String tunnelId);

    /**
     * 大屏-环境监测远传压力表
     * @param tunnelId
     * @return
     */
    List<Map> getPressure(@Param("tunnelId")String tunnelId);

    /**
     * 大屏-设备数据
     * @param tunnelId
     * @return
     */
    List<Map> getDeviceData(@Param("tunnelId")String tunnelId);
}
