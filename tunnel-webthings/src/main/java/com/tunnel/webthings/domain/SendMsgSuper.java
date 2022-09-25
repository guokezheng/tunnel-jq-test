package com.tunnel.webthings.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author dzy
 * @date 2022/7/21 14:34
 */
public class SendMsgSuper {

    @NotEmpty
    @ApiModelProperty("设备编号")
    private String devNo;

    @NotEmpty
    @ApiModelProperty("设备类型")
    private String devType;

    @ApiModelProperty("时间戳")
    private String timeStamp;

    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "SendMsgSuper{" +
                "devNo='" + devNo + '\'' +
                ", devType='" + devType + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
