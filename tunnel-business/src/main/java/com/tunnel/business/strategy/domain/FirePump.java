package com.tunnel.business.strategy.domain;

import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.strategy.service.DeviceDataStrategyService;

import java.util.Map;

/**
 * describe: 消防水泵
 *
 * @author zs
 * @date 2023/4/23
 */
public class FirePump implements DeviceDataStrategyService {
    /**
     * 获取实时数据
     *
     * @param devices
     * @param data
     */
    @Override
    public void getDeviceData(Map<String, String> devices, SdDeviceData data) {
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.XIAO_FANG_SHUI_BENG.getCode()) {
            devices.put("state", data.getData());
        } else if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.XIAO_FANG_SHUAN_STATUS.getCode()) {
            devices.put("xfsStatus", data.getData());
        } else if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.XIAO_FANG_DIAN_LIU_IA.getCode()) {
            devices.put("ia", data.getData());
        } else if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.XIAO_FANG_DIAN_LIU_IB.getCode()) {
            devices.put("ib", data.getData());
        } else if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.XIAO_FANG_DIAN_LIU_IC.getCode()) {
            devices.put("ic", data.getData());
        } else if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.XIAO_FANG_DIAN_YA_IA.getCode()) {
            devices.put("va", data.getData());
        } else if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.XIAO_FANG_DIAN_YA_IB.getCode()) {
            devices.put("vb", data.getData());
        } else if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.XIAO_FANG_DIAN_YA_IC.getCode()) {
            devices.put("vc", data.getData());
        }

    }
}
