package com.tunnel.business.domain.wisdomLight;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.sql.Date;

/**
 * 【智慧调光】对象  Sd_Wisdom_Light
 * 
 * @author HYW
 * @date 2023-08-10
 */
public class SdWisdomLight extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

    /** 所属隧道 */
    @Excel(name = "所属隧道")
    private String tunnelName;

    /** 模式类型： 0定时模式  1  自动模式  2 节能模式 */
    @Excel(name = "模式类型： 0定时模式  1  自动模式  2 节能模式")
    private Integer modeType;

    /** 时间段配置参数 */
    @Excel(name = "时间段配置参数")
    private String timeSlot;

    /** 当前亮度值(0-100) */
//    @Excel(name = "当前亮度值(0-100)")
    private String beforeLuminance;

    /** 最小亮度值(0-100) */
    @Excel(name = "最小亮度值(0-100)")
    private Integer minLuminance;

    /** 响应时长 单位：秒s */
    @Excel(name = "响应时长 单位：秒s")
    private Long respondTime;

    /** 调光最大区间 */
    @Excel(name = "调光最大区间")
    private Integer maxLuminanceRange;

    /** 调光最小区间 */
    @Excel(name = "调光最小区间")
    private Integer minLuminanceRange;

    /** 最大车流量 */
    @Excel(name = "最大车流量")
    private Long maxTrafficFlow;

    /** 是否开启调光模式 0 关闭  1开启 */
    @Excel(name = "是否开启调光模式 0 关闭  1开启")
    private Integer isStatus;

    /** 是否开启车流量模式 0 关闭  1开启 */
    @Excel(name = "是否开启车流量模式 0 关闭  1开启")
    private Integer isTrafficVolume;

    /** 隧道方向 */
    @Excel(name = "上行下行")
    private String direction;


//    private String createBy;
//
//    /** 创建时间 */
//    @ApiModelProperty("创建时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date createTime;

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
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
    public void setModeType(Integer modeType)
    {
        this.modeType = modeType;
    }

    public Integer getModeType()
    {
        return modeType;
    }
    public void setTimeSlot(String timeSlot)
    {
        this.timeSlot = timeSlot;
    }

    public String getTimeSlot()
    {
        return timeSlot;
    }
    public void setBeforeLuminance(String beforeLuminance)
    {
        this.beforeLuminance = beforeLuminance;
    }

    public String getBeforeLuminance()
    {
        return beforeLuminance;
    }
    public void setMinLuminance(Integer minLuminance)
    {
        this.minLuminance = minLuminance;
    }

    public Integer getMinLuminance()
    {
        return minLuminance;
    }
    public void setRespondTime(Long respondTime)
    {
        this.respondTime = respondTime;
    }

    public Long getRespondTime()
    {
        return respondTime;
    }
    public void setMaxLuminanceRange(Integer maxLuminanceRange)
    {
        this.maxLuminanceRange = maxLuminanceRange;
    }

    public Integer getMaxLuminanceRange()
    {
        return maxLuminanceRange;
    }
    public void setMinLuminanceRange(Integer minLuminanceRange)
    {
        this.minLuminanceRange = minLuminanceRange;
    }

    public Integer getMinLuminanceRange()
    {
        return minLuminanceRange;
    }
    public void setMaxTrafficFlow(Long maxTrafficFlow)
    {
        this.maxTrafficFlow = maxTrafficFlow;
    }

    public Long getMaxTrafficFlow()
    {
        return maxTrafficFlow;
    }
    public void setIsStatus(Integer isStatus)
    {
        this.isStatus = isStatus;
    }

    public Integer getIsStatus()
    {
        return isStatus;
    }
    public void setIsTrafficVolume(Integer isTrafficVolume)
    {
        this.isTrafficVolume = isTrafficVolume;
    }

    public Integer getIsTrafficVolume()
    {
        return isTrafficVolume;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }


//    @Override
//    public String getCreateBy() {
//        return createBy;
//    }
//
//    @Override
//    public void setCreateBy(String createBy) {
//        this.createBy = createBy;
//    }
//
//    @Override
//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tunnelId", getTunnelId())
                .append("modeType", getModeType())
                .append("timeSlot", getTimeSlot())
                .append("beforeLuminance", getBeforeLuminance())
                .append("minLuminance", getMinLuminance())
                .append("respondTime", getRespondTime())
                .append("maxLuminanceRange", getMaxLuminanceRange())
                .append("minLuminanceRange", getMinLuminanceRange())
                .append("maxTrafficFlow", getMaxTrafficFlow())
                .append("isStatus", getIsStatus())
                .append("isTrafficVolume", getIsTrafficVolume())
                .toString();
    }
}
