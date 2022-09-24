package com.zc.domain.dataShareDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 收费站车流量
 */
public class TollStatTrafficFlowDto {

    //系统id
    @JsonProperty(value = "sysid")
    private String sysId;

    //服务代码
    @JsonProperty(value = "service_code")
    private String serviceCode;

    //出入口标识： 1入口  2出口
    @JsonProperty(value = "station_type")
    private String stationType;

    //开始时间
    @JsonProperty(value = "start_time")
    private String startTime;

    //终止时间
    @JsonProperty(value = "end_time")
    private String endTime;

    //收费站编码省标
    @JsonProperty(value = "station_id")
    private String stationId;

    //收费站编码国标
    @JsonProperty(value = "station_id_gb")
    private String stationIdGb;

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

    public String getStationType() {
        return stationType;
    }

    public void setStationType(String stationType) {
        this.stationType = stationType;
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

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationIdGb() {
        return stationIdGb;
    }

    public void setStationIdGb(String stationIdGb) {
        this.stationIdGb = stationIdGb;
    }

    @Override
    public String toString() {
        return "TollStatTrafficFlowDto{" +
                "sysId='" + sysId + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", stationType='" + stationType + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", stationId='" + stationId + '\'' +
                ", stationIdGb='" + stationIdGb + '\'' +
                '}';
    }
}
