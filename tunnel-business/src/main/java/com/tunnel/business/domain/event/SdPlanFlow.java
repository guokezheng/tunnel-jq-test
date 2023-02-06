package com.tunnel.business.domain.event;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 事件环节对象 sd_plan_flow
 * 
 * @author ruoyi
 * @date 2022-12-12
 */
public class SdPlanFlow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** pid父级 */
    @Excel(name = "pid父级")
    private Long pid;

    /** 环节名称 */
    @Excel(name = "环节名称")
    private String flowName;

    /** 排序 */
    @Excel(name = "排序")
    private String sort;

    private String label;

    /**
     * 子路由
     */
    private List<SdPlanFlow> children;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<SdPlanFlow> getChildren() {
        return children;
    }

    public void setChildren(List<SdPlanFlow> children) {
        this.children = children;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPid(Long pid) 
    {
        this.pid = pid;
    }

    public Long getPid() 
    {
        return pid;
    }
    public void setFlowName(String flowName) 
    {
        this.flowName = flowName;
    }

    public String getFlowName() 
    {
        return flowName;
    }
    public void setSort(String sort) 
    {
        this.sort = sort;
    }

    public String getSort() 
    {
        return sort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("pid", getPid())
            .append("flowName", getFlowName())
            .append("sort", getSort())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
