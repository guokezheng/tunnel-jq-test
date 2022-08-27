package com.tunnel.platform.mapper.roadState;

import com.tunnel.platform.domain.roadState.SdRoadDetectorData;
import com.tunnel.platform.domain.roadState.SdRoadDetectorDataDTO;
import com.tunnel.platform.domain.roadState.SdRoadDetectorData;
import com.tunnel.platform.domain.roadState.SdRoadDetectorDataDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 路面监测信息Mapper接口
 *
 * @author lihaodong
 * @date 2021-11-17
 */
public interface SdRoadDetectorDataMapper
{
    /**
     * 查询路面监测信息
     *
     * @param id 路面监测信息ID
     * @return 路面监测信息
     */
    public SdRoadDetectorData selectSdRoadDetectorDataById(Integer id);

    /**
     * 查询路面监测信息列表
     *
     * @param sdRoadDetectorData 路面监测信息
     * @return 路面监测信息集合
     */
    public List<SdRoadDetectorDataDTO> selectSdRoadDetectorDataList(SdRoadDetectorData sdRoadDetectorData);

    /**
     * 新增路面监测信息
     *
     * @param sdRoadDetectorData 路面监测信息
     * @return 结果
     */
    public int insertSdRoadDetectorData(SdRoadDetectorData sdRoadDetectorData);

    /**
     * 修改路面监测信息
     *
     * @param sdRoadDetectorData 路面监测信息
     * @return 结果
     */
    public int updateSdRoadDetectorData(SdRoadDetectorData sdRoadDetectorData);

    /**
     * 删除路面监测信息
     *
     * @param id 路面监测信息ID
     * @return 结果
     */
    public int deleteSdRoadDetectorDataById(Integer id);

    /**
     * 批量删除路面监测信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdRoadDetectorDataByIds(Integer[] ids);

    /**
     * 获取路面状态监测图数据
     *
     * @return
     */
    public Map<String, Object> statisticsRoadMonitoring();

    /**
     * 获取设备预警趋势分析
     *
     * @return
     */
    public Map<String, Object> statisticsYearSdDevices(@Param("year") String year, @Param("devno") String devno);

    /**
     * 按月搜索设备预警趋势分析
     *
     * @param startTime
     * @param endTime
     * @param eqId
     * @return
     */
    public Map<String, Object> statisticsWarningTrendSearchMonth(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("eqId") String eqId);


    /**
     * 按日搜索设备预警趋势分析
     *
     * @param startTime
     * @param endTime
     * @param eqId
     * @return
     */
    public Map<String, Object> statisticsWarningTrendSearchDay(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("eqId") String eqId);


    public List<Map<String, Object>> statisticsSearchDay(@Param("tunnelId") String tunnelId, @Param("startTime") String startTime);

    public List<Map<String, Object>> statisticsSearchMon(@Param("tunnelId") String tunnelId, @Param("startTime") String startTime);

    public List<Map<String, Object>> statisticsSearchYear(@Param("tunnelId") String tunnelId, @Param("startTime") String startTime);

    public List<Map<String, Object>> statisticsSearchPavementstatusDay(@Param("tunnelId") String tunnelId, @Param("startTime") String startTime);

    public List<Map<String, Object>>  statisticsSearchPavementstatusMonth(@Param("tunnelId") String tunnelId, @Param("startTime") String startTime);

    public List<Map<String, Object>> statisticsSearchPavementstatusYear(@Param("tunnelId") String tunnelId, @Param("startTime") String startTime);

    public Map<String, Object> statisticsSearcMaxPavementtemp(@Param("startTime") String startTime);

    public Map<String, Object> selectByEqDeno(@Param("eqNumber") String eqNumber);

}
