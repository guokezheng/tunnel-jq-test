package com.tunnel.platform.service.dataInfo;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.tunnel.platform.domain.dataInfo.SdDataTrafficDay;
import com.tunnel.platform.domain.dataInfo.SdDataTrafficHour;

/**
 * 各路段日车流量统计Service接口
 *
 * @author ruoyi
 * @date 2022-02-18
 */
public interface ISdDataTrafficDayService
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
     * 批量删除各路段日车流量统计
     *
     * @param ids 需要删除的各路段日车流量统计主键集合
     * @return 结果
     */
    public int deleteSdDataTrafficDayByIds(Long[] ids);

    /**
     * 删除各路段日车流量统计信息
     *
     * @param id 各路段日车流量统计主键
     * @return 结果
     */
    public int deleteSdDataTrafficDayById(Long id);

    public List<Map<String, Object>> getCarNumberByDay(SdDataTrafficDay sdDataTrafficDay) throws ParseException;

    public List<Map<String, Object>> getCarFlowNumberOfTodayGroupByTunnel();

    public List<Map<String, Object>> getCarTypeOfToday(SdDataTrafficDay sdDataTrafficDay);

    public List<Map<String, Object>> summarizeTrafficFlowData(List<Map<String, Object>> sdDataTrafficHours);
}
