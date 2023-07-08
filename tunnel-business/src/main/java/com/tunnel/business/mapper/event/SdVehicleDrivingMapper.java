package com.tunnel.business.mapper.event;

import com.tunnel.business.domain.event.SdVehicleDriving;

import java.util.List;

/**
 * 路段车辆行驶记录Mapper接口
 * 
 * @author ruoyi
 * @date 2023-01-13
 */
public interface SdVehicleDrivingMapper 
{
    /**
     * 查询路段车辆行驶记录
     * 
     * @param id 路段车辆行驶记录主键
     * @return 路段车辆行驶记录
     */
    public SdVehicleDriving selectSdVehicleDrivingById(Long id);

    /**
     * 查询路段车辆行驶记录列表
     * 
     * @param sdVehicleDriving 路段车辆行驶记录
     * @return 路段车辆行驶记录集合
     */
    public List<SdVehicleDriving> selectSdVehicleDrivingList(SdVehicleDriving sdVehicleDriving);

    /**
     * 新增路段车辆行驶记录
     * 
     * @param sdVehicleDriving 路段车辆行驶记录
     * @return 结果
     */
    public int insertSdVehicleDriving(SdVehicleDriving sdVehicleDriving);

    /**
     * 修改路段车辆行驶记录
     * 
     * @param sdVehicleDriving 路段车辆行驶记录
     * @return 结果
     */
    public int updateSdVehicleDriving(SdVehicleDriving sdVehicleDriving);

    /**
     * 删除路段车辆行驶记录
     * 
     * @param id 路段车辆行驶记录主键
     * @return 结果
     */
    public int deleteSdVehicleDrivingById(Long id);

    /**
     * 批量删除路段车辆行驶记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdVehicleDrivingByIds(Long[] ids);
}
