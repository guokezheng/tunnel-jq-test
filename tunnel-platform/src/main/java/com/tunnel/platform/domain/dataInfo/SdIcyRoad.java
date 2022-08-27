package com.tunnel.platform.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 道路结冰记录对象 sd_icy_road
 * 
 * @author liubaokui
 * @date 2021-03-26
 */
public class SdIcyRoad extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Excel(name = "ID")
    private Long id;

    /** 设备ID */
    @Excel(name = "设备ID")
    private String deviceId;
    
    /** 隧道名称 */
    private String tunnelName;
    
    /** 设备名称 */
    private String eqName;

    /** 设备状态 */
    @Excel(name = "设备状态")
    private String status;

    /** 表面温度 */
    @Excel(name = "表面温度")
    private String surfaceTemperature;

    /** 结冰温度 */
    @Excel(name = "结冰温度")
    private String freezeTemperature;

    /** 冰厚度 */
    @Excel(name = "冰厚度")
    private String iceThickness;

    /** 盐度值 */
    @Excel(name = "盐度值")
    private String salinityValue;

    /** 湿滑系数 */
    @Excel(name = "湿滑系数")
    private String wetslidingCoefficient;

    /** 路面状况 */
    @Excel(name = "路面状况")
    private String roadCondition;
    
    /** 水厚度 */
    @Excel(name = "水厚度")
    private String roadWater;
    
    public String getTunnelName() {
		return tunnelName;
	}

	public void setTunnelName(String tunnelName) {
		this.tunnelName = tunnelName;
	}

	public String getEqName() {
		return eqName;
	}

	public void setEqName(String eqName) {
		this.eqName = eqName;
	}

	public String getRoadWater() {
		return roadWater;
	}

	public void setRoadWater(String roadWater) {
		this.roadWater = roadWater;
	}

	public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDeviceId(String deviceId) 
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId() 
    {
        return deviceId;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setSurfaceTemperature(String surfaceTemperature) 
    {
        this.surfaceTemperature = surfaceTemperature;
    }

    public String getSurfaceTemperature() 
    {
        return surfaceTemperature;
    }
    public void setFreezeTemperature(String freezeTemperature) 
    {
        this.freezeTemperature = freezeTemperature;
    }

    public String getFreezeTemperature() 
    {
        return freezeTemperature;
    }
    public void setIceThickness(String iceThickness) 
    {
        this.iceThickness = iceThickness;
    }

    public String getIceThickness() 
    {
        return iceThickness;
    }
    public void setSalinityValue(String salinityValue) 
    {
        this.salinityValue = salinityValue;
    }

    public String getSalinityValue() 
    {
        return salinityValue;
    }
    public void setWetslidingCoefficient(String wetslidingCoefficient) 
    {
        this.wetslidingCoefficient = wetslidingCoefficient;
    }

    public String getWetslidingCoefficient() 
    {
        return wetslidingCoefficient;
    }
    public void setRoadCondition(String roadCondition) 
    {
        this.roadCondition = roadCondition;
    }

    public String getRoadCondition() 
    {
        return roadCondition;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceId", getDeviceId())
            .append("status", getStatus())
            .append("surfaceTemperature", getSurfaceTemperature())
            .append("freezeTemperature", getFreezeTemperature())
            .append("iceThickness", getIceThickness())
            .append("salinityValue", getSalinityValue())
            .append("wetslidingCoefficient", getWetslidingCoefficient())
            .append("roadCondition", getRoadCondition())
            .append("createTime", getCreateTime())
            .append("tunnelName",getTunnelName())
            .append("eqName",getEqName())
            .toString();
    }
}
