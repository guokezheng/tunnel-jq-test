package com.tunnel.platform.domain.trafficOperationControl.situationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）对象 sd_traffic_incident_monitor
 *
 * @author ruoyi
 * @date 2022-03-29
 */
public class SdTrafficIncidentMonitor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long monitorId;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

    /** 方向 */
    @Excel(name = "方向")
    private String direction;

    /** 桩号（KM） */
    @Excel(name = "桩号", readConverterExp = "K=M")
    private String stakeNumber;

    /** 桩号距离（+m） */
    @Excel(name = "桩号距离", readConverterExp = "+=m")
    private String stakeDistance;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 事件源（设备类型） */
    @Excel(name = "事件源", readConverterExp = "设=备类型")
    private String incidentSource;

    /** 事件类型（拥堵事件、交通事故、火灾事件等） */
    @Excel(name = "事件类型", readConverterExp = "拥=堵事件、交通事故、火灾事件等")
    private String incidentType;

    /** 事件内容 */
    @Excel(name = "事件内容")
    private String incidentContent;

    public void setMonitorId(Long monitorId)
    {
        this.monitorId = monitorId;
    }

    public Long getMonitorId()
    {
        return monitorId;
    }
    public void setTunnelId(String tunnelId)
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId()
    {
        return tunnelId;
    }
    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public String getDirection()
    {
        return direction;
    }
    public void setStakeNumber(String stakeNumber)
    {
        this.stakeNumber = stakeNumber;
    }

    public String getStakeNumber()
    {
        return stakeNumber;
    }
    public void setStakeDistance(String stakeDistance)
    {
        this.stakeDistance = stakeDistance;
    }

    public String getStakeDistance()
    {
        return stakeDistance;
    }
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }
    public void setIncidentSource(String incidentSource)
    {
        this.incidentSource = incidentSource;
    }

    public String getIncidentSource()
    {
        return incidentSource;
    }
    public void setIncidentType(String incidentType)
    {
        this.incidentType = incidentType;
    }

    public String getIncidentType()
    {
        return incidentType;
    }
    public void setIncidentContent(String incidentContent)
    {
        this.incidentContent = incidentContent;
    }

    public String getIncidentContent()
    {
        return incidentContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("monitorId", getMonitorId())
            .append("tunnelId", getTunnelId())
            .append("direction", getDirection())
            .append("stakeNumber", getStakeNumber())
            .append("stakeDistance", getStakeDistance())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("incidentSource", getIncidentSource())
            .append("incidentType", getIncidentType())
            .append("incidentContent", getIncidentContent())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
