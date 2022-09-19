package com.tunnel.business.domain.dataInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 传感器列对象 sd_sensor
 * 
 * @author yanghousheng
 * @date 2020-11-09
 */
public class SdSensor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 传感器类型id */
    @Excel(name = "传感器类型id")
    private Long sensorTypeId;
    
    /** 传感器类型对象 */
    @Excels({
        @Excel(name = "sensorType", targetAttr = "sensorType"),
    })
    private  SdSensorType sensorType;

	/** 传感器名称 */
    @Excel(name = "传感器名称")
    private String sensorName;

    /** tunnel对象 */
    @Excels({
        @Excel(name = "tunnel", targetAttr = "tunnelName"),
    })
    private SdTunnels tunnelName;

	/** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

    /** 洞内亮度 */
    @Excel(name = "洞内亮度")
    private String brightnessInTunnel;

    /** 洞外亮度 */
    @Excel(name = "洞外亮度")
    private String brightnessOutsideTunnel;

    /** 一氧化碳浓度 */
    @Excel(name = "一氧化碳浓度")
    private String coConcentration;

    /** 可见度 */
    @Excel(name = "可见度")
    private String visibility;

    /** 风速 */
    @Excel(name = "风速")
    private Long windSpeed;

    /** 风向 */
    @Excel(name = "风向")
    private String windDirection;

    /** 水池液位 */
    @Excel(name = "水池液位")
    private String tankLevel;

    /** 真实时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "真实时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date realTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setSensorTypeId(Long sensorTypeId) 
    {
        this.sensorTypeId = sensorTypeId;
    }

    public Long getSensorTypeId() 
    {
        return sensorTypeId;
    }
    
    public SdSensorType getSensorType() {
    	if(sensorType==null){
    		sensorType = new SdSensorType();
    	}
		return sensorType;
	}

	public void setSensorType(SdSensorType sensorType) {
		this.sensorType = sensorType;
	}
    public void setSensorName(String sensorName) 
    {
        this.sensorName = sensorName;
    }

    public String getSensorName() 
    {
        return sensorName;
    }
    public void setTunnelId(String tunnelId)
    {
        this.tunnelId = tunnelId;
    }
    
    public SdTunnels getTunnelName() {
    	if (tunnelName == null)
        {
			tunnelName = new SdTunnels();
        }
		return tunnelName;
	}

	public void setTunnelName(SdTunnels tunnelName) {
		
		this.tunnelName = tunnelName;
	}

    public String getTunnelId()
    {
        return tunnelId;
    }
    public void setBrightnessInTunnel(String brightnessInTunnel) 
    {
        this.brightnessInTunnel = brightnessInTunnel;
    }

    public String getBrightnessInTunnel() 
    {
        return brightnessInTunnel;
    }
    public void setBrightnessOutsideTunnel(String brightnessOutsideTunnel) 
    {
        this.brightnessOutsideTunnel = brightnessOutsideTunnel;
    }

    public String getBrightnessOutsideTunnel() 
    {
        return brightnessOutsideTunnel;
    }
    public void setCoConcentration(String coConcentration) 
    {
        this.coConcentration = coConcentration;
    }

    public String getCoConcentration() 
    {
        return coConcentration;
    }
    public void setVisibility(String visibility) 
    {
        this.visibility = visibility;
    }

    public String getVisibility() 
    {
        return visibility;
    }
    public void setWindSpeed(Long windSpeed) 
    {
        this.windSpeed = windSpeed;
    }

    public Long getWindSpeed() 
    {
        return windSpeed;
    }
    public void setWindDirection(String windDirection) 
    {
        this.windDirection = windDirection;
    }

    public String getWindDirection() 
    {
        return windDirection;
    }
    public void setTankLevel(String tankLevel) 
    {
        this.tankLevel = tankLevel;
    }

    public String getTankLevel() 
    {
        return tankLevel;
    }
    public void setRealTime(Date realTime) 
    {
        this.realTime = realTime;
    }

    public Date getRealTime() 
    {
        return realTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sensorTypeId", getSensorTypeId())
            .append("sensorType",getSensorType())
            .append("sensorName", getSensorName())
            .append("tunnelName", getTunnelName())
            .append("tunnelId", getTunnelId())
            .append("brightnessInTunnel", getBrightnessInTunnel())
            .append("brightnessOutsideTunnel", getBrightnessOutsideTunnel())
            .append("coConcentration", getCoConcentration())
            .append("visibility", getVisibility())
            .append("windSpeed", getWindSpeed())
            .append("windDirection", getWindDirection())
            .append("tankLevel", getTankLevel())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("realTime", getRealTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}