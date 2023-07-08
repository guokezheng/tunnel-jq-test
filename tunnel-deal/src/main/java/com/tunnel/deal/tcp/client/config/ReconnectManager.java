package com.tunnel.deal.tcp.client.config;

import java.util.HashMap;
import java.util.Map;

/**
 * describe: 重连次数缓存类
 *
 * @author zs
 * @date 2021/2/21
 */
public class ReconnectManager {

    /**
     * 存储ip+port、重连次数
     */
    public static Map<String,Integer> countMap = new HashMap<>();
}
