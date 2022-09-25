package com.tunnel.business.domain.digitalmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dzy
 * @date 2022/9/6 11:25
 */
public class WjDeviceData {

    @ApiModelProperty("设备实时运行状态")
    private Integer runStatus;

    @ApiModelProperty("设备实时运行数据（开关）无单位")
    private String runDate;

    @ApiModelProperty("单位")
    private String	unit;

    @ApiModelProperty("设备实时运行模式(同步闪、流水闪)")
    private Integer runMode;

    @ApiModelProperty("风速")
    private Double windSpeed;

    @ApiModelProperty("风向")
    private String windDirection;

    @ApiModelProperty("CO值")
    private Double CO;

    @ApiModelProperty("VI值")
    private Double VI;

    @ApiModelProperty("信息内容")
    private String message;

    /**
     *设备状态查询后补字段
     */
    @ApiModelProperty("设备ID")
    private String deviceId;

    @ApiModelProperty("设备类型")
    private String deviceType;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备状态")
    private Integer deviceStatus;

    @ApiModelProperty("设备状态")
    private Integer alarmSource;

    public Integer getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Integer runStatus) {
        this.runStatus = runStatus;
    }

    public String getRunDate() {
        return runDate;
    }

    public void setRunDate(String runDate) {
        this.runDate = runDate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getRunMode() {
        return runMode;
    }

    public void setRunMode(Integer runMode) {
        this.runMode = runMode;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public Double getCO() {
        return CO;
    }

    public void setCO(Double CO) {
        this.CO = CO;
    }

    public Double getVI() {
        return VI;
    }

    public void setVI(Double VI) {
        this.VI = VI;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Integer getAlarmSource() {
        return alarmSource;
    }

    public void setAlarmSource(Integer alarmSource) {
        this.alarmSource = alarmSource;
    }

    @Override
    public String toString() {
        return "WjDeviceData{" +
                "runStatus=" + runStatus +
                ", runDate='" + runDate + '\'' +
                ", unit='" + unit + '\'' +
                ", runMode=" + runMode +
                ", windSpeed=" + windSpeed +
                ", windDirection='" + windDirection + '\'' +
                ", CO=" + CO +
                ", VI=" + VI +
                ", message='" + message + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceStatus=" + deviceStatus +
                ", alarmSource=" + alarmSource +
                '}';
    }
}
