package com.tunnel.platform.mapper.event;


import com.tunnel.platform.domain.event.SdStrategyBack;

import java.util.List;


/**
 * 策略还原Mapper接口
 * 
 * @author ruoyi
 * @date 2021-06-08
 */
public interface SdStrategyBackMapper
{
    /**
     * 查询策略还原
     * 
     * @param id 策略还原ID
     * @return 策略还原
     */
    public SdStrategyBack selectSdStrategyBackById(Long id);
    public List<SdStrategyBack> selectSdStrategyBackByWarId(Long warningId);

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
     * 删除策略还原
     * 
     * @param id 策略还原ID
     * @return 结果
     */
    public int deleteSdStrategyBackById(Long id);
    public int deleteSdStrategyBackByWarId(Long warningId);

    /**
     * 批量删除策略还原
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdStrategyBackByIds(Long[] ids);
}
