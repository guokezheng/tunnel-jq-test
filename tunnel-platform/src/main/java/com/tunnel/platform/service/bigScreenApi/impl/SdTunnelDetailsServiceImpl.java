package com.tunnel.platform.service.bigScreenApi.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.platform.mapper.bigScreenApi.SdTunnelDetailsMapper;
import com.tunnel.platform.service.bigScreenApi.ISdTunnelDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SdTunnelDetailsServiceImpl implements ISdTunnelDetailsService {

    private static final Logger log = LoggerFactory.getLogger(SdTunnelDetailsServiceImpl.class);

    @Autowired
    private SdTunnelDetailsMapper sdTunnelDetailsMapper;

    public Map<String, String> checkParamsOfSearchTunnel(JSONObject jsonObject){
        if(null == jsonObject || jsonObject.isEmpty()){
            log.error("当前查询针对隧道详细信息的查询参数为空，无法进行后续操作");
            throw new RuntimeException("当前查询针对隧道详细信息的查询参数为空，无法进行后续操作!");
        }
        String tunnelName = "";
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isNotNull(jsonObject.get("name")) && !jsonObject.get("name").toString().equals("")){
            //获取要查询的隧道名并进行核对看是否在数据库中存在
            tunnelName = jsonObject.get("name").toString();
            List<Map<String, Object>> checkTunnelExist = sdTunnelDetailsMapper.checkTunnelExist(tunnelName);
            if (checkTunnelExist.size() == 0) {
                throw new RuntimeException("查询的隧道不存在");
            } else if (checkTunnelExist.size() > 1) {
                throw new RuntimeException("隧道数据存在异常，请联系管理员");
            }
            String tunnel_id = checkTunnelExist.get(0).get("tunnel_id").toString();
            map.put("tunnel_id",tunnel_id);
        } else {
            log.error("隧道名获取异常，无法进行后续操作");
            throw new RuntimeException("隧道名获取异常，无法进行后续操作");
        }
        log.info("tunnelName:" + tunnelName);
        map.put("tunnelName",tunnelName);
        return map;
    }

    @Override
    public List<Map<String, Object>> getTodayTunnelIncedentmsg(JSONObject jsonObject) {
        String tunnelName = checkParamsOfSearchTunnel(jsonObject).get("tunnelName");
        //根据隧道名查询过去24小时内的事件事故
        List<Map<String, Object>> incidentDetailsIn24Hours = sdTunnelDetailsMapper.getIncidentDetailsIn24Hours(tunnelName);
        return incidentDetailsIn24Hours;
    }

    @Override
    public List<Map<String, Object>> getTodayTunnelWarningmsg(JSONObject jsonObject) {
        String tunnelName = checkParamsOfSearchTunnel(jsonObject).get("tunnelName");
        //根据隧道名查询过去24小时内的预警信息
        List<Map<String, Object>> warningDetailsIn24Hours = sdTunnelDetailsMapper.getWarningDetailsIn24Hours(tunnelName);
        return warningDetailsIn24Hours;
    }

    @Override
    public Map<String, Object> getDetailedQuantity(JSONObject jsonObject) {
        String tunnelName = checkParamsOfSearchTunnel(jsonObject).get("tunnelName");
        //分别查询24小时内事件事故、预警信息的数量
        Map<String, Object> detailsQuantityIn24Hours = sdTunnelDetailsMapper.getIncidentQuantityIn24Hours(tunnelName);
        Map<String, Object> warningQuantityIn24Hours = sdTunnelDetailsMapper.getWarningQuantityIn24Hours(tunnelName);
        detailsQuantityIn24Hours.put("earlyWarningVal",warningQuantityIn24Hours.get("earlyWarningVal"));
        return detailsQuantityIn24Hours;
    }

    @Override
    public List<Map<String, Object>> getTrafficFlowData(JSONObject jsonObject) {
        String tunnelId = checkParamsOfSearchTunnel(jsonObject).get("tunnel_id");
        //查询近6个月每个月的车流量数据
        List<Map<String, Object>> trafficFlowDataIn6Month = sdTunnelDetailsMapper.getTrafficFlowDataIn6Month(tunnelId);
        return trafficFlowDataIn6Month;
    }
}
