package com.tunnel.business.mapper.energyManagement;

import com.tunnel.business.domain.energyManagement.EnergyAnalysisElectricityBill;

import java.util.List;

/**
 * 能源分析电力账单Mapper接口
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
public interface SdEnergyAnalysisElectricityBillMapper
{
    /**
     * 查询能源分析电力账单
     * 
     * @param id 能源分析电力账单主键
     * @return 能源分析电力账单
     */
    public EnergyAnalysisElectricityBill selectEnergyAnalysisElectricityBillById(Long id);

    /**
     * 查询能源分析电力账单列表
     * 
     * @param energyAnalysisElectricityBill 能源分析电力账单
     * @return 能源分析电力账单集合
     */
    public List<EnergyAnalysisElectricityBill> selectEnergyAnalysisElectricityBillList(EnergyAnalysisElectricityBill energyAnalysisElectricityBill);

    /**
     * 查询能源分析时段用电量
     *
     * @param energyAnalysisElectricityBill 能源分析电力账单
     * @return 能源分析电力账单集合
     */
    public List<EnergyAnalysisElectricityBill> selectEnergyAnalysisTimeFrame(EnergyAnalysisElectricityBill energyAnalysisElectricityBill);

    /**
     * 新增能源分析电力账单
     * 
     * @param energyAnalysisElectricityBill 能源分析电力账单
     * @return 结果
     */
    public int insertEnergyAnalysisElectricityBill(List<EnergyAnalysisElectricityBill> energyAnalysisElectricityBill);

    /**
     * 修改能源分析电力账单
     * 
     * @param energyAnalysisElectricityBill 能源分析电力账单
     * @return 结果
     */
    public int updateEnergyAnalysisElectricityBill(EnergyAnalysisElectricityBill energyAnalysisElectricityBill);

    /**
     * 删除能源分析电力账单
     * 
     * @param id 能源分析电力账单主键
     * @return 结果
     */
    public int deleteEnergyAnalysisElectricityBillById(Long id);

    /**
     * 批量删除能源分析电力账单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEnergyAnalysisElectricityBillByIds(Long[] ids);
}
