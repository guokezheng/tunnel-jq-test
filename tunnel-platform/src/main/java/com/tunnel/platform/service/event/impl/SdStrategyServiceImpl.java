package com.tunnel.platform.service.event.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.platform.business.instruction.EquipmentControlInstruction;
import com.tunnel.platform.domain.dataInfo.SdDeviceCmd;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.domain.dataInfo.SdEquipmentState;
import com.tunnel.platform.domain.dataInfo.SdEquipmentType;
import com.tunnel.platform.domain.event.*;
import com.tunnel.platform.mapper.dataInfo.SdEquipmentStateMapper;
import com.tunnel.platform.mapper.dataInfo.SdEquipmentTypeMapper;
import com.tunnel.platform.mapper.event.*;
import com.tunnel.platform.service.dataInfo.ISdDeviceCmdService;
import com.tunnel.platform.service.dataInfo.ISdDevicesService;
import com.tunnel.platform.service.event.ISdStrategyService;
import com.zc.common.core.redis.RedisPubSub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制策略Service业务层处理
 *
 * @author gongfanfei
 * @date 2020-08-27
 */
@Service
public class SdStrategyServiceImpl implements ISdStrategyService {

    @Autowired
    private SdStrategyMapper sdStrategyMapper;
    @Autowired
    private SdStrategyRlMapper sdStrategyRlMapper;
    @Autowired
    private SdEquipmentStateMapper sdEquipmentStateMapper;
    @Autowired
    private SdEquipmentTypeMapper sdEquipmentTypeMapper;

    @Autowired
    private SdStrategyBackMapper sdStrategyBackMapper;
    @Autowired
    private ISdDeviceCmdService sdDeviceCmdService;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private static RedisCache redisCache;

    @Autowired
    SdTriggerMapper sdTriggerMapper;

    @Autowired
    SdTriggerDeviceMapper sdTriggerDeviceMapper;

    /**
     * 查询控制策略
     *
     * @param id 控制策略ID
     * @return 控制策略
     */
    @Override
    public SdStrategy selectSdStrategyById(Long id) {
        return sdStrategyMapper.selectSdStrategyById(id);
    }

    /**
     * 查询控制策略
     */
    @Override
    public SdStrategy selectSdStrategyByJobRelationId(String jobRelationId) {
        return sdStrategyMapper.selectSdStrategyByJobRelationId(jobRelationId);
    }

    /**
     * 查询控制策略列表
     *
     * @param sdStrategy 控制策略
     * @return 控制策略
     */
    @Override
    public List<SdStrategy> selectSdStrategyList(SdStrategy sdStrategy) {
        Long deptId = SecurityUtils.getDeptId();
        sdStrategy.getParams().put("deptId",deptId);
        List<SdStrategy> list = sdStrategyMapper.selectSdStrategyList(sdStrategy);
        for (int i = 0; i < list.size(); i++) {
            List<String> sList = new ArrayList<String>();
            SdStrategyRl rl = new SdStrategyRl();
            rl.setStrategyId(list.get(i).getId());
            String strategyType = list.get(i).getStrategyType();
            if ("1".equals(strategyType)) {
                sList.add(list.get(i).getStrategyInfo());
            }
            List<SdStrategyRl> rlList = sdStrategyRlMapper.selectSdStrategyRlList(rl);
            //策略关联表信息
            for (int j = 0; j < rlList.size(); j++) {
                SdEquipmentType typeObject = sdEquipmentTypeMapper.selectSdEquipmentTypeById(Long.parseLong(rlList.get(j).getEqTypeId()));
                String typeName = typeObject.getTypeName();//设备类型名称
                //SdEquipmentState stateObject = sdEquipmentStateMapper.selectSdEquipmentStateById(Long.parseLong(rlList.get(j).getState()));
                SdEquipmentState state = new SdEquipmentState();
                state.setStateTypeId(Long.parseLong(rlList.get(j).getEqTypeId()));
                state.setDeviceState(rlList.get(j).getState());

                // SdEquipmentState stateObject = sdEquipmentStateMapper.selectSdEquipmentStateById(Long.parseLong(rlList.get(j).getState()));
                List<SdEquipmentState> stateObject = sdEquipmentStateMapper.selectDropSdEquipmentStateList(state);
                String stateName = stateObject.get(0).getStateName();//设备状态名称
                sList.add(typeName + "控制执行：" + stateName + "；");
            }

            list.get(i).setSlist(sList);
        }
        return list;
    }

