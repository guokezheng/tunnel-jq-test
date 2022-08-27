package com.zc.relay.dto;

/**
 * 设备状态数据
 */
public class UplinkDeviceStateData {

    /**
     * 离线
     */
    public final static Integer  OFF_LINE = 0;

    /**
     * 在线
     */
    public final static Integer  ON_LINE = 1;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 在线状态
     */
    private Integer state;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        if (!ON_LINE.equals(state) && !OFF_LINE.equals(state)) return;
        this.state = state;
    }

    /**
     * 设置成在线
     */
    public void setOnlineState() {
        this.state = ON_LINE;
    }

    /**
     * 设置成离线
     */
    public void setOfflineState() {
        this.state = OFF_LINE;
    }

}
