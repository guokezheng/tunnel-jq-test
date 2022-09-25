package com.tunnel.webthings.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ZHC
 * {@code @date}  2022/8/24 11:16
 * 数据中台系统用户实体类
 */
@ApiModel("数据中台系统用户实体类")
public class DataSystemUsers extends BaseEntity {

    @ApiModelProperty("单位ID")
    private String companyId;

    @ApiModelProperty("单位名称")
    private String companyName;

    @ApiModelProperty("创建时间")
    private String createDate;

    @ApiModelProperty("部门名称")
    private String deptFullName;

    @ApiModelProperty("部门ID")
    private String deptId;

    @ApiModelProperty("隶属机构")
    private List<String> deptList;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("用户截止时间")
    private Object endDate;

    @ApiModelProperty("性别 0:男 1:女 2:保密")
    private String gender;

    @ApiModelProperty("性别描述")
    private String genderName;

    @ApiModelProperty("头像")
    private String headUrl;

    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("备用手机号")
    private Object mobile2;

    @ApiModelProperty("工作地址")
    private String officeRoom;

    @ApiModelProperty("职位描述")
    private String positionDesc;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("业务角色")
    private List<String> roleBusinessList;

    @ApiModelProperty("用户角色")
    private List<String> roleList;

    @ApiModelProperty("简拼")
    private Object spell;

    @ApiModelProperty("用户启用时间")
    private Object startDate;

    @ApiModelProperty("状态 0:停用 1:正常")
    private long status;

    @ApiModelProperty("超级管理员 0:否 1:是")
    private long superAdmin;

    @ApiModelProperty("上级汇报人ID")
    private Object superiorId;

    @ApiModelProperty("上级汇报人姓名")
    private Object superiorName;

    @ApiModelProperty("用户名")
    private String username;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDeptFullName() {
        return deptFullName;
    }

    public void setDeptFullName(String deptFullName) {
        this.deptFullName = deptFullName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public List<String> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<String> deptList) {
        this.deptList = deptList;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getEndDate() {
        return endDate;
    }

    public void setEndDate(Object endDate) {
        this.endDate = endDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Object getMobile2() {
        return mobile2;
    }

    public void setMobile2(Object mobile2) {
        this.mobile2 = mobile2;
    }

    public String getOfficeRoom() {
        return officeRoom;
    }

    public void setOfficeRoom(String officeRoom) {
        this.officeRoom = officeRoom;
    }

    public String getPositionDesc() {
        return positionDesc;
    }

    public void setPositionDesc(String positionDesc) {
        this.positionDesc = positionDesc;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public List<String> getRoleBusinessList() {
        return roleBusinessList;
    }

    public void setRoleBusinessList(List<String> roleBusinessList) {
        this.roleBusinessList = roleBusinessList;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    public Object getSpell() {
        return spell;
    }

    public void setSpell(Object spell) {
        this.spell = spell;
    }

    public Object getStartDate() {
        return startDate;
    }

    public void setStartDate(Object startDate) {
        this.startDate = startDate;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(long superAdmin) {
        this.superAdmin = superAdmin;
    }

    public Object getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Object superiorId) {
        this.superiorId = superiorId;
    }

    public Object getSuperiorName() {
        return superiorName;
    }

    public void setSuperiorName(Object superiorName) {
        this.superiorName = superiorName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "DataSystemUsers{" +
                "companyId='" + companyId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", createDate='" + createDate + '\'' +
                ", deptFullName='" + deptFullName + '\'' +
                ", deptId='" + deptId + '\'' +
                ", deptList=" + deptList +
                ", deptName='" + deptName + '\'' +
                ", email='" + email + '\'' +
                ", endDate=" + endDate +
                ", gender='" + gender + '\'' +
                ", genderName='" + genderName + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", id='" + id + '\'' +
                ", idCard='" + idCard + '\'' +
                ", mobile='" + mobile + '\'' +
                ", mobile2=" + mobile2 +
                ", officeRoom='" + officeRoom + '\'' +
                ", positionDesc='" + positionDesc + '\'' +
                ", realName='" + realName + '\'' +
                ", roleBusinessList=" + roleBusinessList +
                ", roleList=" + roleList +
                ", spell=" + spell +
                ", startDate=" + startDate +
                ", status=" + status +
                ", superAdmin=" + superAdmin +
                ", superiorId=" + superiorId +
                ", superiorName=" + superiorName +
                ", username='" + username + '\'' +
                '}';
    }
}
