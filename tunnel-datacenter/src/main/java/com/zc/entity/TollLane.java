package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 收费车道信息
 */
@Data
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
}
