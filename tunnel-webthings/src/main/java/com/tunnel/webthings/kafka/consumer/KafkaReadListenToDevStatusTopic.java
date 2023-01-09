package com.tunnel.webthings.kafka.consumer;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceDataRecord;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceDataRecordService;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * 读取DevStatus主题内容
 * 物联中台设备状态
 */
@Component
public class KafkaReadListenToDevStatusTopic {

    private static final Logger log = LoggerFactory.getLogger(KafkaReadListenToDevStatusTopic.class);


    @Value("${authorize.name}")
    private String authorizeName;

    @Autowired
    private ISdDeviceDataService sdDeviceDataService;

    @Autowired
    private ISdDeviceDataRecordService sdDeviceDataRecordService;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private SdDeviceDataMapper sdDeviceDataMapper;

    /**
     * 读取设备状态数据
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    @KafkaListener(topics = {"wq_devStatusTopic"}, containerFactory = "kafkaTwoContainerFactory")
    public void devStatusData(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("高速云端监听到平台数据： --> {}",record.value());
        if (authorizeName != null && !authorizeName.equals("") && authorizeName.equals("GSY")) {
            if (record.value() != null || !record.value().toString().equals("")) {
                System.out.println(record.value());
                JSONObject jsonObject = JSONObject.parseObject(record.value().toString());
                if(jsonObject.get("expands") != null && jsonObject.get("expands") != ""){
                    //解析设备运行数据信息记录
                    Object deviceDataRecordObject = jsonObject.get("deviceDataRecord");
                    //解析设备运行数据信息
                    Object deviceDataObject = jsonObject.get("deviceData");
                    //解析设备状态数据信息
                    Object deviceStatusObject = jsonObject.get("deviceStatus");
                    //设备运行数据信息记录操作
                    if(deviceStatusObject != null){
                        //Object o = jsonObject.get("deviceDataRecord");
                        SdDeviceDataRecord sdDeviceDataRecord = JSONUtil.toBean(deviceDataRecordObject.toString(), SdDeviceDataRecord.class);
                        SdDeviceDataRecord deviceDataRecord = sdDeviceDataRecordService.selectSdDeviceDataRecordById(sdDeviceDataRecord.getId());
                        if (deviceDataRecord != null) {
                            sdDeviceDataRecordService.updateSdDeviceDataRecord(sdDeviceDataRecord);
                        } else {
                            sdDeviceDataRecordService.insertSdDeviceDataRecord(sdDeviceDataRecord);
                        }
                    }
                    //设备运行数据信息操作
                    if(deviceDataObject != null){
                        JSONObject object = JSONObject.parseObject(deviceDataObject.toString());
                        String deviceId = object.getString("deviceId");
                        String deviceData = object.getString("deviceData");
                        Long deviceItemId = object.getLong("deviceItemId");
                        SdDeviceData sdDeviceData = new SdDeviceData();
                        sdDeviceData.setItemId(deviceItemId);
                        List<SdDeviceData> sdDeviceDataList = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
                        List<SdDeviceData> collect = sdDeviceDataList.stream().filter(item -> item.getDeviceId().equals(deviceId) && item.getItemId().toString().equals(deviceItemId.toString())).collect(Collectors.toList());
                        if (collect.size() > 0) {
                            SdDeviceData data = setDeviceData(deviceId, deviceData, deviceItemId);
                            data.setUpdateTime(DateUtils.getNowDate());
                            sdDeviceDataMapper.updateKafkaDeviceData(data);
                        } else {
                            SdDeviceData data = setDeviceData(deviceId, deviceData, deviceItemId);
                            data.setCreateTime(DateUtils.getNowDate());
                            sdDeviceDataMapper.insertSdDeviceData(data);
                        }
                    }
                    //设备状态数据信息操作
                    if(deviceStatusObject != null){
                        SdDevices sdDevices = JSONUtil.toBean(deviceStatusObject.toString(), SdDevices.class);
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
        }
        consumer.commitSync();
    }

    /**
     * 赋值设备实时数据
     * @param deviceId
     * @param runStatus
     * @param itemId
     * @return
     */
    public SdDeviceData setDeviceData(String deviceId,String runStatus,Long itemId){
        SdDeviceData deviceData = new SdDeviceData();
        deviceData.setDeviceId(deviceId);
        deviceData.setItemId(itemId);
        deviceData.setData(runStatus);
        return deviceData;
    }
}
