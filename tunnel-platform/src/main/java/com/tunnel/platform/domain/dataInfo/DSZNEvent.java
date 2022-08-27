package com.tunnel.platform.domain.dataInfo;

import java.sql.Date;

/**
 * @author ：WYZ
 * @date ：Created in 2021/11/30 10:20
 * @description：未分配事件
 * @modified By：
 * @version: $
 */
public class DSZNEvent {
    //事件类型（0-未定义；1-事故；2-逆行或倒车；3-超速；4-拥堵；5-缓行；6-停车；7-变道；8-跟车太近）
    private EventTypeEnum type;
    //道路分段ID
    private  String roadSectionId;
    //设备编号
    //送的实时数据;interval-定时算法；thirdplatform-情报板；喇叭事件推送;flight_light
    private String devEUI;
    //设备顺序编号
    private int devSort;
    //发生的位置范围
    private int devSortBegin;
    //发生的位置范围
    private int devSortEnd;
    //车道
    private int lane;
    //发生的时间
    private Date  createdAt;
    //发生的时间范围
    private Date createBeginTime;
    //发生的时间范围
    private  Date createEndTime;

    public EventTypeEnum getType() {
        return type;
    }

    public void setType(EventTypeEnum type) {
        this.type = type;
    }

    public String getRoadSectionId() {
        return roadSectionId;
    }

    public void setRoadSectionId(String roadSectionId) {
        this.roadSectionId = roadSectionId;
    }

    public String getDevEUI() {
        return devEUI;
    }

    public void setDevEUI(String devEUI) {
        this.devEUI = devEUI;
    }

    public int getDevSort() {
        return devSort;
    }

    public void setDevSort(int devSort) {
        this.devSort = devSort;
    }

    public int getDevSortBegin() {
        return devSortBegin;
    }

    public void setDevSortBegin(int devSortBegin) {
        this.devSortBegin = devSortBegin;
    }

    public int getDevSortEnd() {
        return devSortEnd;
    }

    public void setDevSortEnd(int devSortEnd) {
        this.devSortEnd = devSortEnd;
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreateBeginTime() {
        return createBeginTime;
    }

    public void setCreateBeginTime(Date createBeginTime) {
        this.createBeginTime = createBeginTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }
}
