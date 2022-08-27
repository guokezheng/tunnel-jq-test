package com.tunnel.platform.mapper.intelligentMonitoring;

import com.tunnel.platform.domain.intelligentMonitoring.VehicleWhiteList;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * 车辆通行白名单Mapper接口
 *
 * @author ruoyi
 * @date 2021-11-30
 */
public interface VehicleWhiteListMapper
{
    /**
     * 查询车辆通行白名单
     *
     * @param id 车辆通行白名单ID
     * @return 车辆通行白名单
     */
    public VehicleWhiteList selectVehicleWhiteListById(Integer id);

    /**
     * 查询车辆通行白名单列表
     *
     * @param vehicleWhiteList 车辆通行白名单
     * @return 车辆通行白名单集合
     */
    public List<VehicleWhiteList> selectVehicleWhiteListList(VehicleWhiteList vehicleWhiteList);

    /**
     * 新增车辆通行白名单
     *
     * @param vehicleWhiteList 车辆通行白名单
     * @return 结果
     */
    public int insertVehicleWhiteList(VehicleWhiteList vehicleWhiteList);

    /**
     * 修改车辆通行白名单
     *
     * @param vehicleWhiteList 车辆通行白名单
     * @return 结果
     */
    public int updateVehicleWhiteList(VehicleWhiteList vehicleWhiteList);

    /**
     * 删除车辆通行白名单
     *
     * @param id 车辆通行白名单ID
     * @return 结果
     */
    public int deleteVehicleWhiteListById(Integer id);

    /**
     * 批量删除车辆通行白名单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteVehicleWhiteListByIds(Integer[] ids);

    public List<VehicleWhiteList> selectlistByLicensePlateNumber(@Param("licensePlateNumber") String icensePlateNumber);
}
