package com.zc.domain.dataShareDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 收费车道信息查询
 */
@Data
public class ChargeVehicleLaneDto {

    //系统id
    @JsonProperty(value = "sysid")
    private String sysId;

    //服务代码
    @JsonProperty(value = "service_code")
    private String serviceCode;

    //收费站编号
    @JsonProperty(value = "toll_station_id")
    private String tollStationId;

    //收费站名称
    @JsonProperty(value = "toll_station_name")
    private String tollStationName;

    //国标路线编码
    @JsonProperty(value = "road_id")
    private String roadId;

    //国标路线名称
    @JsonProperty(value = "road_name")
    private String roadName;
}
