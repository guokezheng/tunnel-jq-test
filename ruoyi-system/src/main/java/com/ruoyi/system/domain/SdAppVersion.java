package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * app版本管理对象 sd_app_version
 * 
 * @author guoz
 * @date 2023-08-08
 */
public class SdAppVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** AppId */
    @Excel(name = "AppId")
    private String appId;

    /** 应用名称 */
    @Excel(name = "应用名称")
    private String appName;

    /** 版本号 */
    @Excel(name = "版本号")
    private String editionNumber;

    /** 版本名称 */
    @Excel(name = "版本名称")
    private String editionName;

    /** 系统类型 */
    @Excel(name = "系统类型")
    private Integer sysType;

    /** 安装包类型 */
    @Excel(name = "安装包类型")
    private Integer packageType;

    /** 是否发行 */
    @Excel(name = "是否发行")
    private Integer editionIssue;

    /** 静默更新 */
    @Excel(name = "静默更新")
    private Integer editionSilence;

    /** 强制更新 */
    @Excel(name = "强制更新")
    private Integer editionForce;

    /** 下载地址 */
    private String editionUrl;

    /** 更新内容 */
    private String describe;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public String getAppName() 
    {
        return appName;
    }


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getEditionNumber() {
        return editionNumber;
    }

    public void setEditionNumber(String editionNumber) {
        this.editionNumber = editionNumber;
    }

    public void setEditionName(String editionName)
    {
        this.editionName = editionName;
    }

    public String getEditionName() 
    {
        return editionName;
    }
    public void setSysType(Integer sysType) 
    {
        this.sysType = sysType;
    }

    public Integer getSysType() 
    {
        return sysType;
    }
    public void setPackageType(Integer packageType) 
    {
        this.packageType = packageType;
    }

    public Integer getPackageType() 
    {
        return packageType;
    }
    public void setEditionIssue(Integer editionIssue) 
    {
        this.editionIssue = editionIssue;
    }

    public Integer getEditionIssue() 
    {
        return editionIssue;
    }
    public void setEditionSilence(Integer editionSilence) 
    {
        this.editionSilence = editionSilence;
    }

    public Integer getEditionSilence() 
    {
        return editionSilence;
    }
    public void setEditionForce(Integer editionForce) 
    {
        this.editionForce = editionForce;
    }

    public Integer getEditionForce() 
    {
        return editionForce;
    }
    public void setEditionUrl(String editionUrl) 
    {
        this.editionUrl = editionUrl;
    }

    public String getEditionUrl() 
    {
        return editionUrl;
    }
    public void setDescribe(String describe) 
    {
        this.describe = describe;
    }

    public String getDescribe() 
    {
        return describe;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("appId", getAppId())
            .append("appName", getAppName())
            .append("editionNumber", getEditionNumber())
            .append("editionName", getEditionName())
            .append("sysType", getSysType())
            .append("packageType", getPackageType())
            .append("editionIssue", getEditionIssue())
            .append("editionSilence", getEditionSilence())
            .append("editionForce", getEditionForce())
            .append("editionUrl", getEditionUrl())
            .append("describe", getDescribe())
            .toString();
    }
}
