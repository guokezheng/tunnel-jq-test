package com.tunnel.business.service.bigScreenApi.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.bigScreenApi.SdEventWarning;
import com.tunnel.business.mapper.bigScreenApi.SdSmartBigScreenMapper;
import com.tunnel.business.service.bigScreenApi.SdSmartBigScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhai
 * @date 2022/11/7
 */
@Service
public class SdSmartBigScreenServiceImpl implements SdSmartBigScreenService {

    @Autowired
    private SdSmartBigScreenMapper sdSmartBigScreenMapper;

    @Override
    public Map<String, Object> getEventWarning(String tunnelId) {
        List<SdEventWarning> sdEvents = sdSmartBigScreenMapper.seleteEventWarning(tunnelId);
        List<Map<String, Object>> eventProportion = sdSmartBigScreenMapper.getEventProportion(tunnelId);
        Map<String, Object> map = new HashMap<>();
        map.put("list", sdEvents);
        map.put("eventProportion", eventProportion);
        return map;
    }

    @Override
    public AjaxResult getToDayEventWarning(String tunnelId) {
        List<Map<String, Object>> toDayEventWarning = sdSmartBigScreenMapper.getToDayEventWarning(tunnelId);
        List<Map<String, Object>> toDayFaultWarning = sdSmartBigScreenMapper.getToDayFaultWarning(tunnelId);
        List<Map<String, Object>> list = dataStatistics(toDayEventWarning, toDayFaultWarning);
        return AjaxResult.success(list);
    }

    @Override
    public AjaxResult getToWeekEventWarning(String tunnelId) {
        List<Map<String, Object>> toWeekEventWarning = sdSmartBigScreenMapper.getToWeekEventWarning(tunnelId);
        List<Map<String, Object>> toWeekFaultWarning = sdSmartBigScreenMapper.getToWeekFaultWarning(tunnelId);
        List<Map<String, Object>> list = dataStatistics(toWeekEventWarning, toWeekFaultWarning);
        return AjaxResult.success(list);
    }

