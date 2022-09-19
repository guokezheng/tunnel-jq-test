package com.tunnel.business.domain.digitalmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dzy
 * @date 2022/9/6 11:25
 */
@Data
public class WjDeviceData {

    @ApiModelProperty("设备实时运行状态")
    private Integer runStatus;

    @ApiModelProperty("设备实时运行数据（开关）无单位")
    private String runDate;

    @ApiModelProperty("单位")
    private String	unit;

    @ApiModelProperty("设备实时运行模式(同步闪、流水闪)")
    private Integer runMode;

    @ApiModelProperty("风速")
    private Double windSpeed;

    @ApiModelProperty("风向")
    private String windDirection;

    @ApiModelProperty("CO值")
    private Double CO;

    @ApiModelProperty("VI值")
    private Double VI;

    @ApiModelProperty("信息内容")
    private String message;

    /**
     *设备状态查询后补字段
     */
    @ApiModelProperty("设备ID")
    private String deviceId;

    @ApiModelProperty("设备类型")
    private String deviceType;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备状态")
    private Integer deviceStatus;

}
