package com.jef.cache;

import com.jef.constant.BasicConstant;
import com.jef.redis.RedisJavaUtil;
import com.jef.util.BusinessUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import redis.clients.jedis.Jedis;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author Jef
 * @date 2023/10/29
 */
public class DistributedLockTest {

    /**
     * Redis分布式锁
     * setnx + 过期时间
     * 设置缓存和设置过期时间不是原子性操作
     */
    @Test
    void testSetNx() {
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        String key = BasicConstant.LOGIN_OBJECT_KEY;
        // 实现锁的超时释放
        int expireTime = 10;
        // 在Spring中，redisTemplate.opsForValue().setIfAbsent()
        if (jedis.setnx(key, BasicConstant.USER_NAME) == 1) {
            System.out.println("当前事务获取锁");
            jedis.expire(key, expireTime);
            try {
                // 业务处理
                BusinessUtil.doSomeThing();
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
     */
    @DisplayName("setex实现分布式锁")
    @Test
    void testSetEx() {
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        String key = BasicConstant.LOGIN_OBJECT_KEY;
        // 实现锁的超时释放
        int expireTime = 10;
        for (int i = 0; i < 20; i++) {
            if ("OK".equals(jedis.setex(key, expireTime, BasicConstant.USER_NAME))) {
                System.out.println("当前事务获取锁");
                try {
                    // 业务处理
                    BusinessUtil.doSomeThing();
                } finally {
                    // 释放锁
                    jedis.del(key);
                }

            } else {
                System.out.println("被其它事务占用");
            }
        }
    }

    /**
     * Redis分布式锁
     * 实现库存-1
     */
    @DisplayName("set nx px实现分布式锁")
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

    @DisplayName("Redisson实现分布式锁")
    @Test
    public void testReduceStockByRedisson() throws InterruptedException {
        RedissonClient redissonClient = RedisJavaUtil.getRedissonClient();
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        RLock lock = redissonClient.getLock("redisson:stockLock");
        String STOCKKEY = "stock";
        // 初始化为10个库存
        jedis.set(STOCKKEY, "10");
        System.out.println("开始库存为" + jedis.get(STOCKKEY));
        long expireTime = 10L;
        for (int i = 0; i < 20; i++) {
            // 尝试获取锁
            boolean locked = lock.tryLock(expireTime, TimeUnit.SECONDS);
            // 或者
//            lock.lock(expireTime, TimeUnit.SECONDS);
            if (locked) {
                try {
                    String stock = jedis.get(STOCKKEY);
                    int stockNum = Integer.parseInt(stock);
                    if (stockNum > 0) {
                        // 设置库存减1
                        stockNum--;
                        jedis.set(STOCKKEY, stockNum + "");
                        System.out.println("库存-1，设置库存" + stockNum);
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

    @DisplayName("Zookeeper curator实现分布式锁")
    @Test
    void testByZookeeper() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();
        InterProcessMutex lock = new InterProcessMutex(client, "/mylock");
        long expireTime = 10L;
        // 获取锁

        boolean locked = lock.acquire(expireTime, TimeUnit.SECONDS);
        if (locked) {
            try {
                // 执行互斥访问的代码 2181:
                BusinessUtil.doSomeThing();
            } catch (Exception e) {
                // 处理异常
            } finally {
                try {
                    lock.release(); // 释放锁
                } catch (Exception e) {
                    // 处理异常
                }
            }
        }
        client.close();
    }

}