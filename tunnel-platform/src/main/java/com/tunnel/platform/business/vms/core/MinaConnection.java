package com.tunnel.platform.business.vms.core;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class MinaConnection
//		implements IConnection
{

	private static final Logger logger = LoggerFactory.getLogger(MinaConnection.class);

//	// 1.服务端ip
//	private String ip;
//	// 2.服务端端口
//	private int port;
//	// 3.中断循环标识
//	public static boolean isExit = false;
	// 5.mina连接
	private static NioSocketConnector connector = null;
//	// 6.获取连接session
//	IoSession ioSession = null;

//	public MinaConnection(String ip, int port) {
//		this.ip = ip;
//		this.port = port;
//		init();
//	}

	static{
		initClient();
	}


	/**
	 * 初始化Mina客户端，代码只执行一次
	 *  new NioSocketConnector() 会创建大量连接数，频繁创建会造成系统资源耗尽，报错“文件过多”
	 *  代码变更后new NioSocketConnector()只创建一次，避免此问题
	 */
	public static void initClient(){
		/*
		 * 2.创建nio socket客户端连接
		 */
		connector = new NioSocketConnector();
		LoggingFilter loggingFilter = new LoggingFilter();
		// NONE级别是不打印日志
		loggingFilter.setMessageReceivedLogLevel(LogLevel.NONE);
		loggingFilter.setMessageSentLogLevel(LogLevel.NONE);
		connector.getFilterChain().addLast("logger", loggingFilter);
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
		//添加处理器
		connector.setHandler(new MinaClientHandler());
	}

//	/**
//	 *
//	 * @Description: 初始化连接
//	 */
//	public void init() {
//		/*
//		 * 2.创建nio socket客户端连接
//		 */
//		connector = new NioSocketConnector();
//		LoggingFilter loggingFilter = new LoggingFilter();
//		// NONE级别是不打印日志
//		loggingFilter.setMessageReceivedLogLevel(LogLevel.INFO);
//		loggingFilter.setMessageSentLogLevel(LogLevel.INFO);
//		connector.getFilterChain().addLast("logger", loggingFilter);
//		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
//
////		logger.info(" [init()] 连接Iot设备的初始化参数成功！ Iot设备： [" + ip + ":" + port + "] ");
//	}

	/**
	 *
	 * @Description: 获取里连接session
	 * @return
	 */
	public static IoSession getIoSession(String ip,int port) {
		IoSession ioSession = null;
		if (connector == null){
			//重新初始化客户端
			initClient();
//			init();
 		}
		if (connector == null) {
			return null;
		}
		/*
		 * 3.创建连接。
		 */
		ConnectFuture cf = connector.connect(new InetSocketAddress(ip, port));
		// 3.1 等待连接创建完成 3秒钟超时时间
		cf.awaitUninterruptibly(3L,TimeUnit.SECONDS);

		/*
		 * 4.建立连接后，获取连接session
		 */
		try {
			ioSession = cf.getSession();
			logger.info(" [run()] 获取连接Iot设备成功！Iot设备：[" + ip + ":" + port + "]！创建时间： "+ioSession.getCreationTime());
			return ioSession;
		} catch (Exception e) {
			//关闭session
			if (ioSession != null) {
				ioSession.closeOnFlush();
			}
			logger.error(" [run()] 获取连接Iot设备异常！Iot设备：[" + ip + ":" + port + "]！ " + e.getMessage());
//			connector.dispose();
//			connector = null;
//			e.printStackTrace();
		}
		return null;
	}

//	@Override
	public boolean isConnection(IoSession ioSession) {
		return ioSession == null ? false : ioSession.isConnected();
	}

//	@Override
	public static void disconnect(IoSession ioSession) {
//		if (connector == null || connector.isDisposed()) {
//			return;
//		}

		if (ioSession != null) {
			ioSession.closeOnFlush();
		}
//		if (isConnection()) {
//			ioSession.closeNow();
//			// 等待连接断开
//			ioSession.getCloseFuture().awaitUninterruptibly();
//		}
//		connector.dispose();
//		ioSession = null;
//		connector = null;
//		logger.info(" [disconnect()] Iot设备断开连接！Iot设备：[" + ip + ":" + port + "]！ ");
	}

	public NioSocketConnector getConnector() {
		return connector;
	}
}
