package com.tunnel.deal.tcp.client.netty;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.deal.tcp.client.config.ChannelKey;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 *   netty 启动类
 * @author zs
 */

public class TcpNettySocketClient
{

    private static final Logger log = LoggerFactory.getLogger(TcpNettySocketClient.class);

    private ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);

    private TcpClientGeneralService tcpClientGeneralService = SpringUtils.getBean(TcpClientGeneralService.class);

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
        private static TcpNettySocketClient instance = new TcpNettySocketClient();
    }
    /**
     * 私有化构造方法
     */
    private TcpNettySocketClient(){
//        this.init();
    }
    public static TcpNettySocketClient getInstance(){
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

//        deviceConnect();

//        connect("192.168.1.131",502);
    }

    public void deviceSingleConnect(String ip,int port){
        connect(ip,port);
    }

    /**
     * 遍历设备进行连接
     */
    public void deviceConnect(Map<String,Map> deviceMap){
        //获取所有的设备ip和端口号，循环处理进行连接
        deviceMap.forEach((deviceId, map) ->{
            String ip = map.get("ip") == null ? "" : map.get("ip").toString();
            String port = map.get("port") == null ? "" : map.get("port").toString();
            if(!"".equals(ip) && !"".equals(port)){
                connect(deviceMap,ip,Integer.valueOf(port));
            }
        });
    }

    /**
     * 创建连接
     * @param ip ip
     * @param port 端口号
     */
    public void connect(Map<String,Map> deviceMap,String ip,int port){
        Channel channel = channels.get(ChannelKey.getChannelKey(ip,port));
        if(channel != null && channel.isActive()){
            String deviceId =  tcpClientGeneralService.getDeviceIdByIp(deviceMap,ip);
            //设置设备及子设备在线
            sdDevicesService.updateOnlineStatus(deviceId,true);
            return;
        }
        //连接服务
        ChannelFuture channelFuture = bootstrap.connect(ip, port);
        log.info("TcpNettySocketClient 连接:访问地址="+ip+":"+port);
        channelFuture.addListener(new FutureListener(ip,port,reconnectTimes));
    }

    /**
     * 创建连接
     * @param ip ip
     * @param port 端口号
     */
    public void connect(String ip,int port){
        Channel channel = channels.get(ChannelKey.getChannelKey(ip,port));
        if(channel != null && channel.isActive()){
            return;
        }
        //连接服务
        ChannelFuture channelFuture = bootstrap.connect(ip, port);
        log.info("TcpNettySocketClient 连接:访问地址="+ip+":"+port);
        channelFuture.addListener(new FutureListener(ip,port,reconnectTimes));
    }

    /**
     * 关闭通道
     * @param ip ip
     * @param port 端口号
     */
    public void disconnect(String ip,int port){
        Channel channel = channels.get(ChannelKey.getChannelKey(ip,port));
        channel.close();
    }

    /**
     * 关闭通道
     * @param channel 通道
     */
    public void disconnect(Channel channel){
        channel.close();
    }

    /**
     * 程序退出时释放资源
     */
    @PreDestroy
    public void closeNetty() {
        log.info("程序退出时释放资源!");
        //释放线程资源
        eventLoopGroup.shutdownGracefully();
        System.out.println("程序退出时释放资源");
    }

    public static void sendMsg(String ip,String port,String msg) throws Exception {
        Channel channel = TcpNettySocketClient.channels.get(ChannelKey.getChannelKey(ip,Integer.valueOf(port)));
        if (channel != null) {
            channel.writeAndFlush(msg).sync();
        } else {
            log.info("消息发送失败,连接尚未建立!");
        }
    }



}
