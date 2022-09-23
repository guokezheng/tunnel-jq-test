package com.tunnel.business.instruction;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.domain.dataInfo.SdDeviceCmd;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.service.dataInfo.ISdDeviceCmdService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.zc.common.core.redis.pubsub.RedisPubSub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EquipmentControlInstruction {

    @Autowired
    private ISdDeviceCmdService deviceCmdService;
    @Autowired
    private static ISdDeviceCmdService sdDeviceCmdService;
    @Autowired
    private static ISdDevicesService sdDevicesService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ISdOperationLogService operationLogService;
    @Autowired
    private static ISdOperationLogService sdOperationLogService;
    @Autowired
    private ISdDevicesService devicesService;

    @PostConstruct
    public void init() {
        sdDevicesService = devicesService;
        sdDeviceCmdService = deviceCmdService;
        sdOperationLogService = operationLogService;
    }



    /**
     * 设备控制
     */
    public void controlInstruction(Long deviceTypeId, String tunnelId, List<String> eqId, String strategyType, String stateName, String codeDeviceState) {
        for (int i = 0; i < eqId.size(); i++) {
            controlInstructionHandleOne(deviceTypeId, tunnelId, eqId.get(i), strategyType, stateName, codeDeviceState);
        	/*eqId.get(i);
            Map<String, Object> instruction = new HashMap<String, Object>();
            //设备id
            instruction.put("eqId", eqId.get(i));
            //策略类型 0：手动执行 1：定时控制 2：智能控制
            instruction.put("strategyType", strategyType);
            //状态名称(正红反绿，正绿反红)
            instruction.put("stateName", stateName);
            //设备报文状态
            instruction.put("codeDeviceState", codeDeviceState);
            SdDeviceCmd sdDeviceCmd = sdDeviceCmdService.selectSdDeviceCmd(eqId.get(i), codeDeviceState);
            String command = sdDeviceCmd.getCommand();
            instruction.put("command", command);
            RedisCache redisCache = new RedisCache();
            redisCache = (RedisCache) SpringContextUtils.getBean(RedisCache.class);
            String isSuc = redisCache.sendMsg(JSON.toJSONString(instruction));
            SdOperationLog sdOperationLog = new SdOperationLog();
            //设备状态（正红反绿，正绿反红）
            sdOperationLog.setOperationState(codeDeviceState);
            //控制方式（0：手动执行 1：定时控制 2：智能控制）
            sdOperationLog.setControlType(strategyType);
            sdOperationLog.setTunnelId(tunnelId);
            sdOperationLog.setEqId(eqId.get(i));
            sdOperationLog.setEqTypeId(deviceTypeId);
            sdOperationLog.setEqName(eqName);
            sdOperationLogService.insertSdOperationLog(sdOperationLog);*/
        }
    }

    /**
     * 设备控制
     *
     * @param deviceTypeId
     * @param tunnelId
     * @param eqId
     * @param strategyType
     * @param stateName
     * @param codeDeviceState
     */
    public void controlInstructionHandleOne(Long deviceTypeId, String tunnelId, String eqId, String strategyType, String stateName, String codeDeviceState) {
        Map<String, Object> instruction = new HashMap<>();
        //设备id
        instruction.put("eqId", eqId);
        //策略类型 0：手动执行 1：定时控制 2：智能控制
        instruction.put("strategyType", strategyType);
        //状态名称(正红反绿，正绿反红)
        instruction.put("stateName", stateName);
        //设备报文状态
        instruction.put("codeDeviceState", codeDeviceState);
        //隧道Id
        instruction.put("tunnelId", tunnelId);
        //设备类型
        instruction.put("deviceType", deviceTypeId);
        // 此处为风机-做特殊处理CIO点位控制：正转停止或反转停止
//        if (deviceTypeId == DevicesTypeEnum.FENG_JI_1.getCode() || deviceTypeId == DevicesTypeEnum.FENG_JI_2.getCode()) {
//            if ("3".equals(codeDeviceState)) {
//                SdDevices sdDevices = CmdUtil.deviceList.get(eqId);
//                SdHosts sdHosts = CmdUtil.hostList.get(sdDevices.getEqHostId());
//                if ("CIO".equals(sdHosts.getPlcControlType())) {
//                    redisCache = (RedisCache) SpringContextUtils.getBean(RedisCache.class);
//                    String state = redisCache.getCacheObject(eqId);
//                    if (!"".equals(state) && "2".equals(state)) {
//                        codeDeviceState = "4";
//                    }
//                }
//            }
//        }
        SdDeviceCmd sdDeviceCmd = sdDeviceCmdService.selectSdDeviceCmd(eqId, codeDeviceState);
        instruction.put("plcId", sdDeviceCmd.getCodePlcId());
        String command = sdDeviceCmd.getCommand();
        instruction.put("command", command);
        SpringUtils.getBean(RedisPubSub.class).publish("PLC:CONTROL", JSON.toJSONString(instruction));
        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(deviceTypeId);//设备类型
        sdOperationLog.setEqId(eqId);//设备id
        sdOperationLog.setCreateTime(DateUtils.getNowDate());
        sdOperationLog.setOperationState(codeDeviceState);
        sdOperationLog.setControlType(strategyType);
        sdOperationLog.setUserName("admin");
        sdOperationLog.setTunnelId(tunnelId);
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
    }



}

