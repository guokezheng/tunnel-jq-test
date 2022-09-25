package com.tunnel.business.domain.digitalmodel;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author dzy
 * @date 2022/9/4 17:39
 */
public class WjDeviceCamera {

    @ApiModelProperty("设备类型")
    private Integer deviceType;

    @ApiModelProperty("设备Ip")
    private String	ip;

    @ApiModelProperty("码流")
    private Integer rate;

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

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
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
        return "WjDeviceCamera{" +
                "deviceType=" + deviceType +
                ", ip='" + ip + '\'' +
                ", rate=" + rate +
                ", errorContent='" + errorContent + '\'' +
                ", status=" + status +
                ", eqId='" + eqId + '\'' +
                '}';
    }
}
