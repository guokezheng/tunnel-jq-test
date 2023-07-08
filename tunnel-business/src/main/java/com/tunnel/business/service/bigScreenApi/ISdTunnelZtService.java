package com.tunnel.business.service.bigScreenApi;

import java.util.List;
import java.util.Map;

public interface ISdTunnelZtService {

    /**
     * 获取当天预警事件
     * @param createTimeBegin
     * @param createTimeEnd
     * @return
     */
    List<Map> getTunnelEventNumber(String createTimeBegin,String createTimeEnd,String tunnelId);

    /**
     * 大屏-环境监测风力风向
     * @param tunnelId
     * @return
     */
    List<Map> getWindPowerDirection(String tunnelId);

    /**
     * 大屏-环境监测CO浓度
     * @param tunnelId
     * @return
     */
    List<Map> getCOPotency(String tunnelId);

    /**
     * 大屏-环境监测能见度
     * @param tunnelId
     * @return
     */
    List<Map> getVisibility(String tunnelId);

    /**
     * 大屏-环境监测液位
     * @param tunnelId
     * @return
     */
    List<Map> getLiquidLevel(String tunnelId);

    /**
     * 大屏-环境监测光照监测
     * @param tunnelId
     * @return
     */
    List<Map> getLightDetection(String tunnelId);

    /**
     * 大屏-环境监测远传压力表
     * @param tunnelId
     * @return
     */
    List<Map> getPressure(String tunnelId);

    /**
     * 大屏-设备数据
     * @param tunnelId
     * @return
     */
    List<Map> getDeviceData(String tunnelId);
}
