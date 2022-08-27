package com.tunnel.platform.domain;

import java.util.List;

public class EventEntity {
    //通道序号
    private String channelId ;
    //时间戳
    private Long TimeStamp;
    //目标信息
    private List<EventObjectsEntity> objects;
    //机动车属性
    private Object VehicleAttribute;
    /**交通事件
     */
    private List<EventTrafficIncidentEntity> TrafficIncident;
    /**
     * 交通参数
     */
    private List TrafficParameter;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }

    public List<EventObjectsEntity> getObjects() {
        return objects;
    }

    public void setObjects(List<EventObjectsEntity> objects) {
        this.objects = objects;
    }

    public Object getVehicleAttribute() {
        return VehicleAttribute;
    }

    public void setVehicleAttribute(Object vehicleAttribute) {
        VehicleAttribute = vehicleAttribute;
    }

    public List<EventTrafficIncidentEntity> getTrafficIncident() {
        return TrafficIncident;
    }

    public void setTrafficIncident(List<EventTrafficIncidentEntity> trafficIncident) {
        TrafficIncident = trafficIncident;
    }

    public List getTrafficParameter() {
        return TrafficParameter;
    }

    public void setTrafficParameter(List trafficParameter) {
        TrafficParameter = trafficParameter;
    }
}
