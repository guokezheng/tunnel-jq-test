package com.tunnel.business.service.enhancedLighting.impl;


import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.domain.enhancedLighting.SdEnhancedLightingConfig;
import com.tunnel.business.mapper.enhancedLighting.SdEnhancedLightingConfigMapper;
import com.tunnel.business.mapper.vehicle.SdVehicleDataMapper;
import com.tunnel.business.service.enhancedLighting.ISdEnhancedLightingConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ruoyi
 * @date 2023-03-24
 */
@Service
public class SdEnhancedLightingConfigServiceImpl implements ISdEnhancedLightingConfigService
{

    @Autowired
    private SdEnhancedLightingConfigMapper sdEnhancedLightingConfigMapper;


    /**
     * Redis缓存工具类
     * */
    private RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    /**
     * 查询【加强照明配置】
     *
     * @param id 【加强照明配置】主键
     * @return 【加强照明配置】
     */
    @Override
    public SdEnhancedLightingConfig selectSdEnhancedLightingConfigById(Long id)
    {
        return sdEnhancedLightingConfigMapper.selectSdEnhancedLightingConfigById(id);
    }

    /**
     * 查询【加强照明配置】列表
     *
     * @param sdEnhancedLightingConfig 【加强照明配置】
     * @return 【加强照明配置】
     */
    @Override
    public List<SdEnhancedLightingConfig> selectSdEnhancedLightingConfigList(SdEnhancedLightingConfig sdEnhancedLightingConfig)
    {
        return sdEnhancedLightingConfigMapper.selectSdEnhancedLightingConfigList(sdEnhancedLightingConfig);
    }

    @Override
    public SdEnhancedLightingConfig selectSdEnhancedLightingConfigListByParam(SdEnhancedLightingConfig sdEnhancedLightingConfig) {
        return sdEnhancedLightingConfigMapper.selectSdEnhancedLightingConfigListByParam(sdEnhancedLightingConfig);
    }

    /**
     * 新增【加强照明配置】
     *
     * @param sdEnhancedLightingConfig 【加强照明配置】
     * @return 结果
     */
    @Override
    public SdEnhancedLightingConfig insertSdEnhancedLightingConfig(SdEnhancedLightingConfig sdEnhancedLightingConfig)
    {
        sdEnhancedLightingConfigMapper.insertSdEnhancedLightingConfig(sdEnhancedLightingConfig);
        return sdEnhancedLightingConfig;
    }

    /**
     * 修改【加强照明配置】
     *
     * @param sdEnhancedLightingConfig 【加强照明配置】
     * @return 结果
     */
    @Override
    public int updateSdEnhancedLightingConfig(SdEnhancedLightingConfig sdEnhancedLightingConfig)
    {
        return sdEnhancedLightingConfigMapper.updateSdEnhancedLightingConfig(sdEnhancedLightingConfig);
    }

    /**
     * 批量删除【加强照明配置】
     *
     * @param ids 需要删除的【加强照明配置】主键
     * @return 结果
     */
    @Override
    public int deleteSdEnhancedLightingConfigByIds(Long[] ids)
    {
        return sdEnhancedLightingConfigMapper.deleteSdEnhancedLightingConfigByIds(ids);
    }

    /**
     * 删除【加强照明配置】信息
     *
     * @param id 【加强照明配置】主键
     * @return 结果
     */
    @Override
    public int deleteSdEnhancedLightingConfigById(Long id)
    {
        return sdEnhancedLightingConfigMapper.deleteSdEnhancedLightingConfigById(id);
    }




    /**
     *
     * @param nowTrafficFlow    当前车流量
     * @param maxTrafficFlow     最大车流量
     * @param maxLuminanceRange     最大调光区间值
     * @param minLuminanceRange     最小调光区间值
     * @param luminanceRange        当前时间段调光值
     * @return
     */
    @Override
    public int getLuminanceByParam(Integer nowTrafficFlow, Integer maxTrafficFlow, Integer maxLuminanceRange, Integer minLuminanceRange, Integer luminanceRange) {
        if(nowTrafficFlow >= maxTrafficFlow ){
            //当前车流量大于现在车流量
            return luminanceRange+maxLuminanceRange;
        }else{
            Integer regionLuminanceRange = maxLuminanceRange - minLuminanceRange;
            //计算公式  (当前车流量/最大车流量)*亮度区间值
            BigDecimal nowTrafficFlowBig = new BigDecimal(nowTrafficFlow);
            BigDecimal maxTrafficFlowBig = new BigDecimal(maxTrafficFlow);
            BigDecimal regionLuminanceRangeBig = new BigDecimal(regionLuminanceRange);
            nowTrafficFlowBig = nowTrafficFlowBig.divide(maxTrafficFlowBig, 2,BigDecimal.ROUND_HALF_UP).multiply(regionLuminanceRangeBig);
            return luminanceRange + (nowTrafficFlowBig.intValue()/5)*5;
        }
    }

}
