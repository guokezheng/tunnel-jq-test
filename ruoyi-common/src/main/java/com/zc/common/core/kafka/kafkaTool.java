package com.zc.common.core.kafka;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.zc.common.core.stream.RedisStream;
import com.zc.common.core.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * kafka工具类
 * @author he
 */
@Service
public class kafkaTool {

    private static final RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    /**
     * 给指定的客户端发送请求
     */
    public static void sendAssignWebSocket(List<Map> list, JSONObject object){
        try {
            //获取所有需要发送消息客服端的token
            List<String> scanKey = redisCache.getCacheList("caKokenList");
            for (String key :scanKey){
                //获取隧道以及是否推送
                Map<String, Object> cacheMap = redisCache.getCacheMap(key);
                //推送的token
                String s = key.replaceAll(Constants.CAR_TOKEN, "");
                //获取当前隧道tunnelId
                String tunnelId = (String)list.get(0).get("tunnelId");
                for(String keys : cacheMap.keySet()){
                    //判断是否可以推送  && 前端可以推送的隧道是否和当前隧道匹配
                    if("0".equals(cacheMap.get(keys))&&tunnelId.equals(keys)){
                        // 给指定客户端发送消息
                        WebSocketService.postEvent(s,"radarDataList",object.toString());
                    }
                }
            }
        }catch(Exception e){
        }
    }
}
