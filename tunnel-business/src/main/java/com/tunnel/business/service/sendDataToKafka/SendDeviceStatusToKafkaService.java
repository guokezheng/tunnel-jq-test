package com.tunnel.business.service.sendDataToKafka;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceDataRecord;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SendDeviceStatusToKafkaService {

    private static final Logger log = LoggerFactory.getLogger(SendDeviceStatusToKafkaService.class);

    /**
     * 时间戳格式
     */
    private static final String sdf_pattern = "yyyy-MM-dd HH:mm:ss.SSS";

    @Autowired
    @Qualifier("kafkaTwoTemplate")
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${devStatusTopic}")
    private String devStatusTopic;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Value("${authorize.name}")
    private String authorizeName;

    @Autowired
    private ISdDevicesService sdDevicesService;

    public void pushDevicesDataNowTime(SdDeviceData data) {
        if (authorizeName != null && !authorizeName.equals("") && authorizeName.equals("GLZ")) {
            com.alibaba.fastjson.JSONObject jsonObject = new JSONObject();
            jsonObject.put("devNo", "S00063700001980001");
            jsonObject.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern));
            jsonObject.put("deviceData", data);
            kafkaTemplate.send(devStatusTopic, jsonObject.toString());
            log.info("推送物联中台kafka内容：" + jsonObject);
        }
    }

    public void pushDevicesDataRecord(SdDeviceDataRecord data) {
        if (authorizeName != null && !authorizeName.equals("") && authorizeName.equals("GLZ")) {
            com.alibaba.fastjson.JSONObject jsonObject = new JSONObject();
            jsonObject.put("devNo", "S00063700001980001");
            jsonObject.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern));
            jsonObject.put("deviceDataRecord", data);
            kafkaTemplate.send(devStatusTopic, jsonObject.toString());
            log.info("推送物联中台kafka内容：" + jsonObject);
        }
    }

    public void pushDevicesStatusToOtherSystem(SdDevices sdDevices, String role, String status) {
        if (authorizeName != null && !authorizeName.equals("") && authorizeName.equals("GLZ")) {
            com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
            jsonObject.put("devNo", "S00063700001980001");
            jsonObject.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern));
            if (role.equals("1")) {
                SdDevices devices = sdDevicesService.selectSdDevicesById(sdDevices.getEqId());
                if (status.equals("off")) {
                    devices.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
                } else if (status.equals("on")) {
                    devices.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
                }
                jsonObject.put("deviceStatus", devices);
                kafkaTemplate.send(devStatusTopic, jsonObject.toString());
            } else if (role.equals("2")) {
                List<SdDevices> devicesList = sdDevicesMapper.selectFireComponentsList(sdDevices);
                for (int i = 0;i < devicesList.size();i++) {
                    SdDevices dev = devicesList.get(i);
                    if (status.equals("off")) {
                        dev.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
                    } else if (status.equals("on")) {
                        dev.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
                    }
                    jsonObject.put("deviceStatus", dev);
                    kafkaTemplate.send(devStatusTopic, jsonObject.toString());
                }
            }
        }
    }
}
