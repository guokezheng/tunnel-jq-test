package com.tunnel.business.mapper.informationBoard;


import com.tunnel.business.domain.informationBoard.IotDeviceAccess;

import java.util.List;

/**
 * 设备接入信息Mapper接口
 * 
 * @author yangqichao
 * @date 2020-03-25
 */
public interface IotDeviceAccessMapper 
{
    /**
     * 查询设备接入信息
     * 
     * @param deviceId 设备接入信息ID
     * @return 设备接入信息
     */
    public IotDeviceAccess selectIotDeviceAccessById(Long deviceId);

    /**
     * 查询设备接入信息
     *
     * @param iotDeviceAccess 设备接入信息ID
     * @return 设备接入信息
     */
    public IotDeviceAccess selectIotDeviceAccessQuery(IotDeviceAccess iotDeviceAccess);

    /**
     * 查询设备接入信息列表
     * 
     * @param iotDeviceAccess 设备接入信息
     * @return 设备接入信息集合
     */
    public List<IotDeviceAccess> selectIotDeviceAccessList(IotDeviceAccess iotDeviceAccess);

    /**
     * 新增设备接入信息
     * 
     * @param iotDeviceAccess 设备接入信息
     * @return 结果
     */
    public int insertIotDeviceAccess(IotDeviceAccess iotDeviceAccess);

    /**
     * 修改设备接入信息
     * 
     * @param iotDeviceAccess 设备接入信息
     * @return 结果
     */
    public int updateIotDeviceAccess(IotDeviceAccess iotDeviceAccess);

    /**
     * 删除设备接入信息
     * 
     * @param deviceId 设备接入信息ID
     * @return 结果
     */
    public int deleteIotDeviceAccessById(Long deviceId);

    /**
     * 批量删除设备接入信息
     * 
     * @param deviceIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteIotDeviceAccessByIds(String[] deviceIds);
}
