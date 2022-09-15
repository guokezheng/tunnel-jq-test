package com.tunnel.platform.service.event.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.platform.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.platform.domain.dataInfo.SdEquipmentState;
import com.tunnel.platform.domain.dataInfo.SdEquipmentStateIconFile;
import com.tunnel.platform.domain.event.SdReserveProcess;
import com.tunnel.platform.domain.event.SdReserveProcessModel;
import com.tunnel.platform.domain.event.SdStrategyRl;
import com.tunnel.platform.mapper.dataInfo.SdEquipmentIconFileMapper;
import com.tunnel.platform.mapper.dataInfo.SdEquipmentStateMapper;
import com.tunnel.platform.mapper.event.SdReserveProcessMapper;
import com.tunnel.platform.mapper.event.SdStrategyRlMapper;
import com.tunnel.platform.service.event.ISdReserveProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预案流程节点Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-09-02
 */
@Service
public class SdReserveProcessServiceImpl implements ISdReserveProcessService
{
    @Autowired
    private SdReserveProcessMapper sdReserveProcessMapper;


    /**
     * 查询预案流程节点
     * 
     * @param id 预案流程节点主键
     * @return 预案流程节点
     */
    @Override
    public SdReserveProcess selectSdReserveProcessById(Long id)
    {
        return sdReserveProcessMapper.selectSdReserveProcessById(id);
    }

    /**
     * 查询预案流程节点列表
     * 
     * @param sdReserveProcess 预案流程节点
     * @return 预案流程节点
     */
    @Override
    public List<SdReserveProcess> selectSdReserveProcessList(SdReserveProcess sdReserveProcess)
    {
        return sdReserveProcessMapper.selectSdReserveProcessList(sdReserveProcess);
    }

    /**
     * 新增预案流程节点
     * 
     * @param sdReserveProcess 预案流程节点
     * @return 结果
     */
    @Override
    public int insertSdReserveProcess(SdReserveProcess sdReserveProcess)
    {
        sdReserveProcessMapper.deleteSdReserveProcessByPlanId(sdReserveProcess.getReserveId());
        List<SdStrategyRl> rlList = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlByStrategyId(sdReserveProcess.getHandleStrategyList());
        SdReserveProcess reserveProcess = new SdReserveProcess();
        reserveProcess.setReserveId(sdReserveProcess.getReserveId());
        reserveProcess.setDeviceTypeId(Long.parseLong(rlList.get(0).getEqTypeId()));
        reserveProcess.setProcessName(sdReserveProcess.getProcessName());
        reserveProcess.setProcessSort(sdReserveProcess.getProcessSort());
        reserveProcess.setStrategyId(sdReserveProcess.getHandleStrategyList());
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
            List<SdStrategyRl> rlList = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlByStrategyId(process.getHandleStrategyList());
            SdReserveProcess reserveProcess = new SdReserveProcess();
            reserveProcess.setReserveId(sdReserveProcesses.getReserveId());
            reserveProcess.setDeviceTypeId(Long.parseLong(rlList.get(0).getEqTypeId()));
            reserveProcess.setProcessName(process.getProcessName());
            reserveProcess.setProcessSort(process.getProcessSort());
            reserveProcess.setStrategyId(process.getHandleStrategyList());
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
    public int updateSdReserveProcess(SdReserveProcess sdReserveProcess)
    {
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
    public int deleteSdReserveProcessByIds(Long[] ids)
    {
        return sdReserveProcessMapper.deleteSdReserveProcessByIds(ids);
    }

    /**
     * 删除预案流程节点信息
     * 
     * @param id 预案流程节点主键
     * @return 结果
     */
    @Override
    public int deleteSdReserveProcessById(Long id)
    {
        return sdReserveProcessMapper.deleteSdReserveProcessById(id);
    }

    /**
     * 根据预案id删除预案流程节点信息
     * @param id
     * @return
     */
    @Override
    public int deleteSdReserveProcessByPlanId(Long id) {
        return sdReserveProcessMapper.deleteSdReserveProcessByPlanId(id);
    }

    /**
     * 根据预案id查找预案流程节点信息
     * @param RId
     * @return
     */
    @Override
    public List<SdReserveProcess> selectSdReserveProcessByRId(Long RId) {
        SdReserveProcess sdReserveProcess = new SdReserveProcess();
        sdReserveProcess.setReserveId(RId);
        return sdReserveProcessMapper.selectSdReserveProcessByRid(sdReserveProcess);
    }

    @Override
    public List<Map> selectPreviewDisplay(Long reserveId) {
        SdReserveProcess reserveProcess = new SdReserveProcess();
        reserveProcess.setReserveId(reserveId);
        List<Map> mapList = new ArrayList<>();
        // 预案流程节点
        List<SdReserveProcess> list = sdReserveProcessMapper.selectSdReserveProcessList(reserveProcess);
        for (SdReserveProcess process : list) {
            HashMap<String, Object> map = new HashMap<>();
            // 预案Id
            map.put("reserveId",process.getReserveId());
            // 策略Id
            map.put("strategyId",process.getStrategyId());
            // 设备类型Id
            map.put("deviceTypeId",process.getDeviceTypeId());
            List<SdStrategyRl> rlList = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlByStrategyId(process.getStrategyId());
            map.put("strategyRl",rlList);
            List<String> strings = new ArrayList<>();
            List<List> iFileList = new ArrayList<>();
            for (SdStrategyRl rl : rlList) {
                // 设备类型名称
                String typeName = DevicesTypeEnum.getValue(Integer.parseInt(rl.getEqTypeId()));
                SdEquipmentState state = new SdEquipmentState();
                state.setStateTypeId(Long.parseLong(rl.getEqTypeId()));
                state.setDeviceState(rl.getState());
                List<SdEquipmentState> sdEquipmentStates = SpringUtils.getBean(SdEquipmentStateMapper.class).selectDropSdEquipmentStateList(state);
                // 设备状态名称
                String stateName = sdEquipmentStates.get(0).getStateName();
                strings.add(typeName+ " 控制执行: " + stateName + ";");
                SdEquipmentStateIconFile sdEquipmentStateIconFile = new SdEquipmentStateIconFile();
                sdEquipmentStateIconFile.setStateIconId(sdEquipmentStates.get(0).getIconFileId());
                List<SdEquipmentStateIconFile> sdEquipmentStateIconFiles = SpringUtils.getBean(SdEquipmentIconFileMapper.class).selectStateIconFileList(sdEquipmentStateIconFile);
                iFileList.add(sdEquipmentStateIconFiles);
            }
            // 策略信息
            map.put("policyInformation", strings);
            // 图片
            map.put("iFileList",iFileList);
            mapList.add(map);
        }
        return mapList;
    }
}
