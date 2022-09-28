package com.tunnel.business.service.intelligentMonitoring.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.intelligentMonitoring.VehicleWhiteList;
import com.tunnel.business.mapper.intelligentMonitoring.VehicleWhiteListMapper;
import com.tunnel.business.service.intelligentMonitoring.IVehicleWhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车辆通行白名单Service业务层处理
 *
 * @author ruoyi
 * @date 2021-11-30
 */
@Service
public class VehicleWhiteListServiceImpl implements IVehicleWhiteListService {
    @Autowired
    private VehicleWhiteListMapper vehicleWhiteListMapper;

    /**
     * 查询车辆通行白名单
     *
     * @param id 车辆通行白名单ID
     * @return 车辆通行白名单
     */
    @Override
    public VehicleWhiteList selectVehicleWhiteListById(Integer id) {
        return vehicleWhiteListMapper.selectVehicleWhiteListById(id);
    }

    /**
     * 查询车辆通行白名单列表
     *
     * @param vehicleWhiteList 车辆通行白名单
     * @return 车辆通行白名单
     */
    @Override
    public List<VehicleWhiteList> selectVehicleWhiteListList(VehicleWhiteList vehicleWhiteList) {
        return vehicleWhiteListMapper.selectVehicleWhiteListList(vehicleWhiteList);
    }

    /**
     * 新增车辆通行白名单
     *
     * @param vehicleWhiteList 车辆通行白名单
     * @return 结果
     */
    @Override
    public int insertVehicleWhiteList(VehicleWhiteList vehicleWhiteList) {
        List<VehicleWhiteList> lists = vehicleWhiteListMapper.selectlistByLicensePlateNumber(vehicleWhiteList.getLicensePlateNumber());
        if (lists.size() > 0) {
            throw new RuntimeException("对应车牌号车辆已经记录，请勿重复添加！");
        }
        vehicleWhiteList.setCreateTime(DateUtils.getNowDate());
        vehicleWhiteList.setCreateBy(SecurityUtils.getUsername());
        return vehicleWhiteListMapper.insertVehicleWhiteList(vehicleWhiteList);
    }

    /**
     * 修改车辆通行白名单
     *
     * @param vehicleWhiteList 车辆通行白名单
     * @return 结果
     */
    @Override
    public int updateVehicleWhiteList(VehicleWhiteList vehicleWhiteList) {
        vehicleWhiteList.setUpdateTime(DateUtils.getNowDate());
        vehicleWhiteList.setUpdateBy(SecurityUtils.getUsername());
        return vehicleWhiteListMapper.updateVehicleWhiteList(vehicleWhiteList);
    }

    /**
     * 批量删除车辆通行白名单
     *
     * @param ids 需要删除的车辆通行白名单ID
     * @return 结果
     */
    @Override
    public int deleteVehicleWhiteListByIds(Integer[] ids) {
        return vehicleWhiteListMapper.deleteVehicleWhiteListByIds(ids);
    }

    /**
     * 删除车辆通行白名单信息
     *
     * @param id 车辆通行白名单ID
     * @return 结果
     */
    @Override
    public int deleteVehicleWhiteListById(Integer id) {
        return vehicleWhiteListMapper.deleteVehicleWhiteListById(id);
    }
}
