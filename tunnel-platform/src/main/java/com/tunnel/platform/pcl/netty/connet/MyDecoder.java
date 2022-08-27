package com.tunnel.platform.pcl.netty.connet;

import com.tunnel.platform.utils.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) {
        byte[] req = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(req);
        String code = StringUtil.bytesString16(req);
        list.add(code);
    }
}