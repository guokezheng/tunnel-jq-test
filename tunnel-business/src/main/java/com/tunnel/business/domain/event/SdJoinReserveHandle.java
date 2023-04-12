package com.tunnel.business.domain.event;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 事件流程设备关联信息对象 sd_join_reserve_handle
 * 
 * @author ruoyi
 * @date 2023-04-08
 */
public class SdJoinReserveHandle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 事件id */
    @Excel(name = "事件id")
    private Long eventId;

    /** 流程关联信息id */
    @Excel(name = "流程关联信息id")
    private Long strategyRlId;

    /** 设备信息 */
    @Excel(name = "设备信息")
    private String equipments;

    /**
     * 设备类型id
     */
    private Long eqTypeId;

    /**
     * 设备执行项
     */
    private String state;

    /**
     * 预案流程名称
     */
    private String processName;

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Long getEqTypeId() {
        return eqTypeId;
    }

    public void setEqTypeId(Long eqTypeId) {
        this.eqTypeId = eqTypeId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setEventId(Long eventId) 
    {
        this.eventId = eventId;
    }

    public Long getEventId() 
    {
        return eventId;
    }
    public void setStrategyRlId(Long strategyRlId) 
    {
        this.strategyRlId = strategyRlId;
    }

    public Long getStrategyRlId() 
    {
        return strategyRlId;
    }
    public void setEquipments(String equipments) 
    {
        this.equipments = equipments;
    }

    public String getEquipments() 
    {
        return equipments;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("eventId", getEventId())
            .append("strategyRlId", getStrategyRlId())
            .append("equipments", getEquipments())
            .append("createTime", getCreateTime())
            .toString();
    }
}
