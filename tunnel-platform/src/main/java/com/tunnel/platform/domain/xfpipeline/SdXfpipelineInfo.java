package com.tunnel.platform.domain.xfpipeline;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 消防管道监测对象 sd_xfpipeline_info
 * 
 * @author wangx
 * @date 2021-11-23
 */
public class SdXfpipelineInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 设备编码 */
    @Excel(name = "设备编码")
    private String equipmentId;

    /** 隧道编码 */
    @Excel(name = "隧道编码")
    private String tunnelId;

    /** 管道压力 */
    @Excel(name = "管道压力")
    private String pressure;

    /** 设备状态 */
    @Excel(name = "设备状态")
    private String state;

    /** 压力上限 */
    @Excel(name = "压力上限")
    private String pressureHeight;

    /** 压力下限 */
    @Excel(name = "压力下限")
    private String pressureLow;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setEquipmentId(String equipmentId) 
    {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentId() 
    {
        return equipmentId;
    }
    public void setTunnelId(String tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId() 
    {
        return tunnelId;
    }
    public void setPressure(String pressure) 
    {
        this.pressure = pressure;
    }

    public String getPressure() 
    {
        return pressure;
    }
    public void setState(String state) 
    {
        this.state = state;
    }

    public String getState() 
    {
        return state;
    }
    public void setPressureHeight(String pressureHeight) 
    {
        this.pressureHeight = pressureHeight;
    }

    public String getPressureHeight() 
    {
        return pressureHeight;
    }
    public void setPressureLow(String pressureLow) 
    {
        this.pressureLow = pressureLow;
    }

    public String getPressureLow() 
    {
        return pressureLow;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("equipmentId", getEquipmentId())
            .append("tunnelId", getTunnelId())
            .append("pressure", getPressure())
            .append("state", getState())
            .append("pressureHeight", getPressureHeight())
            .append("pressureLow", getPressureLow())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
