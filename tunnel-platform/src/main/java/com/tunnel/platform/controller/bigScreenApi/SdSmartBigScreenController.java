package com.tunnel.platform.controller.bigScreenApi;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.service.bigScreenApi.SdSmartBigScreenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 大屏
 *
 * @author zhai
 * @date 2022/11/7
 */
@RestController
@RequestMapping("/eventWarning")
@Api(tags = "大屏管理")
@ApiSupport(order = 16)
public class SdSmartBigScreenController extends BaseController {

    @Autowired
    private SdSmartBigScreenService sdSmartBigScreenService;

    /**
     * 查询本月预警事件
     * @return
     */
    @ApiOperation("查询本月预警事件")
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
    @ApiOperation("查询当天报警事件")
    @GetMapping("/getToDayEventWarning")
    public AjaxResult getToDayEventWarning(String tunnelId){
        return sdSmartBigScreenService.getToDayEventWarning(tunnelId);
    }

    /**
     * 查询本周报警事件
     * @param tunnelId
     * @return
     */
    @ApiOperation("查询本周报警事件")
    @GetMapping("/getToWeekEventWarning")
    public AjaxResult getToWeekEventWarning(String tunnelId){
        return sdSmartBigScreenService.getToWeekEventWarning(tunnelId);
    }

    /**
     * 查询本月报警事件
     * @param tunnelId
     * @return
     */
    @ApiOperation("查询本月报警事件")
    @GetMapping("/getSameMonthEventWarning")
    public AjaxResult getSameMonthEventWarning(String tunnelId){
        return sdSmartBigScreenService.getSameMonthEventWarning(tunnelId);
    }

    /**
     * 累计预警分析
     * @param tunnelId
     * @return
     */
    @ApiOperation("累计预警分析")
    @GetMapping("/getCumulativeAlarm")
    public AjaxResult getCumulativeAlarm(String tunnelId){
        return sdSmartBigScreenService.getCumulativeAlarm(tunnelId);
    }

    /**
     * 查询交通事件信息列表
     * @param tunnelId
     * @return
     */
    @ApiOperation("查询交通事件信息列表")
    @GetMapping("/getTrafficIncident")
    public AjaxResult getTrafficIncident(String tunnelId){
        return sdSmartBigScreenService.getTrafficIncident(tunnelId);
    }

    /**
     * 查询主动安全信息列表
     * @param tunnelId
     * @return
     */
    @ApiOperation("查询主动安全信息列表")
    @GetMapping("/getActiveSafety")
    public AjaxResult getActiveSafety(String tunnelId){
        return sdSmartBigScreenService.getActiveSafety(tunnelId);
    }

    /**
     * 查询设备故障信息列表
     * @param tunnelId
     * @return
     */
    @ApiOperation("查询设备故障信息列表")
    @GetMapping("/getequipmentFailure")
    public AjaxResult getequipmentFailure(String tunnelId){
        return sdSmartBigScreenService.getequipmentFailure(tunnelId);
    }

    /**
     * 查询预案列表
     * @return
     */
    @ApiOperation("查询预案列表")
    @GetMapping("/getReservePlan")
    public AjaxResult getReservePlan(String tunnelId){
        return sdSmartBigScreenService.getReservePlan(tunnelId);
    }

    /**
     * 查询周边物资
     * @return
     */
    @ApiOperation("查询周边物资")
    @GetMapping("/getEmergencyMaterials")
    public AjaxResult getEmergencyMaterials(String tunnelId){
        return sdSmartBigScreenService.getEmergencyMaterials(tunnelId);
    }

    /**
     * 查询值班人员
     * @param tunnelId
     * @return
     */
    @ApiOperation("查询值班人员")
    @GetMapping("/getEmergencyPer")
    public AjaxResult getEmergencyPer(String tunnelId){
        return sdSmartBigScreenService.getEmergencyPer(tunnelId);
    }

    /**
     * 查询应急车辆
     * @param tunnelId
     * @return
     */
    @ApiOperation("查询应急车辆")
    @GetMapping("/getEmergencyVehicle")
    public AjaxResult getEmergencyVehicle(String tunnelId){
        return sdSmartBigScreenService.getEmergencyVehicle(tunnelId);
    }

    /**
     * 查询报警信息
     * @param tunnelId
     * @return
     */
    @ApiOperation("查询报警信息")
    @GetMapping("/getAlarmInformation")
    public AjaxResult getAlarmInformation(String tunnelId){
        return sdSmartBigScreenService.getAlarmInformation(tunnelId);
    }

    /**
     * 查询风险指标
     *
     * @param tunnelId
     * @return
     */
    @ApiOperation("查询风险指标")
    @GetMapping("/getRiskIndicators")
    public AjaxResult getRiskIndicators(String tunnelId){
        return sdSmartBigScreenService.getRiskIndicators(tunnelId);
    }

    /**
     * 查询隧道内车辆数、车辆均速
     *
     * @param tunnelId
     * @param roadDir
     * @return
     */
    @ApiOperation("查询隧道内车辆数、车辆均速")
    @GetMapping("/getTunnelVehicles")
    public AjaxResult getTunnelVehicles(String tunnelId, String roadDir){
        return sdSmartBigScreenService.getTunnelVehicles(tunnelId,roadDir);
    }

