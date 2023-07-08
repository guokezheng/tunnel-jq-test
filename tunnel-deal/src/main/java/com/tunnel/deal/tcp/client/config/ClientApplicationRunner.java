package com.tunnel.deal.tcp.client.config;

import com.tunnel.deal.mca.task.McaTask;
import com.tunnel.deal.tcp.client.netty.MCASocketClient;
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



//    /**
//     * 重连次数，默认是1次
//     */
//    @Value("${mca.client.reconnect_times:1}")
//    public Integer reconnectTimes;
    @Override
    public void run(ApplicationArguments args) throws Exception {


        //项目启动时缓存设备信息
        addDeviceInfoCache();

        ThreadPool.executor.execute(() -> {
            try {
                //重连次数设置
                MCASocketClient.getInstance().init(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public void addDeviceInfoCache(){
        //缓存测控执行器设备信息
        mcaTask.macInfoCache();
    }
}
