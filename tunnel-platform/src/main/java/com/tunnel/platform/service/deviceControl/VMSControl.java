package com.tunnel.platform.service.deviceControl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceTypeItem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdJoinPlanStrategy;
import com.tunnel.business.domain.informationBoard.IotBoardTemplate;
import com.tunnel.business.domain.informationBoard.IotBoardTemplateContent;
import com.tunnel.business.domain.informationBoard.SdIotDevice;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.mapper.digitalmodel.RadarEventMapper;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.mapper.event.SdJoinPlanStrategyMapper;
import com.tunnel.business.mapper.informationBoard.IotBoardTemplateContentMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDeviceTypeItemService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.informationBoard.IIotBoardTemplateContentService;
import com.tunnel.business.service.informationBoard.IIotBoardTemplateService;
import com.tunnel.business.service.informationBoard.ISdIotDeviceService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.platform.business.vms.device.DataUtils;
import com.tunnel.platform.business.vms.device.DeviceManagerFactory;
import com.tunnel.platform.controller.informationBoard.BoardController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * describe: 情报板控制类
 *
 * @author zs
 * @date 2023/5/11
 */
@Component
public class VMSControl implements GeneralControlBean {

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdDeviceDataService sdDeviceDataService;

    @Autowired
    private ISdOperationLogService sdOperationLogService;

    @Autowired
    private SdJoinPlanStrategyMapper planStrategyMapper;

    @Autowired
    private IIotBoardTemplateService sdVmsTemplateService;

    @Autowired
    private IIotBoardTemplateContentService sdVmsTemplateContentService;

    @Autowired
    private ISdDeviceTypeItemService sdDeviceTypeItemService;

    @Autowired
    private CommonControlService commonControlService;

    @Autowired
    private ISdIotDeviceService sdIotDeviceService;

    @Autowired
    private SdEventMapper sdEventMapper;

    @Autowired
    private RadarEventMapper radarEventMapper;

    private static final Logger log = LoggerFactory.getLogger(VMSControl.class);

    @Override
    public AjaxResult control(Map<String, Object> map, SdDevices sdDevices) {

        return null;
    }

    /**
     * 设备控制
     *
     * @param map
     * @return
     */
    @Override
    public Integer controlDevices(Map<String, Object> map) {
        int controlState = 0;
        String operationState = "";

        String devId = map.get("devId").toString();
        String state = map.get("state").toString();
//        String controlType = map.get("controlType").toString();
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);

        if (map.get("templateId") == null || map.get("templateId").toString().equals("")) {
            throw new RuntimeException("没有找到需要发布的模板信息");
        }
        Long templateId = Long.parseLong(map.get("templateId").toString());

        //模板内容
        if(map.get("currentId") == null && map.get("type") == null){
            IotBoardTemplateContent templateContent = new IotBoardTemplateContent();
            templateContent.setTemplateId(templateId.toString());
            List<IotBoardTemplateContent> templateContentList = SpringUtils.getBean(IotBoardTemplateContentMapper.class).selectSdVmsTemplateContentList(templateContent);
//            sdOperationLog.setOperationState(templateContentList.size() == 0 ? "" : templateContentList.get(0).getContent());
            operationState = templateContentList.size() == 0 ? "" : templateContentList.get(0).getContent();
        }else {
            SdJoinPlanStrategy planStrategy = new SdJoinPlanStrategy();
            planStrategy.setCurrentId(Long.valueOf(map.get("currentId").toString()));
            planStrategy.setTemplateId(map.get("templateId").toString());
            //0：预案 1：策略
            planStrategy.setType(map.get("type").toString());
            Map<String, Object> sdVmsContent = planStrategyMapper.getTemplateContent(planStrategy);
//            sdOperationLog.setOperationState(sdVmsContent == null ? "" : sdVmsContent.get("content").toString());
            operationState = sdVmsContent == null ? "" : sdVmsContent.get("content").toString();
        }
        System.out.println(devId + "情报板模板内容：" + operationState);
        //控制情报板
        controlState = controlInformationBoard(controlState, templateId, sdDevices, state, map);
        //日志
        addOperationLog(map,sdDevices,controlState,operationState);

        return controlState;
    }

