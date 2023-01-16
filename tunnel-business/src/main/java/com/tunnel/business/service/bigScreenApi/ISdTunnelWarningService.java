package com.tunnel.business.service.bigScreenApi;

import java.util.List;
import java.util.Map;

public interface ISdTunnelWarningService {

    List<Long> getTunnelWarningNumber(String tunnelId);

    /**
     * 查询近30日隧道预警带日期
     *
     * @param tunnelId
     * @param startDate
     * @param endDate
     * @return
     */
    List<Map<String, Object>> getTunnelWarningNumData(String tunnelId, String startDate, String endDate);
}
