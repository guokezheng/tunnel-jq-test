package com.tunnel.business.mapper.intelligentMonitoring;


import com.tunnel.business.domain.intelligentMonitoring.VehicleWhiteListRecord;
import com.tunnel.business.domain.intelligentMonitoring.VehicleWhiteListRecordDTO;

import java.util.List;

/**
 * 白名单车辆通行记录Mapper接口
 *
 * @author ruoyi
 * @date 2021-11-30
 */
public interface VehicleWhiteListRecordMapper
{
    /**
     * 查询白名单车辆通行记录
     *
     * @param id 白名单车辆通行记录ID
     * @return 白名单车辆通行记录
     */
    public VehicleWhiteListRecord selectVehicleWhiteListRecordById(Integer id);

    /**
     * 查询白名单车辆通行记录列表
     *
     * @param vehicleWhiteListRecordDTO 白名单车辆通行记录
     * @return 白名单车辆通行记录集合
     */
    public List<VehicleWhiteListRecord> selectVehicleWhiteListRecordList(VehicleWhiteListRecordDTO vehicleWhiteListRecordDTO);

    /**
     * 新增白名单车辆通行记录
     *
     * @param vehicleWhiteListRecord 白名单车辆通行记录
     * @return 结果
     */
    public int insertVehicleWhiteListRecord(VehicleWhiteListRecord vehicleWhiteListRecord);

    /**
     * 修改白名单车辆通行记录
     *
     * @param vehicleWhiteListRecord 白名单车辆通行记录
     * @return 结果
     */
    public int updateVehicleWhiteListRecord(VehicleWhiteListRecord vehicleWhiteListRecord);

    /**
     * 删除白名单车辆通行记录
     *
     * @param id 白名单车辆通行记录ID
     * @return 结果
     */
    public int deleteVehicleWhiteListRecordById(Integer id);

    /**
     * 批量删除白名单车辆通行记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteVehicleWhiteListRecordByIds(Integer[] ids);
}
