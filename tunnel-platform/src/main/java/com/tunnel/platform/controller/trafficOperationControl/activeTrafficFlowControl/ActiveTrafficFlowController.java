package com.tunnel.platform.controller.trafficOperationControl.activeTrafficFlowControl;

import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficIncidentInfo;
import com.tunnel.business.service.trafficOperationControl.activeTrafficFlowControl.ActiveTrafficFlowService;
import com.tunnel.business.service.trafficOperationControl.eventManage.ISdTrafficIncidentInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * describe: 主动交通流
 *
 * @author zs
 * @date 2022/2/26
 */
@RestController
@RequestMapping("/activeTrafficFlow")
@Api(tags = "主动交通流")
@ApiSupport(order = 16)
public class ActiveTrafficFlowController {

    @Autowired
    private ActiveTrafficFlowService activeTrafficFlowService;

    @Autowired
    private ISdTrafficIncidentInfoService incidentInfoService;

    /**
     * 主动交通流推送管控措施
     *  查询管控等级配置措施-管控原因，得到实时能见度值、平均速度值、路面情况数据，进行匹配，返回对应的管控措施
     * @return
     */
    @GetMapping("/getActiveTrafficMeasure/{id}")
    @ApiOperation("查询管控等级配置措施-管控原因，得到实时能见度值、平均速度值、路面情况数据，进行匹配，返回对应的管控措施")
    public AjaxResult getActiveTrafficMeasure(@PathVariable("id") Long incidentId){
        SdTrafficIncidentInfo incidentInfo = incidentInfoService.selectSdTrafficIncidentInfoById(incidentId);
        String tunnelId = incidentInfo.getTunnelId();
        if(tunnelId == null || tunnelId.isEmpty()){
            return AjaxResult.success();
        }
        JSONObject jsonObject = activeTrafficFlowService.getActiveTrafficMeasure(tunnelId);

        return AjaxResult.success(jsonObject);
    }


    /**
     * 主动交通流推送管控措施
     *
     * @return
     */
    @GetMapping("/getActiveTrafficMeasureByTunnelId")
    @ApiOperation("主动交通流推送管控措施")
    public AjaxResult getActiveTrafficMeasureByTunnelId(String tunnelId){

       JSONObject jsonObject = activeTrafficFlowService.getActiveTrafficMeasure(tunnelId);

        return AjaxResult.success(jsonObject);
    }



    /**
     * 获取突发事件的主动交通流推送措施
     * @param incidentType 事件类型
     * @param incidentGrade 事件级别
     * @return
     */
    @GetMapping("/getActiveMeasureWithEmergencyIncident")
    @ApiOperation("获取突发事件的主动交通流推送措施")
    public AjaxResult getActiveMeasureWithEmergencyIncident(String incidentType,String incidentGrade){
        JSONObject jsonObject = activeTrafficFlowService.getActiveMeasureWithEmergencyIncident(incidentType,incidentGrade);
        return AjaxResult.success(jsonObject);
    }
}
