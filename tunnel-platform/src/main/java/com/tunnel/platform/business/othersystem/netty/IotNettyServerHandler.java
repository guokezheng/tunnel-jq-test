package com.tunnel.platform.business.othersystem.netty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tunnel.platform.domain.roadState.SdRoadDetectorData;
import com.tunnel.platform.domain.roadState.SdRoadDetectorData;
import com.tunnel.platform.mapper.roadState.SdRoadDetectorDataMapper;
import com.tunnel.platform.utils.util.RadixUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 *
* Title: IotNettyServerHandler
* Description:
* @author wangx
* @date 2021-11-22
 */
@Component
public class IotNettyServerHandler extends  ChannelInboundHandlerAdapter {

	private EventLoopGroup group;

	@Autowired
	SdRoadDetectorDataMapper sdRoadDetectorDataMapper;

	private static IotNettyServerHandler iotNettyServerHandler;

	/**
	 * 在方法上加该注解会在项目启动的时候执行该方法，即spring容器初始化的时候执行
	 * 它与构造函数及@Autowired的执行顺序为：构造函数 >> @Autowired >> @PostConstruct
	 */
	@PostConstruct // 第4步 添加 @PostConstruct 注解的方法
	public void init() {
		iotNettyServerHandler = this;
	}

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
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String strIP = insocket.getAddress().getHostAddress();//客户端ip
        dataAnalysis(strIP,hex);
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

	public static void main(String[] args) throws JsonProcessingException {

	}

	/**
	 * 协议解析
	 * @param strIP Ip
	 * @param data 接收数据
	 * @throws JsonProcessingException
	 */
	private void dataAnalysis(String strIP, String data) throws JsonProcessingException {
		// 隧道编号
		data = data.substring(4);
		String tunnelId = data.substring(0, 8);
		// 设备编号
		data = data.substring(8);
		String devno = data.substring(0, 8);
		// 设备类型
		data = data.substring(8);
		String deviceType = data.substring(0,4);
		// 版本号
		data = data.substring(4);
		String version = data.substring(0,4);
		// 数据长度
		data = data.substring(4);
		int dataLength = Integer.valueOf(RadixUtil.hexToDecimal(data.substring(0,2)));
		// 数据
		data = data.substring(4);
		// 设备类型1
		if("0001".equals(deviceType)){
			// 洞外光照强度
			String dwLight = RadixUtil.hexToDecimal(data.substring(0, 4));
			data = data.substring(4);
			data = data.substring(4);
			// 洞外光强故障码
			String dwState = RadixUtil.hexToDecimal(data.substring(0, 4));
			// 洞内光照亮度
			data = data.substring(4);
			String dnLight = RadixUtil.hexToDecimal(data.substring(0, 4));
			data = data.substring(4);
			data = data.substring(4);
			// 洞内光强故障码
			String dnState = RadixUtil.hexToDecimal(data.substring(0, 4));

		}
		// 设备类型2
		if("0002".equals(deviceType)){
			SdRoadDetectorData sdRoadDetectorData = new SdRoadDetectorData();
			// 路面温度
			String pavementtemp = RadixUtil.hexToDecimal(data.substring(0, 4));
			sdRoadDetectorData.setPavementtemp(pavementtemp);
			data = data.substring(4);
			// 积水深度
			String waterfilmheigh = RadixUtil.hexToDecimal(data.substring(0, 4));
			sdRoadDetectorData.setWaterfilmheigh(waterfilmheigh);
			data = data.substring(4);
			data = data.substring(4);
			data = data.substring(4);
			// 湿滑系数
			String frictionalcoe = RadixUtil.hexToDecimal(data.substring(0, 4));
			sdRoadDetectorData.setFrictionalcoe(frictionalcoe);
			data = data.substring(4);
			// 路面状态
			String pavementstatus = RadixUtil.hexToDecimal(data.substring(0, 4));
			sdRoadDetectorData.setPavementstatus(pavementstatus);
			sdRoadDetectorData.setDevno(devno);
			iotNettyServerHandler.sdRoadDetectorDataMapper.insertSdRoadDetectorData(sdRoadDetectorData);
		}

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
