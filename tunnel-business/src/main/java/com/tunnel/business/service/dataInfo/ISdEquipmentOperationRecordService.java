package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdEquipmentOperationRecord;

import java.util.List;

/**
 * 设备运行记录Service接口
 *
 * @author ruoyi
 * @date 2022-01-21
 */
public interface ISdEquipmentOperationRecordService {
    /**
     * 查询设备运行记录
     *
     * @param id 设备运行记录主键
     * @return 设备运行记录
     */
    SdEquipmentOperationRecord selectSdEquipmentOperationRecordById(Long id);

    /**
     * 查询设备运行记录列表
     *
     * @param sdEquipmentOperationRecord 设备运行记录
     * @return 设备运行记录集合
     */
    List<SdEquipmentOperationRecord> selectSdEquipmentOperationRecordList(SdEquipmentOperationRecord sdEquipmentOperationRecord);

    /**
     * 新增设备运行记录
     *
     * @param sdEquipmentOperationRecord 设备运行记录
     * @return 结果
     */
    int insertSdEquipmentOperationRecord(SdEquipmentOperationRecord sdEquipmentOperationRecord);

    /**
     * 修改设备运行记录
     *
     * @param sdEquipmentOperationRecord 设备运行记录
     * @return 结果
     */
    int updateSdEquipmentOperationRecord(SdEquipmentOperationRecord sdEquipmentOperationRecord);

    /**
     * 批量删除设备运行记录
     *
     * @param ids 需要删除的设备运行记录主键集合
     * @return 结果
     */
    int deleteSdEquipmentOperationRecordByIds(Long[] ids);

    /**
     * 删除设备运行记录信息
     *
     * @param id 设备运行记录主键
     * @return 结果
     */
    int deleteSdEquipmentOperationRecordById(Long id);
}
