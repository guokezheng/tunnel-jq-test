package com.tunnel.business.domain.trafficBroadcasting;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 广播信息列对象 sd_broadcast_information_list
 * 
 * @author ruoyi
 * @date 2021-11-24
 */
public class SdBroadcastInformationList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 广播名称 */
    @Excel(name = "广播名称")
    private String broadcastName;

    /** 广播位置 */
    @Excel(name = "广播位置")
    private String broadcastLocation;

    /** 所属隧道id */
    @Excel(name = "所属隧道id")
    private String tunnelId;

    private String tunnelName;

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    /** 厂家 */
    @Excel(name = "厂家")
    private String manufactor;

    /** 最大音量 */
    @Excel(name = "最大音量")
    private String maximumVolume;

    /** 最大广播次数 */
    @Excel(name = "最大广播次数")
    private String maximumNumberOfBroadcasts;

    /** 备注 */
    @Excel(name = "备注")
    private String remake;

    /** 备注1 */
    @Excel(name = "备注1")
    private String remake1;

    /** 备注2 */
    @Excel(name = "备注2")
    private String remake2;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setBroadcastName(String broadcastName) 
    {
        this.broadcastName = broadcastName;
    }

    public String getBroadcastName() 
    {
        return broadcastName;
    }
    public void setBroadcastLocation(String broadcastLocation) 
    {
        this.broadcastLocation = broadcastLocation;
    }

    public String getBroadcastLocation() 
    {
        return broadcastLocation;
    }
    public void setTunnelId(String tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId() 
    {
        return tunnelId;
    }
    public void setManufactor(String manufactor) 
    {
        this.manufactor = manufactor;
    }

    public String getManufactor() 
    {
        return manufactor;
    }
    public void setMaximumVolume(String maximumVolume) 
    {
        this.maximumVolume = maximumVolume;
    }

    public String getMaximumVolume() 
    {
        return maximumVolume;
    }
    public void setMaximumNumberOfBroadcasts(String maximumNumberOfBroadcasts) 
    {
        this.maximumNumberOfBroadcasts = maximumNumberOfBroadcasts;
    }

    public String getMaximumNumberOfBroadcasts() 
    {
        return maximumNumberOfBroadcasts;
    }
    public void setRemake(String remake) 
    {
        this.remake = remake;
    }

    public String getRemake() 
    {
        return remake;
    }
    public void setRemake1(String remake1) 
    {
        this.remake1 = remake1;
    }

    public String getRemake1() 
    {
        return remake1;
    }
    public void setRemake2(String remake2) 
    {
        this.remake2 = remake2;
    }

    public String getRemake2() 
    {
        return remake2;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("broadcastName", getBroadcastName())
            .append("broadcastLocation", getBroadcastLocation())
            .append("tunnelId", getTunnelId())
            .append("tunnelName", getTunnelName())
            .append("manufactor", getManufactor())
            .append("maximumVolume", getMaximumVolume())
            .append("maximumNumberOfBroadcasts", getMaximumNumberOfBroadcasts())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("remake", getRemake())
            .append("remake1", getRemake1())
            .append("remake2", getRemake2())
            .toString();
    }
}
