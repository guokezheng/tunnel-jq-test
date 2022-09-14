package com.tunnel.platform.domain.digitalmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dzy
 * @date 2022/9/14 9:59
 */
@Data
public class SdDeviceDataItem {

    @ApiModelProperty("设备数据")
    private String data;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("设备id")
    private String deviceId;
}
