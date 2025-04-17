package com.inmyhand.refrigerator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);          // 최소 스레드 수
        executor.setMaxPoolSize(30);           // 최대 스레드 수
        executor.setQueueCapacity(100);        // 큐에 대기 가능한 작업 수
        executor.setThreadNamePrefix("async-"); // 스레드 이름 접두사
        executor.initialize();
        return executor;
    }
}
