package com.tunnel.business.service.energyManagement.impl;

import com.tunnel.business.domain.energyManagement.EnergyConfigcenterClassification;
import com.tunnel.business.domain.energyManagement.EnergyConfigcenterItemized;
import com.tunnel.business.mapper.energyManagement.SdEnergyConfigcenterClassificationMapper;
import com.tunnel.business.mapper.energyManagement.SdEnergyConfigcenterItemizedMapper;
import com.tunnel.business.service.energyManagement.ISdEnergyAnalysisElectricityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 能耗报表Service业务层处理
 *
 * @author ruoyi
 * @date 2023-7-14
 */
@Service
public class SdEnergyAnalysisElectricityServiceImpl implements ISdEnergyAnalysisElectricityService {

    @Autowired
    private SdEnergyConfigcenterItemizedMapper mapper;

    @Autowired
    private SdEnergyConfigcenterClassificationMapper classificMapper;


    /**
     * 获取分项树
     * @param itemized
     * @return
     */
    @Override
    public List<EnergyConfigcenterItemized> selectItemizedList(EnergyConfigcenterItemized itemized) {
        return mapper.selectItemizedList(itemized);
    }

    /**
     * 获取分类树
     * @param classification
     * @return
     */
    @Override
    public List<EnergyConfigcenterClassification> selectClassificationList(EnergyConfigcenterClassification classification) {
        return classificMapper.selectClassificationList(classification);
    }
}