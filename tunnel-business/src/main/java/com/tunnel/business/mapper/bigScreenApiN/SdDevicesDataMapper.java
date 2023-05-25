package com.tunnel.business.mapper.bigScreenApiN;

import java.util.List;
import java.util.Map;

public interface SdDevicesDataMapper {

    /* 获取设备总数 */
    Integer getIndexDataCount();

    /* 获取设备设备报警总数 */
    Integer getIndexDataWarnCount();

    /* 获取设备设备在修总数 */
    Integer getIndexDataRepairCount();

    /* 获取设备故障预警列表 */
    List<Map> getFaultWarnDataList();

    /* 按照月份获取各个月份设备总数 */
    Integer[] getMonthFaultDataDevices();

    /* 按照月份获取各个月份故障设备总数  */
    Integer[] getMonthFaultDataFaults();

    /* 获取各个设备实时设备正常总数 */
    Integer[] getRealTimeStatDataNormalList();

    /* 获取各个设备实时设备异常总数 */
    Integer[] getRealTimeStatDataErrorList();

    /* 获取各个设备实时设备列表 */
    String[] getRealTimeStatDataEquipments();

    /* 获取故障时间持续时间TOP10时长 */
    Integer[] getFaultTimeTopDataList();

    /* 获取故障时间持续时间TOP10设备列表 */
    String[] getFaultTimeTopDataEqList();

    /* 获取设备占比 */
    List<Map> getEqPercentData();

    /* 获取故障分类统计 30天  */
    List<Map> getFaultCategoryMonthData();

    /* 获取故障分类统计 6个月 */
    List<Map> getFaultCategorySixMonthsData();
}
