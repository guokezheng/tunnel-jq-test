package com.tunnel.webthings.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author dzy
 * @date 2022/7/21 14:34
 */
@Data
public class SendMsgSuper {

    @NotEmpty
    @ApiModelProperty("设备编号")
    private String devNo;

    @NotEmpty
    @ApiModelProperty("设备类型")
    private String devType;

    @ApiModelProperty("时间戳")
    private String timeStamp;

}
