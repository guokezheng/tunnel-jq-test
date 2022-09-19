package com.tunnel.business.domain.emergencyRescue;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 应急救援对象 sd_emergency_rescue
 * 
 * @author ruoyi
 * @date 2021-11-23
 */
public class SdEmergencyRescue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 应急救援机构 */
    @Excel(name = "应急救援机构")
    private String rescuePortal;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

    private String tunnelName;

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    /** 位置信息 */
    @Excel(name = "位置信息")
    private String rescueLocation;

    /** 联系方式 */
    @Excel(name = "联系方式")
    private String phone;

    /** 救援服务信息 */
    @Excel(name = "救援服务信息")
    private String serviceInformation;

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
    public void setRescuePortal(String rescuePortal) 
    {
        this.rescuePortal = rescuePortal;
    }

    public String getRescuePortal() 
    {
        return rescuePortal;
    }
    public void setTunnelId(String tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId() 
    {
        return tunnelId;
    }
    public void setRescueLocation(String rescueLocation) 
    {
        this.rescueLocation = rescueLocation;
    }

    public String getRescueLocation() 
    {
        return rescueLocation;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setServiceInformation(String serviceInformation) 
    {
        this.serviceInformation = serviceInformation;
    }

    public String getServiceInformation() 
    {
        return serviceInformation;
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
            .append("rescuePortal", getRescuePortal())
            .append("tunnelId", getTunnelId())
            .append("tunnelName", getTunnelName())
            .append("rescueLocation", getRescueLocation())
            .append("phone", getPhone())
            .append("serviceInformation", getServiceInformation())
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
