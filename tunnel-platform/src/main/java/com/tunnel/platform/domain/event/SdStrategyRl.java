package com.tunnel.platform.domain.event;

import com.tunnel.platform.domain.dataInfo.SdEquipmentState;
import com.tunnel.platform.domain.dataInfo.SdEquipmentType;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 策略关联设备信息对象 sd_strategy_rl
 * 
 * @author gongfanfei
 * @date 2020-08-31
 */
public class SdStrategyRl extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 策略ID */
    @Excel(name = "策略关联ID")
    private String strategyId;

    /** 设备类型ID */
    @Excel(name = "设备类型ID")
    private String eqTypeId;

    private List<SdEquipmentState> eqStateList;
    
    
    /** 设备类型对象 */
    @Excels({
        @Excel(name = "设备类型", targetAttr = "eqType"),
    })
    private SdEquipmentType eqType;
    
    
    /** 相关设备 */
    @Excel(name = "相关设备")
    private String equipments;

    /** 设备状态 */
    @Excel(name = "设备状态")
    private String state;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setStrategyId(String strategyId) 
    {
        this.strategyId = strategyId;
    }

    public String getStrategyId() 
    {
        return strategyId;
    }
    public void setEqTypeId(String eqTypeId) 
    {
        this.eqTypeId = eqTypeId;
    }

    public String getEqTypeId() 
    {
        return eqTypeId;
    }
    public void setEquipments(String equipments) 
    {
        this.equipments = equipments;
    }

    public String getEquipments() 
    {
        return equipments;
    }
    public void setState(String state) 
    {
        this.state = state;
    }

    public String getState() 
    {
        return state;
    }

    public SdEquipmentType getEqType() {
    	if (eqType == null)
        {
    		eqType = new SdEquipmentType();
        }
		return eqType;
	}

	public void setEqType(SdEquipmentType eqType) {
		this.eqType = eqType;
	}

	public List<SdEquipmentState> getEqStateList() {
		if (eqStateList == null)
		{
			eqStateList = new ArrayList<SdEquipmentState>();
		}
		return eqStateList;
	}
	
	public void setEqStateList(List<SdEquipmentState> equipmentState) {
		this.eqStateList = equipmentState;
	}

    
    
	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("strategyId", getStrategyId())
            .append("eqTypeId", getEqTypeId())
            .append("equipments", getEquipments())
            .append("state", getState())
            .append("eqType", getEqType())
            .append("eqStateObj", getEqStateList())
            .toString();
    }
}