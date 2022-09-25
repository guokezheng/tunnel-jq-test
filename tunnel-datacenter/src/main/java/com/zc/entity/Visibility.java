package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 能见度设备数据
 */
public class Visibility {

    @JsonProperty(value = "dev_no")
    private String devNo;

    private String visibility;

    @JsonProperty(value = "time_stamp")
    private String timeStamp;

    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Visibility{" +
                "devNo='" + devNo + '\'' +
                ", visibility='" + visibility + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
