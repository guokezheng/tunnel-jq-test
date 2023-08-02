package com.tunnel.business.domain.electromechanicalPatrol;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 部门表 sys_dept
 */
@ApiModel("部门实体")
public class SdTeamsList extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @ApiModelProperty("班组id")
    private String deptId;

    /**
     * 部门名称
     */
    @Excel(name = "班组名称")
    @ApiModelProperty("班组名称")
    private String deptName;

    /**
     * 父部门ID
     */
    @Excel(name = "所属部门")
    @ApiModelProperty("父部门ID")
    private String parentId;

    /**
     * 祖级列表
     */
    @ApiModelProperty("祖级列表")
    private String ancestors;



    /**
     * 显示顺序
     */
   /* @Excel(name = "排序")*/
    @ApiModelProperty("显示顺序")
    private String orderNum;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private String leader;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 部门状态:0正常,1停用
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    @ApiModelProperty("部门状态:0正常,1停用")
    private String status;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;

    /**
     * 父部门名称
     */
    @ApiModelProperty("父部门名称")
    private String parentName;

    private String ids;


    /**
     * 子部门
     */
    @ApiModelProperty("子部门")
    private List<SdTeamsList> children = new ArrayList<SdTeamsList>();

    private String depts;

    public String getDepts() {
        return this.depts;
    }

    public void setDepts( String depts) {
        this.depts = depts;
    }

    @Override
    public Date getCreateTime() {
        return this.createTime;
    }

    @Override
    public void setCreateTime( Date createTime) {
        this.createTime = createTime;
    }

    public String getIds() {
        return this.ids;
    }

    public void setIds( String ids) {
        this.ids = ids;
    }

    private String value;

    private String lable;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }



    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    @NotBlank(message = "部门名称不能为空")
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }


    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    @Size(min = 0, max = 11, message = "联系电话长度不能超过11个字符")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<SdTeamsList> getChildren() {
        return children;
    }

    public void setChildren(List<SdTeamsList> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("deptId", getDeptId())
                .append("parentId", getParentId())
                .append("ancestors", getAncestors())
                .append("deptName", getDeptName())
                .append("orderNum", getOrderNum())
                .append("leader", getLeader())
                .append("phone", getPhone())
                .append("email", getEmail())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
