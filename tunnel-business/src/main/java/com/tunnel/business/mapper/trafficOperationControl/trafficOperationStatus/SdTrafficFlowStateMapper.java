package com.tunnel.business.mapper.trafficOperationControl.trafficOperationStatus;

import com.tunnel.business.domain.trafficOperationControl.trafficOperationStatus.SdTrafficFlowState;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【交通流状态记录】Mapper接口
 *
 * @author ruoyi
 * @date 2022-02-14
 */
public interface SdTrafficFlowStateMapper
{
    /**
     * 查询【交通流状态记录】
     *
     * @param id 【交通流状态记录】主键
     * @return 【交通流状态记录】
     */
    public SdTrafficFlowState selectSdTrafficFlowStateById(Long id);

    /**
     * 查询【交通流状态记录】列表
     *
     * @param sdTrafficFlowState 【交通流状态记录】
     * @return 【交通流状态记录】集合
     */
    public List<SdTrafficFlowState> selectSdTrafficFlowStateList(SdTrafficFlowState sdTrafficFlowState);

    /**
     * 新增【交通流状态记录】
     *
     * @param sdTrafficFlowState 【交通流状态记录】
     * @return 结果
     */
    public int insertSdTrafficFlowState(SdTrafficFlowState sdTrafficFlowState);

    /**
     * 修改【交通流状态记录】
     *
     * @param sdTrafficFlowState 【交通流状态记录】
     * @return 结果
     */
    public int updateSdTrafficFlowState(SdTrafficFlowState sdTrafficFlowState);

    /**
     * 删除【交通流状态记录】
     *
     * @param id 【交通流状态记录】主键
     * @return 结果
     */
    public int deleteSdTrafficFlowStateById(Long id);

    /**
     * 批量删除【交通流状态记录】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdTrafficFlowStateByIds(Long[] ids);


    /**
     * 展示各隧道最近24小时内各个种类车型的车流量和平均速度的趋势图
     * @param tunnelId 隧道id
     * @return
     */
    List<SdTrafficFlowState> getChartData(@Param("tunnelId") String tunnelId, @Param("deptId") Long deptId);
}
