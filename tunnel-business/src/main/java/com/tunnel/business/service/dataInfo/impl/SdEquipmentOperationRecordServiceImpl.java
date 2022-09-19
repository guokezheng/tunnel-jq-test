package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdEquipmentOperationRecord;
import com.tunnel.business.mapper.dataInfo.SdEquipmentOperationRecordMapper;
import com.tunnel.business.service.dataInfo.ISdEquipmentOperationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备运行记录Service业务层处理
 *
 * @author ruoyi
 * @date 2022-01-21
 */
@Service
public class SdEquipmentOperationRecordServiceImpl implements ISdEquipmentOperationRecordService {
    @Autowired
    private SdEquipmentOperationRecordMapper sdEquipmentOperationRecordMapper;

    /**
     * 查询设备运行记录
     *
     * @param id 设备运行记录主键
     * @return 设备运行记录
     */
    @Override
    public SdEquipmentOperationRecord selectSdEquipmentOperationRecordById(Long id) {
        return sdEquipmentOperationRecordMapper.selectSdEquipmentOperationRecordById(id);
    }

    /**
     * 查询设备运行记录列表
     *
     * @param sdEquipmentOperationRecord 设备运行记录
     * @return 设备运行记录
     */
    @Override
    public List<SdEquipmentOperationRecord> selectSdEquipmentOperationRecordList(SdEquipmentOperationRecord sdEquipmentOperationRecord) {
        Long deptId = SecurityUtils.getDeptId();
        sdEquipmentOperationRecord.getParams().put("deptId", deptId);
        return sdEquipmentOperationRecordMapper.selectSdEquipmentOperationRecordList(sdEquipmentOperationRecord);
    }

    /**
     * 新增设备运行记录
     *
     * @param sdEquipmentOperationRecord 设备运行记录
     * @return 结果
     */
    @Override
    public int insertSdEquipmentOperationRecord(SdEquipmentOperationRecord sdEquipmentOperationRecord) {
        return sdEquipmentOperationRecordMapper.insertSdEquipmentOperationRecord(sdEquipmentOperationRecord);
    }

    /**
     * 修改设备运行记录
     *
     * @param sdEquipmentOperationRecord 设备运行记录
     * @return 结果
     */
    @Override
    public int updateSdEquipmentOperationRecord(SdEquipmentOperationRecord sdEquipmentOperationRecord) {
        sdEquipmentOperationRecord.setUpdateTime(DateUtils.getNowDate());
        return sdEquipmentOperationRecordMapper.updateSdEquipmentOperationRecord(sdEquipmentOperationRecord);
    }

    /**
     * 批量删除设备运行记录
     *
     * @param ids 需要删除的设备运行记录主键
     * @return 结果
     */
    @Override
    public int deleteSdEquipmentOperationRecordByIds(Long[] ids) {
        return sdEquipmentOperationRecordMapper.deleteSdEquipmentOperationRecordByIds(ids);
    }

    /**
     * 删除设备运行记录信息
     *
     * @param id 设备运行记录主键
     * @return 结果
     */
    @Override
    public int deleteSdEquipmentOperationRecordById(Long id) {
        return sdEquipmentOperationRecordMapper.deleteSdEquipmentOperationRecordById(id);
    }
}
