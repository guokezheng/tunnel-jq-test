package com.tunnel.business.mapper.energyManagement;


import com.tunnel.business.domain.energyManagement.EnergyConfigcenterClassification;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 分类表Mapper接口
 *
 * @author ruoyi
 * @date 2023-7-14
 */
public interface SdEnergyConfigcenterClassificationMapper
{

    int insertEnergyTypeData(List<EnergyConfigcenterClassification> list);

    /**
     * 判断是否存在
     * @param
     * @return
     */
    List<EnergyConfigcenterClassification> getEnergyTypeData(@Param("codeList") List<String>codeList);

    int deleteEnergyTypeData(String classificationCode);

    /**
     * 获取分类树
     * @param classification
     * @return
     */
    List<EnergyConfigcenterClassification> selectClassificationList(EnergyConfigcenterClassification classification);

    /**
     * 更新energy_configcenter_classification
     * @param list
     * @return
     */
    int updateEnergyTypeData(List<EnergyConfigcenterClassification> list);

    /**
     * 查询分类数据
     * @param codeList
     * @return
     */
    List<Map<String, Object>> getFicationMap(@Param("codeList") List<String>codeList);
}
