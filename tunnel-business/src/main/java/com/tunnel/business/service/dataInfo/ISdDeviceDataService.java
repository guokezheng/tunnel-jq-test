package com.tunnel.business.service.dataInfo;


import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.*;

import java.util.List;
import java.util.Map;

/**
 * 设备实时数据（存储模拟量）Service接口
 *
 * @author ruoyi
 * @date 2022-09-13
 */
public interface ISdDeviceDataService {
    /**
     * 查询设备实时数据（存储模拟量）
     *
     * @param id 设备实时数据（存储模拟量）主键
     * @return 设备实时数据（存储模拟量）
     */
    SdDeviceData selectSdDeviceDataById(Long id);

    /**
     * 查询设备实时数据（存储模拟量）列表
     *
     * @param sdDeviceData 设备实时数据（存储模拟量）
     * @return 设备实时数据（存储模拟量）集合
     */
    List<SdDeviceData> selectSdDeviceDataList(SdDeviceData sdDeviceData);

    /**
     * 新增设备实时数据（存储模拟量）
     *
     * @param sdDeviceData 设备实时数据（存储模拟量）
     * @return 结果
     */
    int insertSdDeviceData(SdDeviceData sdDeviceData);

    /**
     * 修改设备实时数据（存储模拟量）
     *
     * @param sdDeviceData 设备实时数据（存储模拟量）
     * @return 结果
     */
    int updateSdDeviceData(SdDeviceData sdDeviceData);

    /**
     * 批量删除设备实时数据（存储模拟量）
     *
     * @param ids 需要删除的设备实时数据（存储模拟量）主键集合
     * @return 结果
     */
    int deleteSdDeviceDataByIds(Long[] ids);

    /**
     * 删除设备实时数据（存储模拟量）信息
     *
     * @param id 设备实时数据（存储模拟量）主键
     * @return 结果
     */
    int deleteSdDeviceDataById(Long id);


    /**
     * 根据隧道id查询当前设备的监测状态、实时数据或状态
     *
     * @param tunnelId 隧道id
     * @return
     */
    List<Map<String, String>> getDeviceDataByTunnelId(String tunnelId);

    Map<String, Object> getTodayCOVIData(String deviceId);

    Map<String, Object> getTodayFSFXData(String deviceId);

    Map<String, Object> getTodayYcylData(String deviceId);

    Map<String, Object> getTodayLDData(String deviceId);

    List<Map<String, String>> dataLogInfoList(SdDeviceData sdDeviceData);

    Map<String, Object> energyConsumptionDetection(String tunnelId);

    /**
     * 获取风机安全检测仪实时数据
     * @param deviceId
     * @return
     */
    AjaxResult getFanSafeData(String deviceId);

    List<Map<String, String>> dataLogInfoLineList(SdDeviceData sdDeviceData);

    /**
     * 查询设备列表
     * @param sdDeviceData
     * @return
     */
    List<Map<String, String>> dataDevicesLogInfoList(SdDeviceData sdDeviceData);


    /**
     * 导出Tab
     * @param sdDeviceData
     * @return
     */
    List<SdDeviceData> exportDatainforTab(SdDeviceData sdDeviceData);

    List<SdDeviceCOVIData> handleExportRecord(SdDeviceCOVIData sdDeviceCOVIData);

    List<SdDeviceFSFXData> handleFSFXExportRecord(SdDeviceCOVIData sdDeviceCOVIData);

    List<SdDeviceDNData> handleDNExportRecord(SdDeviceCOVIData sdDeviceCOVIData);

    List<SdDeviceDWData> handleDWExportRecord(SdDeviceCOVIData sdDeviceCOVIData);

     void updateDeviceData(String deviceId, String value, Integer itemId,boolean createLog);


    /**
     * 修改设备数据表中实时数据
     * @param sdDevices 设备信息
     * @param value 数据
     * @param itemId 数据项
     */
   void updateDeviceData(SdDevices sdDevices, String value, Long itemId);

    List<Map> getItemDataByEqId(String eqId);
}
