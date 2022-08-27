package com.tunnel.platform.service.event;

import com.tunnel.platform.domain.event.SdReservePlan;
import com.tunnel.platform.domain.event.SdStrategy;
import com.tunnel.platform.domain.event.SdWarningInfo;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 预警信息Service接口
 *
 * @author gongfanfei
 * @date 2020-08-21sd_sensor_message
 */
public interface ISdWarningInfoService
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
     * 批量删除预警信息
     *
     * @param ids 需要删除的预警信息ID
     * @return 结果
     */
    public int deleteSdWarningInfoByIds(Long[] ids);

    /**
     * 删除预警信息信息
     *
     * @param id 预警信息ID
     * @return 结果
     */
    public int deleteSdWarningInfoById(Long id);
    /**
     * 查询相关预案信息
     * @param id 预警类型ID
     * @return 预警信息
     */
    public List<SdReservePlan> seePlanListByWarningTypeId(Long warningTypeId);
    /**
     * 查询相关策略信息
     * @param id 预警类型ID
     * @return 预警信息
     */
    public List<SdStrategy> seeStrategyListById(Long warningTypeId);
    public Map<String, Object> selectSdWarningInfoCount(SdWarningInfo sdWarningInfo);
	/**
	 * 查询最近一周的报警数量
	 * @param sdWarningInfo
	 * @return
	 */
	public List<Map<String, String>> selectSdWarningInfoEcharts(SdWarningInfo sdWarningInfo);

	public List<SdWarningInfo> selectSdWarningList(SdWarningInfo sdWarningInfo);

    public List<SdWarningInfo> getAllPressureGaugesWarningMsg();

    public List<Map<String, Object>> getWarningDataAnalysis(SdWarningInfo sdWarningInfo) throws ParseException;

    public List<Map<String, Object>> getTheWarningDataOfToday(SdWarningInfo sdWarningInfo);

    public List<String> getSystemWarningMsg();

    public List<String> getTrafficIncident();

    public Map<String, Object> getWarningInfoCount();

    public Map<String, Object> getCarPosition(String tunnelId);

    public Map<String, Object> getWarningMessageCountOfToday(String tunnelId);

    public List<SdWarningInfo> getWarningMassageOfUnProcess(String tunnelId);

}
