package com.tunnel.task;

import com.tunnel.platform.pcl.netty.client.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @author YangChao
 * @description
 * @date 2021/11/23/023
 * @apiNote netty定时任务
 */
@Component("plcTask")
public class PlcTask {

    private static final Logger log = LoggerFactory.getLogger(PlcTask.class);

    /** plc请求指令下发 */
    public void clientSend() throws Exception{
        log.info("开始执行链接定时任务");
        log.info("初始化");
        NettyClient nettyClient = new NettyClient("127.0.0.1",60000);
        nettyClient.start();
        String  code = "00 00 00 00 00 0B 01 10 00 00 00 02 04 00 01 00 02".replace(" ","");
        log.info("拼接推送数据:"+code);
        //定时请求数据拼接需要请求节点
                
        nettyClient.pushHexCode(code);
        nettyClient.stop();
        log.info("断开链接");

    }
}
