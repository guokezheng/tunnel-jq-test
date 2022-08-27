package com.tunnel.platform.datacenter.domain;


/**
 * @author yangqichao
 * @date 2019/9/17 13:36
 */
public class DeviceModel {

    private static final long serialVersionUID = 1L;

    private Integer id;                   // 主键

    private Long hostId;               // 所属主机

    private String  name;                 // 设备名称

    private String  stakeMark;            // 桩号

    private Integer type;                 // 设备类型

    private Integer lampType;                 // 照明类型

    private String  feedbackAddress1;     // 上位机状态反馈地址1

    private String  feedbackAddress2;     // 上位机状态反馈地址2

    private String  feedbackAddress3;     // 上位机状态反馈地址3

    private String  feedbackAddress4;     // 上位机状态反馈地址4

    private String  feedbackAddress5;     // 上位机状态反馈地址5

    private String  controlPointAddress;  // 上位机控制点位地址

    private String  remark;               // 备注

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStakeMark() {
        return stakeMark;
    }

    public void setStakeMark(String stakeMark) {
        this.stakeMark = stakeMark;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLampType() {
        return lampType;
    }

    public void setLampType(Integer lampType) {
        this.lampType = lampType;
    }

    public String getControlPointAddress() {
        return controlPointAddress;
    }

    public void setControlPointAddress(String controlPointAddress) {
        this.controlPointAddress = controlPointAddress;
    }

    public String getFeedbackAddress1() {
        return feedbackAddress1;
    }

    public void setFeedbackAddress1(String feedbackAddress1) {
        this.feedbackAddress1 = feedbackAddress1;
    }

    public String getFeedbackAddress2() {
        return feedbackAddress2;
    }

    public void setFeedbackAddress2(String feedbackAddress2) {
        this.feedbackAddress2 = feedbackAddress2;
    }

    public String getFeedbackAddress3() {
        return feedbackAddress3;
    }

    public void setFeedbackAddress3(String feedbackAddress3) {
        this.feedbackAddress3 = feedbackAddress3;
    }

    public String getFeedbackAddress4() {
        return feedbackAddress4;
    }

    public void setFeedbackAddress4(String feedbackAddress4) {
        this.feedbackAddress4 = feedbackAddress4;
    }

    public String getFeedbackAddress5() {
        return feedbackAddress5;
    }

    public void setFeedbackAddress5(String feedbackAddress5) {
        this.feedbackAddress5 = feedbackAddress5;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
