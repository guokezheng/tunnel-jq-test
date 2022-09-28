package com.tunnel.business.domain.digitalmodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 重点车辆对象 sd_special_vehicle
 * 
 * @author ruoyi
 * @date 2022-09-04
 */
public class SdSpecialVehicles extends BaseEntity
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

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String vehicleLicense;

    /** 车牌颜色 */
    @Excel(name = "车牌颜色")
    private String licenseColor;

    /** 开始时间 */
  //  @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private String startTime;

    /** 结束时间 */
  //  @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private String endTime;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("vehicleId", getVehicleId())
            .append("tunnelId", getTunnelId())
            .append("vehicleType", getVehicleType())
            .append("vehicleColor", getVehicleColor())
            .append("vehicleLicense", getVehicleLicense())
            .append("licenseColor", getLicenseColor())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .toString();
    }
}
