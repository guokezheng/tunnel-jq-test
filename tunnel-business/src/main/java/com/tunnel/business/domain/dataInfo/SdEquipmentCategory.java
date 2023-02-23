package com.tunnel.business.domain.dataInfo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备类型对象 sd_equipment_category
 * 
 * @author ruoyi
 * @date 2023-02-06
 */
public class SdEquipmentCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备类型ID */
    private Long id;

    /** 设备类型名称 */
    @Excel(name = "设备类型名称")
    private String name;

    /** 改为是否显示/是否可控：1：是 0：否(用于预案中展示) */
    @Excel(name = "是否可控：1：是 0：否")
    private String isControl;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setIsControl(String isControl)
    {
        this.isControl = isControl;
    }

    public String getIsControl() 
    {
        return isControl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("isControl", getIsControl())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
