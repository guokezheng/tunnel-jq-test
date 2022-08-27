package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 枢纽立交
 */
@Data
public class JunctionInterchange {

    @JsonProperty(value = "road_id")
    private String roadId;

    @JsonProperty(value = "stake_num")
    private String stakeNum;

    private String lat;

    private String lng;

    private String id;

    private String name;
}
