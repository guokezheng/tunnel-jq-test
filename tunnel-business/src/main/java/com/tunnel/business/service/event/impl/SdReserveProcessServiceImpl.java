package com.tunnel.business.service.event.impl;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdEquipmentState;
import com.tunnel.business.domain.dataInfo.SdEquipmentStateIconFile;
import com.tunnel.business.domain.event.*;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.dataInfo.SdEquipmentIconFileMapper;
import com.tunnel.business.mapper.dataInfo.SdEquipmentStateMapper;
import com.tunnel.business.mapper.event.SdReservePlanMapper;
import com.tunnel.business.mapper.event.SdReserveProcessMapper;
import com.tunnel.business.mapper.event.SdStrategyMapper;
import com.tunnel.business.mapper.event.SdStrategyRlMapper;
import com.tunnel.business.service.event.ISdEventFlowService;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.event.ISdReserveProcessService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 预案流程节点Service业务层处理
 *
 * @author ruoyi
 * @date 2022-09-02
 */
@Service
public class SdReserveProcessServiceImpl implements ISdReserveProcessService {
    @Autowired
    private SdReserveProcessMapper sdReserveProcessMapper;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Autowired
    private ISdEventFlowService sdEventFlowService;

    @Autowired
    private ISdEventService sdEventService;

    /**
     * 查询预案流程节点
     *
     * @param id 预案流程节点主键
     * @return 预案流程节点
     */
    @Override
    public SdReserveProcess selectSdReserveProcessById(Long id) {
        return sdReserveProcessMapper.selectSdReserveProcessById(id);
    }

    /**
     * 查询预案流程节点列表
     *
     * @param sdReserveProcess 预案流程节点
     * @return 预案流程节点
     */
    @Override
    public List<SdReserveProcess> selectSdReserveProcessList(SdReserveProcess sdReserveProcess) {
        return sdReserveProcessMapper.selectSdReserveProcessList(sdReserveProcess);
    }

    /**
     * 新增预案流程节点
     *
     * @param sdReserveProcess 预案流程节点
     * @return 结果
     */
    @Override
    public int insertSdReserveProcess(SdReserveProcess sdReserveProcess) {
        sdReserveProcessMapper.deleteSdReserveProcessByPlanId(sdReserveProcess.getReserveId());
        List<SdStrategyRl> rlList = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlByStrategyId(sdReserveProcess.getHandleStrategyList()[1]);
        SdReserveProcess reserveProcess = new SdReserveProcess();
        reserveProcess.setReserveId(sdReserveProcess.getReserveId());
        reserveProcess.setDeviceTypeId(Long.parseLong(rlList.get(0).getEqTypeId()));
        reserveProcess.setProcessName(sdReserveProcess.getProcessName());
        reserveProcess.setProcessSort(sdReserveProcess.getProcessSort());
        reserveProcess.setStrategyId(sdReserveProcess.getHandleStrategyList()[1]);
        reserveProcess.setCreateTime(DateUtils.getNowDate());
        reserveProcess.setCreateBy(SecurityUtils.getUsername());
        int result = -1;
        result = sdReserveProcessMapper.insertSdReserveProcess(reserveProcess);
        return result;
    }

