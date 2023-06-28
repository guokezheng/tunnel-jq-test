package com.tunnel.deal.tcp.client.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * describe: 设备列表缓存数据
 * 将HashMap替换为线程安全类ConcurrentHashMap
 *
 * @author zs
 * @date 2021/2/26
 */
public class DeviceManager {

    /**
     * 存储设备数据，key为deviceId,value为设备数据【ip,port】
     */
    public static Map<String,Map> deviceMap = new ConcurrentHashMap<>();

    public static Map<String,Map> devicePointMap = new ConcurrentHashMap<>();
}
