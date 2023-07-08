package com.tunnel.business.service.enhancedLighting;

import java.util.List;
import com.tunnel.business.domain.enhancedLighting.SdEnhancedLightingConfig;

/**
 *
 * @author ruoyi
 * @date 2023-03-24
 */
public interface ISdEnhancedLightingConfigService 
{
    /**
     * 查询【加强照明配置】
     * 
     * @param id 【加强照明配置】主键
     * @return 【加强照明配置】
     */
    public SdEnhancedLightingConfig selectSdEnhancedLightingConfigById(Long id);

    /**
     * 查询【加强照明配置】列表
     * 
     * @param sdEnhancedLightingConfig 【加强照明配置】
     * @return 【加强照明配置】集合
     */
    public List<SdEnhancedLightingConfig> selectSdEnhancedLightingConfigList(SdEnhancedLightingConfig sdEnhancedLightingConfig);

    /**
     * 查询【加强照明配置】列表
     *
     * @param sdEnhancedLightingConfig 【加强照明配置】
     * @return 【加强照明配置】集合
     */
    public SdEnhancedLightingConfig selectSdEnhancedLightingConfigListByParam(SdEnhancedLightingConfig sdEnhancedLightingConfig);

    /**
     * 新增【加强照明配置】
     * 
     * @param sdEnhancedLightingConfig 【加强照明配置】
     * @return 结果
     */
    public int insertSdEnhancedLightingConfig(SdEnhancedLightingConfig sdEnhancedLightingConfig);

    /**
     * 修改【加强照明配置】
     * 
     * @param sdEnhancedLightingConfig 【加强照明配置】
     * @return 结果
     */
    public int updateSdEnhancedLightingConfig(SdEnhancedLightingConfig sdEnhancedLightingConfig);

    /**
     * 批量删除【加强照明配置】
     * 
     * @param ids 需要删除的【加强照明配置】主键集合
     * @return 结果
     */
    public int deleteSdEnhancedLightingConfigByIds(Long[] ids);

    /**
     * 删除【加强照明配置】信息
     * 
     * @param id 【加强照明配置】主键
     * @return 结果
     */
    public int deleteSdEnhancedLightingConfigById(Long id);

    /**
     * 根据车流量  以及 调光区间  计算 亮度
     * @param nowTrafficFlow
     * @param maxTrafficFlow
     * @param maxLuminanceRange
     * @param minLuminanceRange
     * @param luminanceRange
     * @return
     */
    public int getLuminanceByParam(Integer nowTrafficFlow, Integer maxTrafficFlow, Integer maxLuminanceRange,Integer minLuminanceRange,Integer luminanceRange);
}
