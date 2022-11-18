package com.tunnel.platform.controller.bigScreenApi;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.service.bigScreenApi.SdSmartBigScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 大屏
 *
 * @author zhai
 * @date 2022/11/7
 */
@RestController
@RequestMapping("/eventWarning")
public class SdSmartBigScreenController extends BaseController {

    @Autowired
    private SdSmartBigScreenService sdSmartBigScreenService;

    /**
     * 查询本月预警事件
     * @return
     */
    @GetMapping("/getEventWarning")
    public AjaxResult getEventWarning(String tunnelId){
        Map<String, Object> eventWarning = sdSmartBigScreenService.getEventWarning(tunnelId);
        return AjaxResult.success(eventWarning);
    }

    /**
     * 查询当天报警事件
     * @param tunnelId
     * @return
     */
    @GetMapping("/getToDayEventWarning")
    public AjaxResult getToDayEventWarning(String tunnelId){
        return sdSmartBigScreenService.getToDayEventWarning(tunnelId);
    }

    /**
     * 查询本周报警事件
     * @param tunnelId
     * @return
     */
    @GetMapping("/getToWeekEventWarning")
    public AjaxResult getToWeekEventWarning(String tunnelId){
        return sdSmartBigScreenService.getToWeekEventWarning(tunnelId);
    }

    /**
     * 查询本月报警事件
     * @param tunnelId
     * @return
     */
    @GetMapping("/getSameMonthEventWarning")
    public AjaxResult getSameMonthEventWarning(String tunnelId){
        return sdSmartBigScreenService.getSameMonthEventWarning(tunnelId);
    }

    /**
     * 累计预警分析
     * @param tunnelId
     * @return
     */
    @GetMapping("/getCumulativeAlarm")
    public AjaxResult getCumulativeAlarm(String tunnelId){
        return sdSmartBigScreenService.getCumulativeAlarm(tunnelId);
    }

    /**
     * 查询交通事件信息列表
     * @param tunnelId
     * @return
     */
    @GetMapping("/getTrafficIncident")
    public AjaxResult getTrafficIncident(String tunnelId){
        return sdSmartBigScreenService.getTrafficIncident(tunnelId);
    }

    /**
     * 查询主动安全信息列表
     * @param tunnelId
     * @return
     */
    @GetMapping("/getActiveSafety")
    public AjaxResult getActiveSafety(String tunnelId){
        return sdSmartBigScreenService.getActiveSafety(tunnelId);
    }

    /**
     * 查询设备故障信息列表
     * @param tunnelId
     * @return
     */
    @GetMapping("/getequipmentFailure")
    public AjaxResult getequipmentFailure(String tunnelId){
        return sdSmartBigScreenService.getequipmentFailure(tunnelId);
    }

    /**
     * 查询预案列表
     * @return
     */
    @GetMapping("/getReservePlan")
    public AjaxResult getReservePlan(String tunnelId){
        return sdSmartBigScreenService.getReservePlan(tunnelId);
    }

    /**
     * 查询周边物资
     * @return
     */
    @GetMapping("/getEmergencyMaterials")
    public AjaxResult getEmergencyMaterials(String tunnelId){
        return sdSmartBigScreenService.getEmergencyMaterials(tunnelId);
    }

    /**
     * 查询值班人员
     * @param tunnelId
     * @return
     */
    @GetMapping("/getEmergencyPer")
    public AjaxResult getEmergencyPer(String tunnelId){
        return sdSmartBigScreenService.getEmergencyPer(tunnelId);
    }
}
