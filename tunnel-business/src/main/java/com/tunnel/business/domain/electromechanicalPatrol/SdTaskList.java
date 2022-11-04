package com.tunnel.business.domain.electromechanicalPatrol;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 巡查任务对象 sd_task_list
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
public class SdTaskList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务编号 */
    private String id;

    /** 所属单位id(新增时保存当前登录人的单位id) */
    @Excel(name = "所属单位id(新增时保存当前登录人的单位id)")
    private String zzjgId;

    /** 计划完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endPlantime;

    /** 派单人员 */
    @Excel(name = "派单人员")
    private String dispatcher;

    /** 派单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "派单时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dispatchTime;

    /** 指派巡查班组id */
    @Excel(name = "指派巡查班组id")
    private String bzId;

    /** 任务描述 */
    @Excel(name = "任务描述")
    private String taskDescription;

    /** 发布状态(0:未发布状态;1:已废止状态;2:发布状态) */
    @Excel(name = "发布状态(0:未发布状态;1:已废止状态;2:发布状态)")
    private Integer publishStatus;

    /** 任务状态（0::待巡检、1:巡检中、2:已完结、3:待回传、4:已超时） */
    @Excel(name = "任务状态", readConverterExp = "0=::待巡检、1:巡检中、2:已完结、3:待回传、4:已超时")
    private Integer taskStatus;

    /** 巡查人员id */
    @Excel(name = "巡查人员id")
    private String walkerId;

    /** 任务完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "任务完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date taskEndtime;

    /** 任务持续时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "任务持续时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date taskCxtime;

    /** 现场情况描述 */
    @Excel(name = "现场情况描述")
    private String siteDescription;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setZzjgId(String zzjgId) 
    {
        this.zzjgId = zzjgId;
    }

    public String getZzjgId() 
    {
        return zzjgId;
    }
    public void setEndPlantime(Date endPlantime) 
    {
        this.endPlantime = endPlantime;
    }

    public Date getEndPlantime() 
    {
        return endPlantime;
    }
    public void setDispatcher(String dispatcher) 
    {
        this.dispatcher = dispatcher;
    }

    public String getDispatcher() 
    {
        return dispatcher;
    }
    public void setDispatchTime(Date dispatchTime) 
    {
        this.dispatchTime = dispatchTime;
    }

    public Date getDispatchTime() 
    {
        return dispatchTime;
    }
    public void setBzId(String bzId) 
    {
        this.bzId = bzId;
    }

    public String getBzId() 
    {
        return bzId;
    }
    public void setTaskDescription(String taskDescription) 
    {
        this.taskDescription = taskDescription;
    }

    public String getTaskDescription() 
    {
        return taskDescription;
    }
    public void setPublishStatus(Integer publishStatus) 
    {
        this.publishStatus = publishStatus;
    }

    public Integer getPublishStatus() 
    {
        return publishStatus;
    }
    public void setTaskStatus(Integer taskStatus) 
    {
        this.taskStatus = taskStatus;
    }

    public Integer getTaskStatus() 
    {
        return taskStatus;
    }
    public void setWalkerId(String walkerId) 
    {
        this.walkerId = walkerId;
    }

    public String getWalkerId() 
    {
        return walkerId;
    }
    public void setTaskEndtime(Date taskEndtime) 
    {
        this.taskEndtime = taskEndtime;
    }

    public Date getTaskEndtime() 
    {
        return taskEndtime;
    }
    public void setTaskCxtime(Date taskCxtime) 
    {
        this.taskCxtime = taskCxtime;
    }

    public Date getTaskCxtime() 
    {
        return taskCxtime;
    }
    public void setSiteDescription(String siteDescription) 
    {
        this.siteDescription = siteDescription;
    }

    public String getSiteDescription() 
    {
        return siteDescription;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("zzjgId", getZzjgId())
            .append("endPlantime", getEndPlantime())
            .append("dispatcher", getDispatcher())
            .append("dispatchTime", getDispatchTime())
            .append("bzId", getBzId())
            .append("taskDescription", getTaskDescription())
            .append("publishStatus", getPublishStatus())
            .append("taskStatus", getTaskStatus())
            .append("walkerId", getWalkerId())
            .append("taskEndtime", getTaskEndtime())
            .append("taskCxtime", getTaskCxtime())
            .append("siteDescription", getSiteDescription())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
