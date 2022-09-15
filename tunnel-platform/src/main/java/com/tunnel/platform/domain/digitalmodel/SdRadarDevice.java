package com.tunnel.platform.domain.digitalmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dzy
 * @date 2022/9/7 10:55
 */
@Data
public class SdRadarDevice {

    /**
     * 万集字段
     */
    @ApiModelProperty("设备类别")
    private String deviceType;

    @ApiModelProperty("设备ID")
    private String deviceId;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备编码")
    private String deviceCode;

    @ApiModelProperty("0-离线，1-正常    1-在线；2-离线；3-故障")
    private Integer deviceStatus;

    @ApiModelProperty("分辨率1e-7°，东经为正，西经为负 经度")
    private Double longitude;

    @ApiModelProperty("分辨率1e-7°，北纬为正，南纬为负 纬度")
    private Double latitude;

    @ApiModelProperty("方向")
    private String direction;

    @ApiModelProperty("桩号")
    private String stakeNum;

    @ApiModelProperty("模型位置姿态缩放信息，需要孪生平台预设信息")
    private String transform;

    /**
     * 设备数据：包括设备实时数据、实时状态，根据deviceType区分
     */
    private WjDeviceData deviceData;
}
