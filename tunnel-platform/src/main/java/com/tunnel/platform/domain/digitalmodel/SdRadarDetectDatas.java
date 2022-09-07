package com.tunnel.platform.domain.digitalmodel;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 雷达监测感知数据对象 sd_radar_detect_data
 * 
 * @author ruoyi
 * @date 2022-09-04
 */
public class SdRadarDetectDatas extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 车辆id */
    @Excel(name = "车辆id")
    private String vehicleId;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

    /** 车辆类型 */
    @Excel(name = "车辆类型")
    private String vehicleType;

    /** 车辆颜色 */
    @Excel(name = "车辆颜色")
    private String vehicleColor;

    /** 经度,分辨率1e-7°，东经为正，西经为负 */
    @Excel(name = "经度,分辨率1e-7°，东经为正，西经为负")
    private String longitude;

    /** 纬度,分辨率1e-7°，北纬为正，南纬为负 */
    @Excel(name = "纬度,分辨率1e-7°，北纬为正，南纬为负")
    private String latitude;

    /** 速度，单位：Km/h */
    @Excel(name = "速度，单位：Km/h")
    private String speed;

    /** 车道号,沿行车方向从左往右依次为1,2,3,4… */
    @Excel(name = "车道号,沿行车方向从左往右依次为1,2,3,4…")
    private String laneNum;

    /** 航向角，单位：°，保留1位小数，车头与正北夹角 */
    @Excel(name = "航向角，单位：°，保留1位小数，车头与正北夹角")
    private String courseAngle;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String vehicleLicense;

    /** 车牌颜色 */
    @Excel(name = "车牌颜色")
    private String licenseColor;

    /** 桩号 */
    @Excel(name = "桩号")
    private String stakeNum;

    /** 监测时间（保留3位毫秒数） */
    @Excel(name = "监测时间", readConverterExp = "保=留3位毫秒数")
    private Date detectTime;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setVehicleId(String vehicleId) 
    {
        this.vehicleId = vehicleId;
    }

    public String getVehicleId() 
    {
        return vehicleId;
    }
    public void setTunnelId(String tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId() 
    {
        return tunnelId;
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
    public void setLongitude(String longitude) 
    {
        this.longitude = longitude;
    }

    public String getLongitude() 
    {
        return longitude;
    }
    public void setLatitude(String latitude) 
    {
        this.latitude = latitude;
    }

    public String getLatitude() 
    {
        return latitude;
    }
    public void setSpeed(String speed) 
    {
        this.speed = speed;
    }

    public String getSpeed() 
    {
        return speed;
    }
    public void setLaneNum(String laneNum) 
    {
        this.laneNum = laneNum;
    }

    public String getLaneNum() 
    {
        return laneNum;
    }
    public void setCourseAngle(String courseAngle) 
    {
        this.courseAngle = courseAngle;
    }

    public String getCourseAngle() 
    {
        return courseAngle;
    }
    public void setVehicleLicense(String vehicleLicense) 
    {
        this.vehicleLicense = vehicleLicense;
    }

    public String getVehicleLicense() 
    {
        return vehicleLicense;
    }
    public void setLicenseColor(String licenseColor) 
    {
        this.licenseColor = licenseColor;
    }

    public String getLicenseColor() 
    {
        return licenseColor;
    }
    public void setStakeNum(String stakeNum) 
    {
        this.stakeNum = stakeNum;
    }

    public String getStakeNum() 
    {
        return stakeNum;
    }
    public void setDetectTime(Date detectTime) 
    {
        this.detectTime = detectTime;
    }

    public Date getDetectTime() 
    {
        return detectTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("vehicleId", getVehicleId())
            .append("tunnelId", getTunnelId())
            .append("vehicleType", getVehicleType())
            .append("vehicleColor", getVehicleColor())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("speed", getSpeed())
            .append("laneNum", getLaneNum())
            .append("courseAngle", getCourseAngle())
            .append("vehicleLicense", getVehicleLicense())
            .append("licenseColor", getLicenseColor())
            .append("stakeNum", getStakeNum())
            .append("detectTime", getDetectTime())
            .toString();
    }
}
