package com.ruoyi.common.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门表 sys_dept
 *
 */
@ApiModel("部门实体")
public class SysDeptTunnel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 部门ID */
    @ApiModelProperty("部门id")
    private String deptId;

    /** 父部门ID */
    @ApiModelProperty("父部门ID")
    private String parentId;


    /** 部门名称 */
    @ApiModelProperty("部门名称")
    private String deptName;

    /** 父部门名称 */
    @ApiModelProperty("父部门名称")
    private String parentName;

    /** 子部门 */
    @ApiModelProperty("子部门")
    private List<SysDeptTunnel> children = new ArrayList<SysDeptTunnel>();

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId(final String deptId) {
        this.deptId = deptId;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(final String parentId) {
        this.parentId = parentId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public void setDeptName(final String deptName) {
        this.deptName = deptName;
    }

    public String getParentName() {
        return this.parentName;
    }

    public void setParentName(final String parentName) {
        this.parentName = parentName;
    }

    public List<SysDeptTunnel> getChildren()
    {
        return children;
    }

    public void setChildren(List<SysDeptTunnel> children)
    {
        this.children = children;
    }

}
