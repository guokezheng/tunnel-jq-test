package com.tunnel.deal.plc.modbus;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.business.strategy.service.CommonControlService;
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

    @Autowired
    private CommonControlService commonControlService;

    @Autowired
    private ISdOperationLogService sdOperationLogService;


    @Override
    public AjaxResult control(Map<String, Object> map, SdDevices sdDevices) {
        boolean isopen = commonControlService.queryAnalogControlConfig();
        if (isopen) {
            //设备模拟控制开启
            return commonControlService.excecuteAnalogControl(sdDevices,map);
        }

        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();

        //控制设备之前获取设备状态
        String beforeState = commonControlService.selectBeforeState(sdDevices);

        Integer controlState = ModbusTcpHandle.getInstance().toControlDev(Integer.parseInt(state), sdDevices);

        //手动控制
//        String controlType = DeviceControlTypeEnum.AUTO_CONTROL.getCode();
        commonControlService.addOperationLog(map,sdDevices,beforeState,controlState);

      return AjaxResult.success(controlState);
    }

    /**
     * 设备控制
     *
     * @param map
     * @return
     */
    @Override
    public Integer controlDevices(Map<String, Object> map) {
        int controlState = 0;

        boolean isopen = commonControlService.queryAnalogControlConfig();

        String devId = map.get("devId").toString();
        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);


        if (!isopen) {
            //连接设备进行控制
            controlState = ModbusTcpHandle.getInstance().toControlDev(Integer.parseInt(state), sdDevices);
//            controlState = controlDevice(map,sdDevices);
        } else {
            //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
            controlState = commonControlService.analogControl(map,sdDevices);
        }

//        //车指控制方法
//        controlState = controlLaneIndicator(map,controlState, isopen, state, sdDevices);

        SdOperationLog sdOperationLog = commonControlService.getOperationLog(map,sdDevices,controlState);
        sdOperationLogService.insertSdOperationLog(sdOperationLog);

        //通过websocket推送到前端
        String[] states = new String[4];
        states[0] = state;
        commonControlService.sendNowDeviceStatusByWebsocket(sdDevices,states,sdOperationLog,"cz");

        return controlState;
    }




}
