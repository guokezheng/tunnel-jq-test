package com.ruoyi.quartz.task;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.TunnelEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.event.SdRadarDetectData;
import com.tunnel.business.domain.event.SdTrafficVolume;
import com.tunnel.business.mapper.digitalmodel.RadarEventMapper;
import com.tunnel.business.mapper.digitalmodel.SdRadarDetectDataTemporaryMapper;
import com.tunnel.business.mapper.digitalmodel.SdTrafficVolumeMapper;
import com.tunnel.business.mapper.event.SdLaneStatisticsMapper;
import com.tunnel.business.mapper.vehicle.SdVehicleDataMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.zc.common.core.websocket.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 定时清理车辆快照数据临时表
 *
 * @author zhai
 * @date 2023/02/14
 */
@Component("radarTemporaryTask")
public class RadarTemporaryTask {

    private static final Logger log = LoggerFactory.getLogger(RadarTemporaryTask.class);

    @Autowired
    private SdTrafficVolumeMapper volumeMapper;

    @Autowired
    private SdVehicleDataMapper sdVehicleDataMapper;

    /**
     * 定时清空临时数据
     */
    public void clearData(){
        SdRadarDetectDataTemporaryMapper mapper = SpringUtils.getBean(SdRadarDetectDataTemporaryMapper.class);
        mapper.deleteData();
    }

    /**
     * 定时清空小车数据表
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

    /**
     * 清除两天前的车道统计数据
     */
    public void clearLaneData(){
        SdLaneStatisticsMapper statisticsMapper = SpringUtils.getBean(SdLaneStatisticsMapper.class);
        statisticsMapper.clearLaneData();
    }

    /**
     * 情况当天车辆数据
     */
    public void clearCarData(){
        SdRadarDetectDataTemporaryMapper mapper = SpringUtils.getBean(SdRadarDetectDataTemporaryMapper.class);
        mapper.clearCarData();
    }

    /**
     * 行山东车辆数据汇总
     */
    public void catSummary(){
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 格式化当前时间
        String formattedStart = now.format(formatter);
        // 加上5分钟
        LocalDateTime newTime = now.minusMinutes(5);
        // 定义日期时间格式
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 格式化新时间
        String formattedNewEnd = newTime.format(formatterEnd);
        Map<Object, Object> param = new HashMap<>();
        param.put("startDate",formattedNewEnd);
        param.put("endDate",formattedStart);
        param.put("tunnelId", TunnelEnum.HANG_SHAN_DONG.getCode());
        param.put("direction",1);
        List<Map<String, Object>> vehicleListsByDate = sdVehicleDataMapper.getVehicleListsByDate(param);
        param.put("direction",2);
        List<Map<String, Object>> vehicleListsByDate1 = sdVehicleDataMapper.getVehicleListsByDate(param);
        SdTrafficVolume sdTrafficVolume = new SdTrafficVolume();
        sdTrafficVolume.setTunnelId(TunnelEnum.HANG_SHAN_DONG.getCode());
        sdTrafficVolume.setDirection("1");
        sdTrafficVolume.setCarNumber(vehicleListsByDate.get(0).get("num").toString());

        volumeMapper.insertSdTrafficVolume(sdTrafficVolume);
        sdTrafficVolume.setDirection("2");
        sdTrafficVolume.setCarNumber(vehicleListsByDate1.get(0).get("num").toString());

        volumeMapper.insertSdTrafficVolume(sdTrafficVolume);
    }
}
