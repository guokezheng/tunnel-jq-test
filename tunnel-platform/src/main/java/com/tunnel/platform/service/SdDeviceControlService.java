package com.tunnel.platform.service;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.service.ISysDictDataService;
import com.tunnel.business.datacenter.domain.enumeration.*;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceTypeItem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.event.SdJoinPlanStrategy;
import com.tunnel.business.domain.informationBoard.IotBoardTemplate;
import com.tunnel.business.domain.informationBoard.IotBoardTemplateContent;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.mapper.event.SdJoinPlanStrategyMapper;
import com.tunnel.business.mapper.informationBoard.IotBoardTemplateContentMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDeviceTypeItemService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.informationBoard.IIotBoardTemplateContentService;
import com.tunnel.business.service.informationBoard.IIotBoardTemplateService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.generalcontrol.service.GeneralControlService;
import com.tunnel.deal.guidancelamp.control.util.GuidanceLampHandle;
import com.tunnel.deal.plc.modbus.ModbusTcpHandle;
import com.tunnel.deal.warninglightstrip.WarningLightStripHandle;
import com.tunnel.platform.business.vms.device.DataUtils;
import com.tunnel.platform.business.vms.device.DeviceManagerFactory;
import com.tunnel.platform.service.deviceControl.HongMengDevService;
import com.tunnel.platform.service.deviceControl.LightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class SdDeviceControlService {

    private static final Logger log = LoggerFactory.getLogger(SdDeviceControlService.class);

    @Value("${authorize.name}")
    private String deploymentType;

    @Autowired
    private SdOptDeviceService sdOptDeviceService;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdOperationLogService sdOperationLogService;

    @Autowired
    private ISysDictDataService sysDictDataService;

    @Autowired
    private ISdDeviceTypeItemService sdDeviceTypeItemService;

    @Autowired
    private ISdDeviceDataService sdDeviceDataService;

    @Autowired
    private LightService lightService;
    @Autowired
    private IIotBoardTemplateService sdVmsTemplateService;
    @Autowired
    private IIotBoardTemplateContentService sdVmsTemplateContentService;

    @Autowired
    private HongMengDevService hongMengDevService;

    @Autowired
    private SdJoinPlanStrategyMapper planStrategyMapper;

    @Autowired
    private GeneralControlService generalControlService;

    @Autowired
    private CommonControlService commonControlService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 高速云端是否可控
     */
    @Value("${platform.control}")
    private String platformControl;


    /**
     * 批量控制设备方法参数不能为空，否则直接返回0（控制失败）
     * 控制车指必传参数：devId（设备ID）、state（变更的状态）
     * 控制诱导灯：devId（设备ID）、state（变更的状态）、brightness（亮度）、frequency（频率）
     * 控制疏散标志：devId（设备ID）、state（变更的状态）、brightness（亮度）、frequency（频率）、fireMark（设备地址标号，正常情况下为255,0为关灯）
     * 照明：devId（设备ID）、state（变更的状态）、brightness（亮度）
     * 情报板控制：devId（设备ID）、state（情报板模板内容对应sdvmstemplatecontent中的content字段）、templateId（情报板模板ID）
     * 警示灯带：devId（设备ID）、state（变更的状态）
     * 控制方式controlType 根据字典sd_control_type 0：手动控制 1：定时控制 2：自动触发 3：分时控制 4：预案执行 8：光强控制
     * */
    public Integer controlDevices(Map<String, Object> map) {
        int controlState = 0;
        SdDevices sdDevices = null;
        try {
            //控制车指
            if (map.get("devId") == null || map.get("devId").toString().equals("")) {
                throw new RuntimeException("未指定设备");
            } else if (map.get("state") == null || map.get("state").toString().equals("")) {
                throw new RuntimeException("未指定设备需要变更的状态信息");
            } else if (map.get("controlType") == null || map.get("controlType").toString().equals("")) {
                throw new RuntimeException("未指定控制方式");
            }
            if ("GSY".equals(deploymentType)) {
                return sdOptDeviceService.optSingleDevice(map);
            }
            String devId = map.get("devId").toString();
            sdDevices = sdDevicesService.selectSdDevicesById(devId);

            //设备控制
            GeneralControlBean generalControlBean = generalControlService.getProtocolBean(sdDevices);
            if(generalControlBean == null){
                throw new RuntimeException("设备协议配置为空");
            }else{
                controlState = generalControlBean.controlDevices(map);
            }
        }catch (Exception e){
            // 异常信息最大支持vachar 2000
            String message = JSON.toJSONString(e.getMessage());
            int index = message.length() <= 2000 ? message.length() : 2000;

            map.put("description",message.substring(0,index));

            if (map.get("templateId") != null &&  !map.get("templateId").toString().equals("")) {
                Long templateId = Long.parseLong(map.get("templateId").toString());
                String operationState = null;
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
                map.put("state",operationState);
            }

            //生成日志
            SdOperationLog sdOperationLog = commonControlService.getOperationLog(map,sdDevices,controlState);
            sdOperationLogService.insertSdOperationLog(sdOperationLog);
            throw new RuntimeException(e.getMessage());
        }

      return controlState;
    }

    /**
     * 批量控制设备方法参数不能为空，否则直接返回0（控制失败）
     * 控制车指必传参数：devId（设备ID）、state（变更的状态）
     * 控制诱导灯：devId（设备ID）、state（变更的状态）、brightness（亮度）、frequency（频率）
     * 控制疏散标志：devId（设备ID）、state（变更的状态）、brightness（亮度）、frequency（频率）、fireMark（设备地址标号，正常情况下为255,0为关灯）
     * 情报板控制：devId（设备ID）、state（情报板模板内容对应sdvmstemplatecontent中的content字段）、templateId（情报板模板ID）
     * 控制方式controlType 根据字典sd_control_type 0：手动控制 1：定时控制 2：自动触发 3：分时控制 4：预案执行 8：光强控制
     * */
    public Integer controlDevicesOld(Map<String, Object> map) {

        //添加操作记录
        SdOperationLog sdOperationLog = new SdOperationLog();

        //部份设备未接入，无法正确获取设备控制结果，默认失败
        sdOperationLog.setState("0");

        int controlState = 0;
        try {
            if (map == null || map.isEmpty()) {
                //当前设备控制参数为空，直接返回
                log.error("当前设备控制参数为空");
                return 0;
            }

            //解析map 杭山东隧道下调用瑞华赢接口控制设备
            String deviceId = map.get("devId").toString();
            String deviceState = map.get("state").toString();
            SdDevices devicesHong = sdDevicesService.selectSdDevicesById(deviceId);
            if(TunnelEnum.HANG_SHAN_DONG.getCode().equals(devicesHong.getEqTunnelId()) && DevicesHongTypeEnum.contains(devicesHong.getEqType()) && "AGREE".equals(platformControl)){
                Map<String, String> hongMap = hongMengDevService.updateHua(deviceId, deviceState);
                Integer code = Integer.valueOf(hongMap.get("code"));
                if(code == 200){
                    return 1;
                }else {
                    return 0;
                }
            }

            if ("GSY".equals(deploymentType)) {
                return sdOptDeviceService.optSingleDevice(map);
            }

            //根据字典中配置的设备模拟控制值进行模拟状态展示
            List<SysDictData> isopenList = sysDictDataService.getSysDictDataByDictType("sys_analog_control_isopen");
            if (isopenList.size() == 0) {
                throw new RuntimeException("设备模拟控制是否开启字典值不存在，请添加后重试");
            }
            SysDictData sysDictData = isopenList.get(0);
            String isopen = sysDictData.getDictValue();

            //控制车指
            if (map.get("devId") == null || map.get("devId").toString().equals("")) {
                throw new RuntimeException("未指定设备");
            } else if (map.get("state") == null || map.get("state").toString().equals("")) {
                throw new RuntimeException("未指定设备需要变更的状态信息");
            } else if (map.get("controlType") == null || map.get("controlType").toString().equals("")) {
                throw new RuntimeException("未指定设备需要变更的状态信息");
            }
            String devId = map.get("devId").toString();
            String state = map.get("state").toString();
            String controlType = map.get("controlType").toString();
            SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);
            //获取当前设备状态
            SdDeviceData sdDeviceData = new SdDeviceData();
            sdDeviceData.setDeviceId(devId);
            //sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode()));
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

            controlState = 0;
            String fireMark = "";
            //控制车指
            if (sdDevices != null && (sdDevices.getEqType().longValue() == DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode().longValue()
                || sdDevices.getEqType().longValue() == DevicesTypeEnum.JUAN_LIAN_MEN.getCode().longValue() ||
                    sdDevices.getEqType().longValue() == DevicesTypeEnum.FENG_JI.getCode().longValue() ||
                  //  sdDevices.getEqType().longValue() == DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().longValue() ||
                    sdDevices.getEqType().longValue() == DevicesTypeEnum.ZHUO_ZHUAN_CHE_ZHI.getCode().longValue() ||
                    sdDevices.getEqType().longValue() == DevicesTypeEnum.ZUO_JIAO_TONG_XIN_HAO_DENG.getCode().longValue()) ||
                    sdDevices.getEqType().longValue() == DevicesTypeEnum.JIAO_TONG_XIN_HAO_DENG.getCode().longValue()) {
                if (data.size() > 0) {
                    sdOperationLog.setBeforeState(data.get(0).getData());
                }

                //车指控制方法
                controlState = controlLaneIndicator(controlState, isopen, devId, state, sdDevices);

                sdOperationLog.setState(String.valueOf(controlState));
                //通过websocket推送到前端
                String[] states = new String[4];
                states[0] = state;
                commonControlService.sendNowDeviceStatusByWebsocket(sdDevices,states,sdOperationLog,"cz");
                //控制诱导灯
            } else if (sdDevices != null && sdDevices.getEqType().longValue() == DevicesTypeEnum.YOU_DAO_DENG.getCode().longValue()) {
                if (map.get("brightness") == null || map.get("brightness").toString().equals("")) {
                    throw new RuntimeException("未指定设备需要变更的亮度信息");
                } else if (map.get("frequency") == null || map.get("frequency").toString().equals("")) {
                    throw new RuntimeException("未指定设备需要变更的频率信息");
                }
                String brightness = map.get("brightness").toString();
                String frequency = map.get("frequency").toString();
                sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode()));
                data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
                if (data.size() > 0) {
                    sdOperationLog.setBeforeState(data.get(0).getData());
                }

                //控制器不在工作台展示，传进来的子设备需要关联查父级控制器的信息
                sdDevices = sdDevicesService.selectSdDevicesById(sdDevices.getFEqId());
                devId = sdDevices.getEqId();
                //诱导灯控制方法
                controlState = controlGuidanceLamp(controlState, isopen, devId, state, sdDevices, brightness, frequency);

                sdOperationLog.setState(String.valueOf(controlState));
                //通过websocket推送到前端
                String[] states = new String[4];
                states[0] = state;
                states[1] = brightness;
                states[2] = frequency;
                commonControlService.sendNowDeviceStatusByWebsocket(sdDevices,states,sdOperationLog,"ydd");
                //控制疏散标志
            } else if (sdDevices != null && sdDevices.getEqType().longValue() == DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode().longValue()) {
                if (map.get("brightness") == null || map.get("brightness").toString().equals("")) {
                    throw new RuntimeException("未指定设备需要变更的亮度信息");
                } else if (map.get("frequency") == null || map.get("frequency").toString().equals("")) {
                    throw new RuntimeException("未指定设备需要变更的频率信息");
                } else if (map.get("fireMark") == null || map.get("fireMark").toString().equals("")) {
                    throw new RuntimeException("未指定设备需要变更的标号位置信息");
                }
                fireMark = map.get("fireMark").toString();
                String brightness = map.get("brightness").toString();
                String frequency = map.get("frequency").toString();
                sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()));
                data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
                if (data.size() > 0) {
                    sdOperationLog.setBeforeState(data.get(0).getData());
                }

                //控制器不在工作台展示，传进来的子设备需要关联查父级控制器的信息
                sdDevices = sdDevicesService.selectSdDevicesById(sdDevices.getFEqId());
                devId = sdDevices.getEqId();
                //疏散标志控制方法
                controlState = controlEvacuationSign(controlState, isopen, devId, state, sdDevices, brightness, frequency, fireMark);

                sdOperationLog.setState(String.valueOf(controlState));
                //通过websocket推送到前端
                String[] states = new String[4];
                states[0] = state;
                states[1] = brightness;
                states[2] = frequency;
                states[3] = fireMark;
                commonControlService.sendNowDeviceStatusByWebsocket(sdDevices,states,sdOperationLog,"ydd");
                //控制照明 目前只有加强照明和基本照明
            } else if (sdDevices != null && sdDevices.getEqType().longValue() == DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().longValue() || sdDevices.getEqType().longValue() == DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().longValue() ||
                    sdDevices.getEqType().longValue() == DevicesTypeEnum.YIN_DAO_ZHAO_MING.getCode() || sdDevices.getEqType().longValue() == DevicesTypeEnum.YING_JI_ZHAO_MING.getCode()) {
                if (data.size() > 0) {
                    sdOperationLog.setBeforeState(data.get(0).getData());
                }
                Integer brightness = 0;
                // 加强照明，可设置照明亮度
                if(sdDevices.getEqType().longValue() == DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().longValue()
                    || sdDevices.getEqType().longValue() == DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().longValue()
                ){
                    if(map.get("stateNum") != null){
                        brightness = Integer.parseInt(map.get("stateNum").toString());
                        sdOperationLog.setDescription(brightness+"");
                    }

                    String operationStateStr = state.equals("1")?"开启":"关闭";
                    operationStateStr += "，亮度："+brightness + "%";
                    sdOperationLog.setOperationState(operationStateStr);
                }
                //控制照明设备
                controlState = controlLightingDevices(controlState, isopen, devId, state, sdDevices,brightness);

                sdOperationLog.setState(String.valueOf(controlState));
            } else if (sdDevices != null && (sdDevices.getEqType().longValue() == DevicesTypeEnum.VMS.getCode().longValue()
                    || sdDevices.getEqType().longValue() == DevicesTypeEnum.MEN_JIA_VMS.getCode().longValue())) {
                //情报板控制需要根据模板ID获取到发送报文需要的各个元素
                if (data.size() > 0) {
                    sdOperationLog.setBeforeState(data.get(0).getData());
                }
                if (map.get("templateId") == null || map.get("templateId").toString().equals("")) {
                    throw new RuntimeException("没有找到需要发布的模板信息");
                }
                Long templateId = Long.parseLong(map.get("templateId").toString());
                //模板内容
                if(map.get("currentId") == null && map.get("type") == null){
                    IotBoardTemplateContent templateContent = new IotBoardTemplateContent();
                    templateContent.setTemplateId(templateId.toString());
                    List<IotBoardTemplateContent> templateContentList = SpringUtils.getBean(IotBoardTemplateContentMapper.class).selectSdVmsTemplateContentList(templateContent);
                    sdOperationLog.setOperationState(templateContentList.size() == 0 ? "" : templateContentList.get(0).getContent());
                }else {
                    SdJoinPlanStrategy planStrategy = new SdJoinPlanStrategy();
                    planStrategy.setCurrentId(Long.valueOf(map.get("currentId").toString()));
                    planStrategy.setTemplateId(map.get("templateId").toString());
                    //0：预案 1：策略
                    planStrategy.setType(map.get("type").toString());
                    Map<String, Object> sdVmsContent = planStrategyMapper.getTemplateContent(planStrategy);
                    sdOperationLog.setOperationState(sdVmsContent == null ? "" : sdVmsContent.get("content").toString());
                }

                //控制情报板
                controlState = controlInformationBoard(controlState, isopen, templateId, sdDevices, state, map);

                sdOperationLog.setState(String.valueOf(controlState));
            } else if (sdDevices != null
                    && sdDevices.getEqType().longValue() == DevicesTypeEnum.JING_SHI_DENG_DAI.getCode().longValue()) {
                if (data.size() > 0) {
                    sdOperationLog.setBeforeState(data.get(0).getData());
                }
                //警示灯带控制方法
                controlState = controlWarningLightStripDevice(controlState, isopen, devId, state, sdDevices);
                sdOperationLog.setState(String.valueOf(controlState));
            }
        } catch (Exception e) {
            sdOperationLog.setState("0");
            // 异常信息最大支持vachar 2000. 实际记录1900
            String errorMsg = "task service error";
            int index = errorMsg.length();
            if(null != e.getMessage()){
                errorMsg = e.getMessage();
                if(e.getMessage().length() < 1900){
                    index = e.getMessage().length();
                }
            }
            sdOperationLog.setDescription(errorMsg.substring(0,index));
            sdOperationLogService.insertSdOperationLog(sdOperationLog);
            throw new RuntimeException(e.getMessage());
        }
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
        return controlState;
    }

    private int controlLightingDevices(int controlState, String isopen, String devId, String state, SdDevices sdDevices,Integer brightness) {
        //将当前设备亮度存储redis缓存。
        String redisLuminanceRangeKey = "control_regular:"+sdDevices.getEqId()+"_LuminanceRange";
        redisCache.setCacheObject(redisLuminanceRangeKey,brightness);
        //将当前设备亮度存储redis缓存。
        String redisStateLuminanceRangeKey = "control_regular:"+sdDevices.getEqId()+"_state";
        redisCache.setCacheObject(redisStateLuminanceRangeKey,state);

        redisCache.setCacheObject(redisLuminanceRangeKey,brightness);
        if (isopen != null && !isopen.equals("") && isopen.equals("0")) {
            controlState = lightService.lineControl(devId, Integer.parseInt(state),brightness);

        } else if (isopen != null && !isopen.equals("") && isopen.equals("1")) {
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
            sdDeviceTypeItems.stream().forEach(item -> {
                if("brightness".equals(item.getItemCode()) &&
                        (DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().equals(sdDevices.getEqType()) ||
                                DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().equals(sdDevices.getEqType())
                        )
                ){
                    sdDeviceDataService.updateDeviceData(sdDevices, brightness.toString(), item.getId());
                }
                if("state".equals(item.getItemCode())){
                    sdDeviceDataService.updateDeviceData(sdDevices, state, item.getId());
                }
            });
           /* SdDeviceTypeItem typeItem = sdDeviceTypeItems.get(0);
            updateDeviceData(sdDevices, state, Integer.parseInt(typeItem.getId().toString()));*/
            controlState = 1;
        }
        return controlState;
    }

    /**
     * 控制情报板
     * @param controlState
     * @param isopen
     * @param templateId
     * @param sdDevices
     * @param state
     * @param map
     * @return
     */
    private int controlInformationBoard(int controlState, String isopen, Long templateId, SdDevices sdDevices, String state, Map<String, Object> map) {
        IotBoardTemplate iotBoardTemplate = sdVmsTemplateService.selectSdVmsTemplateById(templateId);
        SdJoinPlanStrategy planStrategy = new SdJoinPlanStrategy();
        planStrategy.setCurrentId(Long.valueOf(map.get("currentId").toString()));
        planStrategy.setTemplateId(map.get("templateId").toString());
        //0：预案 1：策略
        planStrategy.setType(map.get("type").toString());
        List<SdJoinPlanStrategy> vmsDataList = planStrategyMapper.selectSdJoinPlanStrategyList(planStrategy);
        String parameters = "[Playlist]<r><n>ITEM_NO=2<r><n>ITEM000=300,0,1,\\C000000\\fh2424\\c255255000000谨慎驾驶\\n注意安全<r><n>ITEM001=300,0,1,\\C000000\\fh2424\\c255255000000山东高速\\n欢迎您";
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
            parameters = "[Playlist]<r><n>ITEM_NO=1<r><n>ITEM000="+ iotBoardTemplate.getStopTime()+",1,1,\\C"
                    +templateContent.getCoordinate()+"\\f"+fontType+templateContent.getFontSize()
                    +templateContent.getFontSize()+templateContent.getFontSize()+"\\c"+fontColor+templateContent.getContent();
            state = templateContent.getContent();
        }

        if (isopen != null && !isopen.equals("") && isopen.equals("0")) {
            //连接设备进行控制，需要组装报文
            String commands = DataUtils.contentToGb2312_CG(sdDevices.getAssociatedDeviceId().toString(), parameters, "GUANGDIAN_V33");
            Boolean result = DeviceManagerFactory.getInstance().controlDeviceByDeviceId(sdDevices.getAssociatedDeviceId().toString(), "GUANGDIAN_V33", commands);
            if (result) {
                controlState = 1;
            } else {
                controlState = 0;
            }
        } else if (isopen != null && !isopen.equals("") && isopen.equals("1")) {
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
            controlState = 1;
        }
        return controlState;
    }

    /**
     * 疏散标志控制方法
     * @param controlState
     * @param isopen
     * @param devId
     * @param state
     * @param sdDevices
     * @param brightness
     * @param frequency
     * @param fireMark
     * @return
     */
    private int controlEvacuationSign(int controlState, String isopen, String devId, String state, SdDevices sdDevices, String brightness, String frequency, String fireMark) {
        if (isopen != null && !isopen.equals("") && isopen.equals("0")) {
            //连接设备进行控制
            controlState = GuidanceLampHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices, brightness, frequency, fireMark);
        } else if (isopen != null && !isopen.equals("") && isopen.equals("1")) {
            //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
            sdDevices.setEqStatus("1");
            sdDevices.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(sdDevices);
            if (devId != null && !"".equals(devId)) {
                SdDevices devices = new SdDevices();
                devices.setEqStatus("1");
                devices.setEqStatusTime(new Date());
                devices.setFEqId(devId);
                sdDevicesService.updateSdDevicesByFEqId(sdDevices);
            }
            //父级设备变更
            sdDeviceDataService.updateDeviceData(sdDevices, state, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()));
            sdDeviceDataService.updateDeviceData(sdDevices, brightness, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode()));
            sdDeviceDataService.updateDeviceData(sdDevices, frequency, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode()));
            sdDeviceDataService.updateDeviceData(sdDevices, fireMark, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode()));
            //子级设备变更
            SdDevices dev = new SdDevices();
            dev.setFEqId(devId);
            List<SdDevices> list = sdDevicesService.selectSdDevicesList(dev);
            if (!list.isEmpty()) {
                //疏散标志关灯
                if (fireMark.equals("0") && !fireMark.equals("255")) {
                    state = "1";
                    for (int i = 0;i < list.size();i++) {
                        SdDevices devo = list.get(i);
                        sdDeviceDataService.updateDeviceData(devo, state, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()));
                        sdDeviceDataService.updateDeviceData(devo, brightness, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode()));
                        sdDeviceDataService.updateDeviceData(devo, frequency, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode()));
                        sdDeviceDataService.updateDeviceData(devo, fireMark, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode()));
                    }
                    //疏散标志报警点更新
                } else if (!fireMark.equals("0") && !fireMark.equals("255")) {
                    BigDecimal fMark = new BigDecimal(fireMark);
                    for (int i = 0;i < list.size();i++) {
                        SdDevices devices = list.get(i);
                        BigDecimal addressMark = new BigDecimal(devices.getQueryPointAddress());
                        if (fMark.compareTo(addressMark) < 0) {
                            state = "6";
                        } else if (fMark.compareTo(addressMark) == 0) {
                            state = "5";
                        } else if (fMark.compareTo(addressMark) > 0) {
                            state = "4";
                        }
                        sdDeviceDataService.updateDeviceData(devices, fireMark, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode()));
                        sdDeviceDataService.updateDeviceData(devices, state, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()));
                        sdDeviceDataService.updateDeviceData(devices, brightness, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode()));
                        sdDeviceDataService.updateDeviceData(devices, frequency, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode()));
                    }
                } else {
                    //疏散标志开灯无报警点
                    state = "2";
                    for (int i = 0;i < list.size();i++) {
                        SdDevices devo = list.get(i);
                        sdDeviceDataService.updateDeviceData(devo, state, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()));
                        sdDeviceDataService.updateDeviceData(devo, brightness, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode()));
                        sdDeviceDataService.updateDeviceData(devo, frequency, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode()));
                        sdDeviceDataService.updateDeviceData(devo, fireMark, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode()));
                    }
                }
            }
            controlState = 1;
        }
        return controlState;
    }

    /**
     * 诱导灯控制方法
     * @param controlState
     * @param isopen
     * @param devId
     * @param state
     * @param sdDevices
     * @param brightness
     * @param frequency
     * @return
     */
    public int controlGuidanceLamp(int controlState, String isopen, String devId, String state, SdDevices sdDevices, String brightness, String frequency) {
        if (isopen != null && !isopen.equals("") && isopen.equals("0")) {
            //连接设备进行控制
            if (sdDevices.getBrandId() != null && sdDevices.getBrandId().equals("0057")) {
                controlState = GuidanceLampHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices, brightness, frequency, null);
            } else {
                controlState = GuidanceLampHandle.getInstance().toControlXianKeDev(devId, Integer.parseInt(state), sdDevices, brightness, frequency);
            }
        } else if (isopen != null && !isopen.equals("") && isopen.equals("1")) {
            //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
            sdDevices.setEqStatus("1");
            sdDevices.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(sdDevices);
            if (devId != null && !"".equals(devId)) {
                SdDevices devices = new SdDevices();
                devices.setEqStatus("1");
                devices.setEqStatusTime(new Date());
                devices.setFEqId(devId);
                sdDevicesService.updateSdDevicesByFEqId(sdDevices);
            }
            //父级设备变更
            sdDeviceDataService.updateDeviceData(sdDevices, state, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode()));
            sdDeviceDataService.updateDeviceData(sdDevices, brightness, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode()));
            sdDeviceDataService.updateDeviceData(sdDevices, frequency, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode()));
            //子级设备变更
            SdDevices dev = new SdDevices();
            dev.setFEqId(devId);
            List<SdDevices> list = sdDevicesService.selectSdDevicesList(dev);
            for (int i = 0;i < list.size();i++) {
                SdDevices devo = list.get(i);
                sdDeviceDataService.updateDeviceData(devo, state, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode()));
                sdDeviceDataService.updateDeviceData(devo, brightness, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode()));
                sdDeviceDataService.updateDeviceData(devo, frequency, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode()));
            }
            controlState = 1;
        }
        return controlState;
    }

    /**
     * 警示灯带控制方法
     * @param controlState
     * @param isopen
     * @param devId
     * @param state
     * @param sdDevices
     * @return
     */
    private int controlWarningLightStripDevice(Integer controlState, String isopen, String devId, String state, SdDevices sdDevices) {
        if (isopen != null && !isopen.equals("") && isopen.equals("0")) {
            //连接设备进行控制
            controlState = WarningLightStripHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices);
        } else if (isopen != null && !isopen.equals("") && isopen.equals("1")) {
            //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
            sdDevices.setEqStatus("1");
            sdDevices.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(sdDevices);
            SdDeviceTypeItem sdDeviceTypeItem = new SdDeviceTypeItem();
            sdDeviceTypeItem.setDeviceTypeId(sdDevices.getEqType());
            List<SdDeviceTypeItem> sdDeviceTypeItems = sdDeviceTypeItemService.selectSdDeviceTypeItemList(sdDeviceTypeItem);
            if (sdDeviceTypeItems.size() == 0) {
                throw new RuntimeException("当前设备没有设备类型数据项数据");
            }
            SdDeviceTypeItem typeItem = sdDeviceTypeItems.get(0);
            sdDeviceDataService.updateDeviceData(sdDevices, state, typeItem.getId());
            controlState = 1;
        }
        return controlState;
    }



    /**
     * 车指、信号灯、风机、卷帘门等设备控制方法
     * @param controlState
     * @param isopen
     * @param state
     * @param sdDevices
     * @return
     */
    private int controlLaneIndicator(Integer controlState, String isopen, String devId, String state, SdDevices sdDevices) {
        if (isopen != null && !isopen.equals("") && isopen.equals("0")) {
            //连接设备进行控制
            controlState = ModbusTcpHandle.getInstance().toControlDev(Integer.parseInt(state), sdDevices);
        } else if (isopen != null && !isopen.equals("") && isopen.equals("1")) {
            //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
            sdDevices.setEqStatus("1");
            sdDevices.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(sdDevices);
            SdDeviceTypeItem sdDeviceTypeItem = new SdDeviceTypeItem();
            sdDeviceTypeItem.setDeviceTypeId(sdDevices.getEqType());
            List<SdDeviceTypeItem> sdDeviceTypeItems = sdDeviceTypeItemService.selectSdDeviceTypeItemList(sdDeviceTypeItem);
            if (sdDeviceTypeItems.size() == 0) {
                throw new RuntimeException("当前设备没有设备类型数据项数据");
            }
            SdDeviceTypeItem typeItem = sdDeviceTypeItems.get(0);
            sdDeviceDataService.updateDeviceData(sdDevices, state, typeItem.getId());
            controlState = 1;
        }
        return controlState;
    }


    /**
     * 高速云级平台控制方法
     * @param map 参数信息
     * @return
     */
    public AjaxResult cloudPlatformControl(Map<String, Object> map){

        map.put("controlType", "0");
        //操作IP
        map.put("operIp", IpUtils.getIpAddr(ServletUtils.getRequest()));
        sdOptDeviceService.optSingleDevice(map);
        return AjaxResult.success(1);
    }

}