    /**
     * 新增控制策略
     *
     * @param sdStrategy 控制策略
     * @return 结果
     */
    @Override
    public int insertSdStrategy(SdStrategy sdStrategy) {
        sdStrategy.setCreateTime(DateUtils.getNowDate());
        return sdStrategyMapper.insertSdStrategy(sdStrategy);
    }

    /**
     * 修改控制策略
     *
     * @param sdStrategy 控制策略
     * @return 结果
     */
    @Override
    public int updateSdStrategy(SdStrategy sdStrategy) {
        sdStrategy.setUpdateTime(DateUtils.getNowDate());
        return sdStrategyMapper.updateSdStrategyById(sdStrategy);
    }

    /**
     * 批量删除控制策略
     *
     * @param ids 需要删除的控制策略ID
     * @return 结果
     */
    @Override
    public int deleteSdStrategyByIds(Long[] ids) {
        return sdStrategyMapper.deleteSdStrategyByIds(ids);
    }

    /**
     * 删除控制策略信息
     *
     * @param id 控制策略ID
     * @return 结果
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteSdStrategyById(Long id) {
        // 删除策略之前要先判断策略是否已经在预案管理中被引用
        SdStrategy sdStrategy = new SdStrategy();
        sdStrategy.setId(id);
        List<SdStrategy> sdStrategies = sdStrategyMapper.selectSdStrategyList(sdStrategy);
        if (sdStrategies.size() > 0 && sdStrategies.size() == 1) {
            List<Map<String, Object>> maps = sdStrategyMapper.checkStrategyIfExist(id);
            if (maps.size() > 0) {
                throw new RuntimeException("控制策略已经在预案管理中引用，不可删除!");
            }
        } else {
            throw new RuntimeException("控制策略数据存在异常，请联系管理员。");
        }
        int result = sdStrategyMapper.deleteSdStrategyById(id);
        if (result >0) {
            sdStrategyRlMapper.deleteSdStrategyRlByStrategyId(id);
            SdTrigger sdTrigger = sdTriggerMapper.selectSdTriggerByRelateId(id);
            if (sdTrigger != null){
                if (sdTrigger.getId() < 0) {
                    sdTriggerMapper.deleteSdTriggerById(sdTrigger.getId());
                    sdTriggerDeviceMapper.deleteSdTriggerDeviceByTriggerId(sdTrigger.getId());
                }
            }
        }
        return result;
    }

    /**
     * 批量删除控制策略信息
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteSdStrategyByStrategyIds(Long[] ids) {
        // 删除策略之前要先判断策略是否已经在预案管理中被引用
        for (int i = 0; i < ids.length; i++) {
            SdStrategy sdStrategy = new SdStrategy();
            Long id = ids[i];
            sdStrategy.setId(id);
            List<SdStrategy> sdStrategies = sdStrategyMapper.selectSdStrategyList(sdStrategy);
            if (sdStrategies.size() > 0 && sdStrategies.size() == 1) {
                List<Map<String, Object>> maps = sdStrategyMapper.checkStrategyIfExist(id);
                if (maps.size() > 0) {
                    throw new RuntimeException("控制策略已经在预案管理中引用，不可删除!");
                }
            } else {
                throw new RuntimeException("控制策略数据存在异常，请联系管理员。");
            }
        }
        int result = sdStrategyMapper.deleteSdStrategyByIds(ids);
        if (result >0) {
            sdStrategyRlMapper.deleteSdStrategyRlByStrategyIds(ids);
            for (Long id : ids) {
                SdTrigger sdTrigger = sdTriggerMapper.selectSdTriggerByRelateId(id);
                if (sdTrigger != null){
                    if (sdTrigger.getId() < 0) {
                        sdTriggerMapper.deleteSdTriggerById(sdTrigger.getId());
                        sdTriggerDeviceMapper.deleteSdTriggerDeviceByTriggerId(sdTrigger.getId());
                    }
                }
            }
        }
        return result;
    }

    /**
     * 新增控制策略
     * 对应批量新增关联子表信息
     *
     * @param model 控制策略
     * @return 结果
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int addStrategysInfo(SdStrategyModel model) {
        List<Map> equipment = model.getEquipment();
        if (StringUtils.isNotEmpty(model.getEquipment())) {
            for (int i = 0; i < equipment.size(); i++){
                Map<String,Object> map = equipment.get(i);
                if (StringUtils.isEmpty(map.get("equipmentTypeId")+"")) {
                    throw new RuntimeException("请选择设备类型！");
                }
            }
        }
        List<SdStrategyRl> list = new ArrayList<SdStrategyRl>();
        SdTrigger sdTrigger = model.getTriggers();
        //策略基础表
        SdStrategy sty = new SdStrategy();
        //策略类型
        sty.setStrategyType(model.getStrategyType());
        sty.setStrategyState(model.getStrategyState());
        sty.setStrategyName(model.getStrategyName());
        sty.setTunnelId(model.getTunnelId());
        sty.setWarningId(model.getWarningId());
        sty.setJobRelationId(model.getJobRelationId());
        sty.setSchedulerTime(model.getSchedulerTime());
        sty.setStrategyInfo(model.getStrategyInfo());
        sty.setDirection(model.getDirection());
        sty.setCreateBy(SecurityUtils.getUsername());
        int insetStrResult = sdStrategyMapper.insertSdStrategy(sty);
        if ("1".equals(model.getStrategyType()) || "2".equals(model.getStrategyType())){
            String[] equipments = model.getEquipments().split("#");
            String[] equipmentType = model.getEquipmentTypeId().split("#");
            String[] equipmentState = model.getEquipmentState().split("#");
            for (int i = 0; i < equipmentState.length; i++) {
                SdStrategyRl rl = new SdStrategyRl();
                rl.setEqTypeId(equipmentType[i]);
                rl.setEquipments(equipments[i]);
                rl.setState(equipmentState[i]);
                rl.setStrategyId(sty.getId());
                list.add(rl);
            }
        }else {
            for (int i = 0; i < equipment.size(); i++) {
                Map<String, Object> map = equipment.get(i);
                String equipments = map.get("equipments").toString().replaceAll("#", ",");
                String equipmentTypeId = map.get("equipmentTypeId") + "";
                String eqState = (String) map.get("eqState");
                SdStrategyRl rl = new SdStrategyRl();
                rl.setEqTypeId(equipmentTypeId);
                rl.setEquipments(equipments);
                rl.setState(eqState);
                rl.setStrategyId(sty.getId());
                list.add(rl);
            }
        }
        // 新增策略子表
        int result = 1;
        if (list.size() != 0) {
            int insertBranchResult = sdStrategyRlMapper.batchInsertStrategyRl(list);
            if (insertBranchResult < 0 || insetStrResult < 0) {
                result = -1;
            }
        }
        if ("2".equals(model.getStrategyType())) {
            // 策略触发器表
            sdTrigger.setRelateId(sty.getId());
            int insertSdTrigger = sdTriggerMapper.insertSdTrigger(sdTrigger);
            if (insertSdTrigger > 0) {
                SdTriggerDevice sdTriggerDevice = new SdTriggerDevice();
                sdTriggerDevice.setTriggerId(sdTrigger.getId());
                sdTriggerDevice.setDeviceId(sdTrigger.getDeviceId());
                int insertSdTriggerDevice = sdTriggerDeviceMapper.insertSdTriggerDevice(sdTriggerDevice);
                if (insertSdTriggerDevice < 0) {
                    result = -1;
                }
            }
        }
        return result;
    }

    /**
     * 修改控制策略信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateSdStrategyInfo(SdStrategyModel model) {
        List<Map> equipment = model.getEquipment();
        SdTrigger sdTrigger = model.getTriggers();
        List<SdStrategyRl> list = new ArrayList<SdStrategyRl>();
        //策略基础表
        SdStrategy sty = new SdStrategy();
        //策略类型
        sty.setId(model.getId());
        sty.setStrategyType(model.getStrategyType());
        sty.setStrategyState(model.getStrategyState());
        sty.setStrategyName(model.getStrategyName());
        sty.setTunnelId(model.getTunnelId());
        sty.setWarningId(model.getWarningId());
        sty.setSchedulerTime(model.getSchedulerTime());
        sty.setStrategyInfo(model.getStrategyInfo());
        sty.setDirection(model.getDirection());
        sty.setCreateBy(SecurityUtils.getUsername());
        int result = 1;
        //1.0  更新策略 主表
        int upStr = sdStrategyMapper.updateSdStrategyById(sty);
        if (upStr < 0) {
            result = -1;
        }
        //2.0  删除关联子表相关信息
        int upStrRl = sdStrategyRlMapper.deleteSdStrategyRlByStrategyId(model.getId());
        if (upStrRl < 0) {
            result = -1;
        }
        //3.0  插入关联子表新的相关信息
        if ( "1".equals(model.getStrategyType()) ){
            String[] equipments = model.getEquipments().split("#");
            String[] equipmentType = model.getEquipmentTypeId().split("#");
            String[] equipmentState = model.getEquipmentState().split("#");
            for (int i = 0; i < equipmentState.length; i++) {
                SdStrategyRl rl = new SdStrategyRl();
                rl.setEqTypeId(equipmentType[i]);
                rl.setEquipments(equipments[i]);
                rl.setState(equipmentState[i]);
                rl.setStrategyId(sty.getId());
                list.add(rl);
            }
        } else  {
            for (int i = 0; i < equipment.size(); i++) {
                Map<String, Object> map = equipment.get(i);
                String equipments = map.get("equipments").toString().replaceAll("#", ",");
                String equipmentTypeId = map.get("equipmentTypeId") + "";
                String eqState = (String) map.get("eqState");
                SdStrategyRl rl = new SdStrategyRl();
                rl.setEqTypeId(equipmentTypeId);
                rl.setEquipments(equipments);
                rl.setState(eqState);
                rl.setStrategyId(sty.getId());
                list.add(rl);
            }
        }
        int insertBranchInfo = sdStrategyRlMapper.batchInsertStrategyRl(list);
        if (insertBranchInfo < 0) {
            result = -1;
        }
        //删除触发器
        int upTrigger = sdTriggerMapper.deleteSdTriggerByRelateId(model.getId());
        if (upTrigger < 0) {
            result = -1;
        }
        if ("2".equals(model.getStrategyType())) {
            //删除触发器关联设备
            int deviceByTId = sdTriggerDeviceMapper.deleteSdTriggerDeviceByTriggerId(model.getTriggers().getId());
            if (deviceByTId < 0) {
                result = -1;
            }
            //添加触发器
            sdTrigger.setRelateId(model.getId());
            int insertSdTrigger = sdTriggerMapper.insertSdTrigger(sdTrigger);
            if (insertSdTrigger > 0) {
                SdTriggerDevice sdTriggerDevice = new SdTriggerDevice();
                sdTriggerDevice.setTriggerId(sdTrigger.getId());
                sdTriggerDevice.setDeviceId(sdTrigger.getDeviceId());
                int insertSdTriggerDevice = sdTriggerDeviceMapper.insertSdTriggerDevice(sdTriggerDevice);
                if (insertSdTriggerDevice < 0) {
                    result = -1;
                }
            }
        }
        return result;
    }

    /**
     * @param id 策略id
     *           手动控制策略
     */
    @Override
    public void handleStrategy(Long id) {
        EquipmentControlInstruction instruc = new EquipmentControlInstruction();
        SdStrategy strategy = sdStrategyMapper.selectSdStrategyById(id);
        String strategyType = strategy.getStrategyType();
        String tunnelId = strategy.getTunnelId();
        SdStrategyRl rl = new SdStrategyRl();
        rl.setStrategyId(strategy.getId());
        List<SdStrategyRl> ssgyRlList = sdStrategyRlMapper.selectSdStrategyRlList(rl);
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
            state.setStateType("2");
            List<SdEquipmentState> stateList = sdEquipmentStateMapper.selectDropSdEquipmentStateList(state);
            // 状态名称
            String stateName = stateList.get(0).getStateName();
            System.out.println("手动执行策略，调用发送指令接口【方法前】=====>>>guid：" + id);
            //调用发送指令
//        	instruc.controlInstruction(deviceTypeId, hostId,tunnelId, eqId, strategyType, stateName, codeDeviceState);
            System.out.println("手动执行策略，调用发送指令接口【结束】=====>>>guid：" + id);
        }
    }

    public void handleStrategyByIds(Long[] ids, Long warId) {
        for (Long id : ids) {
            EquipmentControlInstruction instruc = new EquipmentControlInstruction();
            SdStrategy strategy = sdStrategyMapper.selectSdStrategyById(id);
            String strategyType = strategy.getStrategyType();
            String tunnelId = strategy.getTunnelId();
            SdStrategyRl rl = new SdStrategyRl();
            List<SdStrategyRl> ssgyRlList = sdStrategyRlMapper.selectSdStrategyRlList(rl);
            for (int i = 0; i < ssgyRlList.size(); i++) {
                //设备类型
                Long deviceTypeId = Long.parseLong(ssgyRlList.get(i).getEqTypeId());
                //设备状态码
                String codeDeviceState = ssgyRlList.get(i).getState();
                String eqs = ssgyRlList.get(i).getEquipments();
                String[] eqss = eqs.split(",");
                //状态名称
                String stateName = getStateName(deviceTypeId, codeDeviceState);
                //设备list
                List<String> eqId = new ArrayList<String>();
                for (int j = 0; j < eqss.length; j++) {
					if ("全选".equals(eqss[j])){
						continue;
					}
                    eqId.add(eqss[j]);
                    sdStrategyBackMapper.deleteSdStrategyBackByWarId(warId);
                    SdStrategyBack strategyBack = new SdStrategyBack();
                    strategyBack.setEqId(eqss[j]);
                    strategyBack.setWarningId(warId);
                    String deviceState = (String) redisCache.getCacheObject(String.valueOf(eqss[j]));
                    strategyBack.setDeviceState(deviceState);
                    strategyBack.setStateName(getStateName(deviceTypeId, deviceState));
                    strategyBack.setStrategyType(strategyType);
                    strategyBack.setTunnelId(tunnelId);
                    sdStrategyBackMapper.insertSdStrategyBack(strategyBack);
                }
                System.out.println("手动执行策略，调用发送指令接口【方法前】=====>>>guid：" + id);
                //调用发送指令
//				instruc.controlInstruction(deviceTypeId, tunnelId, eqId, strategyType, stateName, codeDeviceState);
                System.out.println("手动执行策略，调用发送指令接口【结束】=====>>>guid：" + id);
            }
        }
    }

    public String getStateName(Long deviceTypeId, String codeDeviceState) {
        SdEquipmentState state = new SdEquipmentState();
        state.setStateTypeId(deviceTypeId);
        state.setDeviceState(codeDeviceState);
        List<SdEquipmentState> stateList = sdEquipmentStateMapper.selectDropSdEquipmentStateList(state);
        //状态名称
        String stateName = stateList.get(0).getStateName();
        return stateName;
    }

    @Override
    public void handleStrategyRollBack(Long warId) {
        List<SdStrategyBack> sdStrategyBacks = sdStrategyBackMapper.selectSdStrategyBackByWarId(warId);
        for (SdStrategyBack strategyBack : sdStrategyBacks) {
            Map<String, Object> instruction = new HashMap<String, Object>();
            //设备id
            instruction.put("eqId", strategyBack.getEqId());
            //策略类型 0：手动执行 1：定时控制 2：智能控制
            instruction.put("strategyType", strategyBack.getStrategyType());
            //状态名称(正红反绿，正绿反红)
            instruction.put("stateName", strategyBack.getStateName());
            //设备报文状态
            instruction.put("codeDeviceState", strategyBack.getDeviceState());
            instruction.put("tunnelId", strategyBack.getTunnelId());
            SdDeviceCmd sdDeviceCmd = sdDeviceCmdService.selectSdDeviceCmd(strategyBack.getEqId(), String.valueOf(strategyBack.getDeviceState()));
            SdDevices sdDevices = sdDevicesService.selectSdDevicesById(strategyBack.getEqId());
            String equipmentName = sdDevices.getEqName();
            instruction.put("plcId", sdDeviceCmd.getCodePlcId());
            String command = sdDeviceCmd.getCommand();
            instruction.put("command", command);
            SpringUtils.getBean(RedisPubSub.class).publish("PLC_CONTROL", JSON.toJSONString(instruction));
        }
    }

}
