package com.tunnel.business.domain.energyManagement;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @author zhai
 * @date 2023/8/1
 */
public class EnergySite extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    //隧道id
    private String tunnelId;

    //用电量
    private double energyValue;

    //隧道名称
    private String tunnelName;

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(String tunnelId) {
        this.tunnelId = tunnelId;
    }

    public double getEnergyValue() {
        return energyValue;
    }

    public void setEnergyValue(double energyValue) {
        this.energyValue = energyValue;
    }
}