    /**
     * 查询实时车辆
     *
     * @param tunnelId
     * @param vehicleLicense
     * @return
     */
    @ApiOperation("查询实时车辆")
    @GetMapping("/getRealCars")
    public AjaxResult getRealCars(String tunnelId, String vehicleLicense){
        return sdSmartBigScreenService.getRealCars(tunnelId,vehicleLicense);
    }

    /**
     * 查询当日累计车辆
     * @param tunnelId
     * @return
     */
    @ApiOperation("查询当日累计车辆")
    @GetMapping("/getCumulativeCar")
    public AjaxResult getCumulativeCar(String tunnelId){
        return sdSmartBigScreenService.getCumulativeCar(tunnelId);
    }

    /**
     * 查询车辆在途数
     * @param tunnelId
     * @return
     */
    @ApiOperation("查询车辆在途数")
    @GetMapping("/getTransitCar")
    public AjaxResult getTransitCar(String tunnelId){
        return sdSmartBigScreenService.getTransitCar(tunnelId);
    }

    /**
     * 查询24小时车流量
     * @param tunnelId
     * @return
     */
    @ApiOperation("查询24小时车流量")
    @GetMapping("/getHoursTrafficVolume")
    public AjaxResult getHoursTrafficVolume(String tunnelId){
        return sdSmartBigScreenService.getHoursTrafficVolume(tunnelId);
    }

    /**
     * 统计设备状态
     * @param tunnelId
     * @return
     */
    @ApiOperation("统计设备状态")
    @GetMapping("/getStatisticalDevice")
    public AjaxResult getStatisticalDevice(String tunnelId){
        return sdSmartBigScreenService.getStatisticalDevice(tunnelId);
    }

    /**
     * 模拟不降速报表数据
     * @return
     */
    @ApiOperation("模拟不降速报表数据")
    @GetMapping("/getNoDeceleration")
    public AjaxResult getNoDeceleration(){
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> one = new HashMap<>();
        List<Integer> oneListOne = new ArrayList<>();
        List<Integer> oneListTwo = new ArrayList<>();
        int oneCountOne = 95;
        int oneCountTwo = 99;
        int twoCountOne = 105;
        int twoCountTwo = 110;
        int threeCount = 120;
        for(int i = 0; i < 10; i++){
            if(i < 5){
                oneCountOne = oneCountOne +1;
                oneListOne.add(oneCountOne);
            }else {
                oneCountTwo = oneCountTwo +1;
                oneListTwo.add(oneCountTwo);
            }
        }
        one.put("oneListOne",oneListOne);
        one.put("oneListTwo",oneListTwo);
        map.put("oneLane",one);
        Map<String, Object> two = new HashMap<>();
        List<Integer> twoListOne = new ArrayList<>();
        List<Integer> twoListTwo = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            if(i < 5){
                twoCountOne = twoCountOne +1;
                twoListOne.add(twoCountOne);
            }else {
                twoCountTwo = twoCountTwo -1;
                twoListTwo.add(twoCountTwo);
            }
        }
        two.put("twoListOne",twoListOne);
        two.put("twoListTwo",twoListTwo);
        map.put("twoLane",two);
        Map<String, Object> three = new HashMap<>();
        List<Integer> threeListOne = new ArrayList<>();
        List<Integer> threeListTwo = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            threeCount = threeCount -1;
            if(i < 5){
                threeListOne.add(threeCount);
            }else {
                threeListTwo.add(threeCount);
            }
        }
        three.put("threeListOne",threeListOne);
        three.put("threeListTwo",threeListTwo);
        map.put("threeLane",three);
        return AjaxResult.success(map);
    }

    /**
     * 隧道平均车速以及车流
     * @return
     */
    @ApiOperation("隧道平均车速以及车流")
    @GetMapping("/getAllTunnelVehicleSpeed")
    public AjaxResult getAllTunnelVehicleSpeed(){
        return sdSmartBigScreenService.getAllTunnelVehicleSpeed();
    }

    /**
     * 隧道平均车速
     * @return
     */
    @ApiOperation("隧道平均车速")
    @GetMapping("/getTunnelSpeed")
    public AjaxResult getTunnelSpeed(){
        return sdSmartBigScreenService.getTunnelSpeed();
    }

    /**
     * 获取各隧道数据
     * @return
     */
    @ApiOperation("获取各隧道数据")
    @GetMapping("/getTunnelStatis")
    public AjaxResult getTunnelStatis(){
        return sdSmartBigScreenService.getTunnelStatis();
    }

    /**
     * 预警统计
     * @param type
     * @param deptId
     * @return
     */
    @ApiOperation("预警统计")
    @GetMapping("/getWarningStatistics")
    public AjaxResult getWarningStatistics(@RequestParam String type,
                                           @RequestParam String deptId){
        return sdSmartBigScreenService.getWarningStatistics(type,deptId);
    }

    /**
     * 当日车流量
     * @param tunnelId
     * @return
     */
    @ApiOperation("当日车流量")
    @GetMapping("/getCarNumber")
    public AjaxResult getCarNumber(@RequestParam String tunnelId){
        return sdSmartBigScreenService.getCarNumber(tunnelId);
    }
    /**
     * 图盟公钥
     * @return
     */
    @ApiOperation("图盟公钥")
    @GetMapping("/getEncryption")
    public AjaxResult getEncryption(){
        return sdSmartBigScreenService.getEncryption();
    }
}
