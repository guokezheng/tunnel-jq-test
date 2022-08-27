package com.tunnel.platform.domain;

//交通事件
public class EventTrafficIncidentEntity {
   /* *//**
     * 事件类型
     * 祥见附录8.1交通事件
     *//*
    private String Type;
    //类型 ID
    private String TypeID;
    //事件状态
    private String TypeStatus;
    //相关事件 ID
    private String RelatedTypeID;
    //事件描述
    private String TypeDescribe;
    *//**
     * 区域ID
     *//*
    private Integer RegionId;
    *//**
     * 目标ID
     *//*
    private List ObjectsID;
    *//**
     * 车道ID列表
     *//*
    private List LaneIdList;
    *//**
     * 目标框坐标（左上角和右下角，实际像素坐标）
     *//*
    private List<List> ObjectCoord;*/
    /**
     * 告警图片，base64编码
     */
    private String picBase64;
/*    private String recordID;
    //是否有告警视频
    private boolean HasAlarmVideo;*/

    public String getPicBase64() {
        return picBase64;
    }

    public void setPicBase64(String picBase64) {
        this.picBase64 = picBase64;
    }
}
