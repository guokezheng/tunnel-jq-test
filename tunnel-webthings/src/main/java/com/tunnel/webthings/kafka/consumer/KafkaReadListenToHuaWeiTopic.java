package com.tunnel.webthings.kafka.consumer;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.datacenter.domain.enumeration.PlatformAuthEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.electromechanicalPatrol.SdFaultList;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDeviceTypeItemMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.electromechanicalPatrol.SdFaultListMapper;
import com.tunnel.platform.controller.platformAuthApi.PlatformApiController;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 读取设备、隧道基础数据
 * 两级平台设备、隧道数据同步
 *
 * @author zhai
 * @date 2022/11/1
 */
@Component
public class KafkaReadListenToHuaWeiTopic {

    private static final Logger log = LoggerFactory.getLogger(KafkaReadListenToHuaWeiTopic.class);

    @Autowired
    @Qualifier("kafkaTwoTemplate")
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private SdDeviceDataMapper sdDeviceDataMapper;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Autowired
    private SdFaultListMapper sdFaultListMapper;

    @Autowired
    private SdDeviceTypeItemMapper sdDeviceTypeItemMapper;

    /**
     * 监听风机实时运行状态
     *
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"rhy_iot_receive_fan_runStatus"}, containerFactory = "kafkaThreeContainerFactory")
    public void fanRunStatus(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        if(record.value() != null && record.value() != ""){
            //获取风机itemId
            Long itemId = Long.valueOf(DevicesTypeItemEnum.FENG_JI_STATUS.getCode());
            //解析风机数据
            JSONArray objects = JSONObject.parseArray(record.value().toString());
            //新增or更新设备数据
            saveOrUpdataRealTime(objects,itemId);
        }
        consumer.commitSync();
    }

    /**
     * 监听交通信号灯实时运行状态
     *
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"rhy_iot_receive_trafficLight_runStatus"}, containerFactory = "kafkaThreeContainerFactory")
    public void trafficLightRunStatus(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        if(record.value() != null && record.value() != ""){
            //获取交通信号灯itemId
            Long itemId = Long.valueOf(DevicesTypeItemEnum.XIN_HAO_DENG.getCode());
            //解析交通信号灯数据
            JSONArray objects = JSONObject.parseArray(record.value().toString());
            //新增or更新设备数据
            saveOrUpdataRealTime(objects,itemId);
        }
        consumer.commitSync();
    }

    /**
     * 监听车道指示器实时运行状态
     *
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"rhy_iot_receive_laneIndicator_runStatus"}, containerFactory = "kafkaThreeContainerFactory")
    public void laneIndicatorRunStatus(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        if(record.value() != null & record.value() != ""){
            //获取车指itemId
            Long itemId = Long.valueOf(DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode());
            //解析车道指示器数据
            JSONArray objects = JSONObject.parseArray(record.value().toString());
            //新增or更新设备数据
            saveOrUpdataRealTime(objects,itemId);
            //将车道指示器数据推送至高速云
            //kafkaTemplate.send("wq_devStatusTopic",objects.toString());
        }
        consumer.commitSync();
    }

    /**
     * 监听卷帘门实时运行状态
     *
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"rhy_iot_receive_rollDoor_runStatus"}, containerFactory = "kafkaThreeContainerFactory")
    public void rollDoorRunStatus(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        if(record.value() != null & record.value() != ""){
            //获取卷帘门itemId
            Long itemId = Long.valueOf(DevicesTypeItemEnum.JUAN_LIAN_MEN.getCode());
            //解析卷帘门数据
            JSONArray objects = JSONObject.parseArray(record.value().toString());
            //新增or更新设备数据
            saveOrUpdataRealTime(objects,itemId);
        }
        consumer.commitSync();
    }

    /**
     * 监听CO/VI实时业务属性
     *
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"rhy_iot_receive_covi_bizAttr"}, containerFactory = "kafkaThreeContainerFactory")
    public void coviBizAttr(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        if(record.value() != null & record.value() != ""){
            //获取co/vi的itemId
            Long coId = Long.valueOf(DevicesTypeItemEnum.CO.getCode());
            Long viId = Long.valueOf(DevicesTypeItemEnum.VI.getCode());
            //解析co/vi数据
            JSONArray objects = JSONObject.parseArray(record.value().toString());
            //新增or更新CO/VI数据
            saveOrUpdateCoViData(objects,coId,viId);
        }
        consumer.commitSync();
    }

    /**
     * 监听风速风向实时业务属性
     *
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"rhy_iot_receive_anemoclinograph_bizAttr"}, containerFactory = "kafkaThreeContainerFactory")
    public void anemoclinographBizAttr(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        if(record.value() != null & record.value() != ""){
            //获取风速风向的itemId
            Long fsId = Long.valueOf(DevicesTypeItemEnum.FENG_SU.getCode());
            Long fxId = Long.valueOf(DevicesTypeItemEnum.FENG_XIANG.getCode());
            //解析风速风向数据
            JSONArray objects = JSONObject.parseArray(record.value().toString());
            //新增or更新风速风向数据
            saveOrUpdateFsFxData(objects,fsId,fxId);
        }
        consumer.commitSync();
    }

    /**
     * 监听洞内亮度实时业务属性
     *
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"rhy_iot_receive_illuminance_bizAttr"}, containerFactory = "kafkaThreeContainerFactory")
    public void illuminanceBizAttr(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        if(record.value() != null & record.value() != ""){
            //获取洞内亮度itemId
            Long itemId = Long.valueOf(DevicesTypeItemEnum.LIANG_DU_INSIDE.getCode());
            //解析洞内亮度数据
            JSONArray objects = JSONObject.parseArray(record.value().toString());
            //新增or更新设备数据
            saveOrUpdataIlluminance(objects,itemId);
        }
        consumer.commitSync();
    }

    /**
     * 监听洞外亮度实时业务属性
     *
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"rhy_iot_receive_brightDetector_bizAttr"}, containerFactory = "kafkaThreeContainerFactory")
    public void brightDetectorBizAttr(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        if(record.value() != null & record.value() != ""){
            //获取洞外亮度itemId
            Long itemId = Long.valueOf(DevicesTypeItemEnum.LIANG_DU_OUTSIDE.getCode());
            //解析洞外亮度数据
            JSONArray objects = JSONObject.parseArray(record.value().toString());
            //新增or更新设备数据
            saveOrUpdataBrightDetector(objects,itemId);
        }
        consumer.commitSync();
    }

    /**
     * 获取实时设备状态
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    @KafkaListener(topics = {"rhy_iot_receive_devStatus"}, containerFactory = "kafkaThreeContainerFactory")
    public void receiveDevStatus(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        if(record.value() != null && record.value() != ""){
            //解析实时设备状态
            JSONArray objects = JSONObject.parseArray(record.value().toString());
            //更新设备状态
            updateDevStatus(objects);
        }
        consumer.commitSync();
    }

    /**
     * 获取实时设备故障信息
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    @KafkaListener(topics = {"rhy_iot_receive_devFault"}, containerFactory = "kafkaThreeContainerFactory")
    public void receiveDevFault(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info(record.value().toString());
        if(record.value() != null && record.value() != ""){
            //解析实时设备状态
            JSONArray objects = JSONObject.parseArray(record.value().toString());
            //更新设备状态
            addDevFault(objects);
        }
        consumer.commitSync();
    }

    /**
     * 更新设备状态
     * @param objects
     */
    public void updateDevStatus(JSONArray objects){
        log.info(objects.toString());
        for(int i = 0; i < objects.size(); i++){
            JSONObject jsonObject = JSONObject.parseObject(objects.get(i).toString());
            //设备id
            String deviceId = jsonObject.getString("deviceId");
            //设备状态
            Integer onlineStatus = jsonObject.getInteger("onlineStatus");
            //是否故障
            Integer isFault = jsonObject.getInteger("isFault");
            SdDevices sdDevices = new SdDevices();
            sdDevices.setEqId(deviceId);
            sdDevices.setUpdateTime(DateUtils.getNowDate());
            sdDevices.setEqStatusTime(DateUtils.getNowDate());
            if(isFault == 0) {
                if (onlineStatus == 0) {
                    //离线
                    sdDevices.setEqStatus("2");
                } else if (onlineStatus == 1) {
                    //在线
                    sdDevices.setEqStatus("1");
                }
            }else {
                //故障
                sdDevices.setEqStatus("3");
            }
            //更新设备状态
            int count = sdDevicesMapper.updateSdDevices(sdDevices);
            if(count > 0){
                SdDevices sdDevices1 = sdDevicesMapper.selectSdDevicesById(deviceId);
                JSONObject jsonObject1 = devStatus(sdDevices1);
                //将实时设备状态数据推送至高速云
                kafkaTemplate.send("wq_devStatusTopic",jsonObject1.toString());
            }
        }
    }

