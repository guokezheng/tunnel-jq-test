package com.tunnel.business.service.enhancedLighting.impl;


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

    /**
     * 新增【加强照明配置】
     *
     * @param sdEnhancedLightingConfig 【加强照明配置】
     * @return 结果
     */
    @Override
    public int insertSdEnhancedLightingConfig(SdEnhancedLightingConfig sdEnhancedLightingConfig)
    {
        return sdEnhancedLightingConfigMapper.insertSdEnhancedLightingConfig(sdEnhancedLightingConfig);
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


}
