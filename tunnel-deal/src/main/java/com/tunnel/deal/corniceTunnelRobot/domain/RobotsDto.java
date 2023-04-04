package com.tunnel.deal.corniceTunnelRobot.domain;

//机器人信息
public class RobotsDto {

    //机器人编号
    private String deviceId;
    //机器人Ip
    private String IP;
    //机器人名称
    private String name;
    //机器人编码 sn
    private String sn;
    //当前状态 1 为在线，0 为离线
    private String online;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }
}
