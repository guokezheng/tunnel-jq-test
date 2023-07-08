package com.tunnel.business.service.sendDataToKafka;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.datacenter.domain.enumeration.TopicEnum;
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

    /*@Value("${devStatusTopic}")
    private String devStatusTopic;*/

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Value("${authorize.name}")
    private String authorizeName;

    @Autowired
    private ISdDevicesService sdDevicesService;

    public void pushDevicesDataNowTime(SdDeviceData data) {
        if (authorizeName != null && !authorizeName.equals("") && authorizeName.equals("GLZ")) {
            /*com.alibaba.fastjson.JSONObject jsonObject = new JSONObject();
            jsonObject.put("devNo", "S00063700001980001");
            jsonObject.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern));
            jsonObject.put("deviceData", data);*/
            //参数名定义
            JSONObject jsonObjectParam = definitionParam(data.getDeviceId(), data.getData(), data.getItemId());
            JSONObject jsonObject = devReaStatus(jsonObjectParam);
            kafkaTemplate.send(TopicEnum.DEV_STATUS_TOPIC.getCode(), jsonObject.toString());
            log.info("推送物联中台kafka内容：" + jsonObject);
        }
    }

    public void pushDevicesDataRecord(SdDeviceDataRecord data) {
        if (authorizeName != null && !authorizeName.equals("") && authorizeName.equals("GLZ")) {
            /*com.alibaba.fastjson.JSONObject jsonObject = new JSONObject();
            jsonObject.put("devNo", "S00063700001980001");
            jsonObject.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern));
            jsonObject.put("deviceDataRecord", data);*/
            JSONObject jsonObject = devRecord(data);
            kafkaTemplate.send(TopicEnum.DEV_STATUS_TOPIC.getCode(), jsonObject.toString());
            log.info("推送物联中台kafka内容：" + jsonObject);
        }
    }

    public void pushDevicesStatusToOtherSystem(SdDevices sdDevices, String role, String status) {
        if (authorizeName != null && !authorizeName.equals("") && authorizeName.equals("GLZ")) {
            if (role.equals("1")) {
                SdDevices devices = sdDevicesService.selectSdDevicesById(sdDevices.getEqId());
                if (status.equals("off")) {
                    devices.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
                } else if (status.equals("on")) {
                    devices.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
                }
                JSONObject jsonObject = devStatus(devices);
                kafkaTemplate.send(TopicEnum.DEV_STATUS_TOPIC.getCode(), jsonObject.toString());
            } else if (role.equals("2")) {
                List<SdDevices> devicesList = sdDevicesMapper.selectFireComponentsList(sdDevices);
                for (int i = 0;i < devicesList.size();i++) {
                    SdDevices dev = devicesList.get(i);
                    if (status.equals("off")) {
                        dev.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
                    } else if (status.equals("on")) {
                        dev.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
                    }
                    JSONObject jsonObject = devStatus(dev);
                    kafkaTemplate.send(TopicEnum.DEV_STATUS_TOPIC.getCode(), jsonObject.toString());
                }
            }
        }
    }

    /**
     * 实时设备运行数据信息记录上传高速云共通方法
     *
     * @param data
     * @return
     */
    public JSONObject devRecord(SdDeviceDataRecord data){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("devNo", "SDEV00063700001980003");
        jsonObject.put("devType","");
        jsonObject.put("loginTime", DateUtils.getNowDate());
        jsonObject.put("devStatus","1");
        jsonObject.put("netstatus","1");
        jsonObject.put("source","suidao");
        jsonObject.put("timeStamp", DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        JSONObject obj = new JSONObject();
        obj.put("deviceDataRecord",data);
        jsonObject.put("expands",obj);
        return jsonObject;
    }

    /**
     * 重置参数名
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
     * @param object
     * @return
     */
    public JSONObject devReaStatus(JSONObject object){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("devNo", "SDEV00063700001980003");
        jsonObject.put("devType", "");
        jsonObject.put("loginTime",DateUtils.getNowDate());
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
     * 实时设备状态上传高速云共通方法
     *
     * @param sdDevices
     * @return
     */
    public JSONObject devStatus(SdDevices sdDevices){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("devNo", "S00063700001980001");
        jsonObject.put("devType",sdDevices.getEqType());
        jsonObject.put("loginTime",DateUtils.getNowDate());
        jsonObject.put("devStatus",sdDevices.getEqStatus());
        jsonObject.put("source","suidao");
        if("1".equals(sdDevices.getEqStatus())){
            jsonObject.put("netstatus",sdDevices.getEqStatus());
            jsonObject.put("netStatusRemark","连通");
            jsonObject.put("devStatusRemark","正常");
        }else if("2".equals(sdDevices.getEqStatus())){
            jsonObject.put("netstatus",sdDevices.getEqStatus());
            jsonObject.put("netStatusRemark","离线");
            jsonObject.put("devStatusRemark","离线");
        }else if("3".equals(sdDevices.getEqStatus())){
            jsonObject.put("devStatusRemark","故障");
        }else {
            jsonObject.put("netstatus","1");
            jsonObject.put("netStatusRemark","连通");
        }
        jsonObject.put("timeStamp", DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        JSONObject obj = new JSONObject();
        obj.put("deviceStatus",sdDevices);
        jsonObject.put("expands",obj);
        return jsonObject;
    }
}
