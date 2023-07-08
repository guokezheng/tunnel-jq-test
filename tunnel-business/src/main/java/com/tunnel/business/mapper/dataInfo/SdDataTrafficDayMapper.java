package com.tunnel.business.mapper.dataInfo;

import com.tunnel.business.domain.dataInfo.SdDataTrafficDay;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 各路段日车流量统计Mapper接口
 *
 * @author ruoyi
 * @date 2022-02-18
 */
public interface SdDataTrafficDayMapper
{
    /**
     * 查询各路段日车流量统计
     *
     * @param id 各路段日车流量统计主键
     * @return 各路段日车流量统计
     */
    public SdDataTrafficDay selectSdDataTrafficDayById(Long id);

    /**
     * 查询各路段日车流量统计列表
     *
     * @param sdDataTrafficDay 各路段日车流量统计
     * @return 各路段日车流量统计集合
     */
    public List<SdDataTrafficDay> selectSdDataTrafficDayList(SdDataTrafficDay sdDataTrafficDay);

    /**
     * 新增各路段日车流量统计
     *
     * @param sdDataTrafficDay 各路段日车流量统计
     * @return 结果
     */
    public int insertSdDataTrafficDay(SdDataTrafficDay sdDataTrafficDay);

    /**
     * 修改各路段日车流量统计
     *
     * @param sdDataTrafficDay 各路段日车流量统计
     * @return 结果
     */
    public int updateSdDataTrafficDay(SdDataTrafficDay sdDataTrafficDay);

    /**
     * 删除各路段日车流量统计
     *
     * @param id 各路段日车流量统计主键
     * @return 结果
     */
    public int deleteSdDataTrafficDayById(Long id);

    /**
     * 批量删除各路段日车流量统计
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdDataTrafficDayByIds(Long[] ids);

    public List<Map<String, Object>> getCarNumberByDay(@Param("tunnelId") String tunnelId, @Param("tunnelName") String tunnelName, @Param("lastDayOfMonth") String lastDayOfMonth, @Param("params") Map<String, Object> params);

    public List<Map<String, Object>> getCarFlowNumberOfTodayGroupByTunnel(@Param("deptId") String deptId);

    public List<Map<String, Object>> getCarTypeOfToday(@Param("tunnelId") String tunnelId);

    public List<Map<String, Object>> selectSdDataTrafficDayCountList(@Param("year") long year, @Param("month") long month);

    //每日定时流量统计
    public Map<String, Object> getVehicleDayListsByParam(Map param);

    //每月定时流量统计
    public Map<String, Object> getVehicleMonthListsByParam(Map param);
}
