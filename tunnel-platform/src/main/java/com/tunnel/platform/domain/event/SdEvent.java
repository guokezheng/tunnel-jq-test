package com.tunnel.platform.domain.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tunnel.platform.domain.dataInfo.SdTunnels;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 事件管理对象 sd_event
 *
 * @author gongfanfei
 * @date 2020-08-24
 */
@ApiModel("事件管理实体")
public class SdEvent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 事件ID */
    @ApiModelProperty("事件id")
    private Long id;

    /** 隧道ID */
    @Excel(name = "隧道ID")
    @ApiModelProperty("隧道id")
    private String tunnelId;

    /** 隧道对象 */
    @Excels({
        @Excel(name = "隧道", targetAttr = "tunnels"),
    })
    @ApiModelProperty("隧道对象")
    private SdTunnels tunnels;

    /** 事件类型 */
    @Excel(name = "事件类型ID")
    @ApiModelProperty("事件类型id")
    private Long eventTypeId;

    /** 事件类型对象 */
    @Excels({
        @Excel(name = "事件类型", targetAttr = "eventType"),
    })
    @ApiModelProperty("事件类型对象")
    private SdEventType eventType;

    /** 事件标题 */
    @Excel(name = "事件标题")
    @ApiModelProperty("事件标题")
    private String eventTitle;

    /** 时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("时间")
    private Date eventTime;

    /** 状态 */
    @Excel(name = "状态")
    @ApiModelProperty("状态")
    private String eventState;

    /** 级别  */
    @Excel(name = "级别 ")
    @ApiModelProperty("级别")
    private String eventGrade;

    /** 位置 */
    @Excel(name = "位置")
    @ApiModelProperty("位置")
    private String eventLocation;

    /** 死亡人数 */
    @Excel(name = "死亡人数")
    @ApiModelProperty("死亡人数")
    private Integer eventDeath;

    /** 受伤人数 */
    @Excel(name = "受伤人数")
    @ApiModelProperty("受伤人数")
    private Integer eventInjured;

    /** 事件描述 */
    @Excel(name = "事件描述")
    @ApiModelProperty("事件描述")
    private String eventDescription;

    /** 预案ID */
    @ApiModelProperty("预案ID")
    private String reservePlanId;

    /** 处理记录ID */
    @ApiModelProperty("处理记录ID")
    private String flowId;

    /** 预警ID */
    @ApiModelProperty("预警ID")
    private Long warningId;

    @ApiModelProperty("开始时间")
    private String beginTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public String getTunnelId() {
		return tunnelId;
	}

	public void setTunnelId(String tunnelId) {
		this.tunnelId = tunnelId;
	}

	public SdTunnels getTunnels() {
		if (tunnels == null)
        {
			tunnels = new SdTunnels();
        }
		return tunnels;
	}

	public void setTunnels(SdTunnels tunnels) {
		this.tunnels = tunnels;
	}

    public void setEventTypeId(Long eventTypeId)
    {
        this.eventTypeId = eventTypeId;
    }

    public Long getEventTypeId()
    {
        return eventTypeId;
    }
    public void setEventTitle(String eventTitle)
    {
        this.eventTitle = eventTitle;
    }

    public String getEventTitle()
    {
        return eventTitle;
    }
    public void setEventTime(Date eventTime)
    {
        this.eventTime = eventTime;
    }

    public Date getEventTime()
    {
        return eventTime;
    }
    public void setEventState(String eventState)
    {
        this.eventState = eventState;
    }

    public String getEventState()
    {
        return eventState;
    }
    public void setEventGrade(String eventGrade)
    {
        this.eventGrade = eventGrade;
    }

    public String getEventGrade()
    {
        return eventGrade;
    }
    public void setEventLocation(String eventLocation)
    {
        this.eventLocation = eventLocation;
    }

    public String getEventLocation()
    {
        return eventLocation;
    }
    public void setEventDeath(Integer eventDeath)
    {
        this.eventDeath = eventDeath;
    }

    public Integer getEventDeath()
    {
        return eventDeath;
    }
    public void setEventInjured(Integer eventInjured)
    {
        this.eventInjured = eventInjured;
    }

    public Integer getEventInjured()
    {
        return eventInjured;
    }
    public void setEventDescription(String eventDescription)
    {
        this.eventDescription = eventDescription;
    }

    public String getEventDescription()
    {
        return eventDescription;
    }
    public void setReservePlanId(String reservePlanId)
    {
        this.reservePlanId = reservePlanId;
    }

    public String getReservePlanId()
    {
        return reservePlanId;
    }
    public void setFlowId(String flowId)
    {
        this.flowId = flowId;
    }

    public String getFlowId()
    {
        return flowId;
    }
    public void setWarningId(Long warningId)
    {
        this.warningId = warningId;
    }

    public Long getWarningId()
    {
        return warningId;
    }

    public SdEventType getEventType() {
    	if (eventType == null)
        {
    		eventType = new SdEventType();
        }
		return eventType;
	}

	public void setEventType(SdEventType eventType) {
		this.eventType = eventType;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tunnelId", getTunnelId())
            .append("tunnels", getTunnels())
            .append("eventTypeId", getEventTypeId())
            .append("eventTitle", getEventTitle())
            .append("eventTime", getEventTime())
            .append("eventState", getEventState())
            .append("eventGrade", getEventGrade())
            .append("eventLocation", getEventLocation())
            .append("eventDeath", getEventDeath())
            .append("eventInjured", getEventInjured())
            .append("eventDescription", getEventDescription())
            .append("reservePlanId", getReservePlanId())
            .append("flowId", getFlowId())
            .append("warningId", getWarningId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("eventType", getEventType())
            .append("beginTime", getBeginTime())
            .append("endTime", getEndTime())
            .toString();
    }
}
