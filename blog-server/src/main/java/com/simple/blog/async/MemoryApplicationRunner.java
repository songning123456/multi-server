package com.simple.blog.async;

import com.simple.blog.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author songning
 * @date 2020/1/8
 * description
 */
@Slf4j
@Component
public class MemoryApplicationRunner implements ApplicationRunner {

    @Autowired
    private CacheService cacheService;

    @Override
    @Async
    public void run(ApplicationArguments arguments) {
        cacheService.refreshLabelConfig();
        log.info("^^^^^缓存memory成功^^^^^");
    }
}
