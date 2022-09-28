package com.tunnel.business.domain.informationBoard;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 交通事件对象 sd_event_operating
 * 
 * @author 刘方堃
 * @date 2021-12-03
 */
public class SdEventOperating extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 事件编号 */
    private String evtId;

    /** 发生时间 */
    @Excel(name = "发生时间")
    private String occurTime;

    /** 上报时间 */
    @Excel(name = "上报时间")
    private String reportTime;

    /** 上报人员 */
    @Excel(name = "上报人员")
    private String reportUser;

    /** 是否结束 */
    @Excel(name = "是否结束")
    private String closed;

    /** 是否有效 */
    @Excel(name = "是否有效")
    private String available;

    /** 是否推送 */
    @Excel(name = "是否推送")
    private String published;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String creatTime;

    /** 修改时间 */
    @Excel(name = "修改时间")
    private String updataTime;

    /** 上报类型 */
    @Excel(name = "上报类型")
    private String type;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setEvtId(String evtId) 
    {
        this.evtId = evtId;
    }

    public String getEvtId() 
    {
        return evtId;
    }
    public void setOccurTime(String occurTime) 
    {
        this.occurTime = occurTime;
    }

    public String getOccurTime() 
    {
        return occurTime;
    }
    public void setReportTime(String reportTime) 
    {
        this.reportTime = reportTime;
    }

    public String getReportTime() 
    {
        return reportTime;
    }
    public void setReportUser(String reportUser) 
    {
        this.reportUser = reportUser;
    }

    public String getReportUser() 
    {
        return reportUser;
    }
    public void setClosed(String closed) 
    {
        this.closed = closed;
    }

    public String getClosed() 
    {
        return closed;
    }
    public void setAvailable(String available) 
    {
        this.available = available;
    }

    public String getAvailable() 
    {
        return available;
    }
    public void setPublished(String published) 
    {
        this.published = published;
    }

    public String getPublished() 
    {
        return published;
    }
    public void setCreatTime(String creatTime) 
    {
        this.creatTime = creatTime;
    }

    public String getCreatTime() 
    {
        return creatTime;
    }
    public void setUpdataTime(String updataTime) 
    {
        this.updataTime = updataTime;
    }

    public String getUpdataTime() 
    {
        return updataTime;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("evtId", getEvtId())
            .append("occurTime", getOccurTime())
            .append("reportTime", getReportTime())
            .append("reportUser", getReportUser())
            .append("closed", getClosed())
            .append("available", getAvailable())
            .append("published", getPublished())
            .append("creatTime", getCreatTime())
            .append("updataTime", getUpdataTime())
            .append("remark", getRemark())
            .append("type", getType())
            .toString();
    }
}
