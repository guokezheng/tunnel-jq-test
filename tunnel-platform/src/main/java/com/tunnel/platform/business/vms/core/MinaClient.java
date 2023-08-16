package com.tunnel.platform.business.vms.core;

import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.utils.exception.BusinessException;
import com.tunnel.business.utils.util.RadixUtil;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MinaClient {

    private static final Logger logger = LoggerFactory.getLogger(MinaClient.class);

    private final String ip;
    private final int port;

    // 缓存Iot设备执行命令侯返回的数据。
    public static ConcurrentHashMap<String, String> dataCache = new ConcurrentHashMap<String, String>();

    public MinaClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        init();
    }

    private MinaConnection connection;

    /**
     * @Description: 初始化Mina连接
     */
    public void init() {
        if (StringUtils.isEmpty(ip) || port < 0) {
            throw new BusinessException("Mina客户端连接ip和端口异常！[ip=" + ip + ",port=" + port + "]");
        }
        connection = new MinaConnection(ip, port);
        setCallBack(new DefaultMinaCallBack());
    }

    /**
     * @param key
     * @param value
     * @Description: 向缓存写入数据
     */
    @Deprecated
    public synchronized static void putDataCache(String key, String value) {
        List<String> datas = getDataCache(key);
        dataCache.put(key + "_" + datas.size(), value);
    }

    /**
     * @param key
     * @return
     * @Description: 根据key获取缓存数据
     */
    @Deprecated
    public synchronized static List<String> getDataCache(String key) {
        List<String> result = new ArrayList<String>();
        for (Map.Entry<String, String> entry : dataCache.entrySet()) {
            String _key = entry.getKey();
            if (_key == null)
                continue;
            if (_key.startsWith(key + "_"))
                result.add(_key);
        }
        return result;
    }

    /**
     * @param callback
     * @Description: 设置回调函数
     */
    public void setCallBack(IMinaCallback callback) {
        connection.getConnector().setHandler(callback);
    }

    /**
     * @param command
     * @param radix   发送指令的sessinid，如果为空系统默认一个值。
     * @param radix   指令的格式，如果为16进制，则写16进制。
     * @Description: 发送指令
     */
    public String sendCommand(String command, int radix) {
        /*
         * 1.校验传入的命令是否为空。
         */
        if (StringUtils.isEmpty(command)) {
            logger.error("[sendCommand(command,sessionId,radix)] 发送指令为空！");
            throw new RuntimeException("[sendCommand(command,sessionId,radix)] 发送指令为空！");
        }
        /*
         * 2.链接Iot设备。
         */
        IoSession session = connection.getIoSession();
        if (session == null) {
            logger.error("[sendCommand(command,sessionId,radix)] 连接Iot设备异常，不能发送指令！指令：[" + command + "] ");
            throw new RuntimeException("[sendCommand(command,sessionId,radix)] 连接Iot设备异常，不能发送指令！指令：[" + command + "] ");
        }
        /*
         * 3.指令为16进制和字符串两种，分别进行处理，并发送指令。
         */
        if (radix == 16) {
            byte[] byteCommand = RadixUtil.hex2Byte(command.split(" "));//103, 104, 0, 2, 0, 1, 7, 13, 10]

            // 3.1.如果指令为16进制，则移除codec的编码；
            session.getFilterChain().remove("codec");
            // 3.2.发送指令
            session.write(IoBuffer.wrap(byteCommand));
        } else {
            session.write(command);
        }
        /*
         * 4.获取响应内容。
         */
        String respondContent = null;
        int waitTime = 50;
        while (StringUtils.isEmpty(respondContent)) {
            try {
                logger.info("[sendCommand(command,sessionId,radix)] 等待接收Iot消息.... ");
                Thread.sleep(100);
                respondContent = dataCache.get(session.getCreationTime() + "");//67 68 00 02 80 01 B1 0D 0A 67 68 00 03 80 02 01 26 0D 0A 67 68 00 06 80 03 0E 28 00 20 7C 0D 0A 67 68 00 0E 80 08 01 FF FF 55 00 4C 33 02 14 1E 00 00 A2 0D 0A 67 68 00 0B 80 09 01 00 60 00 00 00 00 00 00 11 0D 0A 67 68 00 0B 80 09 01 00 60 00 00 00 00 00 00 11 0D 0A
                /*
                 * 1.only ib.2018.01.04
                 */
                if (!StringUtils.isEmpty(respondContent)) {
                    respondContent = respondContent.trim();
                    if (!respondContent.endsWith("03") && !respondContent.endsWith("04") && !respondContent.endsWith("0A")){
                        respondContent = null;
                    }
                }
                waitTime--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (waitTime <= 0) {
                logger.error("[sendCommand(command,sessionId,radix)] 等待接收Iot消息超时,无法获取Iot设备响应数据！ ");
                break;
            }
        }
        /*
         * 5.清除缓存，防止同一个IoSession，非第一次访问Iot设备获取上一次遗留的数据。
         */
        dataCache.remove(session.getCreationTime() + "");
        logger.debug("[sendCommand(command,sessionId,radix)] 清除缓存Iot设备返回的数据！ ");
        /*
         * 6.断开连接。
         */
        connection.disconnect();
        /*
         * 7.返回获取内容。
         */
        return respondContent;//67 68 00 02 80 01 B1 0D 0A 67 68 00 0B 80 09 00 00 5F 00 00 00 00 00 00 E3 0D 0A 67 68 00 0B 80 09 00 00 5F 00 00 00 00 00 00 E3 0D 0A
    }

    /**
     * @param command
     * @param radix   发送指令的sessinid，如果为空系统默认一个值。
     * @param radix   指令的格式，如果为16进制，则写16进制。
     * @Description: 发送指令
     */
    public String sendCommand1(String command, int radix) {
        /*
         * 1.校验传入的命令是否为空。
         */
        if (StringUtils.isEmpty(command)) {
            throw new BusinessException("[sendCommand(command,sessionId,radix)] 发送指令为空！");
        }
        /*
         * 2.链接Iot设备。
         */
        IoSession session = connection.getIoSession();
        if (session == null) {
            throw new BusinessException("[sendCommand(command,sessionId,radix)] 连接Iot设备异常，不能发送指令！指令：[" + command + "] ");
        }
        /*
         * 3.指令为16进制和字符串两种，分别进行处理，并发送指令。
         */
        if (radix == 16) {
            byte[] byteCommand = RadixUtil.hex2Byte(command.split(" "));//103, 104, 0, 2, 0, 1, 7, 13, 10]

            // 3.1.如果指令为16进制，则移除codec的编码；
            session.getFilterChain().remove("codec");
            // 3.2.发送指令
            session.write(IoBuffer.wrap(byteCommand));
        } else {
            session.write(command);
        }
        /*
         * 4.获取响应内容。
         */
        String respondContent = null;
        int waitTime = 100;
        while (StringUtils.isEmpty(respondContent)) {
            try {
                Thread.sleep(100);
                respondContent = dataCache.get(session.getCreationTime() + "");//67 68 00 02 80 01 B1 0D 0A 67 68 00 03 80 02 01 26 0D 0A 67 68 00 06 80 03 0E 28 00 20 7C 0D 0A 67 68 00 0E 80 08 01 FF FF 55 00 4C 33 02 14 1E 00 00 A2 0D 0A 67 68 00 0B 80 09 01 00 60 00 00 00 00 00 00 11 0D 0A 67 68 00 0B 80 09 01 00 60 00 00 00 00 00 00 11 0D 0A
                /*
                 * 1.only ib.2018.01.04
                 */
                if (!StringUtils.isEmpty(respondContent)) {
                    respondContent = respondContent.trim();
                    if (!respondContent.endsWith("0A")){
                        respondContent = null;
                    }
                }
                waitTime--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (waitTime <= 0) {
                break;
            }
        }
        /*
         * 5.清除缓存，防止同一个IoSession，非第一次访问Iot设备获取上一次遗留的数据。
         */
        dataCache.remove(session.getCreationTime() + "");
        /*
         * 6.断开连接。
         */
       /* connection.disconnect();*/
        /*
         * 7.返回获取内容。
         */
        return respondContent;//67 68 00 02 80 01 B1 0D 0A 67 68 00 0B 80 09 00 00 5F 00 00 00 00 00 00 E3 0D 0A 67 68 00 0B 80 09 00 00 5F 00 00 00 00 00 00 E3 0D 0A
    }
}


class DefaultMinaCallBack implements IMinaCallback {
    private static final Logger logger = LoggerFactory.getLogger(IMinaCallback.class.getName());

    @Override
    public void exceptionCaught(IoSession arg0, Throwable execption) throws Exception {
        logger.warn("[exceptionCaught(session,execption)] : " + execption.getMessage());
    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
        session.closeNow();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if (message != null) {//67 68 00 02 80 01 B1 0D 0A
            // // 1.将协议解析并存放到缓存中；
            if (message instanceof IoBuffer) {
                IoBuffer ioBuffer = (IoBuffer) message;
                String hexProtocol = ioBuffer.getHexDump();
                // System.out.println("--接收到消息：" + hexProtocol + "    " +			// session.getCreationTime());

                /*
                 * 20180104 修复bug。由于返回内容超长，超过一次传输的长度，系统只截取最后一部分bug。
                 */
                if (hexProtocol.contains("67 68 00 0E 80 05")){
                    if (MinaClient.dataCache.containsKey(hexProtocol.substring(0,26) )) {
                        //String prefix = MinaClient.dataCache.get(session.getCreationTime() + "");
                        MinaClient.dataCache.put(hexProtocol.substring(0,26), hexProtocol);
//                        System.out.println("++++++++++++++++++++++");
                    } else {
                        MinaClient.dataCache.put(hexProtocol.substring(0,26), hexProtocol);
//                        System.out.println("--------------------");
                    }
                    logger.debug(System.currentTimeMillis() + "[messageReceived(session,message)] 成功获取Iot设备返回数据，并存入缓存。 数据：[" + hexProtocol + "]");
                }else {
                    if (MinaClient.dataCache.containsKey(session.getCreationTime() + "")) {
                        String prefix = MinaClient.dataCache.get(session.getCreationTime() + "");
                        MinaClient.dataCache.put(session.getCreationTime() + "", prefix + " " + hexProtocol);
//                        System.out.println("++++++++++++++++++++++");
                    } else {
                        MinaClient.dataCache.put(session.getCreationTime() + "", hexProtocol);
//                        System.out.println("--------------------");
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
        // session.getFilterChain().remove("codec");
    }

    @Override
    public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {

    }

    @Override
    public void sessionOpened(IoSession arg0) throws Exception {
    }
}
