package com.tunnel.deal.vehicleinspection;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetSocketAddress;
import java.sql.Timestamp;

@ChannelHandler.Sharable
@Component
public class MicrowaveNettyClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(MicrowaveNettyClientHandler.class);

    /**
     * 建立连接时
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        try {
            InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
            int port = ipSocket.getPort();
            String host = ipSocket.getHostString();

            log.info("与设备"+host+":"+port+"连接成功!");
            ctx.fireChannelActive();
        }
        catch (Exception e) {
            log.info("channelActive: " + e.getMessage());
        }
    }

    /**
     * 关闭连接时
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        try {
            InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
            int port = ipSocket.getPort();
            String host = ipSocket.getHostString();

            log.error("与设备" + host + ":" + port + "连接断开!");
            MicrowaveNettyClient.channels.remove(host+":"+String.valueOf(port));
            final EventLoop eventLoop = ctx.channel().eventLoop();
        }
        catch (Exception e) {
            log.info("channelInactive: " + e.getMessage());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("服务端连接异常：" + ctx.channel().id().asShortText());
    }

    /**
     * 收到报文，业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        int port = ipSocket.getPort();
        String host = ipSocket.getHostString();

        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] buffers = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(buffers);
        String receiveStr = ConvertCode.receiveHexToString(buffers);
        byteBuf.release();

        //log.info("接收到 " + host + ":" + port + "的数据");
//        int rplen = OpsProtocol.parseHead(buffers);
        // 断帧处理
//        if(rplen == -1) {
//            byte[] prerp = NettyClient.channels.get(host + ":" + port).getBuffer();
//
//            if(prerp == null) {
//                log.info("错误的报文");
//                return;
//            }
//            else {
//                byte[] buffers2 = new byte[prerp.length+buffers.length];
//                System.arraycopy(prerp, 0, buffers2, 0, prerp.length);
//                System.arraycopy(buffers, 0, buffers2, prerp.length, buffers.length);
//                buffers = buffers2;
//
//                rplen = OpsProtocol.parseHead(buffers2);
//            }
//        }
//        if(rplen > buffers.length) {
//            NettyClient.channels.get(host + ":" + port).setBuffer(buffers);
//        }

//        int cur = 0;

//        while (cur < buffers.length) {
//            // 粘桢处理
//            byte[] bytes = new byte[buffers.length-cur];
//            System.arraycopy(buffers, cur, bytes, 0, buffers.length-cur);
//            StrcApduHead apduHead = new StrcApduHead();
//            Head head = new Head();
//            EnumFunName en = OpsProtocol.parseHead(bytes, head, apduHead);
//
//            //……
//            // 解析报文，发送报文
//            ctx.channel().writeAndFlush(send2);
//            //……
//
//            cur += apduHead.getLength();
//        }

        MicrowaveNettyClient.channels.get(host + ":" + port).setActiveTime(new Timestamp(System.currentTimeMillis()));
    }

}