    /**
     * 新增设备故障信息
     * @param objects
     */
    public void addDevFault(JSONArray objects){
        log.info(objects.toString());
        //数据库已存在数据
        List<SdFaultList> sdFaultLists = sdFaultListMapper.selectDeviceFault();
        List<SdFaultList> sdFaultListsCopy = new ArrayList<>();
        for(SdFaultList item : sdFaultLists){
            sdFaultListsCopy.add(item);
        }
        //新故障数据
        List<SdFaultList> newFaultList = new ArrayList<>();
        for(int i = 0; i < objects.size(); i++){
            JSONObject jsonObject = JSONObject.parseObject(objects.get(i).toString());
            //设备id
            String deviceId = jsonObject.getString("deviceId");
            //设备类型
            Long deviceType = jsonObject.getLong("deviceType");
            //设备故障id
            String id = jsonObject.getString("id");
            //设备故障等级  0:一般故障,1:重大故障,2:特大故障
            String faultLevel = jsonObject.getString("faultLevel");
            //设备故障状态码
            String faultCode = jsonObject.getString("faultCode");
            //设备故障详情
            String faultDetail = jsonObject.getString("faultDetail");
            //设备故障开始时间
            String startTime = jsonObject.getString("startTime");
            //设备故障持续时间
            String alwaysTime = jsonObject.getString("alwaysTime");
            SdFaultList sdFaultList = new SdFaultList();
            sdFaultList.setId(id);
            sdFaultList.setEqId(deviceId);
            sdFaultList.setTunnelId("JQ-WeiFang-JiuLongYu-HSD");
            //类型暂时为 其他
            sdFaultList.setFaultType("6");
            sdFaultList.setFaultFxtime(DateUtils.parseDate(startTime));
            sdFaultList.setFaultCxtime(alwaysTime);
            sdFaultList.setEqType(deviceType.toString());
            sdFaultList.setEqStatus("3");
            sdFaultList.setFaultCode(faultCode);
            sdFaultList.setFaultLevel(faultLevel);
            //消除状态暂时为 未消除
            sdFaultList.setFalltRemoveStatue("1");
            sdFaultList.setFaultDescription(faultDetail);
            //故障状态暂时为 已发布
            sdFaultList.setFaultStatus("0");
            //区分上报类型 0：人为填报 1：设备上报
            sdFaultList.setFaultEscalationType("1");
            //查询设备运行状态
            sdFaultList.setEqRunStatus(sdFaultListMapper.selectEqRunStatus(deviceId,deviceType));
            SdFaultList sdFaultList1 = sdFaultListMapper.selectSdFaultListById(id);
            if(sdFaultList1 == null){
                sdFaultList.setCreateTime(DateUtils.getNowDate());
                sdFaultListMapper.insertSdFaultList(sdFaultList);
            }else {
                sdFaultList.setUpdateTime(DateUtils.getNowDate());
                sdFaultListMapper.updateSdFaultList(sdFaultList);
            }
            newFaultList.add(sdFaultList);
        }

        for(int i = sdFaultLists.size() - 1; i >= 0; i--){
            SdFaultList sdFaultList = sdFaultLists.get(i);
            for(SdFaultList temp : newFaultList){
                if(sdFaultList.getEqId().equals(temp.getEqId()) && sdFaultList.getEqType().equals(temp.getEqType())){
                    sdFaultListsCopy.remove(i);
                }
            }
        }
        for(SdFaultList item : sdFaultListsCopy){
            item.setFalltRemoveStatue("0");
            item.setUpdateTime(DateUtils.getNowDate());
            sdFaultListMapper.updateSdFaultList(item);
        }
    }

