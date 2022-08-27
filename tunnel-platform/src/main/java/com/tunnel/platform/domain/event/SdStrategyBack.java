package com.tunnel.platform.domain.event;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 策略还原对象 sd_strategy_back
 * 
 * @author ruoyi
 * @date 2021-06-08
 */
public class SdStrategyBack extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 策略ID */
    private Long id;

    /** 所属隧道 id */
    @Excel(name = "所属隧道 id")
    private String tunnelId;

    /** 事件ID */
    @Excel(name = "事件ID")
    private Long warningId;

    /** 设备ID */
    @Excel(name = "设备ID")
    private String eqId;

    /** 设备类型 */
    @Excel(name = "设备类型")
    private Integer eqType;

    /** 策略类型 0：手动执行 1：定时控制 2：智能控制 */
    @Excel(name = "策略类型 0：手动执行 1：定时控制 2：智能控制")
    private String strategyType;

    /** 状态名称 */
    @Excel(name = "状态名称")
    private String stateName;

    /** 设备报文状态 */
    private String deviceState;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTunnelId(String tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId() 
    {
        return tunnelId;
    }
    public void setWarningId(Long warningId)
    {
        this.warningId = warningId;
    }

    public Long getWarningId()
    {
        return warningId;
    }
    public void setEqId(String eqId) 
    {
        this.eqId = eqId;
    }

    public String getEqId() 
    {
        return eqId;
    }
    public void setEqType(Integer eqType) 
    {
        this.eqType = eqType;
    }

    public Integer getEqType() 
    {
        return eqType;
    }
    public void setStrategyType(String strategyType) 
    {
        this.strategyType = strategyType;
    }

    public String getStrategyType() 
    {
        return strategyType;
    }
    public void setStateName(String stateName) 
    {
        this.stateName = stateName;
    }

    public String getStateName() 
    {
        return stateName;
    }
    public void setDeviceState(String deviceState)
    {
        this.deviceState = deviceState;
    }

    public String getDeviceState()
    {
        return deviceState;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tunnelId", getTunnelId())
            .append("warningId", getWarningId())
            .append("eqId", getEqId())
            .append("eqType", getEqType())
            .append("strategyType", getStrategyType())
            .append("stateName", getStateName())
            .append("deviceState", getDeviceState())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
