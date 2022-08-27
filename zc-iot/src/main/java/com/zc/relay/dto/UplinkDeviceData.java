package com.zc.relay.dto;

import java.util.List;

/**
 * 上报数据设备实时数据
 */
public class UplinkDeviceData {

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备功能列表
     */
    private List<UplinkFunctionData> uplinkFunctionData;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public List<UplinkFunctionData> getUplinkFunctionData() {
        return uplinkFunctionData;
    }

    public void setUplinkFunctionData(List<UplinkFunctionData> uplinkFunctionData) {
        this.uplinkFunctionData = uplinkFunctionData;
    }
}
