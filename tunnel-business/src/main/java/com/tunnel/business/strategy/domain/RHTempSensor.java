package com.tunnel.business.strategy.domain;

import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.strategy.service.DeviceDataStrategyService;

import java.util.Map;

/**
 * describe: 温湿度传感器
 *
 * @author zs
 * @date 2023/4/23
 */
public class RHTempSensor implements DeviceDataStrategyService {
    /**
     * 获取实时数据
     *
     * @param devices
     * @param data
     */
    @Override
    public void getDeviceData(Map<String, String> devices, SdDeviceData data) {
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.WEN_DU_CHUANGAN.getCode()) {
            devices.put("temperature", data.getData());
        } else if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.SHI_DU_CHUANGAN.getCode()) {
            devices.put("humidity", data.getData());
        }
    }
}
