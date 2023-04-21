package com.tunnel.deal.plc.omron.finsTCP;


import com.tunnel.deal.plc.omron.OmronConnectProperties;
import com.tunnel.deal.plc.omron.exception.PlcException;
import com.tunnel.deal.plc.omron.util.ByteUtil;
import com.tunnel.deal.plc.omron.util.OmronUtils;
import com.zc.common.core.ThreadPool.ThreadPool;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
    public boolean doBuildResponseMessage(byte[] responseMessage) throws PlcException {
        byte[] message = responseMessage;
        // 读
        if(responseMessage[27] == 0x01) {
            this.data = OmronUtils.responseValidAnalysis(message, true);
            return true;
        } else { // 写
            OmronUtils.responseValidAnalysis(message, false);
            return true;
        }
    }



    /**
     * 握手信号报文
     * @param future
     */
    public ByteBuf successCallback(ChannelFuture future) {
        // 发送PLC的握手报文
//        future.channel().writeAndFlush(Unpooled.wrappedBuffer(handSingleMessage));
        return Unpooled.wrappedBuffer(handSingleMessage);
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



    //欧姆龙测试
    public static void main(String[] args) throws InterruptedException, PlcException {
//        OmronConnectProperties conf = new OmronConnectProperties();
//        //DA1地址： 服务器 ip
//        conf.setHost("10.7.187.87");
//        //SA1地址：电脑 ip
//        conf.setLocalHost("127.0.0.1");
//        //服务器port
//        conf.setPort(9600);
//        //创建 client
//        OmronTcpClient omronTcpClient = new OmronTcpClient(conf);
//        //初始化链接     服务器地址
//        omronTcpClient.init("10.7.187.87",9600);
//
//        ChannelFuture channelFuture = omronTcpClient.channelFuture;
//        //推送握手协议
//        omronTcpClient.send(omronTcpClient.successCallback(channelFuture).array());
//
//        //写入数据
////        byte[] writeData = ByteUtil.getBytes(1);
//        boolean isBit = false;
////        byte[] dataInfow =omronTcpClient.buildWriteRequestBody(writeData,"W101.01",true);
//////        byte[] dataInfow =omronTcpClient.buildWriteRequestBody(writeData,"D2000",isBit);
////        OmronMessageHeader  omronMessageHeaderw = new OmronMessageHeader();
////        byte[] dataBodyw = omronTcpClient.getMessageBody(omronMessageHeaderw,dataInfow.length);
////        System.out.println("写入数据："+ByteUtil.bytesToHex(byteMerger(dataBodyw,dataInfow)));
////        byte[] data = omronTcpClient.send(byteMerger(dataBodyw,dataInfow));
////        System.out.println(ByteUtil.bytesToHex(data));
////        System.out.println(omronTcpClient.doBuildResponseMessage(data));
//
//        //读取数据
//        OmronMessageHeader  omronMessageHeader = new OmronMessageHeader();
//        byte[] dataInfo = omronTcpClient.buildReadRequestBody("D512",isBit);
////        byte[] dataInfo = omronTcpClient.buildReadRequestBody("D2000",isBit);
//        byte[] dataBody = omronTcpClient.getMessageBody(omronMessageHeader,dataInfo.length);
//        byte[] dataR = omronTcpClient.send(ByteUtil.byteMerger(dataBody,dataInfo));
//        System.out.println("读取数据："+ByteUtil.bytesToHex(dataR));
//        String num;
//        if(omronTcpClient.doBuildResponseMessage(dataR)){
//            //解析当前数据  根据设备点位信息解析
//            if(isBit){
//                //获取结尾2字节  结果集
//                num = ByteUtil.bytesToIntOfReverse2Byte(dataR,dataR.length-2)+"";
//                System.out.println("解析数据为:"+num);
//            }else{
//                //获取结尾4字节  结果集
////                num =  ByteUtil.bytesToFloatOfReverseToChar(dataR,dataR.length-4)+"";
////                System.out.println("解析数据为:"+num);
//                num =  ByteUtil.bytesToIntByChar(dataR,dataR.length-4)+"";
//                System.out.println("解析数据为:"+num);
//            }
//        }
    }




    /**
     * 根据数据类型解析数据
     * @param bytes
     * @param type
     * @return
     */
    public String getCodeByDataType(byte[] bytes, String type){
        String number = "";
        if(type!=null){
            switch (type){
                case "int":
                    //int
                    number = ByteUtil.bytesToIntByChar(bytes,bytes.length-4)+"";
                    break;
                case "float":
                    //float
                    number =  ByteUtil.bytesToFloatOfReverseToChar(bytes,bytes.length-4)+"";
                    break;
            }
        }
        return number;
    }



    public String readBin(boolean isBit,String address,OmronTcpClient omronTcpClient) {
        String number = "";
        byte[] data;
        try {
            //查看当前点位信息是否为   位信息。
            //读取数据
            byte[] dataInfo = omronTcpClient.buildReadRequestBody(address, isBit);
            OmronMessageHeader omronMessageHeader = new OmronMessageHeader();
            byte[] dataBody = omronTcpClient.getMessageBody(omronMessageHeader, dataInfo.length);
            data = omronTcpClient.send(ByteUtil.byteMerger(dataBody, dataInfo));
            //返回数据校验        true  成功   false  失败
            if (!omronTcpClient.doBuildResponseMessage(data)) {
                throw new RuntimeException("返回数据校验失败。");
            }else{
                //解析当前数据  根据设备点位信息解析
                //获取结尾2字节  结果集
                number = ByteUtil.bytesToIntOfReverse2Byte(data, data.length - 2)+"";
                return number;
            }
        } catch (PlcException e) {
            e.printStackTrace();
        }
        return number;
    }




    public String readReal(boolean isBit,String address,String dataType,OmronTcpClient omronTcpClient) {
        String number = "";
        byte[] data;
        try {
            //查看当前点位信息是否为   位信息。
            //读取数据
            byte[] dataInfo = omronTcpClient.buildReadRequestBody(address, isBit);
            OmronMessageHeader omronMessageHeader = new OmronMessageHeader();
            byte[] dataBody = omronTcpClient.getMessageBody(omronMessageHeader, dataInfo.length);
            data = omronTcpClient.send(ByteUtil.byteMerger(dataBody, dataInfo));
            //返回数据校验        true  成功   false  失败
            if (!omronTcpClient.doBuildResponseMessage(data)) {
                throw new RuntimeException("返回数据校验失败。");
            }else{
                //解析当前数据  根据设备点位信息解析
                //获取结尾4字节  结果集
                number = omronTcpClient.getCodeByDataType(data, dataType);
                return number;
            }
        } catch (PlcException e) {
            e.printStackTrace();
        }
        return number;
    }


    /**
     *
     * @param data
     * @param isBit
     * @param address
     * @param omronTcpClient
     * @return
     */
    public boolean writeBin(byte[] data,boolean isBit,String address,OmronTcpClient omronTcpClient) {
        try {
            //查看当前点位信息是否为   位信息。
            //读取数据
            byte[] dataInfo = omronTcpClient.buildWriteRequestBody(data,address, isBit);
            OmronMessageHeader omronMessageHeader = new OmronMessageHeader();
            byte[] dataBody = omronTcpClient.getMessageBody(omronMessageHeader, dataInfo.length);
            data = omronTcpClient.send(ByteUtil.byteMerger(dataBody, dataInfo));
            //返回数据校验        true  成功   false  失败
            return omronTcpClient.doBuildResponseMessage(data);
        } catch (PlcException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean writeReal(byte[] data,boolean isBit,String address,String dataType,OmronTcpClient omronTcpClient) {
        try {
            //查看当前点位信息是否为   位信息。
            //读取数据
            byte[] dataInfo = omronTcpClient.buildWriteRequestBody(data,address, isBit);
            OmronMessageHeader omronMessageHeader = new OmronMessageHeader();
            byte[] dataBody = omronTcpClient.getMessageBody(omronMessageHeader, dataInfo.length);
            data = omronTcpClient.send(ByteUtil.byteMerger(dataBody, dataInfo));
            //返回数据校验        true  成功   false  失败
            return omronTcpClient.doBuildResponseMessage(data);
        } catch (PlcException e) {
            e.printStackTrace();
        }
        return false;
    }


}
