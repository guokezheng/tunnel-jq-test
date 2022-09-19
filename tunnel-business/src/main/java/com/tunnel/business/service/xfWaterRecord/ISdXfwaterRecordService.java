package com.tunnel.business.service.xfWaterRecord;


import com.tunnel.business.domain.xfWaterRecord.SdXfwaterRecord;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 消防水设备监测记录Service接口
 *
 * @author ruoyi
 * @date 2022-02-11
 */
public interface ISdXfwaterRecordService {
    /**
     * 查询消防水设备监测记录
     *
     * @param id 消防水设备监测记录主键
     * @return 消防水设备监测记录
     */
    SdXfwaterRecord selectSdXfwaterRecordById(Long id);

    /**
     * 查询消防水设备监测记录列表
     *
     * @param sdXfwaterRecord 消防水设备监测记录
     * @return 消防水设备监测记录集合
     */
    List<SdXfwaterRecord> selectSdXfwaterRecordList(SdXfwaterRecord sdXfwaterRecord);

    /**
     * 新增消防水设备监测记录
     *
     * @param sdXfwaterRecord 消防水设备监测记录
     * @return 结果
     */
    int insertSdXfwaterRecord(SdXfwaterRecord sdXfwaterRecord);

    /**
     * 修改消防水设备监测记录
     *
     * @param sdXfwaterRecord 消防水设备监测记录
     * @return 结果
     */
    int updateSdXfwaterRecord(SdXfwaterRecord sdXfwaterRecord);

    /**
     * 批量删除消防水设备监测记录
     *
     * @param ids 需要删除的消防水设备监测记录主键集合
     * @return 结果
     */
    int deleteSdXfwaterRecordByIds(Long[] ids);

    /**
     * 删除消防水设备监测记录信息
     *
     * @param id 消防水设备监测记录主键
     * @return 结果
     */
    int deleteSdXfwaterRecordById(Long id);

    /**
     * 获取指定压力表压力上下限和压力数据
     */
    List<SdXfwaterRecord> getNumberOfPressureGaugesCollectedPerDay(String equipmentId, String tunnelId);

    /**
     * 获取所有压力表一天上报次数
     */
    List<Map<String, Object>> getAllPressureGaugesCollectedPerDay();

    /**
     * 获取指定压力表近一个月的所有采集记录
     */
    List<Map<String, Object>> getNumberOfPressureGaugesCollectedPerMonth(String equipmentId);

    /**
     * 获取指定隧道下压力表的状态记录
     */
    List<SdXfwaterRecord> getObtainTheStatusOfAllPressureGauges(String tunnelId);

    /**
     * 获取所有压力表最近一条采集记录信息并判断是否已经离线
     */
    int getStatusOfAllPressureGauges();

    /**
     * 压力表历史数据统计
     */
    List<Map<String, Object>> getHistoryOfPressureGauges(SdXfwaterRecord sdXfwaterRecord) throws ParseException;
}
