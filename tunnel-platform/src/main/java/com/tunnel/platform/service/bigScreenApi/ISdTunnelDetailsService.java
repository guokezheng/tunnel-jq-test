package com.tunnel.platform.service.bigScreenApi;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface ISdTunnelDetailsService {

    public List<Map<String, Object>> getTodayTunnelIncedentmsg(JSONObject jsonObject);

    public List<Map<String, Object>> getTodayTunnelWarningmsg(JSONObject jsonObject);

    public Map<String, Object> getDetailedQuantity(JSONObject jsonObject);

    public List<Map<String, Object>> getTrafficFlowData(JSONObject jsonObject);

}
