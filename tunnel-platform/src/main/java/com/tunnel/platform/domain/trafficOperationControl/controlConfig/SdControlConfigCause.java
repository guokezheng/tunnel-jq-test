package com.tunnel.platform.domain.trafficOperationControl.controlConfig;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 管控等级配置措施-管控原因对象 sd_control_config_cause
 *
 * @author ruoyi
 * @date 2022-02-15
 */
public class SdControlConfigCause extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 管控等级配置id */
    @Excel(name = "管控等级配置id")
    private Long configLevelId;

    /** 管控原因类型 */
    @Excel(name = "管控原因类型")
    private String causeType;

//    /** 原因阀值 */
//    @Excel(name = "原因阀值")
//    private String causeValue;

    /** 路面情况 */
    @Excel(name = "路面情况")
    private String roadCondition;

    /**
     * 能见度最小值
     */
    @Excel(name = "能见度最小值")
    private String visibilityMin;

    /**
     * 能见度最大值
     */
    @Excel(name = "能见度最大值")
    private String visibilityMax;

    /**
     * 拥挤度
     */
    @Excel(name = "拥挤度")
    private String congestionDegree;

    /**
     * 突发事件类型
     */
    @Excel(name = "事件类型")
    private String incidentType;

    /**
     * 突发事件级别
     */
    @Excel(name = "事件级别")
    private String incidentGrade;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setConfigLevelId(Long configLevelId)
    {
        this.configLevelId = configLevelId;
    }

    public Long getConfigLevelId()
    {
        return configLevelId;
    }
    public void setCauseType(String causeType)
    {
        this.causeType = causeType;
    }

    public String getCauseType()
    {
        return causeType;
    }
//    public void setCauseValue(String causeValue)
//    {
//        this.causeValue = causeValue;
//    }
//
//    public String getCauseValue()
//    {
//        return causeValue;
//    }


    public String getRoadCondition() {
        return roadCondition;
    }

    public void setRoadCondition(String roadCondition) {
        this.roadCondition = roadCondition;
    }

    public String getVisibilityMin() {
        return visibilityMin;
    }

    public void setVisibilityMin(String visibilityMin) {
        this.visibilityMin = visibilityMin;
    }

    public String getVisibilityMax() {
        return visibilityMax;
    }

    public void setVisibilityMax(String visibilityMax) {
        this.visibilityMax = visibilityMax;
    }

    public String getCongestionDegree() {
        return congestionDegree;
    }

    public void setCongestionDegree(String congestionDegree) {
        this.congestionDegree = congestionDegree;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public String getIncidentGrade() {
        return incidentGrade;
    }

    public void setIncidentGrade(String incidentGrade) {
        this.incidentGrade = incidentGrade;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("configLevelId", getConfigLevelId())
            .append("causeType", getCauseType())
//            .append("causeValue", getCauseValue())
                .append("roadCondition",roadCondition)
                .append("visibilityMin",visibilityMin)
                .append("visibilityMax",visibilityMax)
                .append("congestionDegree",congestionDegree)
                .append("incidentType",getIncidentType())
                .append("incidentGrade",getIncidentGrade())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
