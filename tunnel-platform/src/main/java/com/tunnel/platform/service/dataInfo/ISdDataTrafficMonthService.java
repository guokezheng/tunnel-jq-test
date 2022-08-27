package com.tunnel.platform.service.dataInfo;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.tunnel.platform.domain.dataInfo.SdDataTrafficHour;
import com.tunnel.platform.domain.dataInfo.SdDataTrafficMonth;

/**
 * 各路段月车流量统计Service接口
 *
 * @author ruoyi
 * @date 2022-02-18
 */
public interface ISdDataTrafficMonthService
{
    /**
     * 查询各路段月车流量统计
     *
     * @param id 各路段月车流量统计主键
     * @return 各路段月车流量统计
     */
    public SdDataTrafficMonth selectSdDataTrafficMonthById(Long id);

    /**
     * 查询各路段月车流量统计列表
     *
     * @param sdDataTrafficMonth 各路段月车流量统计
     * @return 各路段月车流量统计集合
     */
    public List<SdDataTrafficMonth> selectSdDataTrafficMonthList(SdDataTrafficMonth sdDataTrafficMonth);

    /**
     * 新增各路段月车流量统计
     *
     * @param sdDataTrafficMonth 各路段月车流量统计
     * @return 结果
     */
    public int insertSdDataTrafficMonth(SdDataTrafficMonth sdDataTrafficMonth);

    /**
     * 修改各路段月车流量统计
     *
     * @param sdDataTrafficMonth 各路段月车流量统计
     * @return 结果
     */
    public int updateSdDataTrafficMonth(SdDataTrafficMonth sdDataTrafficMonth);

    /**
     * 批量删除各路段月车流量统计
     *
     * @param ids 需要删除的各路段月车流量统计主键集合
     * @return 结果
     */
    public int deleteSdDataTrafficMonthByIds(Long[] ids);

    /**
     * 删除各路段月车流量统计信息
     *
     * @param id 各路段月车流量统计主键
     * @return 结果
     */
    public int deleteSdDataTrafficMonthById(Long id);

    public List<Map<String, Object>> getCarNumberByMonth(SdDataTrafficMonth sdDataTrafficMonth) throws ParseException;

    public void summarizeTrafficFlowData(List<Map<String, Object>> sdDataTrafficDays);
}
