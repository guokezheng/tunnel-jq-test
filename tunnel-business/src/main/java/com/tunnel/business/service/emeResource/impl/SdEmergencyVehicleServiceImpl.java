package com.tunnel.business.service.emeResource.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.emeResource.SdEmergencyOrg;
import com.tunnel.business.domain.emeResource.SdEmergencyVehicle;
import com.tunnel.business.mapper.emeResource.SdEmergencyVehicleMapper;
import com.tunnel.business.service.emeResource.ISdEmergencyVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应急车辆Service业务层处理
 *
 * @author dzy
 * @date 2022-08-09
 */
@Service
public class SdEmergencyVehicleServiceImpl implements ISdEmergencyVehicleService {
    @Autowired
    private SdEmergencyVehicleMapper sdEmergencyVehicleMapper;

    /**
     * 查询应急车辆
     *
     * @param id 应急车辆主键
     * @return 应急车辆
     */
    @Override
    public SdEmergencyVehicle selectSdEmergencyVehicleById(Long id) {
        return sdEmergencyVehicleMapper.selectSdEmergencyVehicleById(id);
    }

    /**
     * 查询应急车辆列表
     *
     * @param sdEmergencyVehicle 应急车辆
     * @return 应急车辆
     */
    @Override
    public List<SdEmergencyVehicle> selectSdEmergencyVehicleList(SdEmergencyVehicle sdEmergencyVehicle) {
        List<SdEmergencyOrg> org = sdEmergencyVehicleMapper.getOrg();
        List<SdEmergencyVehicle> emergencyVehicles = sdEmergencyVehicleMapper.selectSdEmergencyVehicleList(sdEmergencyVehicle);
        for (int i = 0; i < org.size(); i++) {
            for (int i1 = 0; i1 < emergencyVehicles.size(); i1++) {
                if (String.valueOf(org.get(i).getOrgId()).equals(String.valueOf(emergencyVehicles.get(i1).getOrgId()))) {
                    emergencyVehicles.get(i1).setOrgName(org.get(i).getOrgName());
                }
            }
        }
        return emergencyVehicles;
    }

    /**
     * 新增应急车辆
     *
     * @param sdEmergencyVehicle 应急车辆
     * @return 结果
     */
    @Override
    public int insertSdEmergencyVehicle(SdEmergencyVehicle sdEmergencyVehicle) {
        sdEmergencyVehicle.setCreateTime(DateUtils.getNowDate());
        return sdEmergencyVehicleMapper.insertSdEmergencyVehicle(sdEmergencyVehicle);
    }

    /**
     * 修改应急车辆
     *
     * @param sdEmergencyVehicle 应急车辆
     * @return 结果
     */
    @Override
    public int updateSdEmergencyVehicle(SdEmergencyVehicle sdEmergencyVehicle) {
        sdEmergencyVehicle.setUpdateTime(DateUtils.getNowDate());
        return sdEmergencyVehicleMapper.updateSdEmergencyVehicle(sdEmergencyVehicle);
    }

    /**
     * 批量删除应急车辆
     *
     * @param ids 需要删除的应急车辆主键
     * @return 结果
     */
    @Override
    public int deleteSdEmergencyVehicleByIds(Long[] ids) {
        return sdEmergencyVehicleMapper.deleteSdEmergencyVehicleByIds(ids);
    }

    /**
     * 删除应急车辆信息
     *
     * @param id 应急车辆主键
     * @return 结果
     */
    @Override
    public int deleteSdEmergencyVehicleById(Long id) {
        return sdEmergencyVehicleMapper.deleteSdEmergencyVehicleById(id);
    }

    @Override
    public List<SdEmergencyOrg> getOrg() {
        return sdEmergencyVehicleMapper.getOrg();
    }
}
