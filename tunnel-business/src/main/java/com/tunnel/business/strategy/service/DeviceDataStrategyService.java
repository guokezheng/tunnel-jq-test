package com.tunnel.business.strategy.service;

import com.tunnel.business.domain.dataInfo.SdDeviceData;

import java.util.Map;

/**
 * describe: 设备数据策略接口
 *
 * @author zs
 * @date 2023/4/23
 */
public interface DeviceDataStrategyService {


    /**
     * 组装设备实时数据
     * @param devices 设备信息
     * @param data 设备实时数据
     */
    void getDeviceData(Map<String, String> devices, SdDeviceData data);
}
