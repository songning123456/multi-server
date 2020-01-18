package com.es.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author songning
 * @date 2020/1/18
 * description
 */
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication(scanBasePackages = {"com.es.blog.**", "com.sn.common.**"}, exclude = {DataSourceAutoConfiguration.class})
public class EsBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsBlogApplication.class, args);
    }
}
