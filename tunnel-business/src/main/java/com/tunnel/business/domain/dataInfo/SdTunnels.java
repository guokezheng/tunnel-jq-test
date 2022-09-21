package com.tunnel.business.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.tunnel.business.domain.event.SdSafetyIndex;
import com.tunnel.business.domain.event.SdTunnelSubarea;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 隧道对象 sd_tunnels
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
@ApiModel("隧道实体")
@Data
public class SdTunnels extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 隧道ID */
    @ApiModelProperty("隧道ID")
    private String tunnelId;

    /** 隧道名称 */
    @Excel(name = "隧道名称")
    @ApiModelProperty("隧道名称")
    private String tunnelName;

    /** 隧道编号 */
    @Excel(name = "隧道编号")
    @ApiModelProperty("隧道编号")
    private String tunnelCode;

    /** 隧道地址 */
    @Excel(name = "隧道地址")
    @ApiModelProperty("隧道地址")
    private String tunnelAddress;

    /** 经度 */
    @Excel(name = "经度")
    @ApiModelProperty("经度")
    private String longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    @ApiModelProperty("纬度")
    private String latitude;

    /** 隧道所ID */
    @Excel(name = "隧道所ID")
    @ApiModelProperty("隧道所ID")
    private String tunnelStationId;

    /** 隧道所名称 */
    @Excel(name = "隧道所名称")
    @ApiModelProperty("隧道所名称")
    private String tunnelStationName;

    /** 车道数量 */
    @Excel(name = "车道数量")
    @ApiModelProperty("车道数量")
    private Integer lane;

    /** 三维坐标 */
    @Excel(name = "三维坐标")
    @ApiModelProperty("三维坐标")
    private String coordinates;

    /** 备注 */
    @Excel(name = "备注")
    @ApiModelProperty("备注")
    private String remake;

    /** 隧道长度 */
    @Excel(name = "隧道长度")
    @ApiModelProperty("隧道长度")
    private String tunnelLength;

    @ApiModelProperty("隧道分区")
    public List<SdTunnelSubarea> sdTunnelSubareas;

    public String getTunnelLength() {
		return tunnelLength;
	}

	public void setTunnelLength(String tunnelLength) {
		this.tunnelLength = tunnelLength;
	}

	/** 是否使用 */
    @Excel(name = "是否使用",readConverterExp = "0=可用,1=不可用")
    @ApiModelProperty("是否使用")
    private Long poll;

    /** 存储配置图的html */
    @Excel(name = "存储配置图的html")
    @ApiModelProperty("存储配置图的html")
    private String storeConfigure;

    /**
     * 隧道所属部门id
     * */
    @ApiModelProperty("隧道所属部门id")
    private Long deptId;

    @ApiModelProperty("隧道所属部门名称")
    private String deptName;

    private List<SdTunnelSubarea> list;


    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
     * 安全指数
     */
    @ApiModelProperty("安全指数")
    private SdSafetyIndex percentage;

    public SdSafetyIndex getPercentage() {
        if (percentage == null) {
            percentage = new SdSafetyIndex();
        }
        return percentage;
    }

    public void setPercentage(SdSafetyIndex percentage) {
        this.percentage = percentage;
    }

    public void setTunnelId(String tunnelId)
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId()
    {
        return tunnelId;
    }
    public void setTunnelName(String tunnelName)
    {
        this.tunnelName = tunnelName;
    }

    public String getTunnelName()
    {
        return tunnelName;
    }
    public void setTunnelCode(String tunnelCode)
    {
        this.tunnelCode = tunnelCode;
    }

    public String getTunnelCode()
    {
        return tunnelCode;
    }
    public void setTunnelAddress(String tunnelAddress)
    {
        this.tunnelAddress = tunnelAddress;
    }

    public String getTunnelAddress()
    {
        return tunnelAddress;
    }
    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public String getLongitude()
    {
        return longitude;
    }
    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getLatitude()
    {
        return latitude;
    }
    public void setTunnelStationId(String tunnelStationId)
    {
        this.tunnelStationId = tunnelStationId;
    }

    public String getTunnelStationId()
    {
        return tunnelStationId;
    }
    public void setTunnelStationName(String tunnelStationName)
    {
        this.tunnelStationName = tunnelStationName;
    }

    public String getTunnelStationName()
    {
        return tunnelStationName;
    }
    public void setLane(Integer lane)
    {
        this.lane = lane;
    }

    public Integer getLane()
    {
        return lane;
    }
    public void setCoordinates(String coordinates)
    {
        this.coordinates = coordinates;
    }

    public String getCoordinates()
    {
        return coordinates;
    }
    public void setRemake(String remake)
    {
        this.remake = remake;
    }

    public String getRemake()
    {
        return remake;
    }
    public void setPoll(Long poll)
    {
        this.poll = poll;
    }

    public Long getPoll()
    {
        return poll;
    }

    public void setStoreConfigure(String storeConfigure)
    {
        this.storeConfigure = storeConfigure;
    }

    public String getStoreConfigure()
    {
        return storeConfigure;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("tunnelId", getTunnelId())
            .append("tunnelName", getTunnelName())
            .append("tunnelCode", getTunnelCode())
            .append("tunnelAddress", getTunnelAddress())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("tunnelStationId", getTunnelStationId())
            .append("tunnelStationName", getTunnelStationName())
            .append("lane", getLane())
            .append("coordinates", getCoordinates())
            .append("remake", getRemake())
            .append("poll", getPoll())
            .append("storeConfigure", getStoreConfigure())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("tunnelLength", getTunnelLength())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("percentage", getPercentage())
            .toString();
    }
}
