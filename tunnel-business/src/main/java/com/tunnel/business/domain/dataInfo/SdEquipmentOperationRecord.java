package com.tunnel.business.domain.dataInfo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备运行记录对象 sd_equipment_operation_record
 * 
 * @author ruoyi
 * @date 2022-01-21
 */
public class SdEquipmentOperationRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String equipmentNumber;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String equipmentName;

    /** 品牌 */
    @Excel(name = "品牌")
    private String brand;

    /** 型号 */
    @Excel(name = "型号")
    private String model;

    /** 所属隧道id */
    @Excel(name = "所属隧道id")
    private String tunnelId;

    /** 设备状态(0：正常、1：故障) */
    @Excel(name = "设备状态")
    private String equipmentStatus;

    /** 采集时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "采集时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date acquisitionTime;

    /** 电流 */
    @Excel(name = "电流")
    private String electricCurrent;

    /** 频率 */
    @Excel(name = "频率")
    private String frequency;

    /** 备注 */
    @Excel(name = "备注")
    private String remake;

    /** 备注1 */
    @Excel(name = "备注1")
    private String remake1;

    /** 备注2 */
    @Excel(name = "备注2")
    private String remake2;

    private String tunnelName;

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setEquipmentNumber(String equipmentNumber) 
    {
        this.equipmentNumber = equipmentNumber;
    }

    public String getEquipmentNumber() 
    {
        return equipmentNumber;
    }
    public void setEquipmentName(String equipmentName) 
    {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentName() 
    {
        return equipmentName;
    }
    public void setBrand(String brand) 
    {
        this.brand = brand;
    }

    public String getBrand() 
    {
        return brand;
    }
    public void setModel(String model) 
    {
        this.model = model;
    }

    public String getModel() 
    {
        return model;
    }
    public void setTunnelId(String tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId() 
    {
        return tunnelId;
    }
    public void setEquipmentStatus(String equipmentStatus) 
    {
        this.equipmentStatus = equipmentStatus;
    }

    public String getEquipmentStatus() 
    {
        return equipmentStatus;
    }
    public void setAcquisitionTime(Date acquisitionTime) 
    {
        this.acquisitionTime = acquisitionTime;
    }

    public Date getAcquisitionTime() 
    {
        return acquisitionTime;
    }
    public void setElectricCurrent(String electricCurrent) 
    {
        this.electricCurrent = electricCurrent;
    }

    public String getElectricCurrent() 
    {
        return electricCurrent;
    }
    public void setFrequency(String frequency) 
    {
        this.frequency = frequency;
    }

    public String getFrequency() 
    {
        return frequency;
    }
    public void setRemake(String remake) 
    {
        this.remake = remake;
    }

    public String getRemake() 
    {
        return remake;
    }
    public void setRemake1(String remake1) 
    {
        this.remake1 = remake1;
    }

    public String getRemake1() 
    {
        return remake1;
    }
    public void setRemake2(String remake2) 
    {
        this.remake2 = remake2;
    }

    public String getRemake2() 
    {
        return remake2;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("equipmentNumber", getEquipmentNumber())
            .append("equipmentName", getEquipmentName())
            .append("brand", getBrand())
            .append("model", getModel())
            .append("tunnelId", getTunnelId())
            .append("equipmentStatus", getEquipmentStatus())
            .append("acquisitionTime", getAcquisitionTime())
            .append("electricCurrent", getElectricCurrent())
            .append("frequency", getFrequency())
            .append("updateTime", getUpdateTime())
            .append("remake", getRemake())
            .append("remake1", getRemake1())
            .append("remake2", getRemake2())
            .append("tunnelName", getTunnelName())
            .toString();
    }
}
