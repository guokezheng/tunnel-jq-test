package com.tunnel.deal.tcp.client.general;

import com.alibaba.fastjson.JSONObject;

/**
 * describe: TCP协议公共接口
 *
 * @author zs
 * @date 2023/6/26
 */
public interface TcpClientGeneralBean {
//
//    /**
//     * 创建链接
//     */
//    void connect();
//
//    /**
//     * 发送控制指令
//     */
//    void sendControlCmd();
//
//    /**
//     * 发送查询指令
//     */
//    void sendQueryCmd();

    /**
     * 解析读取的数据
     * @param ip 网关设备IP
     * @param deviceId 网关设备ID
     * @param jsonObject 读取的数据
     */
    void handleReadData(String ip, String deviceId, JSONObject jsonObject);
}
