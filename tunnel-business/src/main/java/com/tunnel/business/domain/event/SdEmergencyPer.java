package com.tunnel.business.domain.event;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 应急人员信息对象 sd_emergency_per
 *
 * @author ruoy
 * @date 2021-05-12
 */
@ApiModel("应急人员信息对象类")
public class SdEmergencyPer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("应急人员信息ID")
    private Long id;

    /** 隧道ID */

    @ApiModelProperty("隧道ID")
    private String tunnelId;

    private String tunnelName;

    @Excel(name = "所属部门")
    private String deptName;

    private String deptId;



    /** 应急人员 */
    @Excel(name = "姓名")
    @ApiModelProperty("应急人员")
    private String userName;

    @Excel(name = "岗位")
    @ApiModelProperty("岗位")
    private String groupName;

    /** 电话 */
    @Excel(name = "电话")
    @ApiModelProperty("电话")
    private String phone;

    private String ids;

    @Override
    public String toString() {
        return "SdEmergencyPer{" +
                "id=" + id +
                ", tunnelId='" + tunnelId + '\'' +
                ", tunnelName='" + tunnelName + '\'' +
                ", userName='" + userName + '\'' +
                ", groupName='" + groupName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public String getDeptName() {
        return this.deptName;
    }

    public void setDeptName( String deptName) {
        this.deptName = deptName;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId( String deptId) {
        this.deptId = deptId;
    }

    public String getIds() {
        return this.ids;
    }

    public void setIds( String ids) {
        this.ids = ids;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTunnelId(String tunnelId)
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId()
    {
        return tunnelId;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

}
