package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 桥梁明细相应参数
 */
@Data
public class BridgeData {

    @JsonProperty(value = "road_id")
    private String roadId;

    @JsonProperty(value = "stake_start")
    private String stakeStart;

    @JsonProperty(value = "stake_end")
    private String stakeEnd;

    @JsonProperty(value = "bridge_name")
    private String bridgeName;

    @JsonProperty(value = "center_stake")
    private String centerStake;

    @JsonProperty(value = "user_id")
    private String userId;

    @JsonProperty(value = "dept_id")
    private String deptId;

    @JsonProperty(value = "del_flag")
    private String delFlag;

    private String remark;

    @JsonProperty(value = "management_range_id")
    private String managementRangeId;

    private String direction;

}
