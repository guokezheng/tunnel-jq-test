package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.event.SdRadarDetectData;
import com.tunnel.business.mapper.digitalmodel.RadarEventMapper;
import com.tunnel.business.mapper.digitalmodel.SdRadarDetectDataTemporaryMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.zc.common.core.websocket.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 定时清理车辆快照数据临时表
 *
 * @author zhai
 * @date 2023/02/14
 */
@Component("radarTemporaryTask")
public class RadarTemporaryTask {

    private static final Logger log = LoggerFactory.getLogger(RadarTemporaryTask.class);

    /**
     * 定时清空临时数据
     */
    public void clearData(){
        SdRadarDetectDataTemporaryMapper mapper = SpringUtils.getBean(SdRadarDetectDataTemporaryMapper.class);
        mapper.deleteData();
    }

    /**
     * 定时小车数据表
     */
    public void catClearData(){
        SdRadarDetectDataTemporaryMapper mapper = SpringUtils.getBean(SdRadarDetectDataTemporaryMapper.class);
        int countDevicesNum = mapper.countDevices();
        //计算循环次数
        int forNum = countDevicesNum/10000;
        for(int i =0 ; i<forNum ; i++){
            mapper.deleteCatData();
        }

    }
}
