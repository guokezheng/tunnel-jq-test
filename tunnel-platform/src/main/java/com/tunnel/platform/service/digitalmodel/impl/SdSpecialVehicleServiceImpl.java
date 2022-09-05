package com.tunnel.platform.service.digitalmodel.impl;

import com.tunnel.platform.domain.digitalmodel.SdSpecialVehicles;
import com.tunnel.platform.mapper.digitalmodel.SdSpecialVehiclesMapper;
import com.tunnel.platform.service.digitalmodel.ISdSpecialVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 重点车辆Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-09-04
 */
@Service
public class SdSpecialVehicleServiceImpl implements ISdSpecialVehicleService
{
    @Autowired
    private SdSpecialVehiclesMapper sdSpecialVehicleMapper;

    /**
     * 查询重点车辆
     * 
     * @param id 重点车辆主键
     * @return 重点车辆
     */
    @Override
    public SdSpecialVehicles selectSdSpecialVehicleById(String id)
    {
        return sdSpecialVehicleMapper.selectSdSpecialVehicleById(id);
    }

    /**
     * 查询重点车辆列表
     * 
     * @param sdSpecialVehicle 重点车辆
     * @return 重点车辆
     */
    @Override
    public List<SdSpecialVehicles> selectSdSpecialVehicleList(SdSpecialVehicles sdSpecialVehicle)
    {
        return sdSpecialVehicleMapper.selectSdSpecialVehicleList(sdSpecialVehicle);
    }

    /**
     * 新增重点车辆
     * 
     * @param sdSpecialVehicle 重点车辆
     * @return 结果
     */
    @Override
    public int insertSdSpecialVehicle(SdSpecialVehicles sdSpecialVehicle)
    {
        return sdSpecialVehicleMapper.insertSdSpecialVehicle(sdSpecialVehicle);
    }

    /**
     * 修改重点车辆
     * 
     * @param sdSpecialVehicle 重点车辆
     * @return 结果
     */
    @Override
    public int updateSdSpecialVehicle(SdSpecialVehicles sdSpecialVehicle)
    {
        return sdSpecialVehicleMapper.updateSdSpecialVehicle(sdSpecialVehicle);
    }

    /**
     * 批量删除重点车辆
     * 
     * @param ids 需要删除的重点车辆主键
     * @return 结果
     */
    @Override
    public int deleteSdSpecialVehicleByIds(String[] ids)
    {
        return sdSpecialVehicleMapper.deleteSdSpecialVehicleByIds(ids);
    }

    /**
     * 删除重点车辆信息
     * 
     * @param id 重点车辆主键
     * @return 结果
     */
    @Override
    public int deleteSdSpecialVehicleById(String id)
    {
        return sdSpecialVehicleMapper.deleteSdSpecialVehicleById(id);
    }

    /**
     * 根据隧道id 查询24小时 重点车辆
     * @param tunnelId
     * @return
     */
    @Override
    public List<SdSpecialVehicles> specialById(String tunnelId) {
        List<SdSpecialVehicles> data = sdSpecialVehicleMapper.specialById(tunnelId);
        return data;
    }
}
