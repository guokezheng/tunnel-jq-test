package com.tunnel.platform.service.dataInfo;

import java.util.List;
import com.tunnel.platform.domain.dataInfo.SdVehicleManagement;

/**
 * 车辆管理Service接口
 * 
 * @author ruoyi
 * @date 2022-02-18
 */
public interface ISdVehicleManagementService 
{
    /**
     * 查询车辆管理
     * 
     * @param id 车辆管理主键
     * @return 车辆管理
     */
    public SdVehicleManagement selectSdVehicleManagementById(Long id);

    /**
     * 查询车辆管理列表
     * 
     * @param sdVehicleManagement 车辆管理
     * @return 车辆管理集合
     */
    public List<SdVehicleManagement> selectSdVehicleManagementList(SdVehicleManagement sdVehicleManagement);

    /**
     * 新增车辆管理
     * 
     * @param sdVehicleManagement 车辆管理
     * @return 结果
     */
    public int insertSdVehicleManagement(SdVehicleManagement sdVehicleManagement);

    /**
     * 修改车辆管理
     * 
     * @param sdVehicleManagement 车辆管理
     * @return 结果
     */
    public int updateSdVehicleManagement(SdVehicleManagement sdVehicleManagement);

    /**
     * 批量删除车辆管理
     * 
     * @param ids 需要删除的车辆管理主键集合
     * @return 结果
     */
    public int deleteSdVehicleManagementByIds(Long[] ids);

    /**
     * 删除车辆管理信息
     * 
     * @param id 车辆管理主键
     * @return 结果
     */
    public int deleteSdVehicleManagementById(Long id);
}
