package com.tunnel.platform.service.dataInfo.impl;

import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.platform.domain.dataInfo.SdDeviceChange;
import com.tunnel.platform.mapper.dataInfo.SdDeviceChangeMapper;
import com.tunnel.platform.service.dataInfo.ISdDeviceChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备变更Service业务层处理
 *
 * @author 刘方堃
 * @date 2021-12-09
 */
@Service
public class SdDeviceChangeServiceImpl implements ISdDeviceChangeService
{
    @Autowired
    private SdDeviceChangeMapper sdDeviceChangeMapper;

    /**
     * 查询设备变更
     *
     * @param id 设备变更主键
     * @return 设备变更
     */
    @Override
    public SdDeviceChange selectSdDeviceChangeById(Long id)
    {
        return sdDeviceChangeMapper.selectSdDeviceChangeById(id);
    }

    /**
     * 查询设备变更列表
     *
     * @param sdDeviceChange 设备变更
     * @return 设备变更
     */
    @Override
    public List<SdDeviceChange> selectSdDeviceChangeList(SdDeviceChange sdDeviceChange)
    {
        Long deptId = SecurityUtils.getDeptId();
        sdDeviceChange.getParams().put("deptId",deptId);
        return sdDeviceChangeMapper.selectSdDeviceChangeList(sdDeviceChange);
    }

    /**
     * 新增设备变更
     *
     * @param sdDeviceChange 设备变更
     * @return 结果
     */
    @Override
    public int insertSdDeviceChange(SdDeviceChange sdDeviceChange)
    {
        return sdDeviceChangeMapper.insertSdDeviceChange(sdDeviceChange);
    }

    /**
     * 修改设备变更
     *
     * @param sdDeviceChange 设备变更
     * @return 结果
     */
    @Override
    public int updateSdDeviceChange(SdDeviceChange sdDeviceChange)
    {
        return sdDeviceChangeMapper.updateSdDeviceChange(sdDeviceChange);
    }

    /**
     * 批量删除设备变更
     *
     * @param ids 需要删除的设备变更主键
     * @return 结果
     */
    @Override
    public int deleteSdDeviceChangeByIds(Long[] ids)
    {
        return sdDeviceChangeMapper.deleteSdDeviceChangeByIds(ids);
    }

    /**
     * 删除设备变更信息
     *
     * @param id 设备变更主键
     * @return 结果
     */
    @Override
    public int deleteSdDeviceChangeById(Long id)
    {
        return sdDeviceChangeMapper.deleteSdDeviceChangeById(id);
    }
}
