package com.tunnel.business.mapper.event;

import com.tunnel.business.domain.event.SdStrategy;
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
     * @param id 控制策略ID
     * @return 控制策略
     */
    public SdStrategy selectSdStrategyAndSdTriggerById(Long id);
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
    public int updateSdStrategyById(SdStrategy sdStrategy);

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
     * 工作台分时控制抽屉数据
     * @return
     */
    public List<Map> getTimeSharingInfo(String tunnelId);

    /**
     * 获取策略关联的所有定时任务ID
     * @param ids
     * @return
     */
    public Long[] getStrategyRefJobIds(Long[] ids);

    List<Map> workTriggerInfo(String tunnelId);
    /**
     * 批量删除控制策略
     * 
     * @param rlIds 需要删除的数据RLID
     * @return 结果
     */
    /*public int deleteSdStrategyByRlIds(String[] rlIds);*/
    
    /**
     * 修改控制策略
     * 
     * @param sdStrategy 控制策略
     * @return 结果
     */
    /*public int updateSdStrategyInfoByRlId(SdStrategy sdStrategy);*/

    /**
     * 查询控制策略列表(根据rlId)
     *
     * @param sdStrategy 控制策略
     * @return 结果
     */
    /*public List<SdStrategy> selectStrategyList(SdStrategy sdStrategy);*/

    public List<Map<String, Object>> checkStrategyIfExist(Long id);

    public List<Map<String,String>> getManualStrategy();

    /**
     * 查询联控策略
     * @param strategy
     * @return
     */
    List<SdStrategy> getSafetyHandle(SdStrategy strategy);
}