package com.tunnel.business.domain.dataInfo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 设备维修记录对象 sd_equipment_repair_record
 *
 * @author zhangweitian
 * @date 2020-12-29
 */
public class SdEquipmentRepairRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备维修记录ID */
    private Long repairId;

    /** tunnel对象 */
    @Excels({
            @Excel(name = "所属隧道", targetAttr = "tunnelName"),
    })
    private SdTunnels tunnelName;

    /** 所属隧道 */
    private String repairTunnelId;

    /** 维修时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "维修时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date repairTime;

    /** equipmentType对象 */
    @Excels({
            @Excel(name = "设备类型", targetAttr = "typeName"),
    })
    private SdEquipmentType typeName;

    /** 设备类型 */
    private Long repairTypeId;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String repairNumber;

    /** 巡检内容 */
    @Excel(name = "巡检内容")
    private String repairContentState;

    /** 维修内容 */
    @Excel(name = "维修内容")
    private String repairContent;

    /** 巡检结果 */
    @Excel(name = "巡检结果")
    private String repairResult;



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

    public void setRepairId(Long repairId)
    {
        this.repairId = repairId;
    }

    public Long getRepairId()
    {
        return repairId;
    }
    public void setRepairTunnelId(String repairTunnelId)
    {
        this.repairTunnelId = repairTunnelId;
    }

    public String getRepairTunnelId()
    {
        return repairTunnelId;
    }
    public void setRepairTime(Date repairTime)
    {
        this.repairTime = repairTime;
    }

    public Date getRepairTime()
    {
        return repairTime;
    }
    public void setRepairTypeId(Long repairTypeId)
    {
        this.repairTypeId = repairTypeId;
    }

    public Long getRepairTypeId()
    {
        return repairTypeId;
    }
    public void setRepairNumber(String repairNumber)
    {
        this.repairNumber = repairNumber;
    }

    public String getRepairNumber()
    {
        return repairNumber;
    }
    public void setRepairContentState(String repairContentState)
    {
        this.repairContentState = repairContentState;
    }

    public String getRepairContentState()
    {
        return repairContentState;
    }
    public void setRepairContent(String repairContent)
    {
        this.repairContent = repairContent;
    }

    public String getRepairContent()
    {
        return repairContent;
    }
    public void setRepairResult(String repairResult)
    {
        this.repairResult = repairResult;
    }

    public String getRepairResult()
    {
        return repairResult;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("repairId", getRepairId())
            .append("repairTunnelId", getRepairTunnelId())
            .append("repairTime", getRepairTime())
            .append("repairTypeId", getRepairTypeId())
            .append("repairNumber", getRepairNumber())
            .append("repairContentState", getRepairContentState())
            .append("repairContent", getRepairContent())
            .append("repairResult", getRepairResult())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("tunnelName", getTunnelName())
            .append("typeName", getTypeName())
            .toString();
    }
}
