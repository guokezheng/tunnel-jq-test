package com.tunnel.business.service.dataInfo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.SdMicrowavePeriodicStatistics;
import com.tunnel.business.mapper.dataInfo.SdMicrowavePeriodicStatisticsMapper;
import com.tunnel.business.service.dataInfo.ISdMicrowavePeriodicStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 车流量信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-07
 */
@Service
public class SdMicrowavePeriodicStatisticsServiceImpl implements ISdMicrowavePeriodicStatisticsService
{
    @Autowired
    private SdMicrowavePeriodicStatisticsMapper sdMicrowavePeriodicStatisticsMapper;

    /**
     * 查询车流量信息
     * 
     * @param statisticsId 车流量信息主键
     * @return 车流量信息
     */
    @Override
    public SdMicrowavePeriodicStatistics selectSdMicrowavePeriodicStatisticsByStatisticsId(Long statisticsId)
    {
        return sdMicrowavePeriodicStatisticsMapper.selectSdMicrowavePeriodicStatisticsByStatisticsId(statisticsId);
    }

    /**
     * 查询车流量信息列表
     * 
     * @param sdMicrowavePeriodicStatistics 车流量信息
     * @return 车流量信息
     */
    @Override
    public List<SdMicrowavePeriodicStatistics> selectSdMicrowavePeriodicStatisticsList(SdMicrowavePeriodicStatistics sdMicrowavePeriodicStatistics)
    {
        return sdMicrowavePeriodicStatisticsMapper.selectSdMicrowavePeriodicStatisticsList(sdMicrowavePeriodicStatistics);
    }

    /**
     * 新增车流量信息
     * 
     * @param sdMicrowavePeriodicStatistics 车流量信息
     * @return 结果
     */
    @Override
    public int insertSdMicrowavePeriodicStatistics(SdMicrowavePeriodicStatistics sdMicrowavePeriodicStatistics)
    {
        sdMicrowavePeriodicStatistics.setCreateTime(DateUtils.getNowDate());
        return sdMicrowavePeriodicStatisticsMapper.insertSdMicrowavePeriodicStatistics(sdMicrowavePeriodicStatistics);
    }

    /**
     * 修改车流量信息
     * 
     * @param sdMicrowavePeriodicStatistics 车流量信息
     * @return 结果
     */
    @Override
    public int updateSdMicrowavePeriodicStatistics(SdMicrowavePeriodicStatistics sdMicrowavePeriodicStatistics)
    {
        return sdMicrowavePeriodicStatisticsMapper.updateSdMicrowavePeriodicStatistics(sdMicrowavePeriodicStatistics);
    }

    /**
     * 批量删除车流量信息
     * 
     * @param statisticsIds 需要删除的车流量信息主键
     * @return 结果
     */
    @Override
    public int deleteSdMicrowavePeriodicStatisticsByStatisticsIds(Long[] statisticsIds)
    {
        return sdMicrowavePeriodicStatisticsMapper.deleteSdMicrowavePeriodicStatisticsByStatisticsIds(statisticsIds);
    }

    /**
     * 删除车流量信息信息
     * 
     * @param statisticsId 车流量信息主键
     * @return 结果
     */
    @Override
    public int deleteSdMicrowavePeriodicStatisticsByStatisticsId(Long statisticsId)
    {
        return sdMicrowavePeriodicStatisticsMapper.deleteSdMicrowavePeriodicStatisticsByStatisticsId(statisticsId);
    }

    @Override
    public List<SdMicrowavePeriodicStatistics> getStatisticsNewList(SdMicrowavePeriodicStatistics statistics) {
        List<SdMicrowavePeriodicStatistics> list = new ArrayList<>();
        //一车道
        statistics.setLaneNo(1L);
        SdMicrowavePeriodicStatistics laneNoOne = sdMicrowavePeriodicStatisticsMapper.getStatisticsNewList(statistics);
        //二车道
        statistics.setLaneNo(2L);
        SdMicrowavePeriodicStatistics laneNoTwo = sdMicrowavePeriodicStatisticsMapper.getStatisticsNewList(statistics);
        //三车道
        statistics.setLaneNo(3L);
        SdMicrowavePeriodicStatistics laneNoThree = sdMicrowavePeriodicStatisticsMapper.getStatisticsNewList(statistics);
        list.add(laneNoOne);
        list.add(laneNoTwo);
        list.add(laneNoThree);
        return list;
    }

    @Override
    public Map<String, List<Map<String, String>>> getStatisticsRealList(SdMicrowavePeriodicStatistics statistics) {
        Map<String, List<Map<String, String>>> map = new HashMap<>();
        //一车道
        statistics.setLaneNo(1L);
        List<Map<String, String>> laneNoOne = sdMicrowavePeriodicStatisticsMapper.getStatisticsRealList(statistics);
        //二车道
        statistics.setLaneNo(2L);
        List<Map<String, String>> laneNoTwo = sdMicrowavePeriodicStatisticsMapper.getStatisticsRealList(statistics);
        //三车道
        statistics.setLaneNo(3L);
        List<Map<String, String>> laneNoThree = sdMicrowavePeriodicStatisticsMapper.getStatisticsRealList(statistics);
        map.put("laneNoOne",laneNoOne);
        map.put("laneNoTwo",laneNoTwo);
        map.put("laneNoThree",laneNoThree);
        return map;
    }


}
