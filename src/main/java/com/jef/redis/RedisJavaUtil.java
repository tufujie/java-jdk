package com.jef.redis;

import com.jef.util.PrintUtil;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

/**
 * @author Jef
 */
public class RedisJavaUtil {

    /**
     * 获取认证的Jedis对象
     *
     * @return
     */
    public static Jedis getAuthJedis() {
        // 连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        // 使用 CONFIG SET requirepass "root"，其中root可自定义，如果已经设置了，
        // 可以通过CONFIG GET requirepass获取
        // (error) NOAUTH Authentication required.如果出现这个错误，则说明没有验证，使用AUTH yourpassword进行验证
        jedis.auth("root");
        System.out.println("Redis连接成功");
        // 查看服务是否运行
        System.out.println("服务正在运行: " + jedis.ping());
        PrintUtil.printSplitLine();
        return jedis;
    }

    private static final String LOCK_SUCCESS = "OK";
    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 尝试获取分布式锁
     *
     * @param jedis                Redis客户端
     * @param lockKey              锁
     * @param requestId            请求标识
     * @param millisecondsToExpire 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, long millisecondsToExpire) {
        String result = jedis.set(lockKey, requestId, SetParams.setParams().nx().px(millisecondsToExpire));
        return LOCK_SUCCESS.equals(result);
    }

    /**
     * 释放分布式锁
     *
     * @param jedis     Redis客户端
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        return RELEASE_SUCCESS.equals(result);
    }

    /**
     * 获取RedissonClient
     *
     * @return RedissonClient
     */
    public static RedissonClient getRedissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:6379").setPassword("root");
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

}