    /**
     * 新增or更新设备数据
     * @param objects
     * @param itemId
     */
    public void saveOrUpdataRealTime(JSONArray objects,Long itemId){
        //循环遍历kafka数据
        for(int i = 0; i < objects.size(); i++){
            JSONObject jsonObject1 = JSONObject.parseObject(objects.get(i).toString());
            String deviceId = jsonObject1.getString("deviceId");
            Integer runStatus = 0;
            if(DevicesTypeItemEnum.XIN_HAO_DENG.getCode() == itemId && jsonObject1.getInteger("runStatus") == 4){
                runStatus = devStatusCync(Long.valueOf(DevicesTypeItemEnum.ZHUO_ZHUAN_XIN_HAO_DENG.getCode()),jsonObject1.getInteger("runStatus"));
                itemId = Long.valueOf(DevicesTypeItemEnum.ZHUO_ZHUAN_XIN_HAO_DENG.getCode());
            }else if(DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode() == itemId && jsonObject1.getInteger("runStatus") == 4){
                runStatus = devStatusCync(Long.valueOf(DevicesTypeItemEnum.ZHUO_ZHUAN_CHE_ZHI.getCode()),jsonObject1.getInteger("runStatus"));
                itemId = Long.valueOf(DevicesTypeItemEnum.ZHUO_ZHUAN_CHE_ZHI.getCode());
            }else {
                runStatus = devStatusCync(itemId,jsonObject1.getInteger("runStatus"));
            }
            //将数据重新定义新的参数名
            JSONObject objectDev = definitionParam(deviceId, runStatus.toString(), itemId);
            //校验数据库是否存在
            int number = checkDeviceData(deviceId, itemId);
            if(number == 0){
                //新增数据
                SdDeviceData deviceData = setDeviceData(deviceId, runStatus.toString(), itemId);
                deviceData.setCreateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.insertSdDeviceData(deviceData);
            }else {
                //更新数据
                SdDeviceData deviceData = setDeviceData(deviceId, runStatus.toString(), itemId);
                deviceData.setUpdateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.updateKafkaDeviceData(deviceData);
            }
            SdDevices sdDevices1 = sdDevicesMapper.selectSdDevicesById(deviceId);
            JSONObject jsonObjectDev = devReaStatus(sdDevices1 == null ? sdDeviceTypeItemMapper.selectSdDeviceTypeItemById(itemId).getDeviceTypeId() : sdDevices1.getEqType(), objectDev);
            //将设备运行状态上传至高速云
            kafkaTemplate.send("wq_devStatusTopic",jsonObjectDev.toString());
        }
    }

