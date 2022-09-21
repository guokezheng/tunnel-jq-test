package com.tunnel.business.domain.event;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 预案文件对象 sd_reserve_plan_file
 * 
 * @author gongfanfei
 * @date 2020-09-10
 */
public class SdReservePlanFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 预案文件关联 */
    @Excel(name = "预案文件关联")
    private String planFileId;

    /** 文件地址 */
    @Excel(name = "文件地址")
    private String url;
    
    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPlanFileId(String planFileId) 
    {
        this.planFileId = planFileId;
    }

    public String getPlanFileId() 
    {
        return planFileId;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }

    public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("planFileId", getPlanFileId())
            .append("url", getUrl())
            .append("fileName", getFileName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}