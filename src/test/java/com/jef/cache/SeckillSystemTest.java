package com.jef.cache;

import com.jef.business.BusinessDemo;
import com.jef.redis.RedisJavaUtil;

import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 秒杀系统设计思路
 *
 * @author Jef
 * @date 2023/10/30
 */
public class SeckillSystemTest {

    @Test
    void testSeckillSystem() {
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        // 产品数量
        int productLimit = 100;
        AtomicInteger atomicInteger = new AtomicInteger(productLimit);
        // 抢产品的数量
        int userNum = 1000000;
        String productName = "XiaoMi Phone";
        RedissonClient redissonClient = RedisJavaUtil.getRedissonClient();
        for (int userId = 1; userId <= userNum; userId++) {
            RLock lock = redissonClient.getLock(productName + "Lock");
            if (RedisJavaUtil.tryLock(lock, 2, TimeUnit.SECONDS)) {
                if (atomicInteger.getAndDecrement() <= 0) {
                    break;
                }
                try {
                    jedis.rpush(productName, String.valueOf(userId));
                    BusinessDemo.doSomeThing();
                } finally {
                    lock.unlock();
                }
            }
        }
        List<String> list = jedis.lrange(productName, 0, productLimit);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("秒杀成功的用户ID为: " + list.get(i));
        }
        jedis.del(productName);
    }

}