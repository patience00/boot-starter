package com.linchtech.boot.starter.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ExecutorService;

/**
 * 定时任务多线程执行配置
 * @author 107
 * @date 2019/8/21 15:09
 */
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    @Autowired
    @Qualifier(value = "scheduleThreadPool")
    private ExecutorService executorService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(executorService);
    }
}