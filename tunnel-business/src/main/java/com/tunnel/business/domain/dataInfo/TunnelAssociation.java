package com.tunnel.business.domain.dataInfo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 隧道关联关系对象 tunnel_association
 */
public class TunnelAssociation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

    /** 隧道方向 */
    @Excel(name = "隧道方向")
    private String tunnelDirection;

    /** 外部系统ID */
    @Excel(name = "外部系统ID")
    private String externalSystemId;

    /** 外部系统隧道ID */
    @Excel(name = "外部系统隧道ID")
    private String externalSystemTunnelId;

    /** 外部系统隧道方向 */
    @Excel(name = "外部系统隧道方向")
    private String externalSystemTunnelDirection;

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
    public void setTunnelDirection(String tunnelDirection)
    {
        this.tunnelDirection = tunnelDirection;
    }

    public String getTunnelDirection()
    {
        return tunnelDirection;
    }
    public void setExternalSystemId(String externalSystemId)
    {
        this.externalSystemId = externalSystemId;
    }

    public String getExternalSystemId()
    {
        return externalSystemId;
    }
    public void setExternalSystemTunnelId(String externalSystemTunnelId)
    {
        this.externalSystemTunnelId = externalSystemTunnelId;
    }

    public String getExternalSystemTunnelId()
    {
        return externalSystemTunnelId;
    }
    public void setExternalSystemTunnelDirection(String externalSystemTunnelDirection)
    {
        this.externalSystemTunnelDirection = externalSystemTunnelDirection;
    }

    public String getExternalSystemTunnelDirection()
    {
        return externalSystemTunnelDirection;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tunnelId", getTunnelId())
            .append("tunnelDirection", getTunnelDirection())
            .append("externalSystemId", getExternalSystemId())
            .append("externalSystemTunnelId", getExternalSystemTunnelId())
            .append("externalSystemTunnelDirection", getExternalSystemTunnelDirection())
            .append("remark", getRemark())
            .toString();
    }
}
