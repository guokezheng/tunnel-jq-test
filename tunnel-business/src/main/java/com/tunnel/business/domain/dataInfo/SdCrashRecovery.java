package com.tunnel.business.domain.dataInfo;

import java.util.Date;

import com.tunnel.business.domain.event.SdWarningInfo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 应急恢复对象 sd_crash_recovery
 * 
 * @author why
 * @date 2022-02-22
 */
public class SdCrashRecovery extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 应急恢复编码 */
    private Long id;

    /** 隧道编码 */
    @Excel(name = "隧道编码")
    private String tunnelId;

    /** 事件编码 */
    @Excel(name = "事件编码")
    private Long eventId;

    /** 触发之前内容 */
    @Excel(name = "触发之前内容")
    private String beforeContent;

    /** 执行之前图片 */
    @Excel(name = "执行之前图片")
    private String beforeImage;

    /** 执行之前截图时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "执行之前截图时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date beforeTime;

    /** 触发之后内容 */
    @Excel(name = "触发之后内容")
    private String afterContent;

    /** 执行之后图片 */
    @Excel(name = "执行之后图片")
    private String afterImage;

    /** 执行之后截图时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "执行之后截图时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date afterTime;

    /** 执行策略内容 */
    @Excel(name = "执行策略内容")
    private String strategyContent;

    /** 状态 1：未恢复 2：已完成 */
    @Excel(name = "状态 1：未恢复 2：已完成")
    private String state;

    /** 恢复时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "恢复时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date recoverTime;

    /** 恢复内容 */
    @Excel(name = "恢复内容")
    private String recoverContent;

    /** 恢复截图 */
    @Excel(name = "恢复截图")
    private String recoverImage;

    /** 完结内容详情描述 */
    @Excel(name = "完结内容详情描述")
    private String recoverDetails;

    private SdWarningInfo warningInfo;

    private String tunnelName;

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public SdWarningInfo getWarningInfo() {
        if (warningInfo == null)
        {
            warningInfo = new SdWarningInfo();
        }
        return warningInfo;
    }

    public void setWarningInfo(SdWarningInfo warningInfo) {
        this.warningInfo = warningInfo;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTunnelId(String tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId() 
    {
        return tunnelId;
    }
    public void setEventId(Long eventId) 
    {
        this.eventId = eventId;
    }

    public Long getEventId() 
    {
        return eventId;
    }
    public void setBeforeContent(String beforeContent) 
    {
        this.beforeContent = beforeContent;
    }

    public String getBeforeContent() 
    {
        return beforeContent;
    }
    public void setBeforeImage(String beforeImage) 
    {
        this.beforeImage = beforeImage;
    }

    public String getBeforeImage() 
    {
        return beforeImage;
    }
    public void setBeforeTime(Date beforeTime) 
    {
        this.beforeTime = beforeTime;
    }

    public Date getBeforeTime() 
    {
        return beforeTime;
    }
    public void setAfterContent(String afterContent) 
    {
        this.afterContent = afterContent;
    }

    public String getAfterContent() 
    {
        return afterContent;
    }
    public void setAfterImage(String afterImage) 
    {
        this.afterImage = afterImage;
    }

    public String getAfterImage() 
    {
        return afterImage;
    }
    public void setAfterTime(Date afterTime) 
    {
        this.afterTime = afterTime;
    }

    public Date getAfterTime() 
    {
        return afterTime;
    }
    public void setStrategyContent(String strategyContent) 
    {
        this.strategyContent = strategyContent;
    }

    public String getStrategyContent() 
    {
        return strategyContent;
    }
    public void setState(String state) 
    {
        this.state = state;
    }

    public String getState() 
    {
        return state;
    }
    public void setRecoverTime(Date recoverTime) 
    {
        this.recoverTime = recoverTime;
    }

    public Date getRecoverTime() 
    {
        return recoverTime;
    }
    public void setRecoverContent(String recoverContent) 
    {
        this.recoverContent = recoverContent;
    }

    public String getRecoverContent() 
    {
        return recoverContent;
    }
    public void setRecoverImage(String recoverImage) 
    {
        this.recoverImage = recoverImage;
    }

    public String getRecoverImage() 
    {
        return recoverImage;
    }
    public void setRecoverDetails(String recoverDetails) 
    {
        this.recoverDetails = recoverDetails;
    }

    public String getRecoverDetails() 
    {
        return recoverDetails;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tunnelId", getTunnelId())
            .append("eventId", getEventId())
            .append("beforeContent", getBeforeContent())
            .append("beforeImage", getBeforeImage())
            .append("beforeTime", getBeforeTime())
            .append("afterContent", getAfterContent())
            .append("afterImage", getAfterImage())
            .append("afterTime", getAfterTime())
            .append("strategyContent", getStrategyContent())
            .append("state", getState())
            .append("recoverTime", getRecoverTime())
            .append("recoverContent", getRecoverContent())
            .append("recoverImage", getRecoverImage())
            .append("recoverDetails", getRecoverDetails())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("warningInfo", getWarningInfo())
            .toString();
    }
}
