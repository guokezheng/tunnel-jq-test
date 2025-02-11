package com.zc.websocket;
import com.zc.common.constant.RedisStreamConstants;
import com.zc.common.core.stream.RedisStream;
import com.zc.websocket.handler.WebSocketStreamCatListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.stream.StreamInfo;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * redis队列定时任务
 */
@Component
public class ConsumerGroupMonitor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisStream redisStream;


    @Resource
    private WebSocketStreamCatListener webSocketStreamCatListener;

    //redis队列经常性挂掉 检测死掉 然后重启
    @Scheduled(fixedRate = 30000)
    public void checkConsumerGroup() {
        StreamInfo.XInfoGroups groups = redisTemplate.opsForStream().groups(RedisStreamConstants.WebSocketCatDirectional.KEY);
        int i = groups.groupCount();
        Long pendingMessages = redisTemplate.opsForStream().size(RedisStreamConstants.WebSocketCatDirectional.KEY);

        System.out.println("未消费的消息数量：" + pendingMessages);
        if(i==0||pendingMessages>2000){
            redisStream.subscription(
                    RedisStreamConstants.WebSocketCatDirectional.KEY,
                    RedisStreamConstants.WebSocketCatDirectional.GROUP,
                    RedisStreamConstants.WebSocketCatDirectional.CONSUMER,
                    webSocketStreamCatListener);
        }
//        StreamInfo.XInfoGroups groups1 = redisTemplate.opsForStream().groups(RedisStreamConstants.WebSocketCatDirectional.GROUP);
        System.out.print(groups);
    }
}