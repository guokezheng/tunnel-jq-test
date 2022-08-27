package com.tunnel.platform.domain.event;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 安全指数对象 sd_safety_index
 * 
 * @author ruoyi
 * @date 2021-12-27
 */
public class SdSafetyIndex extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;
    private String tunnelName;

    /** 安全指数标签 */
    @Excel(name = "安全指数标签")
    private String securityLabel;

    /** 安全指数名称 */
    @Excel(name = "安全指数名称")
    private String securityName;

    /** 安全指数百分比 */
    @Excel(name = "安全指数百分比")
    private String securityPercentage;

    /** 类型(0:加指标，1:扣指标) */
    @Excel(name = "类型")
    private String security;

    /** 描述(3:存在,加指标，6:存在,扣指标，9:存在,按照个数*指数百分比扣指标) */
    @Excel(name = "描述")
    private String securityDescribe;

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
    public void setTunnelId(String tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId() 
    {
        return tunnelId;
    }
    public void setSecurityLabel(String securityLabel) 
    {
        this.securityLabel = securityLabel;
    }

    public String getSecurityLabel() 
    {
        return securityLabel;
    }
    public void setSecurityName(String securityName) 
    {
        this.securityName = securityName;
    }

    public String getSecurityName() 
    {
        return securityName;
    }
    public void setSecurityPercentage(String securityPercentage) 
    {
        this.securityPercentage = securityPercentage;
    }

    public String getSecurityPercentage() 
    {
        return securityPercentage;
    }
    public void setSecurity(String security) 
    {
        this.security = security;
    }

    public String getSecurity() 
    {
        return security;
    }
    public void setSecurityDescribe(String securityDescribe) 
    {
        this.securityDescribe = securityDescribe;
    }

    public String getSecurityDescribe() 
    {
        return securityDescribe;
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

    public String getTunnelName() {
        return tunnelName;
    }
    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tunnelId", getTunnelId())
            .append("securityLabel", getSecurityLabel())
            .append("securityName", getSecurityName())
            .append("securityPercentage", getSecurityPercentage())
            .append("security", getSecurity())
            .append("securityDescribe", getSecurityDescribe())
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
