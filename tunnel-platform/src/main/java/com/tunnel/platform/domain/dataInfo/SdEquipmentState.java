package com.tunnel.platform.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 设备类型状态关系对象 sd_equipment_state
 * 
 * @author gongfanfei
 * @param <SdEquipmentStateIconFile>
 * @param <SdEquipmentStateIconFile>
 * @date 2020-08-28
 */
@ApiModel("设备类型状态关系对象类")
public class SdEquipmentState<SdEquipmentStateIconFile> extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @ApiModelProperty("ID")
    private Long id;

    /** 设备类型ID */
    @Excel(name = "设备类型ID")
    @ApiModelProperty("设备类型ID")
    private Long stateTypeId;


    @ApiModelProperty("状态类型")
    private String stateType;
    
	/** equipmentType对象 */
    @Excels({
        @Excel(name = "equipmentType", targetAttr = "typeName"),
    })
    @ApiModelProperty("equipmentType对象")
    private SdEquipmentType typeName;

    /** 设备状态code */
    @ApiModelProperty("设备状态code")
    @Excel(name = "设备状态code")
    private String deviceState;

    /** 状态名称 */
    @ApiModelProperty("状态名称")
    @Excel(name = "状态名称")
    private String stateName;

    /**
     * 状态名称
     */
    private String stateNames;

    /** 是否作为控制选项 */
    @ApiModelProperty("是否作为控制选项")
    @Excel(name = "是否作为控制选项")
    private Integer isControl;
    
    /**状态默认图标ID */
    @ApiModelProperty("状态默认图标ID")
    @Excel(name = "状态默认图标ID")
    private String iconFileId;

    @ApiModelProperty("排序")
    private Integer sort;
    
    private List<SdEquipmentStateIconFile> iFileList;

    public void setId(Long id) 
    {
        this.id = id;
    }

	public Long getId() 
    {
        return id;
    }
	
	public Long getStateTypeId() {
		return stateTypeId;
	}

	public void setStateTypeId(Long stateTypeId) {
		this.stateTypeId = stateTypeId;
	}

	public void setDeviceState(String deviceState) 
    {
        this.deviceState = deviceState;
    }

    public String getDeviceState() 
    {
        return deviceState;
    }
    public void setStateName(String stateName) 
    {
        this.stateName = stateName;
    }

    public String getStateName() 
    {
        return stateName;
    }

    public String getStateNames() {
        return stateNames;
    }

    public void setStateNames(String stateNames) {
        this.stateNames = stateNames;
    }

    public Integer  getIsControl() {
		return isControl;
	}

	public void setIsControl(Integer  isControl) {
		this.isControl = isControl;
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
    public void setIconFileId(String iconFileId) 
    {
        this.iconFileId = iconFileId;
    }

    public String getIconFileId() 
    {
        return iconFileId;
    }
    public List<SdEquipmentStateIconFile> getiFileList() {
  		return iFileList;
  	}

  	public void setiFileList(List<SdEquipmentStateIconFile> iFileList) {
  		this.iFileList = iFileList;
  	}

    public String getStateType() {
        return stateType;
    }

    public void setStateType(String stateType) {
        this.stateType = stateType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("stateTypeId", getStateTypeId())
            .append("deviceState", getDeviceState())
            .append("stateName", getStateName())
            .append("stateNames", getStateNames())
            .append("isControl", getIsControl())
            .append("typeName", getTypeName())
            .append("iconFileId", getIconFileId())
            .append("iFileList", getiFileList())
            .toString();
    }
}