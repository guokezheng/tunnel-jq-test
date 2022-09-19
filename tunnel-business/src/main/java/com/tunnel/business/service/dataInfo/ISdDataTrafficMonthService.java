package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdDataTrafficMonth;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 各路段月车流量统计Service接口
 *
 * @author ruoyi
 * @date 2022-02-18
 */
public interface ISdDataTrafficMonthService {
    /**
     * 查询各路段月车流量统计
     *
     * @param id 各路段月车流量统计主键
     * @return 各路段月车流量统计
     */
    SdDataTrafficMonth selectSdDataTrafficMonthById(Long id);

    /**
     * 查询各路段月车流量统计列表
     *
     * @param sdDataTrafficMonth 各路段月车流量统计
     * @return 各路段月车流量统计集合
     */
    List<SdDataTrafficMonth> selectSdDataTrafficMonthList(SdDataTrafficMonth sdDataTrafficMonth);

    /**
     * 新增各路段月车流量统计
     *
     * @param sdDataTrafficMonth 各路段月车流量统计
     * @return 结果
     */
    int insertSdDataTrafficMonth(SdDataTrafficMonth sdDataTrafficMonth);

    /**
     * 修改各路段月车流量统计
     *
     * @param sdDataTrafficMonth 各路段月车流量统计
     * @return 结果
     */
    int updateSdDataTrafficMonth(SdDataTrafficMonth sdDataTrafficMonth);

    /**
     * 批量删除各路段月车流量统计
     *
     * @param ids 需要删除的各路段月车流量统计主键集合
     * @return 结果
     */
    int deleteSdDataTrafficMonthByIds(Long[] ids);

    /**
     * 删除各路段月车流量统计信息
     *
     * @param id 各路段月车流量统计主键
     * @return 结果
     */
    int deleteSdDataTrafficMonthById(Long id);

    List<Map<String, Object>> getCarNumberByMonth(SdDataTrafficMonth sdDataTrafficMonth) throws ParseException;

    void summarizeTrafficFlowData(List<Map<String, Object>> sdDataTrafficDays);
}
