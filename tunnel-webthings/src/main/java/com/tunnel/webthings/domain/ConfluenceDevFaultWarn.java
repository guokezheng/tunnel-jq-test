package com.tunnel.webthings.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dzy
 * @date 2022/7/20 10:59
 * 合流区预警设备故障告警类
 */
@Data
@ApiModel("合流区预警设备故障告警类")
public class ConfluenceDevFaultWarn {

    @ApiModelProperty("设备编号")
    private String devCode;

    @ApiModelProperty("设备类型")
    private Integer devType;

    @ApiModelProperty("设备名称")
    private String devName;

    @ApiModelProperty("消息类型")
    private String msgType;

    @ApiModelProperty("消息时间")
    private String devTime;

    @ApiModelProperty("触发类型(1 告警 0 解除)")
    private Integer envTriType;

    @ApiModelProperty("故障代码")
    private String faultCode;

    @ApiModelProperty("事件坐标经度")
    private String envLon;

    @ApiModelProperty("事件坐标纬度")
    private String envLat;

    @ApiModelProperty("备注")
    private String remark;

}