    /**
     * 新增or更新co/vi数据
     * @param objects
     * @param coId
     * @param viId
     */
    public void saveOrUpdateCoViData(JSONArray objects, Long coId, Long viId){
        //循环遍历kafka数据
        for(int i = 0; i < objects.size(); i++){
            JSONObject jsonObject1 = JSONObject.parseObject(objects.get(i).toString());
            String deviceId = jsonObject1.getString("deviceId");
            String co = jsonObject1.getString("co");
            String vi = jsonObject1.getString("vi");
            //将数据重新定义新的参数名
            JSONObject objectCo = definitionParam(deviceId, co, coId);
            JSONObject objectVi = definitionParam(deviceId, vi, viId);
            //校验数据库是否存在
            int numberCo = checkDeviceData(deviceId, coId);
            if(numberCo == 0){
                //新增数据
                SdDeviceData deviceData = setDeviceData(deviceId, co, coId);
                deviceData.setCreateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.insertSdDeviceData(deviceData);
            }else {
                //更新数据
                SdDeviceData deviceData = setDeviceData(deviceId, co, coId);
                deviceData.setUpdateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.updateKafkaDeviceData(deviceData);
            }
            //校验数据库是否存在
            int numberVi = checkDeviceData(deviceId, viId);
            if(numberVi == 0){
                //新增数据
                SdDeviceData deviceData = setDeviceData(deviceId, vi, viId);
                deviceData.setCreateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.insertSdDeviceData(deviceData);
            }else {
                //更新数据
                SdDeviceData deviceData = setDeviceData(deviceId, vi, viId);
                deviceData.setUpdateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.updateKafkaDeviceData(deviceData);
            }
            SdDevices sdDevices1 = sdDevicesMapper.selectSdDevicesById(deviceId);
            JSONObject jsonObjectCo = devReaStatus(sdDevices1 == null ? sdDeviceTypeItemMapper.selectSdDeviceTypeItemById(coId).getDeviceTypeId() : sdDevices1.getEqType(), objectCo);
            //将co数据上传至高速云
            kafkaTemplate.send("wq_devStatusTopic",jsonObjectCo.toString());
            //将vi数据上传至高速云
            JSONObject jsonObjectVi = devReaStatus(sdDevices1 == null ? sdDeviceTypeItemMapper.selectSdDeviceTypeItemById(viId).getDeviceTypeId() : sdDevices1.getEqType(), objectVi);
            kafkaTemplate.send("wq_devStatusTopic",jsonObjectVi.toString());
        }
    }

