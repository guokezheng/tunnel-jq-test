package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdRepair;
import com.tunnel.business.mapper.dataInfo.SdRepairMapper;
import com.tunnel.business.service.dataInfo.ISdRepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备巡检修Service业务层处理
 *
 * @author liubaokui
 * @date 2021-05-12
 */
@Service
public class SdRepairServiceImpl implements ISdRepairService {
    @Autowired
    private SdRepairMapper sdRepairMapper;

    /**
     * 查询设备巡检修
     *
     * @param repairId 设备巡检修ID
     * @return 设备巡检修
     */
    @Override
    public SdRepair selectSdRepairById(Long repairId) {
        return sdRepairMapper.selectSdRepairById(repairId);
    }

    /**
     * 查询设备巡检修列表
     *
     * @param sdRepair 设备巡检修
     * @return 设备巡检修
     */
    @Override
    public List<SdRepair> selectSdRepairList(SdRepair sdRepair) {
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        sdRepair.getParams().put("deptId", deptId);
        return sdRepairMapper.selectSdRepairList(sdRepair);
    }

    /**
     * 新增设备巡检修
     *
     * @param sdRepair 设备巡检修
     * @return 结果
     */
    @Override
    public int insertSdRepair(SdRepair sdRepair) {
        sdRepair.setCreateTime(DateUtils.getNowDate());
        sdRepair.setCreateName(SecurityUtils.getUsername());
        return sdRepairMapper.insertSdRepair(sdRepair);
    }

    /**
     * 修改设备巡检修
     *
     * @param sdRepair 设备巡检修
     * @return 结果
     */
    @Override
    public int updateSdRepair(SdRepair sdRepair) {
        sdRepair.setUpdateName(SecurityUtils.getUsername());
        sdRepair.setUpdateTime(DateUtils.getNowDate());
        return sdRepairMapper.updateSdRepair(sdRepair);
    }

    /**
     * 批量删除设备巡检修
     *
     * @param repairIds 需要删除的设备巡检修ID
     * @return 结果
     */
    @Override
    public int deleteSdRepairByIds(Long[] repairIds) {
        return sdRepairMapper.deleteSdRepairByIds(repairIds);
    }

    /**
     * 删除设备巡检修信息
     *
     * @param repairId 设备巡检修ID
     * @return 结果
     */
    @Override
    public int deleteSdRepairById(Long repairId) {
        return sdRepairMapper.deleteSdRepairById(repairId);
    }
}
