package com.zc.websocket.handler;

import com.google.auto.service.AutoService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zc.common.constant.RedisChannelConstants;
import com.zc.common.core.redis.RedisMessageDispatcher;
import org.springframework.data.redis.connection.Message;

/**
 * 处理接收 Redis 缓存订阅消息
 * @author Athena-xiepufeng
 */
@AutoService(RedisMessageDispatcher.class)
public class RedisMsgReceivedHandle implements RedisMessageDispatcher
{

    @Override
    public void onMessage(Message message, byte[] pattern)
    {

        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        JsonElement jsonElement = JsonParser.parseString(body);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement subscriber = jsonObject.get("subscriber");
        JsonObject cmdMsg = jsonObject.getAsJsonObject("cmdMsg");

        if (cmdMsg == null) return;

        if (RedisChannelConstants.WEBSOCKET_BROADCAST.equals(channel))
        {
            // 广播消息
            WebSocketService.postEvent(cmdMsg.toString());
            return;
        }

        if (RedisChannelConstants.WEBSOCKET_DIRECTIONAL.equals(channel) && subscriber != null)
        {
            // 定向发送消息
            WebSocketService.postEvent(subscriber.getAsString(), cmdMsg.toString());
        }

    }

    /**
     * 判断订阅事件是否存在
     * @param channel
     * @return
     */
    @Override
    public boolean isChannelExist(String channel)
    {
        return RedisChannelConstants.WEBSOCKET_BROADCAST.equals(channel) || RedisChannelConstants.WEBSOCKET_DIRECTIONAL.equals(channel);
    }
}
