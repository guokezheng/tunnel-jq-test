package com.tunnel.platform.domain.event;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 应急人员信息对象 sd_emergency_per
 *
 * @author ruoy
 * @date 2021-05-12
 */
@ApiModel("应急人员信息对象类")
@Data
public class SdEmergencyPer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("应急人员信息ID")
    private Long id;

    /** 隧道ID */
    @Excel(name = "隧道ID")
    @ApiModelProperty("隧道ID")
    private String tunnelId;
    private String tunnelName;

    /** 应急人员 */
    @Excel(name = "应急人员")
    @ApiModelProperty("应急人员")
    private String userName;

    @Excel(name = "小组")
    @ApiModelProperty("小组")
    private String groupName;

    /** 电话 */
    @Excel(name = "电话")
    @ApiModelProperty("电话")
    private String phone;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tunnelId", getTunnelId())
            .append("userName", getUserName())
            .append("phone", getPhone())
            .toString();
    }
}
