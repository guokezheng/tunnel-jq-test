package com.tunnel.platform.domain.event;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

    @Override
    public String toString() {
        return "SdTunnelSubarea{" +
                "sId=" + sId +
                ", sName='" + sName + '\'' +
                ", tunnelId='" + tunnelId + '\'' +
                ", pileMin='" + pileMin + '\'' +
                ", pileMax='" + pileMax + '\'' +
                ", tunnelName='" + tunnelName + '\'' +
                '}';
    }

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    //sd_tunnels   tunnel_name
    private String tunnelName;


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

}
