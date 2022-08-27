package com.zc.domain.dataShareDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 桥梁明细
 */
@Data
public class BridgeDataDto {

    //系统id
    @JsonProperty(value = "sysid")
    private String sysId;

    //服务代码
    @JsonProperty(value = "service_code")
    private String serviceCode;

    //路线id
    @JsonProperty(value = "road_id")
    private String roadId;

    //桥梁名称
    @JsonProperty(value = "bridge_name")
    private String bridgeName;
}
