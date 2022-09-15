package com.tunnel.platform.service.event;

import com.tunnel.platform.domain.event.SdStrategyRl;

import java.util.List;

/**
 * 策略关联设备信息Service接口
 * 
 * @author gongfanfei
 * @date 2020-08-31
 */
public interface ISdStrategyRlService 
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
     * 根据策略id查询策略关联设备信息
     * @param strategyId
     * @return
     */
    List<SdStrategyRl> selectSdStrategyRlListByStrategyId(Long strategyId);

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
     * 批量删除策略关联设备信息
     * 
     * @param ids 需要删除的策略关联设备信息ID
     * @return 结果
     */
    public int deleteSdStrategyRlByIds(Long[] ids);

    /**
     * 删除策略关联设备信息
     * 
     * @param id 策略关联设备信息ID
     * @return 结果
     */
    public int deleteSdStrategyRlById(Long id);
    /**
     * 批量添加策略关联设备信息
     * 
     * @param list
     * @return
     */
    public int batchInsertStrategyRl(List<SdStrategyRl> list);
}