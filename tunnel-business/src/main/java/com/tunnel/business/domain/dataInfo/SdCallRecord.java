package com.tunnel.business.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 紧急电话记录对象 sd_call_record
 * 
 * @author yanghousheng
 * @date 2020-11-10
 */
public class SdCallRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 呼叫编号 */
    @Excel(name = "呼叫编号")
    private String callId;

    /** 主叫人员 */
    @Excel(name = "主叫人员")
    private String callName;

    /** 主叫号码 */
    @Excel(name = "主叫号码")
    private String cpn;
    
    /** 开始时间 */
    private String beginTime;
    
    /** 结束时间 */
    private String endTime;
    
    /**
     * 位置id
     */
    @Excel(name = "位置id")
    private String pathId;
    /**
     * 通话类别
     */
    @Excel(name = "通话类别")
    private String type;
    
    
    /**
     * 被叫号码
     */
    @Excel(name = "被叫号码")
    private String cdpn;
    
    
    /** 紧急区域 */
    @Excel(name = "紧急区域")
    private String position;
    /** 大于或等于此开始时间 */
    @Excel(name = "开始时间")
    private String timeStart;
    /** 小于或等于此结束时间 */
    @Excel(name = "结束时间")
    private String timeEnd;
    

    public String getCpn() {
		return cpn;
	}

	public void setCpn(String cpn) {
		this.cpn = cpn;
	}

	public String getPathId() {
		return pathId;
	}

	public void setPathId(String pathId) {
		this.pathId = pathId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCdpn() {
		return cdpn;
	}

	public void setCdpn(String cdpn) {
		this.cdpn = cdpn;
	}

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	/** 开始时间 */
    @Excel(name = "开始时间")
    private String startDate;

    /** 结束时间 */
    @Excel(name = "结束时间")
    private String endDate;

    /** 接听人员 */
    @Excel(name = "接听人员")
    private String receptionName;

    /** 被叫号码 */
    @Excel(name = "被叫号码")
    private String receptionNumber;

    /** 电话内容 */
    @Excel(name = "电话内容")
    private String telephoneContent;
    
    

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
    public void setCallId(String callId) 
    {
        this.callId = callId;
    }

    public String getCallId() 
    {
        return callId;
    }
    public void setCallName(String callName) 
    {
        this.callName = callName;
    }

    public String getCallName() 
    {
        return callName;
    }
    /*public void setCallNumber(String callNumber) 
    {
        this.callNumber = callNumber;
    }

    public String getCallNumber() 
    {
        return callNumber;
    }*/
    public void setPosition(String position) 
    {
        this.position = position;
    }

    public String getPosition() 
    {
        return position;
    }
    public void setStartDate(String startDate) 
    {
        this.startDate = startDate;
    }

    public String getStartDate() 
    {
        return startDate;
    }
    public void setEndDate(String endDate) 
    {
        this.endDate = endDate;
    }

    public String getEndDate() 
    {
        return endDate;
    }
    public void setReceptionName(String receptionName) 
    {
        this.receptionName = receptionName;
    }

    public String getReceptionName() 
    {
        return receptionName;
    }
    public void setReceptionNumber(String receptionNumber) 
    {
        this.receptionNumber = receptionNumber;
    }

    public String getReceptionNumber() 
    {
        return receptionNumber;
    }
    public void setTelephoneContent(String telephoneContent) 
    {
        this.telephoneContent = telephoneContent;
    }

    public String getTelephoneContent() 
    {
        return telephoneContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("callId", getCallId())
            .append("callName", getCallName())
            .append("cpn",getCpn())
            .append("cdpn",getCdpn())
            .append("pathId",getPathId())
            .append("type",getType())
            .append("timeStart", getTimeStart())
            .append("timeEnd", getTimeEnd())
           /* .append("callNumber", getCallNumber())*/
            .append("position", getPosition())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("receptionName", getReceptionName())
            .append("receptionNumber", getReceptionNumber())
            .append("telephoneContent", getTelephoneContent())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("beginTime",getBeginTime())
            .append("endTime",getEndTime())
            .toString();
    }
}