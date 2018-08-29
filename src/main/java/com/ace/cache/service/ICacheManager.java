/**
 *
 */
package com.ace.cache.service;

import java.util.List;

import com.ace.cache.vo.CacheTree;
import com.ace.cache.entity.CacheBean;


/**
 * 解决问题：
 *
 * @author Ace
 * @version 1.0
 * @date 2017年5月3日
 * @since 1.7
 */
public interface ICacheManager {
    void removeAll();

    void remove(String key);

    void remove(List<CacheBean> caches);

    void removeByPre(String pre);

    List<CacheTree> getAll();

    List<CacheTree> getByPre(String pre);

    void update(String key, int hour);

    void update(List<CacheBean> caches, int hour);

    String get(String key);
}
