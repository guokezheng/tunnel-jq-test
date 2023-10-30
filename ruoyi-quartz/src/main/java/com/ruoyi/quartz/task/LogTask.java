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
        int phoneCount = 0;
        int vmsCount = 0;
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
                phoneCount = operationLogMapper.getPhoneCount(tunnelId);
                vmsCount = boardReleaseLogMapper.getVmsCount(tunnelId);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("vmsNum",vmsCount);
                jsonObject.put("phoneNum",phoneCount);
                jsonObject.put("tunnelId",tunnelId);
                kafkaOneTemplate.send(TopicEnum.TUNNEL_RELEASE_NUM_TOPIC.getCode(),jsonObject.toString());
            }
        }
    }
}
