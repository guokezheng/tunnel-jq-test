package com.tunnel.deal.guidancelamp.protocol.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class DynamicDecoderDec extends ByteToMessageDecoder {

    /**
     *
     * @param ctx
     * @param byteBuf
     * @param list
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws UnsupportedEncodingException {
        byte[] req = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(req);
        String code = new String(req, "GBK");
        list.add(code);
    }
}
