package com.tunnel.business.mapper.emeResource;

import com.tunnel.business.domain.emeResource.SdFocusVehicle;

import java.util.List;

/**
 * 重点车辆Mapper接口
 * 
 * @author ruoyi
 * @date 2024-1-24
 */
public interface SdFocusVehicleMapper
{
    /**
     * 查询车辆类型配置列表
     * 
     * @param sdFocusVehicle 车辆类型配置
     * @return 车辆类型配置集合
     */
    public List<SdFocusVehicle> selectFocusVehicleList(SdFocusVehicle sdFocusVehicle);

    /**
     * 导出车辆类型配置列表
     *
     * @param sdFocusVehicle 车辆类型配置
     * @return 车辆类型配置集合
     */
    public List<SdFocusVehicle> exportFocusVehicleList(SdFocusVehicle sdFocusVehicle);
}
