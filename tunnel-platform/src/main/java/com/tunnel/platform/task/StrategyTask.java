package com.tunnel.platform.task;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.event.SdStrategy;
import com.tunnel.business.domain.event.SdStrategyRl;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.event.SdStrategyMapper;
import com.tunnel.business.mapper.event.SdStrategyRlMapper;
import com.tunnel.business.mapper.event.SdTriggerMapper;
import com.tunnel.business.utils.util.CommonUtil;
import com.tunnel.platform.service.SdDeviceControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Component("strategyTask")
public class StrategyTask {
    private static final Logger log = LoggerFactory.getLogger(StrategyTask.class);
    @Resource
    private RedisCache redisCache;
    /**
     * 定时、分时控制策略定时任务
     * @param strategyRlId
     */
    public void strategyParams(String strategyRlId) throws UnknownHostException {
        SdStrategyRl sdStrategyRl = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlById(Long.valueOf(strategyRlId));
        String[] split = sdStrategyRl.getEquipments().split(",");
        for (String devId : split){
            Map<String,Object> map = new HashMap<>();
            SdStrategy sdStrategy = SpringUtils.getBean(SdStrategyMapper.class).selectSdStrategyById(sdStrategyRl.getStrategyId());
            map.put("devId",devId);
            map.put("state",sdStrategyRl.getState());
            map.put("controlType",sdStrategy.getStrategyType());
            map.put("operIp",InetAddress.getLocalHost().getHostAddress());
            map.put("controlTime", CommonUtil.formatDate(new Date())+" "+sdStrategyRl.getControlTime());
            SpringUtils.getBean(SdDeviceControlService.class).controlDevices(map);
        }
    }

    /**
     * 自动触发定时任务
     */
//    @Scheduled(fixedRate = 3000)
    public void triggerJob() throws UnknownHostException {
        //触发器数据
        Map<String,Object> map = new HashMap<>();
        String serverIp = InetAddress.getLocalHost().getHostAddress();
        List<Map> triggerData = SpringUtils.getBean(SdTriggerMapper.class).getAllTrigger();
        triggerData.forEach(s->{
            String equipment = s.get("device_id").toString();
            String itemId = s.get("element_id").toString();
            String[] equipmentIds = equipment.split(",");
            for(String eq:equipmentIds){
                //redis取当前设备实时数据
                SdDeviceData deviceData = redisCache.getCacheMapValue("deviceData",eq+"-"+itemId);
                if(deviceData == null){
                    SdDeviceData sdDeviceData = new SdDeviceData();
                    sdDeviceData.setDeviceId(eq);
                    sdDeviceData.setItemId(Long.valueOf(itemId));
                    deviceData = SpringUtils.getBean(SdDeviceDataMapper.class).selectLastRecord(sdDeviceData);
                    if(deviceData == null){
                        continue;
                    }
                }
                BigDecimal realTimeData = new BigDecimal(deviceData.getData()).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal compareValue = new BigDecimal(s.get("compare_value").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                String upState = s.get("upstate").toString();
                Integer comparePattern = Integer.valueOf(s.get("compare_pattern").toString());
                //比较设备实时数据与触发值
                int compare = realTimeData.compareTo(compareValue);
                boolean isControl = false;
                switch (comparePattern){
                    //(0:>；1:>=；2:<；3:<=；4:==；5:!=；6:in；7:between；)
                    case 0 : isControl = compare == 1; break;
                    case 1 : isControl = compare == 1 || compare == 0; break;
                    case 2 : isControl = compare == -1; break;
                    case 3 : isControl = compare == -1 || compare == 0; break;
                    case 4 : isControl = compare == 0; break;
                    default: isControl = false;
                        break;
                }
                //下发设备
                if(isControl){
                    Arrays.stream(equipmentIds).forEach(eqId->{
                        map.put("devId",eqId);
                        map.put("state",upState);
                        map.put("controlType",s.get("strategy_type").toString());
                        map.put("operIp",serverIp);
                        SpringUtils.getBean(SdDeviceControlService.class).controlDevices(map);
                        map.clear();
                    });
                    break;
                }
            }
        });
    }
}
