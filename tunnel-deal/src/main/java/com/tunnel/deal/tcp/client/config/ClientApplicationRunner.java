package com.tunnel.deal.tcp.client.config;

import com.tunnel.deal.mca.task.McaTask;
import com.tunnel.deal.tcp.client.netty.MCASocketClient;
import com.tunnel.deal.warninglightstrip.WarningLightStripTask;
import com.zc.common.core.ThreadPool.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 启动netty客户端
 */
@Component
public class ClientApplicationRunner implements ApplicationRunner {

    @Autowired
    private McaTask mcaTask;

    @Autowired
    private WarningLightStripTask warningLightStripTask;



//    /**
//     * 重连次数，默认是1次
//     */
//    @Value("${mca.client.reconnect_times:1}")
//    public Integer reconnectTimes;
    @Override
    public void run(ApplicationArguments args) throws Exception {

        ThreadPool.executor.execute(() -> {
            try {
                //初始化netty客户端，重连次数设置
                MCASocketClient.getInstance().init(1);

                //项目启动时缓存设备信息，与设备建立通信通道
                mcaTask.connect();
                warningLightStripTask.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



}
