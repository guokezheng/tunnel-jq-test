package com.tunnel.platform.domain.event;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 隧道分区对象 sd_tunnel_subarea
 *
 * @author ruoyi
 * @date 2022-08-25
 */
public class SdTunnelSubarea extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分区id */
    private Long sId;

    /** 分区名称 */
    @Excel(name = "分区名称")
    private String sName;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

    public void setsId(Long sId)
    {
        this.sId = sId;
    }

    public Long getsId()
    {
        return sId;
    }
    public void setsName(String sName)
    {
        this.sName = sName;
    }

    public String getsName()
    {
        return sName;
    }
    public void setTunnelId(String tunnelId)
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId()
    {
        return tunnelId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("sId", getsId())
            .append("sName", getsName())
            .append("tunnelId", getTunnelId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
