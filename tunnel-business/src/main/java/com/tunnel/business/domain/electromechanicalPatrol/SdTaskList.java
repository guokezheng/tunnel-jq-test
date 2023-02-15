package com.tunnel.business.domain.electromechanicalPatrol;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Excel(name = "计划完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endPlantime;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;


    /** 任务名称 */
    @Excel(name = "任务名称")
    private String taskName;



    private String time;

    public String getTime() {
        return this.time;
    }

    public void setTime(final String time) {
        this.time = time;
    }

    /** 派单人员 */
    @Excel(name = "派单人员")
    private String dispatcher;

    /** 派单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    private String publishStatus;

    /** 任务状态（0::待巡检、1:巡检中、2:已完结、3:待回传、4:已超时） */
    @Excel(name = "任务状态", readConverterExp = "0=::待巡检、1:巡检中、2:已完结、3:待回传、4:已超时")
    private String taskStatus;

    /** 巡查人员id */
    @Excel(name = "巡查人员id")
    private String walkerId;

    /** 任务完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "任务完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date taskEndtime;

    /** 任务持续时间 */
    @Excel(name = "任务持续时间", width = 30, dateFormat = "yyyy-MM-dd")
    private String  taskCxtime;

    /** 现场情况描述 */
    @Excel(name = "现场情况描述")
    private String siteDescription;

    private String dictLabel;


    private String dictValue;



    /*是否超时*/
    private String ifchaosgu;

    public String getIfchaosgu() {
        return this.ifchaosgu;
    }

    public void setIfchaosgu(final String ifchaosgu) {
        this.ifchaosgu = ifchaosgu;
    }

    /*巡检点数量*/
    private Integer totalNum;


    public String getTunnelId() {
        return this.tunnelId;
    }

    public void setTunnelId(final String tunnelId) {
        this.tunnelId = tunnelId;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(final String taskName) {
        this.taskName = taskName;
    }

    public String getDictLabel() {
        return this.dictLabel;
    }

    public void setDictLabel(final String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public String getDictValue() {
        return this.dictValue;
    }

    public void setDictValue(final String dictValue) {
        this.dictValue = dictValue;
    }

    public Integer getTotalNum() {
        return this.totalNum;
    }

    public void setTotalNum(final Integer totalNum) {
        this.totalNum = totalNum;
    }

    private List<String> devicesList;

    public List<String> getDevicesList() {
        return this.devicesList;
    }

    public void setDevicesList(final List<String> devicesList) {
        this.devicesList = devicesList;
    }

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
    public void setPublishStatus(String publishStatus)
    {
        this.publishStatus = publishStatus;
    }

    public String getPublishStatus()
    {
        return publishStatus;
    }
    public void setTaskStatus(String taskStatus)
    {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatus()
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
    public void setTaskCxtime(String taskCxtime)
    {
        this.taskCxtime = taskCxtime;
    }

    public String getTaskCxtime()
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
