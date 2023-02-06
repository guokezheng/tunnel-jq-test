package com.tunnel.business.domain.event;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author dzy
 * @date 2022/9/4 14:32
 */
public class SdRadarDetectData {

    @ApiModelProperty("数据流水号")
    private  String recordSerialNumber;
    private  Long id;

    @ApiModelProperty("车辆id")
    private  String vehicleId;

    @ApiModelProperty("隧道id")
    private  String tunnelId;

    @ApiModelProperty("车辆类型")
    private  String vehicleType;

    @ApiModelProperty("车辆颜色")
    private  String vehicleColor;

    @ApiModelProperty("经度,分辨率1e-7°，东经为正，西经为负")
    private  String longitude;

    @ApiModelProperty("纬度,分辨率1e-7°，北纬为正，南纬为负")
    private  String latitude;

    @ApiModelProperty("速度，单位：Km/h")
    private  String speed;

    @ApiModelProperty("车道号,沿行车方向从左往右依次为1,2,3,4…")
    private  String laneNum;

    @ApiModelProperty("航向角，单位：°，保留1位小数，车头与正北夹角")
    private  String courseAngle;

    @ApiModelProperty("车牌号")
    private  String vehicleLicense;

    @ApiModelProperty("车牌颜色")
    private  String licenseColor;

    @ApiModelProperty("桩号")
    private  String stakeNum;

    @ApiModelProperty("监测时间（保留3位毫秒数）")
    private Date detectTime;

    private String endTime;
    private String startTime;

    private String deptId;

    private String distance;

    private String objectType;

    private String roadDir;

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getRoadDir() {
        return roadDir;
    }

    public void setRoadDir(String roadDir) {
        this.roadDir = roadDir;
    }
    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getRecordSerialNumber() {
        return recordSerialNumber;
    }

    public void setRecordSerialNumber(String recordSerialNumber) {
        this.recordSerialNumber = recordSerialNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(String tunnelId) {
        this.tunnelId = tunnelId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getLaneNum() {
        return laneNum;
    }

    public void setLaneNum(String laneNum) {
        this.laneNum = laneNum;
    }

    public String getCourseAngle() {
        return courseAngle;
    }

    public void setCourseAngle(String courseAngle) {
        this.courseAngle = courseAngle;
    }

    public String getVehicleLicense() {
        return vehicleLicense;
    }

    public void setVehicleLicense(String vehicleLicense) {
        this.vehicleLicense = vehicleLicense;
    }

    public String getLicenseColor() {
        return licenseColor;
    }

    public void setLicenseColor(String licenseColor) {
        this.licenseColor = licenseColor;
    }

    public String getStakeNum() {
        return stakeNum;
    }

    public void setStakeNum(String stakeNum) {
        this.stakeNum = stakeNum;
    }

    public Date getDetectTime() {
        return detectTime;
    }

    public void setDetectTime(Date detectTime) {
        this.detectTime = detectTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "SdRadarDetectData{" +
                "recordSerialNumber='" + recordSerialNumber + '\'' +
                ", id=" + id +
                ", vehicleId='" + vehicleId + '\'' +
                ", tunnelId='" + tunnelId + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", vehicleColor='" + vehicleColor + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", speed='" + speed + '\'' +
                ", laneNum='" + laneNum + '\'' +
                ", courseAngle='" + courseAngle + '\'' +
                ", vehicleLicense='" + vehicleLicense + '\'' +
                ", licenseColor='" + licenseColor + '\'' +
                ", stakeNum='" + stakeNum + '\'' +
                ", detectTime=" + detectTime +
                ", endTime='" + endTime + '\'' +
                ", startTime='" + startTime + '\'' +
                '}';
    }
}
