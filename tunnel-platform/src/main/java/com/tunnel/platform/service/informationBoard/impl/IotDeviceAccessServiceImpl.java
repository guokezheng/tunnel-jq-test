package com.tunnel.platform.service.informationBoard.impl;

import com.tunnel.platform.domain.informationBoard.IotDeviceAccess;
import com.tunnel.platform.mapper.informationBoard.IotDeviceAccessMapper;
import com.tunnel.platform.service.informationBoard.IIotDeviceAccessService;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备接入信息Service业务层处理
 * 
 * @author yangqichao
 * @date 2020-03-25
 */
@Service("iIotDeviceAccessService")
public class IotDeviceAccessServiceImpl implements IIotDeviceAccessService
{
    @Autowired
    private IotDeviceAccessMapper iotDeviceAccessMapper;

    /**
     * 查询设备接入信息
     * 
     * @param deviceId 设备接入信息ID
     * @return 设备接入信息
     */
    @Override
    public IotDeviceAccess selectIotDeviceAccessById(Long deviceId)
    {
        return iotDeviceAccessMapper.selectIotDeviceAccessById(deviceId);
    }

    @Override
    public IotDeviceAccess selectIotDeviceAccessQuery(IotDeviceAccess iotDeviceAccess) {
        return iotDeviceAccessMapper.selectIotDeviceAccessQuery(iotDeviceAccess);
    }

    /**
     * 查询设备接入信息列表
     * 
     * @param iotDeviceAccess 设备接入信息
     * @return 设备接入信息
     */
    @Override
    public List<IotDeviceAccess> selectIotDeviceAccessList(IotDeviceAccess iotDeviceAccess)
    {
        return iotDeviceAccessMapper.selectIotDeviceAccessList(iotDeviceAccess);
    }

    /**
     * 新增设备接入信息
     * 
     * @param iotDeviceAccess 设备接入信息
     * @return 结果
     */
    @Override
    public int insertIotDeviceAccess(IotDeviceAccess iotDeviceAccess)
    {
        iotDeviceAccess.setCreateTime(DateUtils.getNowDate());
        return iotDeviceAccessMapper.insertIotDeviceAccess(iotDeviceAccess);
    }

    /**
     * 修改设备接入信息
     * 
     * @param iotDeviceAccess 设备接入信息
     * @return 结果
     */
    @Override
    public int updateIotDeviceAccess(IotDeviceAccess iotDeviceAccess)
    {
        iotDeviceAccess.setUpdateTime(DateUtils.getNowDate());
        return iotDeviceAccessMapper.updateIotDeviceAccess(iotDeviceAccess);
    }

    /**
     * 删除设备接入信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteIotDeviceAccessByIds(String ids)
    {
        return iotDeviceAccessMapper.deleteIotDeviceAccessByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除设备接入信息信息
     * 
     * @param deviceId 设备接入信息ID
     * @return 结果
     */
    @Override
    public int deleteIotDeviceAccessById(Long deviceId)
    {
        return iotDeviceAccessMapper.deleteIotDeviceAccessById(deviceId);
    }
}
