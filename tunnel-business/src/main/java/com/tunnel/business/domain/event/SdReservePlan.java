package com.tunnel.business.domain.event;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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

    /** 隧道Id */
    @ApiModelProperty("隧道Id")
    private String tunnelId;

    /*@Excel(name = "分区ID")*/
    @ApiModelProperty("分区ID")
    private Long subareaId;

    /** 预案类型ID */
    /*@Excel(name = "预案类型ID")*/
    @ApiModelProperty("预案类型ID")
    private Long planTypeId;

    /*@Excel(name = "预案类别")*/
    @ApiModelProperty("预案类型")
    private String category;

    @Excels({
            @Excel(name = "隧道名称", targetAttr = "tunnelName"),
    })
    @ApiModelProperty("隧道对象")
    private SdTunnels sdTunnels;

    @Excel(name = "方向")
    private String fx;

    /** 预案类型对象 */
    @Excels({
        @Excel(name = "事件类型", targetAttr = "eventType"),
    })
    @ApiModelProperty("预案类型对象")
    private SdEventType eventType;

    @Excel(name = "事件等级")
    private String dj;

    @ApiModelProperty("预案文件")
    private List<SdReservePlanFile> pFileList;

    /** 策略对象 */
   /* @Excels({
        @Excel(name = "控制策略", targetAttr = "strategy"),
    })*/
    @ApiModelProperty("策略对象")
    private SdStrategy strategy;

    /** 预案名称 */
    @Excel(name = "预案名称")
    @ApiModelProperty("预案名称")
    private String planName;

    /** 预案描述 */
    @Excel(name = "预案描述")
    @ApiModelProperty("预案描述")
    private String planDescription;

    /** 预案文件ID */
    /*@Excel(name = "预案文件ID")*/
    @ApiModelProperty("预案文件ID")
    private String planFileId;

    /** 策略ID */
   /* @Excel(name = "策略IDs")*/
    @ApiModelProperty("策略ID")
    private String strategyId;

    @ApiModelProperty("策略名")
    private List<String> strategyNames;

    @ApiModelProperty("隧道分区对象")
    private SdTunnelSubarea sdTunnelSubarea;






    @ApiModelProperty("管控方向")
    private String controlDirection;

    @ApiModelProperty("隧道方向")
    private String direction;

    @ApiModelProperty("事件id")
    private String eventId;

    @ApiModelProperty("环境名称")
    private String processName;

    @ApiModelProperty("环节Id")
    private Long processId;

    @ApiModelProperty("事件等级")
    private String eventGrade;

    private String ids;

    public String getDj() {
        return this.dj;
    }

    public void setDj(final String dj) {
        this.dj = dj;
    }

    /**
     * 删除的id
     */
    private Long[] removeIds;

    public String getFx() {
        return this.fx;
    }

    public void setFx(final String fx) {
        this.fx = fx;
    }

    public String getIds() {
        return this.ids;
    }

    public void setIds(final String ids) {
        this.ids = ids;
    }

    public Long[] getRemoveIds() {
        return removeIds;
    }

    public void setRemoveIds(Long[] removeIds) {
        this.removeIds = removeIds;
    }

    public String getEventGrade() {
        return eventGrade;
    }

    public void setEventGrade(String eventGrade) {
        this.eventGrade = eventGrade;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getControlDirection() {
        return controlDirection;
    }

    public void setControlDirection(String controlDirection) {
        this.controlDirection = controlDirection;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(String tunnelId) {
        this.tunnelId = tunnelId;
    }

    public SdTunnels getSdTunnels() {
        return sdTunnels;
    }

    public void setSdTunnels(SdTunnels sdTunnels) {
        this.sdTunnels = sdTunnels;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public Long getSubareaId() {
        return subareaId;
    }

    public void setSubareaId(Long subareaId) {
        this.subareaId = subareaId;
    }

    public SdTunnelSubarea getSdTunnelSubarea() {
        return sdTunnelSubarea;
    }

    public void setSdTunnelSubarea(SdTunnelSubarea sdTunnelSubarea) {
        this.sdTunnelSubarea = sdTunnelSubarea;
    }

    public void setPlanTypeId(Long planTypeId)
    {
        this.planTypeId = planTypeId;
    }

    public Long getPlanTypeId()
    {
        return planTypeId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

	public List<String> getStrategyNames() {
		return strategyNames;
	}

	public void setStrategyNames(List<String> strategyNames) {
		this.strategyNames = strategyNames;
	}

    @Override
    public String toString() {
        return "SdReservePlan{" +
                "id=" + id +
                ", tunnelId='" + tunnelId + '\'' +
                ", subareaId=" + subareaId +
                ", planTypeId=" + planTypeId +
                ", category='" + category + '\'' +
                ", eventType=" + eventType +
                ", pFileList=" + pFileList +
                ", strategy=" + strategy +
                ", planDescription='" + planDescription + '\'' +
                ", planName='" + planName + '\'' +
                ", planFileId='" + planFileId + '\'' +
                ", strategyId='" + strategyId + '\'' +
                ", strategyNames=" + strategyNames +
                ", sdTunnelSubarea=" + sdTunnelSubarea +
                ", sdTunnels=" + sdTunnels +
                '}';
    }
}
