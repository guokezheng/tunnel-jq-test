package com.tunnel.business.mapper.energyManagement;


import com.tunnel.business.domain.energyManagement.EnergyConfigcenterItemized;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 分类表Mapper接口
 *
 * @author ruoyi
 * @date 2023-7-14
 */
public interface SdEnergyConfigcenterItemizedMapper
{
    int deleteEnergyItemData(String itemizedCode);

    int insertEnergyItemData(List<EnergyConfigcenterItemized> list);

    List<EnergyConfigcenterItemized> getEnergyItemData(@Param("codeList") List<String> codeList);

    /**
     * 获取分项数据
     * @param itemized
     * @return
     */
    List<EnergyConfigcenterItemized> selectItemizedList(EnergyConfigcenterItemized itemized);

    void updateEnergyItemizedData(@Param("list")List<EnergyConfigcenterItemized> list);

    /**
     * 查询分项数据
     * @param codeList
     * @return
     */
    List<Map<String, Object>> getItemizedMap(@Param("codeList") List<String> codeList);
}
