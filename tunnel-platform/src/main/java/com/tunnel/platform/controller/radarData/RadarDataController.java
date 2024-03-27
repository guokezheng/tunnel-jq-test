package com.tunnel.platform.controller.radarData;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.quartz.task.RadarTask;
import com.tunnel.business.domain.event.SdRadarDetectData;
import com.tunnel.business.mapper.digitalmodel.SdRadarDetectDataMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhai
 * @date 2022/11/22
 */
@RestController
@RequestMapping("/radarData")
@Api(tags = "感知数据初始化Controller")
@ApiSupport(order = 16)
public class RadarDataController {

    private static final Logger log = LoggerFactory.getLogger(RadarDataController.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SdRadarDetectDataMapper sdRadarDetectDataMapper;

    /**
     * 感知数据初始化
     */
    @PostMapping("/radarDataInit")
    @ApiOperation("感知数据初始化")
    public void radarDataInit(){
        int count1 = 0;
        SdRadarDetectData sdRadarDetectData = new SdRadarDetectData();
        sdRadarDetectData.setVehicleId("772");
        List<SdRadarDetectData> radarDetectData = sdRadarDetectDataMapper.selectList(sdRadarDetectData);
        for (SdRadarDetectData item : radarDetectData) {
            redisCache.setCacheObject("radar:" + count1, item);
            count1++;
        }
        redisCache.setCacheObject("count", 0);
        log.info("感知数据初始化完成");
    }
}
