package com.tunnel.business.service.digitalmodel.impl;

import com.tunnel.business.domain.digitalmodel.SdSpecialVehicles;
import com.tunnel.business.mapper.digitalmodel.SdSpecialVehiclesMapper;
import com.tunnel.business.service.digitalmodel.ISdSpecialVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 重点车辆Service业务层处理
 *
 * @author ruoyi
 * @date 2022-09-04
 */
@Service
public class SdSpecialVehicleServiceImpl implements ISdSpecialVehicleService {
    @Autowired
    private SdSpecialVehiclesMapper sdSpecialVehicleMapper;

    /**
     * 查询重点车辆
     *
     * @param id 重点车辆主键
     * @return 重点车辆
     */
    @Override
    public SdSpecialVehicles selectSdSpecialVehicleById(String id) {
        return sdSpecialVehicleMapper.selectSdSpecialVehicleById(id);
    }

    /**
     * 查询重点车辆列表
     *
     * @param sdSpecialVehicle 重点车辆
     * @return 重点车辆
     */
    @Override
    public List<Map<String,String>> selectSdSpecialVehicleList(SdSpecialVehicles sdSpecialVehicle) {
        return sdSpecialVehicleMapper.selectSdSpecialVehicleList(sdSpecialVehicle);
    }

    /**
     * 新增重点车辆
     *
     * @param sdSpecialVehicle 重点车辆
     * @return 结果
     */
    @Override
    public int insertSdSpecialVehicle(SdSpecialVehicles sdSpecialVehicle) {
        return sdSpecialVehicleMapper.insertSdSpecialVehicle(sdSpecialVehicle);
    }

    /**
     * 修改重点车辆
     *
     * @param sdSpecialVehicle 重点车辆
     * @return 结果
     */
    @Override
    public int updateSdSpecialVehicle(SdSpecialVehicles sdSpecialVehicle) {
        return sdSpecialVehicleMapper.updateSdSpecialVehicle(sdSpecialVehicle);
    }

    /**
     * 批量删除重点车辆
     *
     * @param ids 需要删除的重点车辆主键
     * @return 结果
     */
    @Override
    public int deleteSdSpecialVehicleByIds(String[] ids) {
        return sdSpecialVehicleMapper.deleteSdSpecialVehicleByIds(ids);
    }

    /**
     * 删除重点车辆信息
     *
     * @param id 重点车辆主键
     * @return 结果
     */
    @Override
    public int deleteSdSpecialVehicleById(String id) {
        return sdSpecialVehicleMapper.deleteSdSpecialVehicleById(id);
    }

    /**
     * 根据隧道id 查询24小时 重点车辆
     *
     * @param tunnelId
     * @return
     */
    @Override
    public Object[] specialById(String tunnelId) {
        List<Map> maps = sdSpecialVehicleMapper.specialById(tunnelId);
        ArrayList<Object> time = new ArrayList<>();
        ArrayList<Object> num = new ArrayList<>();
        Object[] resArr = new Object[2];
        for (Map map : maps) {
            Object timeData = map.get("time");
            time.add(timeData);
            Object numData = map.get("num");
            num.add(numData);
        }
        resArr[0] = time;
        resArr[1] = num;
        return resArr;
    }
}
