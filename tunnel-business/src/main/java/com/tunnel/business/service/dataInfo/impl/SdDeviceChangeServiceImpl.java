package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdDeviceChange;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.SdDeviceChangeMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备变更Service业务层处理
 *
 * @author 刘方堃
 * @date 2021-12-09
 */
@Service
public class SdDeviceChangeServiceImpl implements ISdDeviceChangeService {
    @Autowired
    private SdDeviceChangeMapper sdDeviceChangeMapper;
    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    /**
     * 查询设备变更
     *
     * @param id 设备变更主键
     * @return 设备变更
     */
    @Override
    public SdDeviceChange selectSdDeviceChangeById(Long id) {
        return sdDeviceChangeMapper.selectSdDeviceChangeById(id);
    }

    /**
     * 查询设备变更列表
     *
     * @param sdDeviceChange 设备变更
     * @return 设备变更
     */
    @Override
    public List<SdDeviceChange> selectSdDeviceChangeList(SdDeviceChange sdDeviceChange) {
        Long userId = SecurityUtils.getUserId();
        if (userId != 0L) {
            boolean admin = SecurityUtils.isAdmin(userId);
            if (admin) {
                return sdDeviceChangeMapper.selectSdDeviceChangeList(sdDeviceChange);
            }
            Long deptId = SecurityUtils.getDeptId();
            sdDeviceChange.getParams().put("deptId", deptId);
        }
        return null;
    }

    /**
     * 新增设备变更
     *
     * @param sdDeviceChange 设备变更
     * @return 结果
     */
    @Override
    public int insertSdDeviceChange(SdDeviceChange sdDeviceChange) {
        SdDevices sdDevices = sdDevicesMapper.selectSdDevicesById(sdDeviceChange.getDeviceId());
        if (sdDevices == null) {
            throw new RuntimeException("设备ID不存在，请核对后再添加！");
        }
        return sdDeviceChangeMapper.insertSdDeviceChange(sdDeviceChange);
    }

    /**
     * 修改设备变更
     *
     * @param sdDeviceChange 设备变更
     * @return 结果
     */
    @Override
    public int updateSdDeviceChange(SdDeviceChange sdDeviceChange) {
        return sdDeviceChangeMapper.updateSdDeviceChange(sdDeviceChange);
    }

    /**
     * 批量删除设备变更
     *
     * @param ids 需要删除的设备变更主键
     * @return 结果
     */
    @Override
    public int deleteSdDeviceChangeByIds(Long[] ids) {
        return sdDeviceChangeMapper.deleteSdDeviceChangeByIds(ids);
    }

    /**
     * 删除设备变更信息
     *
     * @param id 设备变更主键
     * @return 结果
     */
    @Override
    public int deleteSdDeviceChangeById(Long id) {
        return sdDeviceChangeMapper.deleteSdDeviceChangeById(id);
    }
}
