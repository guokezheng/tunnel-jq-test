package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 能见度设备数据
 */
@Data
public class Visibility {

    @JsonProperty(value = "dev_no")
    private String devNo;

    private String visibility;

    @JsonProperty(value = "time_stamp")
    private String timeStamp;
}
