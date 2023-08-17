package com.tunnel.deal.xiaofangpao.client;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.deal.tcp.util.ByteBufUtil;
import com.tunnel.deal.xiaofangpao.msgEnum.SendMsgCodeEnum;
import com.tunnel.deal.xiaofangpao.service.FireMonitorDataParse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Component
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
    private static final Logger log = LoggerFactory.getLogger(com.tunnel.deal.guidancelamp.control.NettyClient.class);

    private FireMonitorDataParse fireMonitorDataParse = SpringUtils.getBean(FireMonitorDataParse.class);


    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        try{
            Channel channel = ctx.channel();
            InetSocketAddress ipSocket = (InetSocketAddress)channel.remoteAddress();
            String ip = ipSocket.getAddress().getHostAddress();
            String strMsg = com.tunnel.deal.tcp.util.ByteBufUtil.convertByteBufToString(msg);
            //JSONObject jsonObject = ModbusCmdResolver.commandParse(strMsg);
            //数据解析
            int code = fireMonitorDataParse.dataParse(ctx,strMsg);

            // 登录成功
            if(code == 201){
                SocketAddress socketAddress = ctx.channel().remoteAddress();
                log.info(socketAddress + " 消防炮服务验签成功");
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.scheduleAtFixedRate(() -> {
                    // 定时发送消息给服务端
                    String systemMsg = SendMsgCodeEnum.XIAOFANGPAO_SYSTEM_STATUS_CODE.getCode();
                    try {
                        channel.writeAndFlush(ByteBufUtil.convertStringToByteBuf(systemMsg.replace(" ","")));
                    } catch (DecoderException e) {
                        log.info("消防炮保持连接异常",e.getMessage());
                        e.printStackTrace();
                    }
                }, 60, 60, TimeUnit.SECONDS);
            }
            ctx.flush();
        }catch (Exception e){
            e.printStackTrace();
            log.error("解析数据报错：" + e.getMessage());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 连接上 就给服务端发送数据 40 40 04 00 01 06 26 1D 0E 0D 06 17 01 00 00 00 00 00 00 00 00 00 00 00 24 00 02 3C 01 01 02 41 32 30 46 45 43 31 37 33 42 36 46 44 34 30 46 44 37 38 43 38 45 35 46 37 43 41 41 41 32 42 30 74 23 23
        String quanXianjiaoYanMsg = SendMsgCodeEnum.XIAOFANGPAO_QUANXIANJIAOYAN_CODE.getCode();
        // quanXianjiaoYanMsg ="";
        ctx.writeAndFlush(ByteBufUtil.convertStringToByteBuf(quanXianjiaoYanMsg.replace(" ","")));
//        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello Server", CharsetUtil.UTF_8));
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        log.info(socketAddress + " 消防炮服务开始验签");


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info(ctx.channel().remoteAddress() + " 已断开连接");

        // 移除
        super.handlerRemoved(ctx);

        log.info("5s之后重新建立连接");
        NettyClient.getInstance().connect();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
