package com.tunnel.plc.fins.mina;

import com.tunnel.platform.datacenter.config.MapCache;

import com.tunnel.platform.utils.util.CommonUtil;
import com.tunnel.platform.utils.util.RadixUtil;
import com.tunnel.plc.fins.CmdProcess;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinaClient {


    private final String ip;
    public final String id;
    private final int port;

    // 缓存Iot设备执行命令侯返回的数据。
    public static Map<String, String> dataCache = new HashMap<String, String>();

    public MinaClient(String id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        init();
    }

    private MinaConnection connection;

    /**
     * @Description: 初始化Mina连接
     */
    public void init() {
        if (CommonUtil.isNull(ip) || port < 0) {
            throw new IllegalArgumentException("Mina客户端连接ip和端口异常！[ip=" + ip + ",port=" + port + "]");
        }
        connection = new MinaConnection(id, ip, port);
    }

    /**
     * @param callback
     * @Description: 设置回调函数
     */
    public void setCallBack(IMinaCallback callback) {
        connection.getConnector().setHandler(callback);
    }

    /**
     * @param ，如果为空系统默认一个值。
     * @Description: 发送指令
     */
    public void closeNow() {
        connection.disconnect();
    }
}

class CIOMinaCallBack implements IMinaCallback {
    private static final Logger logger = LoggerFactory.getLogger(IMinaCallback.class.getName());
    private final List<Map<String, String>> commands;
    private final String hostId;
    private String devType;
    private int nIndex;

    public CIOMinaCallBack(String id, List<Map<String, String>> commands) {
        this.hostId = id;
        this.commands = commands;
        nIndex = 0;
    }

    @Override
    public void exceptionCaught(IoSession arg0, Throwable exception) throws Exception {
    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
        session.closeNow();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if (message != null) {
            // 1.将协议解析并存放到缓存中；
            if (message instanceof IoBuffer) {
                String hostAddress = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
                IoBuffer ioBuffer = (IoBuffer) message;
                String hexProtocol = ioBuffer.getHexDump();
//                logger.info(hostAddress + "设备类型" + devType + "返回内容" + hexProtocol);
                if (nIndex <= commands.size()) {
                    if (devType != null) {
                        CmdProcess.DataUtil.parsingCIOContent(hostId, devType, hexProtocol);
                    }
                    if (nIndex < commands.size()) {
                        Map<String, String> strCom = commands.get(nIndex);
                        for (Map.Entry<String, String> entry : strCom.entrySet()) {
                            devType = entry.getKey();
                            byte[] byteCommand = RadixUtil.hex2Byte(entry.getValue().split(" "));
                            session.write(IoBuffer.wrap(byteCommand));
                            if (devType == "kz") {
                                CmdProcess.DataUtil.parsingKZ(hostId, hexProtocol, commands.get(nIndex));
                                commands.remove(nIndex);
                                logger.info(hostAddress + "发送指令:" + devType + "：" + entry.getValue());
                            }
                            nIndex++;
//                            logger.info(hostAddress + "发送指令:" + devType + "：" + entry.getValue());
                        }
                    } else {
                        MapCache.sessionCache.remove(String.valueOf(hostId));
                    }

                } else {
                    MapCache.sessionCache.remove(String.valueOf(hostId));
                }
            }
        } else {
            MapCache.sessionCache.remove(String.valueOf(hostId));

        }
    }

    @Override
    public void messageSent(IoSession arg0, Object arg1) throws Exception {
    }

    @Override
    public void sessionClosed(IoSession arg0) throws Exception {
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();
        cfg.setReceiveBufferSize(2 * 1024 * 1024);
        cfg.setReadBufferSize(2 * 1024 * 1024);
        cfg.setKeepAlive(false);
        cfg.setSoLinger(0);
    }

    @Override
    public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
    }
}


class DMMinaCallBack implements IMinaCallback {
    private static final Logger logger = LoggerFactory.getLogger(IMinaCallback.class.getName());
    private final List<Map<String, String>> commands;
    private final String hostId;
    private String devType;
    private int nIndex;

    public DMMinaCallBack(String id, List<Map<String, String>> commands) {
        this.hostId = id;
        this.commands = commands;
        nIndex = 0;
    }

    @Override
    public void exceptionCaught(IoSession arg0, Throwable exception) throws Exception {
    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
        session.closeNow();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if (message != null) {
            // 1.将协议解析并存放到缓存中；
            if (message instanceof IoBuffer) {
                String hostAddress = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
                IoBuffer ioBuffer = (IoBuffer) message;
                String hexProtocol = ioBuffer.getHexDump();
//                logger.info(hostAddress + "设备类型" + devType + "返回内容" + hexProtocol);
                if (nIndex <= commands.size()) {
                    if (devType != null) {
                        CmdProcess.DataUtil.parsingDMContent(devType, hexProtocol);
                    }
                    if (nIndex < commands.size()) {
                        Map<String, String> strCom = commands.get(nIndex);
                        for (Map.Entry<String, String> entry : strCom.entrySet()) {
                            devType = entry.getKey();
                            String regex = "(.{2})";
                            String replace = entry.getValue().replaceAll(regex, "$1 ");
                            byte[] byteCommand = RadixUtil.hex2Byte(replace.trim().split(" "));
                            session.write(IoBuffer.wrap(byteCommand));
                            if (devType == "kz") {
                                CmdProcess.DataUtil.parsingKZ(hostId, hexProtocol, commands.get(nIndex));
                                commands.remove(nIndex);
                                logger.info(hostAddress + "发送指令:" + devType + "：" + entry.getValue());
                            }
                            nIndex++;
//                            logger.info(hostAddress + "发送指令:" + devType + "：" + entry.getValue());
                        }
                    } else {
                        MapCache.sessionCache.remove(String.valueOf(hostId));
                    }

                } else {
                    MapCache.sessionCache.remove(String.valueOf(hostId));
                }
            }
        } else {
            MapCache.sessionCache.remove(String.valueOf(hostId));

        }
    }

    @Override
    public void messageSent(IoSession arg0, Object arg1) throws Exception {
    }

    @Override
    public void sessionClosed(IoSession arg0) throws Exception {
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();
        cfg.setReceiveBufferSize(2 * 1024 * 1024);
        cfg.setReadBufferSize(2 * 1024 * 1024);
        cfg.setKeepAlive(false);
        cfg.setSoLinger(0);
    }

    @Override
    public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
    }
}
