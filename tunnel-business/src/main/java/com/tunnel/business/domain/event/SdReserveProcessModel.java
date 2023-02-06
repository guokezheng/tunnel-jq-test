package com.tunnel.business.domain.event;

import java.util.List;
import java.util.Map;

/**
 * 预案流程节点对象接收类
 * @author issuser
 */
public class SdReserveProcessModel {

    private Long reserveId;

    public List<Map> sdReserveProcesses;

    public Long getReserveId() {
        return reserveId;
    }

    public void setReserveId(Long reserveId) {
        this.reserveId = reserveId;
    }

    public List<Map> getSdReserveProcesses() {
        return sdReserveProcesses;
    }

    public void setSdReserveProcesses(List<Map> sdReserveProcesses) {
        this.sdReserveProcesses = sdReserveProcesses;
    }
//    public List<SdReserveProcess> getSdReserveProcesses() {
//        return handleStrategyList;
//    }
//
//    public void setSdReserveProcesses(List<SdReserveProcess> handleStrategyList) {
//        this.handleStrategyList = handleStrategyList;
//    }

    @Override
    public String toString() {
        return "SdReserveProcessModel{" +
                "reserveId=" + reserveId +
            //    ", handleStrategyList=" + handleStrategyList +
                '}';
    }
}
