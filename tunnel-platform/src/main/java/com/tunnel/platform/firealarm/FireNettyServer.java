package com.tunnel.platform.firealarm;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Netty服务端
 *
 */
@Component
public class FireNettyServer implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(FireNettyServer.class);

	private EventLoopGroup bossGroup = null;
	private EventLoopGroup workerGroup = null;
	private List<Integer> portList;
	private List<String> handlerList;

	public List<Integer> getPortList() {
		return portList;
	}

	public void setPortList(List<Integer> portList) {
		this.portList = portList;
	}

	public List<String> getHandlerList() {
		return handlerList;
	}

	public void setHandlerList(List<String> handlerList) {
		this.handlerList = handlerList;
	}

	@Override
	public void run() {
		runServer();
	}

	public void runServer(){
		int port;
		String handler;
		bossGroup = new NioEventLoopGroup();// 用来接收进来的连接
		workerGroup = new NioEventLoopGroup();//用来处理已经被接收的连接
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class) // (3)
            .option(ChannelOption.SO_BACKLOG, 128)          // (5)
            .option(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.SO_REUSEADDR, true)
            .option(ChannelOption.SO_RCVBUF, 10 * 1024)
            .option(ChannelOption.SO_SNDBUF, 10 * 1024)
            .option(EpollChannelOption.SO_REUSEPORT, true);

		    Collection<Channel> channels = new ArrayList<>(portList.size());
		    for (int i = 0; i < portList.size(); i++) {
		    	port = getPortList().get(i);
		    	handler = getHandlerList().get(i);
				Class<?> clz = Class.forName(handler);
				serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast("decoder", new MyDecoder())
								.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 4, 4, -8, 0))
								.addLast((ChannelHandlerAdapter) clz.newInstance());
					}
				});
		        Channel serverChannel = serverBootstrap.bind(port).sync().channel();
		        logger.info("启动Server服务成功，端口号：" + port);
		        channels.add(serverChannel);
		    }
			for (Channel ch : channels) {
		        ch.closeFuture().sync();
		    }
		} catch (InterruptedException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally{
			logger.info("退出，释放线程池资源！！");
			//退出，释放线程池资源
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}
