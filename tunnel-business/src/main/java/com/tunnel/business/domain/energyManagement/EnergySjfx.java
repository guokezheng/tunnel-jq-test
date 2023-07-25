package com.tunnel.business.domain.energyManagement;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EnergySjfx extends BaseEntity {

    /**
     * id
     */
    private Integer id;

    /**
     * 隧道id
     */
    private String tunnelId;

    /**
     * 分项id
     */
    private String itemizedCode;
    /**
     * 分类id
     */
    private String classificationCode;

    /**
     * 用电量
     */
    private Double energyValue;

    /**
     * 统计类型
     */
    private Integer statisticsType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getItemizedCode() {
        return itemizedCode;
    }

    public void setItemizedCode(String itemizedCode) {
        this.itemizedCode = itemizedCode;
    }

    public String getClassificationCode() {
        return classificationCode;
    }

    public void setClassificationCode(String classificationCode) {
        this.classificationCode = classificationCode;
    }

    /**
     * 更改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

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

    public Double getEnergyValue() {
        return energyValue;
    }

    public void setEnergyValue(Double energyValue) {
        this.energyValue = energyValue;
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
    public Date getUpdateTime() {
        return updateTime;
    }
    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tunnelId", getTunnelId())
                .append("energyValue", getEnergyValue())
                .append("statisticsType", getStatisticsType())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }

}
