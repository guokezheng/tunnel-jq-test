package com.tunnel.deal.tcp.client.general;

/**
 * describe: TCP协议公共接口
 *
 * @author zs
 * @date 2023/6/26
 */
public interface TcpClientGeneralBean {

    /**
     * 解析读取的数据
     * @param ip 网关设备IP
     * @param deviceId 网关设备ID
     * @param msg 读取的数据
     */
    void handleReadData(String ip, String deviceId, String msg);
}
