package com.tunnel.platform.mapper.bigScreenApi;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SdTunnelDetailsMapper {

    public List<Map<String, Object>> checkTunnelExist(@Param("tunnelName") String tunnelName);

    public List<Map<String, Object>> getIncidentDetailsIn24Hours(@Param("tunnelName") String tunnelName);

    public List<Map<String, Object>> getWarningDetailsIn24Hours(@Param("tunnelName") String tunnelName);

    public Map<String, Object> getIncidentQuantityIn24Hours(@Param("tunnelName") String tunnelName);

    public Map<String, Object> getWarningQuantityIn24Hours(@Param("tunnelName") String tunnelName);

    public List<Map<String, Object>> getTrafficFlowDataIn6Month(@Param("tunnelId") String tunnelId);

}
