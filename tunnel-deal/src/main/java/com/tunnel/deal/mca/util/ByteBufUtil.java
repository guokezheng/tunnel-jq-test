package com.tunnel.deal.mca.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * ByteBuf、String 转换
 */
public class ByteBufUtil
{

    private static final Logger log = LoggerFactory.getLogger(ByteBufUtil.class);



    /**
     * ByteBuf 转 String
     * @param buf
     * @return
     */
    public static String convertByteBufToString(ByteBuf buf)
    {

        byte[] req = new byte[buf.readableBytes()];

        buf.readBytes(req);

        return Hex.encodeHexString(req);
    }


    /**
     * String(十六进制字符串) 转 ByteBuf
     * @param str
     * @return
     * @throws DecoderException
     */
    public static ByteBuf convertStringToByteBuf(String str) throws DecoderException
    {
        byte[] msgBytes = Hex.decodeHex(str.toCharArray());

        return Unpooled.buffer().writeBytes(msgBytes);
    }


}
