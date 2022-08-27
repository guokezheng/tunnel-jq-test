package com.tunnel.platform.pcl.netty.client;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.ArrayList;
import java.util.List;


public class ClientHandler extends SimpleChannelInboundHandler<Object>{

    private NioEventLoopGroup group;

    private Object code;

    private List<Object> codeList;

    public boolean DOWNLOADFLAG = false;

    //处理服务端返回的数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object obj)  {
        String code = obj.toString();
        List list;
        System.out.println("服务器响应数据："+code);
        setCode(obj);
        if(getCodeList()!=null&&!getCodeList().isEmpty()){
            list  = getCodeList();
        }else{
            list = new ArrayList();
        }
        list.add(obj);
        DOWNLOADFLAG =  true;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 如果发生异常。那么就 关闭通道连接
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }


    public ClientHandler(NioEventLoopGroup group){
        this.group = group;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public void stop(){
        group.shutdownGracefully();
    }

    public List<Object> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<Object> codeList) {
        this.codeList = codeList;
    }
}
