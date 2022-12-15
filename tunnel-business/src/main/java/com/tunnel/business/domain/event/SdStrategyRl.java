package com.tunnel.business.domain.event;

import com.tunnel.business.domain.dataInfo.SdEquipmentState;
import com.tunnel.business.domain.dataInfo.SdEquipmentType;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Date;
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
    private Long strategyId;

    /** 设备类型ID */
    @Excel(name = "设备类型ID")
    private String eqTypeId;

    /** 控制时间 */
    private String controlTime;

    //有效时间
    private String effectiveTime;
    //关闭状态
    private String endState;
    //设备检索规则
    private String retrievalRule;
    //预案ID
    private String planId;

    public String getRetrievalRule() {
        return retrievalRule;
    }

    public void setRetrievalRule(String retrievalRule) {
        this.retrievalRule = retrievalRule;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getEndState() {
        return endState;
    }

    public void setEndState(String endState) {
        this.endState = endState;
    }

    public String getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

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

    public void setStrategyId(Long strategyId)
    {
        this.strategyId = strategyId;
    }

    public Long getStrategyId()
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

    public String getControlTime() {
        return controlTime;
    }

    public void setControlTime(String controlTime) {
        this.controlTime = controlTime;
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