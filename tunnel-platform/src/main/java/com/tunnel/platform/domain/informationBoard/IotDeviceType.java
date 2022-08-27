package com.tunnel.platform.domain.informationBoard;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 设备类型编号对象 iot_device_type
 * 
 * @author yangqichao
 * @date 2020-03-26
 */
public class IotDeviceType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 类型编号 */
    private Long deviceTypeId;

    /** 设备类型 */
    @Excel(name = "设备类型")
    private String deviceTypeNumber;

    /** 设备状态 */
    @Excel(name = "设备状态")
    private String states;

    /** 设备类型名称 */
    @Excel(name = "设备类型名称")
    private String deviceTypeName;

    /** URl */
    @Excel(name = "URl")
    private String url;

    /** 正常图标样式 */
    @Excel(name = "正常图标样式")
    private String normalCssClass;

    /** 故障图标样式 */
    @Excel(name = "故障图标样式")
    private String abnormalCssClass;

    /** 离线图标样式 */
    @Excel(name = "离线图标样式")
    private String faultCssClass;

    /** 推送图标 */
    @Excel(name = "推送图标")
    private String pushCssClass;

    /** 刷新时间 */
    @Excel(name = "刷新时间")
    private String refreshTime;

    /** 是否可用 */
    @Excel(name = "是否可用")
    private Integer visible;

    /** 是否需要推送 */
    @Excel(name = "是否需要推送")
    private Integer ispush;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    public void setDeviceTypeId(Long deviceTypeId) 
    {
        this.deviceTypeId = deviceTypeId;
    }

    public Long getDeviceTypeId() 
    {
        return deviceTypeId;
    }
    public void setDeviceTypeNumber(String deviceTypeNumber) 
    {
        this.deviceTypeNumber = deviceTypeNumber;
    }

    public String getDeviceTypeNumber() 
    {
        return deviceTypeNumber;
    }
    public void setStates(String states) 
    {
        this.states = states;
    }

    public String getStates() 
    {
        return states;
    }
    public void setDeviceTypeName(String deviceTypeName) 
    {
        this.deviceTypeName = deviceTypeName;
    }

    public String getDeviceTypeName() 
    {
        return deviceTypeName;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }
    public void setNormalCssClass(String normalCssClass) 
    {
        this.normalCssClass = normalCssClass;
    }

    public String getNormalCssClass() 
    {
        return normalCssClass;
    }
    public void setAbnormalCssClass(String abnormalCssClass) 
    {
        this.abnormalCssClass = abnormalCssClass;
    }

    public String getAbnormalCssClass() 
    {
        return abnormalCssClass;
    }
    public void setFaultCssClass(String faultCssClass) 
    {
        this.faultCssClass = faultCssClass;
    }

    public String getFaultCssClass() 
    {
        return faultCssClass;
    }
    public void setPushCssClass(String pushCssClass) 
    {
        this.pushCssClass = pushCssClass;
    }

    public String getPushCssClass() 
    {
        return pushCssClass;
    }
    public void setRefreshTime(String refreshTime) 
    {
        this.refreshTime = refreshTime;
    }

    public String getRefreshTime() 
    {
        return refreshTime;
    }
    public void setVisible(Integer visible) 
    {
        this.visible = visible;
    }

    public Integer getVisible() 
    {
        return visible;
    }
    public void setIspush(Integer ispush) 
    {
        this.ispush = ispush;
    }

    public Integer getIspush() 
    {
        return ispush;
    }
    public void setSort(Integer sort) 
    {
        this.sort = sort;
    }

    public Integer getSort() 
    {
        return sort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deviceTypeId", getDeviceTypeId())
            .append("deviceTypeNumber", getDeviceTypeNumber())
            .append("states", getStates())
            .append("deviceTypeName", getDeviceTypeName())
            .append("url", getUrl())
            .append("normalCssClass", getNormalCssClass())
            .append("abnormalCssClass", getAbnormalCssClass())
            .append("faultCssClass", getFaultCssClass())
            .append("pushCssClass", getPushCssClass())
            .append("refreshTime", getRefreshTime())
            .append("visible", getVisible())
            .append("ispush", getIspush())
            .append("sort", getSort())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
