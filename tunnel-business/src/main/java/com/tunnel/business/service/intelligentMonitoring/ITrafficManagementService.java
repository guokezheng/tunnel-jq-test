package com.tunnel.business.service.intelligentMonitoring;


import com.tunnel.business.domain.intelligentMonitoring.TrafficManagement;
import com.tunnel.business.domain.intelligentMonitoring.TrafficManagementDTO;

import java.util.List;

/**
 * 特种车辆通行管理Service接口
 *
 * @author ruoyi
 * @date 2021-12-01
 */
public interface ITrafficManagementService {
    /**
     * 查询特种车辆通行管理
     *
     * @param id 特种车辆通行管理ID
     * @return 特种车辆通行管理
     */
    TrafficManagement selectTrafficManagementById(Long id);

    /**
     * 查询特种车辆通行管理列表
     *
     * @param trafficManagementDTO 特种车辆通行管理
     * @return 特种车辆通行管理集合
     */
    List<TrafficManagement> selectTrafficManagementList(TrafficManagementDTO trafficManagementDTO);

    /**
     * 新增特种车辆通行管理
     *
     * @param trafficManagement 特种车辆通行管理
     * @return 结果
     */
    int insertTrafficManagement(TrafficManagement trafficManagement);

    /**
     * 修改特种车辆通行管理
     *
     * @param trafficManagement 特种车辆通行管理
     * @return 结果
     */
    int updateTrafficManagement(TrafficManagement trafficManagement);

    /**
     * 批量删除特种车辆通行管理
     *
     * @param ids 需要删除的特种车辆通行管理ID
     * @return 结果
     */
    int deleteTrafficManagementByIds(Long[] ids);

    /**
     * 删除特种车辆通行管理信息
     *
     * @param id 特种车辆通行管理ID
     * @return 结果
     */
    int deleteTrafficManagementById(Long id);

    /**
     * 特种车辆通行下发通知
     *
     * @param id
     * @param userId
     * @return
     */
    void assignTrafficManagement(Long id, Long[] userId);
}