    @Override
    public AjaxResult getSameMonthEventWarning(String tunnelId) {
        Map<String, Object> map = new HashMap<>();
        //查询交通事件
        List<Map<String, Object>> eventList = sdSmartBigScreenMapper.getEventList(tunnelId);
        //查询主动安全
        List<Map<String, Object>> warningList = sdSmartBigScreenMapper.getWarningList(tunnelId);
        //查询设备故障
        List<Map<String, Object>> faultList = sdSmartBigScreenMapper.getFaultList(tunnelId);
        map.put("event",eventList);
        map.put("warning",warningList);
        map.put("fault",faultList);
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult getCumulativeAlarm(String tunnelId) {
        //查询交通事件
        BigDecimal cumulativeEvent = new BigDecimal(sdSmartBigScreenMapper.getCumulativeEvent(tunnelId));
        //查询主动安全
        BigDecimal cumulativeWarning = new BigDecimal(sdSmartBigScreenMapper.getCumulativeWarning(tunnelId));
        //查询设备故障
        BigDecimal cumulativeFault = new BigDecimal(sdSmartBigScreenMapper.getCumulativeFault(tunnelId));
        //查询累计分析列表
        List<Map<String, Object>> cumulativeAlarmList = sdSmartBigScreenMapper.getCumulativeAlarmList(tunnelId);
        List<Map<String, Object>> list = new ArrayList<>();
        //预警事件数量总和
        BigDecimal dataCount = cumulativeEvent.add(cumulativeWarning).add(cumulativeFault);
        //计算交通事件数量、百分比
        list.add(trafficIncident(cumulativeEvent,dataCount));
        //计算主动安全数量、百分比
        list.add(activeSafety(cumulativeWarning,dataCount));
        //计算设备故障数量、百分比
        list.add(equipmentFailure(cumulativeFault,dataCount));
        //累计分析列表
        Map<String, Object> map = new HashMap<>();
        map.put("eventPercentage",list);
        map.put("cumulativeAlarmList",cumulativeAlarmList);
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult getTrafficIncident(String tunnelId) {
        return AjaxResult.success(sdSmartBigScreenMapper.getTrafficIncident(tunnelId));
    }

    @Override
    public AjaxResult getActiveSafety(String tunnelId) {
        return AjaxResult.success(sdSmartBigScreenMapper.getActiveSafety(tunnelId));
    }

    @Override
    public AjaxResult getequipmentFailure(String tunnelId) {
        return AjaxResult.success(sdSmartBigScreenMapper.getequipmentFailure(tunnelId));
    }

    @Override
    public AjaxResult getReservePlan() {
        List<Map<String, Object>> reservePlan = sdSmartBigScreenMapper.getReservePlan();
        return AjaxResult.success(reservePlan);
    }

    @Override
    public AjaxResult getEmergencyMaterials() {
        List<Map<String, Object>> emergencyMaterials = sdSmartBigScreenMapper.getEmergencyMaterials();
        return AjaxResult.success(emergencyMaterials);
    }

    @Override
    public AjaxResult getEmergencyPer(String tunnelId) {
        return AjaxResult.success(sdSmartBigScreenMapper.getEmergencyPer(tunnelId));
    }

    public List<Map<String, Object>> dataStatistics(List<Map<String, Object>> eventWarning, List<Map<String, Object>> faultWarning){
        List<Map<String, Object>> list = new ArrayList<>();
        //已完成数量
        BigDecimal completed = new BigDecimal(0);
        //未完成数量
        BigDecimal noCompleted = new BigDecimal(0);
        for(Map<String, Object> item : eventWarning){
            String eventState = item.get("eventState").toString();
            if("0".equals(eventState) || "3".equals(eventState)){
                noCompleted = noCompleted.add(new BigDecimal(item.get("eventNumber").toString()));
            }else {
                completed = completed.add(new BigDecimal(item.get("eventNumber").toString()));
            }
        }
        for(Map<String, Object> item : faultWarning){
            String falltRemoveStatue = item.get("falltRemoveStatue").toString();
            if("1".equals(falltRemoveStatue)){
                noCompleted = noCompleted.add(new BigDecimal(item.get("eventNumber").toString()));
            }else {
                completed = completed.add(new BigDecimal(item.get("eventNumber").toString()));
            }
        }
        Map<String, Object> map = new HashMap<>();
        //0：已完成 1：未完成
        map.put("eventState", "0");
        map.put("eventNumber", completed);
        map.put("percentage", completed.divide(completed.add(noCompleted),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_DOWN));
        list.add(map);
        map = new HashMap<>();
        map.put("eventState", "1");
        map.put("eventNumber", noCompleted);
        map.put("percentage", noCompleted.divide(completed.add(noCompleted),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_DOWN));
        list.add(map);
        return list;
    }

    /**
     * 累计预警分析-交通事件
     * @return
     */
    public Map<String, Object> trafficIncident(BigDecimal cumulativeEvent, BigDecimal dataCount){
        Map<String, Object> map = new HashMap<>();
        //计算交通事件数量、百分比
        map.put("eventCount",cumulativeEvent);
        map.put("percentage",cumulativeEvent.divide(dataCount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_DOWN));
        return map;
    }

    /**
     * 累计预警分析-主动安全
     * @return
     */
    public Map<String, Object> activeSafety(BigDecimal cumulativeWarning, BigDecimal dataCount){
        Map<String, Object> map = new HashMap<>();
        //计算交通事件数量、百分比
        map.put("warningCount",cumulativeWarning);
        map.put("percentage",cumulativeWarning.divide(dataCount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_DOWN));
        return map;
    }

    /**
     * 累计预警分析-设备故障
     * @return
     */
    public Map<String, Object> equipmentFailure(BigDecimal cumulativeFault, BigDecimal dataCount){
        Map<String, Object> map = new HashMap<>();
        //计算设备故障数量、百分比
        map.put("faultCount",cumulativeFault);
        map.put("percentage",cumulativeFault.divide(dataCount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_DOWN));
        return map;
    }
}
