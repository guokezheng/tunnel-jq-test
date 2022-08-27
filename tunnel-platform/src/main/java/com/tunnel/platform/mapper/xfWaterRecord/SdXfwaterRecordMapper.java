package com.tunnel.platform.mapper.xfWaterRecord;

import java.util.List;
import java.util.Map;

import com.tunnel.platform.domain.xfWaterRecord.SdXfwaterRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 消防水设备监测记录Mapper接口
 *
 * @author ruoyi
 * @date 2022-02-11
 */
public interface SdXfwaterRecordMapper
{
    /**
     * 查询消防水设备监测记录
     *
     * @param id 消防水设备监测记录主键
     * @return 消防水设备监测记录
     */
    public SdXfwaterRecord selectSdXfwaterRecordById(Long id);

    /**
     * 查询消防水设备监测记录列表
     *
     * @param sdXfwaterRecord 消防水设备监测记录
     * @return 消防水设备监测记录集合
     */
    public List<SdXfwaterRecord> selectSdXfwaterRecordList(SdXfwaterRecord sdXfwaterRecord);

    /**
     * 新增消防水设备监测记录
     *
     * @param sdXfwaterRecord 消防水设备监测记录
     * @return 结果
     */
    public int insertSdXfwaterRecord(SdXfwaterRecord sdXfwaterRecord);

    /**
     * 修改消防水设备监测记录
     *
     * @param sdXfwaterRecord 消防水设备监测记录
     * @return 结果
     */
    public int updateSdXfwaterRecord(SdXfwaterRecord sdXfwaterRecord);

    /**
     * 删除消防水设备监测记录
     *
     * @param id 消防水设备监测记录主键
     * @return 结果
     */
    public int deleteSdXfwaterRecordById(Long id);

    /**
     * 批量删除消防水设备监测记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdXfwaterRecordByIds(Long[] ids);

    public List<SdXfwaterRecord> getNumberOfPressureGaugesCollectedPerDay(@Param("equipmentId") String equipmentId,@Param("tunnelId") String tunnelId);

    public List<Map<String, Object>> getAllPressureGaugesCollectedPerDay(@Param("deptId") Long deptId);

    public List<Map<String, Object>> getNumberOfPressureGaugesCollectedPerMonth(String equipmentId);

    public SdXfwaterRecord getRecentRecordByEqId(@Param("equipmentId") String equipmentId, @Param("deptId") Long deptId);

    public List<Map<String, Object>> selectWarningInfoMsgByYear(@Param("eqId") String equipmentId, @Param("tunnelId") String tunnelId, @Param("params") Map<String, Object> params);

    public List<Map<String, Object>> selectWarningInfoMsgByMonth(@Param("eqId") String equipmentId, @Param("tunnelId") String tunnelId, @Param("lastDayOfMonth") String lastDayOfMonth,@Param("params") Map<String, Object> params);

    public List<Map<String, Object>> selectWarningInfoMsgByDay(@Param("eqId") String equipmentId, @Param("tunnelId") String tunnelId, @Param("params") Map<String, Object> params);

    public List<Map<String, Object>> selectXfRecord(@Param("equipmentId") String equipmentId, @Param("tunnelId") String tunnelId, @Param("params") Map<String, Object> params);
}
