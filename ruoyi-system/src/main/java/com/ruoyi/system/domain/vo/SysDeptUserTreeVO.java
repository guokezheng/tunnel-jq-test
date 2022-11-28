package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * 部门用户树
 * lhd
 */
public class SysDeptUserTreeVO {

    /** 主键 **/
    private String id;

    /** （组织/用户）名称 **/
    private String name;

    @JsonIgnore
    /** 是否用户 **/
    private Integer isUser;

    /** 是否显示多选 **/
    private  Boolean showCheckbox;

    /** 下级 **/
    private List<SysDeptUserTreeVO> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsUser() {
        return isUser;
    }

    public void setIsUser(Integer isUser) {
        this.isUser = isUser;
    }

    public List<SysDeptUserTreeVO> getChildren() {
        return children;
    }

    public void setChildren(List<SysDeptUserTreeVO> children) {
        this.children = children;
    }

    public Boolean getShowCheckBox() {
        return showCheckbox;
    }

    public void setShowCheckbox(Boolean showCheckbox)
    {
        this.showCheckbox = showCheckbox;
    }
    @Override
    public String toString() {
        return "SysDeptUserTreeVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isUser=" + isUser +
                ", children=" + children +
                ", showCheckbox=" + showCheckbox +
                '}';
    }
}
