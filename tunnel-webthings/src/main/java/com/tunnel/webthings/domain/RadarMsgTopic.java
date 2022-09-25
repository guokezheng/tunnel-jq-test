package com.tunnel.webthings.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dzy
 * {@code @date} 2022/7/22 11:04
 * 雷达信息数据实体类
 */
@ApiModel("雷达消息数据类实体")
public class RadarMsgTopic {

    @ApiModelProperty("融合ID")
    private String fuseId;

    @ApiModelProperty("目标物编号")
    private String id;

    @ApiModelProperty("路侧单元检测到的交通参与者类型")
    private Integer ptcType;

    @ApiModelProperty("车道号")
    private Integer lane;

    @ApiModelProperty("车道上下行")
    private Integer direct;

    @ApiModelProperty("车辆长度")
    private Double vehL;

    @ApiModelProperty("车辆宽度")
    private Double vehW;

    @ApiModelProperty("车辆高度")
    private Double vehH;

    @ApiModelProperty("车牌号")
    private String vehPlate;

    @ApiModelProperty("车牌颜色")
    private String vehPlateColor;

    @ApiModelProperty("车身颜色")
    private String vehColor;

    @ApiModelProperty("车辆品牌")
    private String vehBrand;

    @ApiModelProperty("经度")
    private Double ptcLon;

    @ApiModelProperty("纬度")
    private Double ptcLat;

    @ApiModelProperty("海拔")
    private Double ptcEle;

    @ApiModelProperty("经度")
    private Double amapLon;

    @ApiModelProperty("纬度")
    private Double amapLat;

    @ApiModelProperty("行驶速度")
    private Double ptcSpeed;

    @ApiModelProperty("车辆航向角")
    private Double ptcHeading;

    @ApiModelProperty("机动车类型")
    private Integer vehType;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("备用字段")
    private String spare;

    public String getFuseId() {
        return fuseId;
    }

    public void setFuseId(String fuseId) {
        this.fuseId = fuseId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPtcType() {
        return ptcType;
    }

    public void setPtcType(Integer ptcType) {
        this.ptcType = ptcType;
    }

    public Integer getLane() {
        return lane;
    }

    public void setLane(Integer lane) {
        this.lane = lane;
    }

    public Integer getDirect() {
        return direct;
    }

    public void setDirect(Integer direct) {
        this.direct = direct;
    }

    public Double getVehL() {
        return vehL;
    }

    public void setVehL(Double vehL) {
        this.vehL = vehL;
    }

    public Double getVehW() {
        return vehW;
    }

    public void setVehW(Double vehW) {
        this.vehW = vehW;
    }

    public Double getVehH() {
        return vehH;
    }

    public void setVehH(Double vehH) {
        this.vehH = vehH;
    }

    public String getVehPlate() {
        return vehPlate;
    }

    public void setVehPlate(String vehPlate) {
        this.vehPlate = vehPlate;
    }

    public String getVehPlateColor() {
        return vehPlateColor;
    }

    public void setVehPlateColor(String vehPlateColor) {
        this.vehPlateColor = vehPlateColor;
    }

    public String getVehColor() {
        return vehColor;
    }

    public void setVehColor(String vehColor) {
        this.vehColor = vehColor;
    }

    public String getVehBrand() {
        return vehBrand;
    }

    public void setVehBrand(String vehBrand) {
        this.vehBrand = vehBrand;
    }

    public Double getPtcLon() {
        return ptcLon;
    }

    public void setPtcLon(Double ptcLon) {
        this.ptcLon = ptcLon;
    }

    public Double getPtcLat() {
        return ptcLat;
    }

    public void setPtcLat(Double ptcLat) {
        this.ptcLat = ptcLat;
    }

    public Double getPtcEle() {
        return ptcEle;
    }

    public void setPtcEle(Double ptcEle) {
        this.ptcEle = ptcEle;
    }

    public Double getAmapLon() {
        return amapLon;
    }

    public void setAmapLon(Double amapLon) {
        this.amapLon = amapLon;
    }

    public Double getAmapLat() {
        return amapLat;
    }

    public void setAmapLat(Double amapLat) {
        this.amapLat = amapLat;
    }

    public Double getPtcSpeed() {
        return ptcSpeed;
    }

    public void setPtcSpeed(Double ptcSpeed) {
        this.ptcSpeed = ptcSpeed;
    }

    public Double getPtcHeading() {
        return ptcHeading;
    }

    public void setPtcHeading(Double ptcHeading) {
        this.ptcHeading = ptcHeading;
    }

    public Integer getVehType() {
        return vehType;
    }

    public void setVehType(Integer vehType) {
        this.vehType = vehType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSpare() {
        return spare;
    }

    public void setSpare(String spare) {
        this.spare = spare;
    }

    @Override
    public String toString() {
        return "RadarMsgTopic{" +
                "fuseId='" + fuseId + '\'' +
                ", id='" + id + '\'' +
                ", ptcType=" + ptcType +
                ", lane=" + lane +
                ", direct=" + direct +
                ", vehL=" + vehL +
                ", vehW=" + vehW +
                ", vehH=" + vehH +
                ", vehPlate='" + vehPlate + '\'' +
                ", vehPlateColor='" + vehPlateColor + '\'' +
                ", vehColor='" + vehColor + '\'' +
                ", vehBrand='" + vehBrand + '\'' +
                ", ptcLon=" + ptcLon +
                ", ptcLat=" + ptcLat +
                ", ptcEle=" + ptcEle +
                ", amapLon=" + amapLon +
                ", amapLat=" + amapLat +
                ", ptcSpeed=" + ptcSpeed +
                ", ptcHeading=" + ptcHeading +
                ", vehType=" + vehType +
                ", createTime='" + createTime + '\'' +
                ", spare='" + spare + '\'' +
                '}';
    }
}
