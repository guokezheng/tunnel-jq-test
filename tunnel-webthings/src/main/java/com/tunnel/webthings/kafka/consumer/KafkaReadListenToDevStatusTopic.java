package com.tunnel.webthings.kafka.consumer;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceDataRecord;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.service.dataInfo.ISdDeviceDataRecordService;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.webthings.service.TunnelIotDeviceService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

/**
 * 读取DevStatus主题内容
 */
@Component
public class KafkaReadListenToDevStatusTopic {

    private static final Logger log = LoggerFactory.getLogger(KafkaReadListenToDevStatusTopic.class);

    @Autowired
    private TunnelIotDeviceService service;

    @Value("${authorize.name}")
    private String authorizeName;

    @Autowired
    private ISdDeviceDataService sdDeviceDataService;

    @Autowired
    private ISdDeviceDataRecordService sdDeviceDataRecordService;

    @Autowired
    private ISdDevicesService sdDevicesService;

    /**
     * 读取设备状态数据
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    @KafkaListener(topics = {"wq_devStatusTopic"}, containerFactory = "kafkaTwoContainerFactory")
    public void devStatusData(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("{}", record.value());
        if (authorizeName != null && !authorizeName.equals("") && authorizeName.equals("GSY")) {
            if (record.value() != null || !record.value().toString().equals("")) {
                System.out.println(record.value());
                JSONObject jsonObject = JSONObject.parseObject(record.value().toString());
                if (jsonObject.get("deviceData") != null) {
                    Object o = jsonObject.get("deviceData");
                    SdDeviceData sdDeviceData = JSONUtil.toBean(o.toString(), SdDeviceData.class);
                    SdDeviceData deviceData = sdDeviceDataService.selectSdDeviceDataById(sdDeviceData.getId());
                    if (deviceData != null) {
                        sdDeviceData.setUpdateTime(new Date());
                        sdDeviceDataService.updateSdDeviceData(sdDeviceData);
                    } else {
                        sdDeviceDataService.insertSdDeviceData(sdDeviceData);
                    }
                } else if (jsonObject.get("deviceDataRecord") != null) {
                    Object o = jsonObject.get("deviceDataRecord");
                    SdDeviceDataRecord sdDeviceDataRecord = JSONUtil.toBean(o.toString(), SdDeviceDataRecord.class);
                    SdDeviceDataRecord deviceDataRecord = sdDeviceDataRecordService.selectSdDeviceDataRecordById(sdDeviceDataRecord.getId());
                    if (deviceDataRecord != null) {
                        sdDeviceDataRecordService.updateSdDeviceDataRecord(sdDeviceDataRecord);
                    } else {
                        sdDeviceDataRecordService.insertSdDeviceDataRecord(sdDeviceDataRecord);
                    }
                } else if (jsonObject.get("deviceStatus") != null) {
                    Object o = jsonObject.get("deviceStatus");
                    SdDevices sdDevices = JSONUtil.toBean(o.toString(), SdDevices.class);
                    SdDevices devices = sdDevicesService.selectSdDevicesById(sdDevices.getEqId());
                    if (devices != null) {
                        sdDevices.setUpdateTime(new Date());
                        sdDevicesService.updateSdDevices(sdDevices);
                    } else {
                        sdDevicesService.insertSdDevices(sdDevices);
                    }
                }
            }
        }
        consumer.commitSync();
    }

    /**
     * 图片存储路径修改数据库
     */
    @PostMapping("/aaa")
    public void method(){
        service.method();
    }
}
