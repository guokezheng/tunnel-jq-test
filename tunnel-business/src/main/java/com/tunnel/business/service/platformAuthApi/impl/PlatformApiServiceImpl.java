package com.tunnel.business.service.platformAuthApi.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.*;
import com.tunnel.business.mapper.dataInfo.InductionlampControlStatusParamMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.service.dataInfo.*;
import com.tunnel.business.service.platformAuthApi.PlatformApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhai
 * @date 2022/10/25
 */
@Service
public class PlatformApiServiceImpl implements PlatformApiService {

    private static final Logger log = LoggerFactory.getLogger(PlatformApiService.class);

    @Value("${authorize.gsy.device_push_url}")
    private String devicePushUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IInductionlampControlStatusDetailsService iInductionlampControlStatusDetailsService;

    @Autowired
    private IInductionlampControlStatusParamService iInductionlampControlStatusParamService;

    @Autowired
    private InductionlampControlStatusParamMapper inductionlampControlStatusParamMapper;

    @Autowired
    private ISdEquipmentStateService sdEquipmentStateService;

    @Autowired
    private ISdDeviceCmdService sdDeviceCmdService;

    @Autowired
    private ISdFixedCodeService sdFixedCodeService;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Autowired
    private ISdTunnelsService sdTunnelsService;

    @Autowired
    private ISdEquipmentTypeService equipmentTypeService;

