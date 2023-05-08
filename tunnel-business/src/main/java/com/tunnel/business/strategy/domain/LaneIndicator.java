package com.tunnel.business.strategy.domain;

import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.strategy.service.DeviceDataStrategyService;

import java.util.Map;

/**
 * describe: 车道指示器类
 *
 * @author zs
 * @date 2023/4/23
 */
public class LaneIndicator implements DeviceDataStrategyService {

    @Override
    public void getDeviceData(Map<String, String> devices, SdDeviceData data) {
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode()) {
            devices.put("state", data.getData());
        }
    }

}
