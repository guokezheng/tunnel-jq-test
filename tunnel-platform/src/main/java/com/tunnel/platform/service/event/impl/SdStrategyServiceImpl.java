package com.tunnel.platform.service.event.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.quartz.service.impl.SysJobServiceImpl;
import com.ruoyi.quartz.util.CronUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.util.CronUtil;
import com.tunnel.business.domain.dataInfo.*;
import com.tunnel.business.domain.event.*;
import com.tunnel.business.domain.informationBoard.IotBoardTemplateContent;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.instruction.EquipmentControlInstruction;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.dataInfo.SdEquipmentStateMapper;
import com.tunnel.business.mapper.dataInfo.SdEquipmentTypeMapper;
import com.tunnel.business.mapper.event.*;
import com.tunnel.business.mapper.informationBoard.IotBoardTemplateContentMapper;
import com.tunnel.business.mapper.informationBoard.IotBoardTemplateMapper;
import com.tunnel.business.mapper.logRecord.SdOperationLogMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceCmdService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.event.ISdEventFlowService;
import com.tunnel.deal.guidancelamp.protocol.StringUtil;
import com.tunnel.platform.service.SdDeviceControlService;
import com.tunnel.platform.service.deviceControl.PhoneSpkService;
import com.tunnel.platform.service.event.ISdStrategyService;
import com.zc.common.core.redis.pubsub.RedisPubSub;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
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
    private SysJobServiceImpl sysJobService;

    @Autowired
    private SdReserveProcessMapper sdReserveProcessMapper;

    @Autowired
    private SdDeviceControlService sdDeviceControlService;

    @Autowired
    private ISdEventFlowService sdEventFlowService;

    @Autowired
    private SdEventHandleMapper sdEventHandleMapper;

    @Autowired
    private SdEventMapper sdEventMapper;

    @Autowired
    private SdJoinReserveHandleMapper joinMapper;

    /**
     * 查询控制策略
     *
     * @param id 控制策略ID
     * @return 控制策略
     */
    @Override
    public SdStrategy selectSdStrategyById(Long id) {

        SdStrategy sdStrategy = sdStrategyMapper.selectSdStrategyById(id);
        if(StringUtils.isNotBlank(sdStrategy.getSchedulerTime())){
            sdStrategy.setExecDate(CronUtil.CronConvertDate(sdStrategy.getSchedulerTime()));
        }
        if(StringUtils.isNotBlank(sdStrategy.getSchedulerTime())) {
            sdStrategy.setExecTime(CronUtil.CronConvertDateTime(sdStrategy.getSchedulerTime()));
        }

        return sdStrategy;
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
        String emg = "数据处理异常";
        String[] timeParam = controlTime.split("-");
        SdStrategy strategy = sdStrategyMapper.selectSdStrategyById(strategyId);
        strategy.setTimerOpen(timeParam[0]);
        strategy.setTimerClose(timeParam[1]);
        String[] relationId = strategy.getJobRelationId().split(",");
        List<SdStrategyRl> rlList = sdStrategyRlMapper.selectSdStrategyRlByStrategyId(strategyId);
        if(rlList.size() < 1){
            throw new RuntimeException("未找到策略关联的设备信息");
        }
        //排序保证修改顺序
        //rlList = rlList.stream().sorted((Comparator.comparing(SdStrategyRl::getId))).collect(Collectors.toList());
        List<String> openRl = new ArrayList<>();
        List<String> closeRl = new ArrayList<>();
        try {
            for(SdStrategyRl rl:rlList){
                if(StrUtil.isNotBlank(rl.getEndState())){
                    openRl.add(rl.getId().toString());
                    rl.setControlTime(timeParam[0]);
                }else{
                    closeRl.add(rl.getId().toString());
                    rl.setControlTime(timeParam[1]);
                }
                sdStrategyRlMapper.updateSdStrategyRl(rl);
            }
            for (int i=0;i<relationId.length;i++) {
                Long jobId = Long.valueOf(relationId[i]);
                SysJob job = sysJobService.selectJobById(jobId);
                if(com.ruoyi.common.utils.StringUtils.isNull(job)){
                    emg = "未找到相应的定时任务";
                    throw new RuntimeException("未找到相应的定时任务");
                }
                //  0 偶数开启  奇数关闭
                if(i%2 == 0){
            /*    if(openRl.contains(job.getInvokeTarget().split("'")[1])){*/
                    job.setCronExpression(CronUtil.DateConvertCron(strategy.getTimerOpen()));
                }else{
                    job.setCronExpression(CronUtil.DateConvertCron(strategy.getTimerClose()));
                }
                sysJobService.updateJob(job);
            }
            sdStrategyMapper.updateSdStrategyById(strategy);
        } catch (Exception e) {
            throw new RuntimeException(emg);
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
        if (!"0".equals(sdStrategy.getStrategyType())) {
            String relationId = sdStrategy.getJobRelationId();
            if(StrUtil.isBlank(relationId)) {
                return 1;
            }
            try {
                String[] jobIds =  relationId.split(",");
                SysJob job = new SysJob();
                for (int i = 0; i < jobIds.length; i++) {
                    job = sysJobService.selectJobById(Long.valueOf(jobIds[i]));
                    job.setStatus(change);
                    sysJobService.updateJob(job);
                }
            } catch (Exception e) {
                throw new RuntimeException("开关控制失败！");
            }
        }
        return 1;
    }

    @Override
    public List<Map> workTriggerInfo(String tunnelId) {
        List<Map> result = sdStrategyMapper.workTriggerInfo(tunnelId);
        for(Map map : result){
            String strategyId = map.get("id").toString();
            SdStrategyRl rl = new SdStrategyRl();
            rl.setStrategyId(Long.valueOf(strategyId));
            List<SdStrategyRl> rlList = sdStrategyRlMapper.selectSdStrategyRlList(rl);
            //策略关联表信息
            List<String> sList = new ArrayList<>();
            for (int j = 0; j < rlList.size(); j++) {
                String eqTypeId = rlList.get(j).getEqTypeId();
                SdEquipmentType typeObject = sdEquipmentTypeMapper.selectSdEquipmentTypeById(Long.parseLong(rlList.get(j).getEqTypeId()));
                String typeName = typeObject.getTypeName();//设备类型名称
                //SdEquipmentState stateObject = sdEquipmentStateMapper.selectSdEquipmentStateById(Long.parseLong(rlList.get(j).getState()));
                SdEquipmentState state = new SdEquipmentState();
                state.setStateTypeId(Long.parseLong(eqTypeId));
                state.setDeviceState(rlList.get(j).getState());
                state.setIsControl(1);
                if(eqTypeId.equals(DevicesTypeEnum.VMS.getCode().toString()) || eqTypeId.equals(DevicesTypeEnum.MEN_JIA_VMS.getCode().toString())){
                    String templateId = rlList.get(j).getState();
                    IotBoardTemplateContent content = new IotBoardTemplateContent();
                    content.setTemplateId(templateId);
                    List<IotBoardTemplateContent> contentList = SpringUtils.getBean(IotBoardTemplateContentMapper.class).selectSdVmsTemplateContentList(content);
                    sList.add(typeName + "发布信息：" + contentList.get(0).getContent() + "；");
                    continue;
                }
                // SdEquipmentState stateObject = sdEquipmentStateMapper.selectSdEquipmentStateById(Long.parseLong(rlList.get(j).getState()));
                List<SdEquipmentState> stateObject = sdEquipmentStateMapper.selectDropSdEquipmentStateList(state);
                if(stateObject.size()<1){
                    continue;
                }
                String stateName = stateObject.get(0).getStateName();//设备状态名称
                sList.add(typeName + "控制执行：" + stateName + "；");
            }
            map.put("plan",sList);
        }
        return result;
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
        String deptId = SecurityUtils.getDeptId();
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
            //分时控制策略信息
            String fsControlData = "";
            //策略关联表信息
            for (int j = 0; j < rlList.size(); j++) {
                String eqTypeId = rlList.get(j).getEqTypeId();
                SdEquipmentType typeObject = sdEquipmentTypeMapper.selectSdEquipmentTypeById(Long.parseLong(rlList.get(j).getEqTypeId()));
                String typeName = typeObject.getTypeName();//设备类型名称
                //SdEquipmentState stateObject = sdEquipmentStateMapper.selectSdEquipmentStateById(Long.parseLong(rlList.get(j).getState()));
                SdEquipmentState state = new SdEquipmentState();
                state.setStateTypeId(Long.parseLong(eqTypeId));
                state.setDeviceState(rlList.get(j).getState());
                state.setIsControl(1);
                if(eqTypeId.equals(DevicesTypeEnum.VMS.getCode().toString()) || eqTypeId.equals(DevicesTypeEnum.MEN_JIA_VMS.getCode().toString())){
                    String templateId = rlList.get(j).getState();
                    IotBoardTemplateContent content = new IotBoardTemplateContent();
                    content.setTemplateId(templateId);
                    List<IotBoardTemplateContent> contentList = SpringUtils.getBean(IotBoardTemplateContentMapper.class).selectSdVmsTemplateContentList(content);
                    sList.add(typeName + "发布信息：" + contentList.get(0).getContent() + "；");
                    continue;
                }

                // SdEquipmentState stateObject = sdEquipmentStateMapper.selectSdEquipmentStateById(Long.parseLong(rlList.get(j).getState()));
                List<SdEquipmentState> stateObject = sdEquipmentStateMapper.selectDropSdEquipmentStateList(state);
                if(stateObject.size()<1){
                    continue;
                }
                //设备状态名称
                String stateName = stateObject.get(0).getStateName();
                //查询分是控制关闭指令
                List<SdEquipmentState> endObject = new ArrayList<>();
                if(rlList.get(j).getEndState() != null && !"".equals(rlList.get(j).getEndState())){
                    state.setDeviceState(rlList.get(j).getEndState());
                    endObject = sdEquipmentStateMapper.selectDropSdEquipmentStateList(state);
                    fsControlData = typeName + "控制执行：" + "开启指令：" + stateName + "；" + "关闭指令：" + endObject.get(0).getStateName() + ";";
                    sList.add(fsControlData);
                }else{

                    sList.add(typeName + "控制执行：" + stateName + "；");
                }
            /*    //1：日常策略  3：分时控制
                if("1".equals(list.get(i).getStrategyGroup()) && "3".equals(list.get(i).getStrategyType()) && list.get(i).getId() == rlList.get(j).getStrategyId()){
                    sList.add(fsControlData);
                }else {
                    sList.add(typeName + "控制执行：" + stateName + "；");
                }*/
            }
            list.get(i).setStrategyInfo(StringUtils.join(sList,""));
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
    @Transactional(propagation = Propagation.SUPPORTS)
    public int deleteSdStrategyById(Long id) {
        // 删除策略之前要先判断策略是否已经在预案管理中被引用
        SdStrategy sdStrategy = sdStrategyMapper.selectSdStrategyById(id);
        if (!ObjectUtils.isEmpty(sdStrategy) && "0".equals(sdStrategy.getStrategyType()) && "2".equals(sdStrategy.getStrategyGroup())) {
            int count = sdStrategyMapper.checkStrategyIfExist(id);
            if (count > 0) {
                throw new RuntimeException("控制策略已经在安全预警中引用，不可删除!");
            }
        }
        //删除定时任务
        if(!"0".equals(sdStrategy.getStrategyType())){
            deleteRefJob(sdStrategy.getJobRelationId());
        }
        int result = sdStrategyMapper.deleteSdStrategyById(id);
        if (result >0) {
            sdStrategyRlMapper.deleteSdStrategyRlByStrategyId(id);
            if ("2".equals(sdStrategy.getStrategyType())){
                SdTrigger sdTrigger = sdTriggerMapper.selectSdTriggerByRelateId(id);
                sdTriggerMapper.deleteSdTriggerById(sdTrigger.getId());
                result = sdTriggerDeviceMapper.deleteSdTriggerDeviceByTriggerId(sdTrigger.getId());
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
            if (!ObjectUtils.isEmpty(sdStrategy) && "0".equals(sdStrategy.getStrategyType()) && "2".equals(sdStrategy.getStrategyGroup())) {
                int count = sdStrategyMapper.checkStrategyIfExist(id);
                if (count > 0) {
                    throw new RuntimeException("控制策略已经在安全预警中引用，不可删除!");
                }
            }
        }
        int result = sdStrategyMapper.deleteSdStrategyByIds(ids);
        if (result >0) {
            //查询策略下的所有定时任务ID
            try {
                Long[] jobIds = sdStrategyMapper.getStrategyRefJobIds(ids);
                sysJobService.deleteJobByIds(jobIds);
            } catch (Exception e) {
                e.printStackTrace();
                //throw new RuntimeException("定时任务处理异常");
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
        SdStrategy sty = conditionalJudgement(model);
        // 判断是否符合规范并返回策略
        //参数1：预警策略  参数2：事件触发  参数3：自动执行
        if("2".equals(model.getStrategyGroup()) && "0".equals(model.getStrategyType()) && "1".equals(model.getIsAutomatic())){
            int checkCount = checkAuto(sty);
            if(checkCount > 0){
                throw new RuntimeException("已存在策略信息，请勿重复添加！");
            }
        }
        //校验名称是否存在
        int strNameCount = sdStrategyMapper.checkStrName(sty);
        if(strNameCount > 0){
            throw new RuntimeException("策略名称已存在，请重新输入后保存");
        }

        // 定时策略 格式处理
        // 根据 客户端 填写的 日期 和时间  转换为 cron表达式
        if(model.getStrategyType().equals("1")){

            String schedulerTime = CronUtil.DateConvertCron(model.getExecTime());
            if(null != model.getExecDate() && "" != model.getExecDate()){
                schedulerTime = CronUtil.DateTimeConvertCron(model.getExecDate(),model.getExecTime());
            }
            sty.setSchedulerTime(schedulerTime);
        }

        int insetStrResult = sdStrategyMapper.insertSdStrategy(sty);
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
            sysJobService.deleteJobByIds(param);
        } catch (Exception e) {
            //throw new RuntimeException("定时任务处理异常");
            e.printStackTrace();
        }
    }
    /**
     * 修改控制策略信息
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public int updateSdStrategyInfo(SdStrategyModel model) {
        //判断输入的值是否符合规范并返回策略信息
        SdStrategy sty = conditionalJudgement(model);
        //参数1：预警策略  参数2：事件触发  参数3：自动执行
        if("2".equals(model.getStrategyGroup()) && "0".equals(model.getStrategyType()) && "1".equals(model.getIsAutomatic())){
            int checkCount = checkAuto(sty);
            if(checkCount > 0){
                throw new RuntimeException("已存在策略信息，请勿重复添加！");
            }
        }
        //校验名称是否存在
        int strNameCount = sdStrategyMapper.checkStrName(sty);
        if(strNameCount > 0){
            throw new RuntimeException("策略名称已存在，请重新输入后保存");
        }
        String strategyType = model.getStrategyType();
        //更新策略 主表
        int updatePrimary = sdStrategyMapper.updateSdStrategyById(sty);
        //删除关联子表相关信息
        int upStrRl = sdStrategyRlMapper.deleteSdStrategyRlByStrategyId(model.getId());
        //删除相关联的定时任务
        if (!"0".equals(model.getStrategyType())) {
            deleteRefJob(sty.getJobRelationId());
        }
        //触发策略
        if ("2".equals(strategyType)) {
            //删除触发器
            sdTriggerMapper.deleteSdTriggerByRelateId(model.getId());
            //删除触发器关联设备
            sdTriggerDeviceMapper.deleteSdTriggerDeviceByTriggerId(model.getTriggers().getId());
        }

        // 定时策略 格式处理
        // 根据 客户端 填写的 日期 和时间  转换为 cron表达式
        if(model.getStrategyType().equals("1")){

            String schedulerTime = CronUtil.DateConvertCron(model.getExecTime());
            if(null != model.getExecDate()){
                schedulerTime = CronUtil.DateTimeConvertCron(model.getExecDate(),model.getExecTime());
            }
            sty.setSchedulerTime(schedulerTime);

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
    public void handleStrategy(Long id) throws UnknownHostException {
        ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");
        //ScheduledThreadPoolExecutor poolTaskExecutor = new ScheduledThreadPoolExecutor(8);
        SdStrategy strategy = sdStrategyMapper.selectSdStrategyById(id);
        List<SdStrategyRl> rlList = sdStrategyRlMapper.selectSdStrategyRlByStrategyId(id);//   selectSdStrategyRlList(rl);
        for(SdStrategyRl sdStrategyRl:rlList){
            String[] split = sdStrategyRl.getEquipments().split(",");
            for (String devId : split){
                Map<String,Object> issuedParam = new HashMap<>();
                SdDeviceData searchObj = new SdDeviceData();
                searchObj.setDeviceId(devId);
                SdDeviceData realData = SpringUtils.getBean(SdDeviceDataMapper.class).selectLastRecord(searchObj);
                SdStrategy sdStrategy = SpringUtils.getBean(SdStrategyMapper.class).selectSdStrategyById(sdStrategyRl.getStrategyId());
                issuedParam.put("devId",devId);
                issuedParam.put("state",sdStrategyRl.getState());
                issuedParam.put("controlType",sdStrategy.getStrategyType());
                issuedParam.put("operIp", InetAddress.getLocalHost().getHostAddress());
                SpringUtils.getBean(SdDeviceControlService.class).controlDevices(issuedParam);
                if(StrUtil.isNotBlank(sdStrategyRl.getEffectiveTime())){
                    int duration = Integer.valueOf(sdStrategyRl.getEffectiveTime());
                    String beforeState = realData.getData();
                    executor.schedule(new Runnable() {
                        @Override
                        public void run() {
                            issuedParam.put("state",beforeState);
                            SpringUtils.getBean(SdDeviceControlService.class).controlDevices(issuedParam);
                        }
                    }, duration, TimeUnit.MINUTES);
                }
            }
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
            long num = manualControl.stream().filter(s -> StrUtil.isBlank((String) s.get("state"))).count();
            if (num > 0)
                throw new RuntimeException("请填写完整手动控制！");
        }
//        } else if(!"2".equals(model.getStrategyType())) {
//            List<Map> timeSharingControl = model.getAutoControl();
//            long num = timeSharingControl.stream().filter(s -> StrUtil.isBlank((String)s.get("state"))).count();
//            if(num > 0)
//                throw new RuntimeException("请填写完整策略信息！");
//        }
        SdStrategy sty = new SdStrategy();
        //策略类型
        sty.setId(model.getId());
        sty.setStrategyType(model.getStrategyType());
      //  sty.setStrategyState("1");
        sty.setStrategyState(model.getStrategyState());
        sty.setStrategyName(model.getStrategyName());
        sty.setTunnelId(model.getTunnelId());
        sty.setWarningId(model.getWarningId());
        sty.setIsAutomatic(model.getIsAutomatic());
        if(model.getEventType()!=null){
            sty.setEventType(model.getEventType());
        }
        sty.setStrategyGroup(model.getStrategyGroup());
        sty.setJobRelationId(model.getJobRelationId());
        if("1".equals(model.getStrategyType()) || "2".equals(model.getStrategyType())) {
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
            String equipmentTypeId = map.get("equipmentTypeId") + "";
            String state = (String) map.get("state");
            SdStrategyRl sdStrategyRl = new SdStrategyRl();
            sdStrategyRl.setEquipments(equipments);
            sdStrategyRl.setState(state);
            if(map.get("effectiveTime")!=null){
                sdStrategyRl.setEffectiveTime(map.get("effectiveTime").toString());
            }
            if(map.get("disposalName")!=null){
                sdStrategyRl.setDisposalName(map.get("disposalName").toString());
            }
            sdStrategyRl.setEqTypeId(equipmentTypeId);
            sdStrategyRl.setStrategyId(sty.getId());
            addRows += sdStrategyRlMapper.insertSdStrategyRl(sdStrategyRl);
        }
        if(addRows < 1){
            throw new RuntimeException("数据保存失败！");
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
            List<String> value = (List<String>) map.get("equipments");
            String equipments = StringUtils.join(value,",");
            String equipmentTypeId = map.get("equipmentTypeId") + "";
            if(map.get("state") == null || map.get("state").equals("")){
                throw new RuntimeException("请填写完整策略信息！");
            }
            String eqState = (String) map.get("state");
            SdStrategyRl rl = new SdStrategyRl();
            rl.setEqTypeId(equipmentTypeId);
            // 设备未设置执行时间，则取策略执行时间
            if(null == rl.getControlTime() || "" == rl.getControlTime()){
                rl.setControlTime(model.getExecTime());
            }
            rl.setEquipments(equipments);
            rl.setState(eqState);
            rl.setStrategyId(sty.getId());
            // corn表达式 校验是否合规
            if(!CronUtils.isValid(sty.getSchedulerTime())){
                throw new RuntimeException("当前日期表达式选择有误，请重新选择！");
            }
//            try {
//                CronExpression cronExpression = new CronExpression(sty.getSchedulerTime());
//                Date nextTime = cronExpression.getNextValidTimeAfter(new Date());
//                rl.setControlTime(DateFormat.getTimeInstance().format(nextTime));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            sdStrategyRlMapper.insertSdStrategyRl(rl);
            try{
                Long refId = rl.getId();
                //新增定时任务
                SysJob job = new SysJob();
                // 定时任务名称
                job.setJobName(model.getStrategyName() + "-" + refId);
                // 调用目标字符串
                job.setInvokeTarget("strategyTask.strategyParams('" + refId + "')");
                job.setCronExpression(sty.getSchedulerTime());
                // 计划执行错误策略（1立即执行 2执行一次 3放弃执行）
                job.setMisfirePolicy("1");
                // 是否并发执行（0允许 1禁止）
                job.setConcurrent("0");
                if(null != model.getStrategyState() && model.getStrategyState().equals("0")){
                    job.setStatus("0");
                }else{
                    // 状态（0正常 1暂停）
                    job.setStatus("1");
                }
                sysJobService.insertJob(job);
                System.out.println(job.getJobId());
                jobIdList.add(job.getJobId().toString());
            }catch (Exception ex){
                throw new RuntimeException("新增数据失败！");
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
        String startTime = model.getStartTime();
        String endTime = model.getEndTime();
        Long refId = 0L;
        for (Map<String,Object> map : timeSharingControl) {
            List<String> value = (List<String>) map.get("equipments");
            String equipments = StringUtils.join(value,",");
            String equipmentTypeId = map.get("equipmentTypeId") + "";

            String state = null;
            String openState = null;
            String closeState = null;

            if(equipmentTypeId.equals("16") || equipmentTypeId.equals("36")){
                if (null != map.get("state") && StringUtils.isNotBlank(map.get("state").toString())) {
                    state = map.get("state").toString();
                }else {
                    throw new RuntimeException("请填写完整策略信息！");
                }
            }else{

                if ((null != map.get("openState") &&  StringUtils.isNotBlank(map.get("openState").toString()) ) &&
                        (null != map.get("closeState") &&  StringUtils.isNotBlank(map.get("closeState").toString()))) {
                    openState = map.get("openState").toString();
                    closeState = map.get("closeState").toString();
                }else{
                    throw new RuntimeException("请填写完整策略信息！");
                }
            }

            try{
                SdStrategyRl openRlData = new SdStrategyRl();
                openRlData.setEqTypeId(equipmentTypeId);
                openRlData.setEquipments(equipments);
                if(equipmentTypeId.equals("16") || equipmentTypeId.equals("36")){
                    openRlData.setState(state);
                }else{
                    openRlData.setState(openState);
                    openRlData.setEndState(closeState);
                }
                openRlData.setStrategyId(sty.getId());
                openRlData.setControlTime(startTime);

                sdStrategyRlMapper.insertSdStrategyRl(openRlData);
                refId = openRlData.getId();
                //新增定时任务
                try {
                    SysJob job = new SysJob();
                    // 定时任务名称
                    job.setJobName(model.getStrategyName()+ "-" + refId + "-执行");
                    // 调用目标字符串
                    job.setInvokeTarget("strategyTask.strategyParamsPlus('" + refId + "','1')");
                    // corn表达式
                    String cronDate = CronUtil.DateConvertCron(startTime);
                    job.setCronExpression(cronDate);
                    // 计划执行错误策略（1立即执行 2执行一次 3放弃执行）
                    job.setMisfirePolicy("1");
                    // 是否并发执行（0允许 1禁止）
                    job.setConcurrent("0");
                    if(null != model.getStrategyState() && model.getStrategyState().equals("0")){
                        job.setStatus("0");
                    }else{
                        // 状态（0正常 1暂停）
                        job.setStatus("1");
                    }
                    sysJobService.insertJob(job);
                    jobIdList.add(job.getJobId().toString());
                } catch (Exception e) {
                    throw new RuntimeException("新增数据失败！");
                }

                //新增定时任务
                try {
                    SysJob job = new SysJob();
                    // 定时任务名称
                    job.setJobName(model.getStrategyName()+ "-" + refId + "-恢复");
                    // 调用目标字符串
                    job.setInvokeTarget("strategyTask.strategyParamsPlus('" + refId + "','2')");
                    // corn表达式
                    String cronDate = CronUtil.DateConvertCron(endTime);
                    job.setCronExpression(cronDate);
                    // 计划执行错误策略（1立即执行 2执行一次 3放弃执行）
                    job.setMisfirePolicy("1");
                    // 是否并发执行（0允许 1禁止）
                    job.setConcurrent("0");
                    if(null != model.getStrategyState() && model.getStrategyState().equals("0")){
                        job.setStatus("0");
                    }else{
                        // 状态（0正常 1暂停）
                        job.setStatus("1");
                    }
                    sysJobService.insertJob(job);
                    jobIdList.add(job.getJobId().toString());
                } catch (Exception e) {
                    throw new RuntimeException("新增数据失败！");
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        String jobIdStr = jobIdList.stream().collect(Collectors.joining(","));
        sty.setJobRelationId(jobIdStr);
        sty.setTimerOpen(startTime);
        sty.setTimerClose(endTime);
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
        //预警联动
        if(model.getTriggers().getWarningType().equals("1")){
            if(autoControl.isEmpty()){
                throw new RuntimeException("请填写完整策略信息！");
            }
            for (Map<String,Object> map : autoControl) {
                if(StrUtil.isEmpty(map.get("state").toString())){
                    throw new RuntimeException("请填写完整策略信息！");
                }
                List<String> value = (List<String>) map.get("equipments");
                String equipments = StringUtils.join(value,",");
                String equipmentTypeId = map.get("equipmentTypeId") + "";
                String eqState = (String) map.get("state");
                SdStrategyRl rl = new SdStrategyRl();
                if(map.get("disposalName")!=null){
                    rl.setDisposalName(map.get("disposalName").toString());
                }
                rl.setEqTypeId(equipmentTypeId);
                rl.setEquipments(equipments);
                rl.setState(eqState);
                rl.setStrategyId(sty.getId());
                sdStrategyRlMapper.insertSdStrategyRl(rl);
            }
        }
        // 保存触发器
        SdTrigger sdTrigger = model.getTriggers();
        sdTrigger.setRelateId(sty.getId());
        int insertSdTrigger = sdTriggerMapper.insertSdTrigger(sdTrigger);
        //新增定时任务 corn表达式 校验是否合规
        if(!CronUtils.isValid(sty.getSchedulerTime())){
            throw new RuntimeException("当前日期表达式选择有误，请重新选择！");
        }
        Long refId = sdTrigger.getId();
        // 新增定时任务
        SysJob job = new SysJob();
        // 定时任务名称
        job.setJobName(model.getStrategyName());
        // 调用目标字符串
        job.setInvokeTarget("strategyTask.triggerJob('" + refId + "')");
        job.setCronExpression(sty.getSchedulerTime());
        // 计划执行错误策略（1立即执行 2执行一次 3放弃执行）
        job.setMisfirePolicy("1");
        // 是否并发执行（0允许 1禁止）
        job.setConcurrent("0");
        if(null != model.getStrategyState() && model.getStrategyState().equals("0")){
            job.setStatus("0");
        }else{
            // 状态（0正常 1暂停）
            job.setStatus("1");
        }
        try{
            sysJobService.insertJob(job);
        }catch (Exception ex){
            throw new RuntimeException("新增数据失败！");
        }
        sty.setJobRelationId(job.getJobId().toString());
        int updateRows = sdStrategyMapper.updateSdStrategyById(sty);
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
        return updateRows;
    }
    /**
     * 策略维护关系表
     * @param model
     * @return
     */
    private int saveRelation(SdStrategyModel model,SdStrategy sty) {
        String strategyType = model.getStrategyType();
        //COMMENT '策略类型 0：手动执行 1：定时控制 2：智能控制（自动触发）3: 分时控制',
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

    /**
     * 设置过有效时间的策略 需要执行恢复操作
     * @param issuedParam
     * @param effectiveTime
     */
    public void scheduledRecoveryState(Map issuedParam,String effectiveTime) {
        ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");
        SdOperationLog log = SpringUtils.getBean(SdOperationLogMapper.class).getLatestRecord(issuedParam.get("devId").toString());
        if(StrUtil.isBlank(log.getBeforeState())){
            return;
        }
        String eqTypeId = issuedParam.get("eqTypeId").toString();
        issuedParam.put("state",log.getBeforeState());
        //疏散标志
        if(eqTypeId.equals(DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode().toString())) {
            issuedParam.put("brightness","50");
            issuedParam.put("frequency","60");
            issuedParam.put("fireMark","255");
            issuedParam.put("state","2");
        }
        //诱导灯
        if(eqTypeId.equals(DevicesTypeEnum.YOU_DAO_DENG.getCode().toString())){
            issuedParam.put("brightness","50");
            issuedParam.put("frequency","60");
            if(log.getBeforeState().equals("2")){
                issuedParam.put("fireMark","255");
            }
        }
        //情报板
        if(eqTypeId.equals(DevicesTypeEnum.MEN_JIA_VMS.getCode().toString()) || eqTypeId.equals(DevicesTypeEnum.VMS.getCode().toString())){
            return;
            //issuedParam.put("templateId",log.getBeforeState());
        }
        //issuedParam.put("controlType","0");
        int duration = Integer.valueOf(effectiveTime);
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                SpringUtils.getBean(SdDeviceControlService.class).controlDevices(issuedParam);
            }
        }, duration, TimeUnit.MINUTES);
    }
    /**
     * 下发设备
     * @param rl
     * @param eventId
     * @return
     */
    public int issuedDevice(SdStrategyRl rl,Long eventId,String controlType){
        String eqTypeId = rl.getEqTypeId();
        String controlStatus = rl.getState();
        SdEvent event = SpringUtils.getBean(SdEventMapper.class).selectSdEventById(eventId);
        Map issuedParam = new HashMap();
        int issueResult = 0;
        //疏散标志控制逻辑
        if(eqTypeId.equals(DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode().toString())){
            SdDevices searchObject = new SdDevices();
            searchObject.setEqTunnelId(event.getTunnelId());
            searchObject.setEqType(DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode());
            searchObject.setEqDirection(event.getDirection());
            //事故点整形桩号
            int compareValue = Integer.valueOf(event.getStakeNum().replace("K","").replace("+","").replace(" ",""));
            List<SdDevices> list = SpringUtils.getBean(SdDevicesMapper.class).selectSdDevicesList(searchObject);
            //同一方向上的疏散标志整形桩号去重
            int[] allNum = list.stream().filter(s-> StringUtils.isNotBlank(s.getFEqId()))
                    .mapToInt(s->s.getPileNum().intValue()).distinct().toArray();
            //查找事故点最近的疏散标志
            int index = Math.abs(compareValue-allNum[0]);
            int closest = allNum[0];
            for (int i=0;i<allNum.length;i++) {
                int abs = Math.abs(compareValue-allNum[i]);
                if(abs <= index){
                    index = abs;
                    closest = allNum[i];
                }
            }
            Long pile = new Long((long)closest);
            list = list.stream().filter(devices->devices.getPileNum().equals(pile)).collect(Collectors.toList());
            if(list.size()<1){
                return 0;
            }
            //报警点位设备ID
            String alarmPointEqId = list.get(0).getEqId();
            //fireMark标号位置信息
            String fireMark = "0";
            //1关闭 2常亮 5报警
            if(controlStatus.equals("5")){
                fireMark = list.get(0).getQueryPointAddress();
            }else if(controlStatus.equals("2")){
                fireMark = "255";
            }
            //下发设备
            issuedParam.put("brightness","50");
            issuedParam.put("frequency","60");
            issuedParam.put("fireMark",fireMark);
            issuedParam.put("state",controlStatus);
            issuedParam.put("devId",alarmPointEqId);
            issuedParam.put("eventId",eventId);
            issuedParam.put("controlType",controlType);
            issuedParam.put("operIp", IpUtils.getIpAddr(ServletUtils.getRequest()));
            issueResult = sdDeviceControlService.controlDevices(issuedParam);
            if(StrUtil.isNotBlank(rl.getEffectiveTime()) && issueResult > 0) {
                issuedParam.put("eqTypeId",eqTypeId);
                scheduledRecoveryState(issuedParam,rl.getEffectiveTime());
            }
        }else if(eqTypeId.equals(DevicesTypeEnum.LS.getCode().toString())){
            JSONObject object = new JSONObject();
            object.put("loop",false);
            object.put("loopCount",2);
            object.put("tunnelId",event.getTunnelId());
            object.put("volume",80);
            object.put("fileNames",new ArrayList<String>(){{
                add(rl.getState());
            }});
            object.put("lib","YeastarHost");
            object.put("controlType",controlType);
            object.put("spkDeviceIds", new ArrayList<String>(){{
                addAll(Arrays.asList(rl.getEquipments().split(",")));
            }});
            AjaxResult result = SpringUtils.getBean(PhoneSpkService.class).playVoice(object);
            return Integer.valueOf(result.get("data").toString());
        } else{
            String[] split = rl.getEquipments().split(",");
            for (String devId : split){
                issuedParam.put("devId",devId);
                issuedParam.put("state",controlStatus);
                issuedParam.put("eventId",eventId);
                issuedParam.put("controlType",controlType);
                //诱导灯
                if(eqTypeId.equals(DevicesTypeEnum.YOU_DAO_DENG.getCode().toString())){
                    issuedParam.put("brightness","50");
                    issuedParam.put("frequency","60");
                    if(controlStatus.equals("2")){
                        issuedParam.put("fireMark","255");
                    }
                }
                //情报板
                if(eqTypeId.equals(DevicesTypeEnum.MEN_JIA_VMS.getCode().toString()) || eqTypeId.equals(DevicesTypeEnum.VMS.getCode().toString())){
                    issuedParam.put("templateId",controlStatus);
                }
                issuedParam.put("operIp", IpUtils.getIpAddr(ServletUtils.getRequest()));
                issueResult = sdDeviceControlService.controlDevices(issuedParam);
                if(StrUtil.isNotBlank(rl.getEffectiveTime()) && issueResult > 0) {
                    issuedParam.put("eqTypeId",eqTypeId);
                    scheduledRecoveryState(issuedParam,rl.getEffectiveTime());
                }
                //issuedParam.clear();
            }
        }
        return issueResult;
    }

    @Override
    public AjaxResult implementPlan(String planId,Long eventId){
        //储存失败的数据
        List<String> errorList = new ArrayList();
        //将id拆分
        List<String> list = Arrays.asList(planId.split(","));
        int issueResult = 1;
        for(String processId : list){
            //排除已经执行过的流程
            SdEventHandle sdEventHandle = new SdEventHandle();
            sdEventHandle.setEventId(eventId);
            sdEventHandle.setProcessId(Long.valueOf(processId));
            //0：未完成 1：已完成
            sdEventHandle.setEventState("1");
            if(sdEventHandleMapper.selectSdEventHandleList(sdEventHandle).size() > 0){
                continue;
            }
            int count = implementProcess(Long.valueOf(processId),eventId);
            if(count == 0){
                //查询失败的流程名称
                SdJoinReserveHandle joinReserveHandle = joinMapper.selectSdJoinReserveHandleById(Long.valueOf(processId));
                errorList.add(joinReserveHandle.getProcessName());
            }
        }
        if(errorList.size() == 0){
            return AjaxResult.success("执行成功");
        }if(list.size() == errorList.size()){
            return AjaxResult.error("执行失败");
        }else {
            return AjaxResult.success("执行成功,部分流程执行失败[" + StringUtils.join(errorList,",") + "]");
        }
    }

    @Override
    public int implementProcess(Long processId,Long eventId) {
        //如果不是指定设备则查询相关设备
        SdJoinReserveHandle joinReserveHandle = joinMapper.selectSdJoinReserveHandleById(processId);
        SdStrategyRl rl = new SdStrategyRl();
        if(joinReserveHandle.getEquipments() == null || "".equals(joinReserveHandle.getEquipments())){
            //更新事件处置记录表状态
            return updateHandleState(processId,eventId);
        }
        rl.setEquipments(joinReserveHandle.getEquipments());
        rl.setEqTypeId(joinReserveHandle.getEqTypeId().toString());
        rl.setState(joinReserveHandle.getState());
        int issueResult = issuedDevice(rl,eventId,"4");
        String deviceExecutionState = getDeviceExecutionState(processId);
        if(issueResult>0){
            //保存处置记录
            setEventFlowData(eventId,joinReserveHandle.getProcessName() + "：" + deviceExecutionState + "【成功】");
            //更新事件处置记录表状态
            updateHandleState(processId,eventId);
            SdEvent sdEvent = new SdEvent();
            sdEvent.setUpdateTime(DateUtils.getNowDate());
            sdEvent.setId(eventId);
            sdEventMapper.updateSdEvent(sdEvent);
        }else {
            setEventFlowData(eventId,joinReserveHandle.getProcessName() + "：" + deviceExecutionState + "【失败】");
        }
        return issueResult;
    }

    /**
     * 获取状态、模板、文件名称
     * @param processId
     * @return
     */
    public String getDeviceExecutionState(Long processId){
        //状态、模板、文件名称
        String eqStateData = "";
        SdJoinReserveHandleMapper joinMapper = SpringUtils.getBean(SdJoinReserveHandleMapper.class);
        SdJoinReserveHandle joinReserveHandle = joinMapper.selectSdJoinReserveHandleById(processId);
        //设备类型
        Long eqType = joinReserveHandle.getEqTypeId();
        SdEventMapper sdEventMapper = SpringUtils.getBean(SdEventMapper.class);
        if(eqType == DevicesTypeEnum.VMS.getCode() || eqType == DevicesTypeEnum.MEN_JIA_VMS.getCode()){
            //查询情报板
            //String vmsData = sdEventMapper.getManagementVmsLs(sdReserveProcess);
            Map<String, Object> sdVmsContent = SpringUtils.getBean(IotBoardTemplateMapper.class).getSdVmsTemplateContent(Long.valueOf(joinReserveHandle.getState()));
            eqStateData = sdVmsContent.get("content").toString().concat("    ------");
        }else if(eqType == DevicesTypeEnum.LS.getCode()){
            //截取广播文件名称
            //String lsData = sdEventMapper.getManagementVmsLs(sdReserveProcess);
            List<String> list = Arrays.asList(joinReserveHandle.getState().split("\\\\"));
            eqStateData = list.get(list.size() - 1).concat("    ------");
        }else {
            //查询普通设备状态
            List<Map<String, Object>> maps = sdEventMapper.getManagementDeviceState(joinReserveHandle.getId());
            eqStateData = maps.get(0).get("stateName").toString().concat("    ------");
        }
        return eqStateData;
    }

    /**
     * 保存处置记录
     * @param eventId
     * @param content
     * @return
     */
    public void setEventFlowData(Long eventId, String content){
        Map flowParam = new HashMap();
        flowParam.put("eventId",eventId);
        flowParam.put("content",content);
        sdEventFlowService.savePlanProcessFlow(flowParam);
    }

    @Override
    public int implementDisposalStrategy(Long strategyId,Long eventId) {
        List<SdStrategyRl> rlList = sdStrategyRlMapper.selectSdStrategyRlByStrategyId(strategyId);
        int issueResult = 0;
        for(SdStrategyRl rl:rlList){
            issueResult = issuedDevice(rl,eventId,"0");
            //更新事件处置记录表状态
            updateHandleState(rl.getId(),eventId);
        }
        return issueResult;
    }

    @Override
    public int implementDisposalStrategyRl(Long rlId,Long eventId) {
        SdStrategyRl rl = sdStrategyRlMapper.selectSdStrategyRlById(rlId);
        int count = issuedDevice(rl, eventId, "0");
        //更新事件处置记录表状态
        updateHandleState(rl.getId(),eventId);
        return count;
    }

    @Override
    public AjaxResult getStrategyData(SdStrategy strategy) {
        List<SdStrategy> sdStrategies = sdStrategyMapper.selectSdStrategyList(strategy);
        return AjaxResult.success(sdStrategies);
    }

    /**
     * 更新事件处置记录表状态
     * @param processId
     * @param eventId
     */
    public int updateHandleState(Long processId,Long eventId){
        SdEventHandle sdEventHandle = new SdEventHandle();
        sdEventHandle.setEventId(eventId);
        sdEventHandle.setProcessId(processId);
        //0:未完成 1:已完成'
        sdEventHandle.setEventState("1");
        sdEventHandle.setUpdateTime(DateUtils.getNowDate());
        return sdEventHandleMapper.updateHandleState(sdEventHandle);
    }

    /**
     * 预警策略-事件触发-自动时校验
     * @param sty
     * @return
     */
    public int checkAuto(SdStrategy sty){
        SdStrategy strategy = new SdStrategy();
        if(sty.getId() != null){
            strategy.setId(sty.getId());
        }
        strategy.setTunnelId(sty.getTunnelId());
        strategy.setDirection(sty.getDirection());
        strategy.setStrategyType(sty.getStrategyType());
        strategy.setEventType(sty.getEventType());
        strategy.setIsAutomatic(sty.getIsAutomatic());
        return sdStrategyMapper.checkStrategy(strategy);
    }
}
