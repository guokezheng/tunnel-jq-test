package com.tunnel.business.service.emeResource;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.tunnel.business.domain.emeResource.SdEmergencyOrg;
import com.tunnel.business.domain.emeResource.SdEmergencyVehicle;

import java.util.List;
import java.util.Map;

/**
 * 应急车辆Service接口
 *
 * @author dzy
 * @date 2022-08-09
 */
public interface ISdEmergencyVehicleService {
    /**
     * 查询应急车辆
     *
     * @param id 应急车辆主键
     * @return 应急车辆
     */
    SdEmergencyVehicle selectSdEmergencyVehicleById(Long id);

    /**
     * 查询应急车辆列表
     *
     * @param sdEmergencyVehicle 应急车辆
     * @return 应急车辆集合
     */
    List<SdEmergencyVehicle> selectSdEmergencyVehicleList(SdEmergencyVehicle sdEmergencyVehicle);

    /**
     * 新增应急车辆
     *
     * @param sdEmergencyVehicle 应急车辆
     * @return 结果
     */
    int insertSdEmergencyVehicle(SdEmergencyVehicle sdEmergencyVehicle);

    /**
     * 修改应急车辆
     *
     * @param sdEmergencyVehicle 应急车辆
     * @return 结果
     */
    int updateSdEmergencyVehicle(SdEmergencyVehicle sdEmergencyVehicle);

    /**
     * 批量删除应急车辆
     *
     * @param ids 需要删除的应急车辆主键集合
     * @return 结果
     */
    int deleteSdEmergencyVehicleByIds(Long[] ids);

    /**
     * 删除应急车辆信息
     *
     * @param id 应急车辆主键
     * @return 结果
     */
    int deleteSdEmergencyVehicleById(Long id);

    List<Map<String, Object>> getOrg();

    /**
     * 同步应急车辆数据
     * @return
     */
    String synVehicleData();

    /**
     * 查询车辆详情
     * @param plateNumber
     * @return
     */
    SdEmergencyVehicle getVehicleDetails(String plateNumber);
}
