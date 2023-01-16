package com.tunnel.business.domain.event;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 路段统计数据对象 sd_road_section_statistics
 * 
 * @author ruoyi
 * @date 2023-01-13
 */
public class SdRoadSectionStatistics extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

    /** 平均速度 路段平均速度，单位km/h */
    @Excel(name = "平均速度 路段平均速度，单位km/h")
    private String speed;

    /** 时间占有率 */
    @Excel(name = "时间占有率")
    private String timeOccupy;

    /** 空间占有率,路段车辆占道路比例 */
    @Excel(name = "空间占有率,路段车辆占道路比例")
    private String spaceOccupy;

    /** 平均车头间距 单位米 */
    @Excel(name = "平均车头间距 单位米")
    private String gap;

    /** 平均车头时距 单位秒 */
    @Excel(name = "平均车头时距 单位秒")
    private String gapTime;

    /** 时间戳 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "时间戳", width = 30, dateFormat = "yyyy-MM-dd")
    private Date time;

    /** 进入流量 */
    @Excel(name = "进入流量")
    private String inFlow;

    /** 离开流量 */
    @Excel(name = "离开流量")
    private String outFlow;

    /** 拥堵指标 */
    @Excel(name = "拥堵指标")
    private String congestionIndex;

    /** 上下行标志 1：上行，2：下行 */
    @Excel(name = "上下行标志 1：上行，2：下行")
    private String roadDir;

    /** 饱和度 */
    @Excel(name = "饱和度")
    private String saturationVc;

    /** 统计开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "统计开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 统计结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "统计结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    @Excel(name = "车辆数")
    private String cars;

    public String getCars() {
        return cars;
    }

    public void setCars(String cars) {
        this.cars = cars;
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
    public void setSpeed(String speed) 
    {
        this.speed = speed;
    }

    public String getSpeed() 
    {
        return speed;
    }
    public void setTimeOccupy(String timeOccupy) 
    {
        this.timeOccupy = timeOccupy;
    }

    public String getTimeOccupy() 
    {
        return timeOccupy;
    }
    public void setSpaceOccupy(String spaceOccupy) 
    {
        this.spaceOccupy = spaceOccupy;
    }

    public String getSpaceOccupy() 
    {
        return spaceOccupy;
    }
    public void setGap(String gap) 
    {
        this.gap = gap;
    }

    public String getGap() 
    {
        return gap;
    }
    public void setGapTime(String gapTime) 
    {
        this.gapTime = gapTime;
    }

    public String getGapTime() 
    {
        return gapTime;
    }
    public void setTime(Date time) 
    {
        this.time = time;
    }

    public Date getTime() 
    {
        return time;
    }
    public void setInFlow(String inFlow) 
    {
        this.inFlow = inFlow;
    }

    public String getInFlow() 
    {
        return inFlow;
    }
    public void setOutFlow(String outFlow) 
    {
        this.outFlow = outFlow;
    }

    public String getOutFlow() 
    {
        return outFlow;
    }
    public void setCongestionIndex(String congestionIndex) 
    {
        this.congestionIndex = congestionIndex;
    }

    public String getCongestionIndex() 
    {
        return congestionIndex;
    }
    public void setRoadDir(String roadDir) 
    {
        this.roadDir = roadDir;
    }

    public String getRoadDir() 
    {
        return roadDir;
    }
    public void setSaturationVc(String saturationVc) 
    {
        this.saturationVc = saturationVc;
    }

    public String getSaturationVc() 
    {
        return saturationVc;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tunnelId", getTunnelId())
            .append("speed", getSpeed())
            .append("timeOccupy", getTimeOccupy())
            .append("spaceOccupy", getSpaceOccupy())
            .append("gap", getGap())
            .append("gapTime", getGapTime())
            .append("time", getTime())
            .append("inFlow", getInFlow())
            .append("outFlow", getOutFlow())
            .append("congestionIndex", getCongestionIndex())
            .append("roadDir", getRoadDir())
            .append("saturationVc", getSaturationVc())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
