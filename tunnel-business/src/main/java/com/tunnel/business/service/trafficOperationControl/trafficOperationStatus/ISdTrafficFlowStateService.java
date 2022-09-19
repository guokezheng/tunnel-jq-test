package com.tunnel.business.service.trafficOperationControl.trafficOperationStatus;


import com.tunnel.business.domain.trafficOperationControl.trafficOperationStatus.SdTrafficFlowState;
import com.tunnel.business.utils.json.JSONObject;

import java.util.List;

/**
 * 【交通流状态记录】Service接口
 *
 * @author ruoyi
 * @date 2022-02-14
 */
public interface ISdTrafficFlowStateService {
    /**
     * 查询【交通流状态记录】
     *
     * @param id 【交通流状态记录】主键
     * @return 【交通流状态记录】
     */
    SdTrafficFlowState selectSdTrafficFlowStateById(Long id);

    /**
     * 查询【交通流状态记录】列表
     *
     * @param sdTrafficFlowState 【交通流状态记录】
     * @return 【交通流状态记录】集合
     */
    List<SdTrafficFlowState> selectSdTrafficFlowStateList(SdTrafficFlowState sdTrafficFlowState);

    /**
     * 新增【交通流状态记录】
     *
     * @param sdTrafficFlowState 【交通流状态记录】
     * @return 结果
     */
    int insertSdTrafficFlowState(SdTrafficFlowState sdTrafficFlowState);

    /**
     * 修改【交通流状态记录】
     *
     * @param sdTrafficFlowState 【交通流状态记录】
     * @return 结果
     */
    int updateSdTrafficFlowState(SdTrafficFlowState sdTrafficFlowState);

    /**
     * 批量删除【交通流状态记录】
     *
     * @param ids 需要删除的【交通流状态记录】主键集合
     * @return 结果
     */
    int deleteSdTrafficFlowStateByIds(Long[] ids);

    /**
     * 删除【交通流状态记录】信息
     *
     * @param id 【交通流状态记录】主键
     * @return 结果
     */
    int deleteSdTrafficFlowStateById(Long id);

    /**
     * 展示各隧道最近24小时内各个种类车型的车流量和平均速度的趋势图
     *
     * @param tunnelId 隧道id
     * @return
     */
    JSONObject getChartData(String tunnelId);
}
