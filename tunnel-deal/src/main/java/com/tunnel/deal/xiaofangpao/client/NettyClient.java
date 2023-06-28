package com.tunnel.deal.xiaofangpao.client;

import com.tunnel.deal.tcp.util.ByteBufUtil;
import com.tunnel.deal.xiaofangpao.msgEnum.SendMsgCodeEnum;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    private static final Logger log = LoggerFactory.getLogger(com.tunnel.deal.guidancelamp.control.NettyClient.class);

    /**
     * 传入的重连次数
     */
    private int reconnectTimes;

    public static NettyClient getInstance(){
        return NettyClient.NettyClientHolder.instance;
    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
     */
    private static class NettyClientHolder{
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static NettyClient instance = new NettyClient();
    }

    public void init(int times){
        this.reconnectTimes = times;
        try {
            start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private int port = 8001;
    private String host="127.0.0.1";

    public void start() throws InterruptedException {
        try{
            Bootstrap bootstrap = new Bootstrap();
            // 客户端不需要处理连接 所以一个线程组就够了
            bootstrap.group(workerGroup)
                    // 连接通道
                    .channel(NioSocketChannel.class)
                    .remoteAddress(host, port)
                    .option(ChannelOption.TCP_NODELAY, true)
                    // 数据处理
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect();
            //客户端断线重连逻辑
            future.addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    log.info("连接Netty服务端成功");
                } else {
                    log.info("连接失败，进行断线重连");
                    future1.channel().eventLoop().schedule(() -> {
                        try {
                            start();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }, 20, TimeUnit.SECONDS);
                }
            });

            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(() -> {
                // 定时发送消息给服务端
                String systemMsg = SendMsgCodeEnum.XIAOFANGPAO_SYSTEM_STATUS_CODE.getCode();
                try {
                    future.channel().writeAndFlush(ByteBufUtil.convertStringToByteBuf(systemMsg.replace(" ","")));
                } catch (DecoderException e) {
                    e.printStackTrace();
                }
            }, 60, 60, TimeUnit.SECONDS);
            future.channel().closeFuture().sync();
        }catch (Exception e){
            log.info("服务端异常");
        }finally {
            //workerGroup.shutdownGracefully();
        }
    }

    /*public static void main(String[] args) throws InterruptedException {
        new NettyClient().start();
    }*/
}
