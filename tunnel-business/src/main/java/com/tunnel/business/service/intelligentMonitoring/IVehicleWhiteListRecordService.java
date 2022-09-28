package com.tunnel.business.service.intelligentMonitoring;


import com.tunnel.business.domain.intelligentMonitoring.VehicleWhiteListRecord;
import com.tunnel.business.domain.intelligentMonitoring.VehicleWhiteListRecordDTO;

import java.util.List;

/**
 * 白名单车辆通行记录Service接口
 *
 * @author ruoyi
 * @date 2021-11-30
 */
public interface IVehicleWhiteListRecordService {
    /**
     * 查询白名单车辆通行记录
     *
     * @param id 白名单车辆通行记录ID
     * @return 白名单车辆通行记录
     */
    VehicleWhiteListRecord selectVehicleWhiteListRecordById(Integer id);

    /**
     * 查询白名单车辆通行记录列表
     *
     * @param vehicleWhiteListRecordDTO 白名单车辆通行记录
     * @return 白名单车辆通行记录集合
     */
    List<VehicleWhiteListRecord> selectVehicleWhiteListRecordList(VehicleWhiteListRecordDTO vehicleWhiteListRecordDTO);

    /**
     * 新增白名单车辆通行记录
     *
     * @param vehicleWhiteListRecord 白名单车辆通行记录
     * @return 结果
     */
    int insertVehicleWhiteListRecord(VehicleWhiteListRecord vehicleWhiteListRecord);

    /**
     * 修改白名单车辆通行记录
     *
     * @param vehicleWhiteListRecord 白名单车辆通行记录
     * @return 结果
     */
    int updateVehicleWhiteListRecord(VehicleWhiteListRecord vehicleWhiteListRecord);

    /**
     * 批量删除白名单车辆通行记录
     *
     * @param ids 需要删除的白名单车辆通行记录ID
     * @return 结果
     */
    int deleteVehicleWhiteListRecordByIds(Integer[] ids);

    /**
     * 删除白名单车辆通行记录信息
     *
     * @param id 白名单车辆通行记录ID
     * @return 结果
     */
    int deleteVehicleWhiteListRecordById(Integer id);
}
