package com.tunnel.business.strategy.domain;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.business.strategy.service.DeviceDataStrategyService;

import java.util.Map;
import java.util.Optional;

/**
 * describe: 基本照明
 *
 * @author zs
 * @date 2023/4/23
 */
public class BasicLight implements DeviceDataStrategyService {

    private ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);

    private ISdDeviceDataService sdDeviceDataService = SpringUtils.getBean(ISdDeviceDataService.class);

    private CommonControlService commonControlService = SpringUtils.getBean(CommonControlService.class);


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

    /**
     * 模拟控制设备
     *
     * @param map       控制参数
     * @param sdDevices 设备信息
     */
    @Override
    public AjaxResult analogControl(Map<String, Object> map, SdDevices sdDevices) {
        //模拟控制状态-控制成功
        int controlState = 1;

        String deviceId = sdDevices.getEqId();
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
        //设置为设备在线
        sdDevicesService.updateOnlineStatus(deviceId,false);
        //修改实时状态
        Long stateItemCode = (long) DevicesTypeItemEnum.JI_BEN_ZHAO_MING_OPENCLOSE.getCode();
        Long brightnessItemCode = (long) DevicesTypeItemEnum.JB_LIGHT_BRIGHNESS.getCode();
        sdDeviceDataService.updateDeviceData(sdDevices, state, stateItemCode);
        sdDeviceDataService.updateDeviceData(sdDevices, state, brightnessItemCode);
        String beforeState = commonControlService.selectBeforeState(deviceId, stateItemCode);
        //添加控制日志
        commonControlService.addOperationLog(map,sdDevices,beforeState,controlState);
        return AjaxResult.success(controlState);
    }
}
