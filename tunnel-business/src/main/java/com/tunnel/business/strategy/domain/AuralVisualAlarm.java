package com.tunnel.business.strategy.domain;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.strategy.service.DeviceDataStrategyService;

import java.util.Map;

/**
 * describe: 声光报警器
 *
 * @author zs
 * @date 2023/4/23
 */
public class AuralVisualAlarm implements DeviceDataStrategyService {
    /**
     * 获取实时数据
     *
     * @param devices
     * @param data
     */
    @Override
    public void getDeviceData(Map<String, String> devices, SdDeviceData data) {
        if (data != null && (data.getItemId() == (long) DevicesTypeItemEnum.FLAME_DETECTOR_ALARM.getCode()
                || data.getItemId() == (long) DevicesTypeItemEnum.SHOU_BAO_ALARM.getCode())) {
            if (devices.get("eqState") != null && "1".equals(devices.get("eqState"))) {
                devices.put("state", data.getData());
            }
        }
    }

    /**
     * 模拟控制设备
     *
     * @param map       控制参数
     * @param sdDevices 设备信息
     */
    @Override
    public AjaxResult analogControl(Map<String, Object> map, SdDevices sdDevices) {
        return null;
    }
}
