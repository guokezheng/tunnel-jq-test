package com.tunnel.business.mapper.event;

import com.tunnel.business.domain.event.SdRoadSectionStatistics;

import java.util.List;


/**
 * 路段统计数据Mapper接口
 * 
 * @author ruoyi
 * @date 2023-01-13
 */
public interface SdRoadSectionStatisticsMapper 
{
    /**
     * 查询路段统计数据
     * 
     * @param id 路段统计数据主键
     * @return 路段统计数据
     */
    public SdRoadSectionStatistics selectSdRoadSectionStatisticsById(Long id);

    /**
     * 查询路段统计数据列表
     * 
     * @param sdRoadSectionStatistics 路段统计数据
     * @return 路段统计数据集合
     */
    public List<SdRoadSectionStatistics> selectSdRoadSectionStatisticsList(SdRoadSectionStatistics sdRoadSectionStatistics);

    /**
     * 新增路段统计数据
     * 
     * @param sdRoadSectionStatistics 路段统计数据
     * @return 结果
     */
    public int insertSdRoadSectionStatistics(SdRoadSectionStatistics sdRoadSectionStatistics);

    /**
     * 修改路段统计数据
     * 
     * @param sdRoadSectionStatistics 路段统计数据
     * @return 结果
     */
    public int updateSdRoadSectionStatistics(SdRoadSectionStatistics sdRoadSectionStatistics);

    /**
     * 删除路段统计数据
     * 
     * @param id 路段统计数据主键
     * @return 结果
     */
    public int deleteSdRoadSectionStatisticsById(Long id);

    /**
     * 批量删除路段统计数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdRoadSectionStatisticsByIds(Long[] ids);
}
