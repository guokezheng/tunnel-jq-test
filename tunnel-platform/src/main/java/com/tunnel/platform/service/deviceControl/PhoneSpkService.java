package com.tunnel.platform.service.deviceControl;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.datacenter.domain.enumeration.PhoneSpkEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.platform.service.SdDeviceControlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class PhoneSpkService {

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Autowired
    private SdDeviceControlService sdDeviceControlService;

    public void onMessage(@RequestBody JSONObject jsonObject) {
        System.out.println("电话广播websocket>>>>>>>>>>>"+jsonObject);

        if (jsonObject.containsKey("attribute") && jsonObject.containsKey("ext")) {
            String attribute = jsonObject.getString("attribute");
            String id = jsonObject.getJSONObject("ext").getString("id");

            String deviceType = jsonObject.getJSONObject("ext").getString("deviceType");
            if (StringUtils.isBlank(deviceType) && jsonObject.containsKey("device")) {
                deviceType = jsonObject.getJSONObject("device").getString("type");
            }
            Integer itemId = null;
            SdDevices devices = new SdDevices();
            devices.setExternalDeviceId(id);
            if ("linePhoneExt".equals(deviceType) || "masterPhoneExt".equalsIgnoreCase(deviceType)) {
                devices.setEqType(DevicesTypeEnum.ET.getCode());
                itemId = DevicesTypeItemEnum.JIN_JI_DIAN_HUA.getCode();
            } else if ("spk".equals(deviceType)) {
                devices.setEqType(DevicesTypeEnum.LS.getCode());
                itemId = DevicesTypeItemEnum.GUANG_BO.getCode();
            }
            SdDevices device = sdDevicesMapper.selectPhoneSpk(devices);
            String data = PhoneSpkEnum.getValue(attribute);

            sdDeviceControlService.updateDeviceData(device, data, itemId);
        }
    }
}
