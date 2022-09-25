package com.zc.domain.dataShareDto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 桥梁明细
 */
public class BridgeDataDto {

    //系统id
    @JsonProperty(value = "sysid")
    private String sysId;

    //服务代码
    @JsonProperty(value = "service_code")
    private String serviceCode;

    //路线id
    @JsonProperty(value = "road_id")
    private String roadId;

    //桥梁名称
    @JsonProperty(value = "bridge_name")
    private String bridgeName;

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

    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public String getBridgeName() {
        return bridgeName;
    }

    public void setBridgeName(String bridgeName) {
        this.bridgeName = bridgeName;
    }

    @Override
    public String toString() {
        return "BridgeDataDto{" +
                "sysId='" + sysId + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", roadId='" + roadId + '\'' +
                ", bridgeName='" + bridgeName + '\'' +
                '}';
    }
}
