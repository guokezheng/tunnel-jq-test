package com.ruoyi.quartz.task;


import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.enhancedLighting.SdEnhancedLightingConfig;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.digitalmodel.SdRadarDetectDataTemporaryMapper;
import com.tunnel.business.mapper.enhancedLighting.SdEnhancedLightingConfigMapper;
import com.tunnel.deal.light.impl.SanJingLight;
import com.tunnel.deal.vehicleinspection.MicrowaveNettyClientHandler;
import com.zc.common.core.ThreadPool.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 测试定时任务
 * testTask.runTask
 */
@Component("testTask")
public class TestTask {

    private static final Logger log = LoggerFactory.getLogger(TestTask.class);

    private static SdEnhancedLightingConfigMapper sdEnhancedLightingConfigMapper = SpringUtils.getBean(SdEnhancedLightingConfigMapper.class);

    private SanJingLight sanJingLight =  SpringUtils.getBean(SanJingLight.class);

    private RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    private static Thread[] threadArrs = new Thread[1];

    private int lowLuminance = 20;


    public void runTask(){
        //模拟数据
        for (int i = 0; i < 100; i++) {
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
        adjustBrightnessByRunMode(tunnelId,roadDir,carNumber);
    }

    /**
     *
     * @param tunnelId      隧道id
     * @param roadDir     方向
     */
    public void adjustBrightnessByRunMode(String tunnelId,String roadDir,Integer carNumber){
        SdEnhancedLightingConfig sdEnhancedLightingConfig;
        sdEnhancedLightingConfig = redisCache.getCacheObject("control:"+tunnelId+"_config");
        if(sdEnhancedLightingConfig == null){
            //根据隧道id获取对应隧道  加强照明策略 配置信息
            sdEnhancedLightingConfig = new SdEnhancedLightingConfig();
            sdEnhancedLightingConfig.setTunnelId(tunnelId);
            sdEnhancedLightingConfig = sdEnhancedLightingConfigMapper.selectSdEnhancedLightingConfigListByParam(sdEnhancedLightingConfig);
            redisCache.setCacheObject("control:"+tunnelId+"_config",sdEnhancedLightingConfig);
            redisCache.expire("control:"+tunnelId+"_config",60);    //每分钟，重新请求一次
        }
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
        //从 sd_radar_detect_data_temporary  表中 获取当前1分钟内过车流量信息
        //int nowTrafficFlow = sdRadarDetectDataTemporaryMapper.getSdRadarDetectDataCount();
        int nowTrafficFlow =  carNumber;

        //测试数据  后去需要根据隧道id  以及方向  获取 所有加强照明信息
        List<SdDevices> deviceIds = new ArrayList<>();
        SdDevices sdDevices1 = new SdDevices();
        sdDevices1.setEqId("JQ-WeiFang-JiuLongYu-HSD-RLC-001");
        SdDevices sdDevices2 = new SdDevices();
        sdDevices2.setEqId("JQ-WeiFang-JiuLongYu-HSD-RLC-001");
        SdDevices sdDevices3 = new SdDevices();
        sdDevices3.setEqId("JQ-WeiFang-JiuLongYu-HSD-RLC-001");
        deviceIds.add(sdDevices1);
        deviceIds.add(sdDevices2);
        deviceIds.add(sdDevices3);


        String operIp = "";
        try {
            operIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Integer luminanceRange = 0;
        // 0定时模式  1  自动模式  2 节能模式
        switch (modeType){
            case 0:
                //定时模式
                //根据隧道ID  以及方向查出所有 加强照明设备。
                for (Map map:jsonArry) {
                    String startTime = map.get("startTime").toString();
                    String endTimne = map.get("endTime").toString();
                    //获取当前时间  查看是否符合当前时间段
                    try {
                        //查看当前时间是否在此时间范围内
                        if(belongCalendar(startTime,endTimne)){
                            //缓存获取亮度值  与当前亮度值   与当前亮度值比对。如果相同 忽略当前操作。
                            Integer num = redisCache.getCacheObject("control:"+tunnelId+"_LuminanceRange");

                            luminanceRange =  Integer.parseInt(map.get("value").toString());
                            //查看1分钟内车流量  是否超过最大车流量  maxTrafficFlow
                            int nowLuminanceRange =  getLuminanceByParam(nowTrafficFlow,maxTrafficFlow,maxLuminanceRange,minLuminanceRange,luminanceRange);
                            if(num == null ){
                                num = luminanceRange;
                            }
                            log.info("当前亮度num："+num+" 根据车流量计算的亮度nowLuminanceRange:" +nowLuminanceRange);
                            if(num ==null || num != nowLuminanceRange){
                                redisCache.setCacheObject("control:"+tunnelId+"_LuminanceRange",nowLuminanceRange);
                                Integer finalNum = num;
                                String finalOperIp = operIp;
                                ThreadPool.executor.execute(() -> {
                                    //推送调光 指令。
                                    sanJingLight.setBrightnessByList(deviceIds, finalNum,nowLuminanceRange,"2", finalOperIp);
                                    log.info(Thread.currentThread().getName()+"开始推送调光指令");
                                });
                            }
                            break;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 1:  //自动模式
                //当前亮度值初始值
                luminanceRange = sdEnhancedLightingConfig.getBeforeLuminance();
                //查看1分钟内车流量  是否超过最大车流量  maxTrafficFlow
                int nowLuminanceRange =  getLuminanceByParam(nowTrafficFlow,maxTrafficFlow,maxLuminanceRange,minLuminanceRange,luminanceRange);

                String finalOperIp = operIp;
                Integer finalLuminanceRange = luminanceRange;
                ThreadPool.executor.execute(() -> {
                    try {
                        //缓存获取亮度值  与当前亮度值   与当前亮度值比对。如果相同 忽略当前操作。
                        Integer num = redisCache.getCacheObject("control:"+tunnelId+"_LuminanceRange");
                        if(num == null ){
                            //给与初始值
                            num = finalLuminanceRange;
                        }
                        log.info("当前亮度num："+num+" 根据车流量计算的亮度nowLuminanceRange:" +nowLuminanceRange);
                        if(threadArrs[0]==null|| num == null || num != nowLuminanceRange){
                            //推送调光 指令。
                            sanJingLight.setBrightnessByList(deviceIds, num,nowLuminanceRange,"2", finalOperIp);
                            redisCache.setCacheObject("control:"+tunnelId+"_LuminanceRange",nowLuminanceRange);
                            log.info(Thread.currentThread().getName()+"开始推送调光指令，原有调光值："+num+"    当前调光值:"+nowLuminanceRange);
                        }
                        //替换线程
                        replaceThread(Thread.currentThread());
                        //等待30秒后 执行 降低 光照强度功能
                        Thread.sleep(respondTime);
                        log.info(Thread.currentThread().getName()+"结束推送调光指令，原有调光值："+nowLuminanceRange+"    当前调光值:"+lowLuminance);
                        //降低光照强度执行完毕
                        sanJingLight.setBrightnessByList(deviceIds, nowLuminanceRange,lowLuminance,"2", finalOperIp);
                        //记录当前亮度值
                        redisCache.setCacheObject("control:"+tunnelId+"_LuminanceRange",lowLuminance);
                        //清除当前记录线程
                        threadArrs[0] =  null;
                    } catch (InterruptedException e) {
                        log.error("经过一辆车，当前线程被阻断。");
                    }
                });
                break;
            case 2://2节能模式
                Map nowMap = new HashMap();
                for (Map map:jsonArry) {
                    String startTime = map.get("startTime").toString();
                    String endTimne = map.get("endTime").toString();
                    //获取当前时间  查看是否符合当前时间段
                    try {
                        //查看当前时间是否在此时间范围内
                        if(belongCalendar(startTime,endTimne)){
                            nowMap = map;
                            break;
                        }else{
                            log.info("当前过车信息未在时间范围内，故放弃当前调光。");
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                Map finalNowMap = nowMap;
                String finalOperIp1 = operIp;
                //当前时段  亮度值
                Integer eluminanceRange =  Integer.parseInt(finalNowMap.get("value").toString());

                //查看1分钟内车流量  是否超过最大车流量  maxTrafficFlow
                int enowLuminanceRange =  getLuminanceByParam(nowTrafficFlow,maxTrafficFlow,maxLuminanceRange,minLuminanceRange,eluminanceRange);

                ThreadPool.executor.execute(() -> {
                    try {
                        //缓存获取亮度值  与当前亮度值   与当前亮度值比对。如果相同 忽略当前操作。
                        Integer num = redisCache.getCacheObject("control:"+tunnelId+"_LuminanceRange");
                        if(num == null ){
                            num = eluminanceRange;
                        }
                        Integer finalNum = num;
                        String efinalOperIp = finalOperIp1;
                        log.info("当前亮度num："+num+" 根据车流量计算的亮度nowLuminanceRange:" +enowLuminanceRange);
                        if(threadArrs[0]==null|| num ==null || num != enowLuminanceRange){
                            ThreadPool.executor.execute(() -> {
                                //推送调光 指令。
                                sanJingLight.setBrightnessByList(deviceIds, finalNum,enowLuminanceRange,"2", efinalOperIp);
                                redisCache.setCacheObject("control:"+tunnelId+"_LuminanceRange",enowLuminanceRange);
                                log.info(Thread.currentThread().getName()+"开始推送调光指令，原有调光值："+finalNum+"    当前调光值:"+enowLuminanceRange);
                            });
                        }
                        //替换线程
                        replaceThread(Thread.currentThread());
                        //等待30秒后 执行 降低 光照强度功能
                        Thread.sleep(respondTime);

                        System.out.println(Thread.currentThread().getName()+"结束推送调光指令，原有调光值："+enowLuminanceRange+"    当前调光值:"+lowLuminance);
                        //降低光照强度执行完毕
                        sanJingLight.setBrightnessByList(deviceIds, enowLuminanceRange,lowLuminance,"2", efinalOperIp);
                        //记录当前亮度值
                        redisCache.setCacheObject("control:"+tunnelId+"_LuminanceRange",lowLuminance);
                        //清除当前记录线程
                        threadArrs[0] =  null;
                    } catch (InterruptedException e) {
                        log.error("经过一辆车，当前线程被阻断。");
                    }
                });
                break;

        }
    }


    /**
     *
     * @param thread
     */
    public static synchronized void replaceThread(Thread thread){
        if(threadArrs[0]!=null){
            Thread oldThread = threadArrs[0];
            //删除旧线程
            oldThread.interrupt();
        }
        //存入新线程
        threadArrs[0] = thread;
    }


    public static boolean belongCalendar(String startTimeStr, String endTimneStr) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // 设置日期格式
        Date nowTime = df.parse(df.format(new Date()));
        Date startTime = df.parse(startTimeStr);
        Date endTime = df.parse(endTimneStr);
        boolean result;
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar start = Calendar.getInstance();
        start.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        //查看当前  开始时间是否大于结束时间
        if(start.getTimeInMillis()>end.getTimeInMillis()){
            end.add(Calendar.DAY_OF_MONTH,1);
            if (date.after(start) && date.before(end)) {
                result =  true;
            } else {
                result = false;
            }
        }else{
            if (date.after(start) && date.before(end)) {
                result =  true;
            } else {
                result = false;
            }
        }
        return result;
    }


    /**
     *
     * @param nowTrafficFlow    当前车流量
     * @param maxTrafficFlow     最大车流量
     * @param maxLuminanceRange     最大调光区间值
     * @param minLuminanceRange     最小调光区间值
     * @param luminanceRange        当前时间段调光值
     * @return
     */
    public static int getLuminanceByParam(Integer nowTrafficFlow, Integer maxTrafficFlow, Integer maxLuminanceRange,Integer minLuminanceRange,Integer luminanceRange) {
        if(nowTrafficFlow >= maxTrafficFlow ){
            //当前车流量大于现在车流量
            return luminanceRange+maxLuminanceRange;
        }else{
            Integer regionLuminanceRange = maxLuminanceRange - minLuminanceRange;
            //计算公式  (当前车流量/最大车流量)*亮度区间值
            BigDecimal nowTrafficFlowBig = new BigDecimal(nowTrafficFlow);
            BigDecimal maxTrafficFlowBig = new BigDecimal(maxTrafficFlow);
            BigDecimal regionLuminanceRangeBig = new BigDecimal(regionLuminanceRange);
            nowTrafficFlowBig = nowTrafficFlowBig.divide(maxTrafficFlowBig, 2,BigDecimal.ROUND_HALF_UP).multiply(regionLuminanceRangeBig);
            return luminanceRange + (nowTrafficFlowBig.intValue()/5)*5;
        }
    }
}
