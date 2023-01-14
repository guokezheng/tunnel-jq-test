package com.tunnel.business.service.bigScreenApi.impl;

import com.tunnel.business.mapper.bigScreenApi.SdTunnelWarningMapper;
import com.tunnel.business.mapper.bigScreenApi.SdTunnelZtMapper;
import com.tunnel.business.service.bigScreenApi.ISdTunnelWarningService;
import com.tunnel.business.service.bigScreenApi.ISdTunnelZtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SdTunnelZtServiceImpl implements ISdTunnelZtService {

    private static final Logger log = LoggerFactory.getLogger(SdTunnelZtServiceImpl.class);

    @Autowired
    private SdTunnelZtMapper sdTunnelZtMapper;

    /**
     * 获取当天的预警事件
     * @param createTimeBegin
     * @param createTimeEnd
     * @return
     */
    @Override
    public List<Map> getTunnelEventNumber(String createTimeBegin, String createTimeEnd,String tunnelId) {
        return sdTunnelZtMapper.getTunnelEventNumber(createTimeBegin,createTimeEnd,tunnelId);
    }

    /**
     * 大屏-环境监测风力风向
     * @param tunnelId
     * @return
     */
    @Override
    public List<Map> getWindPowerDirection(String tunnelId) {
        return sdTunnelZtMapper.getWindPowerDirection(tunnelId);
    }

    /**
     * 大屏-环境监测CO浓度
     * @param tunnelId
     * @return
     */
    @Override
    public List<Map> getCOPotency(String tunnelId) {
        return sdTunnelZtMapper.getCOPotency(tunnelId);
    }

    /**
     * 大屏-环境监测能见度
     * @param tunnelId
     * @return
     */
    @Override
    public List<Map> getVisibility(String tunnelId) {
        return sdTunnelZtMapper.getVisibility(tunnelId);
    }

    /**
     * 大屏-环境监测液位
     * @param tunnelId
     * @return
     */
    @Override
    public List<Map> getLiquidLevel(String tunnelId) {
        return sdTunnelZtMapper.getLiquidLevel(tunnelId);
    }

    /**
     * 大屏-环境监测光照监测
     * @param tunnelId
     * @return
     */
    @Override
    public List<Map> getLightDetection(String tunnelId) {
        return sdTunnelZtMapper.getLightDetection(tunnelId);
    }

    /**
     * 大屏-环境监测远传压力表
     * @param tunnelId
     * @return
     */
    @Override
    public List<Map> getPressure(String tunnelId) {
        return sdTunnelZtMapper.getPressure(tunnelId);
    }

    /**
     * 大屏-设备数据
     * @param tunnelId
     * @return
     */
    @Override
    public List<Map> getDeviceData(String tunnelId) {
        return sdTunnelZtMapper.getDeviceData(tunnelId);
    }
}
