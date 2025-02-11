package com.tunnel.business.service.energyManagement;

import com.tunnel.business.domain.energyManagement.EnergyAnalysisElectricityBill;
import com.tunnel.business.domain.energyManagement.EnergySite;

import java.util.List;
import java.util.Map;

public interface EnergyAnalysisElectricityBillService {

    /**
     * 查询时段用电柱状图
     * @param energyAnalysisElectricityBill 能源分析电力账单实体
     * @return
     */
    public List<EnergyAnalysisElectricityBill> selectEnergyAnalysisElectricityBillList(EnergyAnalysisElectricityBill energyAnalysisElectricityBill);

    /**
     * 计算用能总电量
     * @return
     */
    public Map selectEnergyValueSum(EnergySite energySite);

    /**
     * energySite
     * @return
     */
    public Map getrealTimeData(EnergySite energySite);
}
