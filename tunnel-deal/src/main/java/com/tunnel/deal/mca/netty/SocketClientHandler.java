package com.tunnel.deal.mca.netty;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.deal.mca.modbus.ModbusCmdResolver;
import com.tunnel.deal.mca.service.McaDataParse;
import com.tunnel.deal.mca.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * netty socket 消息处理回调类
 * @author zs
 */
@Component
public class SocketClientHandler extends SimpleChannelInboundHandler<ByteBuf>
{

    private static final Logger log = LoggerFactory.getLogger(SocketClientHandler.class);

    private McaDataParse mcaDataParse = SpringUtils.getBean(McaDataParse.class);

    private ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        try{
            Channel channel = ctx.channel();
            InetSocketAddress ipSocket = (InetSocketAddress)channel.remoteAddress();
            String ip = ipSocket.getAddress().getHostAddress();
//            int port = ipSocket.getPort();
            String strMsg = ByteBufUtil.convertByteBufToString(msg);
//            String strMsg = "9c73000000050103020004";
//            log.info("回复指令：ip="+ip+",msg="+strMsg);
            JSONObject jsonObject = ModbusCmdResolver.commandParse(strMsg);
            //数据解析
            mcaDataParse.dataParse(ip,jsonObject);

            ctx.flush();
        }catch (Exception e){
            e.printStackTrace();
            log.error("解析数据报错：" + e.getMessage());
        }

    }


    /**
     * Handler 被加入 Pipeline 时触发（仅仅触发一次）
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
        Channel channel = ctx.channel();
        log.info("handlerAdded：handler添加到Pipeline");

    }

    /**
     * handler 被从 Pipeline 移除时触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
    {
        Channel channel = ctx.channel();
        log.error("handlerRemoved：handler从Pipeline中移除");

    }

    /**
     * channel 连接就绪时触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        Channel channel = ctx.channel();
        InetSocketAddress ipSocket = (InetSocketAddress)channel.remoteAddress();
        String ip = ipSocket.getAddress().getHostAddress();
        int port = ipSocket.getPort();
        String deviceId =  mcaDataParse.getMcaDeviceId(ip);
        //设置设备及子设备在线
        sdDevicesService.updateOnlineStatus(deviceId,true);

        log.info("channelActive:访问地址="+ip+":"+port);

    }

    /**
     * channel 断开时触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        Channel channel = ctx.channel();
        InetSocketAddress ipSocket = (InetSocketAddress)channel.remoteAddress();
        String ip = ipSocket.getAddress().getHostAddress();
        int port = ipSocket.getPort();
        String deviceId =  mcaDataParse.getMcaDeviceId(ip);
        //设置设备及子设备离线
        sdDevicesService.updateOfflineStatus(deviceId,true);
        log.error("channelInactive:访问地址="+ip+":"+port);
        //连接失败，重新连接
        MCASocketClient.getInstance().connect(ip,port);
    }

    /**
     * 异常发生时调用
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        Channel channel = ctx.channel();
        InetSocketAddress ipSocket = (InetSocketAddress)channel.remoteAddress();
        String ip = ipSocket.getAddress().getHostAddress();
        int port = ipSocket.getPort();
        log.error("exceptionCaught:访问地址="+ip+":"+port, cause.getMessage());
    }

}
