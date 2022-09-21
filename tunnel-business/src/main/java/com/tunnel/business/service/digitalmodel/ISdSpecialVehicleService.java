package com.tunnel.business.service.digitalmodel;


import com.tunnel.business.domain.digitalmodel.SdSpecialVehicles;

import java.util.List;


/**
 * 重点车辆Service接口
 *
 * @author ruoyi
 * @date 2022-09-04
 */
public interface ISdSpecialVehicleService {
    /**
     * 查询重点车辆
     *
     * @param id 重点车辆主键
     * @return 重点车辆
     */
    SdSpecialVehicles selectSdSpecialVehicleById(String id);

    /**
     * 查询重点车辆列表
     *
     * @param sdSpecialVehicle 重点车辆
     * @return 重点车辆集合
     */
    List<SdSpecialVehicles> selectSdSpecialVehicleList(SdSpecialVehicles sdSpecialVehicle);

    /**
     * 新增重点车辆
     *
     * @param sdSpecialVehicle 重点车辆
     * @return 结果
     */
    int insertSdSpecialVehicle(SdSpecialVehicles sdSpecialVehicle);

    /**
     * 修改重点车辆
     *
     * @param sdSpecialVehicle 重点车辆
     * @return 结果
     */
    int updateSdSpecialVehicle(SdSpecialVehicles sdSpecialVehicle);

    /**
     * 批量删除重点车辆
     *
     * @param ids 需要删除的重点车辆主键集合
     * @return 结果
     */
    int deleteSdSpecialVehicleByIds(String[] ids);

    /**
     * 删除重点车辆信息
     *
     * @param id 重点车辆主键
     * @return 结果
     */
    int deleteSdSpecialVehicleById(String id);

    /**
     * 根据隧道id 查询24小时 重点车辆
     *
     * @param tunnelId
     * @return
     */
    Object[] specialById(String tunnelId);
}