    /**
     * 批量添加预案流程节点
     *
     * @param sdReserveProcesses
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchSdReserveProcessed(SdReserveProcessModel sdReserveProcesses) {
        List<SdReserveProcess> list = new ArrayList<>();
        //删除预案流程节点
        sdReserveProcessMapper.deleteSdReserveProcessByPlanId(sdReserveProcesses.getReserveId());
        for (SdReserveProcess process : sdReserveProcesses.getSdReserveProcesses()) {
            Long[] strategyIds = process.getHandleStrategyList();
            List<SdStrategyRl> rlList = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlByStrategyId(strategyIds[1]);
            if(rlList.isEmpty()){
                continue;
            }
            SdReserveProcess reserveProcess = new SdReserveProcess();
            reserveProcess.setReserveId(sdReserveProcesses.getReserveId());
            reserveProcess.setDeviceTypeId(Long.parseLong(rlList.get(0).getEqTypeId()));
            reserveProcess.setProcessName(process.getProcessName());
            reserveProcess.setProcessSort(process.getProcessSort());
            reserveProcess.setStrategyId(process.getHandleStrategyList()[1]);
            reserveProcess.setCreateTime(DateUtils.getNowDate());
            reserveProcess.setCreateBy(SecurityUtils.getUsername());
            list.add(reserveProcess);
        }
        int result = -1;
        if(list.isEmpty()){
            throw new RuntimeException("无效数据策略添加失败");
        }
        result = sdReserveProcessMapper.batchSdReserveProcess(list);
        return result;
    }

    /**
     * 修改预案流程节点
     *
     * @param sdReserveProcess 预案流程节点
     * @return 结果
     */
    @Override
    public int updateSdReserveProcess(SdReserveProcess sdReserveProcess) {
        sdReserveProcess.setUpdateTime(DateUtils.getNowDate());
        int result = -1;
        result = sdReserveProcessMapper.deleteSdReserveProcessByPlanId(sdReserveProcess.getReserveId());
        if (result > -1) {
            result = sdReserveProcessMapper.updateSdReserveProcess(sdReserveProcess);
        }
        return result;
    }

    /**
     * 批量删除预案流程节点
     *
     * @param ids 需要删除的预案流程节点主键
     * @return 结果
     */
    @Override
    public int deleteSdReserveProcessByIds(Long[] ids) {
        return sdReserveProcessMapper.deleteSdReserveProcessByIds(ids);
    }

    /**
     * 删除预案流程节点信息
     *
     * @param id 预案流程节点主键
     * @return 结果
     */
    @Override
    public int deleteSdReserveProcessById(Long id) {
        return sdReserveProcessMapper.deleteSdReserveProcessById(id);
    }

    /**
     * 根据预案id删除预案流程节点信息
     *
     * @param id
     * @return
     */
    @Override
    public int deleteSdReserveProcessByPlanId(Long id) {
        return sdReserveProcessMapper.deleteSdReserveProcessByPlanId(id);
    }

    /**
     * 根据预案id查找预案流程节点信息
     *
     * @param RId
     * @return
     */
    @Override
    public List<SdReserveProcess> selectSdReserveProcessByRId(Long RId) {
        List<SdReserveProcess> sdReserveProcesses = sdReserveProcessMapper.selectSdReserveProcessByRid(RId);
        for (SdReserveProcess process : sdReserveProcesses) {
            List<SdStrategyRl> rlList = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlByStrategyId(process.getStrategyId());
            if(rlList.isEmpty()){
                continue;
            }
            Long[] list = new Long[2];
            list[0] = Long.valueOf(rlList.get(0).getEqTypeId());
            list[1] = process.getStrategyId();
            process.setHandleStrategyList(list);
        }
        return sdReserveProcesses;
    }

