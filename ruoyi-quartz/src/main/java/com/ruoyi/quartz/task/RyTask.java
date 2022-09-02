package com.ruoyi.quartz.task;

import com.tunnel.platform.business.instruction.EquipmentControlInstruction;
import com.tunnel.platform.domain.dataInfo.SdDataTrafficHour;
import com.tunnel.platform.domain.dataInfo.SdEquipmentState;
import com.tunnel.platform.domain.event.SdStrategy;
import com.tunnel.platform.domain.event.SdStrategyRl;
import com.tunnel.platform.service.dataInfo.*;
import com.tunnel.platform.service.event.ISdStrategyRlService;
import com.tunnel.platform.service.event.ISdStrategyService;
import com.tunnel.platform.service.event.ISdWarningInfoService;
import com.tunnel.platform.service.logRecord.ISdOperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 定时任务调度测试
 *
 */
@Component("ryTask")
public class RyTask {

    private static final Logger log = LoggerFactory.getLogger(RyTask.class);

    @Autowired
    private ISdStrategyService sdStrategyService;
    @Autowired
    private ISdStrategyRlService sdStrategyRlService;
    @Autowired
    private ISdEquipmentStateService sdEquipmentStateService;
    @Autowired
    private ISdOperationLogService sdOperationLogService;
    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private ISdWarningInfoService sdWarningInfoService;
    @Autowired
    private ISdDataTrafficHourService sdDataTrafficHourService;
    @Autowired
    private ISdDataTrafficDayService sdDataTrafficDayService;
    @Autowired
    private ISdDataTrafficMonthService sdDataTrafficMonthService;

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params) {

    }

    public void ryNoParams() {
        System.out.println("执行无参方法");
    }

    public void strategyParams(String params) {
        System.out.println("执行无参方法");
        EquipmentControlInstruction instruc = new EquipmentControlInstruction();
        SdStrategy strategy = sdStrategyService.selectSdStrategyByJobRelationId(params);
        String strategyType = strategy.getStrategyType();
        String tunnelId = strategy.getTunnelId();
        SdStrategyRl rl = new SdStrategyRl();
        List<SdStrategyRl> ssgyRlList = sdStrategyRlService.selectSdStrategyRlList(rl);
        for (int i = 0; i < ssgyRlList.size(); i++) {
            //设备类型
            Long deviceTypeId = Long.parseLong(ssgyRlList.get(i).getEqTypeId());
            //设备状态码
            String codeDeviceState = ssgyRlList.get(i).getState();
            String eqs = ssgyRlList.get(i).getEquipments();
            String[] eqss = eqs.split(",");
            //设备list
            List<String> eqId = new ArrayList<String>();
            for (int j = 0; j < eqss.length; j++) {
                if ("全选".equals(eqss[j])){
                    continue;
                }
                eqId.add(eqss[j]);
            }
            SdEquipmentState state = new SdEquipmentState();
            state.setStateTypeId(deviceTypeId);
            state.setDeviceState(codeDeviceState);
            List<SdEquipmentState> stateList = sdEquipmentStateService.selectSdEquipmentStateList(state);
            //状态名称
            String stateName = stateList.get(0).getStateName();
            System.out.println("定时执行策略，调用发送指令接口【方法前】=====>>>guid：" + params);
            //调用发送指令
            instruc.controlInstruction(deviceTypeId, tunnelId, eqId, strategyType, stateName, codeDeviceState);
            System.out.println("定时执行策略，调用发送指令接口【结束】=====>>>guid：" + params);
        }
    }
}
