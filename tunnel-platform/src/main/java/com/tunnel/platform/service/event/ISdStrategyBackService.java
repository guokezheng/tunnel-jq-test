package com.tunnel.platform.service.event;

import com.tunnel.platform.domain.event.SdStrategyBack;

import java.util.List;


/**
 * 策略还原Service接口
 * 
 * @author ruoyi
 * @date 2021-06-08
 */
public interface ISdStrategyBackService 
{
    /**
     * 查询策略还原
     * 
     * @param id 策略还原ID
     * @return 策略还原
     */
    public SdStrategyBack selectSdStrategyBackById(Long id);

    /**
     * 查询策略还原列表
     * 
     * @param SdStrategyBack 策略还原
     * @return 策略还原集合
     */
    public List<SdStrategyBack> selectSdStrategyBackList(SdStrategyBack SdStrategyBack);

    /**
     * 新增策略还原
     * 
     * @param SdStrategyBack 策略还原
     * @return 结果
     */
    public int insertSdStrategyBack(SdStrategyBack SdStrategyBack);

    /**
     * 修改策略还原
     * 
     * @param SdStrategyBack 策略还原
     * @return 结果
     */
    public int updateSdStrategyBack(SdStrategyBack SdStrategyBack);

    /**
     * 批量删除策略还原
     * 
     * @param ids 需要删除的策略还原ID
     * @return 结果
     */
    public int deleteSdStrategyBackByIds(Long[] ids);

    /**
     * 删除策略还原信息
     * 
     * @param id 策略还原ID
     * @return 结果
     */
    public int deleteSdStrategyBackById(Long id);
}
