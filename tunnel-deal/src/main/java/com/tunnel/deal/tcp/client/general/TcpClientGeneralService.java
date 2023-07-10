package com.tunnel.deal.tcp.client.general;

import com.tunnel.business.domain.dataInfo.SdDevices;

import java.util.Map;

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
     * @param deviceMap 设备信息缓存Map
     * @param ip 设备IP
     * @return
     */
    String getDeviceIdByIp(Map<String,Map> deviceMap,String ip);


    /**
     * 根据设备IP筛选设备ID
     * @param ip 设备IP
     * @return
     */
     String getDeviceIdByIp(String ip);


    /**
     * 设备信息缓存
     * 缓存所有配置对应协议的设备
     * @param deviceMap 设备信息缓存Map
     * @param protocolCode 协议标识
     * @param eqType 设备类型
     */
     void deviceInfoCache(Map<String, Map> deviceMap, String protocolCode, Long eqType);
}
