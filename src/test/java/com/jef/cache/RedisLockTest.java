package com.jef.cache;

import com.jef.business.BusinessDemo;
import com.jef.constant.BasicConstant;
import com.jef.redis.RedisJavaUtil;

import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * @author Jef
 * @date 2023/10/29
 */
public class RedisLockTest {

    /**
     * Redis分布式锁
     */
    @Test
    void testSetNx() {
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        String key = BasicConstant.LOGIN_OBJECT_KEY;
        // 实现锁的超时释放
        int expireTime = 60;
        // 在Spring中，redisTemplate.opsForValue().setIfAbsent()
        // 优化
      /*  if ("OK".equals(jedis.setex(key, expireTime, BasicConstant.USER_NAME))) {

        }*/
        if (jedis.setnx(key, BasicConstant.USER_NAME) == 1) {
            System.out.println("当前事务获取锁");
            jedis.expire(key, expireTime);
            try {
                // 业务处理
                BusinessDemo.doSomeThing();
            } finally {
                // 释放锁
                jedis.del(key);
            }

        } else {
            System.out.println("被其它事务占用");
        }
    }

    /**
     * Redis分布式锁
     * 实现库存-1
     */
    @Test
    void testSetNxV2() throws InterruptedException {
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        String LOCKKEY = "LOCKKEY";
        String STOCKKEY = "stock";
        // 初始化为10个库存
        jedis.set(STOCKKEY, "10");
        for (int i = 0; i < 20; i++) {
            String numberStr = jedis.get(STOCKKEY);
            if (numberStr == null) {
                numberStr = "0";
            }
            String requestId = UUID.randomUUID().toString();
            int stock = Integer.parseInt(numberStr);
            if (RedisJavaUtil.tryGetDistributedLock(jedis, LOCKKEY, requestId, 1000) && stock > 0) {
                Thread.sleep(500);
                stock--;
                jedis.set(STOCKKEY, String.valueOf(stock));
                System.out.println("扣减库存成功，剩余库存数=" + stock);
                RedisJavaUtil.releaseDistributedLock(jedis, LOCKKEY, requestId);
            } else {
                System.out.println("被其它事务占用或库存不足，剩余库存数=" + stock);
            }
        }
    }

    @Test
    public void testReduceStockByRedisson() {
        RedissonClient redissonClient = RedisJavaUtil.getRedissonClient();
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        RLock lock = redissonClient.getLock("redisson:stockLock");
        String STOCKKEY = "stock";
        // 初始化为10个库存
        jedis.set(STOCKKEY, "10");
        for (int i = 0; i < 20; i++) {
            try {
                // 加锁
                lock.lock();
                String stock = jedis.get(STOCKKEY);
                int stockNum = Integer.parseInt(stock);
                if (stockNum > 0) {
                    // 设置库存减1
                    stockNum--;
                    jedis.set(STOCKKEY, stockNum + "");
                    System.out.println("设置库存" + stockNum);
                } else {
                    System.out.println("库存不足");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 释放锁
                lock.unlock();
            }
        }
    }

}