package com.tunnel.business.service.road;


import com.tunnel.business.domain.roadState.SdRoadDetectorData;
import com.tunnel.business.domain.roadState.SdRoadDetectorDataDTO;

import java.util.List;
import java.util.Map;

/**
 * 路面监测信息Service接口
 *
 * @author lihaodong
 * @date 2021-11-17
 */
public interface ISdRoadDetectorDataService {
    /**
     * 查询路面监测信息
     *
     * @param id 路面监测信息ID
     * @return 路面监测信息
     */
    SdRoadDetectorData selectSdRoadDetectorDataById(Integer id);

    /**
     * 查询路面监测信息列表
     *
     * @param sdRoadDetectorDataDTO 路面监测信息
     * @return 路面监测信息集合
     */
    List<SdRoadDetectorDataDTO> selectSdRoadDetectorDataList(SdRoadDetectorDataDTO sdRoadDetectorDataDTO);

    /**
     * 新增路面监测信息
     *
     * @param sdRoadDetectorData 路面监测信息
     * @return 结果
     */
    int insertSdRoadDetectorData(SdRoadDetectorData sdRoadDetectorData);

    /**
     * 修改路面监测信息
     *
     * @param sdRoadDetectorData 路面监测信息
     * @return 结果
     */
    int updateSdRoadDetectorData(SdRoadDetectorData sdRoadDetectorData);

    /**
     * 批量删除路面监测信息
     *
     * @param ids 需要删除的路面监测信息ID
     * @return 结果
     */
    int deleteSdRoadDetectorDataByIds(Integer[] ids);

    /**
     * 删除路面监测信息信息
     *
     * @param id 路面监测信息ID
     * @return 结果
     */
    int deleteSdRoadDetectorDataById(Integer id);

    /**
     * 获取路面状态监测图数据
     *
     * @return
     */
    Map<String, Object> statisticsRoadMonitoring();

    /**
     * 获取路面状态监测图数据
     *
     * @return
     */
    Map<String, Object> statisticsWarningTrend();

    /**
     * 搜索路面状态监测图数据
     *
     * @param type
     * @param startTime
     * @param endTime
     * @param eqId
     * @return
     */
    Map<String, Object> statisticsWarningTrendSearch(String type, String startTime, String endTime, String eqId);

    /**
     * 根据隧道和年、月、日查询路面温度、路面温度预警信息、积水厚度和积雪厚度
     */
    List<Map<String, Object>> statisticsSearch(String type, String startTime, String tunnelId);

    /**
     * 根据隧道和年、月、日查询预警次数
     */
    List<Map<String, Object>> statisticsSearchWarningInfo(String type, String startTime, String tunnelId);

    /**
     * 根据隧道和年、月、日查询路面状态
     */
    List<Map<String, Object>> statisticsSearchPavementstatus(String type, String startTime, String tunnelId);

    /**
     * 查询本年度隧道路面最高温度
     */
    Map<String, Object> statisticsSearcMaxPavementtemp(String startTime);

    /**
     * 查询本年度最大预警次数
     */
    Map<String, Object> statisticsSearcMaxpWarningInfo(String startTime);

    /**
     * 解析路面通讯协议数据返回命令
     *
     * @param firstContent 返回命令
     * @param eqNumber     设备编号
     */
    void dataAnalySis(String firstContent, String eqNumber);

    /**
     * 根据设备编号查询采集的最新数据
     *
     * @param eqNumber
     * @return
     */
    Map<String, Object> selectByEqDeno(String eqNumber);
}
