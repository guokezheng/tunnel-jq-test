package com.tunnel.business.domain.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.checkerframework.checker.units.qual.A;

import java.util.Date;

/**
 * 预警信息对象 sd_warning_info
 *
 * @author gongfanfei
 * @date 2020-08-21
 */
@ApiModel("预警信息实体")
public class SdWarningInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @ApiModelProperty("id")
    private Long id;

    /** 预警类型 */
    @Excel(name = "预警类型")
    @ApiModelProperty("预警类型")
    private String warningType;

	/** 隧道ID */
    @Excel(name = "隧道ID")
    @ApiModelProperty("隧道ID")
    private String tunnelId;

    /** 隧道名称 */
    @Excel(name = "隧道名称")
    @ApiModelProperty("隧道名称")
    private String tunnelName;

    /** 预警名称 */
    @Excel(name = "预警名称")
    @ApiModelProperty("预警名称")
    private String warningName;

    /** 预警时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "预警时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("预警时间")
    private Date warningTime;

    /** 预警内容 */
    @Excel(name = "预警内容")
    @ApiModelProperty("预警内容")
    private String inforSources;

    /** 车洞 */
    @Excel(name = "车洞")
    @ApiModelProperty("车洞")
    private String holeDirection;

    /** 处理状态  0：未处置；1：已处置 */
    @Excel(name = "处理状态")
    @ApiModelProperty("处理状态  0：未处置；1：已处置 ")
    private String processState;

	/** 预警位置/桩号 */
    @Excel(name = "预警位置/桩号")
    @ApiModelProperty("预警位置/桩号")
    private String position;

    /** 文件（图片） */
    @Excel(name = "文件（图片）")
    @ApiModelProperty("文件（图片）")
    private String picture;

    /** 文件（录像短视频） */
    @Excel(name = "文件（录像短视频）")
    @ApiModelProperty("文件（录像短视频）")
    private String vedio;

    /** 开始触发集合 */
    @Excel(name = "开始触发集合")
    @ApiModelProperty("开始触发集合")
    private String starts;

    /** 忽略信息集合 */
    @Excel(name = "忽略信息集合")
    @ApiModelProperty("忽略信息集合")
    private String ignores;

    /** 处理信息集合 */
    @Excel(name = "处理信息集合")
    @ApiModelProperty("处理信息集合")
    private String handles;

    /** 抓图ID */
    @Excel(name = "抓图ID")
    @ApiModelProperty("抓图ID")
    private String captureId;

    /** 关联摄像机 */
    @Excel(name = "关联摄像机")
    @ApiModelProperty("关联摄像机")
    private String linkedCamera;

    @ApiModelProperty("开始时间")
    private String beginTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    /** 备注 */
    @Excel(name = "备注")
    @ApiModelProperty("备注")
    private String remark;

    /** 处理人 */
    @Excel(name = "处理人")
    @ApiModelProperty("处理人")
    private String handler;

    /** 处理人名称 */
    @Excel(name = "处理人名称")
    @ApiModelProperty("处理人名称")
    private String nickName;

    /** 创建者 */
    @Excel(name = "创建者")
    @ApiModelProperty("创建者")
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /** 更新者 */
    @Excel(name = "更新者")
    @ApiModelProperty("更新者")
    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /** 预警类型ID */
    @ApiModelProperty("预警类型ID")
    private Long warningTypeId;

    /** 车牌号文件（图片） */
    @ApiModelProperty("车牌号文件（图片）")
    private String carNumberPicture;

    /** 设备ID */
    @ApiModelProperty("设备ID")
    private String eqId;

    /** 事件结束时间 */
    @ApiModelProperty("事件结束时间")
    private Date eventEndTime;

    @ApiModelProperty("年")
    private String year;

    @ApiModelProperty("月")
    private String month;

    @ApiModelProperty("日")
    private String day;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Date getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(Date eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getEqId() {
        return eqId;
    }

    public void setEqId(String eqId) {
        this.eqId = eqId;
    }

    public String getCarNumberPicture() {
        return carNumberPicture;
    }

    public void setCarNumberPicture(String carNumberPicture) {
        this.carNumberPicture = carNumberPicture;
    }

    public String getWarningType() {
		return warningType;
	}

	public void setWarningType(String warningType) {
		this.warningType = warningType;
	}

    public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public String getTunnelId() {
		return tunnelId;
	}

	public void setTunnelId(String tunnelId) {
		this.tunnelId = tunnelId;
	}

	public String getTunnelName() {
		return tunnelName;
	}

	public void setTunnelName(String tunnelName) {
		this.tunnelName = tunnelName;
	}

	public void setInforSources(String inforSources)
    {
        this.inforSources = inforSources;
    }

    public String getInforSources()
    {
        return inforSources;
    }
    public void setWarningName(String warningName)
    {
        this.warningName = warningName;
    }

    public String getWarningName()
    {
        return warningName;
    }
    public void setWarningTime(Date warningTime)
    {
        this.warningTime = warningTime;
    }

    public Date getWarningTime()
    {
        return warningTime;
    }
    public void setProcessState(String processState)
    {
        this.processState = processState;
    }

    public String getProcessState()
    {
        return processState;
    }
    public void setHandler(String handler)
    {
        this.handler = handler;
    }

    public String getHandler()
    {
        return handler;
    }

    public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getVedio() {
		return vedio;
	}

	public void setVedio(String video) {
		this.vedio = video;
	}

	public String getStarts() {
		return starts;
	}

	public void setStarts(String starts) {
		this.starts = starts;
	}

	public String getIgnores() {
		return ignores;
	}

	public void setIgnores(String ignores) {
		this.ignores = ignores;
	}

	public String getHandles() {
		return handles;
	}

	public void setHandles(String handles) {
		this.handles = handles;
	}

	public String getCaptureId() {
		return captureId;
	}

	public void setCaptureId(String captureId) {
		this.captureId = captureId;
	}

	public String getLinkedCamera() {
		return linkedCamera;
	}

	public void setLinkedCamera(String linkedCamera) {
		this.linkedCamera = linkedCamera;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

    public Long getWarningTypeId() {
		return warningTypeId;
	}

	public void setWarningTypeId(Long warningTypeId) {
		this.warningTypeId = warningTypeId;
	}

	 public String getHoleDirection() {
			return holeDirection;
	}

	public void setHoleDirection(String holeDirection) {
		this.holeDirection = holeDirection;
	}


	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("warningType", getWarningType())
            .append("tunnelId", getTunnelId())
            .append("tunnelName", getTunnelName())
            .append("inforSources", getInforSources())
            .append("warningName", getWarningName())
            .append("warningTime", getWarningTime())
            .append("processState", getProcessState())
            .append("handler", getHandler())
            .append("remark", getRemark())
            .append("warningTypeId", getWarningTypeId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("nickName", getNickName())
            .append("position", getPosition())
            .append("picture", getPicture())
            .append("vedio", getVedio())
            .append("starts", getStarts())
            .append("ignores", getIgnores())
            .append("handles", getHandles())
            .append("captureId", getCaptureId())
            .append("beginTime", getBeginTime())
            .append("endTime", getEndTime())
            .append("linkedCamera", getLinkedCamera())
            .append("holeDirection", getHoleDirection())
            .append("carNumberPicture", getCarNumberPicture())
            .append("eqId", getEqId())
            .append("eventEndTime", getEventEndTime())
            .append("year", getYear())
            .append("month", getMonth())
            .append("day", getDay())
            .toString();
    }

}
