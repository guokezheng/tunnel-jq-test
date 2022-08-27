package com.tunnel.platform.domain.dataInfo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车辆管理对象 sd_vehicle_management
 * 
 * @author ruoyi
 * @date 2022-02-18
 */
public class SdVehicleManagement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

    /** 车辆类型id */
    @Excel(name = "车辆类型id")
    private String vehicleTypeId;

    /** 方向（0：上行；1下行） */
    @Excel(name = "方向", readConverterExp = "0=：上行；1下行")
    private String direction;

    /** 标记(0：未标记；1：以标记) */
    @Excel(name = "标记(0：未标记；1：以标记)")
    private String mark;

    /** 驾驶员 */
    @Excel(name = "驾驶员")
    private String driver;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String carNumber;

    /** 进入通道时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "进入通道时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date accessTime;

    /** 备注 */
    @Excel(name = "备注")
    private String remake;

    /** 备注1 */
    @Excel(name = "备注1")
    private String remake1;

    /** 备注2 */
    @Excel(name = "备注2")
    private String remake2;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTunnelId(String tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId() 
    {
        return tunnelId;
    }
    public void setVehicleTypeId(String vehicleTypeId) 
    {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getVehicleTypeId() 
    {
        return vehicleTypeId;
    }
    public void setDirection(String direction) 
    {
        this.direction = direction;
    }

    public String getDirection() 
    {
        return direction;
    }
    public void setMark(String mark) 
    {
        this.mark = mark;
    }

    public String getMark() 
    {
        return mark;
    }
    public void setDriver(String driver) 
    {
        this.driver = driver;
    }

    public String getDriver() 
    {
        return driver;
    }
    public void setCarNumber(String carNumber) 
    {
        this.carNumber = carNumber;
    }

    public String getCarNumber() 
    {
        return carNumber;
    }
    public void setAccessTime(Date accessTime) 
    {
        this.accessTime = accessTime;
    }

    public Date getAccessTime() 
    {
        return accessTime;
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
            .append("tunnelId", getTunnelId())
            .append("vehicleTypeId", getVehicleTypeId())
            .append("direction", getDirection())
            .append("mark", getMark())
            .append("driver", getDriver())
            .append("carNumber", getCarNumber())
            .append("accessTime", getAccessTime())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("remake", getRemake())
            .append("remake1", getRemake1())
            .append("remake2", getRemake2())
            .toString();
    }
}
