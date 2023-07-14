package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.system.service.ISysDictDataService;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.datacenter.domain.enumeration.TunnelEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceTypeItem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.digitalmodel.SdSpecialVehicles;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdRadarDetectData;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.mapper.digitalmodel.RadarEventMapper;
import com.tunnel.business.mapper.digitalmodel.SdRadarDetectDataMapper;
import com.tunnel.business.mapper.digitalmodel.SdSpecialVehiclesMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDeviceTypeItemService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.deal.plc.modbus.ModbusTcpHandle;
import com.zc.common.core.websocket.WebSocketService;
import com.zc.websocket.bo.ChannelProperty;
import com.zc.websocket.constant.AttributeKeyConst;
import com.zc.websocket.util.MsgUtil;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import io.netty.channel.Channel;
import java.util.concurrent.TimeUnit;

import static com.ruoyi.common.utils.DictUtils.getCacheEventKey;

/**
 * 小车跑数据
 *
 * @author zhai
 * @date 2022/11/9
 */
@Component("radarTask")
public class RadarTask {

    private static final Logger log = LoggerFactory.getLogger(RadarTask.class);

    @Value("${authorize.name}")
    private String deploymentType;

    @Autowired
    private RedisCache redisCache;


    @Autowired
    private ISdDeviceDataService sdDeviceDataService;
    @Autowired
    private SdRadarDetectDataMapper sdRadarDetectDataMapper;
    @Autowired
    private ISdEventService sdEventService;


    public static  List<SdRadarDetectData> sdRadarDetectDatalist = null;
    public static  List<SdRadarDetectData> sdRadarDetectDatalist1 = null;
    public static  List<SdRadarDetectData> sdRadarDetectDatalist2 = null;

    public int num = 0;
    public int num1 = 0;
    public int num2 = 0;
    public int num3 = 0;

    /**
     * 测试 遍历redis传事故位置到前端显示
     * @throws InterruptedException
     */
//    @Scheduled(fixedRate = 10000)
    public void radarEven2() throws InterruptedException {
        num3 =num3+1;
        SdEvent sdEventd = new SdEvent();
        sdEventd.setId(3260L);
        List<SdEvent> sdEventList = sdEventService.querySdEventList(sdEventd);
        sdEventList.stream().forEach(sd ->{
            redisCache.setCacheObject(getCacheEventKey(sd.getId().toString()),sd);
        });
        List<String> scanKey = redisCache.getScanKey(Constants.EVENT_KEY + "*");
        List<SdEvent> sdEventLists = new ArrayList<>();
        for (String key :scanKey){
            SdEvent sdEvent = redisCache.getCacheObject(key);
            sdEvent.setDistance("120");
            sdEvent.setRoadDir(sdEvent.getDirection());
            sdEventLists.add(sdEvent);
        }

        if(num3 % 2 == 0) {
            //事件后推送前端  弹出
            JSONObject object = new JSONObject();
            object.put("sdSvgEventList", sdEventLists);
            WebSocketService.broadcast("sdSvgEventList",object.toString());
        } else {
            //事件后推送前端  弹出
            JSONObject object = new JSONObject();
            object.put("sdSvgEventList", new ArrayList<>());
            WebSocketService.broadcast("sdSvgEventList",object.toString());
        }


    }

    /**
     * 遍历redis传事故位置到前端显示
     * @throws InterruptedException
     */
    @Scheduled(fixedRate = 5000)
    public void radarEvent() throws InterruptedException {
//        num3 =num3+1;
//        SdEvent sdEventd = new SdEvent();
//        sdEventd.setId(3260L);
//        List<SdEvent> sdEventList = sdEventService.querySdEventList(sdEventd);
//        sdEventList.stream().forEach(sd ->{
//            redisCache.setCacheObject(getCacheEventKey(sd.getId().toString()),sd);
//        });
        List<String> scanKey = redisCache.getScanKey(Constants.EVENT_KEY + "*");
        List<SdEvent> sdEventLists = new ArrayList<>();
        for (String key :scanKey){
            SdEvent sdEvent = redisCache.getCacheObject(key);
            sdEvent.setRoadDir(sdEvent.getDirection());
            sdEventLists.add(sdEvent);
        }
        //事件后推送前端  弹出
        JSONObject object = new JSONObject();
        object.put("sdSvgEventList", sdEventLists);
        WebSocketService.broadcast("sdSvgEventList",object.toString());
    }