    /**
     * 新增or更新风速风向数据
     * @param objects
     * @param fsId
     * @param fxId
     */
    public void saveOrUpdateFsFxData(JSONArray objects, Long fsId, Long fxId){
        //循环遍历kafka数据
        for(int i = 0; i < objects.size(); i++){
            JSONObject jsonObject1 = JSONObject.parseObject(objects.get(i).toString());
            String deviceId = jsonObject1.getString("deviceId");
            String windSpeed = jsonObject1.getString("windSpeed");
            String windDirection = jsonObject1.getString("windDirection");
            //校验数据库是否存在
            int numberFs = checkDeviceData(deviceId, fsId);
            if(numberFs == 0){
                //新增数据
                SdDeviceData deviceData = setDeviceData(deviceId, windSpeed, fsId);
                deviceData.setCreateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.insertSdDeviceData(deviceData);
            }else {
                //更新数据
                SdDeviceData deviceData = setDeviceData(deviceId, windSpeed, fsId);
                deviceData.setUpdateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.updateKafkaDeviceData(deviceData);
            }
            //校验数据库是否存在
            int numberFx = checkDeviceData(deviceId, fxId);
            if(numberFx == 0){
                //新增数据
                SdDeviceData deviceData = setDeviceData(deviceId, windDirection, fxId);
                deviceData.setCreateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.insertSdDeviceData(deviceData);
            }else {
                //更新数据
                SdDeviceData deviceData = setDeviceData(deviceId, windDirection, fxId);
                deviceData.setUpdateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.updateKafkaDeviceData(deviceData);
            }
            SdDevices sdDevices1 = sdDevicesMapper.selectSdDevicesById(deviceId);
            //重新定义参数名
            JSONObject objectFs = definitionParam(deviceId, windSpeed, fsId);
            JSONObject objectFx = definitionParam(deviceId, windDirection, fxId);
            JSONObject jsonObjectFs = devReaStatus(sdDevices1 == null ? sdDeviceTypeItemMapper.selectSdDeviceTypeItemById(fsId).getDeviceTypeId() : sdDevices1.getEqType(), objectFs);
            JSONObject jsonObjectFx = devReaStatus(sdDevices1 == null ? sdDeviceTypeItemMapper.selectSdDeviceTypeItemById(fxId).getDeviceTypeId() : sdDevices1.getEqType(), objectFx);
            //将风速风向数据上传至高速云
            kafkaTemplate.send("wq_devStatusTopic",jsonObjectFs.toString());
            kafkaTemplate.send("wq_devStatusTopic",jsonObjectFx.toString());
        }
    }

