package com.tunnel.business.domain.event;

import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 控制策略对象 sd_strategy
 *
 * @author gongfanfei
 * @date 2020-08-27
 */
@ApiModel("控制策略实体")
public class SdStrategy extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 策略ID */
    @ApiModelProperty("预案ID")
    private Long id;

    /** 隧道ID */
    /*@Excel(name = "隧道ID")*/
    @ApiModelProperty("隧道ID")
    private String tunnelId;

    /** 隧道对象 */
    @Excels({
        @Excel(name = "隧道名称", targetAttr = "tunnelName"),
    })
    @ApiModelProperty("隧道对象")
    private SdTunnels tunnels;

    /** 策略名称 */
    @Excel(name = "策略名称")
    @ApiModelProperty("策略名称")
    private String strategyName;

    @Excel(name = "方向")
    private String fx;

    @Excel(name = "策略类型")
    private String lx;

    /** 策略类型 */
   /* @Excel(name = "策略类型")*/
    @ApiModelProperty("策略类型")
    private String strategyType;

    /** 策略信息 */
    @Excel(name = "策略信息")
    @ApiModelProperty("策略信息策略信息")
    private String strategyInfo;

    @Excel(name = "状态", readConverterExp = "0=启用,1=弃用")
    @ApiModelProperty("预案状态")
    private String strategyState;

    @ApiModelProperty("列表")
    private List<String> slist;




    private String ids;



    /** 预警信息ID */
    @ApiModelProperty("预警信息ID")
    private Long warningId;

    /** 时间表 */
    /*@Excel(name = "时间表")*/
    @ApiModelProperty("时间表")
    private String schedulerTime;

    /** 定时任务id */
    @ApiModelProperty("定时任务id")
    private String jobRelationId;

    @ApiModelProperty("分时控制开启时间")
    private String timerOpen;

    @ApiModelProperty("分时控制关闭时间")
    private String timerClose;

    @ApiModelProperty("处置名称")
    private String disposalName;

    @ApiModelProperty("策略关联id")
    private Long strategyRlId;

    @ApiModelProperty("定时策略-执行日期-不填则每日执行")
    private String execDate;

    @ApiModelProperty("定时策略-执行时间")
    private String execTime;

    public String getExecDate() {
        return execDate;
    }

    public void setExecDate(String execDate) {
        this.execDate = execDate;
    }

    public String getExecTime() {
        return execTime;
    }

    public void setExecTime(String execTime) {
        this.execTime = execTime;
    }

    public String getIds() {
        return this.ids;
    }

    public void setIds( String ids) {
        this.ids = ids;
    }

    public String getFx() {
        return this.fx;
    }

    public void setFx(final String fx) {
        this.fx = fx;
    }

    public String getLx() {
        return this.lx;
    }

    public void setLx(final String lx) {
        this.lx = lx;
    }

    public Long getStrategyRlId() {
        return strategyRlId;
    }

    public void setStrategyRlId(Long strategyRlId) {
        this.strategyRlId = strategyRlId;
    }

    public String getDisposalName() {
        return disposalName;
    }

    public void setDisposalName(String disposalName) {
        this.disposalName = disposalName;
    }

    public String getTimerOpen() {
        return timerOpen;
    }

    public void setTimerOpen(String timerOpen) {
        this.timerOpen = timerOpen;
    }

    public String getTimerClose() {
        return timerClose;
    }

    public void setTimerClose(String timerClose) {
        this.timerClose = timerClose;
    }


    /*@Excel(name = "方向")*/
    @ApiModelProperty("方向")
    private String direction;

    @ApiModelProperty("策略组")
    private String strategyGroup;

    @ApiModelProperty("事件类型")
    private String eventType;

    @ApiModelProperty("手动自动")
    private String isAutomatic;

    public String getIsAutomatic() {
        return isAutomatic;
    }

    public void setIsAutomatic(String isAutomatic) {
        this.isAutomatic = isAutomatic;
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

    public List<String> getSlist() {
		return slist;
	}

	public void setSlist(List<String> slist) {
		this.slist = slist;
	}

	public String getJobRelationId() {
		return jobRelationId;
	}

	public void setJobRelationId(String jobRelationId) {
		this.jobRelationId = jobRelationId;
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setStrategyState(String strategyState) {
        this.strategyState = strategyState;
    }

    public String getStrategyState() {
        return strategyState;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tunnelId", getTunnelId())
            .append("strategyName", getStrategyName())
            .append("strategyType", getStrategyType())
            .append("strategyInfo", getStrategyInfo())
            .append("slist", getSlist())
            .append("warningId", getWarningId())
            .append("schedulerTime", getSchedulerTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("tunnels", getTunnels())
            .append("jobRelationId", getJobRelationId())
            .toString();
    }
}
