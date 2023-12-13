package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.business.datacenter.domain.enumeration.TopicEnum;
import com.tunnel.business.datacenter.domain.enumeration.TunnelEnum;
import com.tunnel.business.datacenter.domain.enumeration.WjCarVolumeEnum;
import com.tunnel.business.mapper.informationBoard.IotBoardReleaseLogMapper;
import com.tunnel.business.mapper.logRecord.SdOperationLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhai
 * @date 2023/10/26
 */
@Component("logTask")
public class LogTask {

    private static final Logger log = LoggerFactory.getLogger(LogTask.class);

    @Autowired
    private SdOperationLogMapper operationLogMapper;

    @Autowired
    private IotBoardReleaseLogMapper boardReleaseLogMapper;

    @Value("${wj_carVolume}")
    private String glzName;

    @Autowired
    @Qualifier("kafkaOneTemplate")
    private KafkaTemplate<String, String> kafkaOneTemplate;

    //@Scheduled(fixedRate = 1000)
    public void getVmsOrPhone(){
        List<Map<String, Object>> phoneCount = new ArrayList<>();
        List<Map<String, Object>> vmsCount = new ArrayList<>();
        List<String> list = new ArrayList<>();
        if("wzb".equals(glzName)){
            list.add(TunnelEnum.HU_SHAN.getCode());
        }else if("th".equals(glzName)){
            list.add(TunnelEnum.PAN_DING_SHAN.getCode());
            list.add(TunnelEnum.QING_FENG_LING.getCode());
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
        if(list.size() > 0){
            for(String tunnelId : list){
                List<Map<String, Object>> phoneCount1 = operationLogMapper.getPhoneCount(tunnelId);
                List<Map<String, Object>> vmsCount1 = boardReleaseLogMapper.getVmsCount(tunnelId);
                if(phoneCount1.size() > 0){
                    phoneCount = phoneCount1;
                }else {
                    //笨办法应急用
                    Map<String, Object> mapp1 = new HashMap<>();
                    Map<String, Object> mapp2 = new HashMap<>();
                    mapp1.put("phoneNum",0);
                    mapp1.put("direction","1");
                    mapp2.put("phoneNum",0);
                    mapp2.put("direction","2");
                    phoneCount.add(mapp1);
                    phoneCount.add(mapp2);
                }
                if(vmsCount1.size() > 0){
                    vmsCount = vmsCount1;
                }else {
                    //笨办法应急用
                    Map<String, Object> mapv1 = new HashMap<>();
                    Map<String, Object> mapv2 = new HashMap<>();
                    mapv1.put("vmsNum",0);
                    mapv1.put("direction","1");
                    mapv2.put("vmsNum",0);
                    mapv2.put("direction","2");
                    vmsCount.add(mapv1);
                    vmsCount.add(mapv2);
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("vmsNum",vmsCount);
                jsonObject.put("phoneNum",phoneCount);
                jsonObject.put("tunnelId",tunnelId);
                kafkaOneTemplate.send(TopicEnum.TUNNEL_RELEASE_NUM_TOPIC.getCode(),jsonObject.toString());
            }
        }
    }
}
