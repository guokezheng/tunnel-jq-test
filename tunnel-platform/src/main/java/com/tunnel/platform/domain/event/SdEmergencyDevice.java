package com.tunnel.platform.domain.event;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 应急物资信息对象 sd_emergency_device
 * 
 * @author ruoyi
 * @date 2021-05-12
 */
public class SdEmergencyDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 隧道ID */
    @Excel(name = "隧道ID")
    private String tunnelId;

    private String tunnelName;
    /** 方向 */
    @Excel(name = "方向")
    private String direction;

    /** 桩号 */
    @Excel(name = "桩号")
    private String mileage;

    /** 灭火器 */
    @Excel(name = "灭火器")
    private Integer eqFire;

    /** 消火栓 */
    @Excel(name = "消火栓")
    private Integer eqFireHydrant;

    /** 泡沫枪 */
    @Excel(name = "泡沫枪")
    private Integer eqFoam;

    /** 创建者 */
    private String create_by;

    /** 更新时间 */
    private String update_time;

    /** 创建时间 */
    private String create_time;

    /** 更新者 */
    private String update_by;

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
    public void setDirection(String direction) 
    {
        this.direction = direction;
    }

    public String getDirection() 
    {
        return direction;
    }
    public void setMileage(String mileage) 
    {
        this.mileage = mileage;
    }

    public String getMileage() 
    {
        return mileage;
    }
    public void setEqFire(Integer eqFire) 
    {
        this.eqFire = eqFire;
    }

    public Integer getEqFire() 
    {
        return eqFire;
    }
    public void setEqFireHydrant(Integer eqFireHydrant) 
    {
        this.eqFireHydrant = eqFireHydrant;
    }

    public Integer getEqFireHydrant() 
    {
        return eqFireHydrant;
    }
    public void setEqFoam(Integer eqFoam) 
    {
        this.eqFoam = eqFoam;
    }

    public Integer getEqFoam() 
    {
        return eqFoam;
    }
    public void setCreate_by(String create_by) 
    {
        this.create_by = create_by;
    }

    public String getCreate_by() 
    {
        return create_by;
    }
    public void setUpdate_time(String update_time) 
    {
        this.update_time = update_time;
    }

    public String getUpdate_time() 
    {
        return update_time;
    }
    public void setCreate_time(String create_time) 
    {
        this.create_time = create_time;
    }

    public String getCreate_time() 
    {
        return create_time;
    }
    public void setUpdate_by(String update_by) 
    {
        this.update_by = update_by;
    }

    public String getUpdate_by() 
    {
        return update_by;
    }

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tunnelId", getTunnelId())
            .append("direction", getDirection())
            .append("mileage", getMileage())
            .append("eqFire", getEqFire())
            .append("eqFireHydrant", getEqFireHydrant())
            .append("eqFoam", getEqFoam())
            .append("remark", getRemark())
            .append("create_by", getCreate_by())
            .append("update_time", getUpdate_time())
            .append("create_time", getCreate_time())
            .append("update_by", getUpdate_by())
            .toString();
    }


}
