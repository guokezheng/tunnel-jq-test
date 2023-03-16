package com.tunnel.deal.plc.omron.finsTCP;

import com.tunnel.deal.plc.omron.OmronConnectProperties;
import com.tunnel.deal.plc.omron.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

public class OmronClientHandler extends ChannelInboundHandlerAdapter {


    public static Map<String,Object> content = new HashMap<>();

    /**
     * 消息处理
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        //接收服务端发送过来的消息
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] message = ByteBufUtil.getBytes(byteBuf);
        Channel channel = ctx.channel();
        OmronConnectProperties omronConnectProperties = OmronTcpClient.channelsGroup.get(channel);
        content.put(omronConnectProperties.getConnectKey(),message);
        if(omronConnectProperties.getCountDownLatch()!=null){
            omronConnectProperties.getCountDownLatch().countDown();
        }
    }
}
