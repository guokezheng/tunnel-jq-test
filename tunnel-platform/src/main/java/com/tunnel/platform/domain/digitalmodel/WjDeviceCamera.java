package com.tunnel.platform.domain.digitalmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dzy
 * @date 2022/9/4 17:39
 */
@Data
public class WjDeviceCamera {

    @ApiModelProperty("设备类型")
    private Integer deviceType;

    @ApiModelProperty("设备Ip")
    private String	ip;

    @ApiModelProperty("码流")
    private Integer rate;

    @ApiModelProperty("异常事件")
    private String	errorContent;

    @ApiModelProperty("相机状态，0-离线，1-正常，2-异常，和正晨设备检测状态保持一致")
    private Integer status;

    @ApiModelProperty("设备id")
    private String eqId;
}
