package com.tunnel.deal.plc.fins.mina;

import com.tunnel.business.datacenter.config.MapCache;
import com.tunnel.business.utils.util.RadixUtil;
import com.tunnel.deal.plc.fins.CmdProcess;
import com.tunnel.deal.plc.fins.PlcProcess;
import org.apache.mina.core.buffer.IoBuffer;
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
import java.util.List;
import java.util.Map;

/**
 * @author yangqichao
 */
public class MinaConnection implements IConnection {

    private static final Logger logger = LoggerFactory.getLogger(MinaConnection.class);
    // 3.中断循环标识
    public static boolean isExit = false;
    private final String handshake;
    private final List<Map<String, String>> commands;
    private final String hostControlType;
    // 5.mina连接
    NioSocketConnector connector = null;
    // 6.获取连接session
    IoSession ioSession = null;
    // 1.服务端ip
    private String ip;
    // 2.服务端端口
    private int port;
    private String id;

    public MinaConnection(String id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.commands = CmdProcess.CmdUtil.getCmdMap().get(id).getCmdList();
        this.hostControlType = CmdProcess.CmdUtil.getCmdMap().get(id).getHostControlType();
        this.handshake = CmdProcess.DisPlayUtil.handshake();
        init();
    }

    /**
     * @Description: 初始化连接
     */
    @Override
    public void init() {
        /*
         * 2.创建nio socket客户端连接
         */
        connector = new NioSocketConnector();
        LoggingFilter loggingFilter = new LoggingFilter();
        // NONE级别是不打印日志
        loggingFilter.setMessageReceivedLogLevel(LogLevel.INFO);
        loggingFilter.setMessageSentLogLevel(LogLevel.INFO);
        //connector.getFilterChain().addLast("logger", loggingFilter);
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
        if ("DM".equals(hostControlType)) {
            connector.setHandler(new DMMinaCallBack(id, commands));
        } else {
            connector.setHandler(new CIOMinaCallBack(id, commands));
        }
        logger.info(" [init()] 连接Iot设备的初始化参数成功！ Iot设备： [" + ip + ":" + port + "] ");
        if (commands.size() > 0) {
            IoSession session = getIoSession();
            byte[] byteHandshake = RadixUtil.hex2Byte(handshake.split(" "));
            // 3.1.如果指令为16进制，则移除codec的编码；
            session.getFilterChain().remove("codec");
            // 3.2.发送指令
            session.write(IoBuffer.wrap(byteHandshake));
        }
    }


    /**
     * @Description: 获取里连接session
     */
    public IoSession getIoSession() {
        if (connector == null) {
            init();
        }
        if (connector == null) {
            return null;
        }
        /*
         * 3.创建连接。
         */
        ConnectFuture cf = connector.connect(new InetSocketAddress(ip, port));
        // 3.1 等待连接创建完成
        cf.awaitUninterruptibly();

        /*
         * 4.建立连接后，获取连接session
         */
        try {
            ioSession = cf.getSession();
            MapCache.sessionCache.put(String.valueOf(id), id);
            logger.info(" [run()] 获取连接Iot设备成功！Iot设备：[" + ip + ":" + port + "]！创建时间： " + ioSession.getCreationTime());
            return ioSession;
        } catch (Exception e) {
            MapCache.sessionCache.remove(String.valueOf(id), id);
            PlcProcess.shutdown();
            logger.warn(" [run()] 获取连接Iot设备异常！Iot设备：[" + ip + ":" + port + "]！ " + e.getMessage());
            connector.dispose();
            connector = null;
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isConnection() {
        return ioSession != null && ioSession.isConnected();
    }

    @Override
    public void disconnect() {
        if (connector == null || connector.isDisposed()) {
            return;
        }
        if (isConnection()) {
            ioSession.closeNow();
            // 等待连接断开
            ioSession.getCloseFuture().awaitUninterruptibly();
        }
        connector.dispose();
        ioSession = null;
        connector = null;
        logger.info(" [disconnect()] Iot设备断开连接！Iot设备：[" + ip + ":" + port + "]！ ");
    }

    public NioSocketConnector getConnector() {
        return connector;
    }
}
