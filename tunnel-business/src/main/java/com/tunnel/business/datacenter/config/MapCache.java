package com.tunnel.business.datacenter.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapCache {
    public static ConcurrentHashMap<String, String> sessionCache = new ConcurrentHashMap<>();

}
