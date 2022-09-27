package com.tunnel.business.domain.digitalmodel;

import java.util.HashMap;
import java.util.Map;

/**
 * describe: 隧道方向映射
 *  万集定义的隧道方向与平台的隧道方向映射
 * @author zs
 * @date 2022/9/27
 */
public class EventDirectionMap {

    /**
     * 万集：隧道方向 1北向-上行 2南向-下行
     * 平台：1--上行，0--下行
     */

    public static final Map<String,String>  DIRECTION_MAP = new HashMap<>();

    static{
        DIRECTION_MAP.put("1","0");
        DIRECTION_MAP.put("2","1");
    }

}
