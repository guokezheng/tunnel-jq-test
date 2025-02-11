package com.tunnel.business.domain.event;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Map;

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

    /** 策略类型 策略类型 0：手动执行、事件触发 1：定时控制 2：自动触发（阈值）3：分时*/
    @Excel(name = "策略类型")
    @ApiModelProperty("策略类型")
    private String strategyType;

    @Excel(name = "策略状态")
    @ApiModelProperty("策略状态")
    private String strategyState;

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

    @ApiModelProperty("事件类型ID")
    private String eventType;

    @ApiModelProperty("策略组")
    private String strategyGroup;

    @ApiModelProperty("定时控制-执行时间")
    private String execTime;

    @ApiModelProperty("定时控制-执行日期")
    private String execDate;

    @ApiModelProperty("分时控制-开始时间")
    private String startTime;

    @ApiModelProperty("分时控制-结束时间")
    private String endTime;

    @ApiModelProperty("手动自动")
    private String isAutomatic;

    private String timingType;

    public String getExecTime() {
        return execTime;
    }

    public void setExecTime(String execTime) {
        this.execTime = execTime;
    }

    public String getExecDate() {
        return execDate;
    }

    public void setExecDate(String execDate) {
        this.execDate = execDate;
    }

    public String getIsAutomatic() {
        return isAutomatic;
    }

    public void setIsAutomatic(String isAutomatic) {
        this.isAutomatic = isAutomatic;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getStrategyGroup() {
        return strategyGroup;
    }

    public void setStrategyGroup(String strategyGroup) {
        this.strategyGroup = strategyGroup;
    }
/** 设备,以#分割 */
    /*@ApiModelProperty("设备,以#分割")
    private String equipments;*/

    /** 设备状态 */
    /*@ApiModelProperty("设备状态")
    private String equipmentState;*/

    @ApiModelProperty("方向")
    private String direction;

    @ApiModelProperty("设备")
    private List<Map> equipment;

    /** 触发器 */
    @ApiModelProperty("触发器")
    private SdTrigger triggers;

    @ApiModelProperty("手动控制")
    private List<Map> manualControl;

    @ApiModelProperty("定时控制 || 自动触发")
    private List<Map> autoControl;

    @ApiModelProperty("分时控制")
    private List<Map> timeSharingControl;

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

    /*public void setEquipments(String equipments)
    {
        this.equipments = equipments;
    }

    public String getEquipments()
    {
        return equipments;
    }*/

    /*public void setEquipmentState(String equipmentState)
    {
        this.equipmentState = equipmentState;
    }

    public String getEquipmentState()
    {
        return equipmentState;
    }*/

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

    public SdTrigger getTriggers() {
        return triggers;
    }

    public void setTriggers(SdTrigger triggers) {
        this.triggers = triggers;
    }

    public List<Map> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Map> equipment) {
        this.equipment = equipment;
    }

    public String getStrategyState() {
        return strategyState;
    }

    public void setStrategyState(String strategyState) {
        this.strategyState = strategyState;
    }

    public List<Map> getManualControl() {
        return manualControl;
    }

    public void setManualControl(List<Map> manualControl) {
        this.manualControl = manualControl;
    }

    public List<Map> getAutoControl() {
        return autoControl;
    }

    public void setAutoControl(List<Map> autoControl) {
        this.autoControl = autoControl;
    }

    public List<Map> getTimeSharingControl() {
        return timeSharingControl;
    }

    public void setTimeSharingControl(List<Map> timeSharingControl) {
        this.timeSharingControl = timeSharingControl;
    }

    public String getTimingType() {
        return timingType;
    }

    public void setTimingType(String timingType) {
        this.timingType = timingType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tunnelId", getTunnelId())
            .append("strategyName", getStrategyName())
            .append("strategyType", getStrategyType())
            .append("strategyInfo", getStrategyInfo())
            .append("warningId", getWarningId())
            .append("schedulerTime", getSchedulerTime())
            .append("equipmentTypeId", getEquipmentTypeId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("jobRelationId", getJobRelationId())
            .append("tunnels", getTunnels())
            .toString();
    }
}
