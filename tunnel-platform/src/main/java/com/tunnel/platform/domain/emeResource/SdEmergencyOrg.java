package com.tunnel.platform.domain.emeResource;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 应急机构对象 sd_emergency_org
 * 
 * @author ruoyi
 * @date 2022-08-09
 */
public class SdEmergencyOrg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long orgId;

    /** 机构名称 */
    @Excel(name = "机构名称")
    private String orgName;

    /** 驻点名称 */
    @Excel(name = "驻点名称")
    private String stationName;

    /** 机构地址 */
    @Excel(name = "机构地址")
    private String orgAddress;

    /** 经度 */
    @Excel(name = "经度")
    private String longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    private String latitude;

    /** 救援外协单位 */
    @Excel(name = "救援外协单位")
    private String outsourceUnit;

    public void setOrgId(Long orgId) 
    {
        this.orgId = orgId;
    }

    public Long getOrgId() 
    {
        return orgId;
    }
    public void setOrgName(String orgName) 
    {
        this.orgName = orgName;
    }

    public String getOrgName() 
    {
        return orgName;
    }
    public void setStationName(String stationName) 
    {
        this.stationName = stationName;
    }

    public String getStationName() 
    {
        return stationName;
    }
    public void setOrgAddress(String orgAddress) 
    {
        this.orgAddress = orgAddress;
    }

    public String getOrgAddress() 
    {
        return orgAddress;
    }
    public void setLongitude(String longitude) 
    {
        this.longitude = longitude;
    }

    public String getLongitude() 
    {
        return longitude;
    }
    public void setLatitude(String latitude) 
    {
        this.latitude = latitude;
    }

    public String getLatitude() 
    {
        return latitude;
    }
    public void setOutsourceUnit(String outsourceUnit) 
    {
        this.outsourceUnit = outsourceUnit;
    }

    public String getOutsourceUnit() 
    {
        return outsourceUnit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orgId", getOrgId())
            .append("orgName", getOrgName())
            .append("stationName", getStationName())
            .append("orgAddress", getOrgAddress())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("outsourceUnit", getOutsourceUnit())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
