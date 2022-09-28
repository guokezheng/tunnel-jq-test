package com.tunnel.platform.controller.bigScreenApi;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.service.bigScreenApi.ISdTunnelDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tunnelDetails")
public class SdTunnelDetailsController extends BaseController {
    
    @Autowired
    private ISdTunnelDetailsService iSdTunnelDetailsService;

    //查询sd_warning_info表
    /**
     * 24小时内隧道的事件事故
     * */
    @PostMapping("/incidentdetails")
    public AjaxResult getTunnelIncidentDetails(@RequestBody JSONObject jsonObject){
        List<Map<String, Object>> todayTunnelIncedentmsg = iSdTunnelDetailsService.getTodayTunnelIncedentmsg(jsonObject);
        return AjaxResult.success(todayTunnelIncedentmsg);
    }

    /**
     * 24小时内隧道的预警信息
     * */
    @PostMapping("/warningdetails")
    public AjaxResult getTunnelWarningDetails(@RequestBody JSONObject jsonObject){
        List<Map<String, Object>> todayTunnelWarningmsg = iSdTunnelDetailsService.getTodayTunnelWarningmsg(jsonObject);
        return AjaxResult.success(todayTunnelWarningmsg);
    }

    /**
     * 24小时内隧道的事件事故、预警信息的数量
     * */
    @PostMapping("/detailedquantity")
    public AjaxResult getDetailedQuantity(@RequestBody JSONObject jsonObject){
        Map<String, Object> detailedQuantity = iSdTunnelDetailsService.getDetailedQuantity(jsonObject);
        return AjaxResult.success(detailedQuantity);
    }

    /**
     * 指定隧道近6个月车流量数据
     */
    @PostMapping("/trafficFlowData")
    public AjaxResult getTrafficFlowData(@RequestBody JSONObject jsonObject){
        List<Map<String, Object>> trafficFlowData = iSdTunnelDetailsService.getTrafficFlowData(jsonObject);
        return AjaxResult.success(trafficFlowData);
    }

}
