package com.ace.cache.config;

import com.ace.cache.config.properties.RedisProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Autowired
    RedisProperties redisProperties;

    @Bean
    public JedisPoolConfig constructJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        // 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(Integer.parseInt(redisProperties.getPool().getMaxActive()));
        // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(Integer.parseInt(redisProperties.getPool().getMaxIdle()));
        // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(Integer.parseInt(redisProperties.getPool().getMaxWait()));
        config.setTestOnBorrow(true);

        return config;
    }

    @Bean(name = "pool")
    public JedisPool constructJedisPool() {
        String ip = redisProperties.getHost();
        int port = Integer.parseInt(redisProperties.getPort());
        String password = redisProperties.getPassword();
        int timeout = Integer.parseInt(redisProperties.getTimeout());
        int database = Integer.parseInt(redisProperties.getDatabase());
        JedisPool pool;
        if (StringUtils.isBlank(password)) {
            pool = new JedisPool(constructJedisPoolConfig(), ip, port, timeout);
        } else {
            pool = new JedisPool(constructJedisPoolConfig(), ip, port, timeout, password, database);
        }
        return pool;
    }
}
