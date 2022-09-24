package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 微波车检器周期过车数据查询
 */
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


    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public String getVolCnt() {
        return volCnt;
    }

    public void setVolCnt(String volCnt) {
        this.volCnt = volCnt;
    }

    public String getVolSamllCnt() {
        return volSamllCnt;
    }

    public void setVolSamllCnt(String volSamllCnt) {
        this.volSamllCnt = volSamllCnt;
    }

    public String getVolMediumCnt() {
        return volMediumCnt;
    }

    public void setVolMediumCnt(String volMediumCnt) {
        this.volMediumCnt = volMediumCnt;
    }

    public String getVolLarge1Cnt() {
        return volLarge1Cnt;
    }

    public void setVolLarge1Cnt(String volLarge1Cnt) {
        this.volLarge1Cnt = volLarge1Cnt;
    }

    public String getVolLarge2Cnt() {
        return volLarge2Cnt;
    }

    public void setVolLarge2Cnt(String volLarge2Cnt) {
        this.volLarge2Cnt = volLarge2Cnt;
    }

    public String getVolLarge3Cnt() {
        return volLarge3Cnt;
    }

    public void setVolLarge3Cnt(String volLarge3Cnt) {
        this.volLarge3Cnt = volLarge3Cnt;
    }

    public String getAveSpeed() {
        return aveSpeed;
    }

    public void setAveSpeed(String aveSpeed) {
        this.aveSpeed = aveSpeed;
    }

    public String getSmallSpeed() {
        return smallSpeed;
    }

    public void setSmallSpeed(String smallSpeed) {
        this.smallSpeed = smallSpeed;
    }

    public String getMediumSpeed() {
        return mediumSpeed;
    }

    public void setMediumSpeed(String mediumSpeed) {
        this.mediumSpeed = mediumSpeed;
    }

    public String getLarge1Speed() {
        return large1Speed;
    }

    public void setLarge1Speed(String large1Speed) {
        this.large1Speed = large1Speed;
    }

    public String getLarge2Speed() {
        return large2Speed;
    }

    public void setLarge2Speed(String large2Speed) {
        this.large2Speed = large2Speed;
    }

    public String getLarge3Speed() {
        return large3Speed;
    }

    public void setLarge3Speed(String large3Speed) {
        this.large3Speed = large3Speed;
    }

    public String getAveHeadWay() {
        return aveHeadWay;
    }

    public void setAveHeadWay(String aveHeadWay) {
        this.aveHeadWay = aveHeadWay;
    }

    public String getAveLength() {
        return aveLength;
    }

    public void setAveLength(String aveLength) {
        this.aveLength = aveLength;
    }

    public String getAveOccupancy() {
        return aveOccupancy;
    }

    public void setAveOccupancy(String aveOccupancy) {
        this.aveOccupancy = aveOccupancy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "MicrowaveCarDetector{" +
                "devNo='" + devNo + '\'' +
                ", transTime='" + transTime + '\'' +
                ", lane='" + lane + '\'' +
                ", volCnt='" + volCnt + '\'' +
                ", volSamllCnt='" + volSamllCnt + '\'' +
                ", volMediumCnt='" + volMediumCnt + '\'' +
                ", volLarge1Cnt='" + volLarge1Cnt + '\'' +
                ", volLarge2Cnt='" + volLarge2Cnt + '\'' +
                ", volLarge3Cnt='" + volLarge3Cnt + '\'' +
                ", aveSpeed='" + aveSpeed + '\'' +
                ", smallSpeed='" + smallSpeed + '\'' +
                ", mediumSpeed='" + mediumSpeed + '\'' +
                ", large1Speed='" + large1Speed + '\'' +
                ", large2Speed='" + large2Speed + '\'' +
                ", large3Speed='" + large3Speed + '\'' +
                ", aveHeadWay='" + aveHeadWay + '\'' +
                ", aveLength='" + aveLength + '\'' +
                ", aveOccupancy='" + aveOccupancy + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
