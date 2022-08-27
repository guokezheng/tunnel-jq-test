package com.tunnel.platform.mapper.dataInfo;

import java.util.List;
import java.util.Map;

import com.tunnel.platform.domain.dataInfo.SdDataTrafficHour;
import org.apache.ibatis.annotations.Param;

/**
 * 各路段小时车流量统计Mapper接口
 *
 * @author ruoyi
 * @date 2022-02-18
 */
public interface SdDataTrafficHourMapper
{
    /**
     * 查询各路段小时车流量统计
     *
     * @param id 各路段小时车流量统计主键
     * @return 各路段小时车流量统计
     */
    public SdDataTrafficHour selectSdDataTrafficHourById(Long id);

    /**
     * 查询各路段小时车流量统计列表
     *
     * @param sdDataTrafficHour 各路段小时车流量统计
     * @return 各路段小时车流量统计集合
     */
    public List<SdDataTrafficHour> selectSdDataTrafficHourList(SdDataTrafficHour sdDataTrafficHour);

    /**
     * 新增各路段小时车流量统计
     *
     * @param sdDataTrafficHour 各路段小时车流量统计
     * @return 结果
     */
    public int insertSdDataTrafficHour(SdDataTrafficHour sdDataTrafficHour);

    /**
     * 修改各路段小时车流量统计
     *
     * @param sdDataTrafficHour 各路段小时车流量统计
     * @return 结果
     */
    public int updateSdDataTrafficHour(SdDataTrafficHour sdDataTrafficHour);

    /**
     * 删除各路段小时车流量统计
     *
     * @param id 各路段小时车流量统计主键
     * @return 结果
     */
    public int deleteSdDataTrafficHourById(Long id);

    /**
     * 批量删除各路段小时车流量统计
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdDataTrafficHourByIds(Long[] ids);

    public List<Map<String, Object>> getCarNumberByHour(@Param("tunnelId") String tunnelId, @Param("tunnelName") String tunnelName, @Param("params") Map<String, Object> params);

    public List<Map<String, Object>> countYesterdayTrafficFlowData();
}
