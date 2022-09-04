package com.tunnel.platform.service.digitalmodel;

import com.tunnel.platform.domain.digitalmodel.SdSpecialVehicles;

import java.util.List;


/**
 * 重点车辆Service接口
 * 
 * @author ruoyi
 * @date 2022-09-04
 */
public interface ISdSpecialVehicleService 
{
    /**
     * 查询重点车辆
     * 
     * @param id 重点车辆主键
     * @return 重点车辆
     */
    public SdSpecialVehicles selectSdSpecialVehicleById(String id);

    /**
     * 查询重点车辆列表
     * 
     * @param sdSpecialVehicle 重点车辆
     * @return 重点车辆集合
     */
    public List<SdSpecialVehicles> selectSdSpecialVehicleList(SdSpecialVehicles sdSpecialVehicle);

    /**
     * 新增重点车辆
     * 
     * @param sdSpecialVehicle 重点车辆
     * @return 结果
     */
    public int insertSdSpecialVehicle(SdSpecialVehicles sdSpecialVehicle);

    /**
     * 修改重点车辆
     * 
     * @param sdSpecialVehicle 重点车辆
     * @return 结果
     */
    public int updateSdSpecialVehicle(SdSpecialVehicles sdSpecialVehicle);

    /**
     * 批量删除重点车辆
     * 
     * @param ids 需要删除的重点车辆主键集合
     * @return 结果
     */
    public int deleteSdSpecialVehicleByIds(String[] ids);

    /**
     * 删除重点车辆信息
     * 
     * @param id 重点车辆主键
     * @return 结果
     */
    public int deleteSdSpecialVehicleById(String id);
}
