package com.tunnel.business.mapper.bigScreenApi;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SdTunnelWarningMapper {

    public List<Long> getTunnelWarningNumber(@Param("tunnelId") String tunnelId);

    List<Map<String, Object>> getTunnelWarningNumData(@Param("tunnelId") String tunnelId,
                                                      @Param("startDate") String startDate,
                                                      @Param("endDate") String endDate);
}
