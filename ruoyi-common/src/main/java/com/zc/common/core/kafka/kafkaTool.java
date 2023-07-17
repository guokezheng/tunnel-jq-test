package com.zc.common.core.kafka;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.zc.common.core.stream.RedisStream;
import com.zc.common.core.websocket.WebSocketService;
import io.netty.channel.group.ChannelGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * 小车运行给指定的客户端发送请求
     */
    public static void sendAssignWebSocket(String Token,List<Map> list, JSONObject object){
       try{
           // 给指定客户端发送消息
           WebSocketService.catPostEvent(Token,"radarDataList",object.toString());
       }catch (Exception e){

       }

    }
}
