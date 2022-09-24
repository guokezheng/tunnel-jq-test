package com.tunnel.business.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 设备指令对象 sd_device_cmd
 *
 * @author zhangweitian
 * @date 2020-09-04
 */
public class SdDeviceCmd extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 指令ID */
    private Long codeId;

    /** 设备ID */
    @Excel(name = "设备ID")
    private String codeDeviceId;

    /** 设备对象 */
    @Excels({
        @Excel(name = "eq", targetAttr = "eqName"),
    })
    private SdDevices eqName;

    /** plc对象 */
    @Excels({
            @Excel(name = "plc", targetAttr = "plcName"),
    })
    private SdDevices plcName;

	/** plc ID */
    @Excel(name = "plc ID")
    private String codePlcId;

    /** 指令 */
    @Excel(name = "指令")
    private String command;

    /** 设备类型ID */
    @Excel(name = "设备类型ID")
    private Long deviceTypeId;

    /** equipmentType对象 */
    @Excels({
        @Excel(name = "equipmentType", targetAttr = "typeName"),
    })
    private SdEquipmentType typeName;

    /** 设备状态 */
    @Excel(name = "设备状态")
    private String codeDeviceState;

    /** SdEquipmentState对象 */
    @Excels({
        @Excel(name = "SdEquipmentState", targetAttr = "stateName"),
    })
    private SdEquipmentState stateName;

	public void setCodeId(Long codeId)
    {
        this.codeId = codeId;
    }

    public Long getCodeId()
    {
        return codeId;
    }
    public void setCodeDeviceId(String codeDeviceId)
    {
        this.codeDeviceId = codeDeviceId;
    }

    public String getCodeDeviceId()
    {
        return codeDeviceId;
    }
    public void setCodePlcId(String codePlcId)
    {
        this.codePlcId = codePlcId;
    }

    public String getCodePlcId()
    {
        return codePlcId;
    }
    public void setCommand(String command)
    {
        this.command = command;
    }

    public String getCommand()
    {
        return command;
    }
    public void setDeviceTypeId(Long deviceTypeId)
    {
        this.deviceTypeId = deviceTypeId;
    }

    public Long getDeviceTypeId()
    {
        return deviceTypeId;
    }
    public void setCodeDeviceState(String codeDeviceState)
    {
        this.codeDeviceState = codeDeviceState;
    }

    public String getCodeDeviceState()
    {
        return codeDeviceState;
    }

    public SdDevices getPlcName() {
        return plcName;
    }

    public void setPlcName(SdDevices plcName) {
        this.plcName = plcName;
    }

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

	public SdDevices getEqName() {
		if (eqName == null)
        {
			eqName = new SdDevices();
        }
		return eqName;
	}

	public void setEqName(SdDevices eqName) {
		this.eqName = eqName;
	}

	public SdEquipmentState getStateName() {
		if (stateName == null)
        {
			stateName = new SdEquipmentState();
        }
		return stateName;
	}

	public void setStateName(SdEquipmentState stateName) {
		this.stateName = stateName;
	}

    @Override
    public String toString() {
        return "SdDeviceCmd{" +
                "codeId=" + codeId +
                ", codeDeviceId='" + codeDeviceId + '\'' +
                ", eqName=" + eqName +
                ", plcName=" + plcName +
                ", codePlcId='" + codePlcId + '\'' +
                ", command='" + command + '\'' +
                ", deviceTypeId=" + deviceTypeId +
                ", typeName=" + typeName +
                ", codeDeviceState='" + codeDeviceState + '\'' +
                ", stateName=" + stateName +
                '}';
    }
}
