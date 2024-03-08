package com.tunnel.business.domain.dataInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.tunnel.business.domain.event.SdSafetyIndex;
import com.tunnel.business.domain.event.SdTunnelSubarea;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 隧道对象 sd_tunnels
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
@ApiModel("隧道实体")
public class SdTunnels extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 隧道ID */
    @ApiModelProperty("隧道ID")
    @Excel(name = "隧道ID")
    private String tunnelId;

    /** 隧道名称 */
    @Excel(name = "隧道名称")
    @ApiModelProperty("隧道名称")
    private String tunnelName;

    /** 隧道编号 */
    /*@Excel(name = "隧道编号")*/
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
   /* @Excel(name = "隧道所ID")*/
    @ApiModelProperty("隧道所ID")
    private String tunnelStationId;

    /** 隧道所名称 */
    /*@Excel(name = "隧道所名称")*/
    @ApiModelProperty("隧道所名称")
    private String tunnelStationName;

    /** 车道数量 */
    @Excel(name = "车道数量")
    @ApiModelProperty("车道数量")
    private Integer lane;

    /** 备注 */
    /*@Excel(name = "备注")*/
    @ApiModelProperty("备注")
    private String remake;

    /** 隧道长度 */
    @Excel(name = "隧道长度(米)")
    @ApiModelProperty("隧道长度")
    private String tunnelLength;

    @ApiModelProperty("隧道开始桩号")
    @Excel(name = "隧道开始桩号")
    private String startPile;

    @ApiModelProperty("隧道结束桩号")
    @Excel(name = "隧道结束桩号")
    private String endPile;

    /** 三维坐标 */
    @Excel(name = "三维坐标")
    @ApiModelProperty("三维坐标")
    private String coordinates;

    @ApiModelProperty("隧道开始桩号(整形)")
    private String startPileNum;

    @ApiModelProperty("隧道结束桩号(整形)")
    private String endPileNum;

    /**
     * road_id路段ID
     * */
    @ApiModelProperty("路段ID")
    private String roadId;

    @ApiModelProperty("祖级列表")
    private String ancestors;

    @ApiModelProperty("道路编码")
    private String roadCode;

    @ApiModelProperty("道路名称")
    private String roadName;

    @ApiModelProperty("等级编码")
    private int levelCode;

    @ApiModelProperty("等级名称")
    private String levelName;

    @ApiModelProperty("长度编码")
    private int lengthCode;

    @ApiModelProperty("长度名称")
    private String lengthName;

    @ApiModelProperty("修建年度")
    private int constructYear;

    @ApiModelProperty("建成通车日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activateTime;

    @ApiModelProperty("管养单位ID")
    private String managerId;

    @ApiModelProperty("区划编码")
    private String orgCode;

    @ApiModelProperty("区划名称")
    private String orgName;

    private String ids;

    public String getIds() {
        return this.ids;
    }

    public void setIds( String ids) {
        this.ids = ids;
    }

    public String getRoadCode() {
        return roadCode;
    }

    public void setRoadCode(String roadCode) {
        this.roadCode = roadCode;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public int getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(int levelCode) {
        this.levelCode = levelCode;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getLengthCode() {
        return lengthCode;
    }

    public void setLengthCode(int lengthCode) {
        this.lengthCode = lengthCode;
    }

    public String getLengthName() {
        return lengthName;
    }

    public void setLengthName(String lengthName) {
        this.lengthName = lengthName;
    }

    public int getConstructYear() {
        return constructYear;
    }

    public void setConstructYear(int constructYear) {
        this.constructYear = constructYear;
    }

    public Date getActivateTime() {
        return activateTime;
    }

    public void setActivateTime(Date activateTime) {
        this.activateTime = activateTime;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public String getStartPileNum() {
        return startPileNum;
    }

    public void setStartPileNum(String startPileNum) {
        this.startPileNum = startPileNum;
    }

    public String getEndPileNum() {
        return endPileNum;
    }

    public void setEndPileNum(String endPileNum) {
        this.endPileNum = endPileNum;
    }

    public String getStartPile() {
        return startPile;
    }

    public void setStartPile(String startPile) {
        this.startPile = startPile;
    }

    public String getEndPile() {
        return endPile;
    }

    public void setEndPile(String endPile) {
        this.endPile = endPile;
    }



    @ApiModelProperty("隧道分区")
    public List<SdTunnelSubarea> sdTunnelSubareas;

    public String getTunnelLength() {
		return tunnelLength;
	}

	public void setTunnelLength(String tunnelLength) {
		this.tunnelLength = tunnelLength;
	}

    @ApiModelProperty("隧道所属部门名称")
    @Excel(name = "所属部门")
    private String deptName;

	/** 是否使用 */
    @Excel(name = "是否启用",readConverterExp = "0=启用,1=停用")
    @ApiModelProperty("是否启")
    private Long poll;

    /** 存储配置图的html */
   /* @Excel(name = "存储配置图的html")*/
    @ApiModelProperty("存储配置图的html")
    private String storeConfigure;

    /**
     * 隧道所属部门id
     * */
    @ApiModelProperty("隧道所属部门id")
    private String deptId;

    /**
     * 隧道ID集合
     */
    private List<String> tunnelIds;

    private List<SdTunnelSubarea> list;


    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
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

    public List<SdTunnelSubarea> getSdTunnelSubareas() {
        return sdTunnelSubareas;
    }

    public void setSdTunnelSubareas(List<SdTunnelSubarea> sdTunnelSubareas) {
        this.sdTunnelSubareas = sdTunnelSubareas;
    }

    public List<SdTunnelSubarea> getList() {
        return list;
    }

    public void setList(List<SdTunnelSubarea> list) {
        this.list = list;
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

    public List<String> getTunnelIds() {
        return tunnelIds;
    }

    public void setTunnelIds(List<String> tunnelIds) {
        this.tunnelIds = tunnelIds;
    }

    @Override
    public String toString() {
        return "SdTunnels{" +
                "tunnelId='" + tunnelId + '\'' +
                ", tunnelName='" + tunnelName + '\'' +
                ", tunnelCode='" + tunnelCode + '\'' +
                ", tunnelAddress='" + tunnelAddress + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", tunnelStationId='" + tunnelStationId + '\'' +
                ", tunnelStationName='" + tunnelStationName + '\'' +
                ", lane=" + lane +
                ", remake='" + remake + '\'' +
                ", tunnelLength='" + tunnelLength + '\'' +
                ", startPile='" + startPile + '\'' +
                ", endPile='" + endPile + '\'' +
                ", coordinates='" + coordinates + '\'' +
                ", startPileNum='" + startPileNum + '\'' +
                ", endPileNum='" + endPileNum + '\'' +
                ", roadId='" + roadId + '\'' +
                ", ancestors='" + ancestors + '\'' +
                ", roadCode='" + roadCode + '\'' +
                ", roadName='" + roadName + '\'' +
                ", levelCode=" + levelCode +
                ", levelName='" + levelName + '\'' +
                ", lengthCode=" + lengthCode +
                ", lengthName='" + lengthName + '\'' +
                ", constructYear=" + constructYear +
                ", activateTime=" + activateTime +
                ", managerId='" + managerId + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", orgName='" + orgName + '\'' +
                ", ids='" + ids + '\'' +
                ", sdTunnelSubareas=" + sdTunnelSubareas +
                ", deptName='" + deptName + '\'' +
                ", poll=" + poll +
                ", storeConfigure='" + storeConfigure + '\'' +
                ", deptId='" + deptId + '\'' +
                ", tunnelIds=" + tunnelIds +
                ", list=" + list +
                ", percentage=" + percentage +
                '}';
    }
}
