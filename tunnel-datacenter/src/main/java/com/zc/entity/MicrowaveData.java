package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 微波车检器周期过车数据查询2
 */
@Data
public class MicrowaveData {

    @JsonProperty(value = "dev_no")
    private String devNo;

    @JsonProperty(value = "unique_id")
    private String uniqueId;

    @JsonProperty(value = "error_code")
    private String errorCode;

    private String investigation;

    private String cycle;

    @JsonProperty(value = "trans_time")
    private String transTime;

    @JsonProperty(value = "lane_no")
    private String laneNo;

    private String traffic;

    private String speed;

    @JsonProperty(value = "follow_percent")
    private String followPercent;

    @JsonProperty(value = "avg_headway")
    private String avgHeadway;

    @JsonProperty(value = "time_share")
    private String timeShare;

    @JsonProperty(value = "medium_small_bus_traffic")
    private String mediumSmallBusTraffic;

    @JsonProperty(value = "medium_small_bus_avg_speed")
    private String mediumSmallBusAvgSpeed;

    @JsonProperty(value = "small_truck_traffic")
    private String smallTruckTraffic;

    @JsonProperty(value = "small_truck_avg_speed")
    private String smallTruckAvgSpeed;

    @JsonProperty(value = "big_bus_traffic")
    private String bigBusTraffic;

    @JsonProperty(value = "big_bus_avg_speed")
    private String bigBusAvgSpeed;

    @JsonProperty(value = "medium_truck_traffic")
    private String mediumTruckTraffic;

    @JsonProperty(value = "medium_truck_avg_speed")
    private String mediumTruckAvgSpeed;

    @JsonProperty(value = "big_truck_traffic")
    private String bigTruckTraffic;

    @JsonProperty(value = "big_truck_avg_speed")
    private String bigTruckAvgSpeed;

    @JsonProperty(value = "huge_truck_traffic")
    private String hugeTruckTraffic;

    @JsonProperty(value = "huge_truck_avg_speed")
    private String hugeTruckAvgSpeed;

    @JsonProperty(value = "container_truck_traffic")
    private String containerTruckTraffic;

    @JsonProperty(value = "container_truck_avg_speed")
    private String containerTruckAvgSpeed;

    @JsonProperty(value = "tractor_traffic")
    private String tractorTraffic;

    @JsonProperty(value = "tractor_avg_speed")
    private String tractorAvgSpeed;

    @JsonProperty(value = "motor_traffic")
    private String motorTraffic;

    @JsonProperty(value = "motor_avg_speed")
    private String motorSvgSpeed;

    @JsonProperty(value = "time_stamp")
    private String timeStamp;
}
