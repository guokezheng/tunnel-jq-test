package com.ruoyi.quartz.task;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.datacenter.domain.enumeration.TopicEnum;
import com.tunnel.business.datacenter.domain.enumeration.TunnelEnum;
import com.tunnel.business.datacenter.domain.enumeration.WjCarVolumeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.digitalmodel.SdRadarDevice;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.service.digitalmodel.impl.RadarEventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备数据同步
 *
 * @author zhai
 * @date 2023/10/30
 */
@Component("devDataTask")
public class DevDataTask {

    @Autowired
    private SdDeviceDataMapper sdDeviceDataMapper;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Autowired
    private RadarEventServiceImpl radarEventServiceImpl;

    @Autowired
    @Qualifier("kafkaTwoTemplate")
    private KafkaTemplate<String, String> kafkaTwoTemplate;

    /**
     * 线程池
     */
    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Value("${wj_carVolume}")
    private String glzName;

    public void sentDevData(){
        List<String> list = new ArrayList<>();
        if("wzb".equals(glzName)){
            list.add(TunnelEnum.HU_SHAN.getCode());
        }else if("th".equals(glzName)){
            list.add(TunnelEnum.QING_FENG_LING.getCode());
            list.add(TunnelEnum.PAN_DING_SHAN.getCode());
        }else if("mz".equals(glzName)){
            list.add(TunnelEnum.TAI_HE_SHAN.getCode());
            list.add(TunnelEnum.TIAN_CI_SHAN.getCode());
        }else if("jly".equals(glzName)){
            list.add(TunnelEnum.JIN_JIA_LOU.getCode());
            list.add(TunnelEnum.MA_AN_SHAN.getCode());
        }else if("yts".equals(glzName)){
            list.add(TunnelEnum.SHUANG_ZI_SHAN.getCode());
            list.add(TunnelEnum.YANG_TIAN_SHAN.getCode());
        }
        for(String tunnelId : list){
            List<Map<String, Object>> devRealData = sdDeviceDataMapper.getDevRealData(tunnelId);
            devRealData.stream().forEach(item -> {
                threadPoolTaskExecutor.execute(()->{
                    sentWlData(item);
                });
            });
        }
    }

    /**
     * 重新定义参数名
     *
     * @param deviceId
     * @param deviceData
     * @param deviceItemId
     * @return
     */
    public JSONObject definitionParam(String deviceId, String deviceData, Long deviceItemId){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceId",deviceId);
        jsonObject.put("deviceData",deviceData);
        jsonObject.put("deviceItemId",deviceItemId);
        return jsonObject;
    }

    /**
     * 实时设备运行状态上传高速云共通方法
     *
     * @param eqType
     * @param object
     * @return
     */
    public JSONObject devReaStatus(Long eqType, JSONObject object){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("devNo", "S00063700001980001");
        jsonObject.put("devType",eqType);
        jsonObject.put("loginTime", DateUtils.getNowDate());
        jsonObject.put("devStatus","1");
        jsonObject.put("netstatus","1");
        jsonObject.put("source","suidao");
        jsonObject.put("timeStamp", DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        JSONObject obj = new JSONObject();
        obj.put("deviceData",object);
        jsonObject.put("expands",obj);
        return jsonObject;
    }

    /**
     * 推送物联
     */
    public void sentWlData(Map<String, Object> item){
        try {
            JSONObject definitionObject = definitionParam(item.get("deviceId").toString(), item.get("deviceData").toString(), Long.valueOf(item.get("devicesItemId").toString()));
            JSONObject jsonObject = devReaStatus(Long.valueOf(item.get("eqType").toString()), definitionObject);
            kafkaTwoTemplate.send(TopicEnum.DEV_STATUS_TOPIC.getCode(),jsonObject.toString());
        }catch (Exception e){
            e.getMessage();
        }
    }
}
