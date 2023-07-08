package com.ruoyi.common.core.domain;

import java.util.List;

/**
 * 设备类型对象 sd_equipment_category
 *
 * @author ruoyi
 * @date 2023-02-06
 */
public class SdEquipmentCategoryDto extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 类型ID
     */
    private String id;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 是否可控：1：是 0：否
     */
    private String isControl;

    /**
     * 父类型ID
     */
    private String parentId;
    /**
     * 父类型名称
     */
    private String parentName;

    private List<SdEquipmentCategoryDto> children;

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

    public String getIsControl() {
        return isControl;
    }

    public void setIsControl(String isControl) {
        this.isControl = isControl;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<SdEquipmentCategoryDto> getChildren() {
        return children;
    }

    public void setChildren(List<SdEquipmentCategoryDto> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "SdEquipmentCategoryDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isControl='" + isControl + '\'' +
                ", parentId=" + parentId +
                ", parentName='" + parentName + '\'' +
                ", children=" + children +
                '}';
    }
}
