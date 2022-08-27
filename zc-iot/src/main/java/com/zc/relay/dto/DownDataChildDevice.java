package com.zc.relay.dto;

/**
 * 下行数据（网关子设备）
 */
public class DownDataChildDevice extends DownData{


    /**
     * 子设备编号
     */
    private String childDeviceCode;

    public String getChildDeviceCode() {
        return childDeviceCode;
    }

    public void setChildDeviceCode(String childDeviceCode) {
        this.childDeviceCode = childDeviceCode;
    }

}
