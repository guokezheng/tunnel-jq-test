package com.tunnel.deal.corniceTunnelRobot.domain;

//机器人状态
public class StatusDto {

    //在线状态：0 不在线 1 在线
    private String isOnline;
    //运行时长
    private String currentDuration;
    //里程数
    private String currentMileage;
    //当前电量
    private String electricity;
    //是否在充电
    private String charge;
    //当前电压
    private String voltage;
    //当前电流
    private String current;
    //电池温度
    private String batteryTemp;
    //当前所在轨道位置(距离起始点),单位：㎜
    private String position;
    //氧气浓度
    private String oxygenDensity;
    //一氧化碳浓度
    private String carbonMonoxideDensity;
    //环境温度(℃)
    private String temperature;
    //环境湿度(%)
    private String humidity;
    //环境亮度
    private String brightness;

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getCurrentDuration() {
        return currentDuration;
    }

    public void setCurrentDuration(String currentDuration) {
        this.currentDuration = currentDuration;
    }

    public String getCurrentMileage() {
        return currentMileage;
    }

    public void setCurrentMileage(String currentMileage) {
        this.currentMileage = currentMileage;
    }

    public String getElectricity() {
        return electricity;
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getBatteryTemp() {
        return batteryTemp;
    }

    public void setBatteryTemp(String batteryTemp) {
        this.batteryTemp = batteryTemp;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getOxygenDensity() {
        return oxygenDensity;
    }

    public void setOxygenDensity(String oxygenDensity) {
        this.oxygenDensity = oxygenDensity;
    }

    public String getCarbonMonoxideDensity() {
        return carbonMonoxideDensity;
    }

    public void setCarbonMonoxideDensity(String carbonMonoxideDensity) {
        this.carbonMonoxideDensity = carbonMonoxideDensity;
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

    public String getBrightness() {
        return brightness;
    }

    public void setBrightness(String brightness) {
        this.brightness = brightness;
    }
}
