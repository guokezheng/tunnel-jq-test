package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 收费车道信息
 */
public class TollLane {

    @JsonProperty(value = "toll_lane_id")
    private String tollLaneId;

    @JsonProperty(value = "toll_lane_gb_type")
    private String tollLaneGbType;

    @JsonProperty(value = "toll_lane_province_id")
    private String tollLaneProvinceId;

    @JsonProperty(value = "toll_lane_tidal_time")
    private String tollLaneTidalTime;

    @JsonProperty(value = "use_status")
    private String useStatus;

    @JsonProperty(value = "toll_lane_hex")
    private String tollLaneHex;

    @JsonProperty(value = "toll_station_id")
    private String tollStationId;

    @JsonProperty(value = "toll_station_name")
    private String tollStationName;

    @JsonProperty(value = "road_id")
    private String roadId;

    @JsonProperty(value = "road_name")
    private String roadName;

    public String getTollLaneId() {
        return tollLaneId;
    }

    public void setTollLaneId(String tollLaneId) {
        this.tollLaneId = tollLaneId;
    }

    public String getTollLaneGbType() {
        return tollLaneGbType;
    }

    public void setTollLaneGbType(String tollLaneGbType) {
        this.tollLaneGbType = tollLaneGbType;
    }

    public String getTollLaneProvinceId() {
        return tollLaneProvinceId;
    }

    public void setTollLaneProvinceId(String tollLaneProvinceId) {
        this.tollLaneProvinceId = tollLaneProvinceId;
    }

    public String getTollLaneTidalTime() {
        return tollLaneTidalTime;
    }

    public void setTollLaneTidalTime(String tollLaneTidalTime) {
        this.tollLaneTidalTime = tollLaneTidalTime;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public String getTollLaneHex() {
        return tollLaneHex;
    }

    public void setTollLaneHex(String tollLaneHex) {
        this.tollLaneHex = tollLaneHex;
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
        return "TollLane{" +
                "tollLaneId='" + tollLaneId + '\'' +
                ", tollLaneGbType='" + tollLaneGbType + '\'' +
                ", tollLaneProvinceId='" + tollLaneProvinceId + '\'' +
                ", tollLaneTidalTime='" + tollLaneTidalTime + '\'' +
                ", useStatus='" + useStatus + '\'' +
                ", tollLaneHex='" + tollLaneHex + '\'' +
                ", tollStationId='" + tollStationId + '\'' +
                ", tollStationName='" + tollStationName + '\'' +
                ", roadId='" + roadId + '\'' +
                ", roadName='" + roadName + '\'' +
                '}';
    }
}
