package com.tunnel.business.service.emeResource.impl;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.domain.emeResource.SdVehicleType;
import com.tunnel.business.mapper.emeResource.SdVehicleTypeMapper;
import com.tunnel.business.service.emeResource.ISdVehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 车辆类型配置Service业务层处理
 *
 * @author ruoyi
 * @date 2022-12-01
 */
@Service
public class SdVehicleTypeServiceImpl implements ISdVehicleTypeService
{
    @Autowired
    private SdVehicleTypeMapper sdVehicleTypeMapper;

    /**
     * 查询车辆类型配置
     *
     * @param id 车辆类型配置主键
     * @return 车辆类型配置
     */
    @Override
    public SdVehicleType selectSdVehicleTypeById(String id)
    {
        return sdVehicleTypeMapper.selectSdVehicleTypeById(id);
    }

    /**
     * 查询车辆类型配置列表
     *
     * @param sdVehicleType 车辆类型配置
     * @return 车辆类型配置
     */
    @Override
    public List<SdVehicleType> selectSdVehicleTypeList(SdVehicleType sdVehicleType)
    {
        return sdVehicleTypeMapper.selectSdVehicleTypeList(sdVehicleType);
    }

    /**
     * 导出车辆类型配置列表
     *
     * @param sdVehicleType 车辆类型配置
     * @return 车辆类型配置
     */
    @Override
    public List<SdVehicleType> exportSdVehicleTypeList(SdVehicleType sdVehicleType)
    {
        return sdVehicleTypeMapper.exportSdVehicleTypeList(sdVehicleType);
    }

    /**
     * 新增车辆类型配置
     *
     * @param sdVehicleType 车辆类型配置
     * @return 结果
     */
    @Override
    public int insertSdVehicleType(SdVehicleType sdVehicleType)
    {
        sdVehicleType.setCreateTime(DateUtils.getNowDate());
        return sdVehicleTypeMapper.insertSdVehicleType(sdVehicleType);
    }

    /**
     * 修改车辆类型配置
     *
     * @param sdVehicleType 车辆类型配置
     * @return 结果
     */
    @Override
    public int updateSdVehicleType(SdVehicleType sdVehicleType)
    {
        sdVehicleType.setUpdateTime(DateUtils.getNowDate());
        return sdVehicleTypeMapper.updateSdVehicleType(sdVehicleType);
    }

    /**
     * 批量删除车辆类型配置
     *
     * @param ids 需要删除的车辆类型配置主键
     * @return 结果
     */
    @Override
    public int deleteSdVehicleTypeByIds(String[] ids)
    {
        return sdVehicleTypeMapper.deleteSdVehicleTypeByIds(ids);
    }

    /**
     * 删除车辆类型配置信息
     *
     * @param id 车辆类型配置主键
     * @return 结果
     */
    @Override
    public int deleteSdVehicleTypeById(String id)
    {
        return sdVehicleTypeMapper.deleteSdVehicleTypeById(id);
    }

    /**
     * 校验数据是否重复
     * @param sdVehicleType
     * @return
     */
    @Override
    public AjaxResult checkData(SdVehicleType sdVehicleType) {
        SdVehicleType typeData = new SdVehicleType();
        //校验类型编码
        typeData.setVehicleTypeCode(sdVehicleType.getVehicleTypeCode());
        if(sdVehicleType.getId() != null){
            typeData.setId(sdVehicleType.getId());
        }
        int codeCount = sdVehicleTypeMapper.checkData(typeData);
        if(codeCount > 0){
            return AjaxResult.error("类型编码已存在！");
        }
        //校验类型名称
        typeData.setVehicleTypeCode(null);
        typeData.setVehicleTypeName(sdVehicleType.getVehicleTypeName());
        int nameCount = sdVehicleTypeMapper.checkData(typeData);
        if(nameCount > 0){
            return AjaxResult.error("类型名称已存在！");
        }
        return AjaxResult.success();
    }
}
