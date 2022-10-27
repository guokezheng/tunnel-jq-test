package com.tunnel.platform.task;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.quartz.task.RyTask;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.event.SdStrategyRl;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.event.SdStrategyRlMapper;
import com.tunnel.business.mapper.event.SdTriggerMapper;
import com.tunnel.platform.service.SdDeviceControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("strategyTask")
public class StrategyTask {
    private static final Logger log = LoggerFactory.getLogger(StrategyTask.class);
    @Resource
    private RedisCache redisCache;
    /**
     * 定时、分时控制策略定时任务
     * @param strategyRlId
     */
    public void strategyParams(String strategyRlId) {
        System.err.println("---------------------------------strategyRlId  "+strategyRlId);

        SdStrategyRl sdStrategyRl = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlById(Long.valueOf(strategyRlId));
        String[] split = sdStrategyRl.getEquipments().split(",");
        for (String devId : split){
            Map<String,Object> map = new HashMap<>();
            map.put("devId",devId);
            map.put("state",sdStrategyRl.getState());
            map.put("controlType","3");
            SpringUtils.getBean(SdDeviceControlService.class).controlDevices(map);
        }
    }

    /**
     * 自动触发定时任务
     */
//    @Scheduled(fixedRate = 3000)
    public void triggerJob(){
        System.err.println("---------------------------------triggerJob  正在执行");

//        //触发器数据
//        List<Map> triggerData = SpringUtils.getBean(SdTriggerMapper.class).getAllTrigger();
//        Map<String,Object> map = new HashMap<>();
//        triggerData.forEach(s->{
//            String equipment = s.get("device_id").toString();
//            String itemId = s.get("element_id").toString();
//            String[] equipmentIds = equipment.split(",");
//            for(String eq:equipmentIds){
//                //redis取当前设备实时数据
//                SdDeviceData deviceData = redisCache.getCacheMapValue("deviceData",eq+"-"+itemId);
//                if(deviceData == null){
//                    SdDeviceData sdDeviceData = new SdDeviceData();
//                    sdDeviceData.setDeviceId(eq);
//                    sdDeviceData.setItemId(Long.valueOf(itemId));
//                    deviceData = SpringUtils.getBean(SdDeviceDataMapper.class).selectLastRecord(sdDeviceData);
//                    if(deviceData == null){
//                        continue;
//                    }
//                }
//                BigDecimal realTimeData = new BigDecimal(deviceData.getData()).setScale(2, BigDecimal.ROUND_HALF_UP);
//                BigDecimal compareValue = new BigDecimal((Integer)s.get("compare_value")).setScale(2, BigDecimal.ROUND_HALF_UP);
//                String upState = s.get("upstate").toString();
//                Integer comparePattern = (Integer)s.get("compare_pattern");
//                //比较设备实时数据与触发值
//                int compare = realTimeData.compareTo(compareValue);
//                boolean isControl = false;
//                switch (comparePattern){
//                    //(0:>；1:>=；2:<；3:<=；4:==；5:!=；6:in；7:between；)
//                    case 0 : isControl = compare == 1;break;
//                    case 1 : isControl = compare == 1 || compare == 0;break;
//                    case 2 : isControl = compare == -1;break;
//                    case 3 : isControl = compare == -1 || compare == 0;break;
//                    case 4 : isControl = compare == 0;break;
//                    default: isControl = false;
//                        break;
//                }
//                //下发设备
//                if(isControl){
//                    Arrays.stream(equipmentIds).forEach(eqId->{
//                        map.put("devId",eqId);
//                        map.put("state",upState);
//                        map.put("controlType","3");
//                        SpringUtils.getBean(SdDeviceControlService.class).controlDevices(map);
//                        map.clear();
//                    });
//                    break;
//                }
//            }
//        });
    }
}
