package com.tunnel.business.domain.vehicle;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 不降速通行对象 sd_no_slow_down_traffic
 * 
 * @author ruoyi
 * @date 2023-12-14
 */
public class SdNoSlowDownTraffic extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

    /** 洞内速度 */
    @Excel(name = "洞内速度")
    private Integer inAvgSpeed;

    /** 洞口速度 */
    @Excel(name = "洞口速度")
    private Integer outAvgSpeed;

    /** 方向 */
    @Excel(name = "方向")
    private String direction;

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
    public void setInAvgSpeed(Integer inAvgSpeed) 
    {
        this.inAvgSpeed = inAvgSpeed;
    }

    public Integer getInAvgSpeed() 
    {
        return inAvgSpeed;
    }
    public void setOutAvgSpeed(Integer outAvgSpeed) 
    {
        this.outAvgSpeed = outAvgSpeed;
    }

    public Integer getOutAvgSpeed() 
    {
        return outAvgSpeed;
    }
    public void setDirection(String direction) 
    {
        this.direction = direction;
    }

    public String getDirection() 
    {
        return direction;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tunnelId", getTunnelId())
            .append("inAvgSpeed", getInAvgSpeed())
            .append("outAvgSpeed", getOutAvgSpeed())
            .append("direction", getDirection())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
