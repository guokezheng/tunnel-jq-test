package com.tunnel.platform.domain.trafficOperationControl.eventManage;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 交通管制信息对象 sd_traffic_control_info
 *
 * @author ruoyi
 * @date 2022-02-16
 */
public class SdTrafficControlInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 事件表sd_traffic_incident_info主键id */
    @Excel(name = "事件表sd_traffic_incident_info主键id")
    private Long incidentId;

    /** 原因分类 */
    @Excel(name = "原因分类")
    private String reasonClass;

    /** 原因类型 */
    @Excel(name = "原因类型")
    private String reasonType;

    /** 方向 */
    @Excel(name = "方向")
    private String direction;

    /** 桩号（KM） */
    @Excel(name = "桩号", readConverterExp = "K=M")
    private String stakeNumber;

    /** 桩号距离（+m） */
    @Excel(name = "桩号距离", readConverterExp = "+=m")
    private String stakeDistance;

    /** 特殊地点描述 */
    @Excel(name = "特殊地点描述")
    private String specialSiteDesc;

    /**
     * 交通状况
     */
    @Excel(name = "交通状况")
    private String trafficCondition;

    /** 是否结束 */
    @Excel(name = "是否结束")
    private String isFinish;

//    /**
//     * 关联的交通事件信息
//     */
//    private SdTrafficIncidentInfo incidentInfo;


    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setIncidentId(Long incidentId)
    {
        this.incidentId = incidentId;
    }

    public Long getIncidentId()
    {
        return incidentId;
    }
    public void setReasonClass(String reasonClass)
    {
        this.reasonClass = reasonClass;
    }

    public String getReasonClass()
    {
        return reasonClass;
    }
    public void setReasonType(String reasonType)
    {
        this.reasonType = reasonType;
    }

    public String getReasonType()
    {
        return reasonType;
    }
    public void setIsFinish(String isFinish)
    {
        this.isFinish = isFinish;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getStakeNumber() {
        return stakeNumber;
    }

    public void setStakeNumber(String stakeNumber) {
        this.stakeNumber = stakeNumber;
    }

    public String getStakeDistance() {
        return stakeDistance;
    }

    public void setStakeDistance(String stakeDistance) {
        this.stakeDistance = stakeDistance;
    }

    public String getSpecialSiteDesc() {
        return specialSiteDesc;
    }

    public void setSpecialSiteDesc(String specialSiteDesc) {
        this.specialSiteDesc = specialSiteDesc;
    }

    public String getTrafficCondition() {
        return trafficCondition;
    }

    public void setTrafficCondition(String trafficCondition) {
        this.trafficCondition = trafficCondition;
    }

    public String getIsFinish()
    {
        return isFinish;
    }

//    public SdTrafficIncidentInfo getIncidentInfo() {
//        return incidentInfo;
//    }
//
//    public void setIncidentInfo(SdTrafficIncidentInfo incidentInfo) {
//        this.incidentInfo = incidentInfo;
//    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("incidentId", getIncidentId())
            .append("reasonClass", getReasonClass())
            .append("reasonType", getReasonType())
                .append("direction", getDirection())
                .append("stakeNumber", getStakeNumber())
                .append("stakeDistance", getStakeDistance())
                .append("specialSiteDesc", getSpecialSiteDesc())
                .append("trafficCondition",getTrafficCondition())
            .append("isFinish", getIsFinish())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
