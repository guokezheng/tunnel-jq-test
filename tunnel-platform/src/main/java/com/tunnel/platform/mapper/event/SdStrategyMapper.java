package com.tunnel.platform.mapper.event;

import com.tunnel.platform.domain.event.SdStrategy;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 控制策略Mapper接口
 * 
 * @author gongfanfei
 * @date 2020-08-27
 */
public interface SdStrategyMapper 
{
    /**
     * 查询控制策略
     * 
     * @param id 控制策略ID
     * @return 控制策略
     */
    public SdStrategy selectSdStrategyById(Long id);
    /**
     * 查询控制策略
     * 
     * @param jobRelationId 任务调度控制策略ID
     * @return 控制策略
     */
    public SdStrategy selectSdStrategyByJobRelationId(@Param("jobRelationId") String jobRelationId);

    /**
     * 查询控制策略列表
     * 
     * @param sdStrategy 控制策略
     * @return 控制策略集合
     */
    public List<SdStrategy> selectSdStrategyList(SdStrategy sdStrategy);

    /**
     * 新增控制策略
     * 
     * @param sdStrategy 控制策略
     * @return 结果
     */
    public int insertSdStrategy(SdStrategy sdStrategy);

    /**
     * 修改控制策略
     * 
     * @param sdStrategy 控制策略
     * @return 结果
     */
    public int updateSdStrategy(SdStrategy sdStrategy);

    /**
     * 删除控制策略
     * 
     * @param id 控制策略ID
     * @return 结果
     */
    public int deleteSdStrategyById(Long id);

    /**
     * 批量删除控制策略
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdStrategyByIds(Long[] ids);
    /**
     * 批量删除控制策略
     * 
     * @param rlIds 需要删除的数据RLID
     * @return 结果
     */
    public int deleteSdStrategyByRlIds(String[] rlIds);
    
    /**
     * 修改控制策略
     * 
     * @param sdStrategy 控制策略
     * @return 结果
     */
    public int updateSdStrategyInfoByRlId(SdStrategy sdStrategy);

    public List<SdStrategy> selectStrategyList(SdStrategy sdStrategy);

    public List<Map<String, Object>> checkStrategyIfExist(Long id);
}