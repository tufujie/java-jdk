package com.jef;

import com.jef.business.CreateOrderService;
import com.jef.business.RedisPool;
import com.jef.constant.BasicConstant;
import com.jef.redis.RedisJavaUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

/**
 * @author tufujie
 * @date 2023/12/7
 */
public class CreateOrderServiceTest {
    CreateOrderService createOrderService = new CreateOrderService();

    private static final Integer USER_SIZE = 10;

    @Test
    @DisplayName("错误案例一：数据库update相互覆盖")
    void testCreateOrderErrorV1() throws Exception {
        for (int i = 1; i <= USER_SIZE; i++) {
            int finalI = i;
            new Thread() {
                @Override
                public void run() {
                    try {
                        createOrderService.createOrderErrorV1(1L, 1, Long.valueOf(finalI));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    @Test
    @DisplayName("错误案例二：扣减串行执行，但是库存被扣减为负数")
    void testCreateOrderErrorV2() throws Exception {
        for (int i = 1; i <= USER_SIZE; i++) {
            int finalI = i;
            new Thread() {
                @Override
                public void run() {
                    try {
                        createOrderService.createOrderErrorV2(1L, 1, Long.valueOf(finalI));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    @Test
    @DisplayName("错误案例三：使用 synchronized 实现内存中串行校验，但是依旧扣减为负数")
    void testCreateOrderErrorV3() throws Exception {
        for (int i = 1; i <= USER_SIZE; i++) {
            int finalI = i;
            new Thread() {
                @Override
                public void run() {
                    try {
                        createOrderService.createOrderErrorV3(1L, 1, Long.valueOf(finalI));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    @Test
    @DisplayName("正确示例：将事务包含在锁的控制范围内")
    void testCreateOrderSingleRightV1() throws Exception {
        for (int i = 1; i <= USER_SIZE; i++) {
            int finalI = i;
            new Thread() {
                @Override
                public void run() {
                    try {
                        createOrderService.createOrderSingleRightV1(1L, 1, Long.valueOf(finalI));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    @Test
    @DisplayName("正确示例：使用synchronized的代码块")
    void testCreateOrderSingleRightV2() throws Exception {
        for (int i = 1; i <= USER_SIZE; i++) {
            int finalI = i;
            new Thread() {
                @Override
                public void run() {
                    try {
                        createOrderService.createOrderSingleRightV2(1L, 1, Long.valueOf(finalI));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    @Test
    @DisplayName("正确示例：使用Lock")
    void testCreateOrderSingleRightV3() throws Exception {
        for (int i = 1; i <= USER_SIZE; i++) {
            int finalI = i;
            new Thread() {
                @Override
                public void run() {
                    try {
                        createOrderService.createOrderSingleRightV3(1L, 1, Long.valueOf(finalI));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    @Test
    @DisplayName("正确示例：Redis setNx")
    void testCreateOrderDistributedRightV1() throws Exception {
        Jedis jedis = RedisPool.getJedis();
        String orderStockKey = BasicConstant.ORDER_STOCK_KEY;
        // 实现锁的超时释放
        int millisecondsToExpire = 1000;
        for (int i = 1; i <= USER_SIZE; i++) {
            String requestId = String.valueOf(System.currentTimeMillis());
            // 在Spring中，redisTemplate.opsForValue().setIfAbsent()
            if (RedisJavaUtil.tryGetDistributedLock(jedis, orderStockKey, requestId, millisecondsToExpire)) {
                try {
                    createOrderService.createOrderDistributedRightV1(1L, 1, Long.valueOf(i));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 释放锁
                    RedisJavaUtil.releaseDistributedLock(jedis, orderStockKey, requestId);
                }
            } else {
                System.out.println("当前事务没有获得锁");
            }
        }
    }

    @Test
    @DisplayName("正确示例：Redission")
    void testCreateOrderDistributedRightV2() throws Exception {
        RedissonClient redissonClient = RedisJavaUtil.getRedissonClient();
        long waitTime = 30L;
        for (int i = 1; i <= USER_SIZE; i++) {
                   /* // 尝试获取锁
                    boolean locked = false;
                    try {
                        locked = lock.tryLock(waitTime, TimeUnit.SECONDS);
                    } catch (Throwable e) {
                        if (lock.isHeldByCurrentThread()) {
                            lock.unlock();
                        }
                        throw new Exception(e.getMessage());
                    }
                    if (locked) {
                        try {
                            createOrderService.createOrderDistributedRightV1(1L, 1, Long.valueOf(i));
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            // 释放锁
                            lock.unlock();
                        }
                    }*/
            RLock rLock = redissonClient.getLock("redisson:stockLock");
            rLock.lock(waitTime, TimeUnit.SECONDS);
            try {
                createOrderService.createOrderDistributedRightV1(1L, 1, Long.valueOf(i));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                rLock.unlock();
            }
        }
    }

    @DisplayName("正确示例：Zookeeper curator实现分布式锁")
    @Test
    void testCreateOrderDistributedRightV3() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();
        InterProcessMutex lock = new InterProcessMutex(client, "/order");
        long waitTime = 10L;
        for (int i = 1; i <= USER_SIZE; i++) {
            // 获取锁
            boolean locked;
            try {
                locked = lock.acquire(waitTime, TimeUnit.SECONDS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (locked) {
                try {
                    // 执行互斥访问的代码 2181:
                    createOrderService.createOrderDistributedRightV1(1L, 1, Long.valueOf(i));
                } catch (Exception e) {
                    // 处理异常
                    e.printStackTrace();
                } finally {
                    try {
                        lock.release(); // 释放锁
                    } catch (Exception e) {
                        // 处理异常
                        e.printStackTrace();
                    }
                }
            }
        }
        client.close();
    }

}