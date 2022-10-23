package com.tunnel.business.domain.digitalmodel;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author dzy
 * @date 2022/9/4 11:39
 */
public class WjParticipants {

    @ApiModelProperty("数据流水号")
    private String recordSerialNumber;

    @ApiModelProperty("参与者全域ID")
    private Integer  id;

    @ApiModelProperty("车辆类型")
    private Integer originalType;

    @ApiModelProperty("车辆颜色")
    private Integer originalColor;

    @ApiModelProperty("分辨率1e-7°，东经为正，西经为负")
    private Double	longitude;

    @ApiModelProperty("分辨率1e-7°，北纬为正，南纬为负")
    private Double latitude;

    @ApiModelProperty("速度，单位：Km/h")
    private Double speed;

    @ApiModelProperty("车道号,沿行车方向从左往右依次为1,2,3,4…")
    private Integer laneNum;

    @ApiModelProperty("航向角，单位：°，保留1位小数，车头与正北夹角")
    private Double courseAngle;

    @ApiModelProperty("车牌号")
    private String picLicense;

    @ApiModelProperty("车牌颜色")
    private Integer  vehicleColor;

    @ApiModelProperty("桩号")
    private String stakeNum;

    public String getRecordSerialNumber() {
        return recordSerialNumber;
    }

    public void setRecordSerialNumber(String recordSerialNumber) {
        this.recordSerialNumber = recordSerialNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOriginalType() {
        return originalType;
    }

    public void setOriginalType(Integer originalType) {
        this.originalType = originalType;
    }

    public Integer getOriginalColor() {
        return originalColor;
    }

    public void setOriginalColor(Integer originalColor) {
        this.originalColor = originalColor;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getLaneNum() {
        return laneNum;
    }

    public void setLaneNum(Integer laneNum) {
        this.laneNum = laneNum;
    }

    public Double getCourseAngle() {
        return courseAngle;
    }

    public void setCourseAngle(Double courseAngle) {
        this.courseAngle = courseAngle;
    }

    public String getPicLicense() {
        return picLicense;
    }

    public void setPicLicense(String picLicense) {
        this.picLicense = picLicense;
    }

    public Integer getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(Integer vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getStakeNum() {
        return stakeNum;
    }

    public void setStakeNum(String stakeNum) {
        this.stakeNum = stakeNum;
    }

    @Override
    public String toString() {
        return "WjParticipants{" +
                "recordSerialNumber='" + recordSerialNumber + '\'' +
                ", id=" + id +
                ", originalType=" + originalType +
                ", originalColor=" + originalColor +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", speed=" + speed +
                ", laneNum=" + laneNum +
                ", courseAngle=" + courseAngle +
                ", picLicense='" + picLicense + '\'' +
                ", vehicleColor=" + vehicleColor +
                ", stakeNum='" + stakeNum + '\'' +
                '}';
    }
}
