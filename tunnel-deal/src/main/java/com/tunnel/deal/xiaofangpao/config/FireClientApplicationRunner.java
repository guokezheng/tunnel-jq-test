package com.tunnel.deal.xiaofangpao.config;

import com.tunnel.deal.xiaofangpao.client.NettyClient;
import com.zc.common.core.ThreadPool.ThreadPool;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 启动netty客户端
 */
@Component
public class FireClientApplicationRunner implements ApplicationRunner {

//    /**
//     * 重连次数，默认是1次
//     */
//    @Value("${mca.client.reconnect_times:1}")
//    public Integer reconnectTimes;
    @Override
    public void run(ApplicationArguments args) throws Exception {

        //缓存测控执行器点位信息 todo

        ThreadPool.executor.execute(() -> {
            try {
                //重连次数设置
                NettyClient.getInstance().init(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
