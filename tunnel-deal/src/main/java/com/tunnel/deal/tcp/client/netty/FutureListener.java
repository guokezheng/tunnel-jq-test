package com.tunnel.deal.tcp.client.netty;

import com.tunnel.deal.tcp.client.config.ChannelKey;
import com.tunnel.deal.tcp.client.config.ReconnectManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * describe:
 *
 * @author zs
 * @date 2021/2/21
 */
public class FutureListener implements ChannelFutureListener {

    private static final Logger log = LoggerFactory.getLogger(FutureListener.class);
    private String ip;
    private int port;

    /**
     * 传入的重连次数
     */
    private int reconnectTimes;


    public FutureListener(String ip, int port){
        this.ip = ip;
        this.port = port;
    }
    public FutureListener(String ip, int port, int times){
        this.ip = ip;
        this.port = port;
        this.reconnectTimes = times;
    }


    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        String key = ip + ":" + port;
        Integer reconnectCount = ReconnectManager.countMap.get(key);
        if(channelFuture.isSuccess()){
            Channel channel = channelFuture.channel();
            MCASocketClient.channels.put(ChannelKey.getChannelKey(ip,port),channel);
            log.info("FutureListener：连接成功，访问地址="+ip+":"+port);
            if(reconnectCount != null && reconnectCount != -1){
//                reconnectCount = -1;
                ReconnectManager.countMap.put(key,-1);
            }
        }else{
            if( reconnectCount == null || reconnectCount == -1){
                //开始重连时，设置重连次数
                reconnectCount = this.reconnectTimes;
            }
//            if(reconnectCount == -1){
//                //开始重连时，设置重连次数
//                reconnectCount = this.reconnectTimes;
//            }
            if(reconnectCount == 0){
                //达到重连次数，不再继续尝试重连,重置重连计数变量，方便下次调用重连方法时执行重连次数策略
//                reconnectCount = -1;
                ReconnectManager.countMap.put(key,-1);
                return;
            }
            reconnectCount--;
            ReconnectManager.countMap.put(key,reconnectCount);
            //释放未成功连接的资源
            channelFuture.channel().close();
            log.info("FutureListener：连接失败，访问地址="+ip+":"+port+",重新连接");
            //连接失败，重新连接
            MCASocketClient.getInstance().connect(ip,port);
        }


//        channel.closeFuture().sync();
    }

}
