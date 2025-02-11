package com.zc.iot.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备对象 athena_iot_device
 * 
 * @author Athena-xiepufeng
 * @date 2021-11-04
 */
public class IotDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 删除 **/
    private static final int DESTROY = 0;
    /** 创建 **/
    private static final int GIVE = 1;
    /** 启用 **/
    private static final int ENABLE = 2;
    /** 禁用 **/
    private static final int DISABLE = 3;

    /** $column.columnComment */
    private Long id;

    /** 网关设备（当设备是子设备时必须） */
    @Excel(name = "网关设备")
    private Long gatewayDevice;

    /** 所属产品 */
    @Excel(name = "所属产品")
    private Long productKey;

    /** 所属分组 */
    @Excel(name = "所属分组")
    private Long groupKey;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String deviceCode;

    /** 在线状态 */
    @Excel(name = "在线状态")
    private Integer onlineState;

    /** 启用状态 */
    @Excel(name = "启用状态")
    private Integer enabled;

    /** 固件版本 */
    @Excel(name = "固件版本")
    private String version;

    /** IP地址 */
    @Excel(name = "IP地址")
    private String ipAddr;

    /** 位置 */
    @Excel(name = "位置")
    private String location;

    /** 上线时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上线时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastInlineTime;

    /** 离线时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "离线时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastOfflineTime;

    /**
     * 生命周期（1 创建、0 删除、2 禁用、3 启用）
     */
    private Integer lifecycle;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setProductKey(Long productKey) 
    {
        this.productKey = productKey;
    }

    public Long getProductKey() 
    {
        return productKey;
    }
    public void setGroupKey(Long groupKey) 
    {
        this.groupKey = groupKey;
    }

    public Long getGroupKey() 
    {
        return groupKey;
    }
    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }
    public void setDeviceCode(String deviceCode) 
    {
        this.deviceCode = deviceCode;
    }

    public String getDeviceCode() 
    {
        return deviceCode;
    }
    public void setOnlineState(Integer onlineState)
    {
        this.onlineState = onlineState;
    }

    public Integer getOnlineState()
    {
        return onlineState;
    }
    public void setEnabled(Integer enabled)
    {
        this.enabled = enabled;
    }

    public Integer getEnabled()
    {
        return enabled;
    }
    public void setVersion(String version) 
    {
        this.version = version;
    }

    public String getVersion() 
    {
        return version;
    }
    public void setIpAddr(String ipAddr) 
    {
        this.ipAddr = ipAddr;
    }

    public String getIpAddr() 
    {
        return ipAddr;
    }
    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }
    public void setLastInlineTime(Date lastInlineTime) 
    {
        this.lastInlineTime = lastInlineTime;
    }

    public Date getLastInlineTime() 
    {
        return lastInlineTime;
    }
    public void setLastOfflineTime(Date lastOfflineTime) 
    {
        this.lastOfflineTime = lastOfflineTime;
    }

    public Date getLastOfflineTime() 
    {
        return lastOfflineTime;
    }

    public Integer getLifecycle() {
        return lifecycle;
    }

    public void setLifecycle(Integer lifecycle) {
        this.lifecycle = lifecycle;
    }

    public Long getGatewayDevice() {
        return gatewayDevice;
    }

    public void setGatewayDevice(Long gatewayDevice) {
        this.gatewayDevice = gatewayDevice;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("gatewayDevice", getGatewayDevice())
            .append("productKey", getProductKey())
            .append("groupKey", getGroupKey())
            .append("deviceName", getDeviceName())
            .append("deviceCode", getDeviceCode())
            .append("onlineState", getOnlineState())
            .append("enabled", getEnabled())
            .append("version", getVersion())
            .append("ipAddr", getIpAddr())
            .append("location", getLocation())
            .append("lastInlineTime", getLastInlineTime())
            .append("lastOfflineTime", getLastOfflineTime())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
