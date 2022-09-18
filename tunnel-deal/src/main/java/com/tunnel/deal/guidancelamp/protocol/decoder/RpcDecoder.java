package com.tunnel.deal.guidancelamp.protocol.decoder;


import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {
    private Class<?> clazz;

    public RpcDecoder(Class<?> clazz) {
        this.clazz = clazz;
    }
    /**
     * 自定义解码器解码方法实现
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes()<4){
            return;
        }
        //标记一下读指针位置，此时没有过读操作，读指针处于初始位置
        byteBuf.markReaderIndex();
        //读取一个int长度 此时读指针会后移4个位置
        int length = byteBuf.readInt();
        if(byteBuf.readableBytes()<length){
            //读指针回到刚才标记的初始位置
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Object o = JSON.parseObject(bytes, clazz);
        list.add(o);
    }
}