    @Override
    public int devicesPush(List<SdDevices> sdDevicesList) {
        JSONArray objects = JSONObject.parseArray(JSONObject.toJSONString(sdDevicesList));
        //请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        //设置JSON格式数据
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        //requestHeaders.add("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjgwYmViZjFjLWUxNDYtNDQyZC1hNjA4LWI4ZjdjZGUyNzc2ZSJ9.Rw-a71HfnvpBwwyK2G_w5tTGeHxugXHj1MlDvdWhx8c-3leM9bmbMKWyIRV_SVq5rUt9eLNQtqcktZYk5Yhlgw");
        HttpEntity<String> requestEntity = new HttpEntity<>(objects.toString(), requestHeaders);
        try {
            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(devicePushUrl, requestEntity, String.class);
            log.info("返回值 --> {}", stringResponseEntity.getBody());
        } catch (Exception e) {
            log.error("高速云推送失败！{}", e.getMessage());
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public int insertSdDevices(List<SdDevices> sdDevicesList) {
        int count = 0;
        for(SdDevices sdDevices : sdDevicesList){
            sdDevices.setEqId("1211212121212121");
            sdDevices.setCreateTime(DateUtils.getNowDate());
            //判断当前是否是诱导灯设备
            Long yddEqTypeId = Long.parseLong(String.valueOf(DevicesTypeEnum.YOU_DAO_DENG.getCode()));
            if (null != sdDevices.getEqType() && sdDevices.getEqType().longValue() == yddEqTypeId.longValue()
                    && (sdDevices.getControlStatus() == null || "".equals(sdDevices.getControlStatus()))) {
                String eqId = sdDevices.getEqId();
                //诱导灯设备暂时只对接手动控制模式，直接指定并默认运行模式为0
                sdDevices.setControlStatus("1");
                InductionlampControlStatusDetails inductionlampControlStatusDetails = new InductionlampControlStatusDetails();
                inductionlampControlStatusDetails.setEquipmentId(eqId);
                inductionlampControlStatusDetails.setEquipmentModeType(0);
                inductionlampControlStatusDetails.setBrightness("50");
                inductionlampControlStatusDetails.setFrequency("69");
                inductionlampControlStatusDetails.setCreateTime(DateUtils.getNowDate());
                iInductionlampControlStatusDetailsService.insertInductionlampControlStatusDetails(inductionlampControlStatusDetails);
                InductionlampControlStatusParam inductionlampControlStatusParam = new InductionlampControlStatusParam();
                inductionlampControlStatusParam.setEquipmentId(eqId);
                List<InductionlampControlStatusParam> controlStatusParams = iInductionlampControlStatusParamService.selectInductionlampControlStatusParamList(inductionlampControlStatusParam);
                if (controlStatusParams == null || controlStatusParams.size() == 0) {
                    if (controlStatusParams.size() < 6) {
                        for (int i = 0; i < controlStatusParams.size(); i++) {
                            iInductionlampControlStatusParamService.deleteInductionlampControlStatusParamById(controlStatusParams.get(i).getId());
                        }
                    }
                    //创建默认模式类型
                    InductionlampControlStatusParam statusParam0 = new InductionlampControlStatusParam();
                    statusParam0.setEquipmentId(eqId);
                    statusParam0.setModeName("模式0");
                    statusParam0.setModeCode(0);
                    //温度
                    statusParam0.setTemperatureStart(0);
                    //湿度
                    statusParam0.setHumidityEnd(89);
                    //光照
                    statusParam0.setIlluminationStart(1000);
                    inductionlampControlStatusParamMapper.insertInductionlampControlStatusParam(statusParam0);

                    InductionlampControlStatusParam statusParam1 = new InductionlampControlStatusParam();
                    statusParam1.setEquipmentId(eqId);
                    statusParam1.setModeName("模式1");
                    statusParam1.setModeCode(1);
                    //温度
                    statusParam1.setTemperatureEnd(0);
                    //湿度
                    statusParam1.setHumidityStart(90);
                    //光照
                    statusParam1.setIlluminationEnd(1000);
                    //亮度
                    statusParam1.setBrightnessParam(50);
                    inductionlampControlStatusParamMapper.insertInductionlampControlStatusParam(statusParam1);

                    InductionlampControlStatusParam statusParam2 = new InductionlampControlStatusParam();
                    statusParam2.setEquipmentId(eqId);
                    statusParam2.setModeName("模式2");
                    statusParam2.setModeCode(2);
                    //光照
                    statusParam2.setIlluminationEnd(500);
                    //闪烁/每秒
                    statusParam2.setTimeSecond(30);
                    inductionlampControlStatusParamMapper.insertInductionlampControlStatusParam(statusParam2);
                }
            }
            count = sdDevicesMapper.insertSdDevices(sdDevices);
            if (sdDevices.getEqType() != 31L) {
                insertOrUpdateOrDeleteSdDeviceCmd(sdDevices);
            }
        }

        return count;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public int updateSdDevices(List<SdDevices> sdDevicesList) {
        int count = 0;
        for(SdDevices sdDevices : sdDevicesList){
            sdDevices.setEqId("1211212121212121");
            sdDevices.setUpdateTime(DateUtils.getNowDate());
            String eqId = sdDevices.getEqId();
            Long yddEqTypeId = Long.parseLong(String.valueOf(DevicesTypeEnum.YOU_DAO_DENG.getCode()));
            if (null != sdDevices.getEqType() && sdDevices.getEqType().longValue() == yddEqTypeId.longValue()) {
                InductionlampControlStatusDetails inductionlampControlStatusDetails = new InductionlampControlStatusDetails();
                inductionlampControlStatusDetails.setEquipmentId(eqId);
                List<InductionlampControlStatusDetails> controlStatusDetails = iInductionlampControlStatusDetailsService.selectInductionlampControlStatusDetailsList(inductionlampControlStatusDetails);
                if (controlStatusDetails == null || controlStatusDetails.size() == 0) {
                    inductionlampControlStatusDetails.setEquipmentModeType(0);
                    inductionlampControlStatusDetails.setBrightness("50");
                    inductionlampControlStatusDetails.setFrequency("69");
                    iInductionlampControlStatusDetailsService.insertInductionlampControlStatusDetails(inductionlampControlStatusDetails);
                }
                sdDevices.setControlStatus("1");
                InductionlampControlStatusParam inductionlampControlStatusParam = new InductionlampControlStatusParam();
                inductionlampControlStatusParam.setEquipmentId(eqId);
                List<InductionlampControlStatusParam> controlStatusParams = iInductionlampControlStatusParamService.selectInductionlampControlStatusParamList(inductionlampControlStatusParam);
                if (controlStatusParams == null || controlStatusParams.size() == 0) {
                    if (controlStatusParams.size() < 3) {
                        for (int i = 0; i < controlStatusParams.size(); i++) {
                            iInductionlampControlStatusParamService.deleteInductionlampControlStatusParamById(controlStatusParams.get(i).getId());
                        }
                    }
                    //创建默认模式类型
                    InductionlampControlStatusParam statusParam0 = new InductionlampControlStatusParam();
                    statusParam0.setEquipmentId(eqId);
                    statusParam0.setModeName("模式0");
                    statusParam0.setModeCode(0);
                    //温度
                    statusParam0.setTemperatureStart(0);
                    //湿度
                    statusParam0.setHumidityEnd(89);
                    //光照
                    statusParam0.setIlluminationStart(1000);
                    inductionlampControlStatusParamMapper.insertInductionlampControlStatusParam(statusParam0);

                    InductionlampControlStatusParam statusParam1 = new InductionlampControlStatusParam();
                    statusParam1.setEquipmentId(eqId);
                    statusParam1.setModeName("模式1");
                    statusParam1.setModeCode(1);
                    //温度
                    statusParam1.setTemperatureEnd(0);
                    //湿度
                    statusParam1.setHumidityStart(90);
                    //光照
                    statusParam1.setIlluminationEnd(1000);
                    //亮度
                    statusParam1.setBrightnessParam(50);
                    inductionlampControlStatusParamMapper.insertInductionlampControlStatusParam(statusParam1);

                    InductionlampControlStatusParam statusParam2 = new InductionlampControlStatusParam();
                    statusParam2.setEquipmentId(eqId);
                    statusParam2.setModeName("模式2");
                    statusParam2.setModeCode(2);
                    //光照
                    statusParam2.setIlluminationEnd(500);
                    //闪烁/每秒
                    statusParam2.setTimeSecond(30);
                    inductionlampControlStatusParamMapper.insertInductionlampControlStatusParam(statusParam2);
                }
            } else {
                InductionlampControlStatusDetails inductionlampControlStatusDetails = new InductionlampControlStatusDetails();
                inductionlampControlStatusDetails.setEquipmentId(eqId);
                List<InductionlampControlStatusDetails> controlStatusDetails = iInductionlampControlStatusDetailsService.selectInductionlampControlStatusDetailsList(inductionlampControlStatusDetails);
                if (controlStatusDetails.size() > 0) {
                    for (int i = 0; i < controlStatusDetails.size(); i++) {
                        Long id = controlStatusDetails.get(i).getId();
                        iInductionlampControlStatusDetailsService.deleteInductionlampControlStatusDetailsById(id);
                    }
                }
                InductionlampControlStatusParam inductionlampControlStatusParam = new InductionlampControlStatusParam();
                inductionlampControlStatusParam.setEquipmentId(eqId);
                List<InductionlampControlStatusParam> controlStatusParams = iInductionlampControlStatusParamService.selectInductionlampControlStatusParamList(inductionlampControlStatusParam);
                if (controlStatusParams.size() > 0) {
                    for (int i = 0; i < controlStatusParams.size(); i++) {
                        iInductionlampControlStatusParamService.deleteInductionlampControlStatusParamById(controlStatusParams.get(i).getId());
                    }
                }
            }
            count = sdDevicesMapper.updateSdDevices(sdDevices);
        }

        return count;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public int deleteSdDevices(List<SdDevices> sdDevicesList) {
        int count = 0;
        for(SdDevices sdDevices : sdDevicesList){
            for(String eqId : sdDevices.getEqIds()){
                InductionlampControlStatusDetails inductionlampControlStatusDetails = new InductionlampControlStatusDetails();
                inductionlampControlStatusDetails.setEquipmentId(eqId);
                List<InductionlampControlStatusDetails> controlStatusDetails = iInductionlampControlStatusDetailsService.selectInductionlampControlStatusDetailsList(inductionlampControlStatusDetails);
                if (controlStatusDetails.size() > 0) {
                    for (int i = 0; i < controlStatusDetails.size(); i++) {
                        Long id = controlStatusDetails.get(i).getId();
                        iInductionlampControlStatusDetailsService.deleteInductionlampControlStatusDetailsById(id);
                    }
                }
                InductionlampControlStatusParam inductionlampControlStatusParam = new InductionlampControlStatusParam();
                inductionlampControlStatusParam.setEquipmentId(eqId);
                List<InductionlampControlStatusParam> controlStatusParams = iInductionlampControlStatusParamService.selectInductionlampControlStatusParamList(inductionlampControlStatusParam);
                if (controlStatusParams.size() > 0) {
                    for (int i = 0; i < controlStatusParams.size(); i++) {
                        iInductionlampControlStatusParamService.deleteInductionlampControlStatusParamById(controlStatusParams.get(i).getId());
                    }
                }
            }
            String[] eqIds = sdDevices.getEqIds().toArray(new String[sdDevices.getEqIds().size()]);
            eqIds[0] = "1211212121212121";
            count = sdDevicesMapper.deleteSdDevicesByIds(eqIds);
        }
        return count;
    }

    @Override
    public int importSdDevices(List<SdDevices> sdDevicesList) {
        if (StringUtils.isNull(sdDevicesList) || sdDevicesList.size() == 0) {
            throw new ServiceException("导入设备数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SdDevices devices : sdDevicesList) {
            try {
                SdDevices d = sdDevicesMapper.selectSdDevicesById(devices.getEqId());
                if (StringUtils.isNull(d)) {
                    Map map = checkDevices(devices);
                    if ((Boolean) map.get("flag")) {
                        //todo 目前没有点位信息，先注释掉生成指令相关代码
//                        StringBuilder sb=new StringBuilder();
//                        sb.append(getCommandCode(devices,devices.getInstructionSeat().split("_")[1],devices.getInstructionSeat().split("_")[0],"0"));
//                        sb.append(getIpleftPad(devices.getEqControlPointAddress()));//点位地址
//                        devices.setEqControlPointAddress(sb.toString());
                        devices.setCreateBy(SecurityUtils.getUsername());
                        List<SdDevices> list = new ArrayList<>();
                        list.add(devices);
                        this.insertSdDevices(list);
//                        insertOrUpdateOrDeleteSdDeviceCmd(devices);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、设备ID " + devices.getEqId() + " 导入成功");
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + map.get("failureMsg").toString());
                    }
                } else if (devices.isUpdateSupport()) {
                    Map map = checkDevices(devices);
                    if ((Boolean) map.get("flag")) {
                        devices.setUpdateBy(SecurityUtils.getUsername());
                        //todo 目前没有点位信息，先注释掉生成指令相关代码
//                        StringBuilder sb=new StringBuilder();
//                        sb.append(getCommandCode(devices,devices.getInstructionSeat().split("_")[1],devices.getInstructionSeat().split("_")[0],"0"));
//                        sb.append(getIpleftPad(devices.getEqControlPointAddress()));//点位地址
//                        devices.setEqControlPointAddress(sb.toString());
                        List<SdDevices> list = new ArrayList<>();
                        list.add(devices);
                        this.updateSdDevices(list);
//                        insertOrUpdateOrDeleteSdDeviceCmd(devices);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、设备ID " + devices.getEqId() + " 更新成功");
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + map.get("failureMsg").toString());
                    }
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、设备ID " + devices.getEqId() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、设备ID " + devices.getEqId() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            if (successNum > 0) {
                failureMsg.insert(0, successNum + " 条数据导入成功" + "，" + failureNum + " 条数据格式不正确，导入失败！错误如下：");
            } else {
                failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            }
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return 1;
    }

    public void insertOrUpdateOrDeleteSdDeviceCmd(SdDevices devices) {
        SdEquipmentState sdEquipmentState = new SdEquipmentState();
        sdEquipmentState.setStateTypeId(devices.getEqType());
        sdEquipmentState.setIsControl(1);
        List<SdEquipmentState> sdEquipmentStates = sdEquipmentStateService.selectSdEquipmentStateList(sdEquipmentState);
        if (sdEquipmentStates.size() > 0) {
            for (SdEquipmentState sdEquipmentState1 : sdEquipmentStates) {
                //根据类型查询类型状态表
                SdDeviceCmd sdDeviceCmd = new SdDeviceCmd();
                sdDeviceCmd.setCodeDeviceId(devices.getEqId());
                sdDeviceCmd.setCodePlcId(devices.getFEqId());
                if (StringUtils.isNotNull(devices.getDmcontrolSeat()) && StringUtils.isNotEmpty(devices.getDmcontrolSeat())) {
                    sdDeviceCmd.setDeviceTypeId(devices.getEqType());
                    sdDeviceCmd.setCodeDeviceState(sdEquipmentState1.getDeviceState());
                    StringBuilder sb = new StringBuilder();
                    sb.append(getCommandCode(devices, devices.getDmcontrolSeat().split("_")[1], devices.getDmcontrolSeat().split("_")[0], "1"));
                    sb.append(getIpleftPad(sdEquipmentState1.getDeviceState()));//点位地址
                    List<SdDeviceCmd> sdDeviceCmds = sdDeviceCmdService.selectSdDeviceCmdList(sdDeviceCmd);
                    if (sdDeviceCmds.size() > 0) {
                        sdDeviceCmds.get(0).setCommand(sb.toString());
                        sdDeviceCmdService.updateSdDeviceCmd(sdDeviceCmds.get(0));
                    } else {
                        sdDeviceCmd.setCommand(sb.toString());
                        sdDeviceCmdService.insertSdDeviceCmd(sdDeviceCmd);
                    }
                }/*else {
                        List<SdDeviceCmd> sdDeviceCmds = sdDeviceCmdService.selectSdDeviceCmdList(sdDeviceCmd);
                        for (SdDeviceCmd sdDeviceCmd1:sdDeviceCmds){
                            sdDeviceCmdService.deleteSdDeviceCmdById(sdDeviceCmd1.getCodeId());
                        }
                    }*/
            }
        } else {
            //没有设备状态，删除该设备关联的设备报文指令
            SdDeviceCmd sdDeviceCmd = new SdDeviceCmd();
            sdDeviceCmd.setCodeDeviceId(devices.getEqId());
            sdDeviceCmd.setCodePlcId(devices.getFEqId());
            List<SdDeviceCmd> sdDeviceCmds = sdDeviceCmdService.selectSdDeviceCmdList(sdDeviceCmd);
            for (SdDeviceCmd sdDeviceCmd1 : sdDeviceCmds) {
                sdDeviceCmdService.deleteSdDeviceCmdById(sdDeviceCmd1.getCodeId());
            }
        }

    }

    public StringBuffer getCommandCode(SdDevices devices, String seat, String deviceState, String deviceType) {
        StringBuffer sb = new StringBuffer();
        SdFixedCode sdFixedCode = new SdFixedCode();
        sdFixedCode.setModelCode(deviceState);
        if ("0".equals(deviceType)) {
            sdFixedCode.setQueryControl("0101");
        } else {
            sdFixedCode.setQueryControl("0102");
        }
        List<SdFixedCode> sdFixedCodes = sdFixedCodeService.selectSdFixedCodeList(sdFixedCode);
        if (sdFixedCodes.size() > 0) {
            sb.append(sdFixedCodes.get(0).getFixedCode());
            SdDevices plc = sdDevicesMapper.selectSdDevicesById(devices.getFEqId());
            String ip = plc.getIp().split("\\.")[3];
            sb.append(getIpleftPad(intToHex(Integer.parseInt(ip))) + "00");//目标ip地址
            sb.append(sdFixedCodes.get(0).getLocalIp());//本地IP
            sb.append("00" + sdFixedCodes.get(0).getQueryControl());//查询/控制
            sb.append(sdFixedCodes.get(0).getModelNum());//查询
            sb.append(getIpleftPad(intToHex(Integer.parseInt(seat))) + "00");//机位
            if (sdFixedCodes.get(0).getControlCode() != null) {
                sb.append(sdFixedCodes.get(0).getControlCode());//DM控制固定码
            }
        }
        return sb;
    }

    public String getIpleftPad(String ip) {
        return StringUtils.leftPad(ip, 4, "0");
    }

    public String intToHex(int n) {
        StringBuffer s = new StringBuffer();
        String a;
        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        while (n != 0) {
            s = s.append(b[n % 16]);
            n = n / 16;
        }
        a = s.reverse().toString();
        return a;
    }

    public Map checkDevices(SdDevices devices) {
        StringBuilder failureMsg = new StringBuilder();
        Map<String, Object> map = new HashMap<String, Object>();
        Long eqType = devices.getEqType();
        String fEqId = devices.getFEqId();
        //所属隧道
        SdTunnels sdTunnels = sdTunnelsService.selectSdTunnelsById(devices.getEqTunnelId());
        //设备类型
        SdEquipmentType sdEquipmentType = equipmentTypeService.selectSdEquipmentTypeById(eqType);
//        SdEquipmentState sdEquipmentState = sdEquipmentStateService.selectSdEquipmentStateById(devices.getEqType());
        if (!StringUtil.isEmpty(fEqId)) {
            //有些设备不需要关联PLC，只有当录入PLC主机ID时，才校验PLC
            SdDevices plc = sdDevicesMapper.selectSdDevicesById(fEqId);
            if (StringUtils.isNull(plc)) {
                failureMsg.append("、设备ID " + devices.getEqId() + " PLC主机ID不正确");
                map.put("flag", false);
                map.put("failureMsg", failureMsg);
                return map;
            }
        } else if (StringUtils.isNull(sdTunnels)) {
            failureMsg.append("、设备ID " + devices.getEqId() + " 所属隧道ID不正确");
            map.put("flag", false);
            map.put("failureMsg", failureMsg);
            return map;
        } else if (StringUtils.isNull(sdEquipmentType)) {
            failureMsg.append("、设备ID " + devices.getEqId() + " 设备类型ID不正确");
            map.put("flag", false);
            map.put("failureMsg", failureMsg);
            return map;
        }
        //设备方向
        //devices.getEqDirection();
        //控制模式+机位
        //devices.getDmcontrolSeat();
        //查询模式+机位 指令校验
        /*if (devices.getInstructionSeat()!=null){
            StringBuffer sbquery;
            if (devices.getInstructionSeat().contains("DM")){
                sbquery= getCommandCode(sdHosts.getPlcIp().split("\\.")[3],devices.getInstructionSeat().split("DM")[1],"DM","0");//查询
            }else {
                sbquery= getCommandCode(sdHosts.getPlcIp().split("\\.")[3],devices.getInstructionSeat().split("CIO")[1],"CIO","0");//查询
            }
            if (!devices.getEqControlPointAddress().contains(sbquery.toString()) ){
                failureMsg.append("、设备ID " + devices.getEqId() + " 指令不正确");
                map.put("flag",false);
                map.put("failureMsg",failureMsg);
                return map;
            }
        }*/
        map.put("flag", true);
        return map;
    }
}
