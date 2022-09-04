package com.tunnel.platform.domain.event;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author dzy
 * @date 2022/9/4 14:32
 */
@Data
public class SdRadarDetectData {

    @ApiModelProperty("主键")
    private  String id;

    @ApiModelProperty("车辆id")
    private  String vehicleId;

    @ApiModelProperty("隧道id")
    private  String tunnelId;

    @ApiModelProperty("车辆类型")
    private  String vehicleType;

    @ApiModelProperty("车辆颜色")
    private  String vehicleColor;

    @ApiModelProperty("经度,分辨率1e-7°，东经为正，西经为负")
    private  String longitude;

    @ApiModelProperty("纬度,分辨率1e-7°，北纬为正，南纬为负")
    private  String latitude;

    @ApiModelProperty("速度，单位：Km/h")
    private  String speed;

    @ApiModelProperty("车道号,沿行车方向从左往右依次为1,2,3,4…")
    private  String laneNum;

    @ApiModelProperty("航向角，单位：°，保留1位小数，车头与正北夹角")
    private  String courseAngle;

    @ApiModelProperty("车牌号")
    private  String vehicleLicense;

    @ApiModelProperty("车牌颜色")
    private  String licenseColor;

    @ApiModelProperty("桩号")
    private  String stakeNum;

    @ApiModelProperty("监测时间（保留3位毫秒数）")
    private Date detectTime;
}
