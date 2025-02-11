package com.zc.task;

import com.zc.connect.config.NettyClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author YangChao
 * @description
 * @date 2021/11/23/023
 * @apiNote netty定时任务
 */
@Component("NettyTask")
public class NettyTask {

    @Autowired
    private NettyClientConfig nettyClientConfig;

    /** 客户端指令下发 */
    public void clientSend() throws Exception{
        String order_combination = nettyClientConfig.getOrder_combination();
        Object order_combination_class = Class.forName(order_combination).newInstance();
        Class clazz = Class.forName(order_combination);
        Method method1 = clazz.getMethod("orderSend");
        method1.invoke(order_combination_class);
//        System.err.println("客户端下发定时任务");
    }
}
