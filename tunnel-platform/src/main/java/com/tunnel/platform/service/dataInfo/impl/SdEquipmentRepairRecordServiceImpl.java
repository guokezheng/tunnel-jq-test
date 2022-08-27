package com.tunnel.platform.service.dataInfo.impl;


import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.platform.domain.dataInfo.SdEquipmentRepairRecord;
import com.tunnel.platform.mapper.dataInfo.SdEquipmentRepairRecordMapper;
import com.tunnel.platform.service.dataInfo.ISdEquipmentRepairRecordService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备维修记录Service业务层处理
 *
 * @author zhangweitian
 * @date 2020-12-29
 */
@Service
public class SdEquipmentRepairRecordServiceImpl implements ISdEquipmentRepairRecordService
{
    @Autowired
    private SdEquipmentRepairRecordMapper sdEquipmentRepairRecordMapper;

    /**
     * 查询设备维修记录
     *
     * @param repairId 设备维修记录ID
     * @return 设备维修记录
     */
    @Override
    public SdEquipmentRepairRecord selectSdEquipmentRepairRecordById(Long repairId)
    {
        return sdEquipmentRepairRecordMapper.selectSdEquipmentRepairRecordById(repairId);
    }

    /**
     * 查询设备维修记录列表
     *
     * @param sdEquipmentRepairRecord 设备维修记录
     * @return 设备维修记录
     */
    @Override
    public List<SdEquipmentRepairRecord> selectSdEquipmentRepairRecordList(SdEquipmentRepairRecord sdEquipmentRepairRecord)
    {
        Long deptId = SecurityUtils.getDeptId();
        sdEquipmentRepairRecord.getParams().put("deptId",deptId);
        return sdEquipmentRepairRecordMapper.selectDropSdEquipmentRepairRecordList(sdEquipmentRepairRecord);
    }

    /**
     * 新增设备维修记录
     *
     * @param sdEquipmentRepairRecord 设备维修记录
     * @return 结果
     */
    @Override
    public int insertSdEquipmentRepairRecord(SdEquipmentRepairRecord sdEquipmentRepairRecord)
    {
        sdEquipmentRepairRecord.setCreateTime(DateUtils.getNowDate());
        return sdEquipmentRepairRecordMapper.insertSdEquipmentRepairRecord(sdEquipmentRepairRecord);
    }

    /**
     * 修改设备维修记录
     *
     * @param sdEquipmentRepairRecord 设备维修记录
     * @return 结果
     */
    @Override
    public int updateSdEquipmentRepairRecord(SdEquipmentRepairRecord sdEquipmentRepairRecord)
    {
        sdEquipmentRepairRecord.setUpdateTime(DateUtils.getNowDate());
        return sdEquipmentRepairRecordMapper.updateSdEquipmentRepairRecord(sdEquipmentRepairRecord);
    }

    /**
     * 批量删除设备维修记录
     *
     * @param repairIds 需要删除的设备维修记录ID
     * @return 结果
     */
    @Override
    public int deleteSdEquipmentRepairRecordByIds(Long[] repairIds)
    {
        return sdEquipmentRepairRecordMapper.deleteSdEquipmentRepairRecordByIds(repairIds);
    }

    /**
     * 删除设备维修记录信息
     *
     * @param repairId 设备维修记录ID
     * @return 结果
     */
    @Override
    public int deleteSdEquipmentRepairRecordById(Long repairId)
    {
        return sdEquipmentRepairRecordMapper.deleteSdEquipmentRepairRecordById(repairId);
    }
}
