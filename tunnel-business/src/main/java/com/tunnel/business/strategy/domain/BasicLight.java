package com.tunnel.business.strategy.domain;

import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.strategy.service.DeviceDataStrategyService;

import java.util.Map;

/**
 * describe: 基本照明
 *
 * @author zs
 * @date 2023/4/23
 */
public class BasicLight implements DeviceDataStrategyService {
    /**
     * 获取实时数据
     *
     * @param devices
     * @param data
     */
    @Override
    public void getDeviceData(Map<String, String> devices, SdDeviceData data) {
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.JI_BEN_ZHAO_MING_OPENCLOSE.getCode()) {
            devices.put("state", data.getData());
        }else if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.JB_LIGHT_BRIGHNESS.getCode()) {
            devices.put("brightness", data.getData());
        }
    }
}
