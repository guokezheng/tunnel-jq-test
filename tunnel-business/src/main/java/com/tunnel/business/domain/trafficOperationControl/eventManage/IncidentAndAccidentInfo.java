package com.tunnel.business.domain.trafficOperationControl.eventManage;

/**
 * describe: 事件信息和交通事故、清障事件信息
 * 为方便前后台传递数据
 *
 * @author zs
 * @date 2022/2/18
 */
public class IncidentAndAccidentInfo {

    /**
     * 事件信息
     */
    private SdTrafficIncidentInfo incidentInfo;

    /**
     * 交通事故、清障信息
     */
    private SdTrafficAccidentInfo accidentInfo;


    public SdTrafficIncidentInfo getIncidentInfo() {
        return incidentInfo;
    }

    public void setIncidentInfo(SdTrafficIncidentInfo incidentInfo) {
        this.incidentInfo = incidentInfo;
    }

    public SdTrafficAccidentInfo getAccidentInfo() {
        return accidentInfo;
    }

    public void setAccidentInfo(SdTrafficAccidentInfo accidentInfo) {
        this.accidentInfo = accidentInfo;
    }
}
