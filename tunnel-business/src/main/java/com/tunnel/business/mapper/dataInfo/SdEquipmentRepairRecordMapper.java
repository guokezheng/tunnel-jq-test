package com.tunnel.business.mapper.dataInfo;


import com.tunnel.business.domain.dataInfo.SdEquipmentRepairRecord;

import java.util.List;

/**
 * 设备维修记录Mapper接口
 * 
 * @author zhangweitian
 * @date 2020-12-29
 */
public interface SdEquipmentRepairRecordMapper 
{
    /**
     * 查询设备维修记录
     * 
     * @param repairId 设备维修记录ID
     * @return 设备维修记录
     */
    public SdEquipmentRepairRecord selectSdEquipmentRepairRecordById(Long repairId);

    /**
     * 查询设备维修记录列表
     * 
     * @param sdEquipmentRepairRecord 设备维修记录
     * @return 设备维修记录集合
     */
    public List<SdEquipmentRepairRecord> selectSdEquipmentRepairRecordList(SdEquipmentRepairRecord sdEquipmentRepairRecord);
    public List<SdEquipmentRepairRecord> selectDropSdEquipmentRepairRecordList(SdEquipmentRepairRecord sdEquipmentRepairRecord);
    /**
     * 新增设备维修记录
     * 
     * @param sdEquipmentRepairRecord 设备维修记录
     * @return 结果
     */
    public int insertSdEquipmentRepairRecord(SdEquipmentRepairRecord sdEquipmentRepairRecord);

    /**
     * 修改设备维修记录
     * 
     * @param sdEquipmentRepairRecord 设备维修记录
     * @return 结果
     */
    public int updateSdEquipmentRepairRecord(SdEquipmentRepairRecord sdEquipmentRepairRecord);

    /**
     * 删除设备维修记录
     * 
     * @param repairId 设备维修记录ID
     * @return 结果
     */
    public int deleteSdEquipmentRepairRecordById(Long repairId);

    /**
     * 批量删除设备维修记录
     * 
     * @param repairIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdEquipmentRepairRecordByIds(Long[] repairIds);
}
