package com.tunnel.platform.pcl.netty.client;


import com.tunnel.platform.pcl.netty.connet.MyDecoder;
import com.tunnel.platform.utils.util.StringUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;

public class NettyClient {

    private static final Logger log = LoggerFactory.getLogger(NettyClient.class);

    private final Bootstrap bootstrap = new Bootstrap();

    private final NioEventLoopGroup group = new NioEventLoopGroup();

    private final String host;

    private final int port;

    private Channel channel;

    private ClientHandler clientHandler;

    @PostConstruct
    private void init() {
        log.info("初始化");
        //设置线程池
        bootstrap.group(group);
        //设置socket工厂
        bootstrap.channel(NioSocketChannel.class);
        //设置管道
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new MyDecoder());// 字符串解码器
                ch.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));// 字符串编码器
                // 业务处理器
                clientHandler = new ClientHandler(group);
                ch.pipeline().addLast(clientHandler);
            }
        });
    }


    public void start() throws Exception {
        startReturnString();
    }


    public ClientHandler getClientHandler() {
        return clientHandler;
    }


    public void startReturnString() throws InterruptedException {
        //发起异步连接请求，绑定连接端口和host信息
        final ChannelFuture future = bootstrap.connect(host, port).sync();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture arg0) throws Exception {
                if (future.isSuccess()) {
                    log.info("连接服务器成功");
                } else {
                    log.error("连接服务器失败");
                    future.cause().printStackTrace();
                    group.shutdownGracefully(); //关闭线程组
                }
            }
        });
        this.channel = future.channel();
    }



    /**
     * 客户端推送16进制数据
     * @param hexCode
     * @return
     */
    public void pushHexCode(String hexCode){
        ByteBuf bufff = Unpooled.buffer();
        // 字符串传输
        bufff.writeBytes(StringUtil.hexString2Bytes(hexCode));
        channel.writeAndFlush(bufff);
    }


    //连接服务端的端口号地址和端口号   type 解码格式 0
    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
        init();
    }

    public void stop(){
        group.shutdownGracefully();
    }


}
