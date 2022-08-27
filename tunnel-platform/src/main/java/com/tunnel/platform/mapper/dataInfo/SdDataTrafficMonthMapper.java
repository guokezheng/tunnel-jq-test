package com.tunnel.platform.mapper.dataInfo;

import java.util.List;
import java.util.Map;

import com.tunnel.platform.domain.dataInfo.SdDataTrafficMonth;
import org.apache.ibatis.annotations.Param;

/**
 * 各路段月车流量统计Mapper接口
 * 
 * @author ruoyi
 * @date 2022-02-18
 */
public interface SdDataTrafficMonthMapper 
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
     * 删除各路段月车流量统计
     * 
     * @param id 各路段月车流量统计主键
     * @return 结果
     */
    public int deleteSdDataTrafficMonthById(Long id);

    /**
     * 批量删除各路段月车流量统计
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdDataTrafficMonthByIds(Long[] ids);

    public List<Map<String, Object>> getCarNumberByMonth(@Param("tunnelId") String tunnelId, @Param("tunnelName") String tunnelName, @Param("params") Map<String, Object> params);
}
