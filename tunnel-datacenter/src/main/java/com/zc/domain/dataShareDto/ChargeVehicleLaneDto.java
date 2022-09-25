package com.zc.domain.dataShareDto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 收费车道信息查询
 */
public class ChargeVehicleLaneDto {

    //系统id
    @JsonProperty(value = "sysid")
    private String sysId;

    //服务代码
    @JsonProperty(value = "service_code")
    private String serviceCode;

    //收费站编号
    @JsonProperty(value = "toll_station_id")
    private String tollStationId;

    //收费站名称
    @JsonProperty(value = "toll_station_name")
    private String tollStationName;

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

    public String getTollStationId() {
        return tollStationId;
    }

    public void setTollStationId(String tollStationId) {
        this.tollStationId = tollStationId;
    }

    public String getTollStationName() {
        return tollStationName;
    }

    public void setTollStationName(String tollStationName) {
        this.tollStationName = tollStationName;
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
        return "ChargeVehicleLaneDto{" +
                "sysId='" + sysId + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", tollStationId='" + tollStationId + '\'' +
                ", tollStationName='" + tollStationName + '\'' +
                ", roadId='" + roadId + '\'' +
                ", roadName='" + roadName + '\'' +
                '}';
    }
}
