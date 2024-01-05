package com.jef.cache;

import com.alibaba.fastjson.JSONObject;
import com.jef.constant.BasicConstant;
import com.jef.constant.BasicList;
import com.jef.entity.User;
import com.jef.redis.RedisJavaUtil;
import com.jef.util.PrintUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.SetParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Jef
 */
public class RedisJavaTest {

    private Jedis jedis;

    @BeforeEach
    void init() {
        jedis = RedisJavaUtil.getAuthJedis();
    }

    /**
     * Redis Java String(字符串) 实例
     */
    @Test
    public void testSetAndGetString() {
        jedis = RedisJavaUtil.getAuthJedis();
        // 设置 redis 字符串数据
        jedis.set(BasicConstant.USER_NAME_KEY, BasicConstant.USER_NAME);
        // 会覆盖
        jedis.set(BasicConstant.USER_NAME_KEY, BasicConstant.USER_NAME_ZHANGSAN);
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: " + jedis.get(BasicConstant.USER_NAME_KEY));
    }

    @DisplayName("setnx")
    @Test
    public void testSetNx() {
        // 为1，表示无对应key设置成功
        Long setnxResult = jedis.setnx(BasicConstant.USER_NAME_KEY, BasicConstant.USER_NAME);
        System.out.println("setnxResult = " + setnxResult);
        // 不会覆盖，为0，表示有对应key没有设置成功
        setnxResult = jedis.setnx(BasicConstant.USER_NAME_KEY, BasicConstant.USER_NAME_ZHANGSAN);
        System.out.println("setnxResult = " + setnxResult);
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: " + jedis.get(BasicConstant.USER_NAME_KEY));
    }

    /**
     * Redis Java List(列表) 实例
     * 数据列表、最近搜索
     */
    @Test
    public void testSetAndGetList() {
        // 存储数据到列表中
        String key = "site-list";
        jedis.lpush(key, "Runoob");
        jedis.lpush(key, "Google");
        jedis.lpush(key, "Taobao");
//        jedis.lpop(key);
        // lpush lpop 或者 rpush rpop后进先出->栈
        // 获取存储的数据并输出，从左到右
        List<String> list = jedis.lrange(key, 0, -1);
        for (String s : list) {
            System.out.println("列表项为: " + s);
        }
    }

    /**
     * 实现消息队列
     * 先进先出
     */
    @Test
    public void testMessageQueue() {
        String key = "site-list";
        // 存储数据到列表中
        // 保持队列有最新的10个
        // rpush lpop，先进先出->队列
        for (int i = 1; i <= 20; i++) {
            List<String> list = jedis.lrange(key, 0, -1);
            if (list.size() >= 10) {
                jedis.lpop(key);
            }
            jedis.rpush(key, String.valueOf(i));
        }
        List<String> list = jedis.lrange(key, 0, -1);
        System.out.println(list);

    }

    /**
     * Redis Java Set(集合) 实例
     */
    @Test
    public void testGetSetAndGetSet() {
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
    public void testGetSetAndGetHash() {
        jedis.hset(BasicConstant.LOGIN_OBJECT_KEY, BasicConstant.USER_NAME_KEY, BasicConstant.USER_NAME);
        String userName = jedis.hget(BasicConstant.LOGIN_OBJECT_KEY, BasicConstant.USER_NAME_KEY);
        Assertions.assertEquals(BasicConstant.USER_NAME, userName);
    }

    /**
     * Redis Java SortedSet(有序集合) 实例
     * 排行、热搜
     */
    @Test
    public void testGetSetAndGetZSet() {
        String key = BasicConstant.PROGRAMMING_LANGUAGE_RANK;
        jedis.zadd(key, 10, "Java");
        jedis.zadd(key, 8, "GO");
        jedis.zadd(key, 9, "Python");
        jedis.zadd(key, 7, "C++");
        long rankSetSize = jedis.zcard(key);
        System.out.println("rankSetSize=" + rankSetSize);
        PrintUtil.printSplitLine();

        Set<Tuple> rankRevSet = jedis.zrevrangeWithScores(key, 0, -1);
        System.out.println("正排行，从大到小=" + rankRevSet);
        Set<String> rankRevSetV2 = jedis.zrevrange(key, 0L, 2L);
        System.out.println("正排行V2=" + rankRevSetV2);
        Set<Tuple> rankSet = jedis.zrangeWithScores(key, 0L, 2L);
        System.out.println("逆排行=" + rankSet);
        // 其中有序集成员按分数值递增(从小到大)顺序排列。也就是说数值越大，返回的索引越大
        long javaRankIndex = jedis.zrank(key, "Java");
        long javaRank = rankSetSize - javaRankIndex;
        System.out.println("一开始的Java排行=" + javaRank);
        PrintUtil.printSplitLine();

        System.out.println("Python分数加2");
        jedis.zincrby(key, 2, "Python");
        rankRevSet = jedis.zrevrangeWithScores(key, 0L, 2L);
        System.out.println("正排行=" + rankRevSet);
        javaRankIndex = jedis.zrank(key, "Java");
        javaRank = rankSetSize - javaRankIndex;
        System.out.println("然后的Java排行=" + javaRank);
    }


    /**
     * Redis Java Keys 实例
     */
    @Test
    public void testGetAllKeys() {
        // 获取Jedis中的所有key数据并输出
        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }
    }

