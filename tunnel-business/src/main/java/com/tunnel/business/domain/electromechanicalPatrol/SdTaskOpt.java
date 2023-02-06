package com.tunnel.business.domain.electromechanicalPatrol;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * 巡检任务操作记录表 sd_task_opt
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
public class SdTaskOpt extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 巡检任务id */
    @Excel(name = "巡检任务id")
    private String taskId;

    /** 操作类型 */
    @Excel(name = "操作类型")
    private String optType;

    /** 操作人 */
    @Excel(name = "操作人")
    private String optPersonId;

    private String tunnelName;

    /** 操作时间 */
    @Excel(name = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date optTime;

    /** 操作说明 */
    @Excel(name = "操作说明")
    private String optDescription;

    public String getTunnelName() {
        return this.tunnelName;
    }

    public void setTunnelName(final String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(final String taskId) {
        this.taskId = taskId;
    }

    public String getOptType() {
        return this.optType;
    }

    public void setOptType(final String optType) {
        this.optType = optType;
    }

    public String getOptPersonId() {
        return this.optPersonId;
    }

    public void setOptPersonId(final String optPersonId) {
        this.optPersonId = optPersonId;
    }

    public Date getOptTime() {
        return this.optTime;
    }

    public void setOptTime(final Date optTime) {
        this.optTime = optTime;
    }

    public String getOptDescription() {
        return this.optDescription;
    }

    public void setOptDescription(final String optDescription) {
        this.optDescription = optDescription;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskId", getTaskId())
                .append("optType", getOptType())
                .append("optDescription", getOptDescription())
                .append("optTime", getOptTime())
                .append("optPersonId", getOptPersonId())
                .toString();
    }




}
