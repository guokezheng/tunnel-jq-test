package com.tunnel.business.service.event.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
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
import com.tunnel.business.service.event.ISdReserveProcessService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public int batchSdReserveProcessed(SdReserveProcessModel sdReserveProcesses) {
        List<SdReserveProcess> list = new ArrayList<>();
        sdReserveProcessMapper.deleteSdReserveProcessByPlanId(sdReserveProcesses.getReserveId());
        for (SdReserveProcess process : sdReserveProcesses.getSdReserveProcesses()) {
            List<SdStrategyRl> rlList = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlByStrategyId(process.getHandleStrategyList()[1]);
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
            Long[] list = new Long[2];
            list[0] = Long.valueOf(rlList.get(0).getEqTypeId());
            list[1] = process.getStrategyId();
            process.setHandleStrategyList(list);
        }
        return sdReserveProcesses;
    }

    /**
     * 查询预览
     *
     * @param reserveId
     * @return
     */
    @Override
    public List<Map> selectPreviewDisplay(Long reserveId) {
        List<Map> mapList = new ArrayList<>();
        // 预案流程节点
        List<SdReserveProcess> list = sdReserveProcessMapper.selectSdReserveProcessByRid(reserveId);
        for (SdReserveProcess process : list) {
            HashMap<String, Object> map = new HashMap<>();
            // 预案Id
            map.put("reserveId", process.getReserveId());
            // 策略Id
            map.put("strategyId", process.getStrategyId());
            // 策略名称
            SdStrategy sdStrategy = SpringUtils.getBean(SdStrategyMapper.class).selectSdStrategyById(process.getStrategyId());
            map.put("strategyName",sdStrategy.getStrategyName());
            map.put("strategy",sdStrategy);
            // 设备类型Id
            map.put("deviceTypeId", process.getDeviceTypeId());
            List<SdStrategyRl> rlList = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlByStrategyId(process.getStrategyId());

            map.put("strategyRl", rlList);
            List<String> strings = new ArrayList<>();
            List<List> iFileList = new ArrayList<>();
            List<Map> equipmentData = new ArrayList<>();
            for (SdStrategyRl rl : rlList) {
                // 设备类型名称
                String typeName = DevicesTypeEnum.getValue(Long.parseLong(rl.getEqTypeId()));
                // 设备类型
                map.put("typeName", typeName);
                SdEquipmentState state = new SdEquipmentState();
                state.setStateTypeId(Long.parseLong(rl.getEqTypeId()));
                state.setDeviceState(rl.getState());
                List<SdEquipmentState> sdEquipmentStates = SpringUtils.getBean(SdEquipmentStateMapper.class).selectDropSdEquipmentStateList(state);
                // 设备状态名称
                String stateName = sdEquipmentStates.get(0).getStateName();
                //所有设备ID
                //String[] allEquipment = rlList.stream().map(p->p.getEquipments()).collect(Collectors.joining(",")).split(",");
                String[] allEquipment = rl.getEquipments().split(",");
                //设备信息
                List<Map> result = sdDevicesMapper.getReserveProcessDevices(allEquipment);
                result = result.stream().peek(s->s.put("eq_status",stateName)).collect(Collectors.toList());
                equipmentData.addAll(result);
                strings.add(typeName + " 控制执行: " + stateName + ";");
                SdEquipmentStateIconFile sdEquipmentStateIconFile = new SdEquipmentStateIconFile();
                sdEquipmentStateIconFile.setStateIconId(sdEquipmentStates.get(0).getIconFileId());
                List<SdEquipmentStateIconFile> sdEquipmentStateIconFiles = SpringUtils.getBean(SdEquipmentIconFileMapper.class).selectStateIconFileList(sdEquipmentStateIconFile);
                iFileList.add(sdEquipmentStateIconFiles);
            }
            map.put("equipmentData",equipmentData);
            // 策略信息
            map.put("policyInformation", strings);
            // 图片
            map.put("iFileList", iFileList);
            mapList.add(map);
        }
        return mapList;
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
