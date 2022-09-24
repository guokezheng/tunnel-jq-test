package com.tunnel.webthings.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dzy
 * @date 2022/7/20 10:59
 * 合流区预警设备故障告警类
 */
@ApiModel("合流区预警设备故障告警类")
public class ConfluenceDevFaultWarn {

    @ApiModelProperty("设备编号")
    private String devCode;

    @ApiModelProperty("设备类型")
    private Integer devType;

    @ApiModelProperty("设备名称")
    private String devName;

    @ApiModelProperty("消息类型")
    private String msgType;

    @ApiModelProperty("消息时间")
    private String devTime;

    @ApiModelProperty("触发类型(1 告警 0 解除)")
    private Integer envTriType;

    @ApiModelProperty("故障代码")
    private String faultCode;

    @ApiModelProperty("事件坐标经度")
    private String envLon;

    @ApiModelProperty("事件坐标纬度")
    private String envLat;

    @ApiModelProperty("备注")
    private String remark;

    public String getDevCode() {
        return devCode;
    }

    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }

    public Integer getDevType() {
        return devType;
    }

    public void setDevType(Integer devType) {
        this.devType = devType;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getDevTime() {
        return devTime;
    }

    public void setDevTime(String devTime) {
        this.devTime = devTime;
    }

    public Integer getEnvTriType() {
        return envTriType;
    }

    public void setEnvTriType(Integer envTriType) {
        this.envTriType = envTriType;
    }

    public String getFaultCode() {
        return faultCode;
    }

    public void setFaultCode(String faultCode) {
        this.faultCode = faultCode;
    }

    public String getEnvLon() {
        return envLon;
    }

    public void setEnvLon(String envLon) {
        this.envLon = envLon;
    }

    public String getEnvLat() {
        return envLat;
    }

    public void setEnvLat(String envLat) {
        this.envLat = envLat;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ConfluenceDevFaultWarn{" +
                "devCode='" + devCode + '\'' +
                ", devType=" + devType +
                ", devName='" + devName + '\'' +
                ", msgType='" + msgType + '\'' +
                ", devTime='" + devTime + '\'' +
                ", envTriType=" + envTriType +
                ", faultCode='" + faultCode + '\'' +
                ", envLon='" + envLon + '\'' +
                ", envLat='" + envLat + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
