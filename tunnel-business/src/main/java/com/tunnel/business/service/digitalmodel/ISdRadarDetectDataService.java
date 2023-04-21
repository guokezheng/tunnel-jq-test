package com.tunnel.business.service.digitalmodel;


import com.tunnel.business.domain.digitalmodel.SdRadarDetectDatas;
import com.tunnel.business.domain.event.SdRadarDetectData;

import java.util.List;
import java.util.Map;

/**
 * 雷达监测感知数据Service接口
 *
 * @author ruoyi
 * @date 2022-09-04
 */
public interface ISdRadarDetectDataService {
    /**
     * 查询雷达监测感知数据
     *
     * @param id 雷达监测感知数据主键
     * @return 雷达监测感知数据
     */
    SdRadarDetectData selectSdRadarDetectDataById(String id);

    /**
     * 查询雷达监测感知数据列表
     *
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 雷达监测感知数据集合
     */
    List<Map<String,String>> selectSdRadarDetectDataList(SdRadarDetectData sdRadarDetectData);

    /**
     * 新增雷达监测感知数据
     *
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 结果
     */
    int insertSdRadarDetectData(SdRadarDetectData sdRadarDetectData);

    /**
     * 修改雷达监测感知数据
     *
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 结果
     */
    int updateSdRadarDetectData(SdRadarDetectData sdRadarDetectData);

    /**
     * 批量删除雷达监测感知数据
     *
     * @param ids 需要删除的雷达监测感知数据主键集合
     * @return 结果
     */
    int deleteSdRadarDetectDataByIds(String[] ids);

    /**
     * 删除雷达监测感知数据信息
     *
     * @param id 雷达监测感知数据主键
     * @return 结果
     */
    int deleteSdRadarDetectDataById(String id);

    /**
     * 根据隧道id 查询24小时 感知数据
     *
     * @param tunnelId
     * @return
     */
    Object[] eventById(String tunnelId);

    List<Map<String, Object>> vehicleMonitoringInRecent24Hours(String tunnelId);

    /**
     * 统计当天24小时的重点车辆
     * @param tunnelId
     * @return
     */
    List<Map<String, Object>> specialVehicleMonitoringInRecent24Hours(String tunnelId);
}
