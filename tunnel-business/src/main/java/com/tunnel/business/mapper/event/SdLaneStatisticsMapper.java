package com.tunnel.business.mapper.event;

import com.tunnel.business.domain.event.SdLaneStatistics;

import java.util.List;

/**
 * 车道统计数据Mapper接口
 * 
 * @author ruoyi
 * @date 2023-01-13
 */
public interface SdLaneStatisticsMapper 
{
    /**
     * 查询车道统计数据
     * 
     * @param id 车道统计数据主键
     * @return 车道统计数据
     */
    public SdLaneStatistics selectSdLaneStatisticsById(Long id);

    /**
     * 查询车道统计数据列表
     * 
     * @param sdLaneStatistics 车道统计数据
     * @return 车道统计数据集合
     */
    public List<SdLaneStatistics> selectSdLaneStatisticsList(SdLaneStatistics sdLaneStatistics);

    /**
     * 新增车道统计数据
     * 
     * @param sdLaneStatistics 车道统计数据
     * @return 结果
     */
    public int insertSdLaneStatistics(SdLaneStatistics sdLaneStatistics);

    /**
     * 修改车道统计数据
     * 
     * @param sdLaneStatistics 车道统计数据
     * @return 结果
     */
    public int updateSdLaneStatistics(SdLaneStatistics sdLaneStatistics);

    /**
     * 删除车道统计数据
     * 
     * @param id 车道统计数据主键
     * @return 结果
     */
    public int deleteSdLaneStatisticsById(Long id);

    /**
     * 批量删除车道统计数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdLaneStatisticsByIds(Long[] ids);
}
