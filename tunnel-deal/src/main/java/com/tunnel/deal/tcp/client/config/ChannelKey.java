package com.tunnel.deal.tcp.client.config;

/**
 * describe: 通道channel缓存key管理
 *
 * @author zs
 * @date 2021/2/26
 */
public class ChannelKey {

    /**
     * 获取channel缓存key
     * @param ip ip
     * @param port port
     * @return
     */
    public static String getChannelKey(String ip,int port){
        return ip + ":" + port;
    }
}
