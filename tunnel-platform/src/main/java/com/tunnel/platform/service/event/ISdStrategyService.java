package com.tunnel.platform.service.event;

import com.tunnel.platform.domain.event.SdStrategy;
import com.tunnel.platform.domain.event.SdStrategyModel;

import java.util.List;

/**
 * 控制策略Service接口
 * 
 * @author gongfanfei
 * @date 2020-08-27
 */
public interface ISdStrategyService 
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
     * @param jobRelationId 控制策略ID
     * @return 控制策略
     */
    public SdStrategy selectSdStrategyByJobRelationId(String jobRelationId);
    
    
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
     * 批量删除控制策略
     * 
     * @param ids 需要删除的控制策略ID
     * @return 结果
     */
    public int deleteSdStrategyByIds(Long[] ids);

    /**
     * 删除控制策略信息
     * 
     * @param id 控制策略ID
     * @return 结果
     */
    public int deleteSdStrategyById(Long id);

    /**
     * 批量删除控制策略信息
     * @param ids
     * @return
     */
    public int deleteSdStrategyByStrategyIds(Long[] ids);

    /**
     * 删除控制策略信息
     * @param id
     * @return
     */
    public int deleteSdStrategyByStrategyId(Long id);
    
    /**
     * 新增控制策略
     * ---对应批量新增关联信息子表
     * @param SdStrategyModel 控制策略
     * @return 结果
     */
    public int addStrategysInfo(SdStrategyModel model);
    /**
     * 修改控制策略
     * 
     * @param SdStrategyModel 控制策略
     * @return 结果
     */
    public int updateSdStrategyInfo(SdStrategyModel model);
    /**
     * 手动控制策略
     * 
     * @param id 控制策略
     * @return 结果
     */
    public void handleStrategy(Long id);
    /**
     * 手动控制策略一键处理
     *
     * @param ids 控制策略
     * @return 结果
     */
    public void handleStrategyByIds(Long[] ids,Long warId);
    /**
     * 手动控制策略一键处理  返回原来的状态
     *
     * @param ids 控制策略
     * @return 结果
     */
    public void handleStrategyRollBack(Long warId);
}