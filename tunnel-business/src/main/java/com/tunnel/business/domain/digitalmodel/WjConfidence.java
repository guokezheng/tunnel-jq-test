package com.tunnel.business.domain.digitalmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dzy
 * @date 2022/9/2 10:07
 */
public class WjConfidence {

    @ApiModelProperty("目标全域ID")
    private Integer targetId;

    @ApiModelProperty("事件置信度")
    private Integer eventConfidence;

    @ApiModelProperty("事件id")
    private Long eventIds;

    @ApiModelProperty("车速")
    private String speed;

    @ApiModelProperty("车牌号")
    private String plate;

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Integer getEventConfidence() {
        return eventConfidence;
    }

    public void setEventConfidence(Integer eventConfidence) {
        this.eventConfidence = eventConfidence;
    }

    public Long getEventIds() {
        return eventIds;
    }

    public void setEventIds(Long eventIds) {
        this.eventIds = eventIds;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    @Override
    public String toString() {
        return "WjConfidence{" +
                "targetId=" + targetId +
                ", eventConfidence=" + eventConfidence +
                ", eventIds=" + eventIds +
                ", speed='" + speed + '\'' +
                ", plate='" + plate + '\'' +
                '}';
    }
}
