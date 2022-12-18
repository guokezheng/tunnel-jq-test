package com.tunnel.business.mapper.event;

import com.tunnel.business.domain.event.SdWarningInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预警信息Mapper接口
 *
 * @author gongfanfei
 * @date 2020-08-21
 */
public interface SdWarningInfoMapper
{
    /**
     * 查询预警信息
     *
     * @param id 预警信息ID
     * @return 预警信息
     */
    public SdWarningInfo selectSdWarningInfoById(Long id);

    /**
     * 查询预警信息列表
     *
     * @param sdWarningInfo 预警信息
     * @return 预警信息集合
     */
    public List<SdWarningInfo> selectSdWarningInfoList(SdWarningInfo sdWarningInfo);

    /**
     * 新增预警信息
     *
     * @param sdWarningInfo 预警信息
     * @return 结果
     */
    public int insertSdWarningInfo(SdWarningInfo sdWarningInfo);

    /**
     * 修改预警信息
     *
     * @param sdWarningInfo 预警信息
     * @return 结果
     */
    public int updateSdWarningInfo(SdWarningInfo sdWarningInfo);
    public int updateSdWarningInfoByProcessState(SdWarningInfo sdWarningInfo);

    /**
     * 删除预警信息
     *
     * @param id 预警信息ID
     * @return 结果
     */
    public int deleteSdWarningInfoById(Long id);

    /**
     * 批量删除预警信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdWarningInfoByIds(Long[] ids);
    /**
     * 查询当天的报警数量
     * @param sdWarningInfo
     * @return
     */
    public Map<String, Object> selectSdWarningInfoCount(SdWarningInfo sdWarningInfo);
	/**
	 * 查询最近7天的报警数量
	 * @param sdWarningInfo
	 * @return
	 */
	public List<Map<String, String>> selectSdWarningInfoEcharts(SdWarningInfo sdWarningInfo);

	public List<SdWarningInfo> selectSdWarningList(SdWarningInfo sdWarningInfo);

    public List<SdWarningInfo> getAllPressureGaugesWarningMsg(@Param("deptId") String deptId);

    public List<Map<String, Object>> selectWarningInfoMsgByYear(@Param("tunnelId") String tunnelId, @Param("year") String year, @Param("params") Map<String, Object> params);

    public List<Map<String, Object>> selectWarningInfoMsgByMonth(@Param("tunnelId") String tunnelId, @Param("lastDayOfMonth") String lastDayOfMonth,@Param("month") String month, @Param("params") Map<String, Object> params);

    public List<Map<String, Object>> selectWarningInfoMsgByDay(@Param("tunnelId") String tunnelId, @Param("params") Map<String, Object> params);

    public List<Map<String, Object>> getTheWarningDataOfToday(SdWarningInfo sdWarningInfo);

    public List<String> sdWarningInfoMapper(@Param("deptId") String deptId);

    public List<String> getTrafficIncident(@Param("deptId") String deptId);

    public List<Map<String, Object>> statisticsSearchWarningDay(@Param("tunnelId") String tunnelId, @Param("startTime") String startTime);

    public List<Map<String, Object>> statisticsSearchWarningMon(@Param("tunnelId") String tunnelId, @Param("startTime") String startTime);

    public List<Map<String, Object>> statisticsSearcWarningYear(@Param("tunnelId") String tunnelId, @Param("startTime") String startTime);

    public Map<String, Object> statisticsSearcMaxpWarningInfo(@Param("startTime") java.lang.String startTime);

    public Map<String, Object> getWarningInfoCount();

    public Map<String, Object> getWarningMessageCountOfToday(@Param("tunnelId") String tunnelId);

    public Map<String, Object> getUnProcessWarningOfMonth(@Param("tunnelId") String tunnelId);

    public List<SdWarningInfo> getWarningMassageOfUnProcess(SdWarningInfo sdWarningInfo);

}
