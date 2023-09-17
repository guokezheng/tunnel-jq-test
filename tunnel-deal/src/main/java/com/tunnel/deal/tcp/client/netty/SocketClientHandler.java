package com.tunnel.deal.tcp.client.netty;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralBean;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralService;
import com.tunnel.deal.tcp.util.ByteBufUtil;
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

    private ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);

    private TcpClientGeneralService tcpClientGeneralService = SpringUtils.getBean(TcpClientGeneralService.class);

    /**
     * 欧姆龙默认端口（短连接，特殊处理）
     */
    private static final Integer OMRON_PLC__DEFAULT_PORT = 9600;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        try{
            Channel channel = ctx.channel();
            InetSocketAddress ipSocket = (InetSocketAddress)channel.remoteAddress();
            String ip = ipSocket.getAddress().getHostAddress();
//            int port = ipSocket.getPort();
            String strMsg = ByteBufUtil.convertByteBufToString(msg);
//            log.info("回复指令：ip="+ip+",msg="+strMsg);
//            System.out.println("回复指令：ip="+ip+",msg="+strMsg);


           String deviceId = tcpClientGeneralService.getDeviceIdByIp(ip);
            System.out.println("响应命令：ip="+ip+",cmd="+strMsg+","+"设备id="+deviceId+",时间："+System.currentTimeMillis());
            SdDevices sdDevices = sdDevicesService.selectSdDevicesById(deviceId);
            //设备解析类
            TcpClientGeneralBean tcpClientGeneralBean = tcpClientGeneralService.getProtocolBean(sdDevices);
            if(tcpClientGeneralBean != null){
                tcpClientGeneralBean.handleReadData(ip,deviceId,strMsg);
            }else{
                log.error("设备协议配置为空");
            }

//            //数据解析
//            mcaDataParse.dataParse(ip,jsonObject);

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
//        log.error("handlerRemoved：handler从Pipeline中移除");

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
        String deviceId =  tcpClientGeneralService.getDeviceIdByIp(ip);
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
        if(port != OMRON_PLC__DEFAULT_PORT){
            //除了欧姆龙PLC短连接，普通长连接需要在断连后额外处理
            String deviceId =  tcpClientGeneralService.getDeviceIdByIp(ip);
            //设置设备及子设备离线
            sdDevicesService.updateOfflineStatus(deviceId,true);
            log.error("channelInactive:访问地址="+ip+":"+port);
            //连接失败，重新连接
            TcpNettySocketClient.getInstance().connect(ip,port);
        }

        channel.close();
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
        channel.close();
    }

}
