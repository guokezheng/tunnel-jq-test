package com.zc.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 品类对象 athena_iot_category
 * 
 * @author YangChao
 * @date 2021-10-29
 */
public class IotCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 品类名称 */
    @Excel(name = "品类名称")
    private String categoryName;

    /** 领域id */
    @Excel(name = "领域id")
    private Long field;

    /** 场景id */
    @Excel(name = "场景id")
    private Long sceneId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCategoryName(String categoryName) 
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName() 
    {
        return categoryName;
    }
    public void setField(Long field) 
    {
        this.field = field;
    }

    public Long getField() 
    {
        return field;
    }
    public void setSceneId(Long sceneId) 
    {
        this.sceneId = sceneId;
    }

    public Long getSceneId() 
    {
        return sceneId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("categoryName", getCategoryName())
            .append("field", getField())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("sceneId", getSceneId())
            .toString();
    }
}
