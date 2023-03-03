package com.tunnel.deal.plc.omron.finsTCP;


import com.tunnel.deal.plc.omron.OmronConnectProperties;
import com.tunnel.deal.plc.omron.exception.PlcException;
import com.tunnel.deal.plc.omron.util.ByteUtil;
import com.tunnel.deal.plc.omron.util.OmronUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import java.util.HashMap;
import java.util.Map;


/**
 * 欧姆龙PLC基于tcp实现的客户端
 */
public class OmronTcpClient{

    public static Map<Channel,OmronConnectProperties> channelsGroup = new HashMap<>();

    private OmronConnectProperties omronConnectProperties;

    public ChannelFuture channelFuture;

    public NioEventLoopGroup eventExecutors;


    // Futrue的请求时间，用于计算Future是否超时
    private long beginTime = System.currentTimeMillis();

    private byte[] data;

    // 握手信号报文
    private final byte[] handSingleMessage = new byte[] {
            0x46, 0x49, 0x4E, 0x53, // FINS
            0x00, 0x00, 0x00, 0x0C, // 后面的命令长度
            0x00, 0x00, 0x00, 0x00, // 命令码
            0x00, 0x00, 0x00, 0x00, // 错误码
            0x00, 0x00, 0x00, 0x01  // 节点号
    };


    public OmronTcpClient(OmronConnectProperties config) {
        this.omronConnectProperties = config;
        this.handSingleMessage[19] = config.getSA1();
    }

    public byte[] getHandSingleMessage() {
        return handSingleMessage;
    }

    public void init(String ip, int port) throws InterruptedException {
        eventExecutors = new NioEventLoopGroup();
        //创建bootstrap对象，配置参数
        Bootstrap bootstrap = new Bootstrap();
        //设置线程组
        bootstrap.group(eventExecutors)
                //设置客户端的通道实现类型
                .channel(NioSocketChannel.class)
                //使用匿名内部类初始化通道
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //添加客户端通道的处理器
                        ch.pipeline().addLast(new ObjectEncoder());
                        ch.pipeline().addLast(new OmronClientHandler());
                    }
                });
        //连接服务端
        channelFuture = bootstrap.connect(ip, port).sync();
        channelsGroup.put(channelFuture.channel(),omronConnectProperties);
    }

    /**
     * 获取关闭监听
     * @throws InterruptedException
     */
    public void getOmronListen() throws InterruptedException {
        channelFuture.channel().closeFuture().sync();
        System.out.println("服务器链接关闭.................");
        //退出，释放线程池资源
        eventExecutors.shutdownGracefully();
    }

    public void close(){
        //退出，释放线程池资源
        eventExecutors.shutdownGracefully();
    }


    /**
     * 发送数据
     *
     * @param data 数据
     */
    public byte[] send(byte[] data) {
        try {
            omronConnectProperties.setCountDownLatch(new CountDownLatch(1));
            channelFuture.channel().writeAndFlush(Unpooled.wrappedBuffer(data)).sync();
            omronConnectProperties.getCountDownLatch().await(9, TimeUnit.SECONDS);
            Map<String,Object> dataMap =  OmronClientHandler.content;
            //获取当前返回数据
            byte[] resultData = (byte[])dataMap.get(omronConnectProperties.getConnectKey());
            //将map中原有数据清楚
            dataMap.remove(omronConnectProperties.getConnectKey());
            return resultData;
        } catch (Exception e) {
            e.printStackTrace();
            omronConnectProperties.getCountDownLatch().countDown();
            return null;
        }
    }

    public long getBeginTime() {
        return beginTime;
    }

    /**
     * 返回结果校验解析
     * @param responseMessage
     */
    public boolean doBuildResponseMessage(byte[] responseMessage) {
        byte[] message = responseMessage;
        try {
            // 读
            if(responseMessage[27] == 0x01) {
                this.data = OmronUtils.responseValidAnalysis(message, true);
                System.out.println("读取成功");
                return true;
            } else { // 写
                OmronUtils.responseValidAnalysis(message, false);
                System.out.println("写入成功");
                return true;
            }
        } catch (PlcException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }



    /**
     * 握手信号报文
     * @param future
     */
    public void successCallback(ChannelFuture future) {
        // 发送PLC的握手报文
        future.channel().writeAndFlush(Unpooled.wrappedBuffer(handSingleMessage));
    }


    /**
     * 定义报文
     * @param length    数据长度（字节长度）
     */
    public byte[] getMessageBody(OmronMessageHeader omronMessageHeader,Integer length){
        //读取点位   点位长度*4 按照字符来进行读取
        omronMessageHeader.buildRequestHeader(omronConnectProperties.getSA1(),omronConnectProperties.getDA1(),length);
        return omronMessageHeader.getMessage();
    }


    /**
     * 读取
     * @param address
     * @param isBit
     * @return
     * @throws PlcException
     */
    public  byte[] buildReadRequestBody(String address,boolean isBit) throws PlcException {
        int length = 2;

        byte[] message = new byte[8];

        message[0] = message[1] = 0x01;  // 读取

        byte[] addressByte = OmronUtils.analysisAddress(address, isBit);
        System.arraycopy(addressByte, 0, message, 2, 4);

        byte[] bytes = ByteUtil.getBytes(length);
        message[6] = bytes[1];
        message[7] = bytes[0];

        return message;
    }

    public byte[] getData() {
        return data;
    }


    /**
     *
     * @param writeData     写入数据
     * @param writeAddress  写入地址
     * @param isBit         是否为字节    20.00  true     20  false
     * @return
     * @throws PlcException
     *
     *          写入数据   按照位 1字节  按照字符  2字节
     *         byte[] data = ByteUtil.hexToByte("01");
     *         byte[] bytew = omronTcpClient.buildWriteRequestBody(data,"W212.01",true);
     */
    public  byte[]  buildWriteRequestBody(byte[] writeData,String writeAddress,boolean isBit) throws PlcException {
        int length = writeData.length;
        byte[] message = new byte[8 + length];

        message[0] = 0x01;  // 写入
        message[1] = 0x02;

        byte[] address = OmronUtils.analysisAddress(writeAddress, isBit);
        System.arraycopy(address, 0, message, 2, 4);

        if (isBit) {
            message[6] = (byte) (length / 256);
            message[7] = (byte) (length % 256);
        } else {
            message[6] = (byte) (length / 2 / 256);
            message[7] = (byte) (length / 2 % 256);
        }
        System.arraycopy(writeData, 0, message, 8, length);
        return message;
    }



}
