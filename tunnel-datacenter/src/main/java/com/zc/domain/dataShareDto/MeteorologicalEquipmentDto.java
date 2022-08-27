package com.zc.domain.dataShareDto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 气象设备环境
 */
@Data
public class MeteorologicalEquipmentDto {

    //系统id
    @JsonProperty(value = "sysid")
    private String sysId;

    //服务代码
    @JsonProperty(value = "service_code")
    private String serviceCode;

    //设备编号
    @JsonProperty(value = "dev_no")
    private String devNo;

    //起始时间
    @JsonProperty(value = "start_time")
    private String startTime;

    //终止时间
    @JsonProperty(value = "end_time")
    private String endTime;
}
