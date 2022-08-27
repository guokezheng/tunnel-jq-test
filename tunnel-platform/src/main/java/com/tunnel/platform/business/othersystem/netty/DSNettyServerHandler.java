package com.tunnel.platform.business.othersystem.netty;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetAddress;

/**
 * 
* Title: DSNettyServerHandler
* Description:  
* @author DS
* @date 2021-11-24
 */
public class DSNettyServerHandler extends  ChannelInboundHandlerAdapter {
    
	private EventLoopGroup group;
	
	/**
	 * 收到消息时，返回信息
	 * 
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {

		ByteBuf result = (ByteBuf) msg;
        byte[] byteResult = new byte[result.readableBytes()];
        // msg中存储的是ByteBuf类型的数据，把数据读取到byte[]中
        result.readBytes(byteResult);
        String hex = bytes2HexString(byteResult);
        String res = Convert.hexToStr(hex, CharsetUtil.CHARSET_GBK);
        System.out.println(res);
        JSONObject jsonObject = JSONUtil.parseObj(res);
        System.out.println(jsonObject);
//        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
//        String strIP = insocket.getAddress().getHostAddress();//客户端ip
//        dataAnalysis(strIP,hex);
        System.out.println(hex);
        // 释放资源，这行很关键
        result.release();
        ctx.flush();
	}
	
	/**
	 * 转换
	 * @param b
	 * @return
	 */
	private String bytes2HexString(byte[] b) {
		StringBuffer result = new StringBuffer();
		String hex;
		for (int i = 0; i < b.length; i++) {
			hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			result.append(hex.toUpperCase());
		}
		return result.toString();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) {
	 cause.printStackTrace();
	       ctx.close();
	}

    /*
     * 建立连接时，返回消息
     */
   @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("address:" + ctx.channel().remoteAddress());
        ctx.writeAndFlush("client"+ InetAddress.getLocalHost().getHostName() + "connect！ \n");
        super.channelActive(ctx);
    }
   /**
    * 通道读取完成
    */
   @Override
   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	   
       super.channelReadComplete(ctx);
       ctx.flush();
   }
   
   @Override
   public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
       //设置的超时时间内关闭连接
       if (evt instanceof IdleStateEvent) {
           ctx.close();// 关闭连接
           if (group != null) {
               group.shutdownGracefully();// 释放线程池资源
           }
       } else {
           super.userEventTriggered(ctx, evt);
       }
   }
   
   public <T> String writeAsString(T t) throws JsonProcessingException {
       ObjectMapper objectMapper = new ObjectMapper();
       return objectMapper.writeValueAsString(t);
   }
}