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
 * describe: 警示灯带
 *
 * @author zs
 * @date 2023/4/23
 */
public class WarningLight implements DeviceDataStrategyService {

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
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.JING_SHI_DENG_DAI.getCode()) {
            devices.put("state", data.getData());
        }else if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.JING_SHI_DENG_DAI_STATUS.getCode()) {
            devices.put("brightness", data.getData());
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
        //模拟控制状态-控制成功
        int controlState = 1;

        String deviceId = sdDevices.getEqId();
        //状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
        //亮度
        String brightness = Optional.ofNullable(map.get("brightness")).orElse("").toString();
        //设置为设备在线
        sdDevicesService.updateOnlineStatus(deviceId,false);
        //修改实时状态
        sdDeviceDataService.updateDeviceData(sdDevices, state, (long) DevicesTypeItemEnum.JING_SHI_DENG_DAI.getCode());
        sdDeviceDataService.updateDeviceData(sdDevices, brightness, (long) DevicesTypeItemEnum.JING_SHI_DENG_DAI_STATUS.getCode());

        //添加控制日志
        String beforeState = commonControlService.selectBeforeState(deviceId, (long) DevicesTypeItemEnum.JING_SHI_DENG_DAI.getCode());
        commonControlService.addOperationLog(map,sdDevices,beforeState,controlState);
        return AjaxResult.success(controlState);
    }


}
