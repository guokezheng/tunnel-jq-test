package com.tunnel.business.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 安装场所对象 sd_place_location
 * 
 * @author yanghousheng
 * @date 2020-11-23
 */
public class SdPlaceLocation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 场所id */
    private Long placeId;

    /** 场所名称 */
    @Excel(name = "场所名称")
    private String placeName;

    /** 隧道id */
    @Excel(name = "隧道id")
    private Long tunnelId;

    public void setPlaceId(Long placeId) 
    {
        this.placeId = placeId;
    }

    public Long getPlaceId() 
    {
        return placeId;
    }
    public void setPlaceName(String placeName) 
    {
        this.placeName = placeName;
    }

    public String getPlaceName() 
    {
        return placeName;
    }
    public void setTunnelId(Long tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public Long getTunnelId() 
    {
        return tunnelId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("placeId", getPlaceId())
            .append("placeName", getPlaceName())
            .append("tunnelId", getTunnelId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
