package com.tunnel.business.domain.vehicle;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 隧道车辆数据（单车数据）对象 sd_vehicle_data
 *
 * @author ruoyi
 * @date 2023-02-25
 */
public class SdVehicleData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 轨迹ID */
    @Excel(name = "轨迹ID")
    private Long trackId;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

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

    /** 车辆类型 */
    @Excel(name = "进入离开")
    private String travelType;

    /** 车身颜色 */
    @Excel(name = "车身颜色")
    private String vehicleColor;

    /** 车辆速度，单位：km/h */
    @Excel(name = "车辆速度，单位：km/h")
    private String speed;

    /** 上下行标志 1：上行，2：下行 */
    @Excel(name = "上下行标志 1：上行，2：下行")
    private String direction;

    /** 创建时间 */
    @ApiModelProperty("驶入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTrackId(Long trackId)
    {
        this.trackId = trackId;
    }

    public Long getTrackId()
    {
        return trackId;
    }
    public void setTunnelId(String tunnelId)
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId()
    {
        return tunnelId;
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
    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public String getDirection()
    {
        return direction;
    }

    public String getTravelType() {
        return travelType;
    }

    public void setTravelType(String travelType) {
        this.travelType = travelType;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("trackId", getTrackId())
            .append("tunnelId", getTunnelId())
            .append("plateColor", getPlateColor())
            .append("plateNumber", getPlateNumber())
            .append("objectType", getObjectType())
            .append("vehicleType", getVehicleType())
            .append("vehicleColor", getVehicleColor())
            .append("speed", getSpeed())
            .append("direction", getDirection())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
