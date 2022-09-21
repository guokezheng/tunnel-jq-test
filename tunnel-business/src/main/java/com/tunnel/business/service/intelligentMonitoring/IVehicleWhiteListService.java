package com.tunnel.business.service.intelligentMonitoring;


import com.tunnel.business.domain.intelligentMonitoring.VehicleWhiteList;

import java.util.List;

/**
 * 车辆通行白名单Service接口
 *
 * @author ruoyi
 * @date 2021-11-30
 */
public interface IVehicleWhiteListService {
    /**
     * 查询车辆通行白名单
     *
     * @param id 车辆通行白名单ID
     * @return 车辆通行白名单
     */
    VehicleWhiteList selectVehicleWhiteListById(Integer id);

    /**
     * 查询车辆通行白名单列表
     *
     * @param vehicleWhiteList 车辆通行白名单
     * @return 车辆通行白名单集合
     */
    List<VehicleWhiteList> selectVehicleWhiteListList(VehicleWhiteList vehicleWhiteList);

    /**
     * 新增车辆通行白名单
     *
     * @param vehicleWhiteList 车辆通行白名单
     * @return 结果
     */
    int insertVehicleWhiteList(VehicleWhiteList vehicleWhiteList);

    /**
     * 修改车辆通行白名单
     *
     * @param vehicleWhiteList 车辆通行白名单
     * @return 结果
     */
    int updateVehicleWhiteList(VehicleWhiteList vehicleWhiteList);

    /**
     * 批量删除车辆通行白名单
     *
     * @param ids 需要删除的车辆通行白名单ID
     * @return 结果
     */
    int deleteVehicleWhiteListByIds(Integer[] ids);

    /**
     * 删除车辆通行白名单信息
     *
     * @param id 车辆通行白名单ID
     * @return 结果
     */
    int deleteVehicleWhiteListById(Integer id);
}
