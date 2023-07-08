package com.tunnel.business.mapper.event;

import com.tunnel.business.domain.event.SdJoinPlanStrategy;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预案策略情报板信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-04-28
 */
public interface SdJoinPlanStrategyMapper 
{
    /**
     * 查询预案策略情报板信息
     * 
     * @param id 预案策略情报板信息主键
     * @return 预案策略情报板信息
     */
    public SdJoinPlanStrategy selectSdJoinPlanStrategyById(Long id);

    /**
     * 查询预案策略情报板信息列表
     * 
     * @param sdJoinPlanStrategy 预案策略情报板信息
     * @return 预案策略情报板信息集合
     */
    public List<SdJoinPlanStrategy> selectSdJoinPlanStrategyList(SdJoinPlanStrategy sdJoinPlanStrategy);

    /**
     * 新增预案策略情报板信息
     * 
     * @param sdJoinPlanStrategy 预案策略情报板信息
     * @return 结果
     */
    public int insertSdJoinPlanStrategy(SdJoinPlanStrategy sdJoinPlanStrategy);

    /**
     * 修改预案策略情报板信息
     * 
     * @param sdJoinPlanStrategy 预案策略情报板信息
     * @return 结果
     */
    public int updateSdJoinPlanStrategy(SdJoinPlanStrategy sdJoinPlanStrategy);

    /**
     * 删除预案策略情报板信息
     * 
     * @param id 预案策略情报板信息主键
     * @return 结果
     */
    public int deleteSdJoinPlanStrategyById(Long id);

    /**
     * 批量删除预案策略情报板信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdJoinPlanStrategyByIds(Long[] ids);

    /**
     * 查询情报板模板信息
     * @param planStrategy
     * @return
     */
    Map<String, Object> getTemplateContent(SdJoinPlanStrategy planStrategy);

    /**
     * 删除情报版数据
     * @param id
     * @param type
     * @return
     */
    int deletePlanStrategyVms(@Param("id") Long id,
                              @Param("type") String type);
}
