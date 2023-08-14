package com.tunnel.platform.task;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.*;
import com.tunnel.business.datacenter.util.CronUtil;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdDevicesProtocol;
import com.tunnel.business.domain.digitalmodel.WJEnum;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdStrategy;
import com.tunnel.business.domain.event.SdStrategyRl;
import com.tunnel.business.domain.event.SdTrigger;
import com.tunnel.business.domain.wisdomLight.SdWisdomLight;
import com.tunnel.business.mapper.bigScreenApi.SdTunnelZtMapper;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.mapper.event.SdStrategyMapper;
import com.tunnel.business.mapper.event.SdStrategyRlMapper;
import com.tunnel.business.mapper.event.SdTriggerMapper;
import com.tunnel.business.mapper.wisdomLight.SdWisdomLightMapper;
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
import com.tunnel.platform.service.event.ISdStrategyService;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    @Autowired
    private SdWisdomLightMapper sdWisdomLightMapper;

    @Autowired
    private ISdStrategyService sdStrategyService;
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
     * 定时模式下  照明控制 成功检测
     */
    public  void  errorEquipment() throws UnknownHostException {
        //获取所有照明并且已经执行的定时任务
        List<String> scanKey = redisCache.getScanKey("ERROREQUIPMENT"+ "*");
        log.error("照明任务检测：待判断的任务数："+scanKey.size());
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
                    log.error("照明任务检测：添加任务到重复执行集合：SdStrategyRl ID="+currentId);
                }else{
                    //任务被删除或者禁用
                    redisCache.deleteObject(key);
                    log.error("照明任务检测：任务被删除或者禁用、或执行成功：key="+key);
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
                    log.error("repeatControl重发失败的定时照明策略：strategyId="+strategyId);
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
            SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);
            //检查智慧照明 灯光控制是否开启
            map = this.wisdomLight(map, sdStrategy, sdDevices);
            SpringUtils.getBean(SdDeviceControlService.class).controlDevices(map);

            //查询设备协议表
            SdDevicesProtocol devicesProtocol = sdDevicesProtocolService.selectSdDevicesProtocolById( sdDevices.getProtocolId());
            //大类为照明的才进行检测 17设备大类为照明
            if("17".equals(devicesProtocol.getEqType().toString())){
                redisCache.setCacheObject("ERROREQUIPMENT_"+map.get("devId").toString(),map);
                log.error("照明定时策略执行后，添加到redis缓存中");
            }
        }
    }

    /**
     * 智慧照明车辆 调光控制
     */
    public void wisdomCatLight(){
        SdWisdomLight sdWisdomLight = new SdWisdomLight();
        //车辆照明配置
        sdWisdomLight.setModeType(1);
        List<SdWisdomLight> sdWisdomCatLightList = sdWisdomLightMapper.selectSdWisdomLightList(sdWisdomLight);
        if(sdWisdomCatLightList.size()>0){
            sdWisdomCatLightList.forEach(sdWisdomCatLight ->{
                //IsStatus()==1说明智慧照明状态是开启的
                if(sdWisdomCatLight.getIsStatus()==0){
                    SdStrategy sdStrategy = new SdStrategy();
                    sdStrategy.setStrategyType("1");
                    sdStrategy.setStrategyGroup("1");
                    sdStrategy.setTunnelId(sdWisdomCatLight.getTunnelId());
                    sdStrategy.setTimingType("0");
                    List<SdStrategy> sdStrategies = SpringUtils.getBean(SdStrategyMapper.class).selectSdStrategyList(sdStrategy);
                    //过滤 方向 以及改定时策略是否启用
                    List<SdStrategy> sdStrategyCollect = sdStrategies.stream()
                            .filter(strategy -> (strategy.getDirection().equals(sdWisdomCatLight.getDirection()) || strategy.getDirection().equals("3"))&&
                                    "0".equals(strategy.getStrategyState()) )
                            .collect(Collectors.toList());
                    //时间
                    //事件表达式转换为时间
                    for(SdStrategy strategy:sdStrategyCollect){
                        //执行时间转换
                        if(org.apache.commons.lang3.StringUtils.isNotBlank(strategy.getSchedulerTime())){
                            strategy.setExecDate(CronUtil.CronConvertDate(strategy.getSchedulerTime()));
                        }
                        //定时策略-执行日期
                        if(org.apache.commons.lang3.StringUtils.isNotBlank(strategy.getSchedulerTime())) {
                            strategy.setExecTime(CronUtil.CronConvertDateTime(strategy.getSchedulerTime()));
                        }
                    }
                    //循环车辆照明策略时间段集合
                    this.catTimeSlot(sdWisdomCatLight,sdStrategyCollect);
                }
            });
        }

    }

    /**
     * 循环车辆照明策略时间段集合
     * @param sdWisdomCatLight
     * @param sdStrategyCollect
     */
    public void catTimeSlot(SdWisdomLight sdWisdomCatLight , List<SdStrategy> sdStrategyCollect){
        //智慧照明车辆   定时时间json串转list
        Gson gson = new Gson();
        List<Map<String, String>> brightnessList = gson.fromJson(sdWisdomCatLight.getTimeSlot(), new TypeToken<List<Map<String, String>>>(){}.getType());
        //循环需要下修的时间集合下修相应灯光   下修的设备是主定时任务定义的设备
        for(Map<String, String> maps : brightnessList){
            String startTime = maps.get("startTime");
            String endTime = maps.get("endTime");
            String beforeLuminance = maps.get("beforeLuminance");
            //判断当前时间是否在此时间范围
            boolean compareBoolean = DateUtils.dateCompare(startTime, endTime);
            //如果存在着查看当前时间主定时策略执行的亮度
            if(compareBoolean){
                // 获取当前系统时间
                LocalTime currentTime = LocalTime.now();
                // 使用Stream筛选出距离当前时间最近且小于当前时间的时间
                SdStrategy closestTime = sdStrategyCollect.stream()
                        .filter(time -> LocalTime.parse(time.getExecTime()).isBefore(currentTime))
                        .max((e1, e2) -> LocalTime.parse(e1.getExecTime()).compareTo(LocalTime.parse(e2.getExecTime())))
                        .orElse(null);

                List<SdStrategyRl> sdStrategyRlList = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlByStrategyId(closestTime.getId());
                //设备控制方法
                this.controlDevice(sdStrategyRlList,closestTime,beforeLuminance);
            }
        }
    }

    /**
     * 设备控制方法
     * @param sdStrategyRlList  主定时策略子表设备信息表
     * @param closestTime 当前主定时任务
     * @param beforeLuminance 车辆配置下修比例
     */
    public void controlDevice(List<SdStrategyRl> sdStrategyRlList ,SdStrategy closestTime, String beforeLuminance ){
        for(SdStrategyRl sdStrategyRl :sdStrategyRlList){
            // 默认执行策略关联设备
            String[] split = sdStrategyRl.getEquipments().split(",");
            // 加强照明 基本照明 支持双向执行
            if (closestTime.getDirection().equals(DeviceDirectionEnum.ALl.getCode().toString()) &&
                    (
                            sdStrategyRl.getEqTypeId().equals(DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().toString()) ||
                                    sdStrategyRl.getEqTypeId().equals(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().toString())
                    )
            ){
                split = SpringUtils.getBean(SdStrategyRlMapper.class).selectAllDirectionSdDevListByDevId(split,closestTime.getTunnelId(),sdStrategyRl.getEqTypeId());

            }
            for (String devId : split){
                //组装控制设备map
                Map<String, Object> map = this.controlMap(sdStrategyRl, closestTime, devId);

                SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);
//                                //检查智慧照明 灯光控制是否开启
//                                this.wisdomLight(map,sdStrategy,sdDevices);
                //智慧照明车辆灯光调整
                Integer brightness = Integer.parseInt(map.get("brightness").toString());//主策略亮度

                brightness = brightness - (brightness * Integer.parseInt(beforeLuminance )/ 100);

                map.put("brightness",brightness);

                //判断当前亮度是否需要再次控制灯光调整
                //根据设备id查询现场 设备数值信息
                List<SdDeviceData> otoDeviceData = sdTunnelZtMapper.getOtoDeviceData(sdDevices.getEqId(), sdDevices.getEqType().toString());
                if((otoDeviceData.size()>0 && !otoDeviceData.get(0).getData().equals(brightness.toString()))||otoDeviceData.size()==0){
                    SpringUtils.getBean(SdDeviceControlService.class).controlDevices(map);
                }
            }
        }
    }

    /**
     * 组装控制设备map
     * @param sdStrategyRl 主定时策略子表设备信息表
     * @param closestTime 当前主定时任务
     * @param devId 设备id
     * @return
     */
    public Map<String, Object> controlMap(SdStrategyRl sdStrategyRl, SdStrategy closestTime , String devId ){
        Map<String,Object> map = new HashMap<>();

        if(DevicesTypeEnum.VMS.getCode().toString().equals(sdStrategyRl.getEqTypeId()) || DevicesTypeEnum.MEN_JIA_VMS.getCode().toString().equals(sdStrategyRl.getEqTypeId())){
            map.put("templateId",sdStrategyRl.getState());
        }
        map.put("devId",devId);
        map.put("state",sdStrategyRl.getState());
        map.put("brightness",sdStrategyRl.getStateNum());
        map.put("controlType",closestTime.getStrategyType());
        try {
            map.put("operIp",InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
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
        return map;
    }
    /**
     * 智慧照明控制
     * @param map
     * @param sdStrategy
     */
    public Map wisdomLight(Map<String,Object> map, SdStrategy sdStrategy , SdDevices sdDevices ){
        SdWisdomLight sdWisdomLight = new SdWisdomLight();
        sdWisdomLight.setTunnelId(sdStrategy.getTunnelId());
        //光强
        sdWisdomLight.setModeType(0);
        sdWisdomLight.setDirection(sdStrategy.getDirection());
        List<SdWisdomLight> sdWisdomLightsList = sdWisdomLightMapper.selectSdWisdomLightList(sdWisdomLight);
        // 说明配置了智慧照明    IsStatus()==1说明智慧照明状态是开启的
        if(sdWisdomLightsList.size()>0 &&sdWisdomLightsList.get(0).getIsStatus()==0){
            Gson gson = new Gson();
            List<Map<String, String>> brightnessList = gson.fromJson(sdWisdomLightsList.get(0).getBeforeLuminance(), new TypeToken<List<Map<String, String>>>(){}.getType());
            String eqName = StringUtils.bringHanziStr(sdDevices.getEqName());
            brightnessList.forEach( (maps) -> {
                String brightnessName = maps.get("lightParagraph");
                if(StringUtils.isNotEmpty( maps.get("lightParagraph"))&&brightnessName.equals(eqName)){
                    //主策略定时任务照明强度转Integer
                    Integer brightness = Integer.parseInt(map.get("brightness").toString());
                    brightness = Integer.parseInt(map.get("brightness").toString()) - (brightness * Integer.parseInt(maps.get("beforeLuminance") )/ 100);
                    map.put("brightness",brightness);
                }
            });
        }
        return map;
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
//        SdStrategyRl sdStrategyRl = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlById(Long.valueOf(triggerId));
//
//        SdStrategy sdStrategy = SpringUtils.getBean(SdStrategyMapper.class).selectSdStrategyById(sdStrategyRl.getStrategyId());
//
//        List<Map> triggerData = SpringUtils.getBean(SdTriggerMapper.class).getTriggerInfo(sdStrategy.getId());

        //触发器数据
        List<Map> triggerData = SpringUtils.getBean(SdTriggerMapper.class).getTriggerInfo(triggerId);
        Map<String,Object> issuedParam = new HashMap<>();
        List<Long> strategyIdList = new ArrayList<>();
        String serverIp = InetAddress.getLocalHost().getHostAddress();
        out: for(Map s:triggerData){
            String equipment = s.get("device_id").toString();
            String itemId = s.get("element_id").toString();
            strategyIdList.add(Long.valueOf(s.get("id").toString()));
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
                        this.triggerComparison(s);
                    }else{
                        //生成事件
                        this.triggerComparison(s);
                        for (Long  id : strategyIdList){
                            List<SdStrategyRl> sdStrategyRls = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlByStrategyId(id);
                            for (SdStrategyRl sdStrategyRl : sdStrategyRls) {
                                // 自动触发控制策略执行 控制设备triggerId 策略关联设备信息id
                                this.autoJobParams(sdStrategyRl.getId());
                            }
                        }

                    }
                    break out;
                }
            }
            break;
        }
    }
    //触发控制仅预警
    public void  triggerComparison(Map  s){
        //插入事件
        SdEvent sdEvent = new SdEvent();
        //所有事件类型Map
        Map<Long,String> eventTypeMap = SpringUtils.getBean(ISdEventTypeService.class).getEventTypeMap();
        //所有隧道Map
        Map<String,String> tunnelMap = SpringUtils.getBean(ISdTunnelsService.class).getTunnelNameMap();
        sdEvent.setTunnelId(s.get("tunnel_id").toString());
        sdEvent.setEventSource("6");
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
        //相同定时触发一天内只能触发两次
        SdEvent sdEventOne = new SdEvent();
        //事件来源
        sdEventOne.setEventSource(sdEvent.getEventSource());
        //隧道id
        sdEventOne.setTunnelId(sdEvent.getTunnelId());
        //事件类型
        sdEventOne.setEventTypeId(sdEvent.getEventTypeId());
        //事件标题
        sdEventOne.setEventTitle(sdEvent.getEventTitle());
        //event_state  处理状态
        sdEventOne.setEventState(sdEvent.getEventState());
        List<SdEvent> sdEventsList = SpringUtils.getBean(SdEventMapper.class).selectSdEventSingleList(sdEvent);
        int updateRows = 0;
        if(sdEventsList.size()>0){
            sdEventsList.get(0).setStartTime(DateUtils.getTime());
            sdEventsList.get(0).setEventTime(DateUtils.getNowDate());
            updateRows = SpringUtils.getBean(SdEventMapper.class).updateSdEvent(sdEventsList.get(0));

        }else{
            updateRows = SpringUtils.getBean(SdEventMapper.class).insertSdEvent(sdEvent);
        }
        if(updateRows>0){
            SdEvent event = new SdEvent();
            event.setId(sdEvent.getId());
            List<SdEvent> sdEventList = sdEventService.querySdEventList(sdEvent);
            JSONObject object = new JSONObject();
            object.put("sdEventList", sdEventList);
            WebSocketService.broadcast("sdEventList",object.toString());
            if(!(sdEventsList.size()>0)){//添加
                // 添加事件流程记录
                SpringUtils.getBean(ISdEventFlowService.class).addEventFlowBatch(sdEventList);
            }

        }
    }
    /**
     * 自动触发控制策略执行
     * @param strategyRlId
     * @throws UnknownHostException
     */
    public  void triggerJobParams(String strategyRlId) throws UnknownHostException {
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
            //手动执行 设置时间为当前时间
            if("0".equals(sdStrategy.getStrategyType())){
                map.put("controlTime", CommonUtil.get_Date());
            }else{
                map.put("controlTime", CommonUtil.formatDate(new Date())+" "+sdStrategyRl.getControlTime());
            }

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
        }
    }

    /**
     * 自动触发控制策略执行
     * @param strategyRlId
     * @throws UnknownHostException
     */
    public  void autoJobParams(Long strategyRlId) throws UnknownHostException {
        SdStrategyRl sdStrategyRl = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlById(strategyRlId);

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
            //触发执行 设置时间为当前时间
            map.put("controlTime", CommonUtil.get_Date());


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
        }
    }
}
