package com.tunnel.platform.domain.event;

import java.util.List;

/**
 * 预案流程节点对象接收类
 * @author issuser
 */
public class SdReserveProcessModel {

    private Long reserveId;

    public List<SdReserveProcess> sdReserveProcesses;

    public Long getReserveId() {
        return reserveId;
    }

    public void setReserveId(Long reserveId) {
        this.reserveId = reserveId;
    }

    public List<SdReserveProcess> getSdReserveProcesses() {
        return sdReserveProcesses;
    }

    public void setSdReserveProcesses(List<SdReserveProcess> sdReserveProcesses) {
        this.sdReserveProcesses = sdReserveProcesses;
    }

    @Override
    public String toString() {
        return "SdReserveProcessModel{" +
                "reserveId=" + reserveId +
                ", sdReserveProcesses=" + sdReserveProcesses +
                '}';
    }
}
