package com.tunnel.webthings.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.datacenter.domain.enumeration.TopicEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdStateStorage;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDeviceTypeItemMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.webthings.domain.RadarMsgTopic;
import com.tunnel.webthings.enums.MsgType;
import com.tunnel.webthings.service.SendMsgService;
import com.tunnel.webthings.vo.RadarMsgTopicVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SendMsgServiceImpl implements SendMsgService {

    private static final Logger log = LoggerFactory.getLogger(SendMsgService.class);

    /**
     * 时间戳格式
     */
    private static final String sdf_pattern = "yyyy-MM-dd HH:mm:ss.SSS";

//    @Value("${iot.url}")
//    private String url;


    @Autowired
    @Qualifier("OkHttpRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("kafkaTwoTemplate")
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private SdEventMapper sdEventMapper;

    /*@Value("${devStatusTopic}")
    private String devStatusTopic;*/

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private SdDevicesMapper devicesMapper;
    @Autowired
    @Qualifier("kafkaOneTemplate")
    private KafkaTemplate<String, String> kafkaOneTemplate;

    @Autowired
    private SdDeviceDataMapper sdDeviceDataMapper;

    @Autowired
    private SdDeviceTypeItemMapper sdDeviceTypeItemMapper;


//    /**
//     * 发送指令数据
//     */
//    @Override
//    public String sendDirect(String devNo,String devType) {
//        return null;
//    }

//    @Override
//    public AjaxResult sendEvent() {
//        JSONObject jsonObject = new JSONObject();
//        SdEvent sdEvent = sdEventMapper.selectSdEventById(845674434130L);
//        sdEvent.setUpdateTime(new Date());
//        jsonObject.put("event", sdEvent);
//        jsonObject.put("devNo", "S00063700001980001");
//        jsonObject.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern));
//        kafkaTemplate.send("wq_tunnelEvent", jsonObject.toString());
//        return AjaxResult.success("1");
//    }

//    @Override
//    public AjaxResult devicestatus(String devId) {
//        SdDevices sdDevices = devicesMapper.selectSdDevicesById(devId);
//        sdDevices.setEqStatus("1");
//        sdDevices.setEqStatusTime(new Date());
//        sdDevices.setUpdateTime(new Date());
//        JSONObject jsonObject = devStatus(sdDevices);
//        kafkaTemplate.send("wq_devStatusTopic", jsonObject.toString());
//        return AjaxResult.success("1");
//    }

//    @Override
//    public AjaxResult devicesdata(String devId,String state) {
//        SdDeviceData sdDeviceData = new SdDeviceData();
//        sdDeviceData.setDeviceId(devId);
//        List<SdDeviceData> data = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
//        if (data.size() > 0) {
//            for (int i = 0;i < data.size();i++) {
//                SdDeviceData deviceData = data.get(i);
//                deviceData.setUpdateTime(new Date());
//                deviceData.setData(state);
//                JSONObject jsonObject = definitionParam(deviceData.getDeviceId(), deviceData.getData(), deviceData.getItemId());
//                JSONObject object = devReaStatus(sdDeviceTypeItemMapper.selectSdDeviceTypeItemById(deviceData.getItemId()).getDeviceTypeId(), jsonObject);
//                kafkaTemplate.send("wq_devStatusTopic", object.toString());
//            }
//        }
//        return AjaxResult.success("1");
//    }

//    @Override
//    public String sendDevStatus(RadarMsgTopicVo vo) {
//        //设备编号
//        String devNo = vo.getDevNo();
//        //设备类型
//        String devType = vo.getDevType();
//        //时间戳
//        String format = DateUtil.format(DateUtil.date(), sdf_pattern);
//        //雷达消息数据
//        Map<String, Object> map = new HashMap<>();
//        map.put("devNo",devNo);
//        map.put("devType",devType);
//        map.put("directType","RadarMsgTopic");
//        map.put("directTypeDesc","雷达消息数据"+RandomUtil.randomString(4));
//        map.put("random", RandomUtil.randomString(20)); //随机写入
//        map.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern)); //时间戳，必填
//        //自定义字段
//        RadarMsgTopic radarMsgTopic =new RadarMsgTopic();
//        radarMsgTopic.setFuseId("这是融合ID");
//        radarMsgTopic.setId(MsgType.msg31.getCode()+devNo+DateUtil.date()+"车道号"+RandomUtil.randomString(4));
//        radarMsgTopic.setPtcType(1);
//        radarMsgTopic.setLane(2);
//        radarMsgTopic.setDirect(Integer.parseInt(MsgType.direct1.getCode()));
//        radarMsgTopic.setVehL(3.45);
//        radarMsgTopic.setVehW(1.62);
//        radarMsgTopic.setVehH(1.25);
//        radarMsgTopic.setVehPlate("鲁A88888");
//        radarMsgTopic.setVehPlateColor("03");
//        radarMsgTopic.setVehColor("6");
//        radarMsgTopic.setVehBrand("五菱宏光");
//        radarMsgTopic.setPtcLon(116.75199);
//        radarMsgTopic.setPtcLat(36.55358);
//        radarMsgTopic.setPtcEle(8848.88);
//        radarMsgTopic.setAmapLon(116.75199);
//        radarMsgTopic.setAmapLat(36.55358);
//        radarMsgTopic.setPtcSpeed(120.5);
//        radarMsgTopic.setPtcHeading(20.15);
//        radarMsgTopic.setVehType(4);
//        radarMsgTopic.setCreateTime(format);
//        radarMsgTopic.setSpare(null);
//        map.put("expands",radarMsgTopic);
//        map.put("user",SecurityUtils.getUsername());
//
//        List<String> devNos = CollUtil.newArrayList(devNo);
//        List<Map<String, Object>> devList = CollUtil.newArrayList(map);
//        Map<String, Object> content = new HashMap<>();
//        content.put("devList", devList);
//        String s = this.sendData(MsgType.msg31.getCode(), MsgType.msgId01.getCode(), devType, devNos, MsgType.msgUp.getCode(), content);
//        return s;
//    }

//    @Override
//    public AjaxResult storages(SdStateStorage sdStateStorage) {
//        String devNo="S00063700001980001";
//        String devType="198";
//        //时间戳
//        String format = DateUtil.format(DateUtil.date(), sdf_pattern);
//        //消息数据
//        Map<String, Object> map = new HashMap<>();
//        map.put("devNo",devNo);
//        map.put("devType",devType);
//        map.put("directType","车道指示器topic");
//        map.put("directTypeDesc","车道指示器消息");
//        map.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern)); //时间戳，必填
//        //自定义数据
//        SdStateStorage storage=new SdStateStorage();
//        //状态
//        storage.setState(sdStateStorage.getState());
//        storage.setDeviceId(sdStateStorage.getDeviceId());
//        storage.setTunnelId(sdStateStorage.getTunnelId());
//        map.put("expands",storage);
//        map.put("user",SecurityUtils.getUsername());
//        //设备编号
//        List<String> devNos = CollUtil.newArrayList(devNo);
//        List<Map<String, Object>> devList = CollUtil.newArrayList(map);
//        Map<String, Object> content = new HashMap<>();
//        content.put("devList", devList);
//        String s = this.sendData(MsgType.msg31.getCode(), MsgType.msgId02.getCode(), devType, devNos, MsgType.msgUp.getCode(), content);
//        return AjaxResult.success(s);
//    }

    @Override
    public int pushDevicesStatusToOtherSystem(SdDevices sdDevices, String role, String status) {
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
            List<SdDevices> devicesList = devicesMapper.selectFireComponentsList(sdDevices);
            for (int i = 0;i < devicesList.size();i++) {
                SdDevices dev = devicesList.get(i);
                if (status.equals("off")) {
                    dev.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
                } else if (status.equals("on")) {
                    dev.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
                }
                JSONObject object = devStatus(dev);
                kafkaTemplate.send(TopicEnum.DEV_STATUS_TOPIC.getCode(), object.toString());
            }
        }
        return 0;
    }

//    /**
//     * 发送数据
//     *
//     * @param msgType 消息类型
//     * @param msgId 消息编码
//     * @param devType 设备类型
//     * @param devNos 设备编号
//     * @param direct 数据传输
//     * @param content 内容
//     * @return
//     */
//    private String sendData(String msgType, String msgId, String devType, List<String> devNos, String direct, Map<String, Object> content) {
//        JSONObject requestData = this.getBaseRequestData(msgType, msgId, devType, devNos, direct);
//        requestData.put("content", content);
//        //请求头
//        HttpHeaders requestHeaders = new HttpHeaders();
//        //设置JSON格式数据
//        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> requestEntity = new HttpEntity<>(requestData.toString(), requestHeaders);
//        //执行请求
//        log.info("参数 --> {}", requestData);
//        try {
//            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
//            log.info("返回值 --> {}", responseEntity.getBody());
//            return "发送成功--时间:"+DateUtil.format(DateUtil.date(), sdf_pattern);
//        } catch (Exception e) {
//            log.error("物联网关数据发送失败！{}", e.getMessage());
//            return "发送失败--时间:"+DateUtil.format(DateUtil.date(), sdf_pattern);
//        }
//    }
    /**
     * 获取上报外层数据
     *
     * @param msgType
     * @param msgId
     * @param devType
     * @param devNos
     * @param direct
     * @return
     */

    private JSONObject getBaseRequestData(String msgType, String msgId, String devType, List<String> devNos, String direct) {
        JSONObject requestData = new JSONObject();
        String timeStamp = String.valueOf(DateUtil.current());
        //设备类型
        requestData.put("msgType", msgType);
        //消息编码
        requestData.put("msgId", msgId);
        //版本号
        requestData.put("msgVer", "1.0");
        //设备编号
        requestData.put("devNo", devNos);
        //设备类型
        requestData.put("devType", devType);
        //时间戳
        requestData.put("timeStamp", timeStamp);
        //消息方向
        requestData.put("direct", direct);
        //MD5
        String md5 = this.getMd5(timeStamp, devNos);
        requestData.put("md5", md5);
        return requestData;
    }
    /**
     * 获取MD5值
     *
     * @param timeStamp
     * @param devNos
     * @return
     */
    private String getMd5(String timeStamp, List<String> devNos) {
        String plaintext = timeStamp + this.getDevSecureKeys(devNos);
        return SecureUtil.md5(plaintext);
    }

    /**
     * 获取设备秘钥，目前和设备编号一致
     *
     * @param devNos
     * @return
     */
    private String getDevSecureKeys(List<String> devNos) {
        //TODO 目前设备秘钥和设备编号一致
        return StrUtil.join("", devNos);
    }

    /**
     * 上传物联中台数据格式
     * @param sdDevices
     * @return
     */
    public JSONObject devStatus(SdDevices sdDevices){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("devNo", "SDEV00063700001980003");
        jsonObject.put("devType",sdDevices.getEqType());
        jsonObject.put("loginTime", DateUtils.getNowDate());
        jsonObject.put("devStatus",sdDevices.getEqStatus());
        jsonObject.put("source","");
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
        jsonObject.put("expands",sdDevices);
        return jsonObject;
    }

    /**
     * 重新定义参数名
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
     * @param eqType
     * @param object
     * @return
     */
    public JSONObject devReaStatus(Long eqType, JSONObject object){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("devNo", "SRUN00063700001980002");
        jsonObject.put("devType",eqType);
        jsonObject.put("loginTime",DateUtils.getNowDate());
        jsonObject.put("devStatus","1");
        jsonObject.put("netstatus","1");
        jsonObject.put("source","");
        jsonObject.put("timeStamp", DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        jsonObject.put("expands",object.toString());
        return jsonObject;
    }
}
