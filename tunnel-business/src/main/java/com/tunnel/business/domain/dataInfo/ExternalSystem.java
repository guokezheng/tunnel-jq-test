package com.tunnel.business.domain.dataInfo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外部系统对象 external_system
 */
public class ExternalSystem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 厂商品牌id */
    @Excel(name = "厂商品牌id")
    private String brandId;


    /**
     * 隧道ID
     */
    private String tunnelId;

    /**
     * 隧道
     */
    private String tunnel;

    /** 是否映射方向（0：是，1：否） */
    @Excel(name = "是否映射方向", readConverterExp = "0=：是，1：否")
    private String isDirection;

    /** 用户名 */
    @Excel(name = "用户名")
    private String username;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 网络状态 */
    @Excel(name = "网络状态")
    private String networkStatus;

    /** 系统名称 */
    @Excel(name = "系统名称")
    private String systemName;

    /** 系统标识 **/
    private String systemCode;

    /** 系统参数 **/
    private String systemParam;

    /** 系统地址 */
    @Excel(name = "系统地址")
    private String systemUrl;

    /** 是否打开网络检测 */
    private Boolean openCheckNetwork;

    /** 网络类型 */
    private String networkType;

    /** 网络检测地址 */
    private String checkNetworkUrl;

    /** 网络检测超时时长（秒） */
    private int checkNetworkTimeout;


    //系统参数
    private String param;


    public Boolean getOpenCheckNetwork() {
        return openCheckNetwork;
    }

    public void setOpenCheckNetwork(Boolean openCheckNetwork) {
        this.openCheckNetwork = openCheckNetwork;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getCheckNetworkUrl() {
        return checkNetworkUrl;
    }

    public void setCheckNetworkUrl(String checkNetworkUrl) {
        this.checkNetworkUrl = checkNetworkUrl;
    }

    public int getCheckNetworkTimeout() {
        return checkNetworkTimeout;
    }

    public void setCheckNetworkTimeout(int checkNetworkTimeout) {
        this.checkNetworkTimeout = checkNetworkTimeout;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getSystemParam() {
        return systemParam;
    }

    public void setSystemParam(String systemParam) {
        this.systemParam = systemParam;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getTunnel() {
        return tunnel;
    }

    public void setTunnel(String tunnel) {
        this.tunnel = tunnel;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setBrandId(String brandId)
    {
        this.brandId = brandId;
    }

    public String getBrandId()
    {
        return brandId;
    }


    public String getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(String tunnelId) {
        this.tunnelId = tunnelId;
    }

    public void setIsDirection(String isDirection)
    {
        this.isDirection = isDirection;
    }

    public String getIsDirection()
    {
        return isDirection;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return username;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }
    public void setNetworkStatus(String networkStatus)
    {
        this.networkStatus = networkStatus;
    }

    public String getNetworkStatus()
    {
        return networkStatus;
    }
    public void setSystemName(String systemName)
    {
        this.systemName = systemName;
    }

    public String getSystemName()
    {
        return systemName;
    }
    public void setSystemUrl(String systemUrl)
    {
        this.systemUrl = systemUrl;
    }

    public String getSystemUrl()
    {
        return systemUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("brandId", getBrandId())
            .append("tunnelId", getTunnelId())
            .append("isDirection", getIsDirection())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("networkStatus", getNetworkStatus())
            .append("systemName", getSystemName())
            .append("systemUrl", getSystemUrl())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
