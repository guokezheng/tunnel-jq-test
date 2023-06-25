package com.tunnel.platform.media.push.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.tunnel.platform.media.push.caffeine.listener.StreamRemovalListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存配置
 * */
@Configuration
public class CacheConfig {
    /*@Value("${mediaPort.timeOut}")*/
    private long timeOut = 24;
    @Bean
    public Cache<String, Object> caffeineCache() {
        return Caffeine.newBuilder()
                // 在最后一次读或者写入后开始计时，在指定的时间后过期。假如一直有请求访问该key，那么这个缓存将一直不会过期。
                .expireAfterAccess(timeOut, TimeUnit.HOURS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                //确保过期后立即删除。Caffeine的驱逐策略并不是到期直接删除。
                .scheduler(Scheduler.forScheduledExecutorService(Executors.newScheduledThreadPool(1)))
                //添加缓存移除监听
                .removalListener(new StreamRemovalListener())
                .build();
    }
}
