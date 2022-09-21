package com.tunnel.business.domain.xfWaterRecord;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 消防水设备监测记录对象 sd_xfwater_record
 * @date 2022-02-11
 */
public class SdXfwaterRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 设备编码 */
    @Excel(name = "设备编码")
    private String equipmentId;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long deptId;

    /** 电量 */
    @Excel(name = "电量")
    private String electricity;

    /** 数据记录间隔 */
    @Excel(name = "数据记录间隔")
    private String intervalTime;

    /** 信号强度 */
    @Excel(name = "信号强度")
    private String signalInfo;

    /** 模拟量 */
    @Excel(name = "模拟量")
    private String analogQuantity;

    /** 状态 */
    @Excel(name = "状态")
    private String state;

    /** 最低值 */
    @Excel(name = "最低值")
    private String low;

    /** 最高值 */
    @Excel(name = "最高值")
    private String highest;

    /**
     * 是否离线
     */
    private String isOff;

    /**
     * 所属隧道ID
     */
    private String tunnelId;

    /**
     * 所属隧道名称
     */
    private String tunnelName;

    /**
     * 设备桩号
     */
    private String position;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 统计类型
     */
    private String statisticalType;

    /**
     * 方向
     */
    private String eqDirection;

    public String getEqDirection() {
        return eqDirection;
    }

    public void setEqDirection(String eqDirection) {
        this.eqDirection = eqDirection;
    }

    public String getStatisticalType() {
        return statisticalType;
    }

    public void setStatisticalType(String statisticalType) {
        this.statisticalType = statisticalType;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(String tunnelId) {
        this.tunnelId = tunnelId;
    }

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getIsOff() {
        return isOff;
    }

    public void setIsOff(String isOff) {
        this.isOff = isOff;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setEquipmentId(String equipmentId) 
    {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentId() 
    {
        return equipmentId;
    }
    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }
    public void setElectricity(String electricity) 
    {
        this.electricity = electricity;
    }

    public String getElectricity() 
    {
        return electricity;
    }
    public void setIntervalTime(String intervalTime) 
    {
        this.intervalTime = intervalTime;
    }

    public String getIntervalTime() 
    {
        return intervalTime;
    }
    public void setSignalInfo(String signalInfo) 
    {
        this.signalInfo = signalInfo;
    }

    public String getSignalInfo() 
    {
        return signalInfo;
    }
    public void setAnalogQuantity(String analogQuantity) 
    {
        this.analogQuantity = analogQuantity;
    }

    public String getAnalogQuantity() 
    {
        return analogQuantity;
    }
    public void setState(String state) 
    {
        this.state = state;
    }

    public String getState() 
    {
        return state;
    }
    public void setLow(String low) 
    {
        this.low = low;
    }

    public String getLow() 
    {
        return low;
    }
    public void setHighest(String highest) 
    {
        this.highest = highest;
    }

    public String getHighest() 
    {
        return highest;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("equipmentId", getEquipmentId())
            .append("deptId", getDeptId())
            .append("electricity", getElectricity())
            .append("intervalTime", getIntervalTime())
            .append("signalInfo", getSignalInfo())
            .append("analogQuantity", getAnalogQuantity())
            .append("state", getState())
            .append("low", getLow())
            .append("highest", getHighest())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("isOff", getIsOff())
            .append("tunnelId", getTunnelId())
            .append("tunnelName", getTunnelName())
            .append("position", getPosition())
            .append("equipmentName", getEquipmentName())
            .toString();
    }
}
