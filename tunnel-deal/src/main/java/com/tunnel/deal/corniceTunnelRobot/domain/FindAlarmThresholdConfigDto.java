package com.tunnel.deal.corniceTunnelRobot.domain;

// 电量阈值配置
public class FindAlarmThresholdConfigDto {

    //电池低电量
    private String powerLowerLimit;
    //电池高电量
    private String powerTopLimit;
    //主键 id
    private String id;
    //电量阈值配置名称
    private String groupName;
    //生效开始时间
    private String beginTime;
    //生效结束时间
    private String endTime;
    //机器人 id
    private String deviceId;
    //电池开启状态
    private String sendStatus;

    public String getPowerLowerLimit() {
        return powerLowerLimit;
    }

    public void setPowerLowerLimit(String powerLowerLimit) {
        this.powerLowerLimit = powerLowerLimit;
    }

    public String getPowerTopLimit() {
        return powerTopLimit;
    }

    public void setPowerTopLimit(String powerTopLimit) {
        this.powerTopLimit = powerTopLimit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }



    @Override
    public String toString() {
        return "FindAlarmThresholdConfigDto{" +
                "powerLowerLimit='" + powerLowerLimit + '\'' +
                ", powerTopLimit='" + powerTopLimit + '\'' +
                ", id='" + id + '\'' +
                ", groupName='" + groupName + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", sendStatus='" + sendStatus + '\'' +
                '}';
    }
}
