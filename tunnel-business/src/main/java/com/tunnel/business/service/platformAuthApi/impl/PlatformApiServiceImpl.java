package com.tunnel.business.service.platformAuthApi.impl;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.business.domain.dataInfo.*;
import com.tunnel.business.service.dataInfo.*;
import com.tunnel.business.service.platformAuthApi.PlatformApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.RestTemplate;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.*;

/**
 * @author zhai
 * @date 2022/10/25
 */
@Service
public class PlatformApiServiceImpl implements PlatformApiService {

    private static final Logger log = LoggerFactory.getLogger(PlatformApiService.class);

    /**
     * 高速云设备管理接收地址
     */
    @Value("${tunnelDeviceBaseData}")
    private String tunnelDeviceBaseData;

    /**
     * 高速云隧道管理接收地址
     */
    @Value("${tunnelBaseData}")
    private String tunnelBaseDataTopic;

    @Autowired
    private ISdDevicesService iSdDevicesService;

    @Autowired
    private ISdTunnelsService iSdTunnelsService;

    @Autowired
    @Qualifier("kafkaTwoTemplate")
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Async(value = "pushTaskExecutor")
    public void devicesPush(List<SdDevices> sdDevicesList, String pushType, String userName) {
        Map map = new HashMap();
        map.put("sdDevicesList", sdDevicesList);
        map.put("pushType", pushType);
        map.put("userName", userName);
        //将数据转为json字符串
        String objects = JSONObject.toJSONString(map);
        //通过kafka进行推送
        kafkaTemplate.send(tunnelDeviceBaseData, objects);
        log.info("设备基础数据信息：" + objects);
    }

    @Override
    @Async(value = "pushTaskExecutor")
    public void tunnelsPush(List<SdTunnels> sdTunnelsList, String pushType) {
        Map map = new HashMap();
        map.put("sdTunnelsList", sdTunnelsList);
        map.put("pushType", pushType);
        //将数据转为json字符串
        String objects = JSONObject.toJSONString(map);
        //通过kafka进行推送
        kafkaTemplate.send(tunnelBaseDataTopic, objects);
        log.info("隧道基础数据信息：" + objects);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public int insertSdDevices(List<SdDevices> sdDevicesList) {
        int count = 0;
        for(SdDevices sdDevices : sdDevicesList){
            count = iSdDevicesService.insertSdDevices(sdDevices);
            if (sdDevices.getEqType() != 31L) {
                iSdDevicesService.insertOrUpdateOrDeleteSdDeviceCmd(sdDevices);
            }
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public int updateSdDevices(List<SdDevices> sdDevicesList) {
        int count = 0;
        for(SdDevices sdDevices : sdDevicesList){
            count = iSdDevicesService.updateSdDevices(sdDevices);
            if (sdDevices.getEqType() != 31L) {
                iSdDevicesService.insertOrUpdateOrDeleteSdDeviceCmd(sdDevices);
            }
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public int deleteSdDevices(List<SdDevices> sdDevicesList) {
        int count = 0;
        for(SdDevices sdDevices : sdDevicesList){
            count = iSdDevicesService.deleteSdDevicesByIds(sdDevices.getEqIds().toArray(new String[sdDevices.getEqIds().size()]));
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public int importSdDevices(List<SdDevices> sdDevicesList, String userName) {
        iSdDevicesService.importSdDevices(sdDevicesList,sdDevicesList.get(0).isUpdateSupport(),userName);
        return 1;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public int insertSdTunnels(List<SdTunnels> sdTunnelsList) {
        int count = 0;
        for(SdTunnels sdTunnels : sdTunnelsList){
            count = iSdTunnelsService.insertSdTunnels(sdTunnels);
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public int updateSdTunnels(List<SdTunnels> sdTunnelsList) {
        int count = 0;
        for(SdTunnels sdTunnels : sdTunnelsList){
            count = iSdTunnelsService.updateSdTunnels(sdTunnels);
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public int deleteSdTunnelsByIds(List<SdTunnels> sdTunnelsList) {
        int count = 0;
        for(SdTunnels sdTunnels : sdTunnelsList){
            count = iSdTunnelsService.deleteSdTunnelsByIds(sdTunnels.getTunnelIds().toArray(new String[sdTunnels.getTunnelIds().size()]));
        }
        return count;
    }
}
