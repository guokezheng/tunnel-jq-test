package com.tunnel.webthings.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 这个是所有主题父类
 * @author ZHC
 * {@code @date} 2022/7/21
 */
@ApiModel("所有主题父类")
public class ReceiveTopic {

    @ApiModelProperty("设备编号们")
    private List<String> devNo;

    @ApiModelProperty("设备编号")
    private String devN;

    @ApiModelProperty("设备类型")
    private String devType;

    @ApiModelProperty("发送时间")
    private String timeStamp;

    @ApiModelProperty("直接类型")
    private String directType;

    @ApiModelProperty("直接类型描述")
    private String directTypeDesc;

    @ApiModelProperty("随机数")
    private String random;

    @ApiModelProperty("发送人")
    private String user;

    @ApiModelProperty("信息")
    private String ex;

    public List<String> getDevNo() {
        return devNo;
    }

    public void setDevNo(List<String> devNo) {
        this.devNo = devNo;
    }

    public String getDevN() {
        return devN;
    }

    public void setDevN(String devN) {
        this.devN = devN;
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

    public String getDirectType() {
        return directType;
    }

    public void setDirectType(String directType) {
        this.directType = directType;
    }

    public String getDirectTypeDesc() {
        return directTypeDesc;
    }

    public void setDirectTypeDesc(String directTypeDesc) {
        this.directTypeDesc = directTypeDesc;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEx() {
        return ex;
    }

    public void setEx(String ex) {
        this.ex = ex;
    }

    @Override
    public String toString() {
        return "ReceiveTopic{" +
                "devNo=" + devNo +
                ", devN='" + devN + '\'' +
                ", devType='" + devType + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", directType='" + directType + '\'' +
                ", directTypeDesc='" + directTypeDesc + '\'' +
                ", random='" + random + '\'' +
                ", user='" + user + '\'' +
                ", ex='" + ex + '\'' +
                '}';
    }
}
