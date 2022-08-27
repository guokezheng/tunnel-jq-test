package com.tunnel.platform.domain.event;

import com.tunnel.platform.domain.dataInfo.SdTunnels;
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
    @ApiModelProperty("策略信息策略信息")
    private String strategyInfo;

    @ApiModelProperty("列表")
    private List<String> slist;


    /** 预警信息ID */
    @ApiModelProperty("预警信息ID")
    private Long warningId;

    /** 时间表 */
    @Excel(name = "时间表")
    @ApiModelProperty("时间表")
    private String schedulerTime;

    /** 定时任务id */
    @ApiModelProperty("定时任务id")
    private String jobRelationId;

    /** 策略关联ID */
    @ApiModelProperty("策略关联ID")
    private String rlId;

    @Excel(name = "方向")
    @ApiModelProperty("方向")
    private String direction;
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

    public String getRlId() {
		return rlId;
	}

	public void setRlId(String rlId) {
		this.rlId = rlId;
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
            .append("rlId", getRlId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("tunnels", getTunnels())
            .append("jobRelationId", getJobRelationId())
            .toString();
    }
}
