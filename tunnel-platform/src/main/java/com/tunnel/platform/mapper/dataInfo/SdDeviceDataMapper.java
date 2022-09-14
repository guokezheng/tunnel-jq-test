package com.tunnel.platform.mapper.dataInfo;

import java.util.List;
import com.tunnel.platform.domain.dataInfo.SdDeviceData;

/**
 * 设备实时数据（存储模拟量）Mapper接口
 *
 * @author ruoyi
 * @date 2022-09-13
 */
public interface SdDeviceDataMapper
{
    /**
     * 查询设备实时数据（存储模拟量）
     *
     * @param id 设备实时数据（存储模拟量）主键
     * @return 设备实时数据（存储模拟量）
     */
    public SdDeviceData selectSdDeviceDataById(Long id);

    /**
     * 查询设备实时数据（存储模拟量）列表
     *
     * @param sdDeviceData 设备实时数据（存储模拟量）
     * @return 设备实时数据（存储模拟量）集合
     */
    public List<SdDeviceData> selectSdDeviceDataList(SdDeviceData sdDeviceData);

    /**
     * 新增设备实时数据（存储模拟量）
     *
     * @param sdDeviceData 设备实时数据（存储模拟量）
     * @return 结果
     */
    public int insertSdDeviceData(SdDeviceData sdDeviceData);

    /**
     * 修改设备实时数据（存储模拟量）
     *
     * @param sdDeviceData 设备实时数据（存储模拟量）
     * @return 结果
     */
    public int updateSdDeviceData(SdDeviceData sdDeviceData);

    /**
     * 删除设备实时数据（存储模拟量）
     *
     * @param id 设备实时数据（存储模拟量）主键
     * @return 结果
     */
    public int deleteSdDeviceDataById(Long id);

    /**
     * 批量删除设备实时数据（存储模拟量）
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdDeviceDataByIds(Long[] ids);

    public SdDeviceData selectLastRecord(SdDeviceData sdDeviceData);
}
