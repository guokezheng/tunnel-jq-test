package com.tunnel.platform.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 照明设备信息对象 sd_lipower_device
 * 
 * @author wangx
 * @date 2021-03-15
 */
public class SdLipowerDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    
    /** equipmentType对象 */
    private SdEquipmentType typeName;
    
    public SdEquipmentType getTypeName() {
		if (typeName == null)
        {
			typeName = new SdEquipmentType();
        }
		return typeName;
	}
    public void setTypeName(SdEquipmentType typeName) {
		this.typeName = typeName;
	}

    /** ID */
    private String eqId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String eqName;
    
    /** 设备桩号 */
    private String stakeMark;
    
    /** 方向 */
    private String eqDirection;

    /** 所属隧道 */
    @Excel(name = "所属隧道")
    private String tunnelId;

    /** 隧道名称 */
    @Excel(name = "隧道名称")
    private String tunnelName;

    /** 开关状态 */
    @Excel(name = "开关状态")
    private String switchStatus;

    /** 设备类型 */
    @Excel(name = "支路类型")
    private String deviceType;
    
    /** 设备类型 */
    private String eqType;

    /** 调光系数 */
    @Excel(name = "调光系数")
    private String lightValue;

    /** 是否开启定时调光 */
    @Excel(name = "是否开启定时调光")
    private String lightEnable;

    public String getStakeMark() {
		return stakeMark;
	}

	public void setStakeMark(String stakeMark) {
		this.stakeMark = stakeMark;
	}

	public String getEqType() {
		return eqType;
	}

	public void setEqType(String eqType) {
		this.eqType = eqType;
	}
	
	public String getEqDirection() {
		return eqDirection;
	}

	public void setEqDirection(String eqDirection) {
		this.eqDirection = eqDirection;
	}

	public void setEqId(String eqId) 
    {
        this.eqId = eqId;
    }

    public String getEqId() 
    {
        return eqId;
    }
    public void setEqName(String eqName) 
    {
        this.eqName = eqName;
    }

    public String getEqName() 
    {
        return eqName;
    }
    public void setTunnelId(String tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId() 
    {
        return tunnelId;
    }
    public void setTunnelName(String tunnelName) 
    {
        this.tunnelName = tunnelName;
    }

    public String getTunnelName() 
    {
        return tunnelName;
    }
    public void setSwitchStatus(String switchStatus) 
    {
        this.switchStatus = switchStatus;
    }

    public String getSwitchStatus() 
    {
        return switchStatus;
    }
    public void setDeviceType(String deviceType) 
    {
        this.deviceType = deviceType;
    }

    public String getDeviceType() 
    {
        return deviceType;
    }
    public void setLightValue(String lightValue) 
    {
        this.lightValue = lightValue;
    }

    public String getLightValue() 
    {
        return lightValue;
    }
    public void setLightEnable(String lightEnable) 
    {
        this.lightEnable = lightEnable;
    }

    public String getLightEnable() 
    {
        return lightEnable;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("eqId", getEqId())
            .append("deviceName", getEqName())
            .append("tunnelId", getTunnelId())
            .append("tunnelName", getTunnelName())
            .append("switchStatus", getSwitchStatus())
            .append("deviceType", getDeviceType())
            .append("lightValue", getLightValue())
            .append("lightEnable", getLightEnable())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("eqType",getEqType())
            .append("stakeMark",getStakeMark())
            .append("eqDirection",getEqDirection())
            .append("typeName", getTypeName())
            .toString();
    }
}
