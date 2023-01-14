package com.tunnel.business.domain.event;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车道统计数据对象 sd_lane_statistics
 * 
 * @author ruoyi
 * @date 2023-01-13
 */
public class SdLaneStatistics extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

    /** 车道编号 */
    @Excel(name = "车道编号")
    private Long laneNo;

    /** 平均速度 单位km/h */
    @Excel(name = "平均速度 单位km/h")
    private String speed;

    /** 时间占有率 */
    @Excel(name = "时间占有率")
    private String timeOccupy;

    /** 空间占有率 */
    @Excel(name = "空间占有率")
    private String spaceOccupy;

    /** 车头间距 单位米 */
    @Excel(name = "车头间距 单位米")
    private String gap;

    /** 车头时距 单位秒 */
    @Excel(name = "车头时距 单位秒")
    private String gapTime;

    /** 时间戳 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "时间戳", width = 30, dateFormat = "yyyy-MM-dd")
    private Date time;

    /** 小车流量 */
    @Excel(name = "小车流量")
    private Long flowSmall;

    /** 中车流量 */
    @Excel(name = "中车流量")
    private Long flowMedium;

    /** 大车流量 */
    @Excel(name = "大车流量")
    private Long flowLarge;

    /** 超大车流量 */
    @Excel(name = "超大车流量")
    private Long flowxLarge;

    /** 车道车辆数 */
    @Excel(name = "车道车辆数")
    private Long cars;

    /** 统计开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "统计开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 统计结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "统计结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 上下行标志 1：上行，2：下行 */
    @Excel(name = "上下行标志 1：上行，2：下行")
    private String roadDir;

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
    public void setLaneNo(Long laneNo) 
    {
        this.laneNo = laneNo;
    }

    public Long getLaneNo() 
    {
        return laneNo;
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
    public void setFlowSmall(Long flowSmall) 
    {
        this.flowSmall = flowSmall;
    }

    public Long getFlowSmall() 
    {
        return flowSmall;
    }
    public void setFlowMedium(Long flowMedium) 
    {
        this.flowMedium = flowMedium;
    }

    public Long getFlowMedium() 
    {
        return flowMedium;
    }
    public void setFlowLarge(Long flowLarge) 
    {
        this.flowLarge = flowLarge;
    }

    public Long getFlowLarge() 
    {
        return flowLarge;
    }
    public void setFlowxLarge(Long flowxLarge) 
    {
        this.flowxLarge = flowxLarge;
    }

    public Long getFlowxLarge() 
    {
        return flowxLarge;
    }
    public void setCars(Long cars) 
    {
        this.cars = cars;
    }

    public Long getCars() 
    {
        return cars;
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
    public void setRoadDir(String roadDir) 
    {
        this.roadDir = roadDir;
    }

    public String getRoadDir() 
    {
        return roadDir;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tunnelId", getTunnelId())
            .append("laneNo", getLaneNo())
            .append("speed", getSpeed())
            .append("timeOccupy", getTimeOccupy())
            .append("spaceOccupy", getSpaceOccupy())
            .append("gap", getGap())
            .append("gapTime", getGapTime())
            .append("time", getTime())
            .append("flowSmall", getFlowSmall())
            .append("flowMedium", getFlowMedium())
            .append("flowLarge", getFlowLarge())
            .append("flowxLarge", getFlowxLarge())
            .append("cars", getCars())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("roadDir", getRoadDir())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
