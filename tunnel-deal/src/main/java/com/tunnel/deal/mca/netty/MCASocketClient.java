package com.tunnel.deal.mca.netty;

import com.tunnel.deal.mca.config.ChannelKey;
import com.tunnel.deal.mca.config.DeviceManager;
import com.tunnel.deal.mca.util.ByteBufUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 *  MCA netty 启动类
 * @author zs
 */

public class MCASocketClient
{

    private static final Logger log = LoggerFactory.getLogger(MCASocketClient.class);

    /**
     * 存储channel
     */
    public static Map<String, Channel> channels =new ConcurrentHashMap<>();

    /**
     * 设置一个多线程循环器
     */
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    /**
     * 启动辅助类
     */
    private Bootstrap bootstrap = new Bootstrap();

    /**
     * 传入的重连次数
     */
    private int reconnectTimes;

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
     */
    private static class SocketClientHolder{
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static MCASocketClient instance = new MCASocketClient();
    }
    /**
     * 私有化构造方法
     */
    private MCASocketClient(){
//        this.init();
    }
    public static MCASocketClient getInstance(){
        return SocketClientHolder.instance;
    }

    public void init(int times){
        this.reconnectTimes = times;
        init();
    }

    public void init()
    {
        bootstrap.group(eventLoopGroup);
        //指定所使用的NIO传输channel
        bootstrap.channel(NioSocketChannel.class);
        //指定客户端初始化处理
//        bootstrap.handler(new SocketClientChannelInitializer(){
        bootstrap.handler(new ChannelInitializer<SocketChannel>(){
            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new SocketClientHandler());
            }
        });

        deviceConnect();

//        connect("192.168.1.131",502);
    }

    /**
     * 遍历设备进行连接
     */
    public void deviceConnect(){
        //获取所有的设备ip和端口号，循环处理进行连接
        DeviceManager.deviceMap.forEach((deviceId, map) ->{
            String ip = map.get("ip") == null ? "" : map.get("ip").toString();
            String port = map.get("port") == null ? "" : map.get("port").toString();
            if(!"".equals(ip) && !"".equals(port)){
                connect(ip,Integer.valueOf(port));
            }
        });
    }

    /**
     * 创建连接
     * @param ip ip
     * @param port 端口号
     */
    public void connect(String ip,int port){
//        try
//        {
            Channel channel = channels.get(ChannelKey.getChannelKey(ip,port));
            if(channel != null && channel.isActive()){
                return;
            }
            //连接服务
            ChannelFuture channelFuture = bootstrap.connect(ip, port);
            log.info("MCASocketClient 连接:访问地址="+ip+":"+port);
             channelFuture.addListener(new FutureListener(ip,port,reconnectTimes));
//        channelFuture.addListener(new FutureListener(ip,port));
//        } finally
//        {
//            eventLoopGroup.shutdownGracefully();

//            log.info("MCASocketClient 连接ip="+ip+",port="+port+"出现异常");
//        }
    }

    public static void sendMsg(String ip,String port,String msg) throws Exception {
        Channel channel = MCASocketClient.channels.get(ChannelKey.getChannelKey(ip,Integer.valueOf(port)));
        if (channel != null) {
            channel.writeAndFlush(msg).sync();
        } else {
            log.info("消息发送失败,连接尚未建立!");
        }
    }

    public static void main(String[] args) throws InterruptedException, DecoderException {
        /**
         * 设置一个多线程循环器
         */
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        /**
         * 启动辅助类
         */
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        //指定所使用的NIO传输channel
        bootstrap.channel(NioSocketChannel.class);
        //指定客户端初始化处理
//        bootstrap.handler(new SocketClientChannelInitializer(){
        bootstrap.handler(new ChannelInitializer<SocketChannel>(){
            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new SocketClientHandlerss());
            }
        });
        //连接服务
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9700).sync();
        channelFuture.channel().writeAndFlush(ByteBufUtil.convertStringToByteBuf("04E20000000601010000000A"));;
        channelFuture.addListener(new FutureListener("127.0.0.1", 9700,2));
        MCASocketClient.channels.get(ChannelKey.getChannelKey("127.0.0.1", 9700));
    }



}
