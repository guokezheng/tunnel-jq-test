package com.tunnel.business.mapper.dataInfo;

import com.tunnel.business.domain.dataInfo.SdDeviceData;
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
    List<Map<String,String>> getDeviceDataByTunnelId(String tunnelId);

    public SdDeviceData selectLastRecord(SdDeviceData sdDeviceData);

    public List<Map<String, Object>> getTodayCOVIData(@Param("deviceId") String deviceId, @Param("itemId") Long itemId, @Param("today") String today);

    public List<Map<String, String>> selectCOVIDataList(@Param("deptId") String deptId, @Param("tunnelId") String tunnelId,@Param("beginTime") String beginTime,@Param("endTime") String endTime);

    public List<Map<String, String>> selectFSFXDataList(@Param("deptId") String deptId, @Param("tunnelId") String tunnelId,@Param("beginTime") String beginTime,@Param("endTime") String endTime);

    public List<Map<String, String>> selectDWDataList(@Param("deptId") String deptId, @Param("tunnelId") String tunnelId,@Param("beginTime") String beginTime,@Param("endTime") String endTime);

    public List<Map<String, String>> selectDNDataList(@Param("deptId") String deptId, @Param("tunnelId") String tunnelId,@Param("beginTime") String beginTime,@Param("endTime") String endTime);
}
