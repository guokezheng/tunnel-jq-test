package com.tunnel.deal.xiaofangpao.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ConnectionListener implements ChannelFutureListener {

    private static final Logger log = LoggerFactory.getLogger(ConnectionListener.class);

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (!channelFuture.isSuccess()) {
            log.error("-------------消防炮客户端重新连接-----------------");
            final EventLoop loop = channelFuture.channel().eventLoop();
            loop.schedule(new Runnable() {
                @Override
                public void run() {
                    NettyClient.getInstance().connect();
                }
            }, 30L, TimeUnit.SECONDS);
        }
    }

}
