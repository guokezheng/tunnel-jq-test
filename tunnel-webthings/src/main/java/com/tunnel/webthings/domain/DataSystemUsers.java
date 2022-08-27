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
@Data
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

}
