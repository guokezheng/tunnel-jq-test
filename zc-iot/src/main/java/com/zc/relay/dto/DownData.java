package com.zc.relay.dto;

/**
 * 下行数据
 */
public class DownData
{

    /**
     * 下行数据标识
     */
    private String identifier;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 功能标识
     */
    private String functionIdentify;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getFunctionIdentify() {
        return functionIdentify;
    }

    public void setFunctionIdentify(String functionIdentify) {
        this.functionIdentify = functionIdentify;
    }
}
