package com.tunnel.business.mapper.dataInfo;

import com.tunnel.business.domain.dataInfo.SdTrafficStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 车流量信息Mapper接口
 *
 * @author yanghousheng
 * @date 2020-11-10
 */
public interface SdTrafficStatisticsMapper
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
     * 删除车流量信息
     *
     * @param statisticsId 车流量信息ID
     * @return 结果
     */
    public int deleteSdTrafficStatisticsById(Long statisticsId);

    /**
     * 批量删除车流量信息
     *
     * @param statisticsIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdTrafficStatisticsByIds(Long[] statisticsIds);
    /**
     * 查询时间段的车流量信息
     * @param beginTime
     * @param endTime
     * @return
     */
	public List<SdTrafficStatistics> queryStatistics(@Param("beginTime")String beginTime,@Param("endTime") String endTime);
	/**
	 * 查询当天的车流量信息
	 * @param sd
	 * @return
	 */

	public List<SdTrafficStatistics> selectSdTrafficStatisticsToday(SdTrafficStatistics sd);

	/**
	 * 分析
	 * @param type
	 * @param tunnelId
	 * @param holes
	 * @return
	 */
	public List<SdTrafficStatistics> getAnalysisData(@Param("type")String type,@Param("tunnelId") String tunnelId,@Param("holes") String holes);

	public List<SdTrafficStatistics> analysisDataByTime(SdTrafficStatistics sdTrafficStatistics);

	public List<SdTrafficStatistics> analysisDataByHourTime(@Param("params") Map<String, Object> params,@Param("tunnelId") String tunnelId,@Param("holes") String holes);

	public Map<String, Object> selectSumTrafficFlow();

	public List<SdTrafficStatistics> getDevicesIdInBase();

	public Map<String, Object> selectRecentTrafficFlow(@Param("deviceId") String deviceId);

	/**
	 * 查询某隧道最近的车流量相关数据
	 * @param tunnelId 隧道id
	 * @return
	 */
	List<SdTrafficStatistics> selectLatestTrafficFlowList(String tunnelId);
}
