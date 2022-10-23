package com.tunnel.business.domain.digitalmodel;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author dzy
 * @date 2022/9/2 9:51
 */
public class WjEvent extends BaseEntity {

    @ApiModelProperty("事件编码")
    private Long eventId;

    @ApiModelProperty("事件类型")
    private Byte eventType;

    @ApiModelProperty("基站ID")
    private Byte stationId;

    @ApiModelProperty("车道号")
    private Byte laneNo;

    @ApiModelProperty("事件目标数量")
    private Byte targetNum;

    @ApiModelProperty("事件桩号")
    private String stakeNum;

    @ApiModelProperty("目标集合")
    private List<WjConfidence> targetList;

    @ApiModelProperty("事件位置经度")
    private Double eventLongitude;

    @ApiModelProperty("事件位置纬度")
    private Double eventLatitude;

    @ApiModelProperty("事件开始时间戳")
    private String eventTimeStampStart;

    @ApiModelProperty("事件结束时间戳")
    private String eventTimeStampEnd;

    @ApiModelProperty("隧道方向 1北向--上行 2南向--下行")
    private String direction;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Byte getEventType() {
        return eventType;
    }

    public void setEventType(Byte eventType) {
        this.eventType = eventType;
    }

    public Byte getStationId() {
        return stationId;
    }

    public void setStationId(Byte stationId) {
        this.stationId = stationId;
    }

    public Byte getLaneNo() {
        return laneNo;
    }

    public void setLaneNo(Byte laneNo) {
        this.laneNo = laneNo;
    }

    public Byte getTargetNum() {
        return targetNum;
    }

    public void setTargetNum(Byte targetNum) {
        this.targetNum = targetNum;
    }

    public String getStakeNum() {
        return stakeNum;
    }

    public void setStakeNum(String stakeNum) {
        this.stakeNum = stakeNum;
    }

    public List<WjConfidence> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<WjConfidence> targetList) {
        this.targetList = targetList;
    }

    public Double getEventLongitude() {
        return eventLongitude;
    }

    public void setEventLongitude(Double eventLongitude) {
        this.eventLongitude = eventLongitude;
    }

    public Double getEventLatitude() {
        return eventLatitude;
    }

    public void setEventLatitude(Double eventLatitude) {
        this.eventLatitude = eventLatitude;
    }

    public String getEventTimeStampStart() {
        return eventTimeStampStart;
    }

    public void setEventTimeStampStart(String eventTimeStampStart) {
        this.eventTimeStampStart = eventTimeStampStart;
    }

    public String getEventTimeStampEnd() {
        return eventTimeStampEnd;
    }

    public void setEventTimeStampEnd(String eventTimeStampEnd) {
        this.eventTimeStampEnd = eventTimeStampEnd;
    }

    @Override
    public String toString() {
        return "WjEvent{" +
                "eventId=" + eventId +
                ", eventType=" + eventType +
                ", stationId=" + stationId +
                ", laneNo=" + laneNo +
                ", targetNum=" + targetNum +
                ", stakeNum='" + stakeNum + '\'' +
                ", targetList=" + targetList +
                ", eventLongitude=" + eventLongitude +
                ", eventLatitude=" + eventLatitude +
                ", eventTimeStampStart='" + eventTimeStampStart + '\'' +
                ", eventTimeStampEnd='" + eventTimeStampEnd + '\'' +
                '}';
    }
}
