package com.ruoyi.quartz.task;


import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.Threads;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
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
            getCar("JQ-WeiFang-JiuLongYu-HSD" ,"1" ,i);
            long time = (long ) (Math.random()*1000)*4;
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
            redisCache.setCacheObject("control:lightFixedTimeTask",sdEnhancedLightingConfigList);
            redisCache.expire("control:lightFixedTimeTask",60);    //每分钟，重新请求一次
        }
        for (SdEnhancedLightingConfig sdEnhancedLightingConfig:sdEnhancedLightingConfigList) {
            //隧道id , 方向
            //查看当前模式是否为 定时模式。 若为定时模式，则忽略操作
            if(sdEnhancedLightingConfig.getTunnelId().equals("JQ-WeiFang-JiuLongYu-HSD")&&sdEnhancedLightingConfig.getModeType() != 0){
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
        Integer  maxTrafficFlow = Math.toIntExact(sdEnhancedLightingConfig.getMaxTrafficFlow());
        //响应时间
        Long respondTime = sdEnhancedLightingConfig.getRespondTime();
        //最小亮度值
        Integer  minLuminance = sdEnhancedLightingConfig.getMinLuminance();

        Integer luminanceRange;
        //查找所有加强照明
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqTunnelId(tunnelId);
        sdDevices.setEqDirection(roadDir);
        sdDevices.setEqType(DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode());
        sdDevices.setItemId(DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode());
        List<SdDevices> deviceIds = sdDevicesMapper.selectSdDevicesDataByParam(sdDevices);

        //筛选查看当前设备是否开启
        Iterator<SdDevices> iterator = deviceIds.iterator();
        while (iterator.hasNext()) {
            SdDevices param = iterator.next();
            //查看当前加强照明设备开启状态
            SdDeviceData sdParam = new SdDeviceData();
            sdParam.setItemId((long)DevicesTypeItemEnum.JQ_LIGHT_OPENCLOSE.getCode());
            sdParam.setDeviceId(param.getEqId());
            List<SdDeviceData> list = sdDeviceDataMapper.selectSdDeviceDataList(sdParam);
            //查看当前设备是否存在开启状态
            if(list == null ||list.size()<=0){
                iterator.remove();
            }
            //获取开启状态
            String status = list.get(0).getData();
            //当前设备状态是否为关闭
            if("2".equals(status)){
                log.info("当前设备【{}】状态为关闭状态无法控制。",param.getEqId());
                iterator.remove();
            }
        }
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
                //当前亮度值初始值
                luminanceRange = sdEnhancedLightingConfig.getBeforeLuminance();
                //查看1分钟内车流量  是否超过最大车流量  maxTrafficFlow
                int nowLuminanceRange =  sdEnhancedLightingConfigService.getLuminanceByParam(nowTrafficFlow,maxTrafficFlow,maxLuminanceRange,minLuminanceRange,luminanceRange);
                //循环推送当前调光值
                for (SdDevices devices:deviceIds) {
                    ThreadPool.executor.execute(() -> {
                        log.info("【"+Thread.currentThread().getName()+"】开始准备调光：");
                        //查看当前线程  threadArrs  集合是否存在。
                        Thread[] threadArrs;
                        String key = tunnelId+"_"+devices.getEqId();
                        if(threadArrsMap.containsKey(key)){
                            threadArrs = threadArrsMap.get(key);
                        }else{
                            threadArrs = new Thread[1];
                            threadArrsMap.put(key,threadArrs);
                        }
                        try {
                            String redisLuminanceRangeKey = "control:"+devices.getEqId()+"_LuminanceRange";
                            //缓存获取亮度值  与当前亮度值   与当前亮度值比对。如果相同 忽略当前操作。
                            Integer num = redisCache.getCacheObject(redisLuminanceRangeKey);
                            if(num == null ){
                                SdDeviceData sdDeviceData = new SdDeviceData();
                                sdDeviceData.setDeviceId(devices.getEqId());
                                sdDeviceData.setItemId((long)DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode());
                                List<SdDeviceData> sdDeviceDataList = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
                                if(sdDeviceDataList.size()<=0){
                                    //无亮度 默认给个最小亮度值
                                    num = sdEnhancedLightingConfig.getMinLuminance();
                                }else{
                                    num = Integer.parseInt(sdDeviceDataList.get(0).getData());
                                }
                                redisCache.setCacheObject(redisLuminanceRangeKey,num);
                            }
                            if(threadArrs[0]==null|| num == null || num != nowLuminanceRange){
                                //推送调光 指令。
                                try{
                                    log.info("开始亮光值:["+devices.getEqId()+"]当前亮度num："+num+" 根据车流量计算的亮度nowLuminanceRange:" +nowLuminanceRange);
                                    int flag = sanJingLight.setBrightnessByDevice(devices,num,nowLuminanceRange,"2");
                                    if(flag == 0){
                                        log.error(Thread.currentThread().getName()+"推送调光指令异常，未能成功发送调光指令");
                                    }
                                }catch (Exception e){
                                    log.error(Thread.currentThread().getName()+"推送调光指令异常，未能成功发送调光指令");
                                }
                            }
                            //替换线程
                            Threads.replaceThread(threadArrs,Thread.currentThread());
                            //等待30秒后 执行 降低 光照强度功能
                            Thread.sleep(respondTime);
                            //降低光照强度执行完毕,推送调光 指令。
                            try{
                                log.info("结束亮光值:["+devices.getEqId()+"]当前亮度nowLuminanceRange："+nowLuminanceRange+" 结束推送亮度值" +minLuminance);
                                int flag = sanJingLight.setBrightnessByDevice(devices,nowLuminanceRange,minLuminance,"2");
                                //清除当前记录线程
                                threadArrs[0] =  null;
                                if(flag == 0){
                                    log.error(Thread.currentThread().getName()+"推送调光指令异常，未能成功发送调光指令");
                                }
                            }catch (Exception e){
                                log.error(Thread.currentThread().getName()+"推送调光指令异常，未能成功发送调光指令");
                            }
                        } catch (InterruptedException e) {
                            log.error("经过一辆车，当前线程被阻断。");
                        }
                    });
                }
                break;
            case 2://2节能模式
                Map nowMap = new HashMap();
                for (Map map:jsonArry) {
                    String startTime = map.get("startTime").toString();
                    String endTimne = map.get("endTime").toString();
                    //获取当前时间  查看是否符合当前时间段
                    try {
                        //查看当前时间是否在此时间范围内
                        if(DateUtils.belongCalendar(startTime,endTimne)){
                            nowMap = map;
                            break;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(nowMap.size()<=0){
                    log.info("当前过车信息未在时间范围内，故放弃当前调光。");
                    return;
                }
                Map finalNowMap = nowMap;

                //当前时段  亮度值
                Integer eluminanceRange =  Integer.parseInt(finalNowMap.get("value").toString());

                //查看1分钟内车流量  是否超过最大车流量  maxTrafficFlow
                int enowLuminanceRange =  sdEnhancedLightingConfigService.getLuminanceByParam(nowTrafficFlow,maxTrafficFlow,maxLuminanceRange,minLuminanceRange,eluminanceRange);

                //循环推送当前调光值
                for (SdDevices devices:deviceIds) {
                    ThreadPool.executor.execute(() -> {
                        log.info("【"+Thread.currentThread().getName()+"】开始准备调光：");
                        try {
                            //查看当前线程  threadArrs  集合是否存在。
                            Thread[] threadArrs;
                            String key = tunnelId+"_"+devices.getEqId();
                            if(threadArrsMap.containsKey(key)){
                                threadArrs = threadArrsMap.get(key);
                            }else{
                                threadArrs = new Thread[1];
                                threadArrsMap.put(key,threadArrs);
                            }
                            //缓存获取亮度值  与当前亮度值   与当前亮度值比对。如果相同 忽略当前操作。
                            Integer num = Integer.parseInt(devices.getData());
                            if(num == null ){
                                //给与初始值
                                num = eluminanceRange;
                            }
                            if(threadArrs[0]==null|| num ==null || num != enowLuminanceRange){
                                //推送调光 指令。
                                try{
//                                    log.info("开始亮光值:["+devices.getEqId()+"]当前亮度num："+num+" 根据车流量计算的亮度nowLuminanceRange:" +enowLuminanceRange);
                                    log.info("开始亮光值:[{}}]当前亮度num：{} 根据车流量计算的亮度nowLuminanceRange:{}" ,devices.getEqId() ,num ,enowLuminanceRange);
                                    int flag = sanJingLight.setBrightnessByDevice(devices,num,enowLuminanceRange,"2");
                                    if(flag == 0){
                                        log.error(Thread.currentThread().getName()+"推送调光指令异常，未能成功发送调光指令");
                                    }
                                }catch (Exception e){
                                    log.error(Thread.currentThread().getName()+"推送调光指令异常，未能成功发送调光指令");
                                }
                            }
                            //替换线程
                            Threads.replaceThread(threadArrs,Thread.currentThread());
                            //等待30秒后 执行 降低 光照强度功能
                            Thread.sleep(respondTime);
                            //降低光照强度执行完毕,推送调光 指令。
                            try{
                                log.info("结束亮光值:[{}]当前亮度nowLuminanceRange:{} 结束推送亮度值:{}" ,devices.getEqId(), enowLuminanceRange, minLuminance);
                                int flag = sanJingLight.setBrightnessByDevice(devices,enowLuminanceRange,minLuminance,"2");
                                //清除当前记录线程
                                threadArrs[0] =  null;
                                if(flag == 0){
                                    log.error(Thread.currentThread().getName()+"推送调光指令异常，未能成功发送调光指令");
                                }
                            }catch (Exception e){
                                log.error(Thread.currentThread().getName()+"推送调光指令异常，未能成功发送调光指令");
                            }
                        } catch (Exception e) {
                            log.error("经过一辆车，当前线程被阻断。");
                        }
                    });
                }
                break;
        }
    }
}
