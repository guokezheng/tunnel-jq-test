package com.tunnel.deal.tcp.client.general;

import com.tunnel.business.domain.dataInfo.SdDevices;

/**
 * describe: tcp协议通用业务类
 *
 * @author zs
 * @date 2023/6/26
 */
public interface TcpClientGeneralService {

    /**
     * 根据设备ID获得对应的协议类对象
     * @param sdDevices 设备对象
     * @return
     */
    TcpClientGeneralBean getProtocolBean(SdDevices sdDevices);


    /**
     * 根据设备IP筛选设备ID
     * @param ip 设备IP
     * @return
     */
     String getDeviceIdByIp(String ip);


    /**
     * 设备信息缓存
     * 缓存所有配置测控执行器协议的设备（类型为测控执行器）
     * @param protocolCode 协议标识
     * @param eqType 设备类型
     */
     void deviceInfoCache(String protocolCode,Long eqType);
}
