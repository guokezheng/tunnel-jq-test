package com.tunnel.platform.domain.event;

import com.tunnel.platform.domain.dataInfo.SdTunnels;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 控制策略对象 (接收对象)
 *
 * @author gongfanfei
 * @date 2020-09-1
 */
@ApiModel("控制策略对象 (接收对象)")
public class SdStrategyModel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 策略ID */
    @ApiModelProperty("策略ID")
    private Long id;

    /* 关联ID */
    @ApiModelProperty("关联ID")
    private String rlId;


    /** 隧道ID */
    @Excel(name = "隧道ID")
    @ApiModelProperty("隧道ID")
    private String tunnelId;

    /** 隧道对象 */
    @Excels({
        @Excel(name = "隧道", targetAttr = "tunnels"),
    })
    @ApiModelProperty("隧道对象")
    private SdTunnels tunnels;

    /** 策略名称 */
    @Excel(name = "策略名称")
    @ApiModelProperty("策略名称")
    private String strategyName;

    /** 策略类型 */
    @Excel(name = "策略类型")
    @ApiModelProperty("策略类型")
    private String strategyType;

    /** 策略信息 */
    @Excel(name = "策略信息")
    @ApiModelProperty("策略信息")
    private String strategyInfo;

    /** 预警信息ID */
    @ApiModelProperty("预警信息ID")
    private Long warningId;

    /** 时间表 */
    @ApiModelProperty("时间表")
    @Excel(name = "时间表")
    private String schedulerTime;

    /** 定时任务UID */
    @ApiModelProperty("定时任务UID")
    private String jobRelationId;

    /** 设备类型ID */
    @ApiModelProperty("设备类型ID")
    private String equipmentTypeId;

    /** 设备,以#分割 */
    @ApiModelProperty("设备,以#分割")
    private String equipments;

    /** 设备状态 */
    @ApiModelProperty("设备状态")
    private String equipmentState;

    @ApiModelProperty("方向")
    private String direction;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public String getRlId() {
		return rlId;
	}

	public void setRlId(String rlId) {
		this.rlId = rlId;
	}

	public void setTunnelId(String tunnelId)
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId()
    {
        return tunnelId;
    }
    public void setStrategyName(String strategyName)
    {
        this.strategyName = strategyName;
    }

    public String getJobRelationId() {
		return jobRelationId;
	}

	public void setJobRelationId(String jobRelationId) {
		this.jobRelationId = jobRelationId;
	}

	public String getStrategyName()
    {
        return strategyName;
    }
    public void setStrategyType(String strategyType)
    {
        this.strategyType = strategyType;
    }

    public String getStrategyType()
    {
        return strategyType;
    }
    public void setStrategyInfo(String strategyInfo)
    {
        this.strategyInfo = strategyInfo;
    }

    public String getStrategyInfo()
    {
        return strategyInfo;
    }
    public void setWarningId(Long warningId)
    {
        this.warningId = warningId;
    }

    public Long getWarningId()
    {
        return warningId;
    }
    public void setSchedulerTime(String schedulerTime)
    {
        this.schedulerTime = schedulerTime;
    }

    public String getSchedulerTime()
    {
        return schedulerTime;
    }
    public void setEquipmentTypeId(String equipmentTypeId)
    {
        this.equipmentTypeId = equipmentTypeId;
    }

    public String getEquipmentTypeId()
    {
        return equipmentTypeId;
    }
    public void setEquipments(String equipments)
    {
        this.equipments = equipments;
    }

    public String getEquipments()
    {
        return equipments;
    }
    public void setEquipmentState(String equipmentState)
    {
        this.equipmentState = equipmentState;
    }

    public String getEquipmentState()
    {
        return equipmentState;
    }

    public SdTunnels getTunnels() {
		if (tunnels == null)
        {
			tunnels = new SdTunnels();
        }
		return tunnels;
	}

	public void setTunnels(SdTunnels tunnels) {
		this.tunnels = tunnels;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("rlId", getRlId())
            .append("tunnelId", getTunnelId())
            .append("strategyName", getStrategyName())
            .append("strategyType", getStrategyType())
            .append("strategyInfo", getStrategyInfo())
            .append("warningId", getWarningId())
            .append("schedulerTime", getSchedulerTime())
            .append("equipmentTypeId", getEquipmentTypeId())
            .append("equipments", getEquipments())
            .append("equipmentState", getEquipmentState())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("jobRelationId", getJobRelationId())
            .append("tunnels", getTunnels())
            .toString();
    }
}
