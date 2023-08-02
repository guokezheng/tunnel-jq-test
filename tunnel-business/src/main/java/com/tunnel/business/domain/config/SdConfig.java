package com.tunnel.business.domain.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 数字孪生页面配置对象 sd_config
 * 
 * @author ruoyi
 * @date 2023-04-10
 */
public class SdConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 所属部门名称 */
    @Excel(name = "所属部门")
    private String deptId;


    @Excel(name = "所属模块", dictType = "sd_config_module")
    private String configModule;

    /** 页面名称 */
    @Excel(name = "页面名称")
    private String name;


    /** 页面编码 */
    @Excel(name = "页面标识符")
    private String code;

    /** 页面地址 */
    @Excel(name = "页面路径")
    private String url;


    public String getConfigModule() {
        return configModule;
    }

    public void setConfigModule(String configModule) {
        this.configModule = configModule;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDeptId(String deptId) 
    {
        this.deptId = deptId;
    }

    public String getDeptId() 
    {
        return deptId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deptId", getDeptId())
            .append("name", getName())
            .append("code", getCode())
            .append("url", getUrl())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
