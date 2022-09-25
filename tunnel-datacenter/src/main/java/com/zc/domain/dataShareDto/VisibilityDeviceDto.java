package com.zc.domain.dataShareDto;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 能见度设备数据查询
 */
public class VisibilityDeviceDto {

    //系统id
    @JsonProperty(value = "sysid")
    private String sysId;

    //服务代码
    @JsonProperty(value = "service_code")
    private String serviceCode;

    //设备编号
    @JsonProperty(value = "dev_no")
    private String devNo;

    //开始时间
    @JsonProperty(value = "start_time")
    private String startTime;

    //结束时间
    @JsonProperty(value = "end_time")
    private String endTime;

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "VisibilityDeviceDto{" +
                "sysId='" + sysId + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", devNo='" + devNo + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
