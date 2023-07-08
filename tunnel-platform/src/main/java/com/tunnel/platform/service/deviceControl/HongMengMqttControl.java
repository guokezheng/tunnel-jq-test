package com.tunnel.platform.service.deviceControl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.strategy.factory.DeviceDataStrategyFactory;
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.business.strategy.service.DeviceDataStrategyService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.mqtt.service.HongMengMqttService;
import com.tunnel.deal.mqtt.strategy.HongMengMqttStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * describe: 鸿蒙控制器--胡山隧道-Mqtt控制
 *
 * @author zs
 * @date 2023/5/25
 */
@Component
public class HongMengMqttControl implements GeneralControlBean {

    @Autowired
    private HongMengMqttStrategyFactory hongMengMqttStrategyFactory;

    @Autowired
    private CommonControlService commonControlService;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private DeviceDataStrategyFactory deviceDataStrategyFactory;

    /**
     * 设备控制--工作台单个设备控制
     *
     * @param map
     * @param sdDevices
     * @return
     */
    @Override
    public AjaxResult control(Map<String, Object> map, SdDevices sdDevices) {
        String eqType = String.valueOf(sdDevices.getEqType());
        boolean isopen = commonControlService.queryAnalogControlConfig();
        if (isopen) {
            //设备模拟控制开启
            DeviceDataStrategyService deviceDataStrategyService = deviceDataStrategyFactory.strategy(eqType);
            AjaxResult ajaxResult = deviceDataStrategyService.analogControl(map,sdDevices);
            return ajaxResult;
        }

        //实际控制设备
        HongMengMqttService hongMengMqttService = hongMengMqttStrategyFactory.strategy(eqType);
        AjaxResult ajaxResult = hongMengMqttService.deviceControl(map,sdDevices);
        return ajaxResult;
    }

    /**
     * 设备控制+模拟控制--其他模块调用的统一控制方法
     *
     * @param map
     * @return
     */
    @Override
    public Integer controlDevices(Map<String, Object> map) {

        String deviceId = map.get("devId").toString();
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(deviceId);
        AjaxResult ajaxResult = control(map,sdDevices);
        Integer controlState = Integer.valueOf(String.valueOf(ajaxResult.get("data")));
        return controlState;
    }

//    /**
//     * 模拟控制方法
//     *
//     * @param map
//     * @param sdDevices
//     * @return
//     */
//    @Override
//    public Integer analogControl(Map<String, Object> map, SdDevices sdDevices) {
//        return commonControlService.analogControl(map,sdDevices);
//    }
}
