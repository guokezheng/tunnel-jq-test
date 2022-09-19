package com.tunnel.business.domain.trafficOperationControl.controlConfig;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 管控等级配置措施-管控措施对象 sd_control_config_measure
 *
 * @author ruoyi
 * @date 2022-02-15
 */
public class SdControlConfigMeasure extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 管控等级配置id */
    @Excel(name = "管控等级配置id")
    private Long configLevelId;

    /** 措施类型 */
    @Excel(name = "措施类型")
    private String measureType;

    /** 距离事件点管控范围-最小值 */
    @Excel(name = "距离事件点管控范围-最小值")
    private String controlRangeMin;

    /** 距离事件点管控范围-最大值 */
    @Excel(name = "距离事件点管控范围-最大值")
    private String controlRangeMax;

    /** 具体管控措施 */
    @Excel(name = "具体管控措施")
    private String controlMeasure;

    /** 措施值 */
    @Excel(name = "措施值")
    private String measureValue;


    /**
     * 限制速度值
     */
    @Excel(name = "限制速度值")
    private String limitSpeed;


    /**
     * 限制类型
     */
    @Excel(name = "限制类型")
    private String limitType;

    /**
     * 车辆类型
     */
    @Excel(name = "车辆类型")
    private String carType;

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
    public void setMeasureType(String measureType)
    {
        this.measureType = measureType;
    }

    public String getMeasureType()
    {
        return measureType;
    }
    public void setControlRangeMin(String controlRangeMin)
    {
        this.controlRangeMin = controlRangeMin;
    }

    public String getControlRangeMin()
    {
        return controlRangeMin;
    }
    public void setControlRangeMax(String controlRangeMax)
    {
        this.controlRangeMax = controlRangeMax;
    }

    public String getControlRangeMax()
    {
        return controlRangeMax;
    }
    public void setControlMeasure(String controlMeasure)
    {
        this.controlMeasure = controlMeasure;
    }

    public String getControlMeasure()
    {
        return controlMeasure;
    }
    public void setMeasureValue(String measureValue)
    {
        this.measureValue = measureValue;
    }

    public String getMeasureValue()
    {
        return measureValue;
    }

    public String getLimitSpeed() {
        return limitSpeed;
    }

    public void setLimitSpeed(String limitSpeed) {
        this.limitSpeed = limitSpeed;
    }

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
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
            .append("measureType", getMeasureType())
            .append("controlRangeMin", getControlRangeMin())
            .append("controlRangeMax", getControlRangeMax())
            .append("controlMeasure", getControlMeasure())
            .append("measureValue", getMeasureValue())
                .append("limitSpeed",getLimitSpeed())
                .append("limitType",getLimitType())
                .append("carType",getCarType())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
