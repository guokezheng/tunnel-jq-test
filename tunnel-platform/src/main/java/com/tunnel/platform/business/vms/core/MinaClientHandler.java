package com.tunnel.platform.business.vms.core;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * describe: mina消息处理类
 *
 * @author zs
 * @date 2023/8/29
 */
public class MinaClientHandler extends IoHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinaClientHandler.class);

    /**
     * 连接创建时触发
     */
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        // Empty handler
    }

    /**
     * 在打开连接时调用该会话打开事件。它总是在sessionCreated事件之后被调用
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        // Empty handler
    }

    /**
     * 会话关闭时，触发Session Closed事件
     */
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        // Empty handler
    }

    /**
     * 当会话变为空闲时触发会话空闲事件。UDP传输不调用此函数。
     */
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        // Empty handler
    }

    /**
     * 当用户代码或MINA抛出异常时，调用该函数。如果是IOException，则连接关闭。
     */
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("EXCEPTION, please implement " + getClass().getName()
                    + ".exceptionCaught() for proper handling:", cause);
        }
    }

    /**
     * 每当收到消息时，将触发Message Received事件。
     */
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if (message != null) {
            //67 68 00 02 80 01 B1 0D 0A
            // // 1.将协议解析并存放到缓存中；
            if (message instanceof IoBuffer) {
                IoBuffer ioBuffer = (IoBuffer) message;
                String hexProtocol = ioBuffer.getHexDump();
                if (hexProtocol.contains("67 68 00 0E 80 05")){
                    if (MinaClient.dataCache.containsKey(hexProtocol.substring(0,26) )) {
                        MinaClient.dataCache.put(hexProtocol.substring(0,26), hexProtocol);
                    } else {
                        MinaClient.dataCache.put(hexProtocol.substring(0,26), hexProtocol);
                    }
                    LOGGER.debug(System.currentTimeMillis() + "[messageReceived(session,message)] 成功获取Iot设备返回数据，并存入缓存。 数据：[" + hexProtocol + "]");
                }else {
                    if (MinaClient.dataCache.containsKey(session.getCreationTime() + "")) {
                        String prefix = MinaClient.dataCache.get(session.getCreationTime() + "");
                        MinaClient.dataCache.put(session.getCreationTime() + "", prefix + " " + hexProtocol);
                    } else {
                        MinaClient.dataCache.put(session.getCreationTime() + "", hexProtocol);
                    }
                }
            }
        }
    }

    /**
     * 每当发送消息(或响应)(调用iossession .write())时，就会触发Message Sent事件。
     */
    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        // Empty handler
    }

    /**
     * 处理半双工TCP通道的关闭
     */
    @Override
    public void inputClosed(IoSession session) throws Exception {
        session.closeNow();
    }

}
