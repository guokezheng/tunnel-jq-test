package com.ruoyi.quartz.task;


import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.enhancedLighting.SdEnhancedLightingConfig;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.digitalmodel.SdRadarDetectDataTemporaryMapper;
import com.tunnel.business.mapper.enhancedLighting.SdEnhancedLightingConfigMapper;
import com.tunnel.deal.light.impl.SanJingLight;
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
 * 定时任务模式
 * 选择   定时模式时,执行当前定时任务。
 */
@Component("lightFixedTimeTask")
public class LightFixedTimeTask {

    private static final Logger log = LoggerFactory.getLogger(TestTask.class);

    private static SdEnhancedLightingConfigMapper sdEnhancedLightingConfigMapper = SpringUtils.getBean(SdEnhancedLightingConfigMapper.class);

    private SdRadarDetectDataTemporaryMapper sdRadarDetectDataTemporaryMapper = SpringUtils.getBean(SdRadarDetectDataTemporaryMapper.class);

    private SdDevicesMapper sdDevicesMapper =  SpringUtils.getBean(SdDevicesMapper.class);

    private SdDeviceDataMapper sdDeviceDataMapper = SpringUtils.getBean(SdDeviceDataMapper.class);

    private SanJingLight sanJingLight =  SpringUtils.getBean(SanJingLight.class);

    private RedisCache redisCache = SpringUtils.getBean(RedisCache.class);


    /**
     * 当前调光模式为   定时调光模式
     */
    public void runTask(){
        //获取当前调光配置信息
        List<SdEnhancedLightingConfig> sdEnhancedLightingConfigList;
        sdEnhancedLightingConfigList = redisCache.getCacheObject("control:lightFixedTimeTask");
        if(sdEnhancedLightingConfigList == null){
            //根据隧道id获取对应隧道  加强照明策略 配置信息
            sdEnhancedLightingConfigList =  sdEnhancedLightingConfigMapper.selectSdEnhancedLightingConfigList(new SdEnhancedLightingConfig());
            redisCache.setCacheObject("control:lightFixedTimeTask",sdEnhancedLightingConfigList);
            redisCache.expire("control:lightFixedTimeTask",60);    //每分钟，重新请求一次
        }
        for (SdEnhancedLightingConfig sdEnhancedLightingConfig:sdEnhancedLightingConfigList) {
            //定时 开启上行隧道
            adjustBrightnessByRunMode(sdEnhancedLightingConfig.getTunnelId(),"1",sdEnhancedLightingConfig);
            //定时 开启下行隧道
            adjustBrightnessByRunMode(sdEnhancedLightingConfig.getTunnelId(),"2",sdEnhancedLightingConfig);
        }
    }



    /**
     *
     * @param tunnelId      隧道id
     * @param roadDir     方向
     */
    public void adjustBrightnessByRunMode(String tunnelId,String roadDir,SdEnhancedLightingConfig sdEnhancedLightingConfig){
        //查找所有加强照明
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqTunnelId(tunnelId);
        sdDevices.setEqDirection(roadDir);
        sdDevices.setEqType(DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode());
        sdDevices.setItemId(DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode());
        //推送加强照明
        List<SdDevices> deviceIds = sdDevicesMapper.selectSdDevicesDataByParam(sdDevices);
        if(deviceIds.size()<=0){
            log.info("当前隧道【{}】无加强照明设备。忽略当前调光操作。",tunnelId);
            return;
        }
        //从 sd_radar_detect_data_temporary  表中 获取当前1分钟内过车流量信息
        int nowTrafficFlow = sdRadarDetectDataTemporaryMapper.getSdRadarDetectDataCount(tunnelId,roadDir);
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

        Integer luminanceRange;
        //定时模式
        //根据隧道ID  以及方向查出所有 加强照明设备。
        for (Map map:jsonArry) {
            String startTime = map.get("startTime").toString();
            String endTimne = map.get("endTime").toString();
            //获取当前时间  查看是否符合当前时间段
            try {
                //查看当前时间是否在此时间范围内
                if(belongCalendar(startTime,endTimne)){
                    //根据每个 设备  亮度进行逐个调光
                    for (SdDevices devices:deviceIds) {
                        //缓存获取亮度值  与当前亮度值   与当前亮度值比对。如果相同 忽略当前操作。
                        Integer num = Integer.parseInt(devices.getData());
                        luminanceRange =  Integer.parseInt(map.get("value").toString());
                        //查看1分钟内车流量  是否超过最大车流量  maxTrafficFlow
                        int nowLuminanceRange =  getLuminanceByParam(nowTrafficFlow,maxTrafficFlow,maxLuminanceRange,minLuminanceRange,luminanceRange);
                        log.info("当前亮度num："+num+" 根据车流量计算的亮度nowLuminanceRange:" +nowLuminanceRange);
                        if(num ==null || num != nowLuminanceRange){
                            ThreadPool.executor.execute(() -> {
                                //推送调光 指令。
                                try{
                                    log.info(Thread.currentThread().getName()+"开始推送调光指令");
                                    int flag = sanJingLight.setBrightnessByDevice(devices,nowLuminanceRange,"2");
                                    if(flag == 0){
                                        log.error(Thread.currentThread().getName()+"推送调光指令异常，未能成功发送调光指令");
                                    }
                                }catch (Exception e){
                                    log.error(Thread.currentThread().getName()+"推送调光指令异常，未能成功发送调光指令");
                                }
                            });
                        }
                    }
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
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
