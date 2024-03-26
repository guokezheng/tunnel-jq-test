package com.tunnel.platform.controller.bigScreenApi;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.service.bigScreenApi.ISdTunnelZtService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 环境监测、设备、事件-中台Controller
 *
 * @author tianjainwgei
 * @date 2023-1-12
 */
@RestController
@RequestMapping("/tunnelBaseData")
public class SdTunnelZtController extends BaseController {

    @Autowired
    private ISdTunnelZtService sdTunnelZtService;

    /**
     * 查询当天预警事件
     * @return
     */
    @ApiOperation("查询当天预警事件")
    @PostMapping("/event")
    public AjaxResult getTunnelEventNumber(String tunnelId){
        //今天开始
        String createTimeBegin = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00") .withZone(ZoneId.systemDefault()).format(Instant.now().minus(0, ChronoUnit.DAYS));
        //今天结束
        String createTimeEnd = DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59") .withZone(ZoneId.systemDefault()).format(Instant.now().minus(0, ChronoUnit.DAYS));
        List<Map> tunnelEventNumber = new ArrayList<>();
        tunnelEventNumber = sdTunnelZtService.getTunnelEventNumber(createTimeBegin,createTimeEnd,tunnelId);
        return AjaxResult.success(tunnelEventNumber);
    }

    /**
     * 大屏-环境监测风力风向
     * @param tunnelId
     * @return
     */
    @ApiOperation("大屏-环境监测风力风向")
    @PostMapping("/getWindPowerDirection")
    public AjaxResult getWindPowerDirection(String tunnelId){
        List<Map> tunnelWindData = sdTunnelZtService.getWindPowerDirection(tunnelId);
        return AjaxResult.success(tunnelWindData);
    }


    /**
     * 大屏-环境监测CO浓度
     * @param tunnelId
     * @return
     */
    @ApiOperation("大屏-环境监测CO浓度")
    @PostMapping("/getCOPotency")
    public AjaxResult getCOPotency(String tunnelId){
        List<Map> tunnelCO = sdTunnelZtService.getCOPotency(tunnelId);
        return AjaxResult.success(tunnelCO);
    }

    /**
     * 大屏-环境监测能见度
     * @param tunnelId
     * @return
     */
    @ApiOperation("大屏-环境监测能见度")
    @PostMapping("/getVisibility")
    public AjaxResult getVisibility(String tunnelId){
        List<Map> tunnelVisibility = sdTunnelZtService.getVisibility(tunnelId);
        return AjaxResult.success(tunnelVisibility);
    }


    /**
     * 大屏-环境监测液位
     * @param tunnelId
     * @return
     */
    @ApiOperation("大屏-环境监测液位")
    @PostMapping("/getLiquidLevel")
    public AjaxResult getLiquidLevel(String tunnelId){
        List<Map> tunnelLiquidLevel = sdTunnelZtService.getLiquidLevel(tunnelId);
        return AjaxResult.success(tunnelLiquidLevel);
    }

    /**
     * 大屏-环境监测光照监测
     * @param tunnelId
     * @return
     */
    @ApiOperation("大屏-环境监测光照监测")
    @PostMapping("/getLightDetection")
    public AjaxResult getLightDetection(String tunnelId){
        List<Map> tunnelLightDetection = sdTunnelZtService.getLightDetection(tunnelId);
        return AjaxResult.success(tunnelLightDetection);
    }

    /**
     * 大屏-环境监测远传压力表
     * @param tunnelId
     * @return
     */
    @ApiOperation("大屏-环境监测远传压力表")
    @PostMapping("/getPressure")
    public AjaxResult getPressure(String tunnelId){
        List<Map> tunnelPressure = sdTunnelZtService.getPressure(tunnelId);
        return AjaxResult.success(tunnelPressure);
    }

    /**
     * 大屏-设备数据
     * @param tunnelId
     * @return
     */
    @ApiOperation("大屏-设备数据")
    @PostMapping("/getDeviceData")
    public AjaxResult getDeviceData(String tunnelId){
        List<Map> tunnelDeviceData = sdTunnelZtService.getDeviceData(tunnelId);
        return AjaxResult.success(tunnelDeviceData);
    }


}
