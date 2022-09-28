package com.tunnel.business.domain.dataInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 传感器采集数据信息对象 sd_sensor_message
 * 
 * @author yanghousheng
 * @date 2020-12-25
 */
public class SdSensorMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 设备id */
    @Excel(name = "设备id")
    private String eqId;
    @Excels({
        @Excel(name = "sdDevice", targetAttr = "eqName"),
    })
    private SdDevices sdDevice;
    /** equipmentType对象 */
    @Excels({
        @Excel(name = "equipmentType", targetAttr = "typeName"),
    })
    private SdEquipmentType typeName;

    /** 设备类型 */
    @Excel(name = "设备类型")
    private String eqType;
    /** 所属隧道 id */
    @Excel(name = "所属隧道 id")
    private String eqTunnelId;
    /**
     * 隧道名称
     */
    @Excels({@Excel(name = "tunnel",targetAttr = "tunnelName")})
    private SdTunnels sdTunnel;

    public SdDevices getSdDevice() {
    	if(sdDevice == null){
    		sdDevice = new SdDevices();
    	}
		return sdDevice;
	}

	public void setSdDevice(SdDevices sdDevice) {
		this.sdDevice = sdDevice;
	}

	public SdEquipmentType getTypeName() {
		if(typeName == null){
			typeName = new SdEquipmentType();
		}
		return typeName;
	}

	public void setTypeName(SdEquipmentType typeName) {
		this.typeName = typeName;
	}

	public SdTunnels getSdTunnel() {
		if(sdTunnel == null){
			sdTunnel =new SdTunnels();
		}
		return sdTunnel;
	}

	public void setSdTunnel(SdTunnels sdTunnel) {
		this.sdTunnel = sdTunnel;
	}

	public String getEqTunnelId() {
		return eqTunnelId;
	}

	public void setEqTunnelId(String eqTunnelId) {
		this.eqTunnelId = eqTunnelId;
	}

	/** 现场数据值 */
    @Excel(name = "现场数据值")
    private String sensorValue;

    /** 采集数据时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "采集数据时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date gettime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setEqId(String eqId) 
    {
        this.eqId = eqId;
    }

    public String getEqId() 
    {
        return eqId;
    }
    public void setEqType(String eqType) 
    {
        this.eqType = eqType;
    }

    public String getEqType() 
    {
        return eqType;
    }
    public void setSensorValue(String sensorValue) 
    {
        this.sensorValue = sensorValue;
    }

    public String getSensorValue() 
    {
        return sensorValue;
    }
    public void setGettime(Date gettime) 
    {
        this.gettime = gettime;
    }

    public Date getGettime() 
    {
        return gettime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("eqId", getEqId())
            .append("sdDevice", getSdDevice())
            .append("typeName", getTypeName())
            .append("eqType", getEqType())
            .append("eqTunnelId",getEqTunnelId())
            .append("sdTunnel",getSdTunnel())
            .append("sensorValue", getSensorValue())
            .append("gettime", getGettime())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}

