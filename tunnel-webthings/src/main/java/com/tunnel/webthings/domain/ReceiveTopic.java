package com.tunnel.webthings.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 这个是所有主题父类
 * @author ZHC
 * {@code @date} 2022/7/21
 */
@Data
@ApiModel("所有主题父类")
public class ReceiveTopic {

    @ApiModelProperty("设备编号们")
    private List<String> devNo;

    @ApiModelProperty("设备编号")
    private String devN;

    @ApiModelProperty("设备类型")
    private String devType;

    @ApiModelProperty("发送时间")
    private String timeStamp;

    @ApiModelProperty("直接类型")
    private String directType;

    @ApiModelProperty("直接类型描述")
    private String directTypeDesc;

    @ApiModelProperty("随机数")
    private String random;

    @ApiModelProperty("发送人")
    private String user;

    @ApiModelProperty("信息")
    private String ex;

}
