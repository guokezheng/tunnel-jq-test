package com.zc.common.core.redis;

import org.springframework.data.redis.connection.Message;

/**
 * redis 消息分发器
 * @author Athena-xiepufeng
 */
public interface RedisMessageDispatcher {

    void onMessage(Message message, byte[] pattern);

    boolean isChannelExist(String channel);
}
