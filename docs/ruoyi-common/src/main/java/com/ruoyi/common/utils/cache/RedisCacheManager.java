package com.ruoyi.common.utils.cache;

import com.ruoyi.common.utils.spring.SpringUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存管理器 使用redis实现
 * 
 * @author ruoyi
 */
//@Service
public class RedisCacheManager implements CacheManager
{
    /**
     * 用于shiro中用到的cache
     */
    private ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>();

    /**
     * redis cache 工具类
     */
    private RedisTemplate redisTemplate = SpringUtils.getBean("redisTemplate");

    /**
     * redis 存储超时时间默认控制为12小时
     */
    private final long expireTime = 1 * 60 * 60L;

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException
    {
        Cache<K, V> cache = caches.get(name);
        if (cache == null)
        {
            synchronized (this)
            {
                cache = new RedisCache<>(expireTime, redisTemplate);
                caches.put(name, cache);
            }
        }
        return cache;
    }
}
