package com.tunnel.platform.service.dataInfo.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tunnel.platform.mapper.dataInfo.SdVehicleManagementMapper;
import com.tunnel.platform.domain.dataInfo.SdVehicleManagement;
import com.tunnel.platform.service.dataInfo.ISdVehicleManagementService;

/**
 * 车辆管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-02-18
 */
@Service
public class SdVehicleManagementServiceImpl implements ISdVehicleManagementService 
{
    @Autowired
    private SdVehicleManagementMapper sdVehicleManagementMapper;

    /**
     * 查询车辆管理
     * 
     * @param id 车辆管理主键
     * @return 车辆管理
     */
    @Override
    public SdVehicleManagement selectSdVehicleManagementById(Long id)
    {
        return sdVehicleManagementMapper.selectSdVehicleManagementById(id);
    }

    /**
     * 查询车辆管理列表
     * 
     * @param sdVehicleManagement 车辆管理
     * @return 车辆管理
     */
    @Override
    public List<SdVehicleManagement> selectSdVehicleManagementList(SdVehicleManagement sdVehicleManagement)
    {
        return sdVehicleManagementMapper.selectSdVehicleManagementList(sdVehicleManagement);
    }

    /**
     * 新增车辆管理
     * 
     * @param sdVehicleManagement 车辆管理
     * @return 结果
     */
    @Override
    public int insertSdVehicleManagement(SdVehicleManagement sdVehicleManagement)
    {
        sdVehicleManagement.setCreateTime(DateUtils.getNowDate());
        return sdVehicleManagementMapper.insertSdVehicleManagement(sdVehicleManagement);
    }

    /**
     * 修改车辆管理
     * 
     * @param sdVehicleManagement 车辆管理
     * @return 结果
     */
    @Override
    public int updateSdVehicleManagement(SdVehicleManagement sdVehicleManagement)
    {
        sdVehicleManagement.setUpdateTime(DateUtils.getNowDate());
        return sdVehicleManagementMapper.updateSdVehicleManagement(sdVehicleManagement);
    }

    /**
     * 批量删除车辆管理
     * 
     * @param ids 需要删除的车辆管理主键
     * @return 结果
     */
    @Override
    public int deleteSdVehicleManagementByIds(Long[] ids)
    {
        return sdVehicleManagementMapper.deleteSdVehicleManagementByIds(ids);
    }

    /**
     * 删除车辆管理信息
     * 
     * @param id 车辆管理主键
     * @return 结果
     */
    @Override
    public int deleteSdVehicleManagementById(Long id)
    {
        return sdVehicleManagementMapper.deleteSdVehicleManagementById(id);
    }
}
