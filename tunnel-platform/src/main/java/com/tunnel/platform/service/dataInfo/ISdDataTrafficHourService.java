package com.tunnel.platform.service.dataInfo;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.tunnel.platform.domain.dataInfo.SdDataTrafficDay;
import com.tunnel.platform.domain.dataInfo.SdDataTrafficHour;

/**
 * 各路段小时车流量统计Service接口
 *
 * @author ruoyi
 * @date 2022-02-18
 */
public interface ISdDataTrafficHourService
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
     * 批量删除各路段小时车流量统计
     *
     * @param ids 需要删除的各路段小时车流量统计主键集合
     * @return 结果
     */
    public int deleteSdDataTrafficHourByIds(Long[] ids);

    /**
     * 删除各路段小时车流量统计信息
     *
     * @param id 各路段小时车流量统计主键
     * @return 结果
     */
    public int deleteSdDataTrafficHourById(Long id);

    public List<Map<String, Object>> getCarNumberByHour(SdDataTrafficHour sdDataTrafficHour) throws ParseException;

    /**
     * 查询昨日小时统计车流量数据
     * */
    public List<Map<String, Object>> countYesterdayTrafficFlowData();

    public List<Map<String, Object>> getCacheTrafficFlowInformation();

    public void addRedisData();
}
