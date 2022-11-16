package com.tunnel.business.domain.bigScreenApi;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author zhai
 * @date 2022/11/7
 */
public class SdEventWarning implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("事件类型")
    private String eventType;

    @ApiModelProperty("隧道名称")
    private String tunnelName;

    @ApiModelProperty("事件开始时间")
    private String startTime;

    @ApiModelProperty("事件描述")
    private String eventDescription;

    @ApiModelProperty("事件状态")
    private String eventState;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventState() {
        return eventState;
    }

    public void setEventState(String eventState) {
        this.eventState = eventState;
    }
}