    /**
     * 新增or更新洞内亮度数据
     * @param objects
     * @param itemId
     */
    public void saveOrUpdataIlluminance(JSONArray objects,Long itemId){
        //循环遍历kafka数据
        for(int i = 0; i < objects.size(); i++){
            JSONObject jsonObject1 = JSONObject.parseObject(objects.get(i).toString());
            String deviceId = jsonObject1.getString("deviceId");
            String illuminance = jsonObject1.getString("illuminance");
            //校验数据库是否存在
            int number = checkDeviceData(deviceId, itemId);
            if(number == 0){
                //新增数据
                SdDeviceData deviceData = setDeviceData(deviceId, illuminance, itemId);
                deviceData.setCreateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.insertSdDeviceData(deviceData);
            }else {
                //更新数据
                SdDeviceData deviceData = setDeviceData(deviceId, illuminance, itemId);
                deviceData.setUpdateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.updateKafkaDeviceData(deviceData);
            }
        }
    }

    /**
     * 新增or更新洞外亮度数据
     * @param objects
     * @param itemId
     */
    public void saveOrUpdataBrightDetector(JSONArray objects,Long itemId){
        //循环遍历kafka数据
        for(int i = 0; i < objects.size(); i++){
            JSONObject jsonObject1 = JSONObject.parseObject(objects.get(i).toString());
            String deviceId = jsonObject1.getString("deviceId");
            String brightness = jsonObject1.getString("brightness");
            int number = checkDeviceData(deviceId, itemId);
            if(number == 0){
                //新增数据
                SdDeviceData deviceData = setDeviceData(deviceId, brightness, itemId);
                deviceData.setCreateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.insertSdDeviceData(deviceData);
            }else {
                //更新数据
                SdDeviceData deviceData = setDeviceData(deviceId, brightness, itemId);
                deviceData.setUpdateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.updateKafkaDeviceData(deviceData);
            }
        }
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
        jsonObject.put("source","suidao");
        jsonObject.put("timeStamp", DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        jsonObject.put("expands",object.toString());
        return jsonObject;
    }

    /**
     * 实时设备状态上传高速云共通方法
     * @param sdDevices
     * @return
     */
    public JSONObject devStatus(SdDevices sdDevices){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("devNo", "SDEV00063700001980003");
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
        jsonObject.put("expands",sdDevices);
        return jsonObject;
    }

    /**
     * 校验实时数据是否存在
     * @param deviceId
     * @param itemId
     * @return
     */
    public int checkDeviceData(String deviceId, Long itemId){
        //校验数据库是否存在
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setItemId(itemId);
        sdDeviceData.setDeviceId(deviceId);
        List<SdDeviceData> sdDeviceDataList = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
        if(sdDeviceDataList.size() > 0){
            return 1;
        }else {
            return 0;
        }
    }

    /**
     * 设备运行状态与数据库匹配
     * @param itemId
     * @param runStatus
     * @return
     */
    public Integer devStatusCync(Long itemId,Integer runStatus){
        if(DevicesTypeItemEnum.FENG_JI_STATUS.getCode() == itemId){
            if(runStatus == 0){
                return 3;
            }
        }
        if(DevicesTypeItemEnum.XIN_HAO_DENG.getCode() == itemId){
            if(runStatus == 0){
                return 4;
            }
            if(runStatus == 1){
                return 2;
            }
            if(runStatus == 2){
                return 1;
            }
        }
        if(DevicesTypeItemEnum.ZHUO_ZHUAN_XIN_HAO_DENG.getCode() == itemId){
            if(runStatus == 4){
                return 1;
            }
        }
        if(DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode() == itemId){
            if(runStatus == 0){
                return 4;
            }
        }
        if(DevicesTypeItemEnum.ZHUO_ZHUAN_CHE_ZHI.getCode() == itemId){
            if(runStatus == 4){
                return 5;
            }
        }
        if(DevicesTypeItemEnum.JUAN_LIAN_MEN.getCode() == itemId){
            if(runStatus == 0){
                return 3;
            }
        }
        return runStatus;
    }
}
