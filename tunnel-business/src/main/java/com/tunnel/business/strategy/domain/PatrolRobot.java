package com.tunnel.business.strategy.domain;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.strategy.service.DeviceDataStrategyService;

import java.util.Map;

/**
 * describe: 巡检机器人
 *
 * @author zs
 * @date 2023/4/23
 */
public class PatrolRobot implements DeviceDataStrategyService {
    /**
     * 获取实时数据
     *
     * @param devices
     * @param data
     */
    @Override
    public void getDeviceData(Map<String, String> devices, SdDeviceData data) {
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.ROBOT_IS_ONLINE.getCode()) {
            devices.put("isOnline", data.getData());
        }
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.ROBOT_CURRENT_DURATION.getCode()) {
            devices.put("currentDuration", data.getData());
        }
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.ROBOT_CURRENT_MILEAGE.getCode()) {
            devices.put("currentMileage", data.getData());
        }
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.ROBOT_ELECTRICITY.getCode()) {
            devices.put("electricity", data.getData());
        }
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.ROBOT_CHARGE.getCode()) {
            devices.put("charge", data.getData());
        }
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.ROBOT_VOLTAGE.getCode()) {
            devices.put("voltage", data.getData());
        }
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.ROBOT_CURRENT.getCode()) {
            devices.put("current", data.getData());
        }
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.ROBOT_BATTERYTEMP.getCode()) {
            devices.put("batteryTemp", data.getData());
        }
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.ROBOT_POSITION.getCode()) {
            devices.put("position", data.getData());
        }
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.ROBOT_OXYGENDENSITY.getCode()) {
            devices.put("oxygenDensity", data.getData());
        }
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.ROBOT_CARBON_MONOXIDE_DENSITY.getCode()) {
            devices.put("carbonMonoxideDensity", data.getData());
        }
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.ROBOT_WORK_MODEL_TEXT.getCode()) {
            devices.put("workModelText", data.getData());
        }
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.ROBOT_HD_VIDEO.getCode()) {
            devices.put("hd", data.getData());
        }
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.ROBOT_INFRARE_VIDEO.getCode()) {
            devices.put("infrared", data.getData());
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
