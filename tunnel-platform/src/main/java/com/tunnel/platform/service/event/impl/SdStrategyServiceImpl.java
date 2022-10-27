package com.tunnel.platform.service.event.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.job.TaskException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.quartz.mapper.SysJobMapper;
import com.ruoyi.quartz.service.impl.SysJobServiceImpl;
import com.tunnel.business.datacenter.util.CronUtil;
import com.tunnel.business.domain.dataInfo.SdDeviceCmd;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdEquipmentState;
import com.tunnel.business.domain.dataInfo.SdEquipmentType;
import com.tunnel.business.domain.event.*;
import com.tunnel.business.instruction.EquipmentControlInstruction;
import com.tunnel.business.mapper.dataInfo.SdEquipmentStateMapper;
import com.tunnel.business.mapper.dataInfo.SdEquipmentTypeMapper;
import com.tunnel.business.mapper.event.*;
import com.tunnel.business.service.dataInfo.ISdDeviceCmdService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.platform.service.event.ISdStrategyService;
import com.zc.common.core.redis.pubsub.RedisPubSub;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    private SdTriggerMapper sdTriggerMapper;

    @Autowired
    private SdTriggerDeviceMapper sdTriggerDeviceMapper;

    @Autowired
    private SysJobMapper sysJobMapper;




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
     * 获取分时控制数据
     * @return
     */
    @Override
    public List<Map> getTimeSharingInfo(String tunnelId) {
        return sdStrategyMapper.getTimeSharingInfo(tunnelId);
    }

    /**
     * 分时控制修改控制时间
     * @param strategyId
     * @param controlTime
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateControlTime(Long strategyId, String controlTime) {
        String[] timeParam = controlTime.split("-");
        SdStrategy strategy = sdStrategyMapper.selectSdStrategyById(strategyId);
        String[] relationId = strategy.getJobRelationId().split(",");
        List<SdStrategyRl> rlList = sdStrategyRlMapper.selectSdStrategyRlByStrategyId(strategyId);
        if(rlList.size() < 2){
            return 0;
        }
        //排序保证修改顺序
        rlList = rlList.stream().sorted((Comparator.comparing(SdStrategyRl::getId))).collect(Collectors.toList());
        try {
            int updateRows = 0;
            for (int i=0;i<timeParam.length;i++) {
                SdStrategyRl rl = rlList.get(i);
                rl.setControlTime(timeParam[i]);
                updateRows += sdStrategyRlMapper.updateSdStrategyRl(rl);
                Long jobId = Long.valueOf(relationId[i]);
                SysJob job = sysJobMapper.selectJobById(jobId);
                job.setCronExpression(CronUtil.CronDate(timeParam[i]));
                updateRows += sysJobMapper.updateJob(job);
                if(updateRows < 2){
                    return 0;
                }
                updateRows = 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("数据处理异常");
        }
        return 1;
    }

    /**
     * 控制策略开关
     * @param strategyId
     * @param change
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int strategySwitch(Long strategyId, String change) {
        SdStrategy sdStrategy = sdStrategyMapper.selectSdStrategyById(strategyId);
        sdStrategy.setStrategyState(change);
        int updateRows = sdStrategyMapper.updateSdStrategyById(sdStrategy);
        if(updateRows < 1){
            return 0;
        }
        if ("1".equals(sdStrategy.getStrategyType()) || "3".equals(sdStrategy.getStrategyType())) {
            String relationId = sdStrategy.getJobRelationId();
            if(StrUtil.isBlank(relationId)) {
                return 1;
            }
            String[] jobIds = relationId.split(",");
            SysJob job = new SysJob();
            updateRows = 0;
            for (int i = 0; i < jobIds.length; i++) {
                job = sysJobMapper.selectJobById(Long.valueOf(jobIds[i]));
                job.setStatus(change);
                updateRows += sysJobMapper.updateJob(job);
            }
            if(updateRows < jobIds.length){
                throw new RuntimeException("数据处理异常");
            }
        }
        return 1;
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
                if(stateObject.size()<1){
                    continue;
                }
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
        /*SdStrategy sdStrategy = new SdStrategy();
        sdStrategy.setId(id);*/
        SdStrategy sdStrategy = sdStrategyMapper.selectSdStrategyById(id);
        if (!ObjectUtils.isEmpty(sdStrategy)) {
            List<Map<String, Object>> maps = sdStrategyMapper.checkStrategyIfExist(id);
            if (maps.size() > 0) {
                throw new RuntimeException("控制策略已经在预案管理中引用，不可删除!");
            }
        } else {
            throw new RuntimeException("控制策略数据存在异常，请联系管理员。");
        }
        //删除定时任务
        if("3".equals(sdStrategy.getStrategyType()) || "1".equals(sdStrategy.getStrategyType())){
            deleteRefJob(sdStrategy.getJobRelationId());
        }
        int result = sdStrategyMapper.deleteSdStrategyById(id);
        if (result >0) {
            sdStrategyRlMapper.deleteSdStrategyRlByStrategyId(id);
            if ("2".equals(sdStrategy.getStrategyType())){
                SdTrigger sdTrigger = sdTriggerMapper.selectSdTriggerByRelateId(id);
                if (sdTrigger.getId() < 0) {
                    sdTriggerMapper.deleteSdTriggerById(sdTrigger.getId());
                    result = sdTriggerDeviceMapper.deleteSdTriggerDeviceByTriggerId(sdTrigger.getId());
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
            //查询策略下的所有定时任务ID
            try {
                Long[] jobIds = sdStrategyMapper.getStrategyRefJobIds(ids);
                sysJobMapper.deleteJobByIds(jobIds);
            } catch (Exception e) {
                throw new RuntimeException("定时任务处理异常");
            }
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
        // 判断是否符合规范并返回策略
        SdStrategy sty = conditionalJudgement(model);
        int insetStrResult = sdStrategyMapper.insertSdStrategy(sty);
        if(insetStrResult < 1){
            throw new RuntimeException("数据保存异常");
        }
        int result = saveRelation(model, sty);
        return result;
    }

    /**
     * 策略删除相关联的定时任务
     * @param jobRelationId
     */
    private void deleteRefJob(String jobRelationId){
        try {
            //删除关联定时任务
            String[] jobIds = jobRelationId.split(",");
            Long[] param = new Long[jobIds.length];
            for (int i = 0; i < jobIds.length; i++) {
                param[i] = Long.valueOf(jobIds[i]);
            }
            sysJobMapper.deleteJobByIds(param);
        } catch (Exception e) {
            throw new RuntimeException("定时任务处理异常");
        }
    }
    /**
     * 修改控制策略信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateSdStrategyInfo(SdStrategyModel model) {
        //判断输入的值是否符合规范并返回策略信息
        SdStrategy sty = conditionalJudgement(model);
        String strategyType = model.getStrategyType();
        //更新策略 主表
        int updatePrimary = sdStrategyMapper.updateSdStrategyById(sty);
        //删除关联子表相关信息
        int upStrRl = sdStrategyRlMapper.deleteSdStrategyRlByStrategyId(model.getId());
        if (upStrRl < 1 || updatePrimary < 1) {
            throw new RuntimeException("数据处理异常");
        }
        //删除相关联的定时任务
        if ("1".equals(model.getStrategyType()) || "3".equals(model.getStrategyType())) {
            deleteRefJob(sty.getJobRelationId());
        }
        //触发策略
        if ("2".equals(strategyType)) {
            //删除触发器
            int deleteTriggerRows = sdTriggerMapper.deleteSdTriggerByRelateId(model.getId());
            //删除触发器关联设备
            int deleteSdTriggerDeviceRows = sdTriggerDeviceMapper.deleteSdTriggerDeviceByTriggerId(model.getTriggers().getId());
            if (deleteTriggerRows < 1 || deleteSdTriggerDeviceRows < 1) {
                throw new RuntimeException("数据处理异常");
            }
        }
        //重新插入关系表及定时任务信息
        int relation = saveRelation(model, sty);
        return relation;
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

    /**
     * 判断策略是否符合规范
     * @param model
     * @return
     */
    private SdStrategy conditionalJudgement(SdStrategyModel model) {
        if ("0".equals(model.getStrategyType())) {
            List<Map> manualControl = model.getManualControl();
            long num = manualControl.stream().filter(s -> StrUtil.isBlank((String)s.get("state"))).count();
            if(num > 0)
                throw new RuntimeException("请填写完整手动控制！");
        } else {
            List<Map> timeSharingControl = model.getAutoControl();
            long num = timeSharingControl.stream().filter(s -> StrUtil.isBlank((String)s.get("state"))).count();
            if(num > 0)
                throw new RuntimeException("请填写完整策略信息！");
        }
        SdStrategy sty = new SdStrategy();
        //策略类型
        sty.setId(model.getId());
        sty.setStrategyType(model.getStrategyType());
        sty.setStrategyState("1");
        sty.setStrategyName(model.getStrategyName());
        sty.setTunnelId(model.getTunnelId());
        sty.setWarningId(model.getWarningId());
        sty.setJobRelationId(model.getJobRelationId());
        if("1".equals(model.getStrategyType())) {
            sty.setSchedulerTime(model.getSchedulerTime());
            sty.setStrategyInfo(model.getStrategyInfo());
        }
        sty.setDirection(model.getDirection());
        sty.setCreateBy(SecurityUtils.getUsername());
        return sty;
    }
    /**
     * 手动控制
     * @param model
     * @param sty
     * @return
     */
    private int manualControl(SdStrategyModel model,SdStrategy sty){
        List<Map> manualControl = model.getManualControl();
        int addRows = 0;
        for (Map<String,Object> map : manualControl) {
            List<String> value = (List<String>) map.get("value");
            String equipments = StringUtils.join(value,",");
            String equipmentTypeId = model.getEquipmentTypeId();
            String state = (String) map.get("state");
            SdStrategyRl sdStrategyRl = new SdStrategyRl();
            sdStrategyRl.setEquipments(equipments);
            sdStrategyRl.setState(state);
            sdStrategyRl.setEqTypeId(equipmentTypeId);
            sdStrategyRl.setStrategyId(sty.getId());
            addRows += sdStrategyRlMapper.insertSdStrategyRl(sdStrategyRl);
        }
        if(addRows < 1){
            throw new RuntimeException("数据处理异常");
        }
        return 1;
    }
    /**
     * 定时控制
     * @param model
     * @param sty
     * @return
     */
    private int timingControl(SdStrategyModel model,SdStrategy sty){
        List<Map> autoControl = model.getAutoControl();
        List<String> jobIdList = new ArrayList<>();
        for (Map<String,Object> map : autoControl) {
            List<String> value = (List<String>) map.get("value");
            String equipments = StringUtils.join(value,",");
            String equipmentTypeId = map.get("type") + "";
            String eqState = (String) map.get("state");
            SdStrategyRl rl = new SdStrategyRl();
            rl.setEqTypeId(equipmentTypeId);
            rl.setEquipments(equipments);
            rl.setState(eqState);
            rl.setStrategyId(sty.getId());
            sdStrategyRlMapper.insertSdStrategyRl(rl);
            Long refId = rl.getId();
            //新增定时任务
            try {
                SysJob job = new SysJob();
                // 定时任务名称
                job.setJobName(model.getStrategyName());
                // 调用目标字符串
                job.setInvokeTarget("strategyTask.strategyParams('" + refId + "')");
                // corn表达式
                job.setCronExpression(sty.getSchedulerTime());
                // 计划执行错误策略（1立即执行 2执行一次 3放弃执行）
                job.setMisfirePolicy("1");
                // 是否并发执行（0允许 1禁止）
                job.setConcurrent("0");
                // 状态（0正常 1暂停）
                job.setStatus("0");
                sysJobMapper.insertJob(job);
                jobIdList.add(job.getJobId().toString());
            } catch (Exception e) {
                throw new RuntimeException("添加定时任务失败！");
            }
        }
        String jobIdStr = jobIdList.stream().collect(Collectors.joining(","));
        sty.setJobRelationId(jobIdStr);
        int updateRows = sdStrategyMapper.updateSdStrategyById(sty);
        return updateRows;
    }
    /**
     * 分时控制
     * @param model
     * @param sty
     * @return
     */
    private int timeSharingControl(SdStrategyModel model,SdStrategy sty){
        List<Map> timeSharingControl = model.getAutoControl();
        List<String> jobIdList = new ArrayList<>();
        for (Map<String,Object> map : timeSharingControl) {
            List<String> value = (List<String>) map.get("value");
            String equipments = StringUtils.join(value,",");
            String equipmentTypeId = map.get("type") + "";
            String eqState = (String) map.get("state");
            String timeStr = map.get("controlTime").toString();
            try{
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
//                Date time = dateFormat.parse(timeStr.replace("Z","+0000"));
//                String controlTime = new SimpleDateFormat("HH:mm:ss").format(time);
                SdStrategyRl rl = new SdStrategyRl();
                rl.setEqTypeId(equipmentTypeId);
                rl.setEquipments(equipments);
                rl.setState(eqState);
                rl.setStrategyId(sty.getId());
                rl.setControlTime(timeStr);
                sdStrategyRlMapper.insertSdStrategyRl(rl);
                Long refId = rl.getId();
                //新增定时任务
                try {
                    SysJob job = new SysJob();
                    // 定时任务名称
                    job.setJobName(model.getStrategyName());
                    // 调用目标字符串
                    job.setInvokeTarget("strategyTask.strategyParams('" + refId + "')");
                    // corn表达式
                    String cronDate = CronUtil.CronDate(timeStr);
                    job.setCronExpression(cronDate);
                    // 计划执行错误策略（1立即执行 2执行一次 3放弃执行）
                    job.setMisfirePolicy("1");
                    // 是否并发执行（0允许 1禁止）
                    job.setConcurrent("0");
                    // 状态（0正常 1暂停）
                    job.setStatus("0");
                    sysJobMapper.insertJob(job);
                    jobIdList.add(job.getJobId().toString());
                } catch (Exception e) {
                    throw new RuntimeException("添加定时任务失败！");
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        String jobIdStr = jobIdList.stream().collect(Collectors.joining(","));
        sty.setJobRelationId(jobIdStr);
        int updateRows = sdStrategyMapper.updateSdStrategyById(sty);
        return updateRows;
    }
    /**
     * 触发控制
     * @param model
     * @param sty
     * @return
     */
    private int triggerControl(SdStrategyModel model,SdStrategy sty){
        List<Map> autoControl = model.getAutoControl();
        for (Map<String,Object> map : autoControl) {
            List<String> value = (List<String>) map.get("value");
            String equipments = StringUtils.join(value,",");
            String equipmentTypeId = map.get("type") + "";
            String eqState = (String) map.get("state");
            SdStrategyRl rl = new SdStrategyRl();
            rl.setEqTypeId(equipmentTypeId);
            rl.setEquipments(equipments);
            rl.setState(eqState);
            rl.setStrategyId(sty.getId());
            sdStrategyRlMapper.insertSdStrategyRl(rl);
        }
        // 得到触发器
        SdTrigger sdTrigger = model.getTriggers();
        // 添加策略触发器表
        sdTrigger.setRelateId(sty.getId());
        int insertSdTrigger = sdTriggerMapper.insertSdTrigger(sdTrigger);
        // 添加触发器关联设备表
        if (insertSdTrigger > 0) {
            SdTriggerDevice sdTriggerDevice = new SdTriggerDevice();
            sdTriggerDevice.setTriggerId(sdTrigger.getId());
            sdTriggerDevice.setDeviceId(sdTrigger.getDeviceId());
            int insertSdTriggerDevice = sdTriggerDeviceMapper.insertSdTriggerDevice(sdTriggerDevice);
            if(insertSdTriggerDevice > 0){
                return 1;
            }
        }
        throw new RuntimeException("触发器关联设备异常");
    }
    /**
     * 策略维护关系表
     * @param model
     * @return
     */
    private int saveRelation(SdStrategyModel model,SdStrategy sty) {
        String strategyType = model.getStrategyType();
        switch (strategyType) {
            case "0":
                return manualControl(model, sty);
            case "1":
                return timingControl(model, sty);
            case "2":
                return triggerControl(model, sty);
            case "3":
                return timeSharingControl(model, sty);
            default : return 0;
        }
    }
}
