package com.tunnel.platform.domain.dataInfo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备控制状态详情关联对象 inductionlamp_control_status_details
 *
 * @author ruoyi
 * @date 2022-08-30
 */
public class InductionlampControlStatusDetails extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 设备id */
    @Excel(name = "设备id")
    private String equipmentId;

    /** 设备模式类型 */
    @Excel(name = "设备模式类型")
    private Integer equipmentModeType;

    private String brightness;

    private String frequency;

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getBrightness() {
        return brightness;
    }

    public void setBrightness(String brightness) {
        this.brightness = brightness;
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
    public void setEquipmentModeType(Integer equipmentModeType)
    {
        this.equipmentModeType = equipmentModeType;
    }

    public Integer getEquipmentModeType()
    {
        return equipmentModeType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("equipmentId", getEquipmentId())
            .append("equipmentModeType", getEquipmentModeType())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
