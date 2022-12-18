package com.tunnel.business.service.emeResource;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.emeResource.SdVehicleType;

/**
 * 车辆类型配置Service接口
 * 
 * @author ruoyi
 * @date 2022-12-01
 */
public interface ISdVehicleTypeService 
{
    /**
     * 查询车辆类型配置
     *
     * @param id 车辆类型配置主键
     * @return 车辆类型配置
     */
    public Object selectSdVehicleTypeById(String id);

    /**
     * 查询车辆类型配置列表
     * 
     * @param sdVehicleType 车辆类型配置
     * @return 车辆类型配置集合
     */
    public List<SdVehicleType> selectSdVehicleTypeList(SdVehicleType sdVehicleType);

    /**
     * 导出车辆类型配置列表
     *
     * @param sdVehicleType 车辆类型配置
     * @return 车辆类型配置集合
     */
    public List<SdVehicleType> exportSdVehicleTypeList(SdVehicleType sdVehicleType);

    /**
     * 新增车辆类型配置
     * 
     * @param sdVehicleType 车辆类型配置
     * @return 结果
     */
    public int insertSdVehicleType(SdVehicleType sdVehicleType);

    /**
     * 修改车辆类型配置
     * 
     * @param sdVehicleType 车辆类型配置
     * @return 结果
     */
    public int updateSdVehicleType(SdVehicleType sdVehicleType);

    /**
     * 批量删除车辆类型配置
     * 
     * @param ids 需要删除的车辆类型配置主键集合
     * @return 结果
     */
    public int deleteSdVehicleTypeByIds(String[] ids);

    /**
     * 删除车辆类型配置信息
     * 
     * @param id 车辆类型配置主键
     * @return 结果
     */
    public int deleteSdVehicleTypeById(String id);

    /**
     * 校验数据是否重复
     * @param sdVehicleType
     * @return
     */
    AjaxResult checkData(SdVehicleType sdVehicleType);
}
