package com.tunnel.business.service.protocol.impl;

import java.util.List;

import com.tunnel.business.domain.protocol.SdDevicePointState;
import com.tunnel.business.mapper.protocol.SdDevicePointStateMapper;
import com.tunnel.business.service.protocol.ISdDevicePointStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备点位状态Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-11
 */
@Service
public class SdDevicePointStateServiceImpl implements ISdDevicePointStateService
{
    @Autowired
    private SdDevicePointStateMapper sdDevicePointStateMapper;

    /**
     * 查询设备点位状态
     *
     * @param id 设备点位状态主键
     * @return 设备点位状态
     */
    @Override
    public SdDevicePointState selectSdDevicePointStateById(Long id)
    {
        return sdDevicePointStateMapper.selectSdDevicePointStateById(id);
    }

    /**
     * 查询设备点位状态列表
     *
     * @param sdDevicePointState 设备点位状态
     * @return 设备点位状态
     */
    @Override
    public List<SdDevicePointState> selectSdDevicePointStateList(SdDevicePointState sdDevicePointState)
    {
        return sdDevicePointStateMapper.selectSdDevicePointStateList(sdDevicePointState);
    }

    /**
     * 新增设备点位状态
     *
     * @param sdDevicePointState 设备点位状态
     * @return 结果
     */
    @Override
    public int insertSdDevicePointState(SdDevicePointState sdDevicePointState)
    {
        return sdDevicePointStateMapper.insertSdDevicePointState(sdDevicePointState);
    }

    /**
     * 修改设备点位状态
     *
     * @param sdDevicePointState 设备点位状态
     * @return 结果
     */
    @Override
    public int updateSdDevicePointState(SdDevicePointState sdDevicePointState)
    {
        return sdDevicePointStateMapper.updateSdDevicePointState(sdDevicePointState);
    }

    /**
     * 批量删除设备点位状态
     *
     * @param ids 需要删除的设备点位状态主键
     * @return 结果
     */
    @Override
    public int deleteSdDevicePointStateByIds(Long[] ids)
    {
        return sdDevicePointStateMapper.deleteSdDevicePointStateByIds(ids);
    }

    /**
     * 删除设备点位状态信息
     *
     * @param id 设备点位状态主键
     * @return 结果
     */
    @Override
    public int deleteSdDevicePointStateById(Long id)
    {
        return sdDevicePointStateMapper.deleteSdDevicePointStateById(id);
    }
}
