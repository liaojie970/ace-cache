package com.ace.cache.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import redis.clients.jedis.JedisPool;

/**
 * @Author：v
 * @Description：
 * @Date: 2018/8/29
 * @Modified By:
 */
public class RedisProperties {
    private PoolProperties pool;
    private String host;
    private String password;
    private String timeout;
    private String database;
    private String port;
    private String enable;
    private String sysName;

    public PoolProperties getPool() {
        return pool;
    }

    public void setPool(PoolProperties pool) {
        this.pool = pool;
    }

    public static class PoolProperties {
        private String maxActive;
        private String maxIdle;
        private String maxWait;

        public String getMaxActive() {
            return maxActive;
        }

        public void setMaxActive(String maxActive) {
            this.maxActive = maxActive;
        }

        public String getMaxIdle() {
            return maxIdle;
        }

        public void setMaxIdle(String maxIdle) {
            this.maxIdle = maxIdle;
        }

        public String getMaxWait() {
            return maxWait;
        }

        public void setMaxWait(String maxWait) {
            this.maxWait = maxWait;
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
