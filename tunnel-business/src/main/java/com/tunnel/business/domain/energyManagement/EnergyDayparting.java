package com.tunnel.business.domain.energyManagement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 分时段报表Dto
 *
 * @date: 2022/3/19 15:58
 * @author: why
 * @version: 1.0
 */
public class EnergyDayparting extends BaseEntity {

    /**
     * id
     */
    private Integer id;

    /**
     * 隧道id
     */
    private String tunnelId;


    /**
     * 隧道id
     */
    private String tunnelName;

    /**
     * 尖峰
     */
    private Double jValue;
    /**
     * 高峰
     */
    private Double fValue;

    /**
     * 平时
     */
    private Double pValue;

    /**
     * 总
     */
    private Double sValue;
    /**
     * 低
     */
    private Double gValue;

    /**
     * 能耗值
     */
    private Double value;
    /**
     * 分类
     */
    private String target;
    /**
     * 来源
     */
    private String source;

    /**
     * 统计类型
     */
    private Integer statisticsType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(String tunnelId) {
        this.tunnelId = tunnelId;
    }

    public Double getjValue() {
        return jValue;
    }

    public void setjValue(Double jValue) {
        this.jValue = jValue;
    }

    public Double getfValue() {
        return fValue;
    }

    public void setfValue(Double fValue) {
        this.fValue = fValue;
    }

    public Double getpValue() {
        return pValue;
    }

    public void setpValue(Double pValue) {
        this.pValue = pValue;
    }

    public Double getsValue() {
        return sValue;
    }

    public void setsValue(Double sValue) {
        this.sValue = sValue;
    }

    public Double getgValue() {
        return gValue;
    }

    public void setgValue(Double gValue) {
        this.gValue = gValue;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getStatisticsType() {
        return statisticsType;
    }

    public void setStatisticsType(Integer statisticsType) {
        this.statisticsType = statisticsType;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tunnelId", getTunnelId())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }

}
