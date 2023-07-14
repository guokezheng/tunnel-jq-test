package com.tunnel.platform.task;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.*;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdDevicesProtocol;
import com.tunnel.business.domain.digitalmodel.WJEnum;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdStrategy;
import com.tunnel.business.domain.event.SdStrategyRl;
import com.tunnel.business.domain.event.SdTrigger;
import com.tunnel.business.mapper.bigScreenApi.SdTunnelZtMapper;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.mapper.event.SdStrategyMapper;
import com.tunnel.business.mapper.event.SdStrategyRlMapper;
import com.tunnel.business.mapper.event.SdTriggerMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesProtocolService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.event.ISdEventFlowService;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.event.ISdEventTypeService;
import com.tunnel.business.utils.util.CommonUtil;
import com.tunnel.deal.mca.service.McaService;
import com.tunnel.platform.business.vms.device.DeviceManagerFactory;
import com.tunnel.platform.service.SdDeviceControlService;
import com.zc.common.core.websocket.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component("strategyTask")
public class StrategyTask {

    private static final Logger log = LoggerFactory.getLogger(StrategyTask.class);

    private static  ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Resource
    private RedisCache redisCache;
    @Resource
    private McaService mcaService;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private SdTunnelZtMapper sdTunnelZtMapper;

    @Autowired
    private SdStrategyRlMapper sdStrategyRlMapper;

    @Autowired
    private ISdDevicesProtocolService sdDevicesProtocolService;

    @Autowired
    private ISdEventService sdEventService;

    /**
     * 懒汉模式实例化。
     */
    private static StrategyTask instance;

    public static StrategyTask getInstance() {
        if (instance == null)
            instance = new StrategyTask();
        return instance;
    }

    /**
     * 找到时间集合 最接近并且大于 当前时间 的数据
     * @param time 定时任务时间
     * @param times 时间集合
     * @return
     */
    public static Optional<LocalTime> findClosestFutureTime(List<String> times,String time) {
//        LocalTime currentTime = LocalTime.now();
        LocalTime currentTime = LocalTime.parse(time);
        //流操作过滤出所有大于当前时间的时间
        List<LocalTime> futureTimes = times.stream()
                .map(LocalTime::parse)
                .filter(t -> t.isAfter(currentTime))
                .sorted()
                .collect(Collectors.toList());
        // Find the future time closest to the current time.
        return futureTimes.stream()
                .findFirst();
    }

