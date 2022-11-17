package com.ruoyi.common.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysMenu;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Treeselect树结构实体类
 * 
 */
public class DeptTunnelTreeSelect implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private String id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DeptTunnelTreeSelect> children;

    public DeptTunnelTreeSelect()
    {

    }

    public DeptTunnelTreeSelect(SysDeptTunnel dept)
    {
        this.id = dept.getDeptId();
        this.label = dept.getDeptName();
        this.children = dept.getChildren().stream().map(DeptTunnelTreeSelect::new).collect(Collectors.toList());
    }


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public List<DeptTunnelTreeSelect> getChildren()
    {
        return children;
    }

    public void setChildren(List<DeptTunnelTreeSelect> children)
    {
        this.children = children;
    }
}
