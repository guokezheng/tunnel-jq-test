package com.tunnel.business.service.vehicle.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.vehicle.SdVehicleData;
import com.tunnel.business.mapper.vehicle.SdVehicleDataMapper;
import com.tunnel.business.service.vehicle.ISdVehicleDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 隧道车辆数据（单车数据）Service业务层处理
 *
 * @author ruoyi
 * @date 2023-02-25
 */
@Service
public class SdVehicleDataServiceImpl implements ISdVehicleDataService
{
    @Autowired
    private SdVehicleDataMapper sdVehicleDataMapper;

    /**
     * 查询隧道车辆数据（单车数据）
     *
     * @param id 隧道车辆数据（单车数据）主键
     * @return 隧道车辆数据（单车数据）
     */
    @Override
    public SdVehicleData selectSdVehicleDataById(Long id)
    {
        return sdVehicleDataMapper.selectSdVehicleDataById(id);
    }

    /**
     * 查询隧道车辆数据（单车数据）列表
     *
     * @param sdVehicleData 隧道车辆数据（单车数据）
     * @return 隧道车辆数据（单车数据）
     */
    @Override
    public List<SdVehicleData> selectSdVehicleDataList(SdVehicleData sdVehicleData)
    {
        return sdVehicleDataMapper.selectSdVehicleDataList(sdVehicleData);
    }

    /**
     * 新增隧道车辆数据（单车数据）
     *
     * @param sdVehicleData 隧道车辆数据（单车数据）
     * @return 结果
     */
    @Override
    public int insertSdVehicleData(SdVehicleData sdVehicleData)
    {
        sdVehicleData.setCreateTime(DateUtils.getNowDate());
        return sdVehicleDataMapper.insertSdVehicleData(sdVehicleData);
    }

    /**
     * 修改隧道车辆数据（单车数据）
     *
     * @param sdVehicleData 隧道车辆数据（单车数据）
     * @return 结果
     */
    @Override
    public int updateSdVehicleData(SdVehicleData sdVehicleData)
    {
        sdVehicleData.setUpdateTime(DateUtils.getNowDate());
        return sdVehicleDataMapper.updateSdVehicleData(sdVehicleData);
    }

    /**
     * 批量删除隧道车辆数据（单车数据）
     *
     * @param ids 需要删除的隧道车辆数据（单车数据）主键
     * @return 结果
     */
    @Override
    public int deleteSdVehicleDataByIds(Long[] ids)
    {
        return sdVehicleDataMapper.deleteSdVehicleDataByIds(ids);
    }

    /**
     * 删除隧道车辆数据（单车数据）信息
     *
     * @param id 隧道车辆数据（单车数据）主键
     * @return 结果
     */
    @Override
    public int deleteSdVehicleDataById(Long id)
    {
        return sdVehicleDataMapper.deleteSdVehicleDataById(id);
    }

    /**
     * 按小时统计当天的车辆数据
     *
     * @param sdVehicleData 隧道ID
     * @return
     */
    @Override
    public List<Map> getDayVehicleData(SdVehicleData sdVehicleData) {

        return sdVehicleDataMapper.getDayVehicleData(sdVehicleData);
    }
}
