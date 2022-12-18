package com.ruoyi.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 机构对象 sys_dept_sg(对应集团部门表)
 * 
 * @author ruoyi
 * @date 2022-12-02
 */
public class SysDeptSg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 机构主键 */
    private String id;

    /** 父节点代码 */
    @Excel(name = "父节点代码")
    private String pid;

    /** 编码 */
    @Excel(name = "编码")
    private String code;

    /** 父节点编码 */
    @Excel(name = "父节点编码")
    private String pCode;

    /** 所有上级ID，用逗号分开 */
    @Excel(name = "所有上级ID，用逗号分开")
    private String pids;

    /** 机构名称 */
    @Excel(name = "机构名称")
    private String name;

    /** 类别 */
    @Excel(name = "类别")
    private String type;

    /** 排序字段 */
    @Excel(name = "排序字段")
    private Long sort;

    /** 机构简拼 */
    @Excel(name = "机构简拼")
    private String spell;

    /** 是否有叶子 */
    @Excel(name = "是否有叶子")
    private Integer hasLeaf;

    /** 属性(公司 中心 站)等 */
    @Excel(name = "属性(公司 中心 站)等")
    private String property;

    /** 全路径名称 */
    @Excel(name = "全路径名称")
    private String fullName;

    /** 创建人 */
    @Excel(name = "创建人")
    private String creator;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    /** 修改人 */
    @Excel(name = "修改人")
    private String updater;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateDate;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setPid(String pid) 
    {
        this.pid = pid;
    }

    public String getPid() 
    {
        return pid;
    }
    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }
    public void setpCode(String pCode) 
    {
        this.pCode = pCode;
    }

    public String getpCode() 
    {
        return pCode;
    }
    public void setPids(String pids) 
    {
        this.pids = pids;
    }

    public String getPids() 
    {
        return pids;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }
    public void setSpell(String spell) 
    {
        this.spell = spell;
    }

    public String getSpell() 
    {
        return spell;
    }
    public void setHasLeaf(Integer hasLeaf) 
    {
        this.hasLeaf = hasLeaf;
    }

    public Integer getHasLeaf() 
    {
        return hasLeaf;
    }
    public void setProperty(String property) 
    {
        this.property = property;
    }

    public String getProperty() 
    {
        return property;
    }
    public void setFullName(String fullName) 
    {
        this.fullName = fullName;
    }

    public String getFullName() 
    {
        return fullName;
    }
    public void setCreator(String creator) 
    {
        this.creator = creator;
    }

    public String getCreator() 
    {
        return creator;
    }
    public void setCreateDate(Date createDate) 
    {
        this.createDate = createDate;
    }

    public Date getCreateDate() 
    {
        return createDate;
    }
    public void setUpdater(String updater) 
    {
        this.updater = updater;
    }

    public String getUpdater() 
    {
        return updater;
    }
    public void setUpdateDate(Date updateDate) 
    {
        this.updateDate = updateDate;
    }

    public Date getUpdateDate() 
    {
        return updateDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("pid", getPid())
            .append("code", getCode())
            .append("pCode", getpCode())
            .append("pids", getPids())
            .append("name", getName())
            .append("type", getType())
            .append("sort", getSort())
            .append("spell", getSpell())
            .append("hasLeaf", getHasLeaf())
            .append("property", getProperty())
            .append("fullName", getFullName())
            .append("creator", getCreator())
            .append("createDate", getCreateDate())
            .append("updater", getUpdater())
            .append("updateDate", getUpdateDate())
            .toString();
    }
}
