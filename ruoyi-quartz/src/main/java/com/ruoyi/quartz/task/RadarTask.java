package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
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
import com.tunnel.business.domain.event.SdRadarDetectData;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.mapper.digitalmodel.RadarEventMapper;
import com.tunnel.business.mapper.digitalmodel.SdRadarDetectDataMapper;
import com.tunnel.business.mapper.digitalmodel.SdSpecialVehiclesMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDeviceTypeItemService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.deal.plc.modbus.ModbusTcpHandle;
import com.zc.common.core.websocket.WebSocketService;
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
import java.util.concurrent.TimeUnit;

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
    private RedisTemplate redisTemplate;

    @Autowired
    private RadarEventMapper radarEventMapper;

    @Autowired
    private ISdDeviceDataService sdDeviceDataService;
    @Autowired
    private SdRadarDetectDataMapper sdRadarDetectDataMapper;
    public static  List<SdRadarDetectData> sdRadarDetectDatalist = null;
    public static  List<SdRadarDetectData> sdRadarDetectDatalist1 = null;

    public int num = 0;

//    @Scheduled(fixedRate = 100)
    public void radarTask() {
        long l = System.currentTimeMillis();
        int count = redisCache.getCacheObject("count");
        SdRadarDetectData cacheObject = redisCache.getCacheObject("radar:" + count);
        redisCache.setCacheObject("count", count + 1);
        if (757 <= count) {
            redisCache.setCacheObject("count", 0);
        }
        controlDevice(cacheObject);
        //log.info("第" + count + "条   " + cacheObject);
        log.info("第" + count + "条   " + cacheObject.getDistance());
        List<SdRadarDetectData> list = new ArrayList<>();
        list.add(cacheObject);
        JSONObject object = new JSONObject();
        object.put("radarDataList", list);
        WebSocketService.broadcast("radarDataList", object.toString());
        long l1 = System.currentTimeMillis();
        log.info("耗时：" + (l1-l));
    }

    @Scheduled(fixedRate = 2000)
    public void radarTask1() throws InterruptedException {
        if(sdRadarDetectDatalist==null){
            sdRadarDetectDatalist = sdRadarDetectDataMapper.selectSdRadarDetectDataByVehicleLicense("粤BU0697");
            if(sdRadarDetectDatalist!=null&&sdRadarDetectDatalist.size()>0){
                sdRadarDetectDatalist = sdRadarDetectDatalist;
            }
        }


//        for( SdRadarDetectData sdRadarDetectData:sdRadarDetectDatalist){
            Map<String, Object> map = new HashMap<>();
            map.put("tunnelId", TunnelEnum.HANG_SHAN_DONG.getCode());
            map.put("roadDir",sdRadarDetectDatalist.get(num).getRoadDir());
            map.put("speed",sdRadarDetectDatalist.get(num).getSpeed());
            map.put("laneNo",sdRadarDetectDatalist.get(num).getLaneNum());
            map.put("vehicleType",sdRadarDetectDatalist.get(num).getVehicleType());
            map.put("lat",sdRadarDetectDatalist.get(num).getLatitude());
            map.put("lng",sdRadarDetectDatalist.get(num).getLongitude());
            map.put("distance",sdRadarDetectDatalist.get(num).getDistance());
            map.put("vehicleLicense",sdRadarDetectDatalist.get(num).getVehicleLicense());
            List<Map> list = new ArrayList<>();
            list.add(map);
            JSONObject object = new JSONObject();
            object.put("radarDataList", list);
            if(num==sdRadarDetectDatalist.size()-3){
                num= 0;
            }else{
                num=num+1;
            }

            try {
                WebSocketService.broadcast("radarDataList", object.toString());
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("111111"+e.getMessage());
                System.out.println("222222"+e.getLocalizedMessage());
            }

            //redis-key命名规则，固定字段vehicleSnap:隧道id:车牌号
//            redisCache.setCacheObject("vehicleSnap:" + sdRadarDetectData.getTunnelId() + ":" + sdRadarDetectData.getVehicleLicense(),map,5, TimeUnit.MINUTES);
//        }
    }

    @Scheduled(fixedRate = 500)
    public void radarTask2() throws InterruptedException {
        if(sdRadarDetectDatalist1==null){
            sdRadarDetectDatalist1 = sdRadarDetectDataMapper.selectSdRadarDetectDataByVehicleLicense("鲁BUD697");
            if(sdRadarDetectDatalist1!=null&&sdRadarDetectDatalist.size()>0){
                sdRadarDetectDatalist1 = sdRadarDetectDatalist1;
            }
        }


//        for( SdRadarDetectData sdRadarDetectData:sdRadarDetectDatalist1){
        Map<String, Object> map = new HashMap<>();
        map.put("tunnelId", TunnelEnum.HANG_SHAN_DONG.getCode());
        map.put("roadDir",sdRadarDetectDatalist1.get(num).getRoadDir());
        map.put("speed",sdRadarDetectDatalist1.get(num).getSpeed());
        map.put("laneNo",sdRadarDetectDatalist1.get(num).getLaneNum());
        map.put("vehicleType",sdRadarDetectDatalist1.get(num).getVehicleType());
        map.put("lat",sdRadarDetectDatalist1.get(num).getLatitude());
        map.put("lng",sdRadarDetectDatalist1.get(num).getLongitude());
        map.put("distance",sdRadarDetectDatalist1.get(num).getDistance());
        map.put("vehicleLicense",sdRadarDetectDatalist1.get(num).getVehicleLicense());
        List<Map> list = new ArrayList<>();
        list.add(map);
        JSONObject object = new JSONObject();
        object.put("radarDataList", list);
        if(num==sdRadarDetectDatalist1.size()-3){
            num= 0;
        }else{
            num=num+1;
        }

        try {
            WebSocketService.broadcast("radarDataList", object.toString());
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("111111"+e.getMessage());
            System.out.println("222222"+e.getLocalizedMessage());
        }

        //redis-key命名规则，固定字段vehicleSnap:隧道id:车牌号
//            redisCache.setCacheObject("vehicleSnap:" + sdRadarDetectData.getTunnelId() + ":" + sdRadarDetectData.getVehicleLicense(),map,5, TimeUnit.MINUTES);
//        }
    }

    public void controlDevice(SdRadarDetectData cacheObject){
        Integer distance = Integer.valueOf(cacheObject.getDistance());
        String state = "";
        long dataId = 0L;
        if(distance >= 0 && distance <= 300){
            state = "1";
            dataId = 29L;
        }else if(distance > 300 && distance < 600){
            state = "2";
            dataId = 29L;
        }else if(distance >= 600 && distance <= 814){
            state = "1";
            dataId = 30L;
        }else {
            state = "2";
            dataId = 30L;
        }
        if (!"GSY".equals(deploymentType)) {
            SdDeviceData data = new SdDeviceData();
            data.setId(dataId);
            data.setData(state);
            data.setUpdateTime(new Date());
            sdDeviceDataService.updateSdDeviceData(data);
        }
    }
}
