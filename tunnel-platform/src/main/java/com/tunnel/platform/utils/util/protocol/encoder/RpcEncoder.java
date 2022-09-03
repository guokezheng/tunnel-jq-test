package com.tunnel.platform.utils.util.protocol.encoder;


import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Objects;

public class RpcEncoder extends MessageToByteEncoder {
    private Class<?> clazz;

    public RpcEncoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * 自定义编码方法实现 java--》byte
     * @param channelHandlerContext
     * @param o
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if(Objects.nonNull(clazz)&&clazz.isInstance(o)){
            byte[] bytes = JSON.toJSONBytes(o);
            int length = bytes.length;
            byteBuf.writeInt(length);
            byteBuf.writeBytes(bytes);
        }
    }
}

