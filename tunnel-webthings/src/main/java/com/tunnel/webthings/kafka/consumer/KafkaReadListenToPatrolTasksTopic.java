package com.tunnel.webthings.kafka.consumer;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.datacenter.domain.dataReport.OptType;
import com.tunnel.business.datacenter.domain.enumeration.PlatformAuthEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceDataRecord;
import com.tunnel.business.domain.electromechanicalPatrol.SdFaultList;
import com.tunnel.business.domain.electromechanicalPatrol.SdPatrolList;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskList;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskOpt;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import com.tunnel.business.mapper.electromechanicalPatrol.SdFaultListMapper;
import com.tunnel.business.mapper.electromechanicalPatrol.SdPatrolListMapper;
import com.tunnel.business.mapper.electromechanicalPatrol.SdTaskListMapper;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficImageMapper;
import com.tunnel.business.service.electromechanicalPatrol.ISdFaultListService;
import com.tunnel.business.service.electromechanicalPatrol.ISdTaskListService;
import com.tunnel.business.service.electromechanicalPatrol.impl.SdTaskListServiceImpl;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 巡查任务
 * 两级平台任务数据同步
 *
 * @author tjw
 * @date 2023/5/5
 */
@Component
public class KafkaReadListenToPatrolTasksTopic {

    private static final Logger log = LoggerFactory.getLogger(KafkaReadListenToPatrolTasksTopic.class);

    /**
     * 获取当前平台名称
     */
    @Value("${authorize.name}")
    private String authorizeName;

    @Autowired
    private SdTaskListMapper sdTaskListMapper;

    @Autowired
    private ISdTaskListService sdTaskListService;

    @Autowired
    private SdPatrolListMapper sdPatrolListMapper;

    @Autowired
    private SdTrafficImageMapper sdTrafficImageMapper;

    @Autowired
    private SdFaultListMapper sdFaultListMapper;

    @Autowired
    private ISdFaultListService sdFaultListService;


