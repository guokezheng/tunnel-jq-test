package com.zc.domain.dataShareDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 能见度设备数据查询
 */
@Data
public class VisibilityDeviceDto {

    //系统id
    @JsonProperty(value = "sysid")
    private String sysId;

    //服务代码
    @JsonProperty(value = "service_code")
    private String serviceCode;

    //设备编号
    @JsonProperty(value = "dev_no")
    private String devNo;

    //开始时间
    @JsonProperty(value = "start_time")
    private String startTime;

    //结束时间
    @JsonProperty(value = "end_time")
    private String endTime;
}
