package com.tunnel.deal.xiaofangpao.client;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesBrandEnum;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NettyClient {

    private static final Logger log = LoggerFactory.getLogger(com.tunnel.deal.guidancelamp.control.NettyClient.class);


    /**
     * 传入的重连次数
     */
    private int reconnectTimes;

    public static NettyClient getInstance(){
        return NettyClient.NettyClientHolder.instance;
    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
     */
    private static class NettyClientHolder{
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static NettyClient instance = new NettyClient();
    }

    public void init(int times){
        this.reconnectTimes = times;
        start();
        connect();
    }

    private EventLoopGroup workerGroup = new NioEventLoopGroup(1);
    private int port = 8001;
    private String host="10.168.69.3";

    private Bootstrap bootstrap;

    public void start()  {

        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.KE_DA_LI_AN.getCode());
        List<ExternalSystem> list = SpringUtils.getBean(IExternalSystemService.class).selectExternalSystemList(system);

        if(list.size() == 0){
            log.error(DevicesBrandEnum.KE_DA_LI_AN.getName() + "第三方系统未配置");
            return;
        }

        String[] url = list.get(0).getSystemUrl().length() > 0 ? list.get(0).getSystemUrl().split(":"):null;

        if(url.length != 3){
            log.error(DevicesBrandEnum.KE_DA_LI_AN.getName() + "第三方系统地址未配置");
            return;
        }

        host = url[1].replaceAll("/","");
        port = Integer.parseInt(url[2]);

        bootstrap = new Bootstrap();
        // 客户端不需要处理连接 所以一个线程组就够了
        bootstrap.group(workerGroup)
            // 连接通道
            .channel(NioSocketChannel.class)
            .remoteAddress(host, port)
            .option(ChannelOption.TCP_NODELAY, true)
            // 数据处理
            .handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline().addLast(new NettyClientHandler());
                }
        });
    }
    public void connect() {
        try {
            bootstrap.connect().addListener(new ConnectionListener()).sync();
        }catch (Exception e){
            log.error("消防炮连接异常,访问地址="+host+":"+port,e.getMessage());
        }
    }

}
