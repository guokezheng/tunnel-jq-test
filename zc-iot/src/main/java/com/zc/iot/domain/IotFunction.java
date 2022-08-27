package com.zc.iot.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 功能定义对象 athena_iot_function
 * 
 * @author YangChao
 * @date 2021-10-27
 */
public class IotFunction extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**
     * 可读可写的
     */
    public static final Long READABLE_WRITABLE = 1L;
    /**
     * 可读的
     */
    public static final Long READABLE = 2L;
    /**
     * 可写的
     */
    public static final Long WRITABLE = 3L;


    /** $column.columnComment */
    private Long id;

    /** 所属品类 */
    private Long categoryKey;

    /** 品类名称 */
    @Excel(name = "所属品类")
    private String categoryName;

    /** 功能类型 */
    @Excel(name = "功能类型")
    private Long functionType;

    /** 功能名称 */
    @Excel(name = "功能名称")
    private String name;

    /** 标识符 */
    @Excel(name = "标识符")
    private String identifier;

    /** 数据类型 */
    @Excel(name = "数据类型")
    private Long dataType;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

    /** 取值范围（上限） */
    @Excel(name = "取值范围", readConverterExp = "上=限")
    private String valueRangeUpper;

    /** 取值范围（下限） */
    @Excel(name = "取值范围", readConverterExp = "下=限")
    private String valueRangeFloor;

    /** 读写类型 */
    @Excel(name = "读写类型")
    private Long readWriteType;

    /** 实时值 **/
    private Number realValue;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCategoryKey(Long categoryKey) 
    {
        this.categoryKey = categoryKey;
    }

    public Long getCategoryKey() 
    {
        return categoryKey;
    }
    public void setFunctionType(Long functionType)
    {
        this.functionType = functionType;
    }

    public Long getFunctionType()
    {
        return functionType;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setIdentifier(String identifier) 
    {
        this.identifier = identifier;
    }

    public String getIdentifier() 
    {
        return identifier;
    }
    public void setDataType(Long dataType)
    {
        this.dataType = dataType;
    }

    public Long getDataType()
    {
        return dataType;
    }
    public void setUnit(String unit) 
    {
        this.unit = unit;
    }

    public String getUnit() 
    {
        return unit;
    }
    public void setValueRangeUpper(String valueRangeUpper) 
    {
        this.valueRangeUpper = valueRangeUpper;
    }

    public String getValueRangeUpper() 
    {
        return valueRangeUpper;
    }
    public void setValueRangeFloor(String valueRangeFloor) 
    {
        this.valueRangeFloor = valueRangeFloor;
    }

    public String getValueRangeFloor() 
    {
        return valueRangeFloor;
    }
    public void setReadWriteType(Long readWriteType)
    {
        this.readWriteType = readWriteType;
    }

    public Long getReadWriteType()
    {
        return readWriteType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Number getRealValue() {
        return realValue;
    }

    public void setRealValue(Number realValue) {
        this.realValue = realValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("categoryKey", getCategoryKey())
            .append("functionType", getFunctionType())
            .append("name", getName())
            .append("identifier", getIdentifier())
            .append("dataType", getDataType())
            .append("unit", getUnit())
            .append("valueRangeUpper", getValueRangeUpper())
            .append("valueRangeFloor", getValueRangeFloor())
            .append("readWriteType", getReadWriteType())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
