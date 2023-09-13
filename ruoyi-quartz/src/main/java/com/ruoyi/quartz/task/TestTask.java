package com.ruoyi.quartz.task;


import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.Threads;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.datacenter.domain.enumeration.TunnelDirectionEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.enhancedLighting.SdEnhancedLightingConfig;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.digitalmodel.SdRadarDetectDataTemporaryMapper;
import com.tunnel.business.service.enhancedLighting.ISdEnhancedLightingConfigService;
import com.tunnel.deal.light.impl.SanJingLight;
import com.zc.common.core.ThreadPool.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 测试定时任务
 * testTask.runTask
 */
@Component("testTask")
public class TestTask {

    private static final Logger log = LoggerFactory.getLogger(TestTask.class);

    private ISdEnhancedLightingConfigService sdEnhancedLightingConfigService = SpringUtils.getBean(ISdEnhancedLightingConfigService.class);

    private SanJingLight sanJingLight =  SpringUtils.getBean(SanJingLight.class);

    private SdDevicesMapper sdDevicesMapper = SpringUtils.getBean(SdDevicesMapper.class);

    private SdDeviceDataMapper sdDeviceDataMapper =  SpringUtils.getBean(SdDeviceDataMapper.class);

    private SdRadarDetectDataTemporaryMapper sdRadarDetectDataTemporaryMapper =  SpringUtils.getBean(SdRadarDetectDataTemporaryMapper.class);

    private Map<String,Thread[]> threadArrsMap = new HashMap<>();

    private RedisCache redisCache = SpringUtils.getBean(RedisCache.class);


