package com.zc.common.core.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.ServiceLoader;

/**
 * Redis 订阅消息监听器
 * @author Athena-xiepufeng
 */
@Component
public class RedisReceiver implements MessageListener
{

    @Override
    public void onMessage(Message message, byte[] pattern)
    {
        // 1.获取所有接口实现
        ServiceLoader<RedisMessageDispatcher> load = ServiceLoader.load(RedisMessageDispatcher.class);

        // 2.遍历所有实现类根据订阅信息分发消息
        load.forEach(redisMessageDispatcher ->
        {
            String channel = new String(message.getChannel());

            // 3.调用isChannelExist方法判断是否有当前订阅，有则发消息。
            if (redisMessageDispatcher.isChannelExist(channel))
            {
                redisMessageDispatcher.onMessage(message, pattern);
            }
        });
    }
}
