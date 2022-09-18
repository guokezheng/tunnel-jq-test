package com.tunnel.platform.domain.event;

import java.util.List;

/**
 * 预案流程节点对象接收类
 * @author issuser
 */
public class SdReserveProcessModel {

    private Long reserveId;

    public List<SdReserveProcess> handleStrategyList;

    public Long getReserveId() {
        return reserveId;
    }

    public void setReserveId(Long reserveId) {
        this.reserveId = reserveId;
    }

    public List<SdReserveProcess> getSdReserveProcesses() {
        return handleStrategyList;
    }

    public void setSdReserveProcesses(List<SdReserveProcess> handleStrategyList) {
        this.handleStrategyList = handleStrategyList;
    }

    @Override
    public String toString() {
        return "SdReserveProcessModel{" +
                "reserveId=" + reserveId +
                ", handleStrategyList=" + handleStrategyList +
                '}';
    }
}
