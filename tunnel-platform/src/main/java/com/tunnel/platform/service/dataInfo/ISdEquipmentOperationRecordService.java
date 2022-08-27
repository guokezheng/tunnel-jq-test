package com.tunnel.platform.service.dataInfo;

import java.util.List;
import com.tunnel.platform.domain.dataInfo.SdEquipmentOperationRecord;

/**
 * 设备运行记录Service接口
 * 
 * @author ruoyi
 * @date 2022-01-21
 */
public interface ISdEquipmentOperationRecordService 
{
    /**
     * 查询设备运行记录
     * 
     * @param id 设备运行记录主键
     * @return 设备运行记录
     */
    public SdEquipmentOperationRecord selectSdEquipmentOperationRecordById(Long id);

    /**
     * 查询设备运行记录列表
     * 
     * @param sdEquipmentOperationRecord 设备运行记录
     * @return 设备运行记录集合
     */
    public List<SdEquipmentOperationRecord> selectSdEquipmentOperationRecordList(SdEquipmentOperationRecord sdEquipmentOperationRecord);

    /**
     * 新增设备运行记录
     * 
     * @param sdEquipmentOperationRecord 设备运行记录
     * @return 结果
     */
    public int insertSdEquipmentOperationRecord(SdEquipmentOperationRecord sdEquipmentOperationRecord);

    /**
     * 修改设备运行记录
     * 
     * @param sdEquipmentOperationRecord 设备运行记录
     * @return 结果
     */
    public int updateSdEquipmentOperationRecord(SdEquipmentOperationRecord sdEquipmentOperationRecord);

    /**
     * 批量删除设备运行记录
     * 
     * @param ids 需要删除的设备运行记录主键集合
     * @return 结果
     */
    public int deleteSdEquipmentOperationRecordByIds(Long[] ids);

    /**
     * 删除设备运行记录信息
     * 
     * @param id 设备运行记录主键
     * @return 结果
     */
    public int deleteSdEquipmentOperationRecordById(Long id);
}
