package com.tunnel.business.domain.dataInfo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 巡检任务文件对象 sd_inspection_file
 *
 * @author ruoyi
 * @date 2022-02-25
 */
public class SdInspectionFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 巡检任务文件关联ID */
    @Excel(name = "巡检任务文件关联ID")
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
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
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
