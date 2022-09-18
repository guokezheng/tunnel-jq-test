package com.tunnel.platform.service.dataInfo;

import java.util.List;
import java.util.Map;

import com.tunnel.platform.domain.dataInfo.SdDeviceData;

/**
 * 设备实时数据（存储模拟量）Service接口
 *
 * @author ruoyi
 * @date 2022-09-13
 */
public interface ISdDeviceDataService
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
     * 批量删除设备实时数据（存储模拟量）
     *
     * @param ids 需要删除的设备实时数据（存储模拟量）主键集合
     * @return 结果
     */
    public int deleteSdDeviceDataByIds(Long[] ids);

    /**
     * 删除设备实时数据（存储模拟量）信息
     *
     * @param id 设备实时数据（存储模拟量）主键
     * @return 结果
     */
    public int deleteSdDeviceDataById(Long id);


    /**
     * 根据隧道id查询当前设备的监测状态、实时数据或状态
     * @param tunnelId 隧道id
     * @return
     */
    List<Map<String,String>> getDeviceDataByTunnelId(String tunnelId);

    public Map<String, Object> getTodayCOVIData(String deviceId);

    public Map<String, Object> getTodayFSFXData(String deviceId);

    public Map<String, Object> getTodayLDData(String deviceId);
}
