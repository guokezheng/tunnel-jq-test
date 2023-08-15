package com.ruoyi.framework.config;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import com.ruoyi.common.utils.Threads;

/**
 * 线程池配置
 * 修改线程池大小原因：线上环境大量线程空闲
 *
 **/
@Configuration
public class ThreadPoolConfig
{
    // 核心线程池大小
    private int corePoolSize = 5;

    // 最大可创建的线程数
    private int maxPoolSize = 20;

    // 队列最大长度
    private int queueCapacity = 1000;

    // 线程池维护线程所允许的空闲时间
    private int keepAliveSeconds = 300;

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("threadPoolTaskExecutor-");
        //线程池维护线程的最大数量
        executor.setMaxPoolSize(maxPoolSize);
        //线程池维护线程的最小数量
        executor.setCorePoolSize(corePoolSize);
        //设置ThreadPoolExecutor的BlockingQueue的容量。默认为{@code Integer.MAX_VALUE}。
        //任何正值都将导致LinkedBlockingQueue实例;任何其他值将导致一个SynchronousQueue实例。
        executor.setQueueCapacity(queueCapacity);
        //空闲线程的存活时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程池对拒绝任务(无线程可用)的处理策略
        //最科学的的还是 AbortPolicy 提供的处理方式：抛出异常，由开发人员进行处理。
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 执行周期性或定时任务
     */
    @Bean(name = "scheduledExecutorService")
    protected ScheduledExecutorService scheduledExecutorService()
    {
        return new ScheduledThreadPoolExecutor(corePoolSize,
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build())
        {
            @Override
            protected void afterExecute(Runnable r, Throwable t)
            {
                super.afterExecute(r, t);
                Threads.printException(r, t);
            }
        };
    }
}
