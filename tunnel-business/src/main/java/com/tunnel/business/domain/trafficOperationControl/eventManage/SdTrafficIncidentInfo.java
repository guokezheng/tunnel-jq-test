package com.tunnel.business.domain.trafficOperationControl.eventManage;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 交通运行管控-事件信息管理对象 sd_traffic_incident_info
 *
 * @author ruoyi
 * @date 2022-02-14
 */
public class SdTrafficIncidentInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 事件id */
    private Long id;

//    /** 隧道所id */
//    @Excel(name = "隧道所id")
//    private String tunnelStationId;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

    /** 事件类型 */
    @Excel(name = "事件类型")
    private String incidentType;

    /** 事件状态 */
    @Excel(name = "事件状态")
    private String incidentStatus;

    /**
     * 事件内容
     */
    @Excel(name = "事件内容")
    private String incidentContent;

    /** 发生时间 */
    @Excel(name = "发生时间")
    private String occurTime;

    /** 信息来源 */
    @Excel(name = "信息来源")
    private String msgSource;

    /** 报告人 */
    @Excel(name = "报告人")
    private String reportPerson;

    /** 报告时间 */
    @Excel(name = "报告时间")
    private String reportTime;

    /** 事件级别 */
    @Excel(name = "事件级别")
    private String incidentGrade;

    /** 是否接交警通知 */
    @Excel(name = "是否接交警通知")
    private String policeInform;

    /** 采取措施 */
    @Excel(name = "采取措施")
    private String takeMeasure;

    /** 发布对象 */
    @Excel(name = "发布对象")
//    private List<SdTrafficIncidentPublishObject> publishObject;
    private List<String> publishObject;

    /**
     * 发布状态
     */
    private String publishStatus;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
//    public void setTunnelStationId(String tunnelStationId)
//    {
//        this.tunnelStationId = tunnelStationId;
//    }
//
//    public String getTunnelStationId()
//    {
//        return tunnelStationId;
//    }
    public void setTunnelId(String tunnelId)
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId()
    {
        return tunnelId;
    }
    public void setIncidentType(String incidentType)
    {
        this.incidentType = incidentType;
    }

    public String getIncidentType()
    {
        return incidentType;
    }
    public void setIncidentStatus(String incidentStatus)
    {
        this.incidentStatus = incidentStatus;
    }

    public String getIncidentStatus()
    {
        return incidentStatus;
    }
    public void setOccurTime(String occurTime)
    {
        this.occurTime = occurTime;
    }

    public String getIncidentContent() {
        return incidentContent;
    }

    public void setIncidentContent(String incidentContent) {
        this.incidentContent = incidentContent;
    }

    public String getOccurTime()
    {
        return occurTime;
    }
    public void setMsgSource(String msgSource)
    {
        this.msgSource = msgSource;
    }

    public String getMsgSource()
    {
        return msgSource;
    }
    public void setReportPerson(String reportPerson)
    {
        this.reportPerson = reportPerson;
    }

    public String getReportPerson()
    {
        return reportPerson;
    }
    public void setReportTime(String reportTime)
    {
        this.reportTime = reportTime;
    }

    public String getReportTime()
    {
        return reportTime;
    }
    public void setIncidentGrade(String incidentGrade)
    {
        this.incidentGrade = incidentGrade;
    }

    public String getIncidentGrade()
    {
        return incidentGrade;
    }
    public void setPoliceInform(String policeInform)
    {
        this.policeInform = policeInform;
    }

    public String getPoliceInform()
    {
        return policeInform;
    }
    public void setTakeMeasure(String takeMeasure)
    {
        this.takeMeasure = takeMeasure;
    }

    public String getTakeMeasure()
    {
        return takeMeasure;
    }

    public List<String> getPublishObject() {
        return publishObject;
    }

    public void setPublishObject(List<String> publishObject) {
        this.publishObject = publishObject;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
//            .append("tunnelStationId", getTunnelStationId())
            .append("tunnelId", getTunnelId())
            .append("incidentType", getIncidentType())
            .append("incidentStatus", getIncidentStatus())
                .append("incidentContent",getIncidentContent())
            .append("occurTime", getOccurTime())
            .append("msgSource", getMsgSource())
            .append("reportPerson", getReportPerson())
            .append("reportTime", getReportTime())
            .append("incidentGrade", getIncidentGrade())
            .append("policeInform", getPoliceInform())
            .append("takeMeasure", getTakeMeasure())
            .append("publishObject", getPublishObject())
                .append("publishStatus",getPublishStatus())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
