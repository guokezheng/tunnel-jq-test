package com.tunnel.business.domain.digitalmodel;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author dzy
 * @date 2022/9/7 10:55
 */
public class SdRadarDevice {

    /**
     * 万集字段
     */
    @ApiModelProperty("设备类别")
    private String deviceType;

    @ApiModelProperty("设备ID")
    private String deviceId;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备编码")
    private String deviceCode;

    @ApiModelProperty("0-离线，1-正常    1-在线；2-离线；3-故障")
    private Integer deviceStatus;

    @ApiModelProperty("分辨率1e-7°，东经为正，西经为负 经度")
    private Double longitude;

    @ApiModelProperty("分辨率1e-7°，北纬为正，南纬为负 纬度")
    private Double latitude;

    @ApiModelProperty("方向")
    private String direction;

    @ApiModelProperty("桩号")
    private String stakeNum;

    @ApiModelProperty("模型位置姿态缩放信息，需要孪生平台预设信息")
    private String transform;

    /**
     * 设备数据：包括设备实时数据、实时状态，根据deviceType区分
     */
//    private WjDeviceData deviceData;

    private JSONObject deviceData;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getStakeNum() {
        return stakeNum;
    }

    public void setStakeNum(String stakeNum) {
        this.stakeNum = stakeNum;
    }

    public String getTransform() {
        return transform;
    }

    public void setTransform(String transform) {
        this.transform = transform;
    }

    public JSONObject getDeviceData() {
        return deviceData;
    }

    public void setDeviceData(JSONObject deviceData) {
        this.deviceData = deviceData;
    }

    @Override
    public String toString() {
        return "SdRadarDevice{" +
                "deviceType='" + deviceType + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceCode='" + deviceCode + '\'' +
                ", deviceStatus=" + deviceStatus +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", direction='" + direction + '\'' +
                ", stakeNum='" + stakeNum + '\'' +
                ", transform='" + transform + '\'' +
                ", deviceData=" + deviceData +
                '}';
    }
}
