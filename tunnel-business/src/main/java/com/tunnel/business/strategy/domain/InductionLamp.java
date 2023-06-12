package com.tunnel.business.strategy.domain;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.strategy.service.DeviceDataStrategyService;

import java.util.Map;

/**
 * describe: 诱导灯类
 *
 * @author zs
 * @date 2023/4/23
 */
public class InductionLamp implements DeviceDataStrategyService {
    /**
     * 获取实时数据
     *
     * @param devices
     * @param data
     */
    @Override
    public void getDeviceData(Map<String, String> devices, SdDeviceData data) {
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode()) {
            devices.put("state", data.getData());
        } else if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode()) {
            devices.put("brightness", data.getData());
        } else if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode()) {
            devices.put("frequency", data.getData());
        }

    }

    /**
     * 模拟控制设备
     *
     * @param map       控制参数
     * @param sdDevices 设备信息
     * @return
     */
    @Override
    public AjaxResult analogControl(Map<String, Object> map, SdDevices sdDevices) {
        return null;
    }

}
