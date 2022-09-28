package com.tunnel.business.domain.emeDrill;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 应急演练对象 sd_emergency_drill
 * 
 * @author ruoyi
 * @date 2021-11-22
 */
public class SdEmergencyDrill extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 演习名称 */
    @Excel(name = "演习名称")
    private String name;

    /** 演习描述 */
    @Excel(name = "演习描述")
    private String content;

    /** 演习类型 */
    @Excel(name = "演习类型")
    private String type;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;
    @Excel(name = "隧道名称")
    private String tunnelName;

    /** 负责人 */
    @Excel(name = "负责人")
    private String person;

    /** 联系方式 */
    @Excel(name = "联系方式")
    private String phone;

    /** 演习时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "演习时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date drillTime;

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setTunnelId(String tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId() 
    {
        return tunnelId;
    }
    public void setPerson(String person) 
    {
        this.person = person;
    }

    public String getPerson() 
    {
        return person;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setDrillTime(Date drillTime) 
    {
        this.drillTime = drillTime;
    }

    public Date getDrillTime() 
    {
        return drillTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("content", getContent())
            .append("type", getType())
            .append("tunnelId", getTunnelId())
                .append("tunnelName", getTunnelName())
            .append("person", getPerson())
            .append("phone", getPhone())
            .append("drillTime", getDrillTime())
            .toString();
    }
}
