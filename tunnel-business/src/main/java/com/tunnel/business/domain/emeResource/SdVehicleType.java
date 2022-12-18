package com.tunnel.business.domain.emeResource;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车辆类型配置对象 sd_vehicle_type
 * 
 * @author ruoyi
 * @date 2022-12-01
 */
public class SdVehicleType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 车辆类型编码 */
    @Excel(name = "车辆类型编码")
    private String vehicleTypeCode;

    /** 车辆类型名称 */
    @Excel(name = "车辆类型名称")
    private String vehicleTypeName;

    /** 是否是重点车辆 0:否 1:是  默认为0 */
    @Excel(name = "重点车辆")
    private String iskeyVehicle;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setVehicleTypeCode(String vehicleTypeCode) 
    {
        this.vehicleTypeCode = vehicleTypeCode;
    }

    public String getVehicleTypeCode() 
    {
        return vehicleTypeCode;
    }
    public void setVehicleTypeName(String vehicleTypeName) 
    {
        this.vehicleTypeName = vehicleTypeName;
    }

    public String getVehicleTypeName() 
    {
        return vehicleTypeName;
    }
    public void setIskeyVehicle(String iskeyVehicle)
    {
        this.iskeyVehicle = iskeyVehicle;
    }

    public String getIskeyVehicle()
    {
        return iskeyVehicle;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("vehicleTypeCode", getVehicleTypeCode())
            .append("vehicleTypeName", getVehicleTypeName())
            .append("iskeyVehicle", getIskeyVehicle())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
