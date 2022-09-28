package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 微波车检器周期过车数据查询2
 */
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

    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getInvestigation() {
        return investigation;
    }

    public void setInvestigation(String investigation) {
        this.investigation = investigation;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getLaneNo() {
        return laneNo;
    }

    public void setLaneNo(String laneNo) {
        this.laneNo = laneNo;
    }

    public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getFollowPercent() {
        return followPercent;
    }

    public void setFollowPercent(String followPercent) {
        this.followPercent = followPercent;
    }

    public String getAvgHeadway() {
        return avgHeadway;
    }

    public void setAvgHeadway(String avgHeadway) {
        this.avgHeadway = avgHeadway;
    }

    public String getTimeShare() {
        return timeShare;
    }

    public void setTimeShare(String timeShare) {
        this.timeShare = timeShare;
    }

    public String getMediumSmallBusTraffic() {
        return mediumSmallBusTraffic;
    }

    public void setMediumSmallBusTraffic(String mediumSmallBusTraffic) {
        this.mediumSmallBusTraffic = mediumSmallBusTraffic;
    }

    public String getMediumSmallBusAvgSpeed() {
        return mediumSmallBusAvgSpeed;
    }

    public void setMediumSmallBusAvgSpeed(String mediumSmallBusAvgSpeed) {
        this.mediumSmallBusAvgSpeed = mediumSmallBusAvgSpeed;
    }

    public String getSmallTruckTraffic() {
        return smallTruckTraffic;
    }

    public void setSmallTruckTraffic(String smallTruckTraffic) {
        this.smallTruckTraffic = smallTruckTraffic;
    }

    public String getSmallTruckAvgSpeed() {
        return smallTruckAvgSpeed;
    }

    public void setSmallTruckAvgSpeed(String smallTruckAvgSpeed) {
        this.smallTruckAvgSpeed = smallTruckAvgSpeed;
    }

    public String getBigBusTraffic() {
        return bigBusTraffic;
    }

    public void setBigBusTraffic(String bigBusTraffic) {
        this.bigBusTraffic = bigBusTraffic;
    }

    public String getBigBusAvgSpeed() {
        return bigBusAvgSpeed;
    }

    public void setBigBusAvgSpeed(String bigBusAvgSpeed) {
        this.bigBusAvgSpeed = bigBusAvgSpeed;
    }

    public String getMediumTruckTraffic() {
        return mediumTruckTraffic;
    }

    public void setMediumTruckTraffic(String mediumTruckTraffic) {
        this.mediumTruckTraffic = mediumTruckTraffic;
    }

    public String getMediumTruckAvgSpeed() {
        return mediumTruckAvgSpeed;
    }

    public void setMediumTruckAvgSpeed(String mediumTruckAvgSpeed) {
        this.mediumTruckAvgSpeed = mediumTruckAvgSpeed;
    }

    public String getBigTruckTraffic() {
        return bigTruckTraffic;
    }

    public void setBigTruckTraffic(String bigTruckTraffic) {
        this.bigTruckTraffic = bigTruckTraffic;
    }

    public String getBigTruckAvgSpeed() {
        return bigTruckAvgSpeed;
    }

    public void setBigTruckAvgSpeed(String bigTruckAvgSpeed) {
        this.bigTruckAvgSpeed = bigTruckAvgSpeed;
    }

    public String getHugeTruckTraffic() {
        return hugeTruckTraffic;
    }

    public void setHugeTruckTraffic(String hugeTruckTraffic) {
        this.hugeTruckTraffic = hugeTruckTraffic;
    }

    public String getHugeTruckAvgSpeed() {
        return hugeTruckAvgSpeed;
    }

    public void setHugeTruckAvgSpeed(String hugeTruckAvgSpeed) {
        this.hugeTruckAvgSpeed = hugeTruckAvgSpeed;
    }

    public String getContainerTruckTraffic() {
        return containerTruckTraffic;
    }

    public void setContainerTruckTraffic(String containerTruckTraffic) {
        this.containerTruckTraffic = containerTruckTraffic;
    }

    public String getContainerTruckAvgSpeed() {
        return containerTruckAvgSpeed;
    }

    public void setContainerTruckAvgSpeed(String containerTruckAvgSpeed) {
        this.containerTruckAvgSpeed = containerTruckAvgSpeed;
    }

    public String getTractorTraffic() {
        return tractorTraffic;
    }

    public void setTractorTraffic(String tractorTraffic) {
        this.tractorTraffic = tractorTraffic;
    }

    public String getTractorAvgSpeed() {
        return tractorAvgSpeed;
    }

    public void setTractorAvgSpeed(String tractorAvgSpeed) {
        this.tractorAvgSpeed = tractorAvgSpeed;
    }

    public String getMotorTraffic() {
        return motorTraffic;
    }

    public void setMotorTraffic(String motorTraffic) {
        this.motorTraffic = motorTraffic;
    }

    public String getMotorSvgSpeed() {
        return motorSvgSpeed;
    }

    public void setMotorSvgSpeed(String motorSvgSpeed) {
        this.motorSvgSpeed = motorSvgSpeed;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "MicrowaveData{" +
                "devNo='" + devNo + '\'' +
                ", uniqueId='" + uniqueId + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", investigation='" + investigation + '\'' +
                ", cycle='" + cycle + '\'' +
                ", transTime='" + transTime + '\'' +
                ", laneNo='" + laneNo + '\'' +
                ", traffic='" + traffic + '\'' +
                ", speed='" + speed + '\'' +
                ", followPercent='" + followPercent + '\'' +
                ", avgHeadway='" + avgHeadway + '\'' +
                ", timeShare='" + timeShare + '\'' +
                ", mediumSmallBusTraffic='" + mediumSmallBusTraffic + '\'' +
                ", mediumSmallBusAvgSpeed='" + mediumSmallBusAvgSpeed + '\'' +
                ", smallTruckTraffic='" + smallTruckTraffic + '\'' +
                ", smallTruckAvgSpeed='" + smallTruckAvgSpeed + '\'' +
                ", bigBusTraffic='" + bigBusTraffic + '\'' +
                ", bigBusAvgSpeed='" + bigBusAvgSpeed + '\'' +
                ", mediumTruckTraffic='" + mediumTruckTraffic + '\'' +
                ", mediumTruckAvgSpeed='" + mediumTruckAvgSpeed + '\'' +
                ", bigTruckTraffic='" + bigTruckTraffic + '\'' +
                ", bigTruckAvgSpeed='" + bigTruckAvgSpeed + '\'' +
                ", hugeTruckTraffic='" + hugeTruckTraffic + '\'' +
                ", hugeTruckAvgSpeed='" + hugeTruckAvgSpeed + '\'' +
                ", containerTruckTraffic='" + containerTruckTraffic + '\'' +
                ", containerTruckAvgSpeed='" + containerTruckAvgSpeed + '\'' +
                ", tractorTraffic='" + tractorTraffic + '\'' +
                ", tractorAvgSpeed='" + tractorAvgSpeed + '\'' +
                ", motorTraffic='" + motorTraffic + '\'' +
                ", motorSvgSpeed='" + motorSvgSpeed + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
