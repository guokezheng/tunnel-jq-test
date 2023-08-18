package com.tunnel.business.service.energyManagement.impl;

import com.tunnel.business.domain.energyManagement.EnergyAnalysisElectricityBill;
import com.tunnel.business.mapper.energyManagement.SdEnergyAnalysisElectricityBillMapper;
import com.tunnel.business.service.energyManagement.EnergyAnalysisElectricityBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 能源分析电力账单
 * @date: 2023/7/17 15:45
 * @author: hyw
 * @version: 1.0
 */
@Service
public class EnergyAnalysisElectricityBillServiceImpl implements EnergyAnalysisElectricityBillService {

    @Autowired
    private SdEnergyAnalysisElectricityBillMapper energyAnalysisElectricityBillMapper;

    /**
     * 查询时段用电柱状图
     * @param energyAnalysisElectricityBill 能源分析电力账单实体
     * @return
     */
    @Override
    public List<EnergyAnalysisElectricityBill> selectEnergyAnalysisElectricityBillList(EnergyAnalysisElectricityBill energyAnalysisElectricityBill) {
        return energyAnalysisElectricityBillMapper.selectEnergyAnalysisTimeFrame(energyAnalysisElectricityBill);
    }
}
