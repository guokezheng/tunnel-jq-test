package com.tunnel.business.service.bigScreenApiN;

import java.util.Map;

public interface ISdTunnelsDataService {
    Map getIndexData();
    Map getMajorEventData();

    Map getEventStatData();

    Map getFindEventStatData();

    Map getCarFlowData();

    Map getRealTimeCarFlow();


}
