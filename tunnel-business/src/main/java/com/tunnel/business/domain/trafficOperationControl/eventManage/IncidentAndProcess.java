package com.tunnel.business.domain.trafficOperationControl.eventManage;

import java.util.List;

/**
 * describe: 事件处理流程--方便前后台交互
 *
 * @author zs
 * @date 2022/2/25
 */
public class IncidentAndProcess {

    /**
     * 事件id
     */
    private Long measuresId;

    /**
     * 流程列表
     */
    private List<SdTrafficIncidentProcess> eveList;


    public Long getMeasuresId() {
        return measuresId;
    }

    public void setMeasuresId(Long measuresId) {
        this.measuresId = measuresId;
    }

    public List<SdTrafficIncidentProcess> getEveList() {
        return eveList;
    }

    public void setEveList(List<SdTrafficIncidentProcess> eveList) {
        this.eveList = eveList;
    }
}
