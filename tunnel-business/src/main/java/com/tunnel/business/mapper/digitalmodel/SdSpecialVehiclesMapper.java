package com.tunnel.business.mapper.digitalmodel;


import com.tunnel.business.domain.digitalmodel.SdSpecialVehicles;

import java.util.List;
import java.util.Map;


/**
 * 重点车辆Mapper接口
 * 
 * @author ruoyi
 * @date 2022-09-04
 */
public interface SdSpecialVehiclesMapper
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
    public List<Map<String,String>> selectSdSpecialVehicleList(SdSpecialVehicles sdSpecialVehicle);

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
     * 删除重点车辆
     * 
     * @param id 重点车辆主键
     * @return 结果
     */
    public int deleteSdSpecialVehicleById(String id);

    /**
     * 批量删除重点车辆
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdSpecialVehicleByIds(String[] ids);
    /**
     * 根据隧道id 查询24小时 重点车辆
     * @param tunnelId
     * @return
     */
    public List<Map> specialById(String tunnelId);
}
