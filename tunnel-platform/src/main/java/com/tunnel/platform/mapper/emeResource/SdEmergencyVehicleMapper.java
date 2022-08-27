package com.tunnel.platform.mapper.emeResource;

import java.util.List;

import com.tunnel.platform.domain.emeResource.SdEmergencyOrg;
import com.tunnel.platform.domain.emeResource.SdEmergencyVehicle;

/**
 * 应急车辆Mapper接口
 * 
 * @author dzy
 * @date 2022-08-09
 */
public interface SdEmergencyVehicleMapper 
{
    /**
     * 查询应急车辆
     * 
     * @param id 应急车辆主键
     * @return 应急车辆
     */
    public SdEmergencyVehicle selectSdEmergencyVehicleById(Long id);

    /**
     * 查询应急车辆列表
     * 
     * @param sdEmergencyVehicle 应急车辆
     * @return 应急车辆集合
     */
    public List<SdEmergencyVehicle> selectSdEmergencyVehicleList(SdEmergencyVehicle sdEmergencyVehicle);

    /**
     * 新增应急车辆
     * 
     * @param sdEmergencyVehicle 应急车辆
     * @return 结果
     */
    public int insertSdEmergencyVehicle(SdEmergencyVehicle sdEmergencyVehicle);

    /**
     * 修改应急车辆
     * 
     * @param sdEmergencyVehicle 应急车辆
     * @return 结果
     */
    public int updateSdEmergencyVehicle(SdEmergencyVehicle sdEmergencyVehicle);

    /**
     * 删除应急车辆
     * 
     * @param id 应急车辆主键
     * @return 结果
     */
    public int deleteSdEmergencyVehicleById(Long id);

    /**
     * 批量删除应急车辆
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdEmergencyVehicleByIds(Long[] ids);

    List<SdEmergencyOrg> getOrg();
}
