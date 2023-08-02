package com.tunnel.business.mapper.energyManagement;


import com.tunnel.business.domain.energyManagement.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * EnergySjfxElectricityMapper
 *
 * @date: 2023/7/17 15:08
 * @author: tjw
 * @version: 1.0
 */


@Mapper
public interface EnergySjfxElectricityMapper {
    /**
     * 获取站点用电量
     */
    List<ElectricityData> getSiteByDateType(@Param("codeList") List<String> codeList,
                                            @Param("statisticsType") Integer statisticsType,
                                            @Param("statisticsDate") Date statisticsDate
                                            );


    /**
     * 获取分项用能报表
     */
    List<ElectricityData> getItemizedEnergyByDateType(@Param("codeList") List<String> codeList,
                                                      @Param("statisticsType") int statisticsType,
                                                      @Param("statisticsDate") Date statisticsDate,
                                                      @Param("childCodeList") List<String> childCodeList);

    /**
     * 获取分类用能报表
     */
    List<ElectricityData> getElectricityListByClass(@Param("codeList") List<String> codeList,
                                                    @Param("statisticsType") int statisticsType,
                                                    @Param("statisticsDate") Date statisticsDate,
                                                    @Param("childCodeList") List<String> childCodeList);

    /**
     * 能耗足迹
     * @param codeList
     * @param baseTime
     * @param statisticType
     * @return
     */
    List<EnergyDayparting> getEnergyTrackList(@Param("codeList")List<String> codeList,
                                              @Param("baseTime")Date baseTime,
                                              @Param("statisticsType")Integer statisticsType);

    List<EnergySjfx> test();



    List<EnergyDayparting> getEnergyDayparting(@Param("deptCodeList")List<String> deptCodeList,
                                               @Param("baseTime")Date baseTime,
                                               @Param("statisticsType")Integer statisticsType);

    List<EnergyConfigcenterElectricityPrice> getListOfYear(@Param("createTime")Date createTime);



    EnergyConfigcenterElectricityPrice getListOfMonth(Date baseTime);

    EnergyConfigcenterElectricityPrice getInfoByLastMonth();

    /**
     * 电量分析
     * @param deptCodeList
     * @param baseTime
     * @param statisticsType
     * @return
     */
    List<EnergyAnalysisElectricityBill> getElectricityBillByDept(@Param("deptCodeList")List<String> deptCodeList,
                                                  @Param("baseTime")Date baseTime,
                                                  @Param("statisticsType")Integer statisticsType);
}
