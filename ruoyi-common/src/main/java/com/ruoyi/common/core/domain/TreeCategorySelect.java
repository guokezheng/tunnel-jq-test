package com.ruoyi.common.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Treeselect树结构实体类
 * 针对节点ID为String类型的实体类
 */
public class TreeCategorySelect implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    private String id;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeCategorySelect> children;

    public TreeCategorySelect() {
    }

    public TreeCategorySelect(SdEquipmentCategoryDto categoryDto) {
        this.id = categoryDto.getId();
        this.label = categoryDto.getName();

        if (!Objects.isNull(categoryDto.getChildren())) {
            this.children = categoryDto.getChildren().stream().map(TreeCategorySelect::new).collect(Collectors.toList());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TreeCategorySelect> getChildren() {
        return children;
    }

    public void setChildren(List<TreeCategorySelect> children) {
        this.children = children;
    }
}
