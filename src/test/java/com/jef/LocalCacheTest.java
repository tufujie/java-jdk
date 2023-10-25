package com.jef;

import com.jef.business.BusinessDemo;
import com.jef.constant.BasicConstant;
import com.jef.entity.User;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存
 * HashMap
 * ConcurrentHashMap
 * Guava Cache
 * Ehcache
 *
 * @author Jef
 * @date 2023/10/25
 */
public class LocalCacheTest {

    @DisplayName("GuavaCache测试")
    @Test
    void testGuavaCache() throws ExecutionException {
        LoadingCache<Long, User> cache = CacheBuilder.newBuilder()
                // 缓存最大数量
                .maximumSize(1000)
                // 缓存有效期
                .expireAfterWrite(10, TimeUnit.MINUTES)
                // 缓存并发处理
                .concurrencyLevel(2)
                .build(
                        // 缓存加载方式
                        new CacheLoader<Long, User>() {
                            @Override
                            public User load(Long key) throws Exception {
                                return BusinessDemo.getDataFromDatabase(key);
                            }

                        });
        cache.refresh(BasicConstant.ID);
        cache.refresh(BasicConstant.ID_TWO);
        User user = cache.get(BasicConstant.ID);
        Assertions.assertEquals(BasicConstant.ID, user.getId());
    }

    @DisplayName("Ehcache测试")
    @Test
    void testEhcache() throws ExecutionException {
        LoadingCache<Long, User> cache = CacheBuilder.newBuilder()
                // 缓存最大数量
                .maximumSize(1000)
                // 缓存有效期
                .expireAfterWrite(10, TimeUnit.MINUTES)
                // 缓存并发处理
                .concurrencyLevel(2)
                .build(
                        // 缓存加载方式
                        new CacheLoader<Long, User>() {
                            @Override
                            public User load(Long key) throws Exception {
                                return BusinessDemo.getDataFromDatabase(key);
                            }

                        });
        cache.refresh(BasicConstant.ID);
        cache.refresh(BasicConstant.ID_TWO);
        User user = cache.get(BasicConstant.ID);
        Assertions.assertEquals(BasicConstant.ID, user.getId());
    }
}