    /**
     * Redis递增
     */
    @Test
    public void testIncr() {
        String key = "incrKey";
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
        jedis.hset(BasicConstant.LOGIN_OBJECT_KEY, BasicConstant.USER_NAME_KEY, BasicConstant.USER_NAME);
        String userName = jedis.hget(BasicConstant.LOGIN_OBJECT_KEY, BasicConstant.USER_NAME_KEY);
        System.out.println("userName=" + userName);
        jedis.del(BasicConstant.LOGIN_OBJECT_KEY);
        userName = jedis.hget(BasicConstant.LOGIN_OBJECT_KEY, BasicConstant.USER_NAME_KEY);
        System.out.println("userName=" + userName);
    }

    /**
     * 数据读取
     */
    @DisplayName("数据读取，先缓存，再数据库")
    @Test
    public void testObjectGet() {
        String key = BasicConstant.LOGIN_OBJECT_KEY;
        String value = RedisJavaUtil.get(key);
        System.out.println(value);
        value = RedisJavaUtil.get(key);
        System.out.println(value);
    }

    @DisplayName("数据更新")
    @Test
    public void testObjectUpdate() throws InterruptedException {
        String key = BasicConstant.LOGIN_OBJECT_KEY;
        String value = RedisJavaUtil.get(key);
        System.out.println(value);

        RedisJavaUtil.update(key);
        // 数据更新后，获取数据时仍然从数据库取
        value = RedisJavaUtil.get(key);
        System.out.println(value);
    }

    @DisplayName("设置过期时间并取出")
    @Test
    void testSetTTLAndGet() throws InterruptedException {
        jedis.set(BasicConstant.USER_NAME_KEY, BasicConstant.USER_NAME, SetParams.setParams().ex(60));
        Long ttl = jedis.ttl(BasicConstant.USER_NAME_KEY);
        System.out.println("ttl=" + ttl);
        Thread.sleep(1000);
        ttl = jedis.ttl(BasicConstant.USER_NAME_KEY);
        System.out.println("ttl=" + ttl);
    }

    @DisplayName("其他功能")
    @Test
    void testOtherFunction() {
        jedis.save();
        jedis.bgsave();
    }


    @DisplayName("HyperLogLog")
    @Test
    void testHyperLogLog() {
        int index = 0;
        // N次请求，100次请求
        for (int i = 0; i < 100; i++) {
            // 过期时间不能在这里设置
            jedis.pfadd("uv", "user_" + i);
            jedis.pfadd("uv", "user_" + i);
        }
        // 读取uv 理论值是: 100
        Long uv = jedis.pfcount("uv");
        System.out.println("uv: " + uv);
        // 重复执行时，需要删除原先的
        // jedis.del("uv");
    }

    @Test
    @DisplayName("压缩解压缩")
    void testCompress() {
        List<User> userList = BasicList.getUserList();
        String zipBefore = JSONObject.toJSONString(userList);
        System.out.println("压缩后大小：" + zipBefore.getBytes().length + "字节");
        String gzip = RedisJavaUtil.compress(zipBefore);
        System.out.println("压缩后大小：" + gzip.getBytes().length + "字节");
        String compressKey = "compressKey";
        jedis.set(compressKey, gzip);
        String json = jedis.get(compressKey);
        zipBefore = RedisJavaUtil.uncompress(json);
        List<User> userBefore = JSONObject.parseObject(zipBefore, List.class);
        Assertions.assertEquals(userList.size(), userBefore.size());
    }

    @Test
    @DisplayName("热搜")
    void testHatSearch() {
        String key = "serachKey";
        List<String> searchNameList = new ArrayList<>(Arrays.asList("a", "b", "c", "a", "a", "b", "d"));
        for (String searchName : searchNameList) {
            jedis.zincrby(key, 1, searchName);
        }
        Set<Tuple> rankRevSet = jedis.zrevrangeWithScores(key, 0, -1);
        System.out.println("正排行，从大到小=" + rankRevSet);
        Set<String> rankRevSetV2 = jedis.zrevrange(key, 0L, 2L);
        System.out.println("热搜前3名=" + rankRevSetV2);
    }

}