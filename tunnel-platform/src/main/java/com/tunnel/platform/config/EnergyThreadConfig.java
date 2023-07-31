package com.tunnel.platform.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义线程池
 * tjw
 */
//@EnableAsync
//@Configuration
public class EnergyThreadConfig {

    private static final Logger log = LoggerFactory.getLogger(EnergyThreadConfig.class);

    /**
     * 自定义线程池
     * 作用：定时同步能源数据
     * @return
     */
//    @Bean(name = "synchronousEnergyTaskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setCorePoolSize(10);
        poolTaskExecutor.setMaxPoolSize(20);
        // 设置线程活跃时间（秒）
        poolTaskExecutor.setKeepAliveSeconds(200);
        // 设置队列容量
        poolTaskExecutor.setQueueCapacity(40);
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        poolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        return poolTaskExecutor;
    }


}
