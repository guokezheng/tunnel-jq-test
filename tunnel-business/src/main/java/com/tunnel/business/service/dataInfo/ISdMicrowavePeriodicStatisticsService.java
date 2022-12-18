package com.tunnel.business.service.dataInfo;

import com.tunnel.business.domain.dataInfo.SdMicrowavePeriodicStatistics;

import java.util.List;

/**
 * 车流量信息Service接口
 * 
 * @author ruoyi
 * @date 2022-12-07
 */
public interface ISdMicrowavePeriodicStatisticsService 
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

    /**
     * 修改车流量信息
     * 
     * @param sdMicrowavePeriodicStatistics 车流量信息
     * @return 结果
     */
    public int updateSdMicrowavePeriodicStatistics(SdMicrowavePeriodicStatistics sdMicrowavePeriodicStatistics);

    /**
     * 批量删除车流量信息
     * 
     * @param statisticsIds 需要删除的车流量信息主键集合
     * @return 结果
     */
    public int deleteSdMicrowavePeriodicStatisticsByStatisticsIds(Long[] statisticsIds);

    /**
     * 删除车流量信息信息
     * 
     * @param statisticsId 车流量信息主键
     * @return 结果
     */
    public int deleteSdMicrowavePeriodicStatisticsByStatisticsId(Long statisticsId);
}
