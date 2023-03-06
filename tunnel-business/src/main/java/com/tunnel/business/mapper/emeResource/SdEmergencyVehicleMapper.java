package com.tunnel.business.mapper.emeResource;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.tunnel.business.domain.emeResource.SdEmergencyOrg;
import com.tunnel.business.domain.emeResource.SdEmergencyVehicle;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


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
     * 批量新增应急车辆
     * @param list
     * @return
     */
    public int insertSdEmergencyVehicles(@Param("list") List<SdEmergencyVehicle> list);

    /**
     * 修改应急车辆
     * 
     * @param sdEmergencyVehicle 应急车辆
     * @return 结果
     */
    public int updateSdEmergencyVehicle(SdEmergencyVehicle sdEmergencyVehicle);

    /**
     * 批量修改应急车辆
     * @param list
     * @return
     */
    public int updateSdEmergencyVehicles(@Param("list") List<SdEmergencyVehicle> list);

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

    List<Map<String, Object>> getOrg();

    /**
     * 删除应急车辆数据
     * @return
     */
    int deleteSdVehicle();
}
