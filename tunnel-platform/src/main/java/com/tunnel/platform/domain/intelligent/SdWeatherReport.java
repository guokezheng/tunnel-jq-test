package com.tunnel.platform.domain.intelligent;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 气象采集器信息对象 sd_weather_report
 *
 * @author ruoyi
 * @date 2021-11-22
 */
public class SdWeatherReport extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "所属隧道")
    private String tunnelName;

    /**
     * 上报记录ID
     */
    private Long reportId;

    /**
     * 设备编号
     */
    @Excel(name = "设备编号")
    private Long deviceId;

    /**
     * 风的房向
     */
    @Excel(name = "风的方向")
    private String windDirection;

    /**
     * 风的速度
     */
    @Excel(name = "风的速度")
    private String windSpeed;

    /**
     * 空气温度
     */
    private String airTemperature;

    /**
     * 室内湿度
     */
    private String indoorHumidity;

    /**
     * 气压
     */
    private String pressure;

    /**
     * 降雨量
     */
    private String rainfall;

    /**
     * 降水形态
     */
    private String precipitationPattern;

    /**
     * 辐射
     */
    private String radiation;

    /**
     * 紫外
     */
    private String ultraviolet;

    /**
     * 1分钟能见度
     */
    @Excel(name = "1分钟能见度(m)")
    private String oneMinuteVisibility;

    /**
     * 10分钟能见度
     */
    @Excel(name = "10分钟能见度(m)")
    private String tenMinuteVisibility;

    /**
     * 水膜厚度
     */
    private String waterFilmThickness;

    /**
     * 冰的厚度
     */
    private String iceThickness;

    /**
     * 雪的厚度
     */
    private String snowThickness;

    /**
     * 湿滑系数
     */
    private String wetSliding;

    /**
     * 路面温度
     */
    private String pavementTemperature;

    /**
     * 路面状态
     */
    @Excel(name = "路面状态")
    private String pavementCondition;

    /**
     * 遥感检测仪工作状态
     */
    @Excel(name = "遥感检测仪工作状态", readConverterExp = "0=正常,1=异常")
    private String remoteSenseStatus;

    /**
     * 能见度仪工作状态
     */
    @Excel(name = "能见度仪工作状态", readConverterExp = "0=正常,1=异常")
    private String visibilityStatus;

    /**
     * 是否推送 0:未推送  1:已推送
     */
//    @Excel(name = "是否推送", readConverterExp = "0=未推送,1=已推送")
    private String isPush;
    /**
     * 所属隧道
     */
    private String tunnel;

    @Excel(name = "采集时间")
    private String collectionTime;

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }
    /**
     * 原始数据
     */
    private String rawData;

    public String getTunnel() {
        return tunnel;
    }

    public void setTunnel(String tunnel) {
        this.tunnel = tunnel;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setAirTemperature(String airTemperature) {
        this.airTemperature = airTemperature;
    }

    public String getAirTemperature() {
        return airTemperature;
    }

    public void setIndoorHumidity(String indoorHumidity) {
        this.indoorHumidity = indoorHumidity;
    }

    public String getIndoorHumidity() {
        return indoorHumidity;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getPressure() {
        return pressure;
    }

    public void setRainfall(String rainfall) {
        this.rainfall = rainfall;
    }

    public String getRainfall() {
        return rainfall;
    }

    public void setPrecipitationPattern(String precipitationPattern) {
        this.precipitationPattern = precipitationPattern;
    }

    public String getPrecipitationPattern() {
        return precipitationPattern;
    }

    public void setRadiation(String radiation) {
        this.radiation = radiation;
    }

    public String getRadiation() {
        return radiation;
    }

    public void setUltraviolet(String ultraviolet) {
        this.ultraviolet = ultraviolet;
    }

    public String getUltraviolet() {
        return ultraviolet;
    }

    public void setOneMinuteVisibility(String oneMinuteVisibility) {
        this.oneMinuteVisibility = oneMinuteVisibility;
    }

    public String getOneMinuteVisibility() {
        return oneMinuteVisibility;
    }

    public void setTenMinuteVisibility(String tenMinuteVisibility) {
        this.tenMinuteVisibility = tenMinuteVisibility;
    }

    public String getTenMinuteVisibility() {
        return tenMinuteVisibility;
    }

    public void setWaterFilmThickness(String waterFilmThickness) {
        this.waterFilmThickness = waterFilmThickness;
    }

    public String getWaterFilmThickness() {
        return waterFilmThickness;
    }

    public void setIceThickness(String iceThickness) {
        this.iceThickness = iceThickness;
    }

    public String getIceThickness() {
        return iceThickness;
    }

    public void setSnowThickness(String snowThickness) {
        this.snowThickness = snowThickness;
    }

    public String getSnowThickness() {
        return snowThickness;
    }

    public void setWetSliding(String wetSliding) {
        this.wetSliding = wetSliding;
    }

    public String getWetSliding() {
        return wetSliding;
    }

    public void setPavementTemperature(String pavementTemperature) {
        this.pavementTemperature = pavementTemperature;
    }

    public String getPavementTemperature() {
        return pavementTemperature;
    }

    public void setPavementCondition(String pavementCondition) {
        this.pavementCondition = pavementCondition;
    }

    public String getPavementCondition() {
        return pavementCondition;
    }

    public void setRemoteSenseStatus(String remoteSenseStatus) {
        this.remoteSenseStatus = remoteSenseStatus;
    }

    public String getRemoteSenseStatus() {
        return remoteSenseStatus;
    }

    public void setVisibilityStatus(String visibilityStatus) {
        this.visibilityStatus = visibilityStatus;
    }

    public String getVisibilityStatus() {
        return visibilityStatus;
    }

    public void setIsPush(String isPush) {
        this.isPush = isPush;
    }

    public String getIsPush() {
        return isPush;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public String getRawData() {
        return rawData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("reportId", getReportId())
                .append("deviceId", getDeviceId())
                .append("windDirection", getWindDirection())
                .append("windSpeed", getWindSpeed())
                .append("airTemperature", getAirTemperature())
                .append("indoorHumidity", getIndoorHumidity())
                .append("pressure", getPressure())
                .append("rainfall", getRainfall())
                .append("precipitationPattern", getPrecipitationPattern())
                .append("radiation", getRadiation())
                .append("ultraviolet", getUltraviolet())
                .append("oneMinuteVisibility", getOneMinuteVisibility())
                .append("tenMinuteVisibility", getTenMinuteVisibility())
                .append("waterFilmThickness", getWaterFilmThickness())
                .append("iceThickness", getIceThickness())
                .append("snowThickness", getSnowThickness())
                .append("wetSliding", getWetSliding())
                .append("pavementTemperature", getPavementTemperature())
                .append("pavementCondition", getPavementCondition())
                .append("remoteSenseStatus", getRemoteSenseStatus())
                .append("visibilityStatus", getVisibilityStatus())
                .append("isPush", getIsPush())
                .append("rawData", getRawData())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("remark", getRemark())
                .append("tunnel", getTunnel())
                .append("tunnelName", getTunnelName())
                .append("collectionTime", getCollectionTime())
                .toString();
    }
}