//    /**
//     * 模拟控制方法
//     *
//     * @param map
//     * @param sdDevices
//     * @return
//     */
////    @Override
//    public Integer analogControl(Map<String, Object> map, SdDevices sdDevices) {
//        return null;
//    }

    /**
     * 模拟控制方法
     *
     * @param map
     * @param sdDevices
     * @param state
     * @return
     */
    public Integer analogControl(Map<String, Object> map, SdDevices sdDevices,String state) {

        //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
        sdDevices.setEqStatus("1");
        sdDevices.setEqStatusTime(new Date());
        sdDevicesService.updateSdDevices(sdDevices);
        SdDeviceTypeItem sdDeviceTypeItem = new SdDeviceTypeItem();
        sdDeviceTypeItem.setDeviceTypeId(sdDevices.getEqType());
        List<SdDeviceTypeItem> sdDeviceTypeItems = sdDeviceTypeItemService.selectSdDeviceTypeItemList(sdDeviceTypeItem);
        if (sdDeviceTypeItems.size() == 0) {
            throw new RuntimeException("当前设备没有设备类型数据项数据，请添加后重试！");
        }
        SdDeviceTypeItem typeItem = sdDeviceTypeItems.get(0);
        sdDeviceDataService.updateDeviceData(sdDevices, state, typeItem.getId());
        Integer controlState = 1;
        return controlState;
    }



    public void addOperationLog(Map<String, Object> map,SdDevices sdDevices,Integer controlState,String operationState){

        String devId = map.get("devId").toString();
        String state = map.get("state").toString();
        String controlType = map.get("controlType").toString();

        //添加操作记录
        SdOperationLog sdOperationLog = new SdOperationLog();

        //部份设备未接入，无法正确获取设备控制结果，默认失败
        sdOperationLog.setState("0");

        //获取当前设备状态
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(devId);
        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);

        sdOperationLog.setEqTypeId(sdDevices.getEqType());
        sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
        sdOperationLog.setEqId(sdDevices.getEqId());
        sdOperationLog.setOperationState(state);
        sdOperationLog.setControlType(controlType);
        if (null != map.get("operIp")) {
            sdOperationLog.setOperIp(map.get("operIp").toString());
        }
        if (null != map.get("controlTime")) {
            sdOperationLog.setCreateTime(DateUtils.parseDate(map.get("controlTime")));
        }else{
            sdOperationLog.setCreateTime(new Date());
        }
        if (null != map.get("eventId")) {
            sdOperationLog.setEventId(map.get("eventId").toString());
        }


        //情报板控制需要根据模板ID获取到发送报文需要的各个元素
        if (data.size() > 0) {
            sdOperationLog.setBeforeState(data.get(0).getData());
        }

        sdOperationLog.setOperationState(operationState);
        sdOperationLog.setState(String.valueOf(controlState));

        sdOperationLogService.insertSdOperationLog(sdOperationLog);
    }


    /**
     * 控制情报板
     * @param controlState
     * @param templateId
     * @param sdDevices
     * @param state
     * @param map
     * @return
     */
    private int controlInformationBoard(int controlState, Long templateId, SdDevices sdDevices, String state, Map<String, Object> map) {

        boolean isopen = commonControlService.queryAnalogControlConfig();

        IotBoardTemplate iotBoardTemplate = sdVmsTemplateService.selectSdVmsTemplateById(templateId);
        SdJoinPlanStrategy planStrategy = new SdJoinPlanStrategy();
        planStrategy.setCurrentId(Long.valueOf(map.get("currentId").toString()));
        planStrategy.setTemplateId(map.get("templateId").toString());
        //0：预案 1：策略
        planStrategy.setType(map.get("type").toString());
        List<SdJoinPlanStrategy> vmsDataList = planStrategyMapper.selectSdJoinPlanStrategyList(planStrategy);
        String parameters = "[Playlist]<r><n>ITEM_NO=2<r><n>ITEM000=300,0,1,\\C000000\\fh2424\\c255255000000谨慎驾驶\\n注意安全<r><n>ITEM001=300,0,1,\\C000000\\fh2424\\c255255000000山东高速\\n欢迎您";
        //情报板参数集合
        Map<String, Object> boardMap = new HashMap<>();
        if (iotBoardTemplate != null || vmsDataList.size() > 0) {
            IotBoardTemplateContent iotBoardTemplateContent = new IotBoardTemplateContent();
            iotBoardTemplateContent.setTemplateId(templateId.toString());
            List<IotBoardTemplateContent> iotBoardTemplateContents = sdVmsTemplateContentService.selectSdVmsTemplateContentList(iotBoardTemplateContent);
            IotBoardTemplateContent templateContent = new IotBoardTemplateContent();
            //如果有预案或策略留存的情报板则先使用
            if(vmsDataList.size() > 0){
                BeanUtils.copyProperties(vmsDataList.get(0),templateContent);
                if(iotBoardTemplate == null){
                    iotBoardTemplate = new IotBoardTemplate();
                }
                iotBoardTemplate.setStopTime(vmsDataList.get(0).getStopTime());
            }else {
                templateContent = iotBoardTemplateContents.get(0);
            }
            String fontType = "s";
            if (templateContent.getFontType().equals("KaiTi")) {
                fontType = "k";
            } else if (templateContent.getFontType().equals("SimHei")) {
                fontType = "h";
            }
            String fontColor = "255255000000";
            if (templateContent.getFontColor().equals("red")) {
                fontColor = "255000000000";
            } else if (templateContent.getFontColor().equals("green")) {
                fontColor = "000255000000";
            } else if (templateContent.getFontColor().equals("blue")) {
                fontColor = "000000255000";
            }
            //[Playlist]<r><n>ITEM_NO=1<r><n>ITEM000=500,0,1,\C072004\fh3232\c255255000000前方事故xxxxxx米
            //情报板参数光电，电明
            boardMap.put("deviceIds",sdDevices.getEqId());
            List<Map<String, Object>> mapList = new ArrayList<>();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("STAY", iotBoardTemplate.getStopTime());
            paramMap.put("ACTION","1");
            paramMap.put("SPEED",1);
            paramMap.put("COORDINATE",templateContent.getCoordinate());
            paramMap.put("COLOR",fontColor);
            paramMap.put("FONT",fontType);
            paramMap.put("FONT_SIZE",templateContent.getFontSize());
            if(templateContent.getContent().contains("#")){
                String content = templateContent.getContent().replaceAll("#", "");
                Long eventId = Long.valueOf(map.get("currentId").toString());
                String carPa = radarEventMapper.selectConfidence(eventId);
                paramMap.put("CONTENT",carPa + content);
            }else {
                paramMap.put("CONTENT",templateContent.getContent());
            }
            mapList.add(paramMap);
            boardMap.put("parameters",mapList);
            /*parameters = "[Playlist]<r><n>ITEM_NO=1<r><n>ITEM000="+ iotBoardTemplate.getStopTime()+",1,1,\\C"
                    +templateContent.getCoordinate()+"\\f"+fontType+templateContent.getFontSize()
                    +templateContent.getFontSize()+templateContent.getFontSize()+"\\c"+fontColor+templateContent.getContent();*/
            state = templateContent.getContent();
        }

        if (!isopen) {
            BoardController boardController = SpringUtils.getBean(BoardController.class);
            Object json = JSON.toJSON(boardMap);
            Map<String, Object> map1 = new HashMap<>();
            map1.put("objectData",json);
            AjaxResult ajaxResult = boardController.splicingBoard(map1);
            System.out.println(sdDevices.getEqId() + "：情报板下发  " + ajaxResult.get("code"));
            /*Long dId = sdDevices.getAssociatedDeviceId();
            SdIotDevice sdIotDevice = sdIotDeviceService.selectIotDeviceById(dId);
            String protocolType = sdIotDevice.getProtocolName();
            //连接设备进行控制，需要组装报文
            String commands = DataUtils.contentToGb2312_CG(dId.toString(), parameters, protocolType);
            try{
                Boolean result = DeviceManagerFactory.getInstance().controlDeviceByDeviceId(dId.toString(), protocolType, commands);
                if (result) {
                    controlState = 1;
                } else {
                    controlState = 0;
                }
            }catch (Exception e){
                log.error("情报板控制报错："+e.getMessage());
                controlState = 0;
            }*/
            Integer code = Integer.valueOf(ajaxResult.get("code").toString());
            if(code == 200){
                controlState = 1;
            } else {
                controlState = 0;
            }
        } else  {
            //模拟控制
           controlState = analogControl(map,sdDevices,state);
        }
        return controlState;
    }
}
