package com.tunnel.business.domain.videoevents;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车道信息对象 sdk_event_lane
 * 
 * @author ruoyi
 * @date 2021-04-26
 */
public class SdkEventLane extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 车道ID */
    private Integer id;

    /** 任务ID */
    @Excel(name = "任务ID")
    private Integer taskId;

    /** 车道开始方向 */
    @Excel(name = "车道开始方向")
    private String start;

    /** 车道结束方向 */
    @Excel(name = "车道结束方向")
    private String end;

    /** 车道坐标 */
    @Excel(name = "车道坐标")
    private String coordinates;

    /** 车道检测区域 */
    @Excel(name = "车道检测区域")
    private String virtualLoop;

    /** 车道类型 */
    @Excel(name = "车道类型")
    private String laneType;

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
    public void setStart(String start) 
    {
        this.start = start;
    }

    public String getStart() 
    {
        return start;
    }
    public void setEnd(String end) 
    {
        this.end = end;
    }

    public String getEnd() 
    {
        return end;
    }
    public void setCoordinates(String coordinates) 
    {
        this.coordinates = coordinates;
    }

    public String getCoordinates() 
    {
        return coordinates;
    }
    public void setVirtualLoop(String virtualLoop) 
    {
        this.virtualLoop = virtualLoop;
    }

    public String getVirtualLoop() 
    {
        return virtualLoop;
    }
    public void setLaneType(String laneType) 
    {
        this.laneType = laneType;
    }

    public String getLaneType() 
    {
        return laneType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskId", getTaskId())
            .append("start", getStart())
            .append("end", getEnd())
            .append("coordinates", getCoordinates())
            .append("virtualLoop", getVirtualLoop())
            .append("laneType", getLaneType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