    /**
     * 应急预案预览
     * @param reserveId
     * @return
     */
    @Override
    public List<Map> selectPreviewDisplay(Long reserveId,String eventId) {
        // 预案流程节点
        List<SdReserveProcess> list = sdReserveProcessMapper.selectSdReserveProcessByRid(reserveId);
        if(list.size() < 1){
            return null;
        }
        if(StrUtil.isNotEmpty(eventId)){
            //应急调度预览数据
            return dispatchPreview(list,eventId);
        }else{
            //预案列表预览数据
            return reservePreview(list);
        }
    }
    /**
     * 应急调度预案预览
     * @param list
     * @param eventId
     * @return
     */
    public List<Map> dispatchPreview(List<SdReserveProcess> list,String eventId){
        List<Map> previewData = new ArrayList<>();
        for (SdReserveProcess process : list) {
            HashMap<String, Object> map = new HashMap<>();
            // 预案Id
            map.put("reserveId", process.getReserveId());
            // 策略Id
            map.put("strategyId", process.getStrategyId());
            // 策略名称
            SdStrategy sdStrategy = SpringUtils.getBean(SdStrategyMapper.class).selectSdStrategyById(process.getStrategyId());
            map.put("strategyName", sdStrategy.getStrategyName());
            map.put("strategy", sdStrategy);
            // 设备类型Id
            map.put("deviceTypeId", process.getDeviceTypeId());
            List<SdStrategyRl> rlList = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlByStrategyId(process.getStrategyId());
            if (rlList.size() < 1) {
                continue;
            }
            List<String> eqOperation = new ArrayList<>();
            List<List> iFileList = new ArrayList<>();
            List<Map> equipmentData = new ArrayList<>();
            for (SdStrategyRl rl : rlList) {
                String[] allEquipment = rl.getEquipments().split(",");
                //疏散标志处理
                if (rl.getEqTypeId().equals("30")) {
                    if(rl.getState().equals("1")){
                        eqOperation.add("智能疏散标志控制执行：关灯;");
                        map.put("policyInformation", eqOperation);
                        previewData.add(map);
                        continue;
                    }
                    if(rl.getState().equals("2")){
                        eqOperation.add("智能疏散标志控制执行：常亮;");
                        map.put("policyInformation", eqOperation);
                        previewData.add(map);
                        continue;
                    }
                    SdEvent event = sdEventService.selectSdEventById(Long.parseLong(eventId));
                    SdDevices searchObject = new SdDevices();
                    searchObject.setEqTunnelId(event.getTunnelId());
                    searchObject.setEqType(30L);
                    searchObject.setEqDirection(event.getDirection());
                    //事故点整形桩号
                    int compareValue = Integer.valueOf(event.getStakeNum().replace("K", "").replace("+", "").replace(" ", ""));
                    List<SdDevices> deviceList = SpringUtils.getBean(SdDevicesMapper.class).selectSdDevicesList(searchObject);
                    if(deviceList.size()>0){
                        //同一方向上的疏散标志整形桩号去重
                        int[] allNum = deviceList.stream().filter(s -> StringUtils.isNotBlank(s.getFEqId()))
                                .mapToInt(s -> s.getPileNum().intValue()).distinct().toArray();
                        //查找事故点最近的疏散标志
                        int index = Math.abs(compareValue - allNum[0]);
                        int closest = allNum[0];
                        for (int i = 0; i < allNum.length; i++) {
                            int abs = Math.abs(compareValue - allNum[i]);
                            if (abs <= index) {
                                index = abs;
                                closest = allNum[i];
                            }
                        }
                        Long pile = new Long((long) closest);
                        allEquipment = deviceList.stream().filter(devices -> devices.getPileNum().equals(pile)).collect(Collectors.toList()).
                                stream().map(s -> s.getEqId()).toArray(String[]::new);
                        rl.setEquipments(Arrays.stream(allEquipment).collect(Collectors.joining(",")));
                    }
                }
                // 设备类型名称
                String typeName = DevicesTypeEnum.getValue(Long.parseLong(rl.getEqTypeId()));
                // 设备类型
                map.put("typeName", typeName);
                SdEquipmentState state = new SdEquipmentState();
                state.setStateTypeId(Long.parseLong(rl.getEqTypeId()));
                state.setDeviceState(rl.getState());
                state.setIsControl(1);
                List<SdEquipmentState> sdEquipmentStates = SpringUtils.getBean(SdEquipmentStateMapper.class).selectDropSdEquipmentStateList(state);
                // 设备状态名称
                String stateName = sdEquipmentStates.get(0).getStateName();
                List<Map> result = sdDevicesMapper.getReserveProcessDevices(allEquipment);
                result.forEach(s -> {
                    s.put("eq_status", stateName);
                    eqOperation.add(typeName + s.get("pile") + " 控制执行: " + stateName + ";");
                });
                equipmentData.addAll(result);
                // 设备状态名称
                SdEquipmentStateIconFile sdEquipmentStateIconFile = new SdEquipmentStateIconFile();
                sdEquipmentStateIconFile.setStateIconId(sdEquipmentStates.get(0).getIconFileId());
                List<SdEquipmentStateIconFile> sdEquipmentStateIconFiles = SpringUtils.getBean(SdEquipmentIconFileMapper.class).selectStateIconFileList(sdEquipmentStateIconFile);
                iFileList.add(sdEquipmentStateIconFiles);
            }
            map.put("strategyRl", rlList);
            map.put("equipmentData", equipmentData);
            // 策略信息
            map.put("policyInformation", eqOperation);
            // 图片
            map.put("iFileList", iFileList);
            previewData.add(map);
        }
        return previewData;
    }
    /**
     * 应急预案列表预览
     * @param list
     * @return
     */
    public List<Map> reservePreview(List<SdReserveProcess> list){
        List<Map> previewData = new ArrayList<>();
        for (SdReserveProcess process : list) {
            HashMap<String, Object> map = new HashMap<>();
            // 预案Id
            map.put("reserveId", process.getReserveId());
            // 策略Id
            map.put("strategyId", process.getStrategyId());
            // 策略名称
            SdStrategy sdStrategy = SpringUtils.getBean(SdStrategyMapper.class).selectSdStrategyById(process.getStrategyId());
            map.put("strategyName", sdStrategy.getStrategyName());
            map.put("strategy", sdStrategy);
            // 设备类型Id
            map.put("deviceTypeId", process.getDeviceTypeId());
            List<SdStrategyRl> rlList = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlByStrategyId(process.getStrategyId());
            if (rlList.size() < 1) {
                continue;
            }
            map.put("strategyRl", rlList);
            List<String> eqOperation = new ArrayList<>();
            List<List> iFileList = new ArrayList<>();
            List<Map> equipmentData = new ArrayList<>();
            for (SdStrategyRl rl : rlList) {
                if (StrUtil.isEmpty(rl.getEquipments())) {
                    eqOperation.add("根据事件实际位置动态匹配");
                    continue;
                }
                // 设备类型名称
                String typeName = DevicesTypeEnum.getValue(Long.parseLong(rl.getEqTypeId()));
                // 设备类型
                map.put("typeName", typeName);
                SdEquipmentState state = new SdEquipmentState();
                state.setStateTypeId(Long.parseLong(rl.getEqTypeId()));
                state.setDeviceState(rl.getState());
                state.setIsControl(1);
                List<SdEquipmentState> sdEquipmentStates = SpringUtils.getBean(SdEquipmentStateMapper.class).selectDropSdEquipmentStateList(state);
                // 设备状态名称
                String stateName = sdEquipmentStates.get(0).getStateName();
                //所有设备ID
                String[] allEquipment = rl.getEquipments().split(",");
                //设备信息
                List<Map> result = sdDevicesMapper.getReserveProcessDevices(allEquipment);
                //result = result.stream().peek(s -> s.put("eq_status", stateName)).collect(Collectors.toList());
                equipmentData.addAll(result);
                result.forEach(s -> {
                    s.put("eq_status", stateName);
                    eqOperation.add(typeName + s.get("pile") + " 控制执行: " + stateName + ";");
                });
                SdEquipmentStateIconFile sdEquipmentStateIconFile = new SdEquipmentStateIconFile();
                sdEquipmentStateIconFile.setStateIconId(sdEquipmentStates.get(0).getIconFileId());
                List<SdEquipmentStateIconFile> sdEquipmentStateIconFiles = SpringUtils.getBean(SdEquipmentIconFileMapper.class).selectStateIconFileList(sdEquipmentStateIconFile);
                iFileList.add(sdEquipmentStateIconFiles);
            }
            map.put("equipmentData", equipmentData);
            // 策略信息
            map.put("policyInformation", eqOperation);
            // 图片
            map.put("iFileList", iFileList);
            previewData.add(map);
        }
        return previewData;
    }
    /**
     * 预案执行
     *
     * @param reserveId
     * @return
     */
    @Override
    public Integer planImplementa(String eventId, Long reserveId) {
        Map<String,Object> map = new HashMap<>();
        List<SdReserveProcess> sdReserveProcesses = sdReserveProcessMapper.selectSdReserveProcessByRid(reserveId);
        SdReservePlan sdReservePlan = SpringUtils.getBean(SdReservePlanMapper.class).selectSdReservePlanById(reserveId);
        map.put("planName",sdReservePlan.getPlanName());
        List<String> strategy = new ArrayList<>();
        for (SdReserveProcess process : sdReserveProcesses) {
            SdStrategy sdStrategy = SpringUtils.getBean(SdStrategyMapper.class).selectSdStrategyById(process.getStrategyId());
            strategy.add(sdStrategy.getStrategyName());
        }
        map.put("strategy",strategy);
        int result = sdEventFlowService.execPlanSaveEventFlow(eventId, map);
        return result;
    }
}
