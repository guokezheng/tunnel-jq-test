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
 *
 * @author zhai
 * @date 2022/11/1
 */
@EnableAsync
@Configuration
public class ThreadConfig {

    private static final Logger log = LoggerFactory.getLogger(ThreadConfig.class);

    /**
     * 自定义线程池
     * 作用：管理站-》高速云推送基础数据异步调用
     * @return
     */
    @Bean(name = "pushTaskExecutor")
    public Executor pushTaskExecutor(){
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        //核心线程数
        poolTaskExecutor.setCorePoolSize(5);
        //最大线程数
        poolTaskExecutor.setMaxPoolSize(10);
        //关闭时等待任务完成
        poolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        //线程名称前缀
        poolTaskExecutor.setThreadNamePrefix("pushasicData");
        //缓存队列
        poolTaskExecutor.setQueueCapacity(10);
        //非核心线程超过时间被销毁
        poolTaskExecutor.setKeepAliveSeconds(200);
        //拒绝策略:重试添加当前任务，自动重复调用直到成功
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化
        poolTaskExecutor.initialize();
        log.info("线程池初始化完毕！！！");
        return poolTaskExecutor;
    }
}
