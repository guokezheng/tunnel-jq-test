package com.zc.websocket;

import com.zc.common.constant.RedisChannelConstants;
import com.zc.common.core.redis.RedisPubSub;
import com.zc.websocket.handler.WebSocketStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * websocket 初始化器
 * 1.websocket 启动
 * 2.添加 WebSocket 消息订阅
 * @author Athena-xiepufeng
 */
@Component
public class WebSocketInitializer implements CommandLineRunner {

    @Autowired
    private WebSocketConfigParam param;

    @Autowired
    private RedisPubSub redisPubSub;

    @Override
    public void run(String... args) {

        // 添加 WebSocket 消息订阅
        redisPubSub.subscribe(RedisChannelConstants.WEBSOCKET_BROADCAST);
        redisPubSub.subscribe(RedisChannelConstants.WEBSOCKET_DIRECTIONAL);

        WebSocketStarter webSocketStarter = new WebSocketStarter(
                param.getPort(),
                param.getPath(),
                param.getPassword(),
                param.getInterval(),
                param.getTimeoutIntervals());
        // WebSocket 启动
        webSocketStarter.run();

    }
}
