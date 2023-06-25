package com.tunnel.deal.guidancelamp.control;

import com.tunnel.deal.guidancelamp.control.util.MyDecoder;
import com.tunnel.deal.guidancelamp.protocol.RpcRequest;
import com.tunnel.deal.guidancelamp.protocol.StringUtil;
import com.tunnel.deal.guidancelamp.protocol.decoder.DynamicDecoderDec;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;

public class NettyClient {

    private static final Logger log = LoggerFactory.getLogger(NettyClient.class);

    private final Bootstrap bootstrap = new Bootstrap();

    private final NioEventLoopGroup group = new NioEventLoopGroup();

    //动态协议16进制 协议推送
    public static final int PUSH_DYNAMIC_STRING_HEX = 0;
    //动态协议10进制 诱导灯推送
    public static final int PUSH_DYNAMIC_INDUCTION_DEC = 1;

    public static final int PUSH_TYPE_STRING = 2;

    public static final int PUSH_TYPE_STR = 3;

    public static final int PUSH_TYPE_JSON = 4;
    //区域协议推送
    public static final int PUSH_DYNAMIC_TERRITORY_PROTOCOL = 5;


    public final int PUSH_CODE_LENT = 2048;

    public int OVERTIME = 10; //响应超时时间s

    private final String host;

    private final int port;

    private  String code;

    private RpcRequest rpcRequest;

    private final int type; //推送格式：0 动态协议 1 动态协议10进制 3 字符串 4 JSON 2 三思协议

    private int opeType;

    private Channel channel;

    private ClientHandler clientHandler;

    @PostConstruct
    private void init() {
        //设置线程池
        bootstrap.group(group);
        //设置超时
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,3000);
        //设置socket工厂
        bootstrap.channel(NioSocketChannel.class);
        switch (type){
            case  PUSH_TYPE_STR:
                //设置管道
                bootstrap.handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new MyDecoder());// 字符串解码器
                        ch.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));// 字符串编码器
                        // 业务处理器
                        clientHandler = new ClientHandler(group);
                        ch.pipeline().addLast(clientHandler);
                    }
                });
                break;
            case  PUSH_DYNAMIC_INDUCTION_DEC:
                //设置管道
                bootstrap.handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new DynamicDecoderDec());// 字符串解码器
                        ch.pipeline().addLast();// 字符串编码器
                        // 业务处理器
                        clientHandler = new ClientHandler(group);
                        ch.pipeline().addLast(clientHandler);
                    }
                });
                break;
            default:log.info("无操作,请联系管理员。");
        }
    }


    public void start(RpcRequest rpcRequest) throws Exception {
        switch (type){
            //测试类型
            case  PUSH_TYPE_STR:    startReturnString();    break;
            //测试json类型
            case  PUSH_TYPE_JSON:   startReturnJSON(rpcRequest);  break;
            //三思通信协议
            case PUSH_TYPE_STRING: startReturnStringSansi();  break;
            //动态协议推送
            case PUSH_DYNAMIC_STRING_HEX: startDynamicString();  break;
            //动态协议10进制 诱导灯推送
            case PUSH_DYNAMIC_INDUCTION_DEC: startDynamicString();  break;
            //区域协议推送
            case PUSH_DYNAMIC_TERRITORY_PROTOCOL: startDynamicString();  break;
            default:log.info("无操作,请联系管理员。");
        }
    }


    public ClientHandler getClientHandler() {
        return clientHandler;
    }


    public void startReturnString() throws InterruptedException {
        //发起异步连接请求，绑定连接端口和host信息
        final ChannelFuture future = bootstrap.connect(host, port).sync();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture arg0) throws Exception {
                if (future.isSuccess()) {
                    log.info("连接服务器成功");
                } else {
                    log.error("连接服务器失败");
                    future.cause().printStackTrace();
                    group.shutdownGracefully(); //关闭线程组
                }
            }
        });
        this.channel = future.channel();
        String hexCode = code;
        //netty需要用ByteBuf传输
        ByteBuf bufff = Unpooled.buffer();
        // 字符串传输
        bufff.writeBytes(StringUtil.hexString2Bytes(hexCode));
        channel.writeAndFlush(bufff);
    }

    //动态创建连接
    public void startDynamicString() throws InterruptedException {
        //发起异步连接请求，绑定连接端口和host信息
        final ChannelFuture future = bootstrap.connect(host, port).sync();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture arg0) throws Exception {
                if (future.isSuccess()) {
                    log.info("连接服务器成功");
                } else {
                    log.error("连接服务器失败");
                    future.cause().printStackTrace();
                    group.shutdownGracefully(); //关闭线程组
                }
            }
        });
        this.channel = future.channel();
    }

    /**
     * 三思
     * @throws InterruptedException
     */
    public void startReturnStringSansi() throws InterruptedException {
        //发起异步连接请求，绑定连接端口和host信息
        final ChannelFuture future = bootstrap.connect(host, port).sync();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture arg0) throws Exception {
                if (future.isSuccess()) {
                    log.info("连接服务器成功");
                } else {
                    log.error("连接服务器失败");
                    future.cause().printStackTrace();
                    group.shutdownGracefully(); //关闭线程组
                }
            }
        });
        this.channel = future.channel();
    }


    /**
     * 客户端推送16进制数据
     * @param hexCode
     * @return
     */
    public void pushHexCode(String hexCode){
        ByteBuf bufff = Unpooled.buffer();
        // 字符串传输
        bufff.writeBytes(StringUtil.hexString2Bytes(hexCode));
        channel.writeAndFlush(bufff);
    }

    /**
     * 客户端推送数据
     * @param code
     * @return
     */
    public void pushCode(String code){
        ByteBuf bufff = Unpooled.buffer();
        // 字符串传输
        bufff.writeBytes(code.getBytes());
        channel.writeAndFlush(bufff);
    }



    public void startReturnJSON(RpcRequest rpcRequest) throws InterruptedException {
        //发起异步连接请求，绑定连接端口和host信息
        final ChannelFuture future = bootstrap.connect(host, port).sync();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture arg0) throws Exception {
                if (future.isSuccess()) {
                    log.info("连接服务器成功");
                } else {
                    log.error("连接服务器失败");
                    future.cause().printStackTrace();
                    group.shutdownGracefully(); //关闭线程组
                }
            }
        });
        // 写入 RPC 请求数据并关闭连接
        this.channel = future.channel();
        System.out.println(rpcRequest.toString());
        channel.writeAndFlush(rpcRequest);
    }


    //连接服务端的端口号地址和端口号   type 解码格式 0  字符串 1 JSON
    public NettyClient(String host, int port, String code, int type) {
        this.host = host;
        this.port = port;
        this.code = code;
        this.type = type;
        init();
    }

    //连接服务端的端口号地址和端口号   type 解码格式 0  字符串 1 JSON
    public NettyClient(String host, int port, int type) {
        this.host = host;
        this.port = port;
        this.type = type;
        init();
    }

    public int getType() {
        return type;
    }


    public void stop(){
        group.shutdownGracefully();
    }
}
