package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdDataTrafficHour;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 各路段小时车流量统计Service接口
 *
 * @author ruoyi
 * @date 2022-02-18
 */
public interface ISdDataTrafficHourService {
    /**
     * 查询各路段小时车流量统计
     *
     * @param id 各路段小时车流量统计主键
     * @return 各路段小时车流量统计
     */
    SdDataTrafficHour selectSdDataTrafficHourById(Long id);

    /**
     * 查询各路段小时车流量统计列表
     *
     * @param sdDataTrafficHour 各路段小时车流量统计
     * @return 各路段小时车流量统计集合
     */
    List<SdDataTrafficHour> selectSdDataTrafficHourList(SdDataTrafficHour sdDataTrafficHour);

    /**
     * 新增各路段小时车流量统计
     *
     * @param sdDataTrafficHour 各路段小时车流量统计
     * @return 结果
     */
    int insertSdDataTrafficHour(SdDataTrafficHour sdDataTrafficHour);

    /**
     * 修改各路段小时车流量统计
     *
     * @param sdDataTrafficHour 各路段小时车流量统计
     * @return 结果
     */
    int updateSdDataTrafficHour(SdDataTrafficHour sdDataTrafficHour);

    /**
     * 批量删除各路段小时车流量统计
     *
     * @param ids 需要删除的各路段小时车流量统计主键集合
     * @return 结果
     */
    int deleteSdDataTrafficHourByIds(Long[] ids);

    /**
     * 删除各路段小时车流量统计信息
     *
     * @param id 各路段小时车流量统计主键
     * @return 结果
     */
    int deleteSdDataTrafficHourById(Long id);

    List<Map<String, Object>> getCarNumberByHour(SdDataTrafficHour sdDataTrafficHour) throws ParseException;

    /**
     * 查询昨日小时统计车流量数据
     */
    List<Map<String, Object>> countYesterdayTrafficFlowData();

    List<Map<String, Object>> getCacheTrafficFlowInformation();

    void addRedisData();
}
