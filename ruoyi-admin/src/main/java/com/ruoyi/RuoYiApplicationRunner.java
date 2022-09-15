package com.ruoyi;

import com.tunnel.platform.firealarm.FireNettyServer;
import com.zc.common.core.ThreadPool.ThreadPool;
import com.zc.common.constant.RedisChannelConstants;
import com.zc.common.core.redis.RedisPubSub;
import com.zc.connect.config.NettyServerConfigAttr;
import com.zc.connect.config.NettyServerConfig;
import com.zc.connect.nettyServer.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther Athena-YangChao
 * @date 2021/11/17/017
 * @apiNote 启动类
 */
@Component
public class RuoYiApplicationRunner  implements ApplicationRunner {

    @Autowired
    private NettyServerConfig nettyConfig;

    @Autowired
    private RedisPubSub redisPubSub;

    @Autowired
    private FireNettyServer fireNettyServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 消息订阅（设置属性，获取属性，服务调用）
        redisPubSub.subscribe(RedisChannelConstants.IOT_SET_ATTRIBUTE);
        redisPubSub.subscribe(RedisChannelConstants.IOT_GET_ATTRIBUTE);
        redisPubSub.subscribe(RedisChannelConstants.IOT_SERVE_INVOKE);
        redisPubSub.subscribe(RedisChannelConstants.IOT_SET_ATTRIBUTE_CHILD_DEVICE);
        redisPubSub.subscribe(RedisChannelConstants.IOT_GET_ATTRIBUTE_CHILD_DEVICE);
        redisPubSub.subscribe(RedisChannelConstants.IOT_SERVE_INVOKE_CHILD_DEVICE);


        for (NettyServerConfigAttr configAttr : nettyConfig.getConfig()){

            if(configAttr.getEnabled()){
                String handler_name = configAttr.getHandler_name();
                Object handlerClass = Class.forName(handler_name).newInstance();
                // 线程开启
                ThreadPool.executor.execute(() -> {
                    try {
                        // 启动netty服务
                        new NettyServer(configAttr.getPort(),handlerClass).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }

        List<Integer> portList = new ArrayList<Integer>();
        List<String> handlerList = new ArrayList<String>();
        // 消防设备通信
        portList.add(60000);
        handlerList.add("com.tunnel.platform.firealarm.FireNettyServerHandler");
        fireNettyServer.setPortList(portList);
        fireNettyServer.setHandlerList(handlerList);
        new Thread(fireNettyServer).start();
    }
}
