package com.tunnel.business.service.platformAuthApi;

import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdTunnels;

import java.util.List;

/**
 * @author zhai
 * @date 2022/10/25
 */
public interface PlatformApiService {


    /**
     * http推送设备数据
     *
     * @param sdDevicesList
     * @return
     */
    void devicesPush(List<SdDevices> sdDevicesList, String pushType, String userName);

    /**
     * http推送隧道数据
     *
     * @param sdTunnelsList
     * @return
     */
    void tunnelsPush(List<SdTunnels> sdTunnelsList, String pushType);

    /**
     * 新增设备(接收)
     *
     * @param sdDevicesList 设备集合(接收)
     * @return 结果
     */
    int insertSdDevices(List<SdDevices> sdDevicesList);

    /**
     * 修改设备(接收)
     *
     * @param sdDevicesList 设备集合(接收)
     * @return 结果
     */
    int updateSdDevices(List<SdDevices> sdDevicesList);

    /**
     * 删除设备(接收)
     *
     * @param sdDevicesList 需要删除的设备ID(接收)
     * @return 结果
     */
    int deleteSdDevices(List<SdDevices> sdDevicesList);

    /**
     * 导入设备(接收)
     *
     * @param sdDevicesList 设备集合(接收)
     * @return 结果
     */
    int importSdDevices(List<SdDevices> sdDevicesList,String userName);

    /**
     * 新增隧道
     *
     * @param sdTunnelsList 隧道集合(接收)
     * @return 结果
     */
    int insertSdTunnels(List<SdTunnels> sdTunnelsList);

    /**
     * 修改隧道
     *
     * @param sdTunnelsList 隧道集合(接收)
     * @return 结果
     */
    int updateSdTunnels(List<SdTunnels> sdTunnelsList);

    /**
     * 批量删除隧道
     *
     * @param sdTunnelsList 需要删除的隧道ID(接收)
     * @return 结果
     */
    int deleteSdTunnelsByIds(List<SdTunnels> sdTunnelsList);
}
