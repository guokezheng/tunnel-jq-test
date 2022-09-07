package com.tunnel.platform.domain.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import com.tunnel.platform.domain.dataInfo.SdTunnels;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 事件管理对象 sd_event
 *
 * @author gongfanfei
 * @date 2020-08-24
 */
@ApiModel("事件管理实体")
@Data
public class SdEvent extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 事件ID
     */
    @ApiModelProperty("事件id")
    private Long id;

    /**
     * 隧道ID
     */
    @Excel(name = "隧道ID")
    @ApiModelProperty("隧道id")
    private String tunnelId;

    /**
     * 隧道对象
     */
    @Excels({
            @Excel(name = "隧道", targetAttr = "tunnels"),
    })
    @ApiModelProperty("隧道对象")
    private SdTunnels tunnels;

    /**
     * 事件类型
     */
    @Excel(name = "事件类型ID")
    @ApiModelProperty("事件类型id")
    private Long eventTypeId;

    /**
     * 事件类型对象
     */
    @Excels({
            @Excel(name = "事件类型", targetAttr = "eventType"),
    })
    @ApiModelProperty("事件类型对象")
    private SdEventType eventType;

    /**
     * 事件标题
     */
    @Excel(name = "事件标题")
    @ApiModelProperty("事件标题")
    private String eventTitle;

    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("时间")
    private Date eventTime;

    /**
     * 状态
     */
    @Excel(name = "状态")
    @ApiModelProperty("状态")
    private String eventState;

    /**
     * 级别
     */
    @Excel(name = "级别 ")
    @ApiModelProperty("级别")
    private String eventGrade;


    /**
     * 位置
     */
    @Excel(name = "位置")
    @ApiModelProperty("位置")
    private String eventLocation;

    /**
     * 死亡人数
     */
    @Excel(name = "死亡人数")
    @ApiModelProperty("死亡人数")
    private Integer eventDeath;

    /**
     * 受伤人数
     */
    @Excel(name = "受伤人数")
    @ApiModelProperty("受伤人数")
    private Integer eventInjured;

    /**
     * 事件描述
     */
    @Excel(name = "事件描述")
    @ApiModelProperty("事件描述")
    private String eventDescription;

    /**
     * 预案ID
     */
    @ApiModelProperty("预案ID")
    private String reservePlanId;

    /**
     * 处理记录ID
     */
    @ApiModelProperty("处理记录ID")
    private String flowId;

    /**
     * 预警ID
     */
    @ApiModelProperty("预警ID")
    private Long warningId;

    @ApiModelProperty("开始时间")
    private String beginTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("车道号")
    private String laneNo;

    @ApiModelProperty("事件位置经度")
    private String eventLongitude;

    @ApiModelProperty("事件位置纬度")
    private String eventLatitude;

    @ApiModelProperty("事件开始时间")
    private String startTime;

    @ApiModelProperty("事件短视频URL地址")
    private String videoUrl;

    @ApiModelProperty("基站id")
    private String stationId;

    @ApiModelProperty("事件桩号")
    private String stakeNum;

