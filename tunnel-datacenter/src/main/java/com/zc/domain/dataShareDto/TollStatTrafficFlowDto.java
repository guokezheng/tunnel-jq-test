package com.zc.domain.dataShareDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 收费站车流量
 */
@Data
public class TollStatTrafficFlowDto {

    //系统id
    @JsonProperty(value = "sysid")
    private String sysId;

    //服务代码
    @JsonProperty(value = "service_code")
    private String serviceCode;

    //出入口标识： 1入口  2出口
    @JsonProperty(value = "station_type")
    private String stationType;

    //开始时间
    @JsonProperty(value = "start_time")
    private String startTime;

    //终止时间
    @JsonProperty(value = "end_time")
    private String endTime;

    //收费站编码省标
    @JsonProperty(value = "station_id")
    private String stationId;

    //收费站编码国标
    @JsonProperty(value = "station_id_gb")
    private String stationIdGb;
}
