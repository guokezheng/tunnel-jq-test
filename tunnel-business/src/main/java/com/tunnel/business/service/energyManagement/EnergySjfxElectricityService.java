package com.tunnel.business.service.energyManagement;



import com.tunnel.business.domain.energyManagement.ElectricityData;
import com.tunnel.business.domain.energyManagement.EnergyDayparting;
import com.tunnel.business.domain.energyManagement.EnergySjfx;
import com.tunnel.business.domain.energyManagement.SplitTimeDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * EnergySjfxElectricity
 *
 * @date: 2023/7/17 15:44
 * @author: tjw
 * @version: 1.0
 */

public interface EnergySjfxElectricityService {


    List<List<ElectricityData>> getElectricityReportList(List<String> codeList, Date baseTime, Integer statisticType, String tabType, String deptCode) throws Exception;

    /**
     * 能耗足迹
     * @param deptCodeList
     * @param baseTime
     * @param type
     * @return
     */
    List<EnergyDayparting> getEnergyTrackList(List<String> deptCodeList, Date baseTime, Integer statisticsType);

    List<EnergySjfx> test();

    /**
     * 分时段用能
     * @param deptCodeList
     * @param baseTime
     * @param type
     * @return
     */
    List<SplitTimeDto> getSplitTimeByDept(List<String> deptCodeList, Date baseTime, String type);

    /**
     * 电费分析
     * @param deptCodeList
     * @param baseTime
     * @param type
     * @return
     */
    List<List<Map<String, Object>>> getElectricityBillByDept(List<String> deptCodeList, Date baseTime, String type);
}
