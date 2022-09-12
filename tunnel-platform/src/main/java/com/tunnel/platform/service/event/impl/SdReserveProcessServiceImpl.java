package com.tunnel.platform.service.event.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.platform.domain.event.SdReserveProcess;
import com.tunnel.platform.domain.event.SdStrategyRl;
import com.tunnel.platform.mapper.event.SdReserveProcessMapper;
import com.tunnel.platform.mapper.event.SdStrategyRlMapper;
import com.tunnel.platform.service.event.ISdReserveProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private SdStrategyRlMapper sdStrategyRlMapper;

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
        sdReserveProcess.setCreateTime(DateUtils.getNowDate());
        String[] strategyIds = sdReserveProcess.getStrategyIds().split(",");
        List<SdReserveProcess> list = new ArrayList<>();
        for (int i = 0; i < strategyIds.length; i++) {
            SdReserveProcess reserveProcess = new SdReserveProcess();
            List<SdStrategyRl> rlList = sdStrategyRlMapper.selectSdStrategyRlByStrategyId(Long.parseLong(strategyIds[i]));
            reserveProcess.setReserveId(sdReserveProcess.getReserveId());
            reserveProcess.setDeviceTypeId(Long.parseLong(rlList.get(0).getEqTypeId()));
            reserveProcess.setProcessName(sdReserveProcess.getProcessName());
            reserveProcess.setProcessSort(sdReserveProcess.getProcessSort());
            reserveProcess.setStrategyId(Long.parseLong(strategyIds[i]));
            reserveProcess.setCreateTime(DateUtils.getNowDate());
            reserveProcess.setCreateBy(SecurityUtils.getUsername());
            list.add(reserveProcess);
        }
        return sdReserveProcessMapper.batchSdReserveProcess(list);
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
        return sdReserveProcessMapper.updateSdReserveProcess(sdReserveProcess);
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
}
