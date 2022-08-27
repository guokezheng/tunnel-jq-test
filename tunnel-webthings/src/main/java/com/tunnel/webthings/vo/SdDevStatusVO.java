package com.tunnel.webthings.vo;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tunnel.webthings.domain.DevStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ZHC
 * {@code @date} 2022/7/15 14:33
 * 设备状态信息业务类
 */
@Data
@ApiModel("设备状态信息业务类")
public class SdDevStatusVO {

    @ApiModelProperty("设备编号们")
    private String devNo;

    @ApiModelProperty("设备编号")
    private String devN;

    @ApiModelProperty("设备类型")
    private String devType;

    @ApiModelProperty("设备上线时间")
    private String loginTime;

    @ApiModelProperty("设备状态")
    private String devStatus;

    @ApiModelProperty("网络状态")
    private String netStatus;

    @ApiModelProperty("设备状态描述")
    private String devStatusRemark;

    @ApiModelProperty("设备状态描述")
    private String netStatusRemark;

    @ApiModelProperty("时间戳")
    private String timeStamp;

    @ApiModelProperty("随机数")
    private String random;

    @ApiModelProperty("直接类型")
    private String directType;

    @ApiModelProperty("直接类型描述")
    private String directTypeDesc;


    private DevStatus expands;

    private String ex;

    public void setEx(DevStatus expands) {
        JSONObject jsonObject = JSONUtil.parseObj(expands);
        this.ex = jsonObject.toString();
    }

}