    /**
     * 遍历redis传事故位置到前端显示
     * @throws InterruptedException
     */
//    @Scheduled(fixedRate = 5000)
    public void deleteTokenSN() throws InterruptedException {
        //获取所有需要发送消息客服端的token
        List<String> scanKey = redisCache.getCacheList("caKokenList");
        List<Object> caKokenList = new ArrayList<>();
        for (String key :scanKey) {
            //获取隧道以及是否推送
            Map<String, Object> cacheMap = redisCache.getCacheMap(key);
            //推送的token
            String s = key.replaceAll(Constants.CAR_TOKEN, "");
            for (Channel channel : MsgUtil.channels){
                ChannelProperty channelProperty = channel.attr(AttributeKeyConst.CHANNEL_PROPERTY_KEY).get();
                if(s.equalsIgnoreCase(channelProperty.getTokenSN())){
                    caKokenList.add((Constants.CAR_TOKEN+s));
                }
            }
        }
        redisCache.deleteObject("caKokenList");
        redisCache.setCacheList("caKokenList",caKokenList);
    }


//    @Scheduled(fixedRate = 100)
    public void radarTask1() throws InterruptedException {
        List<Map> list = new ArrayList<>();
        if(sdRadarDetectDatalist2==null){
            sdRadarDetectDatalist2 = sdRadarDetectDataMapper.selectSdRadarDetectDataByVehicleLicense("豫K0376E");
            if(sdRadarDetectDatalist2!=null&&sdRadarDetectDatalist.size()>0){
                sdRadarDetectDatalist2 = sdRadarDetectDatalist2;
            }
        }

        Map<String, Object> map1 = new HashMap<>();
        map1.put("tunnelId", TunnelEnum.HANG_SHAN_DONG.getCode());
        map1.put("roadDir",sdRadarDetectDatalist2.get(num2).getRoadDir());
        map1.put("speed",sdRadarDetectDatalist2.get(num2).getSpeed());
        map1.put("laneNo",sdRadarDetectDatalist2.get(num2).getLaneNum());
        map1.put("vehicleType",sdRadarDetectDatalist2.get(num2).getVehicleType());
        map1.put("lat",sdRadarDetectDatalist2.get(num2).getLatitude());
        map1.put("lng",sdRadarDetectDatalist2.get(num2).getLongitude());
        map1.put("distance",sdRadarDetectDatalist2.get(num2).getDistance());
        map1.put("vehicleLicense",sdRadarDetectDatalist2.get(num2).getVehicleLicense());

        list.add(map1);
        JSONObject object = new JSONObject();
        object.put("radarDataList", list);
        if(num2==sdRadarDetectDatalist2.size()-3){
            num2= 0;
            return;
        }else{
            num2=num2+1;
        }

        try {
//                WebSocketService.broadcast("radarDataList", object.toString());
            //通过redis获取需要推送的数据的key
            List<String> scanKey = redisCache.getScanKey(Constants.CAR_TOKEN + "*");
            for (String key :scanKey){
                Map<String, Object> cacheMap = redisCache.getCacheMap(key);
                String s = key.replaceAll(Constants.CAR_TOKEN, "");
                String tunnelId = (String)map1.get("tunnelId");
                for(String keys : cacheMap.keySet()){
                    if("0".equals(cacheMap.get(keys))&&tunnelId.equals(keys)){
                        WebSocketService.postEvent(s,"radarDataList",object.toString());
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("111111"+e.getMessage());
            System.out.println("222222"+e.getLocalizedMessage());
        }

    }

//    @Scheduled(fixedRate = 100)
    public void radarTask3() throws InterruptedException {
        List<Map> list = new ArrayList<>();
        if(sdRadarDetectDatalist1==null){
            sdRadarDetectDatalist1 = sdRadarDetectDataMapper.selectSdRadarDetectDataByVehicleLicense("辽JMJ225");
            if(sdRadarDetectDatalist1!=null&&sdRadarDetectDatalist.size()>0){
                sdRadarDetectDatalist1 = sdRadarDetectDatalist1;
            }
        }

        Map<String, Object> map1 = new HashMap<>();
        map1.put("tunnelId", TunnelEnum.HANG_SHAN_DONG.getCode());
        map1.put("roadDir",sdRadarDetectDatalist1.get(num).getRoadDir());
        map1.put("speed",sdRadarDetectDatalist1.get(num).getSpeed());
        map1.put("laneNo",sdRadarDetectDatalist1.get(num).getLaneNum());
        map1.put("vehicleType",sdRadarDetectDatalist1.get(num).getVehicleType());
        map1.put("lat",sdRadarDetectDatalist1.get(num).getLatitude());
        map1.put("lng",sdRadarDetectDatalist1.get(num).getLongitude());
        map1.put("distance",sdRadarDetectDatalist1.get(num).getDistance());
        map1.put("vehicleLicense",sdRadarDetectDatalist1.get(num).getVehicleLicense());

        list.add(map1);
        JSONObject object = new JSONObject();
        object.put("radarDataList", list);
        if(num==sdRadarDetectDatalist1.size()-3){
            num= 0;
            return;
        }else{
            num=num+1;
        }

        try {
//                WebSocketService.broadcast("radarDataList", object.toString());
            //通过redis获取需要推送的数据的key
            List<String> scanKey = redisCache.getScanKey(Constants.CAR_TOKEN + "*");
            for (String key :scanKey){
                Map<String, Object> cacheMap = redisCache.getCacheMap(key);
                String s = key.replaceAll(Constants.CAR_TOKEN, "");
                String tunnelId = (String)map1.get("tunnelId");
                for(String keys : cacheMap.keySet()){
                    if("0".equals(cacheMap.get(keys))&&tunnelId.equals(keys)){
                        WebSocketService.postEvent(s,"radarDataList",object.toString());
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("111111"+e.getMessage());
            System.out.println("222222"+e.getLocalizedMessage());
        }

    }

    /**
     * 推送事件提醒以及视频  测试用
     * @throws InterruptedException
     */
//    @Scheduled(fixedRate = 5000)
    public void radarTask2() throws InterruptedException {
        SdEvent sdEvent = new SdEvent();
        sdEvent.setId(3260L);
        List<SdEvent> sdEventList = sdEventService.querySdEventList(sdEvent);
        //新增事件后推送前端
        JSONObject object = new JSONObject();
        object.put("sdEventList", sdEventList);
        WebSocketService.broadcast("sdEventList",object.toString());
    }

}
