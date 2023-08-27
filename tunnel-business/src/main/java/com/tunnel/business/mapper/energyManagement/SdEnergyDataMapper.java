package com.tunnel.business.mapper.energyManagement;


import com.tunnel.business.domain.energyManagement.EnergyDayparting;
import com.tunnel.business.domain.energyManagement.EnergySjfx;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类表Mapper接口
 *
 * @author ruoyi
 * @date 2023-7-14
 */
public interface SdEnergyDataMapper
{
    /**
     * 获取能源站点数据
     * @return
     */
    List<EnergySjfx> getEnergySiteData(@Param("list") List<EnergySjfx> list);

    /**
     * 查询昨天今天实时用电量
     * @param energySjfx 能源分析电力账单实体
     * @return
     */
    List<EnergySjfx> getEnergySiteList(EnergySjfx energySjfx);

    /**
     *批量插入
     * @param list
     * @return
     */
    int insertEnergySiteData(List<EnergySjfx> list);

    /**
     * 删除已经存在的
     * @param energySjfx
     * @return
     */
    int deleteEnergySiteData(EnergySjfx energySjfx);

    /**
     * 获取分项code
     * @return
     */
    List<EnergySjfx> getEnergyItemCodes();

    /**
     * 获取分类code
     * @return
     */
    List<EnergySjfx> getEnergyClassifCodes();

    List<EnergySjfx> getEnergyItemData(@Param("list") List<EnergySjfx>list);

    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertEnergyItemData(List<EnergySjfx> list);

    int deleteEnergyItemData(EnergySjfx energySjfx);

    List<EnergySjfx> getEnergyClassifData(List<EnergySjfx>list);

    int insertEnergyClassifData(List<EnergySjfx> energyClassifList);

    int deleteEnergyClassifData(EnergySjfx energySjfx);

    List<EnergyDayparting> getEnergyDaypartingData(List<EnergyDayparting>list);

    int insertEnergyDaypartingData(List<EnergyDayparting> energyDaypartingList);

    int deleteEnergyDaypartingData(EnergyDayparting energyDayparting);

    List<EnergyDayparting> getEnergyFootprintData(List<EnergyDayparting>list);

    void insertEnergyFootprintData(List<EnergyDayparting> energyFootprintList);

    void deleteEnergyFootprintData(EnergyDayparting energyDayparting);

    void updateEnergySiteData(List<EnergySjfx> list);

    void updateEnergyItemData(List<EnergySjfx> list);

    void updateEnergyClassifData(List<EnergySjfx> list);

    void updateEnergyDayparting(List<EnergyDayparting> list);

    void updateEnergyFootprint(List<EnergyDayparting> list);

    List<EnergySjfx> getEnergycurrentDaySiteData(@Param("currentTime") String currentTime);

    List<EnergySjfx> getEnergycurrentDayItemData(@Param("currentTime")String currentTime);

    List<EnergySjfx> getEnergycurrentMonthSiteData(@Param("currentTime")String currentTime);

    List<EnergySjfx> getEnergycurrentMonthItemData(@Param("currentTime")String currentTime);

    List<EnergySjfx> getEnergycurrentDayClassifData(@Param("currentTime")String currentTime);

    List<EnergySjfx> getEnergycurrentMonthClassifData(@Param("currentTime")String currentTime);

    List<EnergyDayparting> getEnergycurrentDayFootprintData(@Param("currentTime")String currentTime);

    List<EnergyDayparting> getEnergycurrentMonthFootprintData(@Param("currentTime")String currentTime);

    List<EnergyDayparting> getEnergycurrentDayDaypartingData(@Param("currentTime")String currentTime);

    List<EnergyDayparting> getEnergycurrentMonthDaypartingData(@Param("currentTime")String currentTime);

    /**
     * 获取风机或者照明用电量
     * @param energySjfx
     * @return
     */
    EnergySjfx getFengOrZhao(EnergySjfx energySjfx);

    /**
     * 计算本月城市用电总量
     * @param energySjfx
     * @return
     */
    EnergySjfx selectMonthValueSum(EnergySjfx energySjfx);

    /**
     * 计算本月能耗
     * @param energySjfx
     * @return
     */
    EnergySjfx getEnergyMonthSum(EnergySjfx energySjfx);

    /**
     * 计算月分项电量汇总
     * @param energySjfx
     * @return
     */
    EnergySjfx getEnergySubentryMonthSum(EnergySjfx energySjfx);
    /**
     * 计算年总能耗
     * @param energySjfx
     * @return
     */
    EnergySjfx getYearEnergySiteList(EnergySjfx energySjfx);
}
