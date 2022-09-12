package com.tunnel.platform.domain.event;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 预案流程节点对象 sd_reserve_process
 * 
 * @author ruoyi
 * @date 2022-09-02
 */
public class SdReserveProcess extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 预案流程节点id */
    private Long id;

    /** 预案id */
    @Excel(name = "预案id")
    private Long reserveId;

    /** 设备类型id */
    @Excel(name = "设备类型id")
    private Long deviceTypeId;

    /** 策略id */
    @Excel(name = "策略id")
    private Long strategyId;

    /** 流程节点名称 */
    @Excel(name = "流程节点名称")
    private String processName;

    /** 流程节点顺序 */
    @Excel(name = "流程节点顺序")
    private Integer processSort;

    private String strategyIds;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setReserveId(Long reserveId) 
    {
        this.reserveId = reserveId;
    }

    public Long getReserveId() 
    {
        return reserveId;
    }
    public void setDeviceTypeId(Long deviceTypeId) 
    {
        this.deviceTypeId = deviceTypeId;
    }

    public Long getDeviceTypeId() 
    {
        return deviceTypeId;
    }
    public void setStrategyId(Long strategyId) 
    {
        this.strategyId = strategyId;
    }

    public Long getStrategyId() 
    {
        return strategyId;
    }
    public void setProcessName(String processName) 
    {
        this.processName = processName;
    }

    public String getProcessName() 
    {
        return processName;
    }
    public void setProcessSort(Integer processSort) 
    {
        this.processSort = processSort;
    }

    public Integer getProcessSort() 
    {
        return processSort;
    }

    public String getStrategyIds() {
        return strategyIds;
    }

    public void setStrategyIds(String strategyIds) {
        this.strategyIds = strategyIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("reserveId", getReserveId())
            .append("deviceTypeId", getDeviceTypeId())
            .append("strategyId", getStrategyId())
            .append("processName", getProcessName())
            .append("processSort", getProcessSort())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
