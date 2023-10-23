package com.jef.cache;

import com.jef.business.BusinessDemo;
import com.jef.constant.BasicConstant;
import com.jef.redis.RedisJavaUtil;
import com.jef.util.PrintUtil;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author Jef
 */
public class RedisJavaTest {

    /**
     * Redis Java String(字符串) 实例
     */
    @Test
    public void testSetAndGetString() {
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        // 设置 redis 字符串数据
        jedis.set(BasicConstant.USER_NAME_KEY, BasicConstant.USER_NAME);
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: " + jedis.get(BasicConstant.USER_NAME_KEY));
    }

    /**
     * Redis Java List(列表) 实例
     */
    @Test
    public void testSetAndGetList() {
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        // 存储数据到列表中
        jedis.lpush("site-list", "Runoob");
        jedis.lpush("site-list", "Google");
        jedis.lpush("site-list", "Taobao");
        // 获取存储的数据并输出
        List<String> list = jedis.lrange("site-list", 0, 2);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("列表项为: " + list.get(i));
        }
    }

    /**
     * Redis Java Set(集合) 实例
     */
    @Test
    public void testGetSetAndGetSet() {
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        jedis.sadd("site-set", "Runoob");
        jedis.sadd("site-set", "Google");
        jedis.sadd("site-set", "Taobao");
        // 获取存储的数据并输出
        Set<String> list = jedis.smembers("site-set");
        System.out.println(list);
    }

    /**
     * Redis Java Hash(哈希) 实例
     */
    @Test
    public void testGetSetAndGeHash() {
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        jedis.hset(BasicConstant.LOGIN_OBJECT_KEY, BasicConstant.USER_NAME_KEY, BasicConstant.USER_NAME);
        String userName = jedis.hget(BasicConstant.LOGIN_OBJECT_KEY, BasicConstant.USER_NAME_KEY);
        Assertions.assertTrue(BasicConstant.USER_NAME.equals(userName));
    }

    /**
     * Redis Java SortedSet(有序集合) 实例
     */
    @Test
    public void testGetSetAndGeZSet() {
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        jedis.zadd(BasicConstant.PROGRAMMING_LANGUAGE_RANK, 10, "Java");
        jedis.zadd(BasicConstant.PROGRAMMING_LANGUAGE_RANK, 9, "Python");
        jedis.zadd(BasicConstant.PROGRAMMING_LANGUAGE_RANK, 8, "GO");
        jedis.zadd(BasicConstant.PROGRAMMING_LANGUAGE_RANK, 7, "C++");
        long rankSetSize = jedis.zcard(BasicConstant.PROGRAMMING_LANGUAGE_RANK);
        System.out.println("rankSetSize=" + rankSetSize);
        PrintUtil.printSplitLine();

        Set<Tuple> rankRevSet = jedis.zrevrangeWithScores(BasicConstant.PROGRAMMING_LANGUAGE_RANK, 0L, 2L);
        System.out.println("正排行=" + rankRevSet);
        Set<Tuple> rankSet = jedis.zrangeWithScores(BasicConstant.PROGRAMMING_LANGUAGE_RANK, 0L, 2L);
        System.out.println("逆排行=" + rankSet);
        // 其中有序集成员按分数值递增(从小到大)顺序排列。也就是说数值越大，返回的索引越大
        long javaRankIndex = jedis.zrank(BasicConstant.PROGRAMMING_LANGUAGE_RANK, "Java");
        long javaRank = rankSetSize - javaRankIndex;
        System.out.println("一开始的Java排行=" + javaRank);
        PrintUtil.printSplitLine();

        System.out.println("Python分数加2");
        jedis.zincrby(BasicConstant.PROGRAMMING_LANGUAGE_RANK, 2, "Python");
        rankRevSet = jedis.zrevrangeWithScores(BasicConstant.PROGRAMMING_LANGUAGE_RANK, 0L, 2L);
        System.out.println("正排行=" + rankRevSet);
        javaRankIndex = jedis.zrank(BasicConstant.PROGRAMMING_LANGUAGE_RANK, "Java");
        javaRank = rankSetSize - javaRankIndex;
        System.out.println("然后的Java排行=" + javaRank);
    }


    /**
     * Redis Java Keys 实例
     */
    @Test
    public void testGetAllKeys() {
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        // 获取Jedis中的所有key数据并输出
        Set<String> keys = jedis.keys("*");
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            System.out.println(key);
        }
    }

    /**
     * Redis递增
     */
    @Test
    public void testIncr() {
        String key = "incrKey";
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        long num = jedis.incr(key);
        System.out.println("incrKey=" + num);
        num = jedis.incr(key);
        System.out.println("incrKey=" + num);
    }

    /**
     * Redis递减
     */
    @Test
    public void testDecr() {
        String key = "decrKey";
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        jedis.set(key, "10");
        long num = jedis.decr(key);
        System.out.println("decrKey=" + num);
        num = jedis.decr(key);
        System.out.println("decrKey=" + num);
    }

    /**
     * Redis删除
     */
    @Test
    public void testDel() {
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        jedis.hset(BasicConstant.LOGIN_OBJECT_KEY, BasicConstant.USER_NAME_KEY, BasicConstant.USER_NAME);
        String userName = jedis.hget(BasicConstant.LOGIN_OBJECT_KEY, BasicConstant.USER_NAME_KEY);
        System.out.println("userName=" + userName);
        jedis.del(BasicConstant.LOGIN_OBJECT_KEY);
        userName = jedis.hget(BasicConstant.LOGIN_OBJECT_KEY, BasicConstant.USER_NAME_KEY);
        System.out.println("userName=" + userName);
    }

    /**
     * Redis分布式锁
     */
    @Test
    void testSetNx() {
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        String key = BasicConstant.LOGIN_OBJECT_KEY;
        if (jedis.setnx(key, BasicConstant.USER_NAME) == 1) {
            System.out.println("当前事务获取锁");
            // 实现锁的超时释放
            int expireTime = 60;
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
    public void testReduceStock() {
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
