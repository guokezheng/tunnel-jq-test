package com.tunnel.business.service.platformAuthApi.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.datacenter.domain.enumeration.TopicEnum;
import com.tunnel.business.domain.dataInfo.*;
import com.tunnel.business.domain.platformApi.SdPlatformApi;
import com.tunnel.business.mapper.platformApi.SdPlatformApiMapper;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * @author zhai
 * @date 2022/10/25
 */
@Service
public class PlatformApiServiceImpl implements PlatformApiService {

    private static final Logger log = LoggerFactory.getLogger(PlatformApiServiceImpl.class);

    /**
     * 高速云设备管理接收地址
     */
    /*@Value("${tunnelDeviceBaseData}")
    private String tunnelDeviceBaseData;*/

    /**
     * 高速云隧道管理接收地址
     */
    /*@Value("${tunnelBaseData}")
    private String tunnelBaseData;*/

    @Autowired
    private ISdDevicesService iSdDevicesService;

    @Autowired
    private ISdTunnelsService iSdTunnelsService;

    @Autowired
    private SdPlatformApiMapper sdPlatformApiMapper;

    @Autowired
    @Qualifier("kafkaTwoTemplate")
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Async(value = "pushTaskExecutor")
    //@Transactional(isolation = Isolation.READ_COMMITTED)
    public void devicesPush(List<SdDevices> sdDevicesList, String pushType, String userName) {
        Map map = new HashMap();
        map.put("sdDevicesList", sdDevicesList);
        map.put("pushType", pushType);
        map.put("userName", userName);
        //将数据转为json字符串
        String objects = JSONObject.toJSONString(map);
        SdPlatformApi sdPlatformApi = new SdPlatformApi();
        //先查询推送历史记录是否有未推送成功的数据
        List<SdPlatformApi> sdPlatformApis = sdPlatformApiMapper.selectList();
        sdPlatformApi.setDataType("device");
        sdPlatformApi.setPushData(objects);
        sdPlatformApis.add(sdPlatformApi);
        if(!"edit".equals(pushType)){
            if("import".equals(pushType)){
                if(sdDevicesList.get(0).isUpdateSupport() == false){
                    sdPlatformApi.setCreateTime(sdDevicesList.get(0).getCreateTime());
                }else {
                    sdPlatformApi.setCreateTime(sdDevicesList.get(0).getUpdateTime());
                }
            }else {
                sdPlatformApi.setCreateTime(sdDevicesList.get(0).getCreateTime());
            }
        }else {
            sdPlatformApi.setCreateTime(sdDevicesList.get(0).getUpdateTime());
        }
        //通过kafka进行推送
        for(SdPlatformApi item : sdPlatformApis){
            SendResult<String, String> sendResult = kafkaSendData(item.getPushData(), "device");
            if(StringUtils.isNull(sendResult) && StringUtils.isNull(item.getId())){
                sdPlatformApiMapper.insertSdPushHistory(sdPlatformApi);
            }else if(StringUtils.isNotNull(sendResult) && StringUtils.isNotNull(item.getId())){
                SdPlatformApi sdPlatformApi1 = new SdPlatformApi();
                sdPlatformApi1.setId(item.getId());
                //0：未推送 1：已推送
                sdPlatformApi1.setPushStatus("1");
                sdPlatformApiMapper.updateSdPushHistory(sdPlatformApi1);
            }
        }
    }

    @Override
    @Async(value = "pushTaskExecutor")
    //@Transactional(isolation = Isolation.READ_COMMITTED)
    public void tunnelsPush(List<SdTunnels> sdTunnelsList, String pushType) {
        Map map = new HashMap();
        map.put("sdTunnelsList", sdTunnelsList);
        map.put("pushType", pushType);
        //将数据转为json字符串
        String objects = JSONObject.toJSONString(map);
        SdPlatformApi sdPlatformApi = new SdPlatformApi();
        //先查询推送历史记录是否有未推送成功的数据
        List<SdPlatformApi> sdPlatformApis = sdPlatformApiMapper.selectList();
        sdPlatformApi.setDataType("tunnel");
        sdPlatformApi.setPushData(objects);
        sdPlatformApis.add(sdPlatformApi);
        if(!"edit".equals(pushType)){
            sdPlatformApi.setCreateTime(sdTunnelsList.get(0).getCreateTime());
        }else {
            sdPlatformApi.setCreateTime(sdTunnelsList.get(0).getUpdateTime());
        }
        //通过kafka进行推送
        for(SdPlatformApi item : sdPlatformApis){
            SendResult<String, String> sendResult = kafkaSendData(item.getPushData(), "tunnel");
            if(StringUtils.isNull(sendResult) && StringUtils.isNull(item.getId())){
                sdPlatformApiMapper.insertSdPushHistory(sdPlatformApi);
            }else if(StringUtils.isNotNull(sendResult) && StringUtils.isNotNull(item.getId())){
                SdPlatformApi sdPlatformApi1 = new SdPlatformApi();
                sdPlatformApi1.setId(item.getId());
                //0：未推送 1：已推送
                sdPlatformApi1.setPushStatus("1");
                sdPlatformApiMapper.updateSdPushHistory(sdPlatformApi1);
            }
        }
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
            count = iSdDevicesService.deleteSdDevicesByIds((String[])sdDevices.getEqIds().toArray(new String[sdDevices.getEqIds().size()]));
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

    /**
     * 定时任务
     */
    //@Transactional(isolation = Isolation.READ_COMMITTED)
    public void pushTask(){
        //先查询推送历史记录是否有未推送成功的数据
        List<SdPlatformApi> sdPlatformApis = sdPlatformApiMapper.selectList();
        //通过kafka进行推送
        for(SdPlatformApi item : sdPlatformApis){
            SendResult<String, String> sendResult = kafkaSendData(item.getPushData(), item.getDataType());
            if(StringUtils.isNotNull(sendResult) && StringUtils.isNotNull(item.getId())){
                SdPlatformApi sdPlatformApi1 = new SdPlatformApi();
                sdPlatformApi1.setId(item.getId());
                //0：未推送 1：已推送
                sdPlatformApi1.setPushStatus("1");
                sdPlatformApiMapper.updateSdPushHistory(sdPlatformApi1);
            }
        }
    }

    /**
     * kafka推送数据
     *
     * @param objects 数据
     * @param dataType device tunnel
     * @return
     */
    public SendResult<String, String> kafkaSendData(String objects, String dataType){
        try {
            SendResult<String, String> sendResult = null;
            if("device".equals(dataType)){
                sendResult = kafkaTemplate.send(TopicEnum.TUNNEL_DEVICE_BASE_TOPIC.getCode(), objects).get();
            }else {
                sendResult = kafkaTemplate.send(TopicEnum.TUNNEL_BASE_TOPIC.getCode(), objects).get();
            }
            return sendResult;
        } catch (Exception e) {
            log.error("kafka推送失败");
            return null;
        }
    }
}
