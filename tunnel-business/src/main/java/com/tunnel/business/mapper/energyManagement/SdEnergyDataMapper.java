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
}
