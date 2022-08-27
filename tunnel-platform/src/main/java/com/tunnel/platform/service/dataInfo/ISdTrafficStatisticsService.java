package com.tunnel.platform.service.dataInfo;

import com.tunnel.platform.domain.dataInfo.SdTrafficStatistics;

import java.util.List;
import java.util.Map;

/**
 * 车流量信息Service接口
 *
 * @author yanghousheng
 * @date 2020-11-10
 */
public interface ISdTrafficStatisticsService
{
    /**
     * 查询车流量信息
     *
     * @param statisticsId 车流量信息ID
     * @return 车流量信息
     */
    public SdTrafficStatistics selectSdTrafficStatisticsById(Long statisticsId);

    /**
     * 查询车流量信息列表
     *
     * @param sdTrafficStatistics 车流量信息
     * @return 车流量信息集合
     */
    public List<SdTrafficStatistics> selectSdTrafficStatisticsList(SdTrafficStatistics sdTrafficStatistics);

    /**
     * 新增车流量信息
     *
     * @param sdTrafficStatistics 车流量信息
     * @return 结果
     */
    public int insertSdTrafficStatistics(SdTrafficStatistics sdTrafficStatistics);

    /**
     * 修改车流量信息
     *
     * @param sdTrafficStatistics 车流量信息
     * @return 结果
     */
    public int updateSdTrafficStatistics(SdTrafficStatistics sdTrafficStatistics);

    /**
     * 批量删除车流量信息
     *
     * @param statisticsIds 需要删除的车流量信息ID
     * @return 结果
     */
    public int deleteSdTrafficStatisticsByIds(Long[] statisticsIds);

    /**
     * 删除车流量信息信息
     *
     * @param statisticsId 车流量信息ID
     * @return 结果
     */
    public int deleteSdTrafficStatisticsById(Long statisticsId);
    /**
     * 查询时间段的车流量信息
     * @param beginTime
     * @param endTime
     * @return
     */
	public Map<String, Object> queryStatistics(String beginTime, String endTime);
	/**
	 * 查询当天的车流量信息
	 * @param sd
	 * @return
	 */
	public List<SdTrafficStatistics> selectSdTrafficStatisticsToday(SdTrafficStatistics sd);

	/**
	 * 分析
	 * @param tunnelId
	 * @param holes
	 * @return
	 */
	public Map<String,List<SdTrafficStatistics>> getAnalysisData(String tunnelId, String holes);

	/**
	 * 单个分析
	 * @param sdTrafficStatistics
	 * @return
	 */
	public List<SdTrafficStatistics> analysisDataByTime(SdTrafficStatistics sdTrafficStatistics);

	public Map<String, Object> getTrafficFlowMsgToApp();

	/**
	 * 查询某隧道最近的车流量相关数据
	 * @param tunnelId 隧道id
	 * @return
	 */
	List<SdTrafficStatistics> selectLatestTrafficFlowList(String tunnelId);
}
