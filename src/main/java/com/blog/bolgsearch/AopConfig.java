package com.blog.bolgsearch;

import com.blog.bolgsearch.utils.aop.LogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AopConfig {

    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }
}
