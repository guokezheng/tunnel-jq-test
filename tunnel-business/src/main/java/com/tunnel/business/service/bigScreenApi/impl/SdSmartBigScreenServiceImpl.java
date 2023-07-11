package com.tunnel.business.service.bigScreenApi.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DictTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.EventStateEnum;
import com.tunnel.business.datacenter.domain.enumeration.FaultStatusEnum;
import com.tunnel.business.datacenter.domain.enumeration.PrevControlTypeEnum;
import com.tunnel.business.domain.bigScreenApi.SdEventWarning;
import com.tunnel.business.domain.event.SdRoadSectionStatistics;
import com.tunnel.business.mapper.bigScreenApi.SdSmartBigScreenMapper;
import com.tunnel.business.service.bigScreenApi.SdSmartBigScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhai
 * @date 2022/11/7
 */
@Service
public class SdSmartBigScreenServiceImpl implements SdSmartBigScreenService {

    @Autowired
    private SdSmartBigScreenMapper sdSmartBigScreenMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisTemplate redisTemplate;

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
        List<Integer> eventList = sdSmartBigScreenMapper.getEventList(tunnelId, DictTypeEnum.prev_control_type.getCode(), PrevControlTypeEnum.TRAFFIC_NCIDENT.getCode());
        //查询主动安全
        List<Integer> warningList = sdSmartBigScreenMapper.getEventList(tunnelId, DictTypeEnum.prev_control_type.getCode(), PrevControlTypeEnum.ACTIVE_SAFETY.getCode());
        //查询设备故障
        List<Integer> faultList = sdSmartBigScreenMapper.getFaultList(tunnelId);
        map.put("event",eventList);
        map.put("warning",warningList);
        map.put("fault",faultList);
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult getCumulativeAlarm(String tunnelId) {
        //查询交通事件
        BigDecimal cumulativeEvent = new BigDecimal(sdSmartBigScreenMapper.getCumulativeEvent(tunnelId, DictTypeEnum.prev_control_type.getCode(), PrevControlTypeEnum.TRAFFIC_NCIDENT.getCode()));
        //查询主动安全
        BigDecimal cumulativeWarning = new BigDecimal(sdSmartBigScreenMapper.getCumulativeWarning(tunnelId, DictTypeEnum.prev_control_type.getCode(), PrevControlTypeEnum.ACTIVE_SAFETY.getCode()));
        //查询设备故障
        BigDecimal cumulativeFault = new BigDecimal(sdSmartBigScreenMapper.getCumulativeFault(tunnelId));
        //查询累计分析列表
        List<Map<String, Object>> cumulativeAlarmList = sdSmartBigScreenMapper.getCumulativeAlarmList(tunnelId, DictTypeEnum.prev_control_type.getCode(),
                                                                                                        DictTypeEnum.sd_event_state.getCode(),
                                                                                                        DictTypeEnum.fault_type.getCode(),
                                                                                                        DictTypeEnum.fault_remove_statue.getCode(),
                                                                                                        PrevControlTypeEnum.TRAFFIC_NCIDENT.getCode(),
                                                                                                        PrevControlTypeEnum.ACTIVE_SAFETY.getCode());
        List<Map<String, Object>> list = new ArrayList<>();
        //预警事件数量总和
        BigDecimal dataCount = cumulativeEvent.add(cumulativeWarning).add(cumulativeFault);
        //计算交通事件数量、百分比
        list.add(trafficIncident(cumulativeEvent,dataCount));
        //计算主动安全数量、百分比
        list.add(activeSafety(cumulativeWarning,dataCount));
        //计算设备故障数量、百分比
        list.add(equipmentFailure(cumulativeFault,dataCount));
        cumulativeAlarmList.stream().forEach(item -> {
            if(item.get("eventTime") == null){
                item.put("eventTime",null);
            }
        });
        //累计分析列表
        Map<String, Object> map = new HashMap<>();
        map.put("eventPercentage",list);
        map.put("cumulativeAlarmList",cumulativeAlarmList);
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult getTrafficIncident(String tunnelId) {
        return AjaxResult.success(sdSmartBigScreenMapper.getTrafficIncident(tunnelId, DictTypeEnum.prev_control_type.getCode(), PrevControlTypeEnum.TRAFFIC_NCIDENT.getCode()));
    }

    @Override
    public AjaxResult getActiveSafety(String tunnelId) {
        return AjaxResult.success(sdSmartBigScreenMapper.getActiveSafety(tunnelId, DictTypeEnum.prev_control_type.getCode(), PrevControlTypeEnum.ACTIVE_SAFETY.getCode()));
    }

    @Override
    public AjaxResult getequipmentFailure(String tunnelId) {
        return AjaxResult.success(sdSmartBigScreenMapper.getequipmentFailure(tunnelId,
                                                                             DictTypeEnum.sd_direction.getCode(),
                                                                             DictTypeEnum.fault_type.getCode()));
    }

    @Override
    public AjaxResult getReservePlan(String tunnelId) {
        List<Map<String, Object>> reservePlan = sdSmartBigScreenMapper.getReservePlan(tunnelId);
        return AjaxResult.success(reservePlan);
    }

    @Override
    public AjaxResult getEmergencyMaterials(String tunnelId) {
        List<Map<String, Object>> emergencyMaterials = sdSmartBigScreenMapper.getEmergencyMaterials(tunnelId);
        return AjaxResult.success(emergencyMaterials);
    }

    @Override
    public AjaxResult getEmergencyPer(String tunnelId) {
        return AjaxResult.success(sdSmartBigScreenMapper.getEmergencyPer(tunnelId));
    }

    @Override
    public AjaxResult getEmergencyVehicle(String tunnelId) {
        return AjaxResult.success(sdSmartBigScreenMapper.getEmergencyVehicle(tunnelId));
    }

    @Override
    public AjaxResult getAlarmInformation(String tunnelId) {
        return AjaxResult.success(sdSmartBigScreenMapper.getAlarmInformation(tunnelId));
    }

    @Override
    public AjaxResult getRiskIndicators(String tunnelId) {
        List<Map<String, Object>> riskIndicators = sdSmartBigScreenMapper.getRiskIndicators(tunnelId);
        //事件总数量
        BigDecimal numCount = new BigDecimal(0);
        for(int i = 0; i < riskIndicators.size(); i++){
            numCount = numCount.add(new BigDecimal(riskIndicators.get(i).get("eventPercentage").toString()));
        }
        if(numCount.compareTo(new BigDecimal(0)) == 0){
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("id",null);
            map.put("typeName",null);
            map.put("eventPercentage","0");
            list.add(map);
            return AjaxResult.success(list);
        }
        for(Map<String, Object> item : riskIndicators){
            item.put("eventPercentage", new BigDecimal(item.get("eventPercentage").toString()).divide(numCount, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString());
        }
        return AjaxResult.success(riskIndicators);
    }

    @Override
    public AjaxResult getTunnelVehicles(String tunnelId, String roadDir) {
        if(StringUtils.isNull(tunnelId) || StringUtils.isEmpty(tunnelId)){
            Collection<String> keys = redisCache.keys("tunnelVehicleTotal:*");
            List<Map<String, Object>> list = (List<Map<String, Object>>) redisTemplate.opsForValue().multiGet(keys);
            return AjaxResult.success(list);
        }else {
            if(StringUtils.isNotNull(roadDir) && StringUtils.isNotEmpty(roadDir)){
                Map<String, Object> map = (Map<String, Object>)redisCache.getCacheObject("tunnelVehicleTotal:" + tunnelId + ":" + roadDir);
                List<Map<String, Object>> list = new ArrayList<>();
                list.add(map);
                return AjaxResult.success(list);
            }else {
                Collection<String> keys = redisCache.keys("tunnelVehicleTotal:" + tunnelId + ":*");
                List<Map<String, Object>> list = (List<Map<String, Object>>)redisTemplate.opsForValue().multiGet(keys);
                return AjaxResult.success(list);
            }
        }
    }

    @Override
    public AjaxResult getRealCars(String tunnelId, String vehicleLicense) {
        if(StringUtils.isNull(tunnelId) || StringUtils.isEmpty(tunnelId)){
            Collection<String> keys = redisCache.keys("vehicleSnap:*");
            List<Map<String, Object>> list = (List<Map<String, Object>>) redisTemplate.opsForValue().multiGet(keys);
            return AjaxResult.success(list);
        }else {
            if(StringUtils.isNotNull(vehicleLicense) && StringUtils.isNotEmpty(vehicleLicense)){
                Map<String, Object> map = (Map<String, Object>)redisCache.getCacheObject("vehicleSnap:" + tunnelId + ":" + vehicleLicense);
                List<Map<String, Object>> list = new ArrayList<>();
                list.add(map);
                return AjaxResult.success(list);
            }else {
                Collection<String> keys = redisCache.keys("vehicleSnap:" + tunnelId + ":*");
                List<Map<String, Object>> list = (List<Map<String, Object>>)redisTemplate.opsForValue().multiGet(keys);
                return AjaxResult.success(list);
            }
        }
    }

    @Override
    public AjaxResult getCumulativeCar(String tunnelId) {
        //查询当日所有车辆
        Map<String, Object> cumulativeCarAll = sdSmartBigScreenMapper.getCumulativeCar(tunnelId,null);
        //查询客车
        Map<String, Object> cumulativeCarKe = sdSmartBigScreenMapper.getCumulativeCar(tunnelId, "1");
        //查询货车
        Map<String, Object> cumulativeCarHuo = sdSmartBigScreenMapper.getCumulativeCar(tunnelId, "2");
        //查询重点车辆
        Map<String, Object> cumulativeCarKey = sdSmartBigScreenMapper.getCumulativeCar(tunnelId, "3");
        Map<String, Object> map = new HashMap<>();
        map.put("allCars",cumulativeCarAll == null ? 0 : cumulativeCarAll.get("num"));
        map.put("passengerCars",cumulativeCarKe == null ? 0 : cumulativeCarKe.get("num"));
        map.put("goodsCars",cumulativeCarHuo == null ? 0 : cumulativeCarHuo.get("num"));
        map.put("keyCars",cumulativeCarKey == null ? 0 : cumulativeCarKey.get("num"));
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult getTransitCar(String tunnelId) {
        //查询车辆在途数
        Map<String, Object> transitCarAll = sdSmartBigScreenMapper.getTransitCar(tunnelId, null);
        //查询客车
        Map<String, Object> transitCarKe = sdSmartBigScreenMapper.getTransitCar(tunnelId, "1");
        //查询货车
        Map<String, Object> transitCarHuo = sdSmartBigScreenMapper.getTransitCar(tunnelId, "2");
        //查询重点车辆
        Map<String, Object> transitCarKey = sdSmartBigScreenMapper.getTransitCar(tunnelId, "3");
        Map<String, Object> map = new HashMap<>();
        map.put("allCars",transitCarAll == null ? 0 : transitCarAll.get("num"));
        map.put("passengerCars",transitCarKe == null ? 0 : transitCarKe.get("num"));
        map.put("goodsCars",transitCarHuo == null ? 0 : transitCarHuo.get("num"));
        map.put("keyCars",transitCarKey == null ? 0 : transitCarKey.get("num"));
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult getHoursTrafficVolume(String tunnelId) {
        Map<String, Object> map = new HashMap<>();
        //客车
        List<Map<String, Object>> carKe = sdSmartBigScreenMapper.getHoursTrafficVolume(tunnelId, "1");
        //货车
        List<Map<String, Object>> carHuo = sdSmartBigScreenMapper.getHoursTrafficVolume(tunnelId, "2");
        //重点
        List<Map<String, Object>> carKey = sdSmartBigScreenMapper.getHoursTrafficVolume(tunnelId, "3");
        map.put("ke",carKe);
        map.put("huo",carHuo);
        map.put("key",carKey);
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult getStatisticalDevice(String tunnelId) {
        List<Map<String, Object>> devList = sdSmartBigScreenMapper.getStatisticalDevice(tunnelId);
        int devNum = 0;
        int faultNum = 0;
        for(Map<String, Object> item : devList){
            int eqCount = Integer.valueOf(item.get("eqNum").toString());
            int fCount = Integer.valueOf(item.get("fNum").toString());
            devNum = devNum + eqCount;
            faultNum = faultNum + fCount;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("devNum",devNum);
        map.put("faultNum",faultNum);
        BigDecimal dev = new BigDecimal(devNum);
        BigDecimal fault = new BigDecimal(faultNum);
        BigDecimal divide = fault.divide(dev,5,BigDecimal.ROUND_DOWN);
        BigDecimal multiply = divide.multiply(new BigDecimal(100)).setScale(1,BigDecimal.ROUND_HALF_UP);
        map.put("failureRate",multiply);
        map.put("list",devList);
        return AjaxResult.success(map);
    }

    public List<Map<String, Object>> dataStatistics(List<Map<String, Object>> eventWarning, List<Map<String, Object>> faultWarning){
        List<Map<String, Object>> list = new ArrayList<>();
        //已完成数量
        BigDecimal completed = new BigDecimal(0);
        //未完成数量
        BigDecimal noCompleted = new BigDecimal(0);
        for(Map<String, Object> item : eventWarning){
            String eventState = item.get("eventState").toString();
            if(EventStateEnum.processing.getCode().equals(eventState) || EventStateEnum.unprocessed.getCode().equals(eventState)){
                noCompleted = noCompleted.add(new BigDecimal(item.get("eventNumber").toString()));
            }else {
                completed = completed.add(new BigDecimal(item.get("eventNumber").toString()));
            }
        }
        for(Map<String, Object> item : faultWarning){
            String falltRemoveStatue = item.get("falltRemoveStatue").toString();
            if(FaultStatusEnum.DEVICE_NO_REMOVE.getCode().equals(falltRemoveStatue)){
                noCompleted = noCompleted.add(new BigDecimal(item.get("eventNumber").toString()));
            }else {
                completed = completed.add(new BigDecimal(item.get("eventNumber").toString()));
            }
        }
        Map<String, Object> map = new HashMap<>();
        //0：已完成 1：未完成
        map.put("eventState", "0");
        map.put("eventNumber", completed);
        if(eventWarning.size() == 0 && faultWarning.size() == 0){
            map.put("percentage", 0);
        }else {
            map.put("percentage", completed.divide(completed.add(noCompleted),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_DOWN));
        }

        list.add(map);
        map = new HashMap<>();
        map.put("eventState", "1");
        map.put("eventNumber", noCompleted);
        if(eventWarning.size() == 0 && faultWarning.size() == 0){
            map.put("percentage", 0);
        }else {
            map.put("percentage", noCompleted.divide(completed.add(noCompleted),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_DOWN));
        }
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
        if(cumulativeEvent.compareTo(new BigDecimal(0)) == 0){
            map.put("percentage",0);
        }else {
            map.put("percentage",cumulativeEvent.divide(dataCount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_DOWN));
        }
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
        if(cumulativeWarning.compareTo(new BigDecimal(0)) == 0){
            map.put("percentage",0);
        }else {
            map.put("percentage",cumulativeWarning.divide(dataCount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_DOWN));
        }
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
        if(cumulativeFault.compareTo(new BigDecimal(0)) == 0){
            map.put("percentage",0);
        }else {
            map.put("percentage",cumulativeFault.divide(dataCount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_DOWN));
        }
        return map;
    }
}
