package com.tunnel.webthings.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dzy
 * @date 2022/8/22 17:10
 */
@Data
public class DeviceParameter extends BaseEntity {

    private static final long serialVersionUID =-1242493306307174690L;

    @ApiModelProperty("设备类型")
    private String devCategory;

    @ApiModelProperty("设备编号")
    private String devNo;

    @ApiModelProperty("设备类型")
    private String devType;

    @ApiModelProperty("集团公司编码")
    private String opmaCliqueId;

    @ApiModelProperty("管养公司编码")
    private String opmaManagerCropId;

    @ApiModelProperty("管养单位编码")
    private String opmaManagerId;

    @ApiModelProperty("道路编号")
    private String roadId; //如：G000337

    @ApiModelProperty("所属收费站")
    private String stationId;
}
