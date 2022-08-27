package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 气象设备环境数据
 */
@Data
public class Meteorological {

    @JsonProperty(value = "dev_no")
    private String devNo;

    @JsonProperty(value = "time_stamp")
    private String timeStamp;

    @JsonProperty(value = "rain_fall")
    private String rainFall;

    @JsonProperty(value = "sun_rain_fall")
    private String sunRainFall;

    @JsonProperty(value = "wind_speed")
    private String windSpeed;

    @JsonProperty(value = "wind_direction")
    private String windDirection;

    private String temperature;

    private String humidity;

    @JsonProperty(value = "air_pressure")
    private String airPressure;

    @JsonProperty(value = "water_thick")
    private String waterThick;

    @JsonProperty(value = "ice_thick")
    private String iceThick;

    @JsonProperty(value = "snow_thick")
    private String snowThick;

    private String wet;

    @JsonProperty(value = "wind_angel")
    private String windAngel;

    @JsonProperty(value = "humidity_ludian")
    private String humidityLudian;

    @JsonProperty(value = "gas_xiang")
    private String gasXiang;

    @JsonProperty(value = "rain_qiang_du")
    private String rainQiangDu;

    @JsonProperty(value = "humidity_xiang")
    private String humidityXiang;

    @JsonProperty(value = "rain_xing_tai")
    private String rainXingTai;

    private String visibility;

    @JsonProperty(value = "path_contact_lu")
    private String pathContactLu;

    @JsonProperty(value = "path_contact_bing")
    private String pathContactBing;

    @JsonProperty(value = "path_contact_yan")
    private String pathContactYan;

    @JsonProperty(value = "path_contact_zhuang")
    private String pathContactZhuang;

    @JsonProperty(value = "path_contact_gailv")
    private String pathContactGailv;

    @JsonProperty(value = "path_uncontact_lu")
    private String pathUncontactLu;

    @JsonProperty(value = "path_uncontact_bing")
    private String pathUncontactBing;

    @JsonProperty(value = "path_uncontact_hou")
    private String pathUncontactHou;

    @JsonProperty(value = "path_uncontact_xue_hou")
    private String pathUncontactXueHou;

    @JsonProperty(value = "path_uncontact_bing_hou")
    private String pathUncontactBingHou;

    @JsonProperty(value = "path_uncontact_yan")
    private String pathUncontactYan;

    @JsonProperty(value = "path_uncontact_xi_shu")
    private String pathUncontactXiShu;

    @JsonProperty(value = "path_uncontact_zhuang")
    private String pathUncontactZhuang;

    private String temp;

    @JsonProperty(value = "create_time")
    private String createTime;















}
