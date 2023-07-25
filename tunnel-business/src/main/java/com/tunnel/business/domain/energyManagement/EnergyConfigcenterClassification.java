package com.tunnel.business.domain.energyManagement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 分类对象 energy_configcenter_classification
 *
 * @author ruoyi
 * @date 2023-07-14
 */
public class EnergyConfigcenterClassification extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String classificationName;

    /** 分类编号 */
    @Excel(name = "分类编号")
    private String classificationCode;

    /** 父编号 */
    @Excel(name = "父编号")
    private String parentCode;

    /** 1 能耗 2 负载 */
    @Excel(name = "1 能耗 2 负载")
    private String type;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setClassificationName(String classificationName)
    {
        this.classificationName = classificationName;
    }

    public String getClassificationName()
    {
        return classificationName;
    }
    public void setClassificationCode(String classificationCode)
    {
        this.classificationCode = classificationCode;
    }

    public String getClassificationCode()
    {
        return classificationCode;
    }
    public void setParentCode(String parentCode)
    {
        this.parentCode = parentCode;
    }

    public String getParentCode()
    {
        return parentCode;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("classificationName", getClassificationName())
                .append("classificationCode", getClassificationCode())
                .append("parentCode", getParentCode())
                .append("type", getType())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
