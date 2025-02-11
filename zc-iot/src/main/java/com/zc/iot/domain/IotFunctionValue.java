package com.zc.iot.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 功能值定义对象 athena_iot_function_value
 *
 * @author YangChao
 * @date 2021-10-27
 */
@ApiModel(value = "IotFunctionValue", description = "功能值定义对象")
public class IotFunctionValue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @ApiModelProperty("主键")
    private Long id;

    /** 设备功能 */
    @ApiModelProperty("设备功能")
    private Long functionKey;

    /** 设备功能名称 */
    @ApiModelProperty("设备功能名称")
    @Excel(name = "设备功能名称")
    private String functionName;

    /** 功能值 */
    @ApiModelProperty("功能值")
    @Excel(name = "功能值")
    private String value;

    /** 名称 */
    @ApiModelProperty("名称")
    @Excel(name = "名称")
    private String name;

    /** 备注 */
    @ApiModelProperty("备注")
    @Excel(name = "备注")
    private String comments;

    private List<IotFunctionValue> saveFvDataList;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setFunctionKey(Long functionKey)
    {
        this.functionKey = functionKey;
    }

    public Long getFunctionKey()
    {
        return functionKey;
    }
    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setComments(String comments)
    {
        this.comments = comments;
    }

    public String getComments()
    {
        return comments;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public List<IotFunctionValue> getSaveFvDataList() {
        return saveFvDataList;
    }

    public void setSaveFvDataList(List<IotFunctionValue> saveFvDataList) {
        this.saveFvDataList = saveFvDataList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("functionKey", getFunctionKey())
            .append("value", getValue())
            .append("name", getName())
            .append("comments", getComments())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
