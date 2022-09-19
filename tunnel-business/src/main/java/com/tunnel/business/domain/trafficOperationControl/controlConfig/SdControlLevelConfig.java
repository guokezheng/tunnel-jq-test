package com.tunnel.business.domain.trafficOperationControl.controlConfig;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 管控等级配置对象 sd_control_level_config
 *
 * @author ruoyi
 * @date 2022-02-12
 */
public class SdControlLevelConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id

 */
    private Long id;

    /** 管控类别 */
    @Excel(name = "管控类别")
    private String controlType;

    /** 管控级别 */
    @Excel(name = "管控级别")
    private String controlLevel;

    /** 状态：0开启、1关闭 */
    @Excel(name = "状态", readConverterExp = "0=开启,1=关闭")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setControlType(String controlType)
    {
        this.controlType = controlType;
    }

    public String getControlType()
    {
        return controlType;
    }
    public void setControlLevel(String controlLevel)
    {
        this.controlLevel = controlLevel;
    }

    public String getControlLevel()
    {
        return controlLevel;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("controlType", getControlType())
            .append("controlLevel", getControlLevel())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
