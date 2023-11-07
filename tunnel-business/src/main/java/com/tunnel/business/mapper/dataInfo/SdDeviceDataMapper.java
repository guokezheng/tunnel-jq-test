package com.tunnel.business.mapper.dataInfo;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.*;
import org.apache.ibatis.annotations.Param;;

import java.util.List;
import java.util.Map;


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

    /**
     * 根据隧道id查询当前设备的监测状态、实时数据或状态
     * @param tunnelId 隧道id
     * @return
     */
    List<Map<String,Object>> getDeviceDataByTunnelId(String tunnelId);

    public SdDeviceData selectLastRecord(SdDeviceData sdDeviceData);

    public List<Map<String, Double>> getTodayCOVIData(@Param("deviceId") String deviceId, @Param("itemId") Long itemId, @Param("today") String today);

    public List<Map<String, String>> selectCOVIDataList(@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("deviceId") String deviceId);

    public List<Map<String, String>> selectFSFXDataList(@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("deviceId") String deviceId);

    public List<Map<String, String>> selectDWDataList(@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("deviceId") String deviceId);

    public List<Map<String, String>> selectFJDataList(@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("deviceId") String deviceId);

    public List<Map<String, String>> selectFJDataListIds(@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("deviceId") String deviceId,
                                                      @Param("ids") String ids);

    public List<Map<String, String>> selectSJDataList(@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("deviceId") String deviceId);

    public List<Map<String, String>> selectSJDataListIds(@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("deviceId") String deviceId,
                                                         @Param("ids") String ids);

    public List<Map<String, String>> selectWSDDataList(@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("deviceId") String deviceId);

    public List<Map<String, String>> selectWSDDataListIds(@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("deviceId") String deviceId,
                                                          @Param("ids") String ids);
    public List<Map<String, String>> selectDNDataList(@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("deviceId") String deviceId);

    /**
     * 修改设备实时数据（华为kafka）
     *
     * @param sdDeviceData 设备实时数据（华为kafka）
     * @return 结果
     */
    public int updateKafkaDeviceData(SdDeviceData sdDeviceData);

    List<Map<String, String>> selectCOVIDataLineList(@Param("beginTime")String beginTime, @Param("endTime")String endTime, @Param("deviceId")String deviceId);

    List<Map<String, String>> selectFSFXDataLineList(@Param("beginTime")String beginTime, @Param("endTime")String endTime, @Param("deviceId")String deviceId);

    List<Map<String, String>> selectDNDataLineList(@Param("beginTime")String beginTime, @Param("endTime")String endTime, @Param("deviceId")String deviceId);

    List<Map<String, String>> selectDWDataLineList(@Param("beginTime")String beginTime, @Param("endTime")String endTime, @Param("deviceId")String deviceId);


    List<Map<String, String>> dataDevicesLogInfoList(SdDeviceData sdDeviceData);

    List<Map<String, String>> selectDeviceTableDataList(@Param("deviceId")String deviceId,@Param("beginTime")String beginTime,@Param("endTime")String endTime);

    List<SdDeviceData> exportDatainforTab(SdDeviceData sdDeviceData);

    List<SdDeviceCOVIData> selectCOVIExportDataList(@Param("beginTime")String beginTime, @Param("endTime")String endTime, @Param("deviceId")String deviceId, @Param("ids")String ids);

    List<SdDeviceFSFXData> selectFSFXExportDataList(@Param("beginTime")String beginTime, @Param("endTime")String endTime, @Param("deviceId")String deviceId, @Param("ids")String ids);

    List<SdDeviceDNData> selectDNExportDataList(@Param("beginTime")String beginTime, @Param("endTime")String endTime, @Param("deviceId")String deviceId, @Param("ids")String ids);

    List<SdDeviceDWData> selectDWExportDataList(@Param("beginTime")String beginTime, @Param("endTime")String endTime, @Param("deviceId")String deviceId, @Param("ids")String ids);


    List<SdDeviceData> selectSdDeviceDataInfo(SdDeviceData sdDeviceData);

    int updateFireMonitorData(SdDeviceData sdDeviceData);

    int insertFireMonitorData(SdDeviceData sdDeviceData);

    List<Map> getItemDataByEqId(String eqId);

    /**
     * 获取情报板信息
     * @param deviceId
     * @return
     */
    List<Map<String, Object>> getVmsData(@Param("deviceId") String deviceId);

    List<Map<String, Double>> getTodayData(@Param("deviceId") String deviceId, @Param("itemId") Long itemId, @Param("today") String today);

    /**
     * 查询当前隧道设备实时数据
     * @param tunnelId
     * @return
     */
    List<Map<String, Object>> getDevRealData(@Param("tunnelId") String tunnelId);
}
