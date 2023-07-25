package com.tunnel.business.domain.energyManagement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 分项对象 energy_configcenter_itemized
 *
 * @author ruoyi
 * @date 2023-07-14
 */
public class EnergyConfigcenterItemized extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 分项名称 */
    @Excel(name = "分项名称")
    private String itemizedName;

    /** 分项编号 */
    @Excel(name = "分项编号")
    private String itemizedCode;

    /** 父编号 */
    @Excel(name = "父编号")
    private String parentCode;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setItemizedName(String itemizedName)
    {
        this.itemizedName = itemizedName;
    }

    public String getItemizedName()
    {
        return itemizedName;
    }
    public void setItemizedCode(String itemizedCode)
    {
        this.itemizedCode = itemizedCode;
    }

    public String getItemizedCode()
    {
        return itemizedCode;
    }
    public void setParentCode(String parentCode)
    {
        this.parentCode = parentCode;
    }

    public String getParentCode()
    {
        return parentCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("itemizedName", getItemizedName())
                .append("itemizedCode", getItemizedCode())
                .append("parentCode", getParentCode())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
