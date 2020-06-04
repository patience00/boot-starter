package com.linchtech.boot.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 107
 * @date 2020-06-03 22:40
 * @description
 **/
@Data
@ConfigurationProperties(prefix = "system")
public class SystemProperties {

    /**
     * when the system occur unknown Exception, need send to
     */
    private List<String> emailAddr = new ArrayList<>();
    /**
     * 核心线程数
     */
    private Integer corePoolSize;
    /**
     * 最大线程数
     */
    private Integer maximumPoolSize;
    /**
     * 线程存活时间
     */
    private Long keepAliveTime;
    /**
     * 任务队列大小
     */
    private Integer blockingQueueSize;
}
