package com.tunnel.business.domain.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 事件处理流程对象 sd_event_flow
 * 
 * @author gongfanfei
 * @date 2020-09-17
 */
public class SdEventFlow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 处理流程ID */
    private Long id;

    /** 事件关联ID */
    @Excel(name = "事件关联ID")
    private String eventId;

    /** 处理记录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss")
    @Excel(name = "处理记录时间", width = 30, dateFormat = "yyyy-MM-dd  HH:mm:ss")
    private Date flowTime;

    /** 处理记录描述 */
    @Excel(name = "处理记录描述")
    private String flowDescription;

    /** 处理人 */
    @Excel(name = "处理人")
    private String flowHandler;
    
    private String nickName;

    /**
     * 时间
     */
    private String flowDate;

    public String getFlowDate() {
        return flowDate;
    }

    public void setFlowDate(String flowDate) {
        this.flowDate = flowDate;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setEventId(String eventId) 
    {
        this.eventId = eventId;
    }

    public String getEventId() 
    {
        return eventId;
    }
    public void setFlowTime(Date flowTime) 
    {
        this.flowTime = flowTime;
    }

    public Date getFlowTime() 
    {
        return flowTime;
    }
    public void setFlowDescription(String flowDescription) 
    {
        this.flowDescription = flowDescription;
    }

    public String getFlowDescription() 
    {
        return flowDescription;
    }
    public void setFlowHandler(String flowHandler) 
    {
        this.flowHandler = flowHandler;
    }

    public String getFlowHandler() 
    {
        return flowHandler;
    }

	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("eventId", getEventId())
            .append("flowTime", getFlowTime())
            .append("flowDescription", getFlowDescription())
            .append("flowHandler", getFlowHandler())
            .append("nickName", getNickName())
            .toString();
    }
}