package com.tunnel.business.domain.dataInfo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 事件管控对象 sd_event_control
 * 
 * @author ruoyi
 * @date 2022-02-18
 */
public class SdEventControl extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

    /** 事件名称 */
    @Excel(name = "事件名称")
    private String eventName;

    /** 事件配置类型id */
    @Excel(name = "事件配置类型id")
    private String configurationId;

    /** 发生时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发生时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date timeOfOccurrence;

    /** 发生位置 */
    @Excel(name = "发生位置")
    private String locationOfOccurrence;

    /** 备注 */
    @Excel(name = "备注")
    private String remake;

    /** 备注1 */
    @Excel(name = "备注1")
    private String remake1;

    /** 备注2 */
    @Excel(name = "备注2")
    private String remake2;

    private SdEventControlConfiguration configuration;

    public SdEventControlConfiguration getConfiguration() {
        if (configuration == null) {
            configuration = new SdEventControlConfiguration();
        }
        return configuration;
    }

    public void setConfiguration(SdEventControlConfiguration configuration) {
        this.configuration = configuration;
    }

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
    public void setEventName(String eventName) 
    {
        this.eventName = eventName;
    }

    public String getEventName() 
    {
        return eventName;
    }
    public void setConfigurationId(String configurationId) 
    {
        this.configurationId = configurationId;
    }

    public String getConfigurationId() 
    {
        return configurationId;
    }
    public void setTimeOfOccurrence(Date timeOfOccurrence) 
    {
        this.timeOfOccurrence = timeOfOccurrence;
    }

    public Date getTimeOfOccurrence() 
    {
        return timeOfOccurrence;
    }
    public void setLocationOfOccurrence(String locationOfOccurrence) 
    {
        this.locationOfOccurrence = locationOfOccurrence;
    }

    public String getLocationOfOccurrence() 
    {
        return locationOfOccurrence;
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
            .append("tunnelId", getTunnelId())
            .append("eventName", getEventName())
            .append("configurationId", getConfigurationId())
            .append("timeOfOccurrence", getTimeOfOccurrence())
            .append("locationOfOccurrence", getLocationOfOccurrence())
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
