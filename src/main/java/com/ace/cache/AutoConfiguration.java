package com.ace.cache;

import com.ace.cache.config.properties.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author ace
 * @create 2017/11/17.
 */
@ComponentScan({"com.ace.cache"})
@EnableAspectJAutoProxy
public class AutoConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "redis")
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }
}
