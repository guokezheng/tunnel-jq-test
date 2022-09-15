package com.tunnel.platform.service.dataInfo;

import java.util.List;
import com.tunnel.platform.domain.dataInfo.SdDeviceDataRecord;

/**
 * 设备数据历史记录Service接口
 *
 * @author ruoyi
 * @date 2022-09-15
 */
public interface ISdDeviceDataRecordService
{
    /**
     * 查询设备数据历史记录
     *
     * @param id 设备数据历史记录主键
     * @return 设备数据历史记录
     */
    public SdDeviceDataRecord selectSdDeviceDataRecordById(Long id);

    /**
     * 查询设备数据历史记录列表
     *
     * @param sdDeviceDataRecord 设备数据历史记录
     * @return 设备数据历史记录集合
     */
    public List<SdDeviceDataRecord> selectSdDeviceDataRecordList(SdDeviceDataRecord sdDeviceDataRecord);

    /**
     * 新增设备数据历史记录
     *
     * @param sdDeviceDataRecord 设备数据历史记录
     * @return 结果
     */
    public int insertSdDeviceDataRecord(SdDeviceDataRecord sdDeviceDataRecord);

    /**
     * 修改设备数据历史记录
     *
     * @param sdDeviceDataRecord 设备数据历史记录
     * @return 结果
     */
    public int updateSdDeviceDataRecord(SdDeviceDataRecord sdDeviceDataRecord);

    /**
     * 批量删除设备数据历史记录
     *
     * @param ids 需要删除的设备数据历史记录主键集合
     * @return 结果
     */
    public int deleteSdDeviceDataRecordByIds(Long[] ids);

    /**
     * 删除设备数据历史记录信息
     *
     * @param id 设备数据历史记录主键
     * @return 结果
     */
    public int deleteSdDeviceDataRecordById(Long id);
}
