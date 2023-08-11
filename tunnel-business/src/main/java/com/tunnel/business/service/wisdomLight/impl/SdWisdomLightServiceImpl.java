package com.tunnel.business.service.wisdomLight.impl;


import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.domain.enhancedLighting.SdEnhancedLightingConfig;
import com.tunnel.business.domain.wisdomLight.SdWisdomLight;
import com.tunnel.business.mapper.enhancedLighting.SdEnhancedLightingConfigMapper;
import com.tunnel.business.mapper.wisdomLight.SdWisdomLightMapper;
import com.tunnel.business.service.enhancedLighting.ISdEnhancedLightingConfigService;
import com.tunnel.business.service.wisdomLight.ISdWisdomLightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author ruoyi
 * @date 2023-03-24
 */
@Service
public class SdWisdomLightServiceImpl implements ISdWisdomLightService
{

    @Autowired
    private SdWisdomLightMapper sdWisdomLightMapper;


    /**
     * Redis缓存工具类
     * */
    private RedisCache redisCache = SpringUtils.getBean(RedisCache.class);


    /**
     * 查询【智慧调光】列表
     *
     * @param sdWisdomLight 【智慧调光】
     * @return 【加强照明配置】
     */
    @Override
    public List<SdWisdomLight> selectSdWisdomLightList(SdWisdomLight sdWisdomLight)
    {
        return sdWisdomLightMapper.selectSdWisdomLightList(sdWisdomLight);
    }

    /**
     * 新增【智慧调光】
     *
     * @param sdWisdomLight 【智慧调光】
     * @return 结果
     */
    @Override
    public SdWisdomLight insertSdWisdomLight(SdWisdomLight sdWisdomLight)
    {
        sdWisdomLightMapper.insertSdWisdomLight(sdWisdomLight);
        return sdWisdomLight;
    }

    /**
     * 修改【加强照明配置】
     *
     * @param sdWisdomLight 【加强照明配置】
     * @return 结果
     */
    @Override
    public int updateSdWisdomLight(SdWisdomLight sdWisdomLight)
    {
        return sdWisdomLightMapper.updateSdWisdomLight(sdWisdomLight);
    }

}
