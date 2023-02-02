package com.tunnel.webthings.kafka.consumer;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.WindDirectionUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.*;
import com.tunnel.business.domain.dataInfo.*;
import com.tunnel.business.domain.electromechanicalPatrol.SdFaultList;
import com.tunnel.business.domain.event.*;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import com.tunnel.business.mapper.dataInfo.*;
import com.tunnel.business.mapper.digitalmodel.SdRadarDetectDataMapper;
import com.tunnel.business.mapper.electromechanicalPatrol.SdFaultListMapper;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.mapper.event.SdLaneStatisticsMapper;
import com.tunnel.business.mapper.event.SdRoadSectionStatisticsMapper;
import com.tunnel.business.mapper.event.SdVehicleDrivingMapper;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficImageMapper;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.digitalmodel.impl.RadarEventServiceImpl;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.event.ISdEventTypeService;
import com.tunnel.business.service.sendDataToKafka.SendDeviceStatusToKafkaService;
import com.zc.common.core.websocket.WebSocketService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private ISdEventService sdEventService;

    @Autowired
    private ISdTunnelsService sdTunnelsService;

    @Autowired
    private ISdEventTypeService sdEventTypeService;

    @Autowired
    private RadarEventServiceImpl radarEventServiceImpl;

    @Autowired
    private SdEventMapper sdEventMapper;

    @Autowired
    private SdMicrowavePeriodicStatisticsMapper statisticsMapper;

    @Autowired
    private RedisCache redisCache;

    private static SendDeviceStatusToKafkaService sendData = SpringUtils.getBean(SendDeviceStatusToKafkaService.class);

    /**
     * 监听风机实时运行状态
     *
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"rhy_iot_receive_fan_runStatus"}, containerFactory = "kafkaThreeContainerFactory")
    public void fanRunStatus(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("监听到瑞华赢风机数据： --> {}",record.value());
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
        log.info("监听到瑞华赢信号灯数据： --> {}",record.value());
        if(record.value() != null && record.value() != ""){
            //获取交通信号灯itemId
            Long itemId = Long.valueOf(DevicesTypeItemEnum.ZHUO_ZHUAN_XIN_HAO_DENG.getCode());
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
        log.info("监听到瑞华赢车指数据： --> {}",record.value());
        if(record.value() != null & record.value() != ""){
            //获取车指itemId
            Long itemId = Long.valueOf(DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode());
            //解析车道指示器数据
            JSONArray objects = JSONObject.parseArray(record.value().toString());
            //新增or更新设备数据
            saveOrUpdataRealTime(objects,itemId);
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
        log.info("监听到瑞华赢卷帘门数据： --> {}",record.value());
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
        log.info("监听到瑞华赢COVI数据： --> {}",record.value());
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
        log.info("监听到瑞华赢风速风向数据： --> {}",record.value());
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
        log.info("监听到瑞华赢洞内亮度数据： --> {}",record.value());
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
        log.info("监听到瑞华赢洞外亮度数据： --> {}",record.value());
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
     * 获取实时远传压力值信息
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    @KafkaListener(topics = {"rhy_iot_receive_pressureInstrument_bizAttr"}, containerFactory = "kafkaThreeContainerFactory")
    public void pressureInstrumentBizAttr(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("监听到瑞华赢远传压力表数据： --> {}",record.value());
        if(record.value() != null & record.value() != ""){
            //获取远传压力值itemId
            Long itemId = Long.valueOf(DevicesTypeItemEnum.YUAN_CHUAN_YA_LI_ZHI.getCode());
            //解析远传压力值数据
            JSONArray objects = JSONObject.parseArray(record.value().toString());
            //新增远传压力值数据
            savePressureInstrument(objects,itemId);
        }
        consumer.commitSync();
    }

    /**
     * 获取实时风机安全检测仪信息
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    @KafkaListener(topics = {"rhy_iot_receive_fanSafeMonitor_bizAttr"}, containerFactory = "kafkaThreeContainerFactory")
    public void fanSafeMonitorBizAttr(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("监听到瑞华赢风机安全检测仪数据： --> {}",record.value());
        if(record.value() != null & record.value() != ""){
            //获取远传压力值itemId
            Long zhenSuDu = Long.valueOf(DevicesTypeItemEnum.ZHEN_DONG_SU_DU.getCode());
            Long zhenFuDu = Long.valueOf(DevicesTypeItemEnum.ZHEN_DONG_FU_DU.getCode());
            Long chenJiang = Long.valueOf(DevicesTypeItemEnum.CHEN_JIANG_ZHI.getCode());
            Long qingXie = Long.valueOf(DevicesTypeItemEnum.QING_XIE_ZHI.getCode());
            Long zhenGaoJing = Long.valueOf(DevicesTypeItemEnum.ZHEN_DONG_GAO_JING.getCode());
            Long chenQingGaoJing = Long.valueOf(DevicesTypeItemEnum.CHEN_JIANG_QING_XIE_GAO_JING.getCode());
            //解析风机安全检测仪数据
            JSONArray objects = JSONObject.parseArray(record.value().toString());
            //新增or更新风机安全检测仪数据
            saveOrUpdateFanSafeMonitor(objects, zhenSuDu, zhenFuDu, chenJiang, qingXie, zhenGaoJing, chenQingGaoJing);
        }
        consumer.commitSync();
    }

    /**
     * 实时获取火灾报警信息
     *
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"rhy_iot_receive_fire_alert"}, containerFactory = "kafkaThreeContainerFactory")
    public void receiveFireAlert(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("监听到瑞华赢火灾报警数据： --> {}",record.value());
        if(record.value() != null & record.value() != ""){
            //解析火灾报警数据
            JSONArray objects = JSONObject.parseArray(record.value().toString());
            //处理火灾报警数据
            fireDataHandle(objects);
        }
        consumer.commitSync();
    }

    /**
     * 获取实时微波车检信息
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    @KafkaListener(topics = {"rhy_iot_receive_vd_bizAttr"}, containerFactory = "kafkaThreeContainerFactory")
    public void receiveVdBizAttr(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("监听到瑞华赢微波车检数据： --> {}",record.value());
        if(record.value() != null & record.value() != ""){
            //获取微波车检itemId
            Long itemId = Long.valueOf(DevicesTypeItemEnum.WEI_BO_CHE_LIANG_JIAN_CE_QI.getCode());
            //解析微波车检数据
            JSONArray objects = JSONObject.parseArray(record.value().toString());
            //新增微波车检数据
            saveReceiveVdBizAttr(objects,itemId);
        }
        consumer.commitSync();
    }

    /**
     * 获取车道统计数据
     *
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    @KafkaListener(topics = {"analysis.crosslane.hist"}, containerFactory = "kafkaThreeContainerFactory")
    public void analysisCrosslane(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("监听到瑞华赢-图盟车道统计数据： --> {}",record.value());
        if(record.value() != null & record.value() != ""){
            //解析车道统计数据
            JSONObject jsonObject = JSONObject.parseObject(record.value().toString());
            //新增车道统计数据
            analysisAnalysisCrosslane(jsonObject);
        }
        consumer.commitSync();
    }

    /**
     * 获取路段统计数据
     *
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    @KafkaListener(topics = {"analysis.rid.hist"}, containerFactory = "kafkaThreeContainerFactory")
    public void analysisRid(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("监听到瑞华赢-图盟路段统计数据： --> {}",record.value());
        if(record.value() != null & record.value() != ""){
            //解析路段统计数据
            JSONObject jsonObject = JSONObject.parseObject(record.value().toString());
            //新增路段统计数据
            analysisAnalysisRid(jsonObject);
        }
        consumer.commitSync();
    }


    /**
     * 获取路段车辆行驶记录数据
     *
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    @KafkaListener(topics = {"analysis.rid.travel"}, containerFactory = "kafkaThreeContainerFactory")
    public void ridTravel(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("监听到瑞华赢-图盟路段车辆行驶记录数据： --> {}",record.value());
        if(record.value() != null & record.value() != ""){
            //解析路段车辆行驶记录数据
            JSONObject jsonObject = JSONObject.parseObject(record.value().toString());
            //新增路段车辆行驶记录数据
            analysisRidTravel(jsonObject);
        }
        consumer.commitSync();
    }

    /**
     * 获取车辆快照数据
     *
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    @KafkaListener(topics = {"analysis.carsnap"}, containerFactory = "kafkaThreeContainerFactory")
    public void analysisCarsnap(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("监听到瑞华赢-图盟车辆快照数据： --> {}",record.value());
        if(record.value() != null & record.value() != ""){
            //解析车辆快照数据
            JSONObject jsonObject = JSONObject.parseObject(record.value().toString());
            JSONArray track = JSONObject.parseArray(jsonObject.getString("track"));
            //新增车辆快照数据
            saveAnalysisCarsnap(track);
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
        log.info("监听到瑞华赢实时设备数据： --> {}",record.value());
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
     *
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    @KafkaListener(topics = {"rhy_iot_receive_devFault"}, containerFactory = "kafkaThreeContainerFactory")
    public void receiveDevFault(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("监听到瑞华赢实时设备故障数据： --> {}",record.value());
        if(record.value() != null && record.value() != ""){
            //解析实时设备状态
            JSONArray objects = JSONObject.parseArray(record.value().toString());
            //更新设备状态
            addDevFault(objects);
        }
        consumer.commitSync();
    }

    /**
     * 接收瑞华赢实时事件推送
     *
     * @param record
     * @param acknowledgment
     * @param consumer
     */
    @KafkaListener(topics = {"rhy_tunnel_merge_event"}, containerFactory = "kafkaThreeContainerFactory")
    public void receiveEvent(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
        log.info("监听到瑞华赢实时事件数据： --> {}", record.value());
        if (record.value() != null && record.value() != "") {
            JSONObject objects =  JSON.parseObject(record.value().toString());
            joinEvent(objects);
        }
        consumer.commitSync();
    }

    /**
     * 更新设备状态
     *
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
     *
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
            SdDevices sdDevices = sdDevicesMapper.selectSdDevicesById(deviceId);
            sdFaultList.setFaultLocation(sdDevices.getPile());
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
     *
     * @param objects
     * @param itemId
     */
    public void saveOrUpdataRealTime(JSONArray objects,Long itemId){
        //循环遍历kafka数据
        for(int i = 0; i < objects.size(); i++){
            JSONObject jsonObject1 = JSONObject.parseObject(objects.get(i).toString());
            String deviceId = jsonObject1.getString("deviceId");
            Integer runStatus = 0;
            SdDevices sdDevices1 = sdDevicesMapper.selectSdDevicesById(deviceId);
            if(DevicesTypeEnum.ZHUO_ZHUAN_CHE_ZHI.getCode() == sdDevices1.getEqType()){
                runStatus = devStatusCync(Long.valueOf(DevicesTypeItemEnum.ZHUO_ZHUAN_CHE_ZHI.getCode()),jsonObject1.getInteger("runStatus"));
                itemId = Long.valueOf(DevicesTypeItemEnum.ZHUO_ZHUAN_CHE_ZHI.getCode());
            }else if(DevicesTypeEnum.JIAO_TONG_XIN_HAO_DENG.getCode() == sdDevices1.getEqType()){
                runStatus = devStatusCync(Long.valueOf(DevicesTypeItemEnum.XIN_HAO_DENG.getCode()),jsonObject1.getInteger("runStatus"));
                itemId = Long.valueOf(DevicesTypeItemEnum.XIN_HAO_DENG.getCode());
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
            JSONObject jsonObjectDev = devReaStatus(sdDevices1 == null ? sdDeviceTypeItemMapper.selectSdDeviceTypeItemById(itemId).getDeviceTypeId() : sdDevices1.getEqType(), objectDev);
            //将设备运行状态上传至高速云
            kafkaTemplate.send("wq_devStatusTopic",jsonObjectDev.toString());
        }
    }

    /**
     * 新增or更新co/vi数据
     *
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
            if (numberCo == 0) {
                //新增数据
                SdDeviceData deviceData = setDeviceData(deviceId, co, coId);
                deviceData.setCreateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.insertSdDeviceData(deviceData);
            } else {
                //更新数据
                SdDeviceData deviceData = setDeviceData(deviceId, co, coId);
                deviceData.setUpdateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.updateKafkaDeviceData(deviceData);
            }
            //将co设备数据存入历史记录表
            setDeviceDataRecord(deviceId,co,coId);
            //校验数据库是否存在
            int numberVi = checkDeviceData(deviceId, viId);
            if (numberVi == 0) {
                //新增数据
                SdDeviceData deviceData = setDeviceData(deviceId, vi, viId);
                deviceData.setCreateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.insertSdDeviceData(deviceData);
            } else {
                //更新数据
                SdDeviceData deviceData = setDeviceData(deviceId, vi, viId);
                deviceData.setUpdateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.updateKafkaDeviceData(deviceData);
            }
            //将vi设备数据存入历史记录表
            setDeviceDataRecord(deviceId,vi,viId);
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
     *
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
            String windDirection = WindDirectionUtil.windDirectionSwitch(jsonObject1.getFloat("windDirection"));
            //校验数据库是否存在
            int numberFs = checkDeviceData(deviceId, fsId);
            if (numberFs == 0) {
                //新增数据
                SdDeviceData deviceData = setDeviceData(deviceId, windSpeed, fsId);
                deviceData.setCreateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.insertSdDeviceData(deviceData);
            } else {
                //更新数据
                SdDeviceData deviceData = setDeviceData(deviceId, windSpeed, fsId);
                deviceData.setUpdateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.updateKafkaDeviceData(deviceData);
            }
            //将风速数据存入历史记录表
            setDeviceDataRecord(deviceId,windSpeed,fsId);
            //校验数据库是否存在
            int numberFx = checkDeviceData(deviceId, fxId);
            if (numberFx == 0) {
                //新增数据
                SdDeviceData deviceData = setDeviceData(deviceId, windDirection, fxId);
                deviceData.setCreateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.insertSdDeviceData(deviceData);
            } else {
                //更新数据
                SdDeviceData deviceData = setDeviceData(deviceId, windDirection, fxId);
                deviceData.setUpdateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.updateKafkaDeviceData(deviceData);
            }
            //将风向数据存入历史记录表
            setDeviceDataRecord(deviceId,windDirection,fxId);
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
     *
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
            if (number == 0) {
                //新增数据
                SdDeviceData deviceData = setDeviceData(deviceId, illuminance, itemId);
                deviceData.setCreateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.insertSdDeviceData(deviceData);
            } else {
                //更新数据
                SdDeviceData deviceData = setDeviceData(deviceId, illuminance, itemId);
                deviceData.setUpdateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.updateKafkaDeviceData(deviceData);
            }
            //将洞内亮度数据存入历史记录表
            setDeviceDataRecord(deviceId,illuminance,itemId);
        }
    }

    /**
     * 新增or更新洞外亮度数据
     *
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
            if (number == 0) {
                //新增数据
                SdDeviceData deviceData = setDeviceData(deviceId, brightness, itemId);
                deviceData.setCreateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.insertSdDeviceData(deviceData);
            } else {
                //更新数据
                SdDeviceData deviceData = setDeviceData(deviceId, brightness, itemId);
                deviceData.setUpdateTime(DateUtils.getNowDate());
                sdDeviceDataMapper.updateKafkaDeviceData(deviceData);
            }
            //将洞外亮度数据存入历史记录表
            setDeviceDataRecord(deviceId,brightness,itemId);
        }
    }

    /**
     * 新增远传压力值数据
     * @param objects
     * @param itemId
     */
    public void savePressureInstrument(JSONArray objects,Long itemId){
        //循环遍历kafka数据
        for(int i = 0; i < objects.size(); i++){
            JSONObject jsonObject1 = JSONObject.parseObject(objects.get(i).toString());
            String deviceId = jsonObject1.getString("deviceId");
            String pressure = jsonObject1.getString("pressure");
            //新增远传压力值数据
            SdDeviceData deviceData = setDeviceData(deviceId, pressure, itemId);
            deviceData.setCreateTime(DateUtils.getNowDate());
            sdDeviceDataMapper.insertSdDeviceData(deviceData);
        }
    }

    /**
     * 新增or更新风机安全检测仪数据
     * @param objects
     * @param zhenSuDu
     * @param zhenFuDu
     * @param chenJiang
     * @param qingXie
     * @param zhenGaoJing
     * @param chenQingGaoJing
     */
    public void saveOrUpdateFanSafeMonitor(JSONArray objects,Long zhenSuDu,
                                           Long zhenFuDu, Long chenJiang, Long qingXie,
                                           Long zhenGaoJing, Long chenQingGaoJing){
        //循环遍历kafka数据
        for(int i = 0; i < objects.size(); i++){
            JSONObject jsonObject1 = JSONObject.parseObject(objects.get(i).toString());
            String deviceId = jsonObject1.getString("deviceId");
            //振动速度值
            String shakeSpeed = jsonObject1.getString("shakeSpeed");
            //振动幅度值
            String amplitude = jsonObject1.getString("amplitude");
            //沉降值
            String subside = jsonObject1.getString("subside");
            //倾斜值
            String slope = jsonObject1.getString("slope");
            //振动告警
            String shakeAlaram = jsonObject1.getString("shakeAlaram");
            //沉降倾斜告警
            String subsideSlopeAlaram = jsonObject1.getString("subsideSlopeAlaram");
            //校验数据库是否存在
            //振动速度值
            int zhenSuDuNum = checkDeviceData(deviceId, zhenSuDu);
            int zhenFuDuNum = checkDeviceData(deviceId, zhenFuDu);
            int chenJiangNum = checkDeviceData(deviceId, chenJiang);
            int qingXieNum = checkDeviceData(deviceId, qingXie);
            int zhenGaoJingNum = checkDeviceData(deviceId, zhenGaoJing);
            int chenQingGaoJingNum = checkDeviceData(deviceId, chenQingGaoJing);

            //新增or更新远传压力值数据
            //振动速度值
            saveOrUpdateFanSafe(deviceId,shakeSpeed,zhenSuDu,zhenSuDuNum);
            //振动幅度值
            saveOrUpdateFanSafe(deviceId,amplitude,zhenFuDu,zhenFuDuNum);
            //沉降值
            saveOrUpdateFanSafe(deviceId,subside,chenJiang,chenJiangNum);
            //倾斜值
            saveOrUpdateFanSafe(deviceId,slope,qingXie,qingXieNum);
            //振动告警
            saveOrUpdateFanSafe(deviceId,shakeAlaram,zhenGaoJing,zhenGaoJingNum);
            //沉降倾斜告警
            saveOrUpdateFanSafe(deviceId,subsideSlopeAlaram,chenQingGaoJing,chenQingGaoJingNum);
        }
    }

    /**
     * 新增微波车检信息
     *
     * @param objects
     * @param itemId
     */
    public void saveReceiveVdBizAttr(JSONArray objects,Long itemId){
        //循环遍历kafka数据
        for(int i = 0; i < objects.size(); i++){
            //解析数据
            JSONObject jsonObject1 = JSONObject.parseObject(objects.get(i).toString());
            //新增微波车检
            SdMicrowavePeriodicStatistics statistics = setVdBizAttrData(jsonObject1);
            statisticsMapper.insertSdMicrowavePeriodicStatistics(statistics);
        }
    }

    /**
     * 赋值设备实时数据
     *
     * @param deviceId
     * @param data
     * @param itemId
     * @return
     */
    public SdDeviceData setDeviceData(String deviceId,String data,Long itemId){
        SdDeviceData deviceData = new SdDeviceData();
        deviceData.setDeviceId(deviceId);
        deviceData.setItemId(itemId);
        deviceData.setData(data);
        return deviceData;
    }

    /**
     * 储存设备数据历史记录表
     *
     * @param deviceId
     * @param data
     * @param itemId
     */
    public void setDeviceDataRecord(String deviceId,String data,Long itemId){
        SdDeviceDataRecord record = new SdDeviceDataRecord();
        record.setDeviceId(deviceId);
        record.setData(data);
        record.setItemId(itemId);
        record.setCreateTime(DateUtils.getNowDate());
        //获取bean
        SdDeviceDataRecordMapper bean = SpringUtils.getBean(SdDeviceDataRecordMapper.class);
        //将数据存入历史记录表
        bean.insertSdDeviceDataRecord(record);
    }

    /**
     * 赋值微波车检实体类
     *
     * @param jsonObject1
     * @return
     */
    public SdMicrowavePeriodicStatistics setVdBizAttrData(JSONObject jsonObject1){
        String deviceId = jsonObject1.getString("deviceId");//设备id
        Long laneNum = jsonObject1.getLong("laneNum");//车道
        String direction = jsonObject1.getString("direction");//方向
        String flow = jsonObject1.getString("flow");//车流量总数
        String flowS = jsonObject1.getString("flowS");//小型车交通量
        String flowM = jsonObject1.getString("flowM");//中型车交通量
        String flowL = jsonObject1.getString("flowL");//大型车交通量
        String flowSpace = jsonObject1.getString("flowSpace");//车间距
        String speed = jsonObject1.getString("speed");//平均速度
        String speedS = jsonObject1.getString("speedS");//平均速度:小型车
        String speedM = jsonObject1.getString("speedM");//平均速度:中型车
        String speedL = jsonObject1.getString("speedL");//平均速度:小大型车
        String occupancy = jsonObject1.getString("occupancy");//平均占有率
        String carLength = jsonObject1.getString("carLength");//平均车长
        String collectTime = jsonObject1.getString("collectTime");//采集时间
        SdMicrowavePeriodicStatistics statistics = new SdMicrowavePeriodicStatistics();
        statistics.setTunnelId(TunnelEnum.HANG_SHAN_DONG.getCode());
        statistics.setDeviceId(deviceId);
        statistics.setLaneNo(laneNum);
        statistics.setAvgSpeed(speed);
        statistics.setSmallVehicleNum(flowS);
        statistics.setMidVehicleNum(flowM);
        statistics.setHeavyVehicleNum(flowL);
        statistics.setAvgLength(carLength);
        statistics.setAvgHeadway(flowSpace);
        statistics.setAvgOccupancy(occupancy);
        statistics.setTrafficFlowTotal(flow);
        statistics.setEqDirection(direction);
        statistics.setSmallVehicleSpeed(speedS);
        statistics.setMidVehicleSpeed(speedM);
        statistics.setHeavyVehicleSpeed(speedL);
        statistics.setCreateTime(DateUtils.parseDate(collectTime));
        return statistics;
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

    /**
     * 校验实时数据是否存在
     *
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
     *
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
                return 5;
            }
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
        if(DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode() == itemId){
            if(runStatus == 0){
                return 4;
            }
        }
        if(DevicesTypeItemEnum.ZHUO_ZHUAN_CHE_ZHI.getCode() == itemId){
            if(runStatus == 4){
                return 5;
            }
            if(runStatus == 0){
                return 4;
            }
        }
        if(DevicesTypeItemEnum.JUAN_LIAN_MEN.getCode() == itemId){
            if(runStatus == 0){
                return 3;
            }
        }
        return runStatus;
    }

    /**
     * 实时保存风机安全检测仪数据
     *
     * @param deviceId
     * @param data
     * @param itemId
     * @param num
     */
    public void saveOrUpdateFanSafe(String deviceId,String data, Long itemId, int num){
        if(num == 0){
            SdDeviceData deviceData = setDeviceData(deviceId, data, itemId);
            deviceData.setCreateTime(DateUtils.getNowDate());
            sdDeviceDataMapper.insertSdDeviceData(deviceData);
        }else {
            SdDeviceData deviceData = setDeviceData(deviceId, data, itemId);
            deviceData.setCreateTime(DateUtils.getNowDate());
            sdDeviceDataMapper.updateKafkaDeviceData(deviceData);
        }
    }

    /**
     * 事件数据接收
     */
    public void joinEvent(JSONObject jsonObject) {
        MergeRhyEventTypeEnum mergeRhyEventTypeEnum = MergeRhyEventTypeEnum.getMergeEvent(jsonObject.getString("mergeCode"));
        if(mergeRhyEventTypeEnum == null){
            return;
        }
        Long eventTypeId = mergeRhyEventTypeEnum.getTypeId();
        SdEvent event = new SdEvent();
        Long eventId = jsonObject.getLong("eventId");
        event.setId(eventId);
        event.setDirection(jsonObject.getString("direction"));
        event.setEventLongitude(jsonObject.getString("eventLongitude"));
        event.setEventLatitude(jsonObject.getString("eventLatitude"));
        event.setEventTypeId(eventTypeId);
        String stakeNum = jsonObject.getString("eventStakeNo");
        if(StrUtil.isNotBlank(stakeNum)){
            event.setStakeNum(jsonObject.getString("eventStakeNo"));
        }
        event.setEventSource(jsonObject.getString("eventSource"));
        event.setTunnelId(TunnelEnum.HANG_SHAN_DONG.getCode());
        event.setEventDescription(jsonObject.getString("eventDescribe"));
        event.setEventTime(DateUtils.parseDate(jsonObject.getString("foundTime")));
        event.setVideoUrl(jsonObject.getString("eventVideoUrl"));
        event.setEndTime(jsonObject.getString("completeTime"));
        event.setLaneNo(jsonObject.getString("carLane"));
        //rhy 事件状态,1:待复核; 2:处置中; 3:已处置; 4:已确认; 5:已挂起; 6:误报; 7:关联
        //zc  状态             0：处理中 1：已处理           2:忽略 3：未处理
        Integer eventStatus = jsonObject.getInteger("eventStatus");
        String ownStatus = "";
        switch (eventStatus){
            case 2 : ownStatus = "0"; break;
            case 3 : ownStatus = "1"; break;
            case 5 : ownStatus = "2"; break;
            default: ownStatus = "3"; break;
        }
        event.setEventState(ownStatus);
        //所有事件类型Map
        Map<Long,String> eventTypeMap = sdEventTypeService.getEventTypeMap();
        //所有隧道Map
        Map<String,String> tunnelMap = sdTunnelsService.getTunnelNameMap();
        event.setEventTitle(sdEventService.getDefaultEventTitle(event,tunnelMap,eventTypeMap));
        event.setEventImgUrl(jsonObject.getString("eventImgUrl"));
        event.setStartTime(DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS));

        String[] imgList = jsonObject.getString("eventImgUrl").split(";");
        List<SdTrafficImage> imageList = new ArrayList<>();
        for(String img:imgList){
            SdTrafficImage image = new SdTrafficImage();
            image.setImgUrl(img);
            image.setBusinessId(eventId.toString());
            imageList.add(image);
        }
        SpringUtil.getBean(SdTrafficImageMapper.class).brachInsertFaultIconFile(imageList);
        int effectiveRows = 0;
        if(sdEventService.selectSdEventById(eventId) != null){
            effectiveRows = sdEventService.updateSdEvent(event);
        }else{
            effectiveRows = sdEventService.insertSdEvent(event);
            //新增后再查询数据库，返回给前端事件图标等字段
            SdEvent sdEvent = new SdEvent();
            sdEvent.setId(eventId);
            List<SdEvent> sdEventList = sdEventService.querySdEventList(sdEvent);
            //新增事件后推送前端
            JSONObject object = new JSONObject();
            object.put("sdEventList", sdEventList);
            WebSocketService.broadcast("sdEventList",object.toString());
        }
        //推送物联中台，事件类型过滤
        if(effectiveRows > 0 && mergeRhyEventTypeEnum.getPushOrNot() == 1){
            //如果是未处理状态改为处理中
            if(event.getEventState().equals(EventStateEnum.unprocessed.getCode())){
                event.setEventState(EventStateEnum.processing.getCode());
            }
            radarEventServiceImpl.sendDataToOtherSystem(null,event);
//                jsonObject.clear();
//                jsonObject.put("event", event);
//                jsonObject.put("devNo", "S00063700001980001");
//                jsonObject.put("timeStamp", DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss.SSS"));
            //kafkaTemplate.send("wq_tunnelEvent", jsonObject.toString());
        }
    }

    /**
     * 处理火灾报警信息
     *
     * @param jsonArray
     */
    public void fireDataHandle(JSONArray jsonArray){
        for(int i = 0; i < jsonArray.size(); i++){
            //解析数据
            JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(i).toString());
            //设备id
            String deviceId = jsonObject.getString("deviceId");
            String fireData = jsonObject.getString("alertInfo");
            //判断是否为空或类型不对应
            if ("".equals(fireData) || fireData == null) {
                return;
            }
            if (!fireData.contains("火警") && !fireData.contains("模块或探头故障") && !fireData.contains("模块或探头恢复")
                    && !fireData.contains("全部声光启动") && !fireData.contains("全部声光停止")) {
                return;
            }
            if (fireData.contains(":")) {
                //查询设备信息
                SdDevices sdDevices = sdDevicesMapper.selectSdDevicesById(deviceId);
                //查询设备类型数据项表
                SdDeviceTypeItem sdDeviceTypeItem = new SdDeviceTypeItem();
                sdDeviceTypeItem.setDeviceTypeId(sdDevices.getEqType());
                SdDeviceTypeItem deviceTypeItem = sdDeviceTypeItemMapper.selectSdDeviceTypeItemList(sdDeviceTypeItem).get(0);
                //查询实时数据表
                SdDeviceData sdDeviceData = new SdDeviceData();
                sdDeviceData.setDeviceId(deviceId);
                sdDeviceData.setItemId(deviceTypeItem.getId());
                List<SdDeviceData> sdDeviceDataList = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
                //获取火灾报警事件类型
                String alarmType = fireData.substring(0, fireData.indexOf(":"));
                log.info("alarmType:" + alarmType);
                //获取火灾报警时间
                String warningTime = fireData.substring(fireData.length() - 19);
                //事件类型为模块或探头恢复、全部声光停止时清除设备报警状态
                if (fireData.contains("恢复") || fireData.contains("全部声光停止")) {
                    //复位清除设备报警状态
                    //将状态更新为正常
                    for(SdDeviceData item : sdDeviceDataList){
                        //0：正常 1：报警
                        item.setData("0");
                        sdDeviceDataMapper.updateSdDeviceData(item);
                        sendData.pushDevicesDataNowTime(sdDeviceData);
                    }
                    //复位清除预警
                    SdEvent sdEvent = new SdEvent();
                    sdEvent.setEventTypeId(20L);
                    List<SdEvent> sdEvents = sdEventMapper.selectSdEventList(sdEvent);
                    for (SdEvent item : sdEvents) {
                        item.setEventState("1");
                        item.setEndTime(new Date().toString());
                        sdEventMapper.updateSdEvent(item);
                    }
                }
                //事件相关的设备要把数据更新到device_data中
                if (sdDeviceDataList.size() == 0) {
                    sdDeviceData.setData("1");
                    sdDeviceData.setCreateTime(new Date());
                    sdDeviceData.setItemId(deviceTypeItem.getId());
                    sdDeviceDataMapper.insertSdDeviceData(sdDeviceData);
                    sendData.pushDevicesDataNowTime(sdDeviceData);
                } else {
                    SdDeviceData devData = sdDeviceDataList.get(0);
                    devData.setUpdateTime(new Date());
                    devData.setData("1");
                    sdDeviceDataMapper.updateSdDeviceData(devData);
                    sendData.pushDevicesDataNowTime(devData);
                }
            } else {
                log.error("当前报文格式异常，请检查设备！");
                return;
            }
        }
    }

    /**
     * 新增车道统计数据
     *
     * @param jsonArray
     */
    public void saveAnalysisCrosslane(JSONArray jsonArray){
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(i).toString());
            //保存车道统计数据
            analysisAnalysisCrosslane(jsonObject);
        }
    }

    /**
     * 新增路段统计数据
     *
     * @param jsonArray
     */
    public void saveAnalysisRid(JSONArray jsonArray){
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(i).toString());
            //保存路段统计数据
            analysisAnalysisRid(jsonObject);
        }
    }

    /**
     * 新增路段车辆行驶记录数据
     *
     * @param jsonArray
     */
    public void saveRidTravel(JSONArray jsonArray){
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(i).toString());
            //保存路段车辆行驶记录数据
            analysisRidTravel(jsonObject);
        }
    }

    /**
     * 新增车辆快照数据
     *
     * @param jsonArray
     */
    public void saveAnalysisCarsnap(JSONArray jsonArray){
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(i).toString());
            //保存车辆快照数据
            analysisAnalysisCarsnap(jsonObject);
        }
    }

    /**
     * 解析保存车道统计数据
     *
     * @param jsonObject
     * @return
     */
    public void analysisAnalysisCrosslane(JSONObject jsonObject){
        SdLaneStatistics sdLaneStatistics = new SdLaneStatistics();
        sdLaneStatistics.setTunnelId(TunnelEnum.HANG_SHAN_DONG.getCode());
        Long lane = jsonObject.getLong("laneNo");
        Long laneNo = 0L;
        if(lane == 11L){
            laneNo = 1L;
        }else if(lane == 12L){
            laneNo = 2L;
        }else {
            laneNo = 3L;
        }
        sdLaneStatistics.setLaneNo(laneNo);
        sdLaneStatistics.setSpeed(jsonObject.getString("speed"));
        sdLaneStatistics.setTimeOccupy(jsonObject.getString("timeOccupy"));
        sdLaneStatistics.setSpaceOccupy(jsonObject.getString("spaceOccupy"));
        sdLaneStatistics.setGap(jsonObject.getString("gap"));
        sdLaneStatistics.setGapTime(jsonObject.getString("gapTime"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换
        Date time = DateUtils.parseDate(sdf.format(new Date(jsonObject.getLong("time"))));
        sdLaneStatistics.setTime(time);
        sdLaneStatistics.setFlowSmall(jsonObject.getLong("flowSmall"));
        sdLaneStatistics.setFlowMedium(jsonObject.getLong("flowMedium"));
        sdLaneStatistics.setFlowLarge(jsonObject.getLong("flowLarge"));
        sdLaneStatistics.setFlowxLarge(jsonObject.getLong("flowXLarge"));
        sdLaneStatistics.setCars(jsonObject.getLong("cars"));
        sdLaneStatistics.setStartTime(DateUtils.parseDate(jsonObject.getString("startTime")));
        sdLaneStatistics.setEndTime(DateUtils.parseDate(jsonObject.getString("endTime")));
        sdLaneStatistics.setRoadDir(jsonObject.getString("roadDir"));
        sdLaneStatistics.setCreateTime(DateUtils.getNowDate());
        SdLaneStatisticsMapper sdLaneStatisticsMapper = SpringUtils.getBean(SdLaneStatisticsMapper.class);
        sdLaneStatisticsMapper.insertSdLaneStatistics(sdLaneStatistics);
    }

    /**
     * 解析保存路段统计数据
     *
     * @param jsonObject
     */
    public void analysisAnalysisRid(JSONObject jsonObject){
        SdRoadSectionStatistics statistics = new SdRoadSectionStatistics();
        statistics.setTunnelId(TunnelEnum.HANG_SHAN_DONG.getCode());
        statistics.setSpeed(jsonObject.getString("speed"));
        statistics.setTimeOccupy(jsonObject.getString("timeOccupy"));
        statistics.setSpaceOccupy(jsonObject.getString("spaceOccupy"));
        statistics.setGap(jsonObject.getString("gap"));
        statistics.setGapTime(jsonObject.getString("gapTime"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换
        Date time = DateUtils.parseDate(sdf.format(new Date(jsonObject.getLong("time"))));
        statistics.setTime(time);
        statistics.setInFlow(jsonObject.getString("inFlow"));
        statistics.setOutFlow(jsonObject.getString("outFlow"));
        statistics.setCongestionIndex(jsonObject.getString("congestionIndex"));
        statistics.setRoadDir(jsonObject.getString("roadDir"));
        statistics.setSaturationVc(jsonObject.getString("saturationVC"));
        statistics.setStartTime(DateUtils.parseDate(jsonObject.getString("startTime")));
        statistics.setEndTime(DateUtils.parseDate(jsonObject.getString("endTime")));
        statistics.setCreateTime(DateUtils.getNowDate());
        statistics.setCars(jsonObject.getString("cars"));
        SdRoadSectionStatisticsMapper sectionStatisticsMapper = SpringUtils.getBean(SdRoadSectionStatisticsMapper.class);
        sectionStatisticsMapper.insertSdRoadSectionStatistics(statistics);
        setRedis(statistics);
    }

    /**
     * 解析保存路段车辆行驶记录
     *
     * @param jsonObject
     */
    public void analysisRidTravel(JSONObject jsonObject){
        SdVehicleDriving sdVehicleDriving = new SdVehicleDriving();
        sdVehicleDriving.setTunnelId(TunnelEnum.HANG_SHAN_DONG.getCode());
        sdVehicleDriving.setTrackId(jsonObject.getLong("trackID"));
        sdVehicleDriving.setPlateColor(getPlateColor(jsonObject.getString("plateColor")));
        sdVehicleDriving.setPlateNumber(jsonObject.getString("plateNumber"));
        sdVehicleDriving.setObjectType(jsonObject.getString("objectType"));
        sdVehicleDriving.setVehicleType(getVehicleType(jsonObject.getString("vehicleType")));
        sdVehicleDriving.setVehicleColor(getVehicleColor(jsonObject.getString("vehicleColor")));
        sdVehicleDriving.setSpeed(jsonObject.getString("speed"));
        sdVehicleDriving.setTravelType(jsonObject.getString("travelType"));
        sdVehicleDriving.setRoadDir(jsonObject.getString("roadDir"));
        sdVehicleDriving.setStartTime(DateUtils.parseDate(jsonObject.getString("startTime")));
        sdVehicleDriving.setCreateTime(DateUtils.getNowDate());
        SdVehicleDrivingMapper drivingMapper = SpringUtils.getBean(SdVehicleDrivingMapper.class);
        drivingMapper.insertSdVehicleDriving(sdVehicleDriving);
    }

    /**
     * 解析保存车辆快照数据
     *
     * @param jsonObject
     */
    public void analysisAnalysisCarsnap(JSONObject jsonObject){
        SdRadarDetectData sdRadarDetectData = new SdRadarDetectData();
        sdRadarDetectData.setTunnelId(TunnelEnum.HANG_SHAN_DONG.getCode());
        sdRadarDetectData.setRecordSerialNumber(jsonObject.getString("trackID"));
        sdRadarDetectData.setObjectType(jsonObject.getString("objectType"));
        sdRadarDetectData.setVehicleType(getVehicleType(jsonObject.getString("vehicleType")));
        sdRadarDetectData.setVehicleColor(getVehicleColor(jsonObject.getString("vehicleColor")));
        sdRadarDetectData.setVehicleLicense(jsonObject.getString("plateNumber"));
        sdRadarDetectData.setLatitude(jsonObject.getString("lat"));
        sdRadarDetectData.setLongitude(jsonObject.getString("lng"));
        sdRadarDetectData.setCourseAngle(jsonObject.getString("rriveAngle"));
        sdRadarDetectData.setSpeed(jsonObject.getString("speed"));
        sdRadarDetectData.setDistance((950-jsonObject.getInteger("ridOffset"))+"");
        String matchLane = jsonObject.getString("matchLane");
        String laneNo = matchLane.substring(matchLane.length() - 1, matchLane.length());
        sdRadarDetectData.setLaneNum(laneNo);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换
        Date time = DateUtils.parseDate(sdf.format(new Date(jsonObject.getLong("time"))));
        sdRadarDetectData.setDetectTime(time);
        sdRadarDetectData.setRoadDir(jsonObject.getString("roadDir"));
        sdRadarDetectData.setVehicleId(jsonObject.getString("uniqId"));
        SdRadarDetectDataMapper detectDataMapper = SpringUtils.getBean(SdRadarDetectDataMapper.class);
        detectDataMapper.insertSdRadarDetectData(sdRadarDetectData);
        if(StringUtils.isNotEmpty(sdRadarDetectData.getVehicleLicense()) && StringUtils.isNotNull(sdRadarDetectData.getVehicleLicense())){
            setCarsnapRedis(sdRadarDetectData);
        }
    }

    /**
     * 将车辆路段信息存入redis
     * @param statistics
     */
    public void setRedis(SdRoadSectionStatistics statistics){
        Map<String, Object> map = new HashMap<>();
        map.put("tunnelId",TunnelEnum.HANG_SHAN_DONG.getCode());
        map.put("roadDir",statistics.getRoadDir());
        map.put("speed",statistics.getSpeed());
        map.put("cars",statistics.getCars());
        //redis-key命名规则  固定字段tunnelVehicleTotal:隧道Id:方向
        redisCache.setCacheObject("tunnelVehicleTotal:"+statistics.getTunnelId()+":"+statistics.getRoadDir(),map);
    }

    /**
     * 将车辆快照存入redis
     *
     * @param sdRadarDetectData
     */
    public void setCarsnapRedis(SdRadarDetectData sdRadarDetectData){
        Map<String, Object> map = new HashMap<>();
        map.put("tunnelId",TunnelEnum.HANG_SHAN_DONG.getCode());
        map.put("roadDir",sdRadarDetectData.getRoadDir());
        map.put("speed",sdRadarDetectData.getSpeed());
        map.put("laneNo",sdRadarDetectData.getLaneNum());
        map.put("vehicleType",sdRadarDetectData.getVehicleType());
        map.put("lat",sdRadarDetectData.getLatitude());
        map.put("lng",sdRadarDetectData.getLongitude());
        map.put("distance",sdRadarDetectData.getDistance());
        map.put("vehicleLicense",sdRadarDetectData.getVehicleLicense());
        //redis-key命名规则，固定字段vehicleSnap:隧道id:车牌号
        redisCache.setCacheObject("vehicleSnap:" + sdRadarDetectData.getTunnelId() + ":" + sdRadarDetectData.getVehicleLicense(),map,5, TimeUnit.MINUTES);
    }

    /**
     * 对应车辆类型
     *
     * @param vehicleType
     * @return
     */
    public String getVehicleType(String vehicleType){
        String type = "";
        switch (vehicleType){
            case "1":{type = "24"; break;}
            case "2":{type = "17"; break;}
            case "3":{type = "13"; break;}
            case "4":{type = "16"; break;}
            case "5":{type = "14"; break;}
            case "6":{type = "4"; break;}
            case "7":{type = "25"; break;}
            case "9":{type = "4"; break;}
            case "10":{type = "12"; break;}
            case "11":{type = "19"; break;}
            case "12":{type = "7"; break;}
            case "13":{type = "18"; break;}
            case "14":{type = "27"; break;}
            case "15":{type = "4"; break;}
            case "16":{type = "28"; break;}
            case "17":{type = "4"; break;}
            case "18":{type = "4"; break;}
            case "19":{type = "4"; break;}
            case "20":{type = "4"; break;}
            case "21":{type = "4"; break;}
            case "22":{type = "4"; break;}
            case "23":{type = "4"; break;}
            case "24":{type = "4"; break;}
            case "25":{type = "28"; break;}
            default:type = vehicleType;
        }
        return type;
    }

    /**
     * 对应车辆颜色
     *
     * @param vehicleColor
     * @return
     */
    public String getVehicleColor(String vehicleColor){
        String color = "";
        switch (vehicleColor){
            case "0":{color = "2"; break;}
            case "1":{color = "3"; break;}
            case "2":{color = "6"; break;}
            case "3":{color = "12"; break;}
            case "5":{color = "9"; break;}
            case "6":{color = "5"; break;}
            case "7":{color = "8"; break;}
            case "8":{color = "1"; break;}
            case "9":{color = "99"; break;}
            case "11":{color = "7"; break;}
            case "12":{color = "99"; break;}
            case "13":{color = "11"; break;}
            case "14":{color = "99"; break;}
            case "15":{color = "13"; break;}
            default:color = vehicleColor;
        }
        return color;
    }

    /**
     * 对应车牌颜色
     *
     * @param plateColor
     * @return
     */
    public String getPlateColor(String plateColor){
        String color = "";
        switch (plateColor){
            case "0":{color = "3"; break;}
            case "1":{color = "9"; break;}
            case "2":{color = "1"; break;}
            case "3":{color = "9"; break;}
            case "4":{color = "12"; break;}
            case "5":{color = "11"; break;}
            case "6":{color = "0"; break;}
            case "7":{color = "9"; break;}
            case "8":{color = "2"; break;}
            case "10":{color = "9"; break;}
            case "11":{color = "9"; break;}
            case "12":{color = "9"; break;}
            case "13":{color = "9"; break;}
            case "14":{color = "9"; break;}
            case "15":{color = "9"; break;}
            case "20":{color = "4"; break;}
            case "21":{color = "5"; break;}
            case "99":{color = "9"; break;}
            default:color = plateColor;
        }
        return color;
    }
}
