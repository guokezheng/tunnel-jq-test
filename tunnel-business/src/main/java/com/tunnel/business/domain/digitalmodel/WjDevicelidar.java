package com.tunnel.business.domain.digitalmodel;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author dzy
 * @date 2022/9/4 17:32
 * 万集入参雷达类
 */
public class WjDevicelidar {

    @ApiModelProperty("设备类型 详见《附录-设备类别》")
    private Integer deviceType;

    @ApiModelProperty("设备Ip")
    private String ip;

    @ApiModelProperty("转速")
    private Integer speed;

    @ApiModelProperty("线数")
    private Integer line;

    @ApiModelProperty("点数")
    private Integer points;

    @ApiModelProperty("异常事件")
    private String	errorContent;

    @ApiModelProperty("相机状态，0-离线，1-正常，2-异常，和正晨设备检测状态保持一致")
    private Integer status;

    @ApiModelProperty("设备id")
    private String eqId;

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getErrorContent() {
        return errorContent;
    }

    public void setErrorContent(String errorContent) {
        this.errorContent = errorContent;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEqId() {
        return eqId;
    }

    public void setEqId(String eqId) {
        this.eqId = eqId;
    }

    @Override
    public String toString() {
        return "WjDevicelidar{" +
                "deviceType=" + deviceType +
                ", ip='" + ip + '\'' +
                ", speed=" + speed +
                ", line=" + line +
                ", points=" + points +
                ", errorContent='" + errorContent + '\'' +
                ", status=" + status +
                ", eqId='" + eqId + '\'' +
                '}';
    }
}
