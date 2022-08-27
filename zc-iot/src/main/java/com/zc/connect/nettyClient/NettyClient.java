package com.zc.connect.nettyClient;


import com.zc.connect.util.StringUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @program: netty
 * @description netty4的客户端
 * @author Athena-YangChao
 * @create: 2021-11-16
 **/
@Component
public class NettyClient {
    private static final Logger log = LoggerFactory.getLogger(NettyClient.class);

    /**
     * 用于记录和管理所有客户端的Channel
     * */
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 服务类
     */
    private static Bootstrap bootstrap = new Bootstrap();


    private static NioEventLoopGroup group = new NioEventLoopGroup();

    /**
     * 退出后释放连接（防止服务器重启造成的channel脏数据）
     */
    @PreDestroy
    public void setEmpty() {
        log.info("释放设备连接!");
        //清空连接缓存
        clients.clear();
        //释放线程资源
        group.shutdownGracefully();
        log.info("释放设备连接");
    }

    @PostConstruct
    private void init() {
        //设置线程池
        bootstrap.group(group);
        //设置socket工厂、
        bootstrap.channel(NioSocketChannel.class);
        //设置管道
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new StringEncoder());
                ch.pipeline().addLast(new ClientHandler());
            }
        });
    }

    /**
     * 测试方法
     * */
    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 9000;
        String data = "123123";
        // 创建连接
        ChannelFuture connect = bootstrap.connect(ip, port);
        // 异步监听器
        connect.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isDone()) {
                    if (future.isSuccess()) {
                        // 当客户端打开链接后，获取客户端的Channel并且添加Channel至ChannelGroup中进行管理
                        clients.add(connect.channel());
                        //netty需要用ByteBuf传输
                        ByteBuf bufff = Unpooled.buffer();
                        // 字符串传输
                        bufff.writeBytes(StringUtil.hexString2Bytes(data));
                        connect.channel().writeAndFlush(bufff);
                        // 任务执行成功
                    } else if (future.isCancelled()) {
                        //任务被取消...
                    } else if (future.cause() != null) {
                        //执行出错
                    }
                }
            }
        });
    }

    /**
     * 下发指令执行方法
     */
    public void clientOrderSend(String ip, int port, String order, String handler_name){
        // 创建连接
        ChannelFuture connect = bootstrap.connect(ip, port);
        // 异步监听器
        connect.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isDone()) {
                    if (future.isSuccess()) {
                        // 当客户端打开链接后，获取客户端的Channel并且添加Channel至ChannelGroup中进行管理
                        clients.add(connect.channel());
                        // 将处理类放到链接中
                        connect.channel().attr(AttributeKey.valueOf("channel.property.key")).set(handler_name);
                        //netty需要用ByteBuf传输
                        ByteBuf bufff = Unpooled.buffer();
                        // 字符串传输
                        bufff.writeBytes(StringUtil.hexString2Bytes(order));
                        connect.channel().writeAndFlush(bufff);
                        // 任务执行成功
                    } else if (future.isCancelled()) {
                        //任务被取消...
                    } else if (future.cause() != null) {
                        //执行出错
                    }
                }
            }
        });


    }

}
