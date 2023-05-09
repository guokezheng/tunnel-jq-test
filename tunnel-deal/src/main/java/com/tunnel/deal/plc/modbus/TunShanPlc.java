package com.tunnel.deal.plc.modbus;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * describe: 上海吞山PLC控制类
 *
 * @author zs
 * @date 2023/4/4
 */
@Component
public class TunShanPlc implements GeneralControlBean {


    @Autowired
    private ISdDevicesService sdDevicesService;

//    /**
//     * 控制方法
//     *
//     * @param sdDevices 设备信息
//     * @param state     控制状态
//     * @return
//     */
//    @Override
//    public AjaxResult control(SdDevices sdDevices, String state) {
//        Integer controlState = ModbusTcpHandle.getInstance().toControlDev(Integer.parseInt(state), sdDevices);
//        if(controlState == 1){
//            return AjaxResult.success(1);
//        }else {
//            return AjaxResult.error("",0);
//        }
//    }
//
//    @Override
//    public AjaxResult control(Map<String, Object> map) {
//        //设备ID
//        String devId = Optional.ofNullable(map.get("devId")).orElse("").toString();
//
//        if (devId == null || "".equals(devId)) {
//            AjaxResult.error("未指定设备");
//        }
//        //设备状态
//        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
//        //设备信息
//        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);
//
//        Integer controlState = ModbusTcpHandle.getInstance().toControlDev(Integer.parseInt(state), sdDevices);
//        if(controlState == 1){
//            return AjaxResult.success(1);
//        }else {
//            return AjaxResult.error("",0);
//        }
//    }

    @Override
    public AjaxResult control(Map<String, Object> map, SdDevices sdDevices) {
        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();

        Integer controlState = ModbusTcpHandle.getInstance().toControlDev(Integer.parseInt(state), sdDevices);
        if(controlState == 1){
            return AjaxResult.success(1);
        }else {
            return AjaxResult.error("",0);
        }
    }
}
