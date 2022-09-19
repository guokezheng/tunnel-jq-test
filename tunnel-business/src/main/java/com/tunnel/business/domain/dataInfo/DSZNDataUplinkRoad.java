package com.tunnel.business.domain.dataInfo;

import java.util.Date;
import java.util.List;

/**
 * @author ：WYZ
 * @date ：Created in 2021/11/30 9:27
 * @description：戴升智能websocket推送数据结构体
 * @modified By：
 * @version: $
 */
public class DSZNDataUplinkRoad {

    //计算开始时间
    private Date createdAt;
    //计算结束时间
    private Date updatedAt;
    /**
     *dataUplinkRoad:realtime-值表示推送的实时数据;interval-定时算法；thirdplatform-情报板；喇叭事件推送;flight_light
     */
    private String dataUplinkRoad;
    //道路分段ID
    private Long roadSectionId;
    //是否主路
    private boolean mainRoad;
    //主路与匝道汇入，汇出关联关系
    private List<DSZNDeviceRelation> devRels;
    //超过deleteSeconds的时间未收到数据，则删除该车辆信息。默认20秒
    private Integer deleteSeconds;
    //信息最大编号，即信标总数量
    private Integer devSortMax;
    //信标距离
    private Integer devDistance;
    //高于 maxSpeed 则认为是超速
    private Double maxSpeed;
    //低于  minSpeed 则认为是缓行
    private Double minSpeed;
    //车辆数量
    private Double avgSpeed;
    //车速
    private Integer vehiclesCount;
    //车辆尾迹开发
    private Integer trackLightOn;
    //车辆尾迹颜色
    private String trackLightColor;
    //车辆尾迹闪灯时长，单位：second
    private Integer trackLightInterval;
    //车辆
    private List<DSZNVehicles> vehicles;
    //实时统计情况
    private DSZNMonitoringStatistics monitoringStatistics;
    //未分配的事件
    private List<DSZNEvent> unAllocateds;
    //畅通程度
    private List<DSZNRoadSmooth> smoothes;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDataUplinkRoad() {
        return dataUplinkRoad;
    }

    public void setDataUplinkRoad(String dataUplinkRoad) {
        this.dataUplinkRoad = dataUplinkRoad;
    }

    public Long getRoadSectionId() {
        return roadSectionId;
    }

    public void setRoadSectionId(Long roadSectionId) {
        this.roadSectionId = roadSectionId;
    }

    public boolean isMainRoad() {
        return mainRoad;
    }

    public void setMainRoad(boolean mainRoad) {
        this.mainRoad = mainRoad;
    }

    public Integer getDeleteSeconds() {
        return deleteSeconds;
    }

    public void setDeleteSeconds(Integer deleteSeconds) {
        this.deleteSeconds = deleteSeconds;
    }

    public Integer getDevSortMax() {
        return devSortMax;
    }

    public void setDevSortMax(Integer devSortMax) {
        this.devSortMax = devSortMax;
    }

    public Integer getDevDistance() {
        return devDistance;
    }

    public void setDevDistance(Integer devDistance) {
        this.devDistance = devDistance;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Double getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(Double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public Double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(Double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public Integer getVehiclesCount() {
        return vehiclesCount;
    }

    public void setVehiclesCount(Integer vehiclesCount) {
        this.vehiclesCount = vehiclesCount;
    }

    public Integer getTrackLightOn() {
        return trackLightOn;
    }

    public void setTrackLightOn(Integer trackLightOn) {
        this.trackLightOn = trackLightOn;
    }

    public String getTrackLightColor() {
        return trackLightColor;
    }

    public void setTrackLightColor(String trackLightColor) {
        this.trackLightColor = trackLightColor;
    }

    public Integer getTrackLightInterval() {
        return trackLightInterval;
    }

    public void setTrackLightInterval(Integer trackLightInterval) {
        this.trackLightInterval = trackLightInterval;
    }

    public List<DSZNDeviceRelation> getDevRels() {
        return devRels;
    }

    public void setDevRels(List<DSZNDeviceRelation> devRels) {
        this.devRels = devRels;
    }

    public List<DSZNVehicles> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<DSZNVehicles> vehicles) {
        this.vehicles = vehicles;
    }

    public List<DSZNEvent> getUnAllocateds() {
        return unAllocateds;
    }

    public void setUnAllocateds(List<DSZNEvent> unAllocateds) {
        this.unAllocateds = unAllocateds;
    }

    public List<DSZNRoadSmooth> getSmoothes() {
        return smoothes;
    }

    public void setSmoothes(List<DSZNRoadSmooth> smoothes) {
        this.smoothes = smoothes;
    }

    public DSZNMonitoringStatistics getMonitoringStatistics() {
        return monitoringStatistics;
    }

    public void setMonitoringStatistics(DSZNMonitoringStatistics monitoringStatistics) {
        this.monitoringStatistics = monitoringStatistics;
    }
}
