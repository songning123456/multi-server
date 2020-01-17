package com.simple.blog.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author songning
 * @date 2020/1/7
 * description
 */
@Configuration
public class ThreadExecutorConfig {
    private Integer corePoolSize = 100;
    private Integer maxPoolSize = 100;
    private Integer queueCapacity = 200;
    private Integer keepAliveSecond = 60;
    private String threadNamePrefix = "Async_";
    private Integer awaitTerminationSeconds = 60;

    @Bean(name = "TestImageExecutor")
    public Executor downloadTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(this.corePoolSize);
        executor.setMaxPoolSize(this.maxPoolSize);
        executor.setQueueCapacity(this.queueCapacity);
        executor.setKeepAliveSeconds(this.keepAliveSecond);
        executor.setThreadNamePrefix(this.threadNamePrefix);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(this.awaitTerminationSeconds);
        return executor;

    }
}
