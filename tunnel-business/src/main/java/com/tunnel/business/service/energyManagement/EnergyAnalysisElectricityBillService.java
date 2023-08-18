package com.tunnel.business.service.energyManagement;

import com.tunnel.business.domain.energyManagement.EnergyAnalysisElectricityBill;

import java.util.List;

public interface EnergyAnalysisElectricityBillService {

    /**
     * 查询时段用电柱状图
     * @param energyAnalysisElectricityBill 能源分析电力账单实体
     * @return
     */
    public List<EnergyAnalysisElectricityBill> selectEnergyAnalysisElectricityBillList(EnergyAnalysisElectricityBill energyAnalysisElectricityBill);
}