    /**
     * 延迟发送请求
     * @param SdStrategyRlCollect
     * @return
     */
    public  void ScheduledExecutor( List<SdStrategyRl> SdStrategyRlCollect) {
        // 创建Random实例
        Random random = new Random();

        // 生成30-180的随机数字
        int randomNumberInRange = random.nextInt(30) + 1;
        executor.schedule(() -> {
            // 在执行要延迟的代码
            try {
                StrategyTask.getInstance().strategyParams(SdStrategyRlCollect.get(0).getId().toString());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }, randomNumberInRange, TimeUnit.SECONDS);
    }

    /**
     * 定时模式下  照明控制 成功检测
     */
    public  void  errorEquipment() throws UnknownHostException {
        //获取所有照明并且已经执行的定时任务
        List<String> scanKey = redisCache.getScanKey("ERROREQUIPMENT"+ "*");

        Set<String> strategyIdList = new HashSet<>();
        for (String key :scanKey){
            //获取照明定时任务的详细控制数据
            Map currentMap = redisCache.getCacheObject(key);

            //获取 设备详细信息
            SdDevices sdDevices = sdDevicesService.selectSdDevicesById(key.split("ERROREQUIPMENT_")[1]);

            //获取设备id 下 所有运行状态为已运行的定时任务
            List<SdStrategyRl> sdStrategyRls = sdStrategyRlMapper.selectSdStrategyREquipments(sdDevices.getEqId());

            //根据设备id查询现场 设备数值信息
            List<SdDeviceData> otoDeviceData = sdTunnelZtMapper.getOtoDeviceData(sdDevices.getEqId(), sdDevices.getEqType().toString());

            if((otoDeviceData.size()>0 && !otoDeviceData.get(0).getData().equals(currentMap.get("brightness").toString()))||(otoDeviceData.size()==0)){//不为空说明有数据 对比控制照明亮度是否相同 下面对比数据和最近定时任务 控制数值是否相同
                //控制判断 方法主要判断是否需要重发
//                controlJacklight(sdStrategyRls,currentMap,key,sdDevices);
                String currentId = String.valueOf(currentMap.get("currentId"));
                List<Long> idList = sdStrategyRls.stream().map(SdStrategyRl::getId).collect(Collectors.toList());
                if(idList.contains(Long.valueOf(currentId))){
                    //redis缓存的定时任务ID在数据库中存在（运行中的定时任务），允许执行
                    strategyIdList.add(currentId);
                }else{
                    //任务被删除或者禁用
                    redisCache.deleteObject(key);
                }

            }else{//说明 设备控制成功删除定时任务
                //删除不需要的定时任务
                redisCache.deleteObject(key);
            }

        }

        //重发失败的定时照明策略
        repeatControl(strategyIdList);

    }

    /**
     * 重发失败的定时照明策略
     * @param strategyIdList
     */
    public void repeatControl(Set<String> strategyIdList){
        for(String strategyId : strategyIdList){
            executor.schedule(() -> {
                // 在执行要延迟的代码
                try {
                    StrategyTask.getInstance().strategyParams(strategyId);
                    log.info("repeatControl重发失败的定时照明策略：strategyId="+strategyId);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    log.error("repeatControl重发失败的定时照明策略报错：strategyId="+strategyId,e.getCause());
                }
            }, 10, TimeUnit.SECONDS);
        }

//        String currentId = String.valueOf(currentMap.get("currentId"));
//        try {
//            StrategyTask.getInstance().strategyParams(currentId);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
    }


    public static void main(String[] args) {
        // 创建Random实例
        Random random = new Random();

        // 生成30-180的随机数字
        int randomNumberInRange = random.nextInt(100) + 30;
        System.out.print(randomNumberInRange);
    }
    public void controlJacklight( List<SdStrategyRl> sdStrategyRls,Map currentMap ,String key ,SdDevices sdDevices ) throws UnknownHostException {
        List<String> times = new ArrayList<>();
        sdStrategyRls.forEach(sd ->{
            times.add(sd.getControlTime());
        });
        //找到 时间最接近并且大于 当前定时任务的时间
        Optional<LocalTime> closestTime = findClosestFutureTime(times, currentMap.get("controlTime").toString().split(" ")[1]);
        //此设备存在有比失败定时任务时间更晚的定时任务
        if (closestTime.isPresent()) {//说明存在数据
            LocalTime localTime = closestTime.get();//失败定时任务 最近定时任务时间
            LocalTime currentTime = LocalTime.now();//当前时间
            //相等 0 小于-1 大于1
            int comparison = localTime.compareTo(currentTime);

            if (comparison < 0) {//失败定时任务 最近定时任务时间 早于当前时间 说明失败的定时任务已经被别的定时任务替代
                //删除不需要的定时任务
                redisCache.deleteObject(key);
            } else if (comparison > 0) {//失败定时任务 最近定时任务时间 晚于当前时间  说明现在阶段还是失败定时任务阶段 还可以重发

                List<SdStrategyRl> SdStrategyRlCollect = sdStrategyRls.stream().filter(sd ->
                        LocalTime.parse(sd.getControlTime()).compareTo( LocalTime.parse( currentMap.get("controlTime").toString().split(" ")[1])) == 0
                        && sd.getEquipments().indexOf(sdDevices.getEqId())!=-1)
                        .sorted(Comparator.comparingLong(SdStrategyRl::getId).reversed()).collect(Collectors.toList());

//                StrategyTask.getInstance().strategyParams(SdStrategyRlCollect.get(0).getId().toString());
                //延迟方法
                ScheduledExecutor(SdStrategyRlCollect);

            }
        } else {//说明后面没有次设备的定时任务了所以可以重发
            List<SdStrategyRl> SdStrategyRlCollect = sdStrategyRls.stream().filter(sd -> sd.getEquipments().indexOf(sdDevices.getEqId())!=-1)
                    .sorted(Comparator.comparingLong(SdStrategyRl::getId).reversed()).collect(Collectors.toList());

//            StrategyTask.getInstance().strategyParams(SdStrategyRlCollect.get(0).getId().toString());
            //延迟方法
            ScheduledExecutor(SdStrategyRlCollect);
        }
    }
    /**
     * 定时、分时控制策略执行
     * @param strategyRlId
     * @throws UnknownHostException
     */
    public  void strategyParams(String strategyRlId) throws UnknownHostException {
        SdStrategyRl sdStrategyRl = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlById(Long.valueOf(strategyRlId));

        SdStrategy sdStrategy = SpringUtils.getBean(SdStrategyMapper.class).selectSdStrategyById(sdStrategyRl.getStrategyId());

        // 默认执行策略关联设备
        String[] split = sdStrategyRl.getEquipments().split(",");
        // 加强照明 基本照明 支持双向执行
        if (sdStrategy.getDirection().equals(DeviceDirectionEnum.ALl.getCode().toString()) &&
                (
                        sdStrategyRl.getEqTypeId().equals(DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().toString()) ||
                        sdStrategyRl.getEqTypeId().equals(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().toString())
                )
        ){
            split = SpringUtils.getBean(SdStrategyRlMapper.class).selectAllDirectionSdDevListByDevId(split,sdStrategy.getTunnelId(),sdStrategyRl.getEqTypeId());

        }

        for (String devId : split){
            Map<String,Object> map = new HashMap<>();

            if(DevicesTypeEnum.VMS.getCode().toString().equals(sdStrategyRl.getEqTypeId()) || DevicesTypeEnum.MEN_JIA_VMS.getCode().toString().equals(sdStrategyRl.getEqTypeId())){
                map.put("templateId",sdStrategyRl.getState());
            }
            map.put("devId",devId);
            map.put("state",sdStrategyRl.getState());
            map.put("brightness",sdStrategyRl.getStateNum());
            map.put("controlType",sdStrategy.getStrategyType());
            map.put("operIp",InetAddress.getLocalHost().getHostAddress());
            map.put("controlTime", CommonUtil.formatDate(new Date())+" "+sdStrategyRl.getControlTime());
            map.put("type","1");
            map.put("currentId",sdStrategyRl.getId());

            //按照设备初始化默认值
           //疏散标志
           if(sdStrategyRl.getEqTypeId().equals(DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode().toString())) {

               // 1 关闭 2常亮
               if(sdStrategyRl.getState().equals("1")){
                   map.put("fireMark","0");
                   map.put("brightness","0");
                   map.put("frequency","0");
               } else if (sdStrategyRl.getState().equals("2")) {
                   map.put("fireMark","255");
                   map.put("brightness","50");
                   map.put("frequency","60");
               }

           }
            //诱导灯
            if(sdStrategyRl.getEqTypeId().equals(DevicesTypeEnum.YOU_DAO_DENG.getCode().toString())){
                map.put("brightness","50");
                map.put("frequency","60");
            }
            SpringUtils.getBean(SdDeviceControlService.class).controlDevices(map);
            SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);
            //查询设备协议表
            SdDevicesProtocol devicesProtocol = sdDevicesProtocolService.selectSdDevicesProtocolById( sdDevices.getProtocolId());
            //大类为照明的才进行检测 17设备大类为照明
            if("17".equals(devicesProtocol.getEqType().toString())){
                redisCache.setCacheObject("ERROREQUIPMENT_"+map.get("devId").toString(),map);
            }
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

        SdStrategy sdStrategy = SpringUtils.getBean(SdStrategyMapper.class).selectSdStrategyById(sdStrategyRl.getStrategyId());

        // 默认执行策略关联设备
        String[] split = sdStrategyRl.getEquipments().split(",");
        // 加强照明 基本照明 支持双向执行
        if (sdStrategy.getDirection().equals(DeviceDirectionEnum.ALl.getCode().toString()) &&
                (
                        sdStrategyRl.getEqTypeId().equals(DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().toString()) ||
                        sdStrategyRl.getEqTypeId().equals(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().toString())
                )
        ){
            split = SpringUtils.getBean(SdStrategyRlMapper.class).selectAllDirectionSdDevListByDevId(split,sdStrategy.getTunnelId(),sdStrategyRl.getEqTypeId());

        }



        for (String devId : split){
            Map<String,Object> map = new HashMap<>();

            if(DevicesTypeEnum.VMS.getCode().toString().equals(sdStrategyRl.getEqTypeId()) || DevicesTypeEnum.MEN_JIA_VMS.getCode().toString().equals(sdStrategyRl.getEqTypeId())){
                map.put("templateId",sdStrategyRl.getState());
            }
            map.put("devId",devId);
            map.put("controlType",sdStrategy.getStrategyType());
            map.put("operIp",InetAddress.getLocalHost().getHostAddress());
            if(type.equals("1")){
                map.put("controlTime", CommonUtil.formatDate(new Date())+" "+sdStrategy.getTimerOpen());
                map.put("state",sdStrategyRl.getState());
                map.put("stateNum",sdStrategyRl.getStateNum());
            }else{
                map.put("controlTime", CommonUtil.formatDate(new Date())+" "+sdStrategy.getTimerClose());
                map.put("state",sdStrategyRl.getEndState());
                map.put("stateNum",sdStrategyRl.getEndStateNum());
            }
            map.put("type","1");
            map.put("currentId",sdStrategyRl.getId());


            //按照设备初始化默认值
            //疏散标志
            if(sdStrategyRl.getEqTypeId().equals(DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode().toString())) {
                map.put("brightness","50");
                map.put("frequency","60");
                map.put("fireMark","255");
            }
            //诱导灯
            if(sdStrategyRl.getEqTypeId().equals(DevicesTypeEnum.YOU_DAO_DENG.getCode().toString())){
                map.put("brightness","50");
                map.put("frequency","60");
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
                        sdEvent.setEventGrade(EventGradeEnum.YI_BAN.getCode());
                        sdEvent.setEventState(EventStateEnum.unprocessed.getCode());
                        sdEvent.setCreateTime(DateUtils.getNowDate());
                        String eventTitle = SpringUtils.getBean(ISdEventService.class).getDefaultEventTitle(sdEvent,tunnelMap,eventTypeMap);
                        sdEvent.setEventTitle(eventTitle);
                        sdEvent.setEventTime(DateUtils.getNowDate());
                        //方向
//                        if(!StringUtils.isEmpty(f.getDirection())){
//                            sdEvent.setDirection(f.getDirection() + "");
//                        }
                        int updateRows = SpringUtils.getBean(SdEventMapper.class).insertSdEvent(sdEvent);
                        if(updateRows>0){
                            SdEvent event = new SdEvent();
                            event.setId(sdEvent.getId());
                            List<SdEvent> sdEventList = sdEventService.querySdEventList(sdEvent);
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
