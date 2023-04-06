package com.tunnel.business.domain.logRecord;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tunnel.business.domain.dataInfo.SdEquipmentState;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdEquipmentType;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 操作日志对象 sd_operation_log
 * 
 * @author yanghousheng
 * @date 2020-09-03
 */
public class SdOperationLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 设备类型 */
    /*@Excel(name = "设备类型")*/
    private Long eqTypeId;
    /** tunnel对象 */
    @Excels({
            @Excel(name = "隧道名称", targetAttr = "tunnelName"),
    })
    private SdTunnels tunnelName;
    /** equipmentType对象 */
    @Excels({
        @Excel(name = "设备类型", targetAttr = "typeName"),
    })
    private SdEquipmentType typeName;

    /** tunnel对象 */
    @Excels({
            @Excel(name = "设备名称", targetAttr = "eqName"),
    })
    private SdDevices eqName;

    /** 操作状态 */
    private String operationState;

    /** tunnel对象 */
    @Excels({
        @Excel(name = "操作状态", targetAttr = "stateName"),
    })
    private SdEquipmentState stateName;

	/** 隧道id */
   /* @Excel(name = "隧道id")*/
    private String tunnelId;

    /** 用户名称 */
   /* @Excel(name = "用户名称")*/
    private String userName;

    /** 所属设备 */
   /* @Excel(name = "所属设备")*/
    private String eqId;
    /** 设备名称 */
    /*@Excel(name = "设备名称")
    private String eqName;*/

    /** 桩号 */
    @Excel(name = "桩号")
    private String pile;
    /** 方向 */
    @Excel(name = "方向",dictType = "sd_direction")
    private String direction;
    
    private String beginTime;
    
    private String endTime;
    /** 控制方式   3：手动 1：时间控制 2：光强控制 */
    @Excel(name = "控制方式",dictType = "sd_control_type")
    private String controlType;

    /** 操作是否成功 0 成功；1失败 */
    @Excel(name = "操作结果")
    private String state;

    /** 操作地址 */
    @Excel(name = "操作地址")
    private String operIp;

    //事件ID
    private String eventId;

    private String ids;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getIds() {
        return this.ids;
    }

    public void setIds( String ids) {
        this.ids = ids;
    }

    public String getPile() {
        return this.pile;
    }

    public void setPile(String pile) {
        this.pile = pile;
    }

    public String getDirection() {
        return this.direction;
    }

    public void setDirection( String direction) {
        this.direction = direction;
    }

    @Override
    public Date getCreateTime() {
        return this.createTime;
    }

    @Override
    public void setCreateTime( Date createTime) {
        this.createTime = createTime;
    }

    public String getOperIp() {
        return operIp;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setOperIp(String operIp) {
        this.operIp = operIp;
    }

    public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	/** 识别码 */
    /*@Excel(name = "识别码")*/
    private String code;

    /** 操作前状态 */
   /* @Excel(name = "操作前状态")*/
    private String beforeState;

    /** 描述 */
   /* @Excel(name = "描述")*/
    private String description;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public Long getEqTypeId() {
		return eqTypeId;
	}

	public void setEqTypeId(Long eqTypeId) {
		this.eqTypeId = eqTypeId;
	}
	/**
	 * 操作状态
	 * @return
	 */
	public SdEquipmentState getStateName() {
		return stateName;
	}

	public void setStateName(SdEquipmentState stateName) {
		this.stateName = stateName;
	}
	public SdEquipmentType getTypeName() {
		return typeName;
	}

	public void setTypeName(SdEquipmentType typeName) {
		this.typeName = typeName;
	}

	/**
     * 隧道对象
     * @return
     */
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

    public void setTunnelId(String tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId() 
    {
        return tunnelId;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public String getEqId() {
		return eqId;
	}

	public void setEqId(String eqId) {
		this.eqId = eqId;
	}
    public void setCode(String code) 
    {
        this.code = code;
    }

	/*public String getEqName() {
		return eqName;
	}

	public void setEqName(String eqName) {
		this.eqName = eqName;
	}*/

    public String getCode() 
    {
        return code;
    }
    public void setBeforeState(String beforeState) 
    {
        this.beforeState = beforeState;
    }

    public String getBeforeState() 
    {
        return beforeState;
    }
    public void setOperationState(String operationState) 
    {
        this.operationState = operationState;
    }

    public String getOperationState() 
    {
        return operationState;
    }
    public void setControlType(String controlType) 
    {
        this.controlType = controlType;
    }

    public String getControlType() 
    {
        return controlType;
    }
    public void setState(String state) 
    {
        this.state = state;
    }

    public String getState() 
    {
        return state;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("eqTypeId", getEqTypeId())
            .append("stateName", getStateName())
            .append("typeName", getTypeName())
            .append("tunnelId", getTunnelId())
            .append("tunnelName", getTunnelName())
            .append("userName", getUserName())
            .append("eqId", getEqId())
            .append("eqName", getEqName())
            /*.append("eqName", getEqName())*/
            .append("code", getCode())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("beforeState", getBeforeState())
            .append("operationState", getOperationState())
            .append("controlType", getControlType())
            .append("state", getState())
            .append("description", getDescription())
            .append("beginTime",getBeginTime())
            .append("endTime",getEndTime())
            .append("operIp",getOperIp())
            .toString();
    }

}