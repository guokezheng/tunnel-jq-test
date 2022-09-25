package com.tunnel.webthings.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author ZHC
 * {@code @date} 2022/7/15
 * 设备状态
 */
@ApiModel("设备状态")
public class DevStatus {

    @ApiModelProperty("状态码")
    private String code;

    @ApiModelProperty("数据")
    private Map<String,String> data;

    @ApiModelProperty("时间")
    private Date time;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "DevStatus{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", time=" + time +
                '}';
    }
}
