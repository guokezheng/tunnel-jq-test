package com.tunnel.business.service.informationBoard;


import com.tunnel.business.domain.informationBoard.IotDeviceAccess;

import java.util.List;

/**
 * 设备接入信息Service接口
 *
 * @author yangqichao
 * @date 2020-03-25
 */
public interface IIotDeviceAccessService {
    /**
     * 查询设备接入信息
     *
     * @param deviceId 设备接入信息ID
     * @return 设备接入信息
     */
    IotDeviceAccess selectIotDeviceAccessById(Long deviceId);

    /**
     * 查询设备接入信息
     *
     * @param iotDeviceAccess 设备接入信息ID
     * @return 设备接入信息
     */
    IotDeviceAccess selectIotDeviceAccessQuery(IotDeviceAccess iotDeviceAccess);

    /**
     * 查询设备接入信息列表
     *
     * @param iotDeviceAccess 设备接入信息
     * @return 设备接入信息集合
     */
    List<IotDeviceAccess> selectIotDeviceAccessList(IotDeviceAccess iotDeviceAccess);

    /**
     * 新增设备接入信息
     *
     * @param iotDeviceAccess 设备接入信息
     * @return 结果
     */
    int insertIotDeviceAccess(IotDeviceAccess iotDeviceAccess);

    /**
     * 修改设备接入信息
     *
     * @param iotDeviceAccess 设备接入信息
     * @return 结果
     */
    int updateIotDeviceAccess(IotDeviceAccess iotDeviceAccess);

    /**
     * 批量删除设备接入信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteIotDeviceAccessByIds(String ids);

    /**
     * 删除设备接入信息信息
     *
     * @param deviceId 设备接入信息ID
     * @return 结果
     */
    int deleteIotDeviceAccessById(Long deviceId);
}
