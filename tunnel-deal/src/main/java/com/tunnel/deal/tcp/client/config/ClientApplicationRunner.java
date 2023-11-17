package com.tunnel.deal.tcp.client.config;

import com.tunnel.deal.mca.task.McaTask;
import com.tunnel.deal.tcp.client.netty.TcpNettySocketClient;
import com.tunnel.deal.tcp.plc.omron.task.OmronFinsTask;
import com.tunnel.deal.tcp.plc.ximenzi.task.WZBXiMenZiPlcTask;
import com.tunnel.deal.tcp.plc.ximenzi.task.XiMenZiPlcTask;
import com.tunnel.deal.warninglightstrip.WarningLightStripTask;
import com.zc.common.core.ThreadPool.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 启动netty客户端
 */
@Component
public class ClientApplicationRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ClientApplicationRunner.class);

    @Autowired
    private McaTask mcaTask;

    @Autowired
    private WarningLightStripTask warningLightStripTask;

    @Autowired
    private OmronFinsTask omronFinsTask;

    @Autowired
    private XiMenZiPlcTask xiMenZiPlcTask;

    @Autowired
    private WZBXiMenZiPlcTask wzbXiMenZiPlcTask;



//    /**
//     * 重连次数，默认是1次
//     */
//    @Value("${mca.client.reconnect_times:1}")
//    public Integer reconnectTimes;
    @Override
    public void run(ApplicationArguments args) throws Exception {

        ThreadPool.executor.execute(() -> {
//            System.out.println("ClientApplicationRunner--当前线程的名称："+Thread.currentThread().getName());
//            logger.error("ClientApplicationRunner--当前线程的名称："+Thread.currentThread().getName());
            try {
                //初始化netty客户端，重连次数设置
                TcpNettySocketClient.getInstance().init(1);

                //项目启动时缓存设备信息，与设备建立通信通道
                mcaTask.connect();
                warningLightStripTask.connect();
                omronFinsTask.connect();
//                xiMenZiPlcTask.connect();
                wzbXiMenZiPlcTask.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



}
