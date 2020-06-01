package com.linchtech.boot.starter.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 线程池配置
 * @author 107
 * @date 2019/8/7 14:19
 */
@Configuration
public class ThreadPoolConfig {

    @Value("${spring.application.name}")
    private String appName;

    /**
     * 线程池
     * @return ExecutorService
     */
    @Bean(value = "threadPool")
    public ExecutorService buildConsumerQueueThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(appName + "-thread-pool-%d").build();
        return new ThreadPoolExecutor(50, 100, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(2000), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 用于spring中的{@link org.springframework.scheduling.annotation.Scheduled} 多个任务之间异步执行
     * @return ScheduledExecutorService
     */
    @Bean(value = "scheduleThreadPool")
    public ScheduledExecutorService scheduledExecutorService() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(appName + "-schedule-thread-pool-%d").build();
        return new ScheduledThreadPoolExecutor(10, namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

}
