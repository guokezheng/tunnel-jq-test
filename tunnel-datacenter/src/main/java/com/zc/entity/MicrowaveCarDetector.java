package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 微波车检器周期过车数据查询
 */
@Data
public class MicrowaveCarDetector {

    @JsonProperty(value = "dev_no")
    private String devNo;

    @JsonProperty(value = "trans_time")
    private String transTime;

    private String lane;

    @JsonProperty(value = "vol_cnt")
    private String volCnt;

    @JsonProperty(value = "vol_samll_cnt")
    private String volSamllCnt;

    @JsonProperty(value = "vol_medium_cnt")
    private String volMediumCnt;

    @JsonProperty(value = "vol_large1_cnt")
    private String volLarge1Cnt;

    @JsonProperty(value = "vol_large2_cnt")
    private String volLarge2Cnt;

    @JsonProperty(value = "vol_large3_cnt")
    private String volLarge3Cnt;

    @JsonProperty(value = "ave_speed")
    private String aveSpeed;

    @JsonProperty(value = "small_speed")
    private String smallSpeed;

    @JsonProperty(value = "medium_speed")
    private String mediumSpeed;

    @JsonProperty(value = "large1_speed")
    private String large1Speed;

    @JsonProperty(value = "large2_speed")
    private String large2Speed;

    @JsonProperty(value = "large3_speed")
    private String large3Speed;

    @JsonProperty(value = "ave_head_way")
    private String aveHeadWay;

    @JsonProperty(value = "ave_length")
    private String aveLength;

    @JsonProperty(value = "ave_occupancy")
    private String aveOccupancy;

    @JsonProperty(value = "create_time")
    private String createTime;


}
