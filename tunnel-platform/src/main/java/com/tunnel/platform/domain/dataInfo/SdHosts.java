package com.tunnel.platform.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * plc 主机对象 sd_hosts
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
public class SdHosts extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * PLCID
     */
    private String plcId;

    /**
     * 隧道ID
     */
    @Excel(name = "隧道ID")
    private String plcTunnelId;

    /**
     * IP地址
     */
    @Excel(name = "IP地址")
    private String plcIp;

    /**
     * IP地址
     */
    @Excel(name = "IP地址")
    private String plcPort;

    /**
     * 控制协议类型(DM/CIO)
     */
    @Excel(name = "控制协议类型(DM/CIO)")
    private String plcControlType;


    /**
     * 轮询
     */
    @Excel(name = "轮询", readConverterExp = "0=是,1=否")
    private Long isMonitor;

    /**
     * 连接状态
     */
    @Excel(name = "连接状态")
    private Integer connectionStatus;

    public void setPlcId(String plcId) {
        this.plcId = plcId;
    }

    public String getPlcId() {
        return plcId;
    }

    public void setPlcTunnelId(String plcTunnelId) {
        this.plcTunnelId = plcTunnelId;
    }

    public String getPlcTunnelId() {
        return plcTunnelId;
    }

    public void setPlcIp(String plcIp) {
        this.plcIp = plcIp;
    }

    public String getPlcIp() {
        return plcIp;
    }

    public String getPlcPort() {
        return plcPort;
    }

    public void setPlcPort(String plcPort) {
        this.plcPort = plcPort;
    }

    public Long getIsMonitor() {
        return isMonitor;
    }

    public void setIsMonitor(Long isMonitor) {
        this.isMonitor = isMonitor;
    }

    public String getPlcControlType() {
        return plcControlType;
    }

    public void setPlcControlType(String plcControlType) {
        this.plcControlType = plcControlType;
    }

    public void setConnectionStatus(Integer connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
    public Integer getConnectionStatus() {
        return connectionStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("plcId", getPlcId())
                .append("plcTunnelId", getPlcTunnelId())
                .append("plcIp", getPlcIp())
                .append("plcControlType", getPlcControlType())
                .append("connectionStatus", getConnectionStatus())
                .toString();
    }
}
