package com.tunnel.business.domain.event;

import com.tunnel.business.domain.dataInfo.SdDeviceTypeItem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 触发器对象 sd_trigger
 * 
 * @author ruoyi
 * @date 2022-09-01
 */
public class SdTrigger extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 触发器id */
    private Long id;

    /** 关联id（策略id） */
    @Excel(name = "关联id", readConverterExp = "策略id")
    private Long relateId;

    /** 设备类型id */
    @Excel(name = "设备类型id")
    private Long deviceTypeId;

    /** 设备id */
    @Excel(name = "设备id")
    private String deviceId;

    private SdDevices sdDevices;

    /** 触发类型（0:状态触发,1：数据触发） */
    @Excel(name = "触发类型", readConverterExp = "0=:状态触发,1：数据触发")
    private String triggerType;

    /** 触发元素id(状态id;数据项id;) */
    @Excel(name = "触发元素id(状态id;数据项id;)")
    private Long elementId;

    private SdDeviceTypeItem sdDeviceTypeItem;

    /** 比较模式(0:>；1:>=；2:<；3:<=；4:==；5:!=；6:in；7:between；) */
    @Excel(name = "比较模式(0:>；1:>=；2:<；3:<=；4:==；5:!=；6:in；7:between；)")
    private String comparePattern;

    /** 比较数值（如果是in，则数值是一个数组，例如[1,2,3,4],如果是between，也是一个数组，例如[-10,10],其他情况都是数字） */
    @Excel(name = "比较数值", readConverterExp = "如=果是in，则数值是一个数组，例如[1,2,3,4],如果是between，也是一个数组，例如[-10,10],其他情况都是数字")
    private String compareValue;

    private String warningType;

    public String getWarningType() {
        return warningType;
    }

    public void setWarningType(String warningType) {
        this.warningType = warningType;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setRelateId(Long relateId) 
    {
        this.relateId = relateId;
    }

    public Long getRelateId() 
    {
        return relateId;
    }
    public void setDeviceTypeId(Long deviceTypeId) 
    {
        this.deviceTypeId = deviceTypeId;
    }

    public Long getDeviceTypeId() 
    {
        return deviceTypeId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setTriggerType(String triggerType)
    {
        this.triggerType = triggerType;
    }

    public String getTriggerType() 
    {
        return triggerType;
    }
    public void setElementId(Long elementId) 
    {
        this.elementId = elementId;
    }

    public Long getElementId() 
    {
        return elementId;
    }
    public void setComparePattern(String comparePattern) 
    {
        this.comparePattern = comparePattern;
    }

    public String getComparePattern() 
    {
        return comparePattern;
    }
    public void setCompareValue(String compareValue) 
    {
        this.compareValue = compareValue;
    }

    public String getCompareValue() 
    {
        return compareValue;
    }

    public SdDevices getSdDevices() {
        return sdDevices;
    }

    public void setSdDevices(SdDevices sdDevices) {
        this.sdDevices = sdDevices;
    }

    public SdDeviceTypeItem getSdDeviceTypeItem() {
        return sdDeviceTypeItem;
    }

    public void setSdDeviceTypeItem(SdDeviceTypeItem sdDeviceTypeItem) {
        this.sdDeviceTypeItem = sdDeviceTypeItem;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("relateId", getRelateId())
            .append("deviceTypeId", getDeviceTypeId())
            .append("triggerType", getTriggerType())
            .append("elementId", getElementId())
            .append("comparePattern", getComparePattern())
            .append("compareValue", getCompareValue())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
