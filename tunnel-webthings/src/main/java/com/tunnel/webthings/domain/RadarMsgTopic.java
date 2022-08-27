package com.tunnel.webthings.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dzy
 * {@code @date} 2022/7/22 11:04
 * 雷达信息数据实体类
 */
@Data
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

}
