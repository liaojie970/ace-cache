package com.ace.cache.api.impl;

import com.ace.cache.api.CacheAPI;
import com.ace.cache.config.properties.RedisProperties;
import com.ace.cache.constants.CacheConstants;
import com.ace.cache.entity.CacheBean;
import com.ace.cache.service.IRedisService;
import com.ace.cache.utils.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * redis缓存
 * <p/>
 * 解决问题：
 *
 * @author Ace
 * @version 1.0
 * @date 2017年5月4日
 * @since 1.7
 */
@Service
public class CacheRedis implements CacheAPI {
    @Autowired
    private RedisProperties redisProperties;

    @Autowired
    private IRedisService redisCacheService;

    @Override
    public String get(String key) {
        if (!isEnabled())
            return null;
        if (StringUtils.isBlank(key)) {
            return null;
        }
        CacheBean cache = getCacheBean(key);
        if (cache != null) {
            if (cache.getExpireTime().getTime() > new Date().getTime()) {
                return redisCacheService.get(cache.getKey());
            } else {
                redisCacheService.del(addSys(key));
                redisCacheService.del(cache.getKey());
            }
        }
        return null;
    }

    @Override
    public void set(String key, Object value, int expireMin) {
        set(key, value, expireMin, "");
    }

    @Override
    public Long remove(String key) {
        if (!isEnabled())
            return 0L;
        if (StringUtils.isBlank(key))
            return 0L;
        try {
            CacheBean cache = getCacheBean(key);
            if (cache != null) {
                redisCacheService.del(addSys(key));
                redisCacheService.del(cache.getKey());
            }
        } catch (Exception e) {
            return 0L;
        }
        return 1L;
    }

    @Override
    public Long remove(String... keys) {
        if (!isEnabled())
            return null;
        try {
            for (int i = 0; i < keys.length; i++) {
                remove(keys[i]);
            }
        } catch (Exception e) {
            return 0L;
        }
        return 1l;
    }

    @Override
    public Long removeByPre(String pre) {
        if (!isEnabled())
            return 0L;
        if (StringUtils.isBlank(pre))
            return 0L;
        try {
            Set<String> result = redisCacheService.getByPre(addSys(pre));
            List<String> list = new ArrayList<String>();
            for (String key : result) {
                CacheBean cache = getCacheBean(key);
                list.add(cache.getKey());
            }
            redisCacheService.del(list.toArray(new String[]{}));
            redisCacheService.delPre(addSys(pre));
        } catch (Exception e) {
            return 0L;
        }
        return 1L;
    }

    private CacheBean getCacheBean(String key) {
        key = this.addSys(key);
        CacheBean cache = parseCacheBean(key);
        return cache;
    }

    /**
     * 加入系统前缀
     *
     * @param key
     * @return
     * @author Ace
     * @date 2017年5月4日
     */
    @Override
    public String addSys(String key) {
        String result;
        String sys = redisProperties.getSysName();
        if (key.startsWith(sys))
            result = key;
        else
            result = sys + ":" + key;
        return result;
    }

    @Override
    public void set(String key, Object value, int expireMin, String desc) {
        if (StringUtils.isBlank(key) || value == null || StringUtils.isBlank(value.toString()))
            return;
        if (!isEnabled())
            return;
        String realValue;
        try {
            if (value instanceof String) {
                realValue = value.toString();
            } else {
                realValue = JacksonUtils.obj2jsonIgnoreNull(value);
            }
            String realKey = CacheConstants.PRE + addSys(key);
            Date time = new DateTime(redisCacheService.getExpireDate(realKey)).plusMinutes(expireMin).toDate();
            CacheBean cache = new CacheBean(realKey, desc, time);
            String result = JacksonUtils.obj2jsonIgnoreNull(cache);
            redisCacheService.set(addSys(key), result, expireMin * 60);
            redisCacheService.set(realKey, realValue, expireMin * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CacheBean> listAll() {
        Set<String> result = redisCacheService.getByPre(addSys(""));
        List<CacheBean> caches = new ArrayList<CacheBean>();
        if (result == null)
            return caches;
        Iterator<String> it = result.iterator();
        String key;
        CacheBean cache;
        while (it.hasNext()) {
            key = it.next();
            cache = parseCacheBean(key);
            if (cache == null)
                continue;
            cache.setKey(key);
            caches.add(cache);
        }
        return caches;
    }

    private CacheBean parseCacheBean(String key) {
        CacheBean cache;
        try {
            cache = JacksonUtils.json2pojo(redisCacheService.get(key), CacheBean.class);
        } catch (Exception e) {
            cache = new CacheBean();
            cache.setKey(key);
            cache.setExpireTime(redisCacheService.getExpireDate(key));
        }
        return cache;
    }

    @Override
    public List<CacheBean> getCacheBeanByPre(String pre) {
        if (StringUtils.isBlank(pre)) {
            return new ArrayList<CacheBean>();
        }
        Set<String> result = redisCacheService.getByPre(pre);
        Iterator<String> it = result.iterator();
        List<CacheBean> caches = new ArrayList<CacheBean>();
        String key;
        while (it.hasNext()) {
            key = it.next();
            CacheBean cache = parseCacheBean(key);
            cache.setKey(key);
            caches.add(cache);
        }
        return caches;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.parseBoolean(redisProperties.getEnable());
    }
}
