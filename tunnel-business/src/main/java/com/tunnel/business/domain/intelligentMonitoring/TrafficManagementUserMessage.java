package com.tunnel.business.domain.intelligentMonitoring;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 【请填写功能名称】对象 traffic_management_user_message
 *
 * @author ruoyi
 * @date 2021-12-03
 */
public class TrafficManagementUserMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Integer id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 特殊车辆通行管理ID */
    private Long trafficManagementId;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setTrafficManagementId(Long trafficManagementId)
    {
        this.trafficManagementId = trafficManagementId;
    }

    public Long getTrafficManagementId()
    {
        return trafficManagementId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("trafficManagementId", getTrafficManagementId())
            .append("createTime", getCreateTime())
            .toString();
    }
}
