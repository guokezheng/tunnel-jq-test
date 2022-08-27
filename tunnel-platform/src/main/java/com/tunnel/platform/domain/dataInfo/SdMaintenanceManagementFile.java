package com.tunnel.platform.domain.dataInfo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 养护文件对象 sd_maintenance_management_file
 * 
 * @author ruoyi
 * @date 2022-02-15
 */
public class SdMaintenanceManagementFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 养护文件关联 */
    @Excel(name = "养护文件关联")
    private String planfileid;

    /** 文件地址 */
    @Excel(name = "文件地址")
    private String url;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String filename;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPlanfileid(String planfileid) 
    {
        this.planfileid = planfileid;
    }

    public String getPlanfileid() 
    {
        return planfileid;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }
    public void setFilename(String filename) 
    {
        this.filename = filename;
    }

    public String getFilename() 
    {
        return filename;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("planfileid", getPlanfileid())
            .append("url", getUrl())
            .append("filename", getFilename())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
