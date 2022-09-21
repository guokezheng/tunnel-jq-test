package com.tunnel.business.service.bigScreenApi;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface ISdTunnelDetailsService {

    List<Map<String, Object>> getTodayTunnelIncedentmsg(JSONObject jsonObject);

    List<Map<String, Object>> getTodayTunnelWarningmsg(JSONObject jsonObject);

    Map<String, Object> getDetailedQuantity(JSONObject jsonObject);

    List<Map<String, Object>> getTrafficFlowData(JSONObject jsonObject);

}
