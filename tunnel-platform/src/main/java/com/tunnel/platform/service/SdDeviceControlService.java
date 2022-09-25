package com.tunnel.platform.service;

import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.deal.guidancelamp.control.util.GuidanceLampHandle;
import com.tunnel.deal.plc.modbus.ModbusTcpHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class SdDeviceControlService {

    private static final Logger log = LoggerFactory.getLogger(SdDeviceControlService.class);

    @Autowired
    private ISdDevicesService sdDevicesService;

    public Integer controlDevices(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            //当前设备控制参数为空，直接返回
            log.error("当前设备控制参数为空");
            return 0;
        }
        //控制车指
        if (map.get("devId") == null || map.get("devId").toString().equals("")) {
            throw new RuntimeException("未指定设备，请联系管理员");
        } else if (map.get("state") == null || map.get("state").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的状态信息，请联系管理员");
        }
        String devId = map.get("devId").toString();
        String state = map.get("state").toString();
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);
        int controlState = 0;
        String fireMark = "";
        if (sdDevices != null && sdDevices.getEqType().longValue() == DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode().longValue()) {
            controlState = ModbusTcpHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices);
            //控制诱导灯
        } else if (sdDevices != null && sdDevices.getEqType().longValue() == DevicesTypeEnum.YOU_DAO_DENG.getCode().longValue()) {
            if (map.get("brightness") == null || map.get("brightness").toString().equals("")) {
                throw new RuntimeException("未指定设备需要变更的亮度信息，请联系管理员");
            } else if (map.get("frequency") == null || map.get("frequency").toString().equals("")) {
                throw new RuntimeException("未指定设备需要变更的频率信息，请联系管理员");
            }
            String brightness = map.get("brightness").toString();
            String frequency = map.get("frequency").toString();
            controlState = GuidanceLampHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices, brightness, frequency, null);
            //控制疏散标志
        } else if (sdDevices != null && sdDevices.getEqType().longValue() == DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode().longValue()) {
            if (map.get("brightness") == null || map.get("brightness").toString().equals("")) {
                throw new RuntimeException("未指定设备需要变更的亮度信息，请联系管理员");
            } else if (map.get("frequency") == null || map.get("frequency").toString().equals("")) {
                throw new RuntimeException("未指定设备需要变更的频率信息，请联系管理员");
            } else if (map.get("fireMark") == null || map.get("fireMark").toString().equals("")) {
                throw new RuntimeException("未指定设备需要变更的标号位置信息，请联系管理员");
            }
            fireMark = map.get("fireMark").toString();
            String brightness = map.get("brightness").toString();
            String frequency = map.get("frequency").toString();
            controlState = GuidanceLampHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices, brightness, frequency, fireMark);
        }
        return controlState;
    }
}
