package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 收费站车流量信息
 */
@Data
public class TollStatCarFlow {

    @JsonProperty(value = "total_flow")
    private String totalFlow;

    @JsonProperty(value = "bus1_flow")
    private String bus1Flow;

    @JsonProperty(value = "bus2_flow")
    private String bus2Flow;

    @JsonProperty(value = "bus3_flow")
    private String bus3Flow;

    @JsonProperty(value = "bus4_flow")
    private String bus4Flow;

    @JsonProperty(value = "truck1_flow")
    private String truck1Flow;

    @JsonProperty(value = "truck2_flow")
    private String truck2Flow;

    @JsonProperty(value = "truck3_flow")
    private String truck3Flow;

    @JsonProperty(value = "truck4_flow")
    private String truck4Flow;

    @JsonProperty(value = "truck5_flow")
    private String truck5Flow;

    @JsonProperty(value = "truck6_flow")
    private String truck6Flow;

    @JsonProperty(value = "total_free")
    private String totalFree;

    @JsonProperty(value = "bus1_fee")
    private String bus1Fee;

    @JsonProperty(value = "bus2_fee")
    private String bus2Fee;

    @JsonProperty(value = "bus3_fee")
    private String bus3Fee;

    @JsonProperty(value = "bus4_fee")
    private String bus4Fee;

    @JsonProperty(value = "truck1_fee")
    private String truck1Fee;

    @JsonProperty(value = "truck2_fee")
    private String truck2Fee;

    @JsonProperty(value = "truck3_fee")
    private String truck3Fee;

    @JsonProperty(value = "truck4_fee")
    private String truck4Fee;

    @JsonProperty(value = "truck5_fee")
    private String truck5Fee;

    @JsonProperty(value = "truck6_fee")
    private String truck6Fee;

}
