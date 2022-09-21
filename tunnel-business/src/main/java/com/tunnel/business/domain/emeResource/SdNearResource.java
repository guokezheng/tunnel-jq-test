package com.tunnel.business.domain.emeResource;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 周边资源对象 sd_near_resource
 * 
 * @author ruoyi
 * @date 2021-11-22
 */
@ApiModel("周边资源对象类")
public class SdNearResource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @ApiModelProperty("ID")
    private Long id;

    /** 资源编号 */
    @Excel(name = "资源编号")
    @ApiModelProperty("资源编号")
    private String resourceId;

    /** 资源名称 */
    @ApiModelProperty("资源名称")
    @Excel(name = "资源名称")
    private String resourceName;

    /** 资源类型 */
    @Excel(name = "资源类型")
    @ApiModelProperty("资源类型")
    private String resourceType;

    /** 隧道id */
    @Excel(name = "隧道id")
    @ApiModelProperty("隧道id")
    private String tunnelId;

    /** 距离 */
    @Excel(name = "距离")
    @ApiModelProperty("距离")
    private String distance;

    /** 负责人 */
    @Excel(name = "负责人")
    @ApiModelProperty("负责人")
    private String person;

    /** 联系方式 */
    @ApiModelProperty("联系方式")
    @Excel(name = "联系方式")
    private String phone;

    /** 状态（1.启用，2.停用） */
    @ApiModelProperty("状态（1.启用，2.停用")
    @Excel(name = "状态", readConverterExp = "1=.启用，2.停用")
    private String state;

    /** 资源详情 */
    @ApiModelProperty("资源详情")
    @Excel(name = "资源详情")
    private String detail;

    private String tunnelName;

    private Long deptId;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getTunnelName() {   return tunnelName;   }

    public void setTunnelName(String tunnelName) {  this.tunnelName = tunnelName;   }

    private String dictLabel;

    public String getDictLabel() {     return dictLabel;   }

    public void setDictLabel(String dictLabel) {    this.dictLabel = dictLabel;    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setResourceId(String resourceId) 
    {
        this.resourceId = resourceId;
    }

    public String getResourceId() 
    {
        return resourceId;
    }
    public void setResourceName(String resourceName) 
    {
        this.resourceName = resourceName;
    }

    public String getResourceName() 
    {
        return resourceName;
    }
    public void setResourceType(String resourceType) 
    {
        this.resourceType = resourceType;
    }

    public String getResourceType() 
    {
        return resourceType;
    }
    public void setTunnelId(String tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId() 
    {
        return tunnelId;
    }
    public void setDistance(String distance) 
    {
        this.distance = distance;
    }

    public String getDistance() 
    {
        return distance;
    }
    public void setPerson(String person) 
    {
        this.person = person;
    }

    public String getPerson() 
    {
        return person;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setState(String state) 
    {
        this.state = state;
    }

    public String getState() 
    {
        return state;
    }
    public void setDetail(String detail) 
    {
        this.detail = detail;
    }

    public String getDetail() 
    {
        return detail;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("resourceId", getResourceId())
            .append("resourceName", getResourceName())
            .append("resourceType", getResourceType())
            .append("tunnelId", getTunnelId())
            .append("distance", getDistance())
            .append("person", getPerson())
            .append("phone", getPhone())
            .append("state", getState())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("detail", getDetail())
            .toString();
    }
}
