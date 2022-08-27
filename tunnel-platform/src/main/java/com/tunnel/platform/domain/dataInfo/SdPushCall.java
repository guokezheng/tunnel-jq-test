package com.tunnel.platform.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 紧急电话推送记录对象 sd_push_call
 * 
 * @author wangx
 * @date 2021-03-15
 */
public class SdPushCall extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 接收json串 */
    @Excel(name = "接收json串")
    private String reserveData;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setReserveData(String reserveData) 
    {
        this.reserveData = reserveData;
    }

    public String getReserveData() 
    {
        return reserveData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("reserveData", getReserveData())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
