package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 气象设备环境数据
 */
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

    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getRainFall() {
        return rainFall;
    }

    public void setRainFall(String rainFall) {
        this.rainFall = rainFall;
    }

    public String getSunRainFall() {
        return sunRainFall;
    }

    public void setSunRainFall(String sunRainFall) {
        this.sunRainFall = sunRainFall;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getAirPressure() {
        return airPressure;
    }

    public void setAirPressure(String airPressure) {
        this.airPressure = airPressure;
    }

    public String getWaterThick() {
        return waterThick;
    }

    public void setWaterThick(String waterThick) {
        this.waterThick = waterThick;
    }

    public String getIceThick() {
        return iceThick;
    }

    public void setIceThick(String iceThick) {
        this.iceThick = iceThick;
    }

    public String getSnowThick() {
        return snowThick;
    }

    public void setSnowThick(String snowThick) {
        this.snowThick = snowThick;
    }

    public String getWet() {
        return wet;
    }

    public void setWet(String wet) {
        this.wet = wet;
    }

    public String getWindAngel() {
        return windAngel;
    }

    public void setWindAngel(String windAngel) {
        this.windAngel = windAngel;
    }

    public String getHumidityLudian() {
        return humidityLudian;
    }

    public void setHumidityLudian(String humidityLudian) {
        this.humidityLudian = humidityLudian;
    }

    public String getGasXiang() {
        return gasXiang;
    }

    public void setGasXiang(String gasXiang) {
        this.gasXiang = gasXiang;
    }

    public String getRainQiangDu() {
        return rainQiangDu;
    }

    public void setRainQiangDu(String rainQiangDu) {
        this.rainQiangDu = rainQiangDu;
    }

    public String getHumidityXiang() {
        return humidityXiang;
    }

    public void setHumidityXiang(String humidityXiang) {
        this.humidityXiang = humidityXiang;
    }

    public String getRainXingTai() {
        return rainXingTai;
    }

    public void setRainXingTai(String rainXingTai) {
        this.rainXingTai = rainXingTai;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getPathContactLu() {
        return pathContactLu;
    }

    public void setPathContactLu(String pathContactLu) {
        this.pathContactLu = pathContactLu;
    }

    public String getPathContactBing() {
        return pathContactBing;
    }

    public void setPathContactBing(String pathContactBing) {
        this.pathContactBing = pathContactBing;
    }

    public String getPathContactYan() {
        return pathContactYan;
    }

    public void setPathContactYan(String pathContactYan) {
        this.pathContactYan = pathContactYan;
    }

    public String getPathContactZhuang() {
        return pathContactZhuang;
    }

    public void setPathContactZhuang(String pathContactZhuang) {
        this.pathContactZhuang = pathContactZhuang;
    }

    public String getPathContactGailv() {
        return pathContactGailv;
    }

    public void setPathContactGailv(String pathContactGailv) {
        this.pathContactGailv = pathContactGailv;
    }

    public String getPathUncontactLu() {
        return pathUncontactLu;
    }

    public void setPathUncontactLu(String pathUncontactLu) {
        this.pathUncontactLu = pathUncontactLu;
    }

    public String getPathUncontactBing() {
        return pathUncontactBing;
    }

    public void setPathUncontactBing(String pathUncontactBing) {
        this.pathUncontactBing = pathUncontactBing;
    }

    public String getPathUncontactHou() {
        return pathUncontactHou;
    }

    public void setPathUncontactHou(String pathUncontactHou) {
        this.pathUncontactHou = pathUncontactHou;
    }

    public String getPathUncontactXueHou() {
        return pathUncontactXueHou;
    }

    public void setPathUncontactXueHou(String pathUncontactXueHou) {
        this.pathUncontactXueHou = pathUncontactXueHou;
    }

    public String getPathUncontactBingHou() {
        return pathUncontactBingHou;
    }

    public void setPathUncontactBingHou(String pathUncontactBingHou) {
        this.pathUncontactBingHou = pathUncontactBingHou;
    }

    public String getPathUncontactYan() {
        return pathUncontactYan;
    }

    public void setPathUncontactYan(String pathUncontactYan) {
        this.pathUncontactYan = pathUncontactYan;
    }

    public String getPathUncontactXiShu() {
        return pathUncontactXiShu;
    }

    public void setPathUncontactXiShu(String pathUncontactXiShu) {
        this.pathUncontactXiShu = pathUncontactXiShu;
    }

    public String getPathUncontactZhuang() {
        return pathUncontactZhuang;
    }

    public void setPathUncontactZhuang(String pathUncontactZhuang) {
        this.pathUncontactZhuang = pathUncontactZhuang;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Meteorological{" +
                "devNo='" + devNo + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", rainFall='" + rainFall + '\'' +
                ", sunRainFall='" + sunRainFall + '\'' +
                ", windSpeed='" + windSpeed + '\'' +
                ", windDirection='" + windDirection + '\'' +
                ", temperature='" + temperature + '\'' +
                ", humidity='" + humidity + '\'' +
                ", airPressure='" + airPressure + '\'' +
                ", waterThick='" + waterThick + '\'' +
                ", iceThick='" + iceThick + '\'' +
                ", snowThick='" + snowThick + '\'' +
                ", wet='" + wet + '\'' +
                ", windAngel='" + windAngel + '\'' +
                ", humidityLudian='" + humidityLudian + '\'' +
                ", gasXiang='" + gasXiang + '\'' +
                ", rainQiangDu='" + rainQiangDu + '\'' +
                ", humidityXiang='" + humidityXiang + '\'' +
                ", rainXingTai='" + rainXingTai + '\'' +
                ", visibility='" + visibility + '\'' +
                ", pathContactLu='" + pathContactLu + '\'' +
                ", pathContactBing='" + pathContactBing + '\'' +
                ", pathContactYan='" + pathContactYan + '\'' +
                ", pathContactZhuang='" + pathContactZhuang + '\'' +
                ", pathContactGailv='" + pathContactGailv + '\'' +
                ", pathUncontactLu='" + pathUncontactLu + '\'' +
                ", pathUncontactBing='" + pathUncontactBing + '\'' +
                ", pathUncontactHou='" + pathUncontactHou + '\'' +
                ", pathUncontactXueHou='" + pathUncontactXueHou + '\'' +
                ", pathUncontactBingHou='" + pathUncontactBingHou + '\'' +
                ", pathUncontactYan='" + pathUncontactYan + '\'' +
                ", pathUncontactXiShu='" + pathUncontactXiShu + '\'' +
                ", pathUncontactZhuang='" + pathUncontactZhuang + '\'' +
                ", temp='" + temp + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
