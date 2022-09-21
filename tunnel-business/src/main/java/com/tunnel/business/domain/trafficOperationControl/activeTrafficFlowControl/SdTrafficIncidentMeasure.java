package com.tunnel.business.domain.trafficOperationControl.activeTrafficFlowControl;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 交通事件推送措施对象 sd_traffic_incident_measure
 *
 * @author ruoyi
 * @date 2022-03-01
 */
public class SdTrafficIncidentMeasure extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 关联事件id */
    @Excel(name = "关联事件id")
    private Long incidentId;

    /**
     * 关联的管控等级配置id
     */
    private Long configLevelId;

//    /** 措施类型 */
//    @Excel(name = "措施类型")
//    private String measureType;
//
//    /** 距离事件点管控范围-最小值 */
//    @Excel(name = "距离事件点管控范围-最小值")
//    private String controlRangeMin;
//
//    /** 距离事件点管控范围-最大值 */
//    @Excel(name = "距离事件点管控范围-最大值")
//    private String controlRangeMax;
//
//    /** 具体管控措施 */
//    @Excel(name = "具体管控措施")
//    private String controlMeasure;
//
//    /** 限速值 */
//    @Excel(name = "限速值")
//    private String limitSpeed;
//
//    /** 限制类型 */
//    @Excel(name = "限制类型")
//    private String limitType;
//
//    /** 车辆类型 */
//    @Excel(name = "车辆类型")
//    private String carType;

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

    public Long getConfigLevelId() {
        return configLevelId;
    }

    public void setConfigLevelId(Long configLevelId) {
        this.configLevelId = configLevelId;
    }

//    public void setMeasureType(String measureType)
//    {
//        this.measureType = measureType;
//    }
//    public String getMeasureType()
//    {
//        return measureType;
//    }
//    public void setControlRangeMin(String controlRangeMin)
//    {
//        this.controlRangeMin = controlRangeMin;
//    }
//
//    public String getControlRangeMin()
//    {
//        return controlRangeMin;
//    }
//    public void setControlRangeMax(String controlRangeMax)
//    {
//        this.controlRangeMax = controlRangeMax;
//    }
//
//    public String getControlRangeMax()
//    {
//        return controlRangeMax;
//    }
//    public void setControlMeasure(String controlMeasure)
//    {
//        this.controlMeasure = controlMeasure;
//    }
//
//    public String getControlMeasure()
//    {
//        return controlMeasure;
//    }
//    public void setLimitSpeed(String limitSpeed)
//    {
//        this.limitSpeed = limitSpeed;
//    }
//
//    public String getLimitSpeed()
//    {
//        return limitSpeed;
//    }
//    public void setLimitType(String limitType)
//    {
//        this.limitType = limitType;
//    }
//
//    public String getLimitType()
//    {
//        return limitType;
//    }
//    public void setCarType(String carType)
//    {
//        this.carType = carType;
//    }
//
//    public String getCarType()
//    {
//        return carType;
//    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("incidentId", getIncidentId())
//            .append("measureType", getMeasureType())
//            .append("controlRangeMin", getControlRangeMin())
//            .append("controlRangeMax", getControlRangeMax())
//            .append("controlMeasure", getControlMeasure())
//            .append("limitSpeed", getLimitSpeed())
//            .append("limitType", getLimitType())
//            .append("carType", getCarType())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
