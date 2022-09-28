package com.tunnel.business.domain.trafficOperationControl.eventManage;

import com.tunnel.business.domain.trafficOperationControl.controlConfig.SdControlLevelConfig;

/**
 * describe: 事件信息和交通管制事件信息
 * 为方便前后台传递数据
 *
 * @author zs
 * @date 2022/2/16
 */
public class IncidentAndControlInfo {

    /**
     * 事件信息
     */
    private SdTrafficIncidentInfo incidentInfo;

    /**
     * 交通管制信息
     */
    private SdTrafficControlInfo controlInfo;

    /**
     * 关联的推送措施信息
     */
//    private List<SdTrafficIncidentMeasure> incidentMeasure;

    private SdControlLevelConfig levelConfig;

    public SdTrafficIncidentInfo getIncidentInfo() {
        return incidentInfo;
    }

    public void setIncidentInfo(SdTrafficIncidentInfo incidentInfo) {
        this.incidentInfo = incidentInfo;
    }

    public SdTrafficControlInfo getControlInfo() {
        return controlInfo;
    }

    public void setControlInfo(SdTrafficControlInfo controlInfo) {
        this.controlInfo = controlInfo;
    }

    public SdControlLevelConfig getLevelConfig() {
        return levelConfig;
    }

    public void setLevelConfig(SdControlLevelConfig levelConfig) {
        this.levelConfig = levelConfig;
    }

//    public List<SdTrafficIncidentMeasure> getIncidentMeasure() {
//        return incidentMeasure;
//    }
//
//    public void setIncidentMeasure(List<SdTrafficIncidentMeasure> incidentMeasure) {
//        this.incidentMeasure = incidentMeasure;
//    }
}