    public void runTask(){
        //模拟数据
        for (int i = 0; i < 10; i++) {
            getCar("JQ-JiNan-WenZuBei-MJY" ,"1" ,i);

            long time = (long ) (Math.random()*1000)*30;
            System.out.println("主线程等待"+time);
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void getCar(String tunnelId,String roadDir,Integer carNumber){
        System.out.println("经过第"+ carNumber +"辆车"+new Date());

        List<SdEnhancedLightingConfig> sdEnhancedLightingConfigList;
        sdEnhancedLightingConfigList = redisCache.getCacheObject("control:lightFixedTimeTask");
        if(sdEnhancedLightingConfigList == null){
            //根据隧道id获取对应隧道  加强照明策略 配置信息
            sdEnhancedLightingConfigList =  sdEnhancedLightingConfigService.selectSdEnhancedLightingConfigList(new SdEnhancedLightingConfig());
            redisCache.setCacheObject("control:lightFixedTimeTask",sdEnhancedLightingConfigList,60, TimeUnit.SECONDS);//每分钟，重新请求一次
        }


        for (SdEnhancedLightingConfig sdEnhancedLightingConfig:sdEnhancedLightingConfigList) {
            //隧道id , 方向
            //查看当前模式是否为 定时模式。 若为定时模式，则忽略操作
            if(sdEnhancedLightingConfig.getTunnelId().equals("JQ-JiNan-WenZuBei-MJY")&&sdEnhancedLightingConfig.getModeType() != 0){

                //响应时间
                Integer respondTime = Math.toIntExact(sdEnhancedLightingConfig.getRespondTime());
                //模拟过车数据
                Map vehicleMap  = new HashMap<>();
                vehicleMap.put("no",carNumber);
                redisCache.setCacheObject("vehicleSnap:" + "JQ-JiNan-WenZuBei-MJY" + ":" + "1" + ":" + "vehicle"+carNumber, vehicleMap,respondTime, TimeUnit.MILLISECONDS);

                //推送当前指令
                adjustBrightnessByRunMode(tunnelId,roadDir,sdEnhancedLightingConfig);
                break;
            }
        }
    }

    /**
     * 根据过车信息，执行调光任务。实现车来灯亮，车走灯灭效果。
     * 当前默认一辆车经过隧道后，  30秒后关闭隧道内部灯亮信息。
     * 后期如果  能检测到  出口微波车检 数据效果。既可实现 真正的 车来灯亮，车走灯灭效果。
     * @param tunnelId      隧道id
     * @param roadDir     方向
     */
    public void adjustBrightnessByRunMode(String tunnelId,String roadDir,SdEnhancedLightingConfig sdEnhancedLightingConfig){
        //模式
        Integer modeType = sdEnhancedLightingConfig.getModeType();
        log.info("加强照明调光 当前模式为"+modeType);
        //获取当前时间。判断当前时间
        String json = sdEnhancedLightingConfig.getTimeSlot();
        //配置信息 JSON
        List<Map> jsonArry  = JSONObject.parseArray(json, Map.class);
        //调光最大区间
        Integer  maxLuminanceRange = sdEnhancedLightingConfig.getMaxLuminanceRange();
        //调光最小区间
        Integer  minLuminanceRange = sdEnhancedLightingConfig.getMinLuminanceRange();
        //最大车流量
//        Integer  maxTrafficFlow = Math.toIntExact(sdEnhancedLightingConfig.getMaxTrafficFlow());
        //响应时间
        Long respondTime = sdEnhancedLightingConfig.getRespondTime();
        //最小亮度值
        Integer  minLuminance = sdEnhancedLightingConfig.getMinLuminance();

        Integer beforeLuminance = sdEnhancedLightingConfig.getBeforeLuminance();

        Integer luminanceRange;
        //放入redis缓存
        //查找所有加强照明与基本照明
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqTunnelId(tunnelId);
        sdDevices.setEqDirection(roadDir);
        sdDevices.setEqTypes(new Long[]{DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode(),DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode()});
        List<SdDevices> deviceIds = sdDevicesMapper.selectSdDevicesListByParam(sdDevices);

        //推送加强照明
        int nowTrafficFlow;
        //是否开启车流量模式
        if(sdEnhancedLightingConfig.getIsTrafficVolume() == 1){
            //从 sd_radar_detect_data_temporary  表中 获取当前1分钟内过车流量信息
//            nowTrafficFlow = sdRadarDetectDataTemporaryMapper.getSdRadarDetectDataCount(tunnelId,roadDir);
            nowTrafficFlow = 80;
        }else{
            nowTrafficFlow = 0;
        }
        //1  自动模式  2 节能模式
        switch (modeType){
            case 1:  //自动模式

                //忽略车流量
                //查看1分钟内车流量  是否超过最大车流量  maxTrafficFlow
                //int nowLuminanceRange =  sdEnhancedLightingConfigService.getLuminanceByParam(nowTrafficFlow,maxTrafficFlow,maxLuminanceRange,minLuminanceRange,luminanceRange);

                //亮度值计算:
                // 后期修改为 根据洞外亮度计算当前亮度值
                // 当前亮度值 根据定时模式获取当前路段亮度。
                int nowLuminanceRange =  beforeLuminance;

                //推送调光值时，按照隧道入口 -> 出口顺序处理
                ThreadPool.executor.execute(() -> {
                    log.info("【"+Thread.currentThread().getName()+"】开始准备调光：");
                    //查看当前线程  threadArrs  集合是否存在。
                    Thread[] threadArrs;
                    String key = tunnelId+"_"+roadDir;
                    if(threadArrsMap.containsKey(key)){
                        threadArrs = threadArrsMap.get(key);
                    }else{
                        threadArrs = new Thread[1];
                        threadArrsMap.put(key,threadArrs);
                    }
                    ThreadPool.executor.execute(() -> {
                        for (SdDevices devices:deviceIds) {


                            //查看当前设备是否关闭。  如果为关闭。则开启当前设备
                            SdDeviceData sdDeviceData = new SdDeviceData();
                            sdDeviceData.setDeviceId(devices.getEqId());
                            List<SdDeviceData> sdDeviceDataList =   sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
                            //当前设备开关
                            String deviceState = null;
                            //当前设备亮度
                            String luminance = null;

                            //解析  开关状态与亮度值
                            for (SdDeviceData sdDeviceDataInfo:sdDeviceDataList) {
                                //获取开关状态
                                if(DevicesTypeItemEnum.JQ_LIGHT_OPENCLOSE.getCode() == sdDeviceDataInfo.getItemId()||DevicesTypeItemEnum.JI_BEN_ZHAO_MING_OPENCLOSE.getCode() == sdDeviceDataInfo.getItemId()){
                                    deviceState = sdDeviceDataInfo.getData();
                                }else if(DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode() == sdDeviceDataInfo.getItemId()||DevicesTypeItemEnum.JB_LIGHT_BRIGHNESS.getCode() == sdDeviceDataInfo.getItemId()){
                                    luminance = sdDeviceDataInfo.getData();
                                }
                            }

                            String redisLuminanceRangeKey = "control:"+devices.getEqId()+"_LuminanceRange";

                            //缓存获取亮度值  与当前亮度值   与当前亮度值比对。如果相同 忽略当前操作。
                            Integer num = redisCache.getCacheObject(redisLuminanceRangeKey);

                            if(threadArrs[0]==null|| num == null || num != nowLuminanceRange){
                                //查看原有亮度是否为null
                                if(num == null ){
                                    if(sdDeviceDataList.size()<=0){
                                        //无亮度 默认给个最小亮度值
                                        num = sdEnhancedLightingConfig.getMinLuminance();
                                    }else{
                                        num = Integer.parseInt(luminance);
                                    }
                                    redisCache.setCacheObject(redisLuminanceRangeKey,num);
                                }
                                //循环  开启灯光
                                try{
                                    //1开启   2关闭
                                    if("2".equals(deviceState)){
                                        log.info("当前[{}]设备状态为关闭状态,需要开启照明方可进行调光。",devices.getEqId());
                                        int flag  = sanJingLight.lineControlAddLog(devices.getEqId(),1,null);
                                        if(flag == 0){
                                            log.error("当前[{}]加强照明为关闭状态，开启失败。请联系管理员",devices.getEqId());
                                            continue;
                                        }
                                    }
                                    log.info("开始亮光值:[{}]当前亮度num：{} 根据车流量计算的亮度nowLuminanceRange:{}", devices.getEqId(), num, nowLuminanceRange);
                                    int flag = sanJingLight.setBrightnessByDevice(devices,num,nowLuminanceRange,"2");
                                    if(flag == 0){
                                        log.error("【{}】推送调光指令异常，未能成功发送调光指令",devices.getEqId());
                                    }
                                }catch (Exception e){
                                    log.error("【{}】推送调光指令异常，未能成功发送调光指令",devices.getEqId());
                                }
                            }
                        }
                    });
                    try{
                        //替换线程
                        Threads.replaceThread(threadArrs,Thread.currentThread());
                        //等待30秒后 执行 降低 光照强度功能
                        //降低光照强度执行完毕,推送调光 指令。
                        Thread.sleep(respondTime);
                        ThreadPool.executor.execute(() -> {
                            for (SdDevices devices:deviceIds) {
                                log.info("结束亮光值:["+ tunnelId +"]当前亮度nowLuminanceRange："+nowLuminanceRange+" 结束推送亮度值" +minLuminance);
                                int flag = sanJingLight.setBrightnessByDevice(devices,nowLuminanceRange,minLuminance,"2");
                                if(flag == 0){
                                    log.error(Thread.currentThread().getName()+"推送调光指令异常，未能成功发送调光指令");
                                }
                            }
                        });
                        //清除当前记录线程
                        threadArrs[0] =  null;
                    }catch (Exception e){
                        log.error("经过一辆车，当前线程被阻断。");
                    }
                });
                break;
            case 2://2节能模式
                //推送调光值时，按照隧道入口 -> 出口顺序处理
                for (SdDevices devices:deviceIds) {
                    //忽略车流量
                    //查看1分钟内车流量  是否超过最大车流量  maxTrafficFlow
                    //int nowLuminanceRange =  sdEnhancedLightingConfigService.getLuminanceByParam(nowTrafficFlow,maxTrafficFlow,maxLuminanceRange,minLuminanceRange,luminanceRange);
                    //亮度值计算:
                    // 后期修改为 根据洞外亮度计算当前亮度值
                    // 当前亮度值 根据定时模式获取当前路段亮度。
                    //定时亮度值
                    String redisRegularLuminanceRangeKey = "control_regular:"+devices.getEqId()+"_LuminanceRange";
                    Integer nowLuminanceRange1 = redisCache.getCacheObject(redisRegularLuminanceRangeKey);

                    //定时开关状态
                    String redisStateLuminanceRangeKey = "control_regular:"+devices.getEqId()+"_state";
                    String state = redisCache.getCacheObject(redisStateLuminanceRangeKey);

                    //无定时亮度值  或者 当前定时状态为关闭。
                    if((state != null && "2".equals(state)) || nowLuminanceRange1 == null ){
                        continue;
                    }
                    //查看当前设备是否关闭。  如果为关闭。则开启当前设备
                    SdDeviceData sdDeviceData = new SdDeviceData();
                    sdDeviceData.setDeviceId(devices.getEqId());
                    List<SdDeviceData> sdDeviceDataList =   sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
                    //当前设备开关
                    String deviceState = null;
                    //当前设备亮度
                    String luminance = null;

                    //解析  开关状态与亮度值
                    for (SdDeviceData sdDeviceDataInfo:sdDeviceDataList) {
                        //获取开关状态
                        if(DevicesTypeItemEnum.JQ_LIGHT_OPENCLOSE.getCode() == sdDeviceDataInfo.getItemId()||DevicesTypeItemEnum.JI_BEN_ZHAO_MING_OPENCLOSE.getCode() == sdDeviceDataInfo.getItemId()){
                            deviceState = sdDeviceDataInfo.getData();
                        }else if(DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode() == sdDeviceDataInfo.getItemId()||DevicesTypeItemEnum.JB_LIGHT_BRIGHNESS.getCode() == sdDeviceDataInfo.getItemId()){
                            luminance = sdDeviceDataInfo.getData();
                        }
                    }

                    String redisLuminanceRangeKey = "control:"+devices.getEqId()+"_LuminanceRange";

                    //缓存获取亮度值  与当前亮度值   与当前亮度值比对。如果相同 忽略当前操作。
                    Integer num = redisCache.getCacheObject(redisLuminanceRangeKey);

                    if( num == null || !num.equals(nowLuminanceRange1)){
                        //查看原有亮度是否为null
                        if(num == null ){
                            if(sdDeviceDataList.size()<=0){
                                //无亮度 默认给个最小亮度值
                                num = sdEnhancedLightingConfig.getMinLuminance();
                            }else{
                                num = Integer.parseInt(luminance);
                            }
                            redisCache.setCacheObject(redisLuminanceRangeKey,num);
                        }
                        //循环  开启灯光
                        try{
                            //1开启   2关闭
                            if("2".equals(deviceState)){
                                log.info("当前[{}]设备状态为关闭状态,需要开启照明方可进行调光。",devices.getEqId());
                                int flag  = sanJingLight.lineControlAddLog(devices.getEqId(),1,null);
                                if(flag == 0){
                                    log.error("当前[{}]加强照明为关闭状态，开启失败。请联系管理员",devices.getEqId());
                                    continue;
                                }
                            }
                            log.info("开始亮光值:[{}]当前亮度num：{} 根据车流量计算的亮度nowLuminanceRange:{}", devices.getEqId(), num, nowLuminanceRange1);
                            int flag = sanJingLight.setBrightnessByDevice(devices,num,nowLuminanceRange1,"2");
                            if(flag == 0){
                                log.error("【{}】推送调光指令异常，未能成功发送调光指令",devices.getEqId());
                            }
                        }catch (Exception e){
                            log.error("【{}】推送调光指令异常，未能成功发送调光指令",devices.getEqId());
                        }
                    }
                }
            break;
        }
    }




    public void dimmingTask(){

        List<SdEnhancedLightingConfig> sdEnhancedLightingConfigList;
        sdEnhancedLightingConfigList = redisCache.getCacheObject("control:lightFixedTimeTask");
        if(sdEnhancedLightingConfigList == null){
            //根据隧道id获取对应隧道  加强照明策略 配置信息
            sdEnhancedLightingConfigList =  sdEnhancedLightingConfigService.selectSdEnhancedLightingConfigList(new SdEnhancedLightingConfig());
            redisCache.setCacheObject("control:lightFixedTimeTask",sdEnhancedLightingConfigList,60, TimeUnit.SECONDS);//每分钟，重新请求一次
        }
        for (SdEnhancedLightingConfig sdEnhancedLightingConfig:sdEnhancedLightingConfigList) {
            //放入redis缓存
            //查找所有加强照明与基本照明
            SdDevices sdDevices = new SdDevices();
            sdDevices.setEqTunnelId(sdEnhancedLightingConfig.getTunnelId());
            sdDevices.setEqDirection(TunnelDirectionEnum.UP_DIRECTION.getCode());
            sdDevices.setEqTypes(new Long[]{DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode(),DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode()});
            List<SdDevices> deviceIds = sdDevicesMapper.selectSdDevicesListByParam(sdDevices);
            if(sdEnhancedLightingConfig.getModeType() == 2){
                //判断当前隧道是否有车辆信息
                Set<String> keys = redisCache.redisTemplate.keys("vehicleSnap:"+sdEnhancedLightingConfig.getTunnelId()+":"+TunnelDirectionEnum.UP_DIRECTION.getCode()+":*");
                if(keys != null && keys.size() == 0){
                    for (SdDevices devices:deviceIds) {
                        //亮度值计算:
                        // 后期修改为 根据洞外亮度计算当前亮度值
                        // 当前亮度值 根据定时模式获取当前路段亮度。
                        //当前亮度值
                        String redisLuminanceRangeKey = "control:"+devices.getEqId()+"_LuminanceRange";
                        //缓存获取亮度值  与当前亮度值   与当前亮度值比对。如果相同 忽略当前操作。
                        Integer nowLuminanceRange = redisCache.getCacheObject(redisLuminanceRangeKey);
                        //最小亮度值
                        int minLuminance = sdEnhancedLightingConfig.getMinLuminance();

                        if(nowLuminanceRange != null && minLuminance != nowLuminanceRange){
                            log.info("结束亮光值:["+ sdEnhancedLightingConfig.getTunnelId() +"]当前亮度nowLuminanceRange："+nowLuminanceRange+" 结束推送亮度值" +minLuminance);
                            int flag = sanJingLight.setBrightnessByDevice(devices,nowLuminanceRange,minLuminance,"2");
                            if(flag == 0){
                                log.error(Thread.currentThread().getName()+"推送调光指令异常，未能成功发送调光指令");
                            }
                        }
                    }
                }
            }else if(sdEnhancedLightingConfig.getModeType() == 0){
                for (SdDevices devices:deviceIds) {
                    //查看两个亮度值是否一致
                    //定时原有亮度值
                    String redisRegularLuminanceRangeKey = "control_regular:"+devices.getEqId()+"_LuminanceRange";
                    Integer nowLuminanceRange1 = redisCache.getCacheObject(redisRegularLuminanceRangeKey);
                    //当前亮度值
                    String redisLuminanceRangeKey = "control:"+devices.getEqId()+"_LuminanceRange";
                    Integer nowLuminanceRange2 = redisCache.getCacheObject(redisLuminanceRangeKey);
                    //如果亮度值不同，则重新调整亮度值
                    if(nowLuminanceRange1!=null && !nowLuminanceRange1.equals(nowLuminanceRange2)){
                        sanJingLight.setBrightnessByDevice(devices,nowLuminanceRange2,nowLuminanceRange1,"2");
                    }
                }
            }
        }
    }
}
