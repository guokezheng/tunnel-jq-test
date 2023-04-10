package com.tunnel.platform.task;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.TriggerEventTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.digitalmodel.WJEnum;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdStrategy;
import com.tunnel.business.domain.event.SdStrategyRl;
import com.tunnel.business.domain.event.SdTrigger;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.mapper.event.SdStrategyMapper;
import com.tunnel.business.mapper.event.SdStrategyRlMapper;
import com.tunnel.business.mapper.event.SdTriggerMapper;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.event.ISdEventFlowService;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.event.ISdEventTypeService;
import com.tunnel.business.utils.util.CommonUtil;
import com.tunnel.platform.service.SdDeviceControlService;
import com.zc.common.core.websocket.WebSocketService;
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
     * 定时、分时控制策略执行
     * @param strategyRlId
     * @throws UnknownHostException
     */
    public void strategyParams(String strategyRlId) throws UnknownHostException {
        SdStrategyRl sdStrategyRl = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlById(Long.valueOf(strategyRlId));
        String[] split = sdStrategyRl.getEquipments().split(",");
        for (String devId : split){
            Map<String,Object> map = new HashMap<>();
            SdStrategy sdStrategy = SpringUtils.getBean(SdStrategyMapper.class).selectSdStrategyById(sdStrategyRl.getStrategyId());
            if(DevicesTypeEnum.VMS.getCode().toString().equals(sdStrategyRl.getEqTypeId()) || DevicesTypeEnum.MEN_JIA_VMS.getCode().toString().equals(sdStrategyRl.getEqTypeId())){
                map.put("templateId",sdStrategyRl.getState());
            }
            map.put("devId",devId);
            map.put("state",sdStrategyRl.getState());
            map.put("controlType",sdStrategy.getStrategyType());
            map.put("operIp",InetAddress.getLocalHost().getHostAddress());
            map.put("controlTime", CommonUtil.formatDate(new Date())+" "+sdStrategyRl.getControlTime());

            SpringUtils.getBean(SdDeviceControlService.class).controlDevices(map);
        }
    }

    /**
     * 定时、分时控制策略执行
     * @param strategyRlId
     * @Param type  分时控制   1  开始控制   2  结束控制
     * @throws UnknownHostException
     */
    public void strategyParamsPlus(String strategyRlId,String type) throws UnknownHostException {
        SdStrategyRl sdStrategyRl = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlById(Long.valueOf(strategyRlId));
        String[] split = sdStrategyRl.getEquipments().split(",");
        for (String devId : split){
            Map<String,Object> map = new HashMap<>();
            SdStrategy sdStrategy = SpringUtils.getBean(SdStrategyMapper.class).selectSdStrategyById(sdStrategyRl.getStrategyId());
            map.put("devId",devId);
            map.put("controlType",sdStrategy.getStrategyType());
            map.put("operIp",InetAddress.getLocalHost().getHostAddress());
            if(type.equals("1")){
                map.put("controlTime", CommonUtil.formatDate(new Date())+" "+sdStrategy.getTimerOpen());
                map.put("state",sdStrategyRl.getState());
            }else{
                map.put("controlTime", CommonUtil.formatDate(new Date())+" "+sdStrategy.getTimerClose());
                map.put("state",sdStrategyRl.getEndState());
            }
            SpringUtils.getBean(SdDeviceControlService.class).controlDevices(map);
        }
    }

    /**
     * 触发策略执行
     * @param triggerId
     * @throws UnknownHostException
     */
    public void triggerJob(String triggerId) throws UnknownHostException {
        //SdTrigger trigger = SpringUtils.getBean(SdTriggerMapper.class).selectSdTriggerById(Long.parseLong(triggerId));
        //触发器数据
        List<Map> triggerData = SpringUtils.getBean(SdTriggerMapper.class).getTriggerInfo(triggerId);
        Map<String,Object> issuedParam = new HashMap<>();
        String serverIp = InetAddress.getLocalHost().getHostAddress();
        out: for(Map s:triggerData){
            String equipment = s.get("device_id").toString();
            String itemId = s.get("element_id").toString();
            String[] equipmentIds = equipment.split(",");
            for(String eq:equipmentIds){
                //redis取当前设备实时数据
                SdDeviceData deviceData = redisCache.getCacheMapValue("deviceData",eq+"-"+itemId);
                if(deviceData==null){
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
                    default: isControl = false; break;
                }
                //符合触发条件
                if(isControl){
                    //仅预警
                    if(s.get("warning_type").equals("0")){
                        //插入事件
                        SdEvent sdEvent = new SdEvent();
                        //所有事件类型Map
                        Map<Long,String> eventTypeMap = SpringUtils.getBean(ISdEventTypeService.class).getEventTypeMap();
                        //所有隧道Map
                        Map<String,String> tunnelMap = SpringUtils.getBean(ISdTunnelsService.class).getTunnelNameMap();
                        sdEvent.setTunnelId(s.get("tunnel_id").toString());
                        sdEvent.setEventSource("3");
                        sdEvent.setDirection(s.get("eq_direction").toString());
                        sdEvent.setStakeNum(s.get("pile").toString());
                        if(s.get("lane")!=null){
                            sdEvent.setLaneNo(s.get("lane").toString());
                        }
                        if(s.get("event_type")!=null && !s.get("event_type").equals("")){
                            sdEvent.setEventTypeId(Long.valueOf(s.get("event_type").toString()));
                        }else{
                            sdEvent.setEventTypeId(TriggerEventTypeEnum.getTriggerEventTypeEnum(s.get("eq_type").toString()+s.get("element_id")).getEventType());
                        }
                        sdEvent.setStartTime(DateUtils.getTime());
                        sdEvent.setEventState("3");
                        sdEvent.setCreateTime(DateUtils.getNowDate());
                        String eventTitle = SpringUtils.getBean(ISdEventService.class).getDefaultEventTitle(sdEvent,tunnelMap,eventTypeMap);
                        sdEvent.setEventTitle(eventTitle);
                        //方向
//                        if(!StringUtils.isEmpty(f.getDirection())){
//                            sdEvent.setDirection(f.getDirection() + "");
//                        }
                        int updateRows = SpringUtils.getBean(SdEventMapper.class).insertSdEvent(sdEvent);
                        if(updateRows>0){
                            List<SdEvent> sdEventList = new ArrayList<>();
                            sdEventList.add(sdEvent);
                            JSONObject object = new JSONObject();
                            object.put("sdEventList", sdEventList);
                            WebSocketService.broadcast("sdEventList",object.toString());
                            // 添加事件流程记录
                            SpringUtils.getBean(ISdEventFlowService.class).addEventFlowBatch(sdEventList);
                        }
                    }else{
                        //预警联动控制设备
                        for(Map data:triggerData){
                            String equipments = data.get("equipments").toString();
                            String[] eqIds = equipments.split(",");
                            String alterState = data.get("alterState").toString();
                            Arrays.stream(eqIds).forEach(eqId->{
                                issuedParam.put("devId",eqId);
                                issuedParam.put("state",alterState);
                                issuedParam.put("controlType","2");
                                issuedParam.put("operIp",serverIp);
                                SpringUtils.getBean(SdDeviceControlService.class).controlDevices(issuedParam);
                                issuedParam.clear();
                            });
                        }
                    }
                    break out;
                }
            }
            break;
        }
    }
}
