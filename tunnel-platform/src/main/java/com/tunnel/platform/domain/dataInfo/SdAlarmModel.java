package com.tunnel.platform.domain.dataInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 
 * @author gongfanfei
 * @date 2021-5-10
 */
public class SdAlarmModel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    
    /** 设备ID */
    private String eqId;
    
    /** 设备名称 */
    private String eqName;
    
    /** 参数值 **/
    private String sensorValue;
    
    /** 设备类型ID */
    private String eqTypeId;
    
    /** 设备类型名称 */
    private String eqTypeName;
    
    /** 隧道ID */
    private String tunnelId;
    
    /** 隧道名称*/
    private String tunnelName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date happentTime;
    
    public String getEqId() {
		return eqId;
	}


	public void setEqId(String eqId) {
		this.eqId = eqId;
	}


	public String getEqName() {
		return eqName;
	}


	public void setEqName(String eqName) {
		this.eqName = eqName;
	}


	public String getSensorValue() {
		return sensorValue;
	}


	public void setSensorValue(String sensorValue) {
		this.sensorValue = sensorValue;
	}


	public String getEqTypeId() {
		return eqTypeId;
	}


	public void setEqTypeId(String eqTypeId) {
		this.eqTypeId = eqTypeId;
	}


	public String getEqTypeName() {
		return eqTypeName;
	}


	public void setEqTypeName(String eqTypeName) {
		this.eqTypeName = eqTypeName;
	}


	public String getTunnelId() {
		return tunnelId;
	}


	public void setTunnelId(String tunnelId) {
		this.tunnelId = tunnelId;
	}


	public String getTunnelName() {
		return tunnelName;
	}


	public void setTunnelName(String tunnelName) {
		this.tunnelName = tunnelName;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getHappentTime() {
		return happentTime;
	}


	public void setHappentTime(Date happentTime) {
		this.happentTime = happentTime;
	}


	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("eqId", getEqId())
            .append("eqName", getEqName())
            .append("sensorValue", getSensorValue())
            .append("eqTypeId", getEqTypeId())
            .append("eqTypeName",getEqTypeName())
            .append("tunnelId",getTunnelId())
            .append("tunnelName", getTunnelName())
            .append("happentTime", getHappentTime())
            .toString();
    }
}

