package com.tunnel.business.domain.digitalmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dzy
 * @date 2022/9/14 9:59
 */
public class SdDeviceDataItem {

    @ApiModelProperty("设备数据")
    private String data;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("设备id")
    private String deviceId;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "SdDeviceDataItem{" +
                "data='" + data + '\'' +
                ", unit='" + unit + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
