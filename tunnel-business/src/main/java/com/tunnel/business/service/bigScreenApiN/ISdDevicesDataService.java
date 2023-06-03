package com.tunnel.business.service.bigScreenApiN;

import java.util.Map;

public interface ISdDevicesDataService {
    Map getIndexData();

    Map getFaultWarnData();

    Map getMonthFaultData();

    Map getRealTimeStatData();

    Map getFaultTimeTopData();

    Map getEqPercentData();

    Map getFaultCategoryData();

}