    /**
     * 监听巡查任务数据(pc端新增、修改任务)
     *
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"tunnelTaskList"}, containerFactory = "kafkaTwoContainerFactory")
    public void taskListAccept(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("高速云端监听到平台数据： --> {}",record.value());
        if(PlatformAuthEnum.GSY.getCode().equals(authorizeName)){
            log.info("高速云端监听到平台数据： --> {}",record.value());
            if(record.value()!=null && !"".equals(record.value().toString())){
                JSONObject jsonObject = JSONObject.parseObject(record.value().toString());
                if(jsonObject.get("taskRecord") != null && !"".equals(jsonObject.get("taskRecord"))){
                    //解析任务数据信息记录
                    Object taskDataObject = jsonObject.get("taskRecord");
                    if(jsonObject.get("optType").equals(OptType.DELETE.getCode())){//删除任务数据
                        sdTaskListMapper.deleteSdTaskListByIds(jsonObject.get("taskRecord").toString());
                    }else{//新增\修改任务
                        SdTaskList sdTaskList = JSONUtil.toBean(taskDataObject.toString(),SdTaskList.class);
                        if(sdTaskList!=null){
                            //先判断任务是否存在
                            SdTaskList sdTask = sdTaskListService.selectSdTaskById(sdTaskList.getId());
                            if(sdTask!=null){//更新
                                sdTaskListService.updateGsySdTaskList(sdTaskList);
                            }else{//新增
                                sdTaskListMapper.insertSdTaskList(sdTaskList);
                            }
                        }
                    }

                }
            }
        }
        if(PlatformAuthEnum.GLZ.getCode().equals(authorizeName)){
            log.info("管理站监听到平台数据： --> {}",record.value());
            if(record.value()!=null && !"".equals(record.value().toString())){
                JSONObject jsonObject = JSONObject.parseObject(record.value().toString());
                if(jsonObject.get("taskRecord") != null && !"".equals(jsonObject.get("taskRecord"))){
                    //解析任务数据信息记录
                    Object taskDataObject = jsonObject.get("taskRecord");
                    if(jsonObject.get("optType").equals(OptType.JEISHOU.getCode())){//接收任务
                        sdTaskListMapper.acceptSdTaskList(jsonObject.get("taskRecord").toString(), (Long) jsonObject.get("acceptUser"));
                    }else if(jsonObject.get("optType").equals(OptType.TIJIAO.getCode())){//提交任务
                        SdTaskList sdTaskList = JSONUtil.toBean(taskDataObject.toString(),SdTaskList.class);
                        sdTaskListMapper.saveLocal(sdTaskList);
                    }
                }
            }
        }
        consumer.commitSync();
    }


    /**
     * 监听巡检点数据(pc端新增、修改任务)
     *
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"tunnelTaskPatrol"}, containerFactory = "kafkaTwoContainerFactory")
    public void taskPatrolAccept(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("高速云端监听到平台数据： --> {}",record.value());
        if(PlatformAuthEnum.GSY.getCode().equals(authorizeName)){
            log.info("高速云端监听到平台数据： --> {}",record.value());
            if(record.value()!=null && !"".equals(record.value().toString())){
                JSONObject jsonObject = JSONObject.parseObject(record.value().toString());
                if(jsonObject.get("optType").equals(OptType.DELETE.getCode())){//删除巡检点
                    if(jsonObject.get("taskRecord") != null && !"".equals(jsonObject.get("taskRecord"))){//删除巡检点
                        sdPatrolListMapper.batchDeletePatrolListByTaskIds(jsonObject.get("taskRecord").toString());
                    }
                }else if(jsonObject.get("optType").equals(OptType.PAIDAN.getCode())){
                    //设备巡检点
                    if(jsonObject.get("taskPatrolDeviceRecord") != null && !"".equals(jsonObject.get("taskPatrolDeviceRecord"))){
                        //解析设备巡检点信息记录
                        Object taskPatrolDeviceDataObject = jsonObject.get("taskPatrolDeviceRecord");
                        sdPatrolListMapper.batchInsertPatrol((List<SdPatrolList>) taskPatrolDeviceDataObject);
                    }
                    if(jsonObject.get("taskPatrolFaultRecord") != null && !"".equals(jsonObject.get("taskPatrolFaultRecord"))){
                        //解析故障巡检点信息记录
                        Object taskPatrolFaultDataObject = jsonObject.get("taskPatrolDeviceRecord");
                        sdPatrolListMapper.batchInsertPatrol((List<SdPatrolList>) taskPatrolFaultDataObject);
                    }
                }
            }
        }
        if(PlatformAuthEnum.GLZ.getCode().equals(authorizeName)){
            log.info("高速云端监听到平台数据： --> {}",record.value());
            if(record.value()!=null && !"".equals(record.value().toString())){
                JSONObject jsonObject = JSONObject.parseObject(record.value().toString());
                //解析任务巡检点数据信息记录
                Object taskPatrolDataObject = jsonObject.get("taskPatrol");
                SdPatrolList sdPatrolList = JSONUtil.toBean(taskPatrolDataObject.toString(), SdPatrolList.class);
                //解析巡检点图片信息
                Object taskPatrolImgDataObject = jsonObject.get("taskPatrolImg");
                if(taskPatrolImgDataObject!=null&&((List<SdTrafficImage>) taskPatrolImgDataObject).size()>0){
                    sdTrafficImageMapper.brachInsertFaultIconFile((List<SdTrafficImage>) taskPatrolImgDataObject);
                }
                if(sdPatrolList!=null){
                    sdPatrolListMapper.savePatrol(sdPatrolList);//更新巡检点信息数据
                }
            }
        }
        consumer.commitSync();
    }


    /**
     * 监听操作记录数据(pc端新增、修改任务)
     *  待完善提交、接收任务需向管理站推送
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"tunnelTaskOperation"}, containerFactory = "kafkaTwoContainerFactory")
    public void taskOperationAccept(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("高速云端监听到平台数据： --> {}",record.value());
        if(PlatformAuthEnum.GSY.getCode().equals(authorizeName)){
            log.info("高速云端监听到平台数据： --> {}",record.value());
            if(record.value()!=null && !"".equals(record.value().toString())){
                JSONObject jsonObject = JSONObject.parseObject(record.value().toString());
                if(jsonObject.get("taskOptRecord") != null && !"".equals(jsonObject.get("taskOptRecord"))){
                    //解析任务操作记录数据信息记录
                    Object taskOptDataObject = jsonObject.get("taskOptRecord");
                    SdTaskOpt sdTaskOpt = JSONUtil.toBean(taskOptDataObject.toString(), SdTaskOpt.class);
                    if(sdTaskOpt!=null){
                        sdTaskListMapper.insertTaskOpt(sdTaskOpt);
                    }
                }
            }
        }
        consumer.commitSync();
    }


    /**
     * 监听故障数据(pc端新增、修改故障)
     *
     * @param record
     * @param consumer
     */
    @KafkaListener(topics = {"tunnelFaultList"}, containerFactory = "kafkaTwoContainerFactory")
    public void faultListAccept(ConsumerRecord<String,Object> record, Acknowledgment acknowledgment, Consumer<?,?> consumer){
        log.info("高速云端监听到平台数据： --> {}",record.value());
        if(PlatformAuthEnum.GSY.getCode().equals(authorizeName)){
            log.info("高速云端监听到平台数据： --> {}",record.value());
            if(record.value()!=null && !"".equals(record.value().toString())){
                JSONObject jsonObject = JSONObject.parseObject(record.value().toString());
                if(jsonObject.get("faultRecord") != null && !"".equals(jsonObject.get("faultRecord"))){
                    //解析故障信息记录
                    Object faultDataObject = jsonObject.get("faultRecord");
                    if(jsonObject.get("optType").equals("1")){//新增\修改故障
                        SdFaultList sdFaultList = JSONUtil.toBean(faultDataObject.toString(),SdFaultList.class);
                        if(sdFaultList!=null){//更新
                            //先判断故障是否存在
                            SdFaultList sdFault = sdFaultListService.selectSdFaultById(sdFaultList.getId());
                            if(sdFault!=null){//更新
                                //解析故障图片信息
                                Object faultImgDataObject = jsonObject.get("faultImgRecord");
                                if(faultImgDataObject!=null&&((List<SdTrafficImage>) faultImgDataObject).size()>0){
                                    sdTrafficImageMapper.brachInsertFaultIconFile((List<SdTrafficImage>) faultImgDataObject);
                                }
                                //解析图片删除id信息
                                Object faultImgRemoveDataObject = jsonObject.get("removeIds");
                                if(faultImgRemoveDataObject!=null&&((String[]) faultImgRemoveDataObject).length>0){
                                    sdTrafficImageMapper.deleteFaultIconFileByIds((String[]) faultImgRemoveDataObject);
                                }
                                sdFaultListMapper.updateSdFaultList(sdFaultList);//修改故障信息
                            }else{//新增
                                //解析故障图片信息
                                Object faultImgDataObject = jsonObject.get("faultImgRecord");
                                if(faultImgDataObject!=null&&((List<SdTrafficImage>) faultImgDataObject).size()>0){
                                    sdTrafficImageMapper.brachInsertFaultIconFile((List<SdTrafficImage>) faultImgDataObject);
                                }
                                sdFaultListMapper.insertSdFaultList(sdFaultList);//新增故障信息
                            }
                        }
                    }else{//删除
                        //解析删除故障id信息
                        Object faultRemoveDataObject = jsonObject.get("faultRecord");
                        if(faultRemoveDataObject!=null&&((String[]) faultRemoveDataObject).length>0){
                            int result = sdTrafficImageMapper.deleteFaultIconFile((String[]) faultRemoveDataObject);
                            if(result>0){
                                sdFaultListMapper.deleteSdFaultListByIds((String[]) faultRemoveDataObject);
                            }
                        }
                    }

                }
            }
        }
        if(PlatformAuthEnum.GLZ.getCode().equals(authorizeName)){
            log.info("高速云端监听到平台数据： --> {}",record.value());
            if(record.value()!=null && !"".equals(record.value().toString())){
                JSONObject jsonObject = JSONObject.parseObject(record.value().toString());
                if(jsonObject.get("faultRecord") != null && !"".equals(jsonObject.get("faultRecord"))){
                    //解析故障信息记录
                    Object faultDataObject = jsonObject.get("faultRecord");
                    if(jsonObject.get("optType").equals("2")){//更新故障状态
                        //解析故障id信息
                        Object id = jsonObject.get("faultRecord");
                        Object faultStatue = jsonObject.get("faultStatue");
                        sdFaultListMapper.updateFaultRemoveState(id.toString(),faultStatue.toString());
                    }

                }
            }
        }
        consumer.commitSync();
    }


}
