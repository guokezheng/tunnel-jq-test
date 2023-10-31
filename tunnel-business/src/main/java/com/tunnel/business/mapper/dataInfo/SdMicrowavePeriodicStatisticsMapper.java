package com.tunnel.business.mapper.dataInfo;

import com.tunnel.business.domain.dataInfo.SdMicrowavePeriodicStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 车流量信息Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-07
 */
public interface SdMicrowavePeriodicStatisticsMapper 
{
    /**
     * 查询车流量信息
     * 
     * @param statisticsId 车流量信息主键
     * @return 车流量信息
     */
    public SdMicrowavePeriodicStatistics selectSdMicrowavePeriodicStatisticsByStatisticsId(Long statisticsId);

    /**
     * 查询车流量信息列表
     * 
     * @param sdMicrowavePeriodicStatistics 车流量信息
     * @return 车流量信息集合
     */
    public List<SdMicrowavePeriodicStatistics> selectSdMicrowavePeriodicStatisticsList(SdMicrowavePeriodicStatistics sdMicrowavePeriodicStatistics);

    /**
     * 新增车流量信息
     * 
     * @param sdMicrowavePeriodicStatistics 车流量信息
     * @return 结果
     */
    public int insertSdMicrowavePeriodicStatistics(SdMicrowavePeriodicStatistics sdMicrowavePeriodicStatistics);

    public List<Map<String, String>>  selectCatHistory(@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("deviceId") String deviceId);

    public Map<String, String>  selectCarNumber(@Param("deviceId") String deviceId);

    /**
     * 修改车流量信息
     * 
     * @param sdMicrowavePeriodicStatistics 车流量信息
     * @return 结果
     */
    public int updateSdMicrowavePeriodicStatistics(SdMicrowavePeriodicStatistics sdMicrowavePeriodicStatistics);

    /**
     * 删除车流量信息
     * 
     * @param statisticsId 车流量信息主键
     * @return 结果
     */
    public int deleteSdMicrowavePeriodicStatisticsByStatisticsId(Long statisticsId);

    /**
     * 批量删除车流量信息
     * 
     * @param statisticsIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdMicrowavePeriodicStatisticsByStatisticsIds(Long[] statisticsIds);

    /**
     * 查询车流量信息
     * @param statistics
     * @return
     */
    SdMicrowavePeriodicStatistics getStatisticsNewList(SdMicrowavePeriodicStatistics statistics);

    /**
     * 查询24小时之内车流量信息
     * @param statistics
     * @return
     */
    List<Map<String, Integer>> getStatisticsRealList(SdMicrowavePeriodicStatistics statistics);
}
