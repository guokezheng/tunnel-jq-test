package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.TunnelEnum;
import com.tunnel.business.datacenter.domain.enumeration.WjCarVolumeEnum;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdRadarDetectData;
import com.tunnel.business.domain.event.SdRadarDetectDataTemporary;
import com.tunnel.business.domain.event.SdTrafficVolume;
import com.tunnel.business.mapper.digitalmodel.SdRadarDetectDataMapper;
import com.tunnel.business.mapper.digitalmodel.SdRadarDetectDataTemporaryMapper;
import com.tunnel.business.mapper.digitalmodel.SdTrafficVolumeMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.event.ISdEventService;
import com.zc.common.core.kafka.kafkaTool;
import com.zc.common.core.websocket.WebSocketService;
import com.zc.websocket.bo.ChannelProperty;
import com.zc.websocket.constant.AttributeKeyConst;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${wj_carVolume}")
    private String glzName;

    @Resource(name = "HttpTemplate")
    private RestTemplate template;

    @Autowired
    private SdTrafficVolumeMapper volumeMapper;


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
//        List<String> scanKey = redisCache.getCacheList("caKokenList");
//        List<Object> caKokenList = new ArrayList<>();
//        for (String key :scanKey) {
//            //获取隧道以及是否推送
//            Map<String, Object> cacheMap = redisCache.getCacheMap(key);
//            //推送的token
//            String s = key.replaceAll(Constants.CAR_TOKEN, "");
//            for (Channel channel : MsgUtil.channels){
//                ChannelProperty channelProperty = channel.attr(AttributeKeyConst.CHANNEL_PROPERTY_KEY).get();
//                if(s.equalsIgnoreCase(channelProperty.getTokenSN())){
//                    caKokenList.add((Constants.CAR_TOKEN+s));
//                }
//            }
//        }
//        redisCache.deleteObject("caKokenList");
//        redisCache.setCacheList("caKokenList",caKokenList);
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

    public void redisRadarData(){
        SdRadarDetectDataTemporaryMapper temporaryMapper = SpringUtils.getBean(SdRadarDetectDataTemporaryMapper.class);
        //获取前一个小时的数据
        List<String> hourVehicleData = temporaryMapper.hourVehicleData();
        Collection<String> keys = redisCache.keys("vehicleSnap:*");
        List<Map<String, Object>> list = (List<Map<String, Object>>) redisTemplate.opsForValue().multiGet(keys);
        for(Map<String, Object> item : list){
            try {
                SdRadarDetectDataTemporary temporary = new SdRadarDetectDataTemporary();
                temporary.setTunnelId(item.get("tunnelId").toString());
                temporary.setRoadDir(item.get("roadDir").toString());
                temporary.setSpeed(item.get("speed").toString());
                temporary.setVehicleType(item.get("vehicleType").toString());
                temporary.setLatitude(item.get("lat").toString());
                temporary.setLongitude(item.get("lng").toString());
                temporary.setDistance(item.get("distance").toString());
                temporary.setVehicleLicense(item.get("vehicleLicense").toString());
                temporary.setVehicleId(item.get("vehicleId").toString());
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                Date detectTime1 = sdf.parse(item.get("detectTime").toString());
                temporary.setDetectTime(detectTime1);
                boolean contains = hourVehicleData.contains(temporary.getVehicleLicense());
                if(contains){
                    temporaryMapper.updateSyncDetectData(temporary);
                }else {
                    temporaryMapper.insertSdRadarDetectData(temporary);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void getCarVolume(){
        List<Map<String, String>> list = new ArrayList<>();
        String path = "/flow/today";
        Map<String, String > map = new HashMap<>();
        if("wzb".equals(glzName)){
            map.put("tunnelId",WjCarVolumeEnum.HU_SHAN.getCode());
            map.put("url",WjCarVolumeEnum.HU_SHAN.getName() + path);
            list.add(map);
        }else if("th".equals(glzName)){
            map.put("tunnelId",WjCarVolumeEnum.QING_FENG_LING.getCode());
            map.put("url",WjCarVolumeEnum.QING_FENG_LING.getName() + path);
            list.add(map);
            map.put("tunnelId",WjCarVolumeEnum.PAN_DING_SHAN.getCode());
            map.put("url",WjCarVolumeEnum.PAN_DING_SHAN.getName() + path);
            list.add(map);
        }else if("mz".equals(glzName)){
            map.put("tunnelId",WjCarVolumeEnum.TAI_HE_SHAN.getCode());
            map.put("url",WjCarVolumeEnum.TAI_HE_SHAN.getName() + path);
            list.add(map);
            map.put("tunnelId",WjCarVolumeEnum.TIAN_CI_SHAN.getCode());
            map.put("url",WjCarVolumeEnum.TIAN_CI_SHAN.getName() + path);
            list.add(map);
        }else if("jly".equals(glzName)){
            map.put("tunnelId",WjCarVolumeEnum.MA_AN_SHAN.getCode());
            map.put("url",WjCarVolumeEnum.MA_AN_SHAN.getName() + path);
            list.add(map);
            map.put("tunnelId",WjCarVolumeEnum.JIN_JIA_LOU.getCode());
            map.put("url",WjCarVolumeEnum.JIN_JIA_LOU.getName() + path);
            list.add(map);
        }else if("yts".equals(glzName)){
            map.put("tunnelId",WjCarVolumeEnum.SHUANG_ZI_SHAN.getCode());
            map.put("url",WjCarVolumeEnum.SHUANG_ZI_SHAN.getName() + path);
            list.add(map);
            map.put("tunnelId",WjCarVolumeEnum.YANG_TIAN_SHAN.getCode());
            map.put("url",WjCarVolumeEnum.YANG_TIAN_SHAN.getName() + path);
            list.add(map);
        }
        for(Map<String, String> item : list){
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(item.get("url"));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Map> exchange = template.exchange(builder.build().toUri(), HttpMethod.GET, requestEntity, Map.class);
            HashMap<String, Object> body = (HashMap<String, Object>)exchange.getBody();
            if("success".equals(body.get("status").toString())){
                List<Map<String, Object>> mapList = volumeMapper.selectCarNumber(item.get("tunnelId"));
                Map<String, Object> data = (Map<String, Object> )body.get("data");
                String up = "";
                String down = "";
                for(Map<String, Object> objectMap : mapList){
                    if("1".equals(objectMap.get("direction").toString())){
                        Integer oldData = Integer.valueOf(objectMap.get("originalData").toString());
                        Integer newData = Integer.valueOf(data.get("up").toString());
                        up = (newData - oldData) + "";
                    }else {
                        Integer oldData = Integer.valueOf(objectMap.get("originalData").toString());
                        Integer newData = Integer.valueOf(data.get("down").toString());
                        down = (newData - oldData) + "";
                    }
                }
                setTrafficVolumeData(item.get("tunnelId"),"1",up == "" ? "0" : up,data.get("up").toString());
                setTrafficVolumeData(item.get("tunnelId"),"2",down == "" ? "0" : down,data.get("down").toString());
            }
        }
    }

    /**
     * 存入车流量
     * @param tunnelId
     * @param direction
     * @param carNumber
     */
    public void setTrafficVolumeData(String tunnelId,String direction,String carNumber,String originalData){
        SdTrafficVolume sdTrafficVolume = new SdTrafficVolume();
        sdTrafficVolume.setTunnelId(tunnelId);
        sdTrafficVolume.setDirection(direction);
        sdTrafficVolume.setCarNumber(carNumber);
        sdTrafficVolume.setOriginalData(originalData);
        volumeMapper.insertSdTrafficVolume(sdTrafficVolume);
    }
}
