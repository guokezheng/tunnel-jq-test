package com.tunnel.business.service.energyManagement;


import com.tunnel.business.domain.energyManagement.EnergyConfigcenterClassification;
import com.tunnel.business.domain.energyManagement.EnergyConfigcenterItemized;

import java.util.List;

/**
 * 能耗分析Service接口
 *
 * @author ruoyi
 * @date 2022-11-04
 */
public interface ISdEnergyAnalysisElectricityService
{
    /**
     * 获取分项树
     * @param itemized
     * @return
     */
    List<EnergyConfigcenterItemized> selectItemizedList(EnergyConfigcenterItemized itemized);

    /**
     * 获取分类树
     * @param classification
     * @return
     */
    List<EnergyConfigcenterClassification> selectClassificationList(EnergyConfigcenterClassification classification);
}
