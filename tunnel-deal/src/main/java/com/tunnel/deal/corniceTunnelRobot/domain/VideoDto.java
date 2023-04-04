package com.tunnel.deal.corniceTunnelRobot.domain;

//机器人视频流地址
public class VideoDto {
    //高清
    private String hd;
    //红外
    private String infrared;

    public String getHd() {
        return hd;
    }

    public void setHd(String hd) {
        this.hd = hd;
    }

    public String getInfrared() {
        return infrared;
    }

    public void setInfrared(String infrared) {
        this.infrared = infrared;
    }
}
