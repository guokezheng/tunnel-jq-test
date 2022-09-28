package com.tunnel.business.domain.videoevents;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车道事件对象 sdk_event_analysis
 * 
 * @author ruoyi
 * @date 2021-04-26
 */
public class SdkEventAnalysis extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 事件ID */
    private Integer id;

    /** 任务ID */
    @Excel(name = "任务ID")
    private Integer taskId;

    /** 抛洒物检测灵敏度[0,100] */
    @Excel(name = "抛洒物检测灵敏度[0,100]")
    private Integer abandonedObjectSense;

    /** 拥堵判定标准（秒），当拥堵达到该时长时上报拥堵事件 */
    @Excel(name = "拥堵判定标准", readConverterExp = "秒=")
    private Integer congestionInterval;

    /** 非法停车时间(秒) */
    @Excel(name = "非法停车时间(秒)")
    private Integer illegalParkTime;

    /** 事件区域 */
    @Excel(name = "事件区域")
    private String coordinates;

    /** 违章类型【'',''】 */
    @Excel(name = "违章类型【'',''】")
    private String incident;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setTaskId(Integer taskId) 
    {
        this.taskId = taskId;
    }

    public Integer getTaskId() 
    {
        return taskId;
    }
    public void setAbandonedObjectSense(Integer abandonedObjectSense) 
    {
        this.abandonedObjectSense = abandonedObjectSense;
    }

    public Integer getAbandonedObjectSense() 
    {
        return abandonedObjectSense;
    }
    public void setCongestionInterval(Integer congestionInterval) 
    {
        this.congestionInterval = congestionInterval;
    }

    public Integer getCongestionInterval() 
    {
        return congestionInterval;
    }
    public void setIllegalParkTime(Integer illegalParkTime) 
    {
        this.illegalParkTime = illegalParkTime;
    }

    public Integer getIllegalParkTime() 
    {
        return illegalParkTime;
    }
    public void setCoordinates(String coordinates) 
    {
        this.coordinates = coordinates;
    }

    public String getCoordinates() 
    {
        return coordinates;
    }
    public void setIncident(String incident) 
    {
        this.incident = incident;
    }

    public String getIncident() 
    {
        return incident;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskId", getTaskId())
            .append("abandonedObjectSense", getAbandonedObjectSense())
            .append("congestionInterval", getCongestionInterval())
            .append("illegalParkTime", getIllegalParkTime())
            .append("coordinates", getCoordinates())
            .append("incident", getIncident())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
