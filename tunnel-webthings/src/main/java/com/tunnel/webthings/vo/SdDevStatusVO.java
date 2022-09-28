package com.tunnel.webthings.vo;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tunnel.webthings.domain.DevStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ZHC
 * {@code @date} 2022/7/15 14:33
 * 设备状态信息业务类
 */
@ApiModel("设备状态信息业务类")
public class SdDevStatusVO {

    @ApiModelProperty("设备编号们")
    private String devNo;

    @ApiModelProperty("设备编号")
    private String devN;

    @ApiModelProperty("设备类型")
    private String devType;

    @ApiModelProperty("设备上线时间")
    private String loginTime;

    @ApiModelProperty("设备状态")
    private String devStatus;

    @ApiModelProperty("网络状态")
    private String netStatus;

    @ApiModelProperty("设备状态描述")
    private String devStatusRemark;

    @ApiModelProperty("设备状态描述")
    private String netStatusRemark;

    @ApiModelProperty("时间戳")
    private String timeStamp;

    @ApiModelProperty("随机数")
    private String random;

    @ApiModelProperty("直接类型")
    private String directType;

    @ApiModelProperty("直接类型描述")
    private String directTypeDesc;


    private DevStatus expands;

    private String ex;

    public void setEx(DevStatus expands) {
        JSONObject jsonObject = JSONUtil.parseObj(expands);
        this.ex = jsonObject.toString();
    }


    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
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

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(String devStatus) {
        this.devStatus = devStatus;
    }

    public String getNetStatus() {
        return netStatus;
    }

    public void setNetStatus(String netStatus) {
        this.netStatus = netStatus;
    }

    public String getDevStatusRemark() {
        return devStatusRemark;
    }

    public void setDevStatusRemark(String devStatusRemark) {
        this.devStatusRemark = devStatusRemark;
    }

    public String getNetStatusRemark() {
        return netStatusRemark;
    }

    public void setNetStatusRemark(String netStatusRemark) {
        this.netStatusRemark = netStatusRemark;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
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

    public DevStatus getExpands() {
        return expands;
    }

    public void setExpands(DevStatus expands) {
        this.expands = expands;
    }

    public String getEx() {
        return ex;
    }

    public void setEx(String ex) {
        this.ex = ex;
    }

    @Override
    public String toString() {
        return "SdDevStatusVO{" +
                "devNo='" + devNo + '\'' +
                ", devN='" + devN + '\'' +
                ", devType='" + devType + '\'' +
                ", loginTime='" + loginTime + '\'' +
                ", devStatus='" + devStatus + '\'' +
                ", netStatus='" + netStatus + '\'' +
                ", devStatusRemark='" + devStatusRemark + '\'' +
                ", netStatusRemark='" + netStatusRemark + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", random='" + random + '\'' +
                ", directType='" + directType + '\'' +
                ", directTypeDesc='" + directTypeDesc + '\'' +
                ", expands=" + expands +
                ", ex='" + ex + '\'' +
                '}';
    }
}
