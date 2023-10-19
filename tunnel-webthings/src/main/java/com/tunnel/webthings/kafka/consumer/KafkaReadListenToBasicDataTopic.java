package com.tunnel.webthings.kafka.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.PlatformAuthEnum;
import com.tunnel.business.datacenter.domain.enumeration.TunnelEnum;
import com.tunnel.business.domain.event.SdLaneStatistics;
import com.tunnel.business.domain.event.SdRoadSectionStatistics;
import com.tunnel.business.mapper.event.SdLaneStatisticsMapper;
import com.tunnel.business.mapper.event.SdRoadSectionStatisticsMapper;
import com.tunnel.platform.controller.platformAuthApi.PlatformApiController;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 读取设备、隧道基础数据
 * 两级平台设备、隧道数据同步
 *
 * @author zhai
 * @date 2022/11/1
 */
@Component
public class KafkaReadListenToBasicDataTopic {

    private static final Logger log = LoggerFactory.getLogger(KafkaReadListenToBasicDataTopic.class);

    /**
     * 获取当前平台名称
     */
    @Value("${authorize.name}")
    private String authorizeName;

    @Autowired
    private PlatformApiController platformApiController;

    @Autowired
    private RedisCache redisCache;

    /**
     * 监听设备基础数据
     *
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"tunnelDeviceBaseData"}, containerFactory = "kafkaTwoContainerFactory")
    public void devicesAccept(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        if(PlatformAuthEnum.GLZ.getCode().equals(authorizeName)){
            String deviceData = "";
            if(StringUtils.isNotNull(record.value()) && StringUtils.isNotEmpty(record.value().toString())){
                deviceData = record.value().toString();
            }
            platformApiController.devicesAccept(deviceData);
        }
        consumer.commitSync();
    }

    /**
     * 监听设备基础数据
     *
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"tunnelBaseData"}, containerFactory = "kafkaTwoContainerFactory")
    public void tunnelsAccept(ConsumerRecord<String,Object> record, Consumer<?,?> consumer){
        if(PlatformAuthEnum.GLZ.getCode().equals(authorizeName)){
            String tunnelData = "";
            if(StringUtils.isNotNull(record.value()) && StringUtils.isNotEmpty(record.value().toString())){
                tunnelData = record.value().toString();
            }
            platformApiController.tunnelsAccept(tunnelData);
        }
        consumer.commitSync();
    }

    /**
     * 接收车道统计数据
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"laneStatistics"}, containerFactory = "kafkaTwoContainerFactory")
    public void laneStatistics(ConsumerRecord<String,Object> record, Consumer<?,?> consumer){
        if(PlatformAuthEnum.GSY.getCode().equals(authorizeName)){
            if(StringUtils.isNotNull(record.value()) && StringUtils.isNotEmpty(record.value().toString())){
                SdLaneStatistics sdLaneStatistics = JSONObject.parseObject(record.value().toString(), SdLaneStatistics.class);
                SdLaneStatisticsMapper bean = SpringUtils.getBean(SdLaneStatisticsMapper.class);
                bean.insertSdLaneStatistics(sdLaneStatistics);
            }
        }
        consumer.commitSync();
    }


    /**
     * 隧道统计数据
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"tunnelStatistics"}, containerFactory = "kafkaTwoContainerFactory")
    public void tunnelStatistics(ConsumerRecord<String,Object> record, Consumer<?,?> consumer){
        if(PlatformAuthEnum.GSY.getCode().equals(authorizeName)){
            if(StringUtils.isNotNull(record.value()) && StringUtils.isNotEmpty(record.value().toString())){
                SdRoadSectionStatistics roadSectionStatistics = JSONObject.parseObject(record.value().toString(), SdRoadSectionStatistics.class);
                SdRoadSectionStatisticsMapper bean = SpringUtils.getBean(SdRoadSectionStatisticsMapper.class);
                bean.insertSdRoadSectionStatistics(roadSectionStatistics);
            }
        }
        consumer.commitSync();
    }

    /**
     * 万集车辆数据
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    @KafkaListener(topics = {"tunnelRadarData"}, containerFactory = "kafkaTwoContainerFactory")
    public void tunnelRadarData(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        if(PlatformAuthEnum.GSY.getCode().equals(authorizeName)){
            String carData = "";
            if(StringUtils.isNotNull(record.value()) && StringUtils.isNotEmpty(record.value().toString())){
                carData = record.value().toString();
            }
            setRadarData(carData);
        }
        consumer.commitSync();
    }

    public void setRadarData(String carData){
        Map<String, Object> map = (Map<String, Object>)JSON.parse(carData);
        if(TunnelEnum.HANG_SHAN_DONG.getCode().equals(map.get("tunnelId").toString())){
            return;
        }
        redisCache.setCacheObject("vehicleSnap:" + map.get("tunnelId").toString() + ":" + map.get("roadDir") + ":" + map.get("vehicleLicense"),map,1, TimeUnit.HOURS);
    }
}
