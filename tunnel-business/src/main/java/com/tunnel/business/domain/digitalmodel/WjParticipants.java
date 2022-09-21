package com.tunnel.business.domain.digitalmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dzy
 * @date 2022/9/4 11:39
 */
@Data
public class WjParticipants {

    @ApiModelProperty("数据流水号")
    private String recordSerialNumber;

    @ApiModelProperty("参与者全域ID")
    private Integer  id;

    @ApiModelProperty("车辆类型")
    private Integer originalType;

    @ApiModelProperty("车辆颜色")
    private Integer originalColor;

    @ApiModelProperty("分辨率1e-7°，东经为正，西经为负")
    private Double	longitude;

    @ApiModelProperty("分辨率1e-7°，北纬为正，南纬为负")
    private Double latitude;

    @ApiModelProperty("速度，单位：Km/h")
    private Double speed;

    @ApiModelProperty("车道号,沿行车方向从左往右依次为1,2,3,4…")
    private Integer laneNum;

    @ApiModelProperty("航向角，单位：°，保留1位小数，车头与正北夹角")
    private Double courseAngle;

    @ApiModelProperty("车牌号")
    private String picLicense;

    @ApiModelProperty("车牌颜色")
    private Integer  vehicleColor;

    @ApiModelProperty("桩号")
    private String stakeNum;
}
