package com.tunnel.business.domain.event;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 路段车辆行驶记录对象 sd_vehicle_driving
 * 
 * @author ruoyi
 * @date 2023-01-13
 */
public class SdVehicleDriving extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

    /** 轨迹ID */
    @Excel(name = "轨迹ID")
    private Long trackId;

    /** 车牌颜色 */
    @Excel(name = "车牌颜色")
    private String plateColor;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String plateNumber;

    /** 目标类型 */
    @Excel(name = "目标类型")
    private String objectType;

    /** 车辆类型 */
    @Excel(name = "车辆类型")
    private String vehicleType;

    /** 车身颜色 */
    @Excel(name = "车身颜色")
    private String vehicleColor;

    /** 车辆速度，单位：km/h */
    @Excel(name = "车辆速度，单位：km/h")
    private String speed;

    /** 进入或者离开标志 1：进入2：离开 */
    @Excel(name = "进入或者离开标志 1：进入2：离开")
    private String travelType;

    /** 进入或离开的时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "进入或离开的时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

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
    public void setTrackId(Long trackId) 
    {
        this.trackId = trackId;
    }

    public Long getTrackId() 
    {
        return trackId;
    }
    public void setPlateColor(String plateColor) 
    {
        this.plateColor = plateColor;
    }

    public String getPlateColor() 
    {
        return plateColor;
    }
    public void setPlateNumber(String plateNumber) 
    {
        this.plateNumber = plateNumber;
    }

    public String getPlateNumber() 
    {
        return plateNumber;
    }
    public void setObjectType(String objectType) 
    {
        this.objectType = objectType;
    }

    public String getObjectType() 
    {
        return objectType;
    }
    public void setVehicleType(String vehicleType) 
    {
        this.vehicleType = vehicleType;
    }

    public String getVehicleType() 
    {
        return vehicleType;
    }
    public void setVehicleColor(String vehicleColor) 
    {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleColor() 
    {
        return vehicleColor;
    }
    public void setSpeed(String speed) 
    {
        this.speed = speed;
    }

    public String getSpeed() 
    {
        return speed;
    }
    public void setTravelType(String travelType) 
    {
        this.travelType = travelType;
    }

    public String getTravelType() 
    {
        return travelType;
    }
    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
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
            .append("trackId", getTrackId())
            .append("plateColor", getPlateColor())
            .append("plateNumber", getPlateNumber())
            .append("objectType", getObjectType())
            .append("vehicleType", getVehicleType())
            .append("vehicleColor", getVehicleColor())
            .append("speed", getSpeed())
            .append("travelType", getTravelType())
            .append("startTime", getStartTime())
            .append("roadDir", getRoadDir())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
