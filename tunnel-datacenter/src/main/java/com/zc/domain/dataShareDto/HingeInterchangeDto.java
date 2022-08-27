package com.zc.domain.dataShareDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 枢纽立交
 */
@Data
public class HingeInterchangeDto {

    //系统id
    @JsonProperty(value = "sysid")
    private String sysId;

    //服务代码
    @JsonProperty(value = "service_code")
    private String serviceCode;

    //立交枢纽公路编码
    @JsonProperty(value = "road_id")
    private String roadId;

    //立交枢纽id
    private String id;

    //立交枢纽简称
    private String name;
}
