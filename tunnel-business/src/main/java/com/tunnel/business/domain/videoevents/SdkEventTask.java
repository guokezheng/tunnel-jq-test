package com.tunnel.business.domain.videoevents;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 事件任务表对象 sdk_event_task
 * 
 * @author ruoyi
 * @date 2021-04-24
 */
public class SdkEventTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    private Integer id;

    /** 通道ID */
    @Excel(name = "容器ID")
    private Integer dockerId;

    private String dockerName;

    public String getDockerName() {
        return dockerName;
    }

    public void setDockerName(String dockerName) {
        this.dockerName = dockerName;
    }

    /** 通道ID */
    @Excel(name = "通道ID")
    private String channelId;

    /** 视频流类型 */
    @Excel(name = "视频流类型")
    private String taskType;

    /** 视频流地址 */
    @Excel(name = "视频流地址")
    private String url;

    /** 回调地址【数组】 */
    @Excel(name = "回调地址【数组】")
    private String results;

    /** 道路类型 */
    @Excel(name = "道路类型")
    private String sceneMode;

    /** 上报间隔（秒） */
    @Excel(name = "上报间隔", readConverterExp = "秒=")
    private Integer reportingInterval;
    /** 夜间模式 */
    @Excel(name = "夜间模式")
    private String nightMode;
    /** 上报间隔（秒） */
    @Excel(name = "开始时间")
    private Integer nightStart;
    /** 上报间隔（秒） */
    @Excel(name = "结束时间")
    private Integer nightEnd;
    /** 修改时间 */
    private Integer updateTtime;
    /** 是否开启夜间模式 */
    private String alarmVideo;
    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getDockerId() {
        return dockerId;
    }

    public void setDockerId(Integer dockerId) {
        this.dockerId = dockerId;
    }

    public Integer getId()
    {
        return id;
    }
    public void setChannelId(String channelId) 
    {
        this.channelId = channelId;
    }

    public String getChannelId() 
    {
        return channelId;
    }
    public void setTaskType(String taskType) 
    {
        this.taskType = taskType;
    }

    public String getTaskType() 
    {
        return taskType;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }
    public void setResults(String results) 
    {
        this.results = results;
    }

    public String getResults() 
    {
        return results;
    }
    public void setSceneMode(String sceneMode) 
    {
        this.sceneMode = sceneMode;
    }

    public String getSceneMode() 
    {
        return sceneMode;
    }
    public void setReportingInterval(Integer reportingInterval) 
    {
        this.reportingInterval = reportingInterval;
    }

    public Integer getReportingInterval() 
    {
        return reportingInterval;
    }
    public void setUpdateTtime(Integer updateTtime) 
    {
        this.updateTtime = updateTtime;
    }

    public Integer getUpdateTtime() 
    {
        return updateTtime;
    }

    public String getNightMode() {
        return nightMode;
    }

    public void setNightMode(String nightMode) {
        this.nightMode = nightMode;
    }

    public Integer getNightStart() {
        return nightStart;
    }

    public void setNightStart(Integer nightStart) {
        this.nightStart = nightStart;
    }

    public Integer getNightEnd() {
        return nightEnd;
    }

    public void setNightEnd(Integer nightEnd) {
        this.nightEnd = nightEnd;
    }

    public String getAlarmVideo() {
        return alarmVideo;
    }

    public void setAlarmVideo(String alarmVideo) {
        this.alarmVideo = alarmVideo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("channelId", getChannelId())
            .append("taskType", getTaskType())
            .append("url", getUrl())
            .append("results", getResults())
            .append("sceneMode", getSceneMode())
            .append("reportingInterval", getReportingInterval())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTtime", getUpdateTtime())
            .toString();
    }
}
