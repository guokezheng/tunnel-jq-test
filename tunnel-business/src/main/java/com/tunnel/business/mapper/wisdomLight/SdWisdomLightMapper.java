package com.tunnel.business.mapper.wisdomLight;

import com.tunnel.business.domain.enhancedLighting.SdEnhancedLightingConfig;
import com.tunnel.business.domain.wisdomLight.SdWisdomLight;

import java.util.List;

/**
 *智慧调光
 * @author ruoyi
 * @date 2023-08-10
 */
public interface SdWisdomLightMapper
{

    /**
     * 查询【智慧调光】列表
     *
     * @param sdWisdomLight 【智慧调光】
     * @return 【智慧调光】集合
     */
    public List<SdWisdomLight> selectSdWisdomLightList(SdWisdomLight sdWisdomLight);

    /**
     * 新增【智慧调光】
     * 
     * @param sdWisdomLight 【智慧调光】
     * @return 结果
     */
    public int insertSdWisdomLight(SdWisdomLight sdWisdomLight);

    /**
     * 修改【智慧调光】
     * 
     * @param sdWisdomLight 【智慧调光】
     * @return 结果
     */
    public int updateSdWisdomLight(SdWisdomLight sdWisdomLight);

    /**
     * 修改【智慧调光】
     *
     * @param ids 【智慧调光】
     * @return 结果
     */
    public int deleteSdWisdomLight(Long[] ids);


    /**
     * 更改数据状态【智慧调光】
     *
     * @param sdWisdomLight 【智慧调光】
     * @return 结果
     */
    public int updateSdWisdomIsStatus(SdWisdomLight sdWisdomLight);
}
