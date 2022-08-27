package com.tunnel.webthings.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 主动发光标志
 * @author dear_
 * 主动发光标志类
 */
@Data
@ApiModel("主动发光标志实体")
public class ActiveLuminousSigns {

    @ApiModelProperty("任务类型")
    private String taskType;

    @ApiModelProperty("背光开关")
    private String backlitSwitch;

    @ApiModelProperty("背光亮度")
    private String backlightBrightness;

    @ApiModelProperty("可变车道")
    private String reversibleLanes;

    @ApiModelProperty("光衰补偿等级")
    private String droopCompensation;

    @ApiModelProperty("实测电流")
    private String measuredCurrent;

    @ApiModelProperty("默认电流")
    private String defaultCurrent;

}
