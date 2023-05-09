package com.tunnel.business.strategy.domain;

import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.strategy.service.DeviceDataStrategyService;

import java.util.Map;

/**
 * describe: 默认的设备处理类
 *
 * @author zs
 * @date 2023/4/23
 */
public class DefaultType implements DeviceDataStrategyService {
    /**
     * 获取实时数据
     *
     * @param devices
     * @param data
     */
    @Override
    public void getDeviceData(Map<String, String> devices, SdDeviceData data) {
        devices.put("state", data.getData());
    }
}
