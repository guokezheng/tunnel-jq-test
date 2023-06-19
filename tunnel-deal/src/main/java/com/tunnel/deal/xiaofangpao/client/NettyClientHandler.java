package com.tunnel.deal.xiaofangpao.client;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.deal.mca.util.ByteBufUtil;
import com.tunnel.deal.xiaofangpao.msgEnum.SendMsgCodeEnum;
import com.tunnel.deal.xiaofangpao.service.FireMonitorDataParse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;


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
            String strMsg = com.tunnel.deal.mca.util.ByteBufUtil.convertByteBufToString(msg);
            //JSONObject jsonObject = ModbusCmdResolver.commandParse(strMsg);
            //数据解析
            fireMonitorDataParse.dataParse(ctx,strMsg);

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
        ctx.writeAndFlush(ByteBufUtil.convertStringToByteBuf(quanXianjiaoYanMsg.replace(" ","")));
//        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello Server", CharsetUtil.UTF_8));
//        SocketAddress socketAddress = ctx.channel().remoteAddress();
//        log.info(socketAddress + " 已连接");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info(ctx.channel().remoteAddress() + " 已断开连接");
        reconnection(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
    private void reconnection(ChannelHandlerContext ctx) {
        log.info("5s之后重新建立连接");
        //连接失败，重新连接
        try {
            NettyClient.getInstance().start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
