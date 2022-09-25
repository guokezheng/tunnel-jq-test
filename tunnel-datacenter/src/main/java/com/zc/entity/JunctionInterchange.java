package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 枢纽立交
 */
public class JunctionInterchange {

    @JsonProperty(value = "road_id")
    private String roadId;

    @JsonProperty(value = "stake_num")
    private String stakeNum;

    private String lat;

    private String lng;

    private String id;

    private String name;

    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public String getStakeNum() {
        return stakeNum;
    }

    public void setStakeNum(String stakeNum) {
        this.stakeNum = stakeNum;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "JunctionInterchange{" +
                "roadId='" + roadId + '\'' +
                ", stakeNum='" + stakeNum + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
