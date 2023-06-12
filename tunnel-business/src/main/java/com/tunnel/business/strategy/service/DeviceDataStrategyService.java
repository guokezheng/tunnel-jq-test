package com.tunnel.business.strategy.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;

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


    /**
     * 模拟控制设备
     * @param map 控制参数
     * @param sdDevices 设备信息
     * @return
     */
    AjaxResult analogControl(Map<String, Object> map, SdDevices sdDevices);
}
