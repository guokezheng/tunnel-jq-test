package com.tunnel.business.domain.vehicle;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 两客一危车辆数据对象 sd_kvcar
 * 
 * @author ruoyi
 * @date 2023-12-14
 */
public class SdKvcar extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /**
     * 隧道id
     */
    @Excel(name = "隧道id")
    public String tunnelId;

    /**
     * 方向
     */
    @Excel(name = "方向")
    public String direction;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String plate;

    /** 车辆颜色 */
    @Excel(name = "车辆颜色")
    private String carColor;

    /** 车辆类型 */
    @Excel(name = "车辆类型")
    private String carType;

    /** 速度 */
    @Excel(name = "速度")
    private Integer speed;

    /** 进入时间 */
    @Excel(name = "进入时间")
    private String inTime;

    /** 离开时间 */
    @Excel(name = "离开时间")
    private String outTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPlate(String plate) 
    {
        this.plate = plate;
    }

    public String getPlate() 
    {
        return plate;
    }
    public void setCarColor(String carColor) 
    {
        this.carColor = carColor;
    }

    public String getCarColor() 
    {
        return carColor;
    }
    public void setCarType(String carType) 
    {
        this.carType = carType;
    }

    public String getCarType() 
    {
        return carType;
    }
    public void setSpeed(Integer speed) 
    {
        this.speed = speed;
    }

    public Integer getSpeed() 
    {
        return speed;
    }
    public void setInTime(String inTime) 
    {
        this.inTime = inTime;
    }

    public String getInTime() 
    {
        return inTime;
    }
    public void setOutTime(String outTime) 
    {
        this.outTime = outTime;
    }

    public String getOutTime() 
    {
        return outTime;
    }

    public String getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(String tunnelId) {
        this.tunnelId = tunnelId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tunnelId", getTunnelId())
            .append("direction", getDirection())
            .append("plate", getPlate())
            .append("carColor", getCarColor())
            .append("carType", getCarType())
            .append("speed", getSpeed())
            .append("inTime", getInTime())
            .append("outTime", getOutTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
