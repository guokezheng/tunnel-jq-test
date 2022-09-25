package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 桥梁明细相应参数
 */
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


    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public String getStakeStart() {
        return stakeStart;
    }

    public void setStakeStart(String stakeStart) {
        this.stakeStart = stakeStart;
    }

    public String getStakeEnd() {
        return stakeEnd;
    }

    public void setStakeEnd(String stakeEnd) {
        this.stakeEnd = stakeEnd;
    }

    public String getBridgeName() {
        return bridgeName;
    }

    public void setBridgeName(String bridgeName) {
        this.bridgeName = bridgeName;
    }

    public String getCenterStake() {
        return centerStake;
    }

    public void setCenterStake(String centerStake) {
        this.centerStake = centerStake;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getManagementRangeId() {
        return managementRangeId;
    }

    public void setManagementRangeId(String managementRangeId) {
        this.managementRangeId = managementRangeId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "BridgeData{" +
                "roadId='" + roadId + '\'' +
                ", stakeStart='" + stakeStart + '\'' +
                ", stakeEnd='" + stakeEnd + '\'' +
                ", bridgeName='" + bridgeName + '\'' +
                ", centerStake='" + centerStake + '\'' +
                ", userId='" + userId + '\'' +
                ", deptId='" + deptId + '\'' +
                ", delFlag='" + delFlag + '\'' +
                ", remark='" + remark + '\'' +
                ", managementRangeId='" + managementRangeId + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