//    @ApiModelProperty("事件结束时间")
//    private Date endTime;

    @ApiModelProperty("备注")
    public String remark;
    @ApiModelProperty("清障电话")
    public String wreckerPhone;
    @ApiModelProperty("车主电话")
    public String carOwnerPhone;
    @ApiModelProperty("罐车数量")
    public int tankerNum;
    @ApiModelProperty("客车数量")
    public int passengerCarNum;
    @ApiModelProperty("货车数量")
    public int truckNum;

    @ApiModelProperty("方向'")
    public String direction;
    @ApiModelProperty("轻伤人数")
    public int slightInjured;
    @ApiModelProperty("交警电话")
    public String policePhone;
    @ApiModelProperty("小型车数量")
    public int smallCarNum;


    private Long deptId;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setSlightInjured(int slightInjured) {
        this.slightInjured = slightInjured;
    }
    public String getLaneNo() {
        return laneNo;
    }

    public void setLaneNo(String laneNo) {
        this.laneNo = laneNo;
    }

    public String getEventLongitude() {
        return eventLongitude;
    }

    public void setEventLongitude(String eventLongitude) {
        this.eventLongitude = eventLongitude;
    }

    public String getEventLatitude() {
        return eventLatitude;
    }

    public void setEventLatitude(String eventLatitude) {
        this.eventLatitude = eventLatitude;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStakeNum() {
        return stakeNum;
    }

    public void setStakeNum(String stakeNum) {
        this.stakeNum = stakeNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWreckerPhone() {
        return wreckerPhone;
    }

    public void setWreckerPhone(String wreckerPhone) {
        this.wreckerPhone = wreckerPhone;
    }

    public String getCarOwnerPhone() {
        return carOwnerPhone;
    }

    public void setCarOwnerPhone(String carOwnerPhone) {
        this.carOwnerPhone = carOwnerPhone;
    }

    public int getTankerNum() {
        return tankerNum;
    }

    public void setTankerNum(int tankerNum) {
        this.tankerNum = tankerNum;
    }

    public int getPassengerCarNum() {
        return passengerCarNum;
    }

    public void setPassengerCarNum(int passengerCarNum) {
        this.passengerCarNum = passengerCarNum;
    }

    public int getTruckNum() {
        return truckNum;
    }

    public void setTruckNum(int truckNum) {
        this.truckNum = truckNum;
    }

    public int getSmallCarNum() {
        return smallCarNum;
    }

    public void setSmallCarNum(int smallCarNum) {
        this.smallCarNum = smallCarNum;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }


    public String getPolicePhone() {
        return policePhone;
    }

    public void setPolicePhone(String policePhone) {
        this.policePhone = policePhone;
    }

    public int getSlightInjured() {
        return slightInjured;
    }

    @Override
    public String toString() {
        return "SdEvent{" +
                "id=" + id +
                ", tunnelId='" + tunnelId + '\'' +
                ", tunnels=" + tunnels +
                ", eventTypeId=" + eventTypeId +
                ", eventType=" + eventType +
                ", eventTitle='" + eventTitle + '\'' +
                ", eventTime=" + eventTime +
                ", eventState='" + eventState + '\'' +
                ", eventGrade='" + eventGrade + '\'' +
                ", eventLocation='" + eventLocation + '\'' +
                ", eventDeath=" + eventDeath +
                ", eventInjured=" + eventInjured +
                ", eventDescription='" + eventDescription + '\'' +
                ", reservePlanId='" + reservePlanId + '\'' +
                ", flowId='" + flowId + '\'' +
                ", warningId=" + warningId +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", laneNo='" + laneNo + '\'' +
                ", eventLongitude='" + eventLongitude + '\'' +
                ", eventLatitude='" + eventLatitude + '\'' +
                ", startTime='" + startTime + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", stationId='" + stationId + '\'' +
                ", stakeNum='" + stakeNum + '\'' +
                ", remark='" + remark + '\'' +
                ", wreckerPhone='" + wreckerPhone + '\'' +
                ", carOwnerPhone='" + carOwnerPhone + '\'' +
                ", tankerNum=" + tankerNum +
                ", passengerCarNum=" + passengerCarNum +
                ", truckNum=" + truckNum +
                ", direction='" + direction + '\'' +
                ", slightInjured=" + slightInjured +
                ", policePhone='" + policePhone + '\'' +
                ", smallCarNum=" + smallCarNum +
                '}';
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(String tunnelId) {
        this.tunnelId = tunnelId;
    }

    public SdTunnels getTunnels() {
        if (tunnels == null) {
            tunnels = new SdTunnels();
        }
        return tunnels;
    }

    public void setTunnels(SdTunnels tunnels) {
        this.tunnels = tunnels;
    }

    public void setEventTypeId(Long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public Long getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventState(String eventState) {
        this.eventState = eventState;
    }

    public String getEventState() {
        return eventState;
    }

    public void setEventGrade(String eventGrade) {
        this.eventGrade = eventGrade;
    }

    public String getEventGrade() {
        return eventGrade;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventDeath(Integer eventDeath) {
        this.eventDeath = eventDeath;
    }

    public Integer getEventDeath() {
        return eventDeath;
    }

    public void setEventInjured(Integer eventInjured) {
        this.eventInjured = eventInjured;
    }

    public Integer getEventInjured() {
        return eventInjured;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setReservePlanId(String reservePlanId) {
        this.reservePlanId = reservePlanId;
    }

    public String getReservePlanId() {
        return reservePlanId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setWarningId(Long warningId) {
        this.warningId = warningId;
    }

    public Long getWarningId() {
        return warningId;
    }

    public SdEventType getEventType() {
        if (eventType == null) {
            eventType = new SdEventType();
        }
        return eventType;
    }

    public void setEventType(SdEventType eventType) {
        this.eventType = eventType;
    }

}
