package com.tunnel.platform.mapper.event;

import com.tunnel.platform.domain.event.SdStrategyRl;

import java.util.List;

/**
 * 策略关联设备信息Mapper接口
 * 
 * @author gongfanfei
 * @date 2020-08-31
 */
public interface SdStrategyRlMapper 
{
    /**
     * 查询策略关联设备信息
     * 
     * @param id 策略关联设备信息ID
     * @return 策略关联设备信息
     */
    public SdStrategyRl selectSdStrategyRlById(Long id);

    /**
     * 查询策略关联设备信息列表
     * 
     * @param sdStrategyRl 策略关联设备信息
     * @return 策略关联设备信息集合
     */
    public List<SdStrategyRl> selectSdStrategyRlList(SdStrategyRl sdStrategyRl);

    /**
     * 根据策略Id查询策略管关联信息
     * @param strategyId
     * @return
     */
    public List<SdStrategyRl> selectSdStrategyRlByStrategyId(Long strategyId);

    /**
     * 新增策略关联设备信息
     * 
     * @param sdStrategyRl 策略关联设备信息
     * @return 结果
     */
    public int insertSdStrategyRl(SdStrategyRl sdStrategyRl);

    /**
     * 修改策略关联设备信息
     * 
     * @param sdStrategyRl 策略关联设备信息
     * @return 结果
     */
    public int updateSdStrategyRl(SdStrategyRl sdStrategyRl);

    /**
     * 删除策略关联设备信息
     * 
     * @param id 策略关联设备信息ID
     * @return 结果
     */
    public int deleteSdStrategyRlById(Long id);

    /**
     * 批量删除策略关联设备信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdStrategyRlByIds(Long[] ids);
    /**
     * 删除策略关联设备信息
     * 
     * @param strategyId 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdStrategyRlByStrategyId(Long strategyId);
    /**
     * 批量删除策略关联设备信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdStrategyRlByStrategyIds(Long[] ids);
    
    /**
     * 批量添加策略关联设备信息
     * 
     * @param list
     * @return
     */
    public int batchInsertStrategyRl(List<SdStrategyRl> list);
    
    
}