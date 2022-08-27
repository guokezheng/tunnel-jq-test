package com.zc.domain.dataShareDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 服务区
 */
@Data
public class ServiceAreaDto {

    //系统id
    @JsonProperty(value = "sysid")
    private String sysId;

    //服务代码
    @JsonProperty(value = "service_code")
    private String serviceCode;

    //服务区编码
    @JsonProperty(value = "serviceArea_id")
    private String serviceAreaId;

    //服务区名称
    @JsonProperty(value = "servicearea_name")
    private String serviceAreaName;

    //国标路线编码
    @JsonProperty(value = "road_id")
    private String roadId;

    //国标路线名称
    @JsonProperty(value = "road_name")
    private String roadName;
}
