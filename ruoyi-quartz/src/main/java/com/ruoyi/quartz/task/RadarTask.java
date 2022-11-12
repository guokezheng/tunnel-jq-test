package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.tunnel.business.domain.event.SdRadarDetectData;
import com.tunnel.business.mapper.digitalmodel.RadarEventMapper;
import com.tunnel.business.mapper.digitalmodel.SdRadarDetectDataMapper;
import com.zc.common.core.websocket.WebSocketService;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 小车跑数据
 *
 * @author zhai
 * @date 2022/11/9
 */
@Component("radarTask")
public class RadarTask {

    private static final Logger log = LoggerFactory.getLogger(RadarTask.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SdRadarDetectDataMapper sdRadarDetectDataMapper;

    @Autowired
    private RadarEventMapper radarEventMapper;

//    @Scheduled(fixedRate = 180000)
    public void radarTask1() {
        int count1 = 0;
        SdRadarDetectData sdRadarDetectData = new SdRadarDetectData();
        sdRadarDetectData.setVehicleId("772");
        List<SdRadarDetectData> radarDetectData = sdRadarDetectDataMapper.selectList(sdRadarDetectData);
        for (SdRadarDetectData item : radarDetectData) {
            redisCache.setCacheObject("radar:" + count1, item);
            count1++;
        }
        redisCache.setCacheObject("count", 0);
        System.out.println(",,,,,,,,,,,,,,,,,,");
    }

//    @Scheduled(fixedRate = 100)
    public void radarTask() {
        int count = redisCache.getCacheObject("count");
        SdRadarDetectData cacheObject = redisCache.getCacheObject("radar:" + count);
        redisCache.setCacheObject("count", count + 1);
        if (809 == count) {
            redisCache.setCacheObject("count", 0);
        }
        log.info("第" + count + "条   " + cacheObject);
        List<SdRadarDetectData> list = new ArrayList<>();
        list.add(cacheObject);
        JSONObject object = new JSONObject();
        object.put("radarDataList", list);
        WebSocketService.broadcast("radarDataList", object.toString());


    }

}
