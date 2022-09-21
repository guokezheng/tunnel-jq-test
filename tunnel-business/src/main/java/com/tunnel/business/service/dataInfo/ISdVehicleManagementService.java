package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdVehicleManagement;

import java.util.List;

/**
 * 车辆管理Service接口
 *
 * @author ruoyi
 * @date 2022-02-18
 */
public interface ISdVehicleManagementService {
    /**
     * 查询车辆管理
     *
     * @param id 车辆管理主键
     * @return 车辆管理
     */
    SdVehicleManagement selectSdVehicleManagementById(Long id);

    /**
     * 查询车辆管理列表
     *
     * @param sdVehicleManagement 车辆管理
     * @return 车辆管理集合
     */
    List<SdVehicleManagement> selectSdVehicleManagementList(SdVehicleManagement sdVehicleManagement);

    /**
     * 新增车辆管理
     *
     * @param sdVehicleManagement 车辆管理
     * @return 结果
     */
    int insertSdVehicleManagement(SdVehicleManagement sdVehicleManagement);

    /**
     * 修改车辆管理
     *
     * @param sdVehicleManagement 车辆管理
     * @return 结果
     */
    int updateSdVehicleManagement(SdVehicleManagement sdVehicleManagement);

    /**
     * 批量删除车辆管理
     *
     * @param ids 需要删除的车辆管理主键集合
     * @return 结果
     */
    int deleteSdVehicleManagementByIds(Long[] ids);

    /**
     * 删除车辆管理信息
     *
     * @param id 车辆管理主键
     * @return 结果
     */
    int deleteSdVehicleManagementById(Long id);
}
