package com.tunnel.platform.domain.event;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 预案信息对象 sd_reserve_plan
 *
 * @author xuebi
 * @date 2020-09-10
 */
@ApiModel("预案信息实体")
public class SdReservePlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 预案ID */
    @ApiModelProperty("预案ID")
    private Long id;

    /** 预案类型ID */
    @Excel(name = "预案类型ID")
    @ApiModelProperty("预案类型ID")
    private Long planTypeId;

    /** 预案类型对象 */
    @Excels({
        @Excel(name = "预案类型", targetAttr = "planType"),
    })
    @ApiModelProperty("预案类型对象")
    private SdEventType eventType;

    @ApiModelProperty("预案文件")
    private List<SdReservePlanFile> pFileList;

    /** 策略对象 */
    @Excels({
        @Excel(name = "控制策略", targetAttr = "strategy"),
    })
    @ApiModelProperty("策略对象")
    private SdStrategy strategy;


    /** 预案描述 */
    @Excel(name = "预案描述")
    @ApiModelProperty("预案描述")
    private String planDescription;

    /** 预案名称 */
    @Excel(name = "预案名称")
    @ApiModelProperty("预案名称")
    private String planName;

    /** 预案文件ID */
    @Excel(name = "预案文件ID")
    @ApiModelProperty("预案文件ID")
    private String planFileId;

    /** 策略ID */
    @Excel(name = "策略IDs")
    @ApiModelProperty("策略ID")
    private String strategyId;

    @ApiModelProperty("策略名")
    private String strategyNames;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setPlanTypeId(Long planTypeId)
    {
        this.planTypeId = planTypeId;
    }

    public Long getPlanTypeId()
    {
        return planTypeId;
    }



	public SdEventType getEventType() {
		if (eventType == null)
        {
			eventType = new SdEventType();
        }
		return eventType;
	}

	public void setEventType(SdEventType eventType) {
		this.eventType = eventType;
	}

	public SdStrategy getStrategy() {
		if (strategy == null)
        {
			strategy = new SdStrategy();
        }
		return strategy;
	}

	public void setStrategy(SdStrategy strategy) {
		this.strategy = strategy;
	}

	public void setPlanDescription(String planDescription)
    {
        this.planDescription = planDescription;
    }

    public String getPlanDescription()
    {
        return planDescription;
    }
    public void setPlanName(String planName)
    {
        this.planName = planName;
    }

    public String getPlanName()
    {
        return planName;
    }
    public void setPlanFileId(String planFileId)
    {
        this.planFileId = planFileId;
    }

    public String getPlanFileId()
    {
        return planFileId;
    }
    public void setStrategyId(String strategyId)
    {
        this.strategyId = strategyId;
    }

    public String getStrategyId()
    {
        return strategyId;
    }


    public List<SdReservePlanFile> getpFileList() {
		return pFileList;
	}

	public void setpFileList(List<SdReservePlanFile> pFileList) {
		this.pFileList = pFileList;
	}

	public String getStrategyNames() {
		return strategyNames;
	}

	public void setStrategyNames(String strategyNames) {
		this.strategyNames = strategyNames;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("planTypeId", getPlanTypeId())
            .append("planDescription", getPlanDescription())
            .append("planName", getPlanName())
            .append("planFileId", getPlanFileId())
            .append("strategyId", getStrategyId())
            .append("strategyNames", getStrategyNames())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("eventType", getEventType())
            .append("strategy", getStrategy())
            .append("pFileList", getpFileList())
            .toString();
    }
}
