package com.zc.connect.nettyClient;

import com.zc.common.core.ThreadPool.ThreadPool;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @program: netty
 * @description 客户端消息处理
 * @author Athena-YangChao
 * @create: 2021-11-16
 **/
@Component
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);


    /**
     * 客户端接收消息处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String handlerClass = (String) ctx.channel().attr(AttributeKey.valueOf("channel.property.key")).get();
        // 利用线程池处理协议解析 不占用channelRead
        ThreadPool.executor.execute(() -> {
            try {
                Object order_combination_class = Class.forName(handlerClass).newInstance();
                Class clazz = Class.forName(handlerClass);
                Method method1 = clazz.getMethod("response_analysis", String.class);
                method1.invoke(order_combination_class, String.valueOf(msg));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * channel断开连接触发
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().close();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发handlerRemoved,ChannelGroup会自动移除客户端的Channel
        log.info("客户端断开, Channel对应的长ID：" + ctx.channel().id().asLongText());
        log.info("客户端断开, Channel对应的短ID：" + ctx.channel().id().asShortText());
    }

}
