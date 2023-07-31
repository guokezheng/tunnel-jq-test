package com.tunnel.business.mapper.energyManagement;


import com.tunnel.business.domain.energyManagement.EnergyConfigcenterElectricityPrice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类表Mapper接口
 *
 * @author ruoyi
 * @date 2023-7-14
 */
public interface SdEnergyConfigcenterElectricityPriceMapper
{
    int deleteEnergyPriceData(String month);

    int insertEnergyPriceData(List<EnergyConfigcenterElectricityPrice> list);

    List<EnergyConfigcenterElectricityPrice> getEnergyPriceData(@Param("codeList") List<String> codeList);

    void updateEnergyPriceData(@Param("list") List<EnergyConfigcenterElectricityPrice> list);
}
