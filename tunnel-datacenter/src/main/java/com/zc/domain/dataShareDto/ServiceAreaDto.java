package com.zc.domain.dataShareDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 服务区
 */
public class ServiceAreaDto {

    //系统id
    @JsonProperty(value = "sysid")
    private String sysId;

    //服务代码
    @JsonProperty(value = "service_code")
    private String serviceCode;

    //服务区编码
    @JsonProperty(value = "serviceArea_id")
    private String serviceAreaId;

    //服务区名称
    @JsonProperty(value = "servicearea_name")
    private String serviceAreaName;

    //国标路线编码
    @JsonProperty(value = "road_id")
    private String roadId;

    //国标路线名称
    @JsonProperty(value = "road_name")
    private String roadName;

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

    public String getServiceAreaId() {
        return serviceAreaId;
    }

    public void setServiceAreaId(String serviceAreaId) {
        this.serviceAreaId = serviceAreaId;
    }

    public String getServiceAreaName() {
        return serviceAreaName;
    }

    public void setServiceAreaName(String serviceAreaName) {
        this.serviceAreaName = serviceAreaName;
    }

    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    @Override
    public String toString() {
        return "ServiceAreaDto{" +
                "sysId='" + sysId + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", serviceAreaId='" + serviceAreaId + '\'' +
                ", serviceAreaName='" + serviceAreaName + '\'' +
                ", roadId='" + roadId + '\'' +
                ", roadName='" + roadName + '\'' +
                '}';
    }
}
