package com.tunnel.platform.domain.event;

import com.tunnel.platform.domain.dataInfo.SdTunnels;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 隧道分区对象 sd_tunnel_subarea
 *
 * @author ruoyi
 * @date 2022-08-25
 */
@Data
public class SdTunnelSubarea extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分区id */
    @ApiModelProperty("分区Id")
    private Long sId;

    /** 分区名称 */
    @Excel(name = "分区名称")
    @ApiModelProperty("分区名称")
    private String sName;

    @ApiModelProperty("隧道Id")
    private String tunnelId;

    @ApiModelProperty("桩号下限")
    private String pileMin;

    @ApiModelProperty("桩号上限")
    private String pileMax;

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

    public String getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(String tunnelId) {
        this.tunnelId = tunnelId;
    }

    public String getPileMin() {
        return pileMin;
    }

    public void setPileMin(String pileMin) {
        this.pileMin = pileMin;
    }

    public String getPileMax() {
        return pileMax;
    }

    public void setPileMax(String pileMax) {
        this.pileMax = pileMax;
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
