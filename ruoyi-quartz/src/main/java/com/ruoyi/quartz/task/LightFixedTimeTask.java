package com.ruoyi.quartz.task;


import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.*;
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
 * 定时任务模式
 * 选择   定时模式时,执行当前定时任务。
 */
@Component("lightFixedTimeTask")
public class LightFixedTimeTask {

    private static final Logger log = LoggerFactory.getLogger(LightFixedTimeTask.class);

    private static ISdEnhancedLightingConfigService sdEnhancedLightingConfigService = SpringUtils.getBean(ISdEnhancedLightingConfigService.class);

    private SdRadarDetectDataTemporaryMapper sdRadarDetectDataTemporaryMapper = SpringUtils.getBean(SdRadarDetectDataTemporaryMapper.class);

    private SdDevicesMapper sdDevicesMapper =  SpringUtils.getBean(SdDevicesMapper.class);

    private SdDeviceDataMapper sdDeviceDataMapper =  SpringUtils.getBean(SdDeviceDataMapper.class);

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
            sdEnhancedLightingConfigList =  sdEnhancedLightingConfigService.selectSdEnhancedLightingConfigList(new SdEnhancedLightingConfig());
            redisCache.setCacheObject("control:lightFixedTimeTask",sdEnhancedLightingConfigList);
            redisCache.expire("control:lightFixedTimeTask",60);    //每分钟，重新请求一次
        }
        for (SdEnhancedLightingConfig sdEnhancedLightingConfig:sdEnhancedLightingConfigList) {
            //查看当前隧道 照明配置文件中  模式类型是否为定时模式，如果为定时模式 则继续执行调光指令
            //查看当前隧道 照明配置文件中  是否开启调光模式  0关闭  1开启
            if(sdEnhancedLightingConfig.getModeType() == 0 && sdEnhancedLightingConfig.getIsStatus() == 1){
                ThreadPool.executor.execute(() -> {
                    //定时 开启上行隧道  TunnelDirectionEnum.UP_DIRECTION TunnelDirectionEnum.UP_DIRECTION.getCode(),
                    adjustBrightnessByRunMode(sdEnhancedLightingConfig.getTunnelId(),sdEnhancedLightingConfig);
                });
            }
        }
    }



    /**
     *
     * @param tunnelId      隧道id
     */
    public void adjustBrightnessByRunMode(String tunnelId,SdEnhancedLightingConfig sdEnhancedLightingConfig){
        //获取配置信息
        String json = sdEnhancedLightingConfig.getTimeSlot();
        //配置信息 JSON
        List<Map> jsonArry  = JSONObject.parseArray(json, Map.class);
        //调光最大区间
        Integer  maxLuminanceRange = sdEnhancedLightingConfig.getMaxLuminanceRange();
        //调光最小区间
        Integer  minLuminanceRange = sdEnhancedLightingConfig.getMinLuminanceRange();
        //最大车流量
        Integer  maxTrafficFlow = Math.toIntExact(sdEnhancedLightingConfig.getMaxTrafficFlow());
        //当前时间段内  亮度值
        Integer luminanceRange;
        //定时模式
        //根据隧道ID  以及方向查出所有 加强照明设备。
        for (Map map:jsonArry) {
            String startTime = map.get("startTime").toString();
            String endTimne = map.get("endTime").toString();
            String roadDir =  map.get("direction").toString();
            //其他默认全选
            if(!TunnelDirectionEnum.UP_DIRECTION.getCode().equals(roadDir)&&!TunnelDirectionEnum.DOWN_DIRECTION.getCode().equals(roadDir)){
                roadDir = null;
            }
            //从 sd_radar_detect_data_temporary  表中 获取当前1分钟内过车流量信息
            int nowTrafficFlow = sdRadarDetectDataTemporaryMapper.getSdRadarDetectDataCount(tunnelId,roadDir);

            //获取当前时间  查看是否符合当前时间段
            try {
                //查看当前时间是否在此时间范围内
                if(DateUtils.belongCalendar(startTime,endTimne)){
                    //获取到 控制段信息 segment
                    String segment =  map.get("eqIds").toString();
                    if(segment == null ){
                        log.info("当前定时时间段【{}】无加强照明设备。忽略当前调光操作。",tunnelId);
                        return;
                    }
                    //根据每个 设备  亮度进行逐个调光
                    SdDevices devicesParam = new SdDevices();
                    //查看当前属于哪个 路段  并推送其路段 设备信息
                    devicesParam.setEqTunnelId(tunnelId);
                    devicesParam.setEqDirection(roadDir);
                    devicesParam.setItemId(DevicesTypeItemEnum.JQ_LIGHT_OPENCLOSE.getCode());
                    if(TunnelStepEnum.BASE_STEP.getValue().equals(segment)){
                        //基本照明
                        devicesParam.setEqType(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode());
                    }else {
                        devicesParam.setEqType( DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode());
                    }
                    devicesParam.setExternalDeviceId(segment);
                    //获取当前设备信息
                    List<SdDevices>  sdDevicesList = sdDevicesMapper.selectSdDevicesDataByParam(devicesParam);
                    for (SdDevices devices:sdDevicesList) {
                        //查看当前加强照明设备开启状态
                        SdDeviceData sdDeviceData = new SdDeviceData();
                        sdDeviceData.setDeviceId(devices.getEqId());
                        List<SdDeviceData> list = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
                        //查看当前设备是否存在开启状态
                        if(list == null ||list.size()<=0){
                            continue;
                        }else{
                            //获取开启状态
                            String status = "2";
                            Integer num = 0;
                            for (SdDeviceData sdDeviceDataInfo:list) {
                                if(DevicesTypeItemEnum.JI_BEN_ZHAO_MING_OPENCLOSE.getCode() == sdDeviceDataInfo.getItemId()||DevicesTypeItemEnum.JQ_LIGHT_OPENCLOSE.getCode() == sdDeviceDataInfo.getItemId()){
                                    status = sdDeviceDataInfo.getData();
                                }else if(DevicesTypeItemEnum.JB_LIGHT_BRIGHNESS.getCode() == sdDeviceDataInfo.getItemId()||DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode() == sdDeviceDataInfo.getItemId()){
                                    num = Integer.parseInt(sdDeviceDataInfo.getData());
                                }
                            }
                            //推送状态值
                            Integer finalNum = num;
                            //获取 当前时间段内  亮度值
                            luminanceRange =  Integer.parseInt(map.get("value").toString());
                            //如果当前时段调光值为 0   则关闭当前加强照明
                            if( luminanceRange == 0){
                                //状态（1-开，2-关）
                                int flag  = sanJingLight.lineControlAddLog(devices.getEqId(),2,null);
                                if(flag == 0){
                                    log.error("关闭当前照明无效,请联系管理员");
                                }
                                continue;
                            }
                            //当前设备状态是否为关闭
                            if("2".equals(status)){
                                //如果当前状态为关闭 ，则开启当前设备
                                int flag  = sanJingLight.lineControlAddLog(devices.getEqId(),1,null);
                                if(flag == 0){
                                    log.error("当前加强照明为关闭状态，开启失败。请联系管理员");
                                    continue;
                                }
                            }
//                            //缓存获取亮度值  与当前亮度值   与当前亮度值比对。如果相同 忽略当前操作。
//                            Integer num = Integer.parseInt(devices.getData());
                            //等待推送的亮度值
                            int nowLuminanceRange;
                            //查看是否开启车流量模式  0关闭  1开启
                            if(sdEnhancedLightingConfig.getIsTrafficVolume() == 1){
                                //查看1分钟内车流量  是否超过最大车流量  maxTrafficFlow
                                nowLuminanceRange =  sdEnhancedLightingConfigService.getLuminanceByParam(nowTrafficFlow,maxTrafficFlow,maxLuminanceRange,minLuminanceRange,luminanceRange);
                            }else{
                                nowLuminanceRange = luminanceRange;
                            }
                            if( num ==null || num != nowLuminanceRange || "2".equals(status)){
                                ThreadPool.executor.execute(() -> {
                                    //推送调光 指令。
                                    try{
                                        //推送指令时，判断当前灯是否 关闭。  如果关闭。 则开启当前灯  并且  调整 灯亮度。
                                        log.info("当前亮度num：{} 根据车流量计算的亮度nowLuminanceRange:{}", finalNum,nowLuminanceRange);
                                        int flag = sanJingLight.setBrightnessByDevice(devices, finalNum,nowLuminanceRange, DeviceControlTypeEnum.AUTO_EXEC.getCode());
                                        if(flag == 0){
                                            log.error(Thread.currentThread().getName()+"推送调光指令异常，未能成功发送调光指令");
                                        }
                                    }catch (Exception e){
                                        log.error(Thread.currentThread().getName()+"推送调光指令异常，未能成功发送调光指令");
                                    }
                                });
                            }
                        }
                    }
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
