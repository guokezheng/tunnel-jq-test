package com.tunnel.business.service.event.impl;

import com.tunnel.business.domain.event.SdEmergencyDevice;
import com.tunnel.business.service.event.ISdEmergencyDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 应急物资信息Service业务层处理
 *
 * @author ruoyi
 * @date 2021-05-12
 */
@Service
public class SdEmergencyDeviceServiceImpl implements ISdEmergencyDeviceService {
    @Autowired
    private com.tunnel.business.mapper.event.SdEmergencyDeviceMapper SdEmergencyDeviceMapper;

    /**
     * 查询应急物资信息
     *
     * @param id 应急物资信息ID
     * @return 应急物资信息
     */
    @Override
    public SdEmergencyDevice selectSdEmergencyDeviceById(Long id) {
        return SdEmergencyDeviceMapper.selectSdEmergencyDeviceById(id);
    }

    /**
     * 查询应急物资信息列表
     *
     * @param SdEmergencyDevice 应急物资信息
     * @return 应急物资信息
     */
    @Override
    public List<SdEmergencyDevice> selectSdEmergencyDeviceList(SdEmergencyDevice SdEmergencyDevice) {
        return SdEmergencyDeviceMapper.selectSdEmergencyDeviceList(SdEmergencyDevice);
    }

    /**
     * 新增应急物资信息
     *
     * @param SdEmergencyDevice 应急物资信息
     * @return 结果
     */
    @Override
    public int insertSdEmergencyDevice(SdEmergencyDevice SdEmergencyDevice) {
        return SdEmergencyDeviceMapper.insertSdEmergencyDevice(SdEmergencyDevice);
    }

    /**
     * 修改应急物资信息
     *
     * @param SdEmergencyDevice 应急物资信息
     * @return 结果
     */
    @Override
    public int updateSdEmergencyDevice(SdEmergencyDevice SdEmergencyDevice) {
        return SdEmergencyDeviceMapper.updateSdEmergencyDevice(SdEmergencyDevice);
    }

    /**
     * 批量删除应急物资信息
     *
     * @param ids 需要删除的应急物资信息ID
     * @return 结果
     */
    @Override
    public int deleteSdEmergencyDeviceByIds(Long[] ids) {
        return SdEmergencyDeviceMapper.deleteSdEmergencyDeviceByIds(ids);
    }

    /**
     * 删除应急物资信息信息
     *
     * @param id 应急物资信息ID
     * @return 结果
     */
    @Override
    public int deleteSdEmergencyDeviceById(Long id) {
        return SdEmergencyDeviceMapper.deleteSdEmergencyDeviceById(id);
    }
}
