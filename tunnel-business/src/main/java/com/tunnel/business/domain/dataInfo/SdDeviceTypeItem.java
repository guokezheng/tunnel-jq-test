package com.tunnel.business.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备类型数据项对象 sd_device_type_item
 * 
 * @author ruoyi
 * @date 2022-09-01
 */
public class SdDeviceTypeItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 数据项编号 */
    @Excel(name = "数据项编号")
    private String itemCode;

    /** 数据项名称 */
    @Excel(name = "数据项名称")
    private String itemName;

    /** 设备类型id */
    private Long deviceTypeId;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

    @Excel(name = "设备类型名称")
    public String typeName;

    @Override
    public String toString() {
        return "SdDeviceTypeItem{" +
                "id=" + id +
                ", itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", deviceTypeId=" + deviceTypeId +
                ", unit='" + unit + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setItemCode(String itemCode) 
    {
        this.itemCode = itemCode;
    }

    public String getItemCode() 
    {
        return itemCode;
    }
    public void setItemName(String itemName) 
    {
        this.itemName = itemName;
    }

    public String getItemName() 
    {
        return itemName;
    }
    public void setDeviceTypeId(Long deviceTypeId) 
    {
        this.deviceTypeId = deviceTypeId;
    }

    public Long getDeviceTypeId() 
    {
        return deviceTypeId;
    }
    public void setUnit(String unit) 
    {
        this.unit = unit;
    }

    public String getUnit() 
    {
        return unit;
    }

}
