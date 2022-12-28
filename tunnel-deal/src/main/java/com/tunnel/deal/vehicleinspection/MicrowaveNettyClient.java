package com.tunnel.deal.vehicleinspection;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Component
public class MicrowaveNettyClient {

    private static final Logger log = LoggerFactory.getLogger(MicrowaveNettyClient.class);

    public static Map<String, NettyConnectInfo> channels = new HashMap<>();
    static EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    static Bootstrap bootstrap = null;
    static MicrowaveNettyClientHandler handler;

    public static void setHandler(MicrowaveNettyClientHandler h) {
        handler = h;
    }

    /**
     * 初始化Bootstrap
     */
    public static final Bootstrap getBootstrap(EventLoopGroup group) {
        if(bootstrap == null) {
            bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(20480, 20480, 65536))
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ByteArrayEncoder());
                            socketChannel.pipeline().addLast((ChannelHandler) handler);
                        }
                    });
        }

        return bootstrap;
    }

    // 获取所有连接
    public static void getChannel(Map<String, NettyConnectInfo> map) {
        //Map<String , ChannelFuture> result = new HashMap<>();
        Bootstrap bootstrap = getBootstrap(null);

        for (Map.Entry<String, NettyConnectInfo> entry : map.entrySet()) {
            String ip = entry.getKey();

            // 如果IP没有连接，则尝试连接
            if (channels.get(ip) == null) {
                String[] arr = ip.split(":");
                NettyConnectInfo info = entry.getValue();

                //bootstrap.remoteAddress(ip, info.getPort());
                bootstrap.remoteAddress(arr[0], Integer.valueOf(arr[1]));

                //log.info("...尝试连接{}：{}", arr[0], Integer.valueOf(arr[1]));
                ChannelFuture future = bootstrap.connect().addListener((ChannelFuture futureListener) -> {
                    //final EventLoop eventLoop = futureListener.channel().eventLoop();
                    if (!futureListener.isSuccess()) {
                        log.error("与" + ip + "连接失败!");
                    } else {
                        info.setChannel(futureListener.channel());
                        info.setActiveTime(new Timestamp(System.currentTimeMillis()));
                        channels.put(ip, info);
                    }
                });
            }
        }
    }
}
