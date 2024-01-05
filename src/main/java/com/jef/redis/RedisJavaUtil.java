package com.jef.redis;

import com.jef.util.BusinessUtil;
import com.jef.util.PrintUtil;
import com.jef.util.StringUtils;
import org.apache.commons.codec.binary.Base64;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Jef
 */
public class RedisJavaUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisJavaUtil.class);

    private volatile static Jedis jedisSingleton;

    private RedisJavaUtil() {

    }

    /**
     * 获取认证的Jedis对象
     *
     * @return
     */
    public static synchronized Jedis getAuthJedis() {
        if (jedisSingleton == null) {
            synchronized (RedisJavaUtil.class) {
                if (jedisSingleton == null) {
                    // 连接本地的 Redis 服务
                    jedisSingleton = new Jedis("localhost");
                    // 使用 CONFIG SET requirepass "root"，其中root可自定义，如果已经设置了，
                    // 可以通过CONFIG GET requirepass获取
                    // (error) NOAUTH Authentication required.如果出现这个错误，则说明没有验证，使用AUTH yourpassword进行验证
                    jedisSingleton.auth("root");
                    System.out.println("Redis连接成功");
                    // 查看服务是否运行
                    System.out.println("服务正在运行: " + jedisSingleton.ping());
                    PrintUtil.printSplitLine();
                }

            }
        }
        return jedisSingleton;
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

    /**
     * 尝试获取锁
     *
     * @param time     超时时间
     * @param timeUnit 时间单位
     * @return 是否获得锁
     */
    public static boolean tryLock(RLock lock, long time, TimeUnit timeUnit) {
        try {
            return lock.tryLock(time, timeUnit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查询数据
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        Jedis cache = getAuthJedis();
        // 从缓存中获取数据
        String cacheValue = cache.get(key);
        // 缓存为空
        if (StringUtils.isBlank(cacheValue)) {
            System.out.println("缓存没有，从数据库读取");
            // 从存储中获取
            String storageValue = BusinessUtil.getDataFromDatabase(key);
            cache.set(key, storageValue);
            // 如果存储数据为空， 需要设置一个过期时间(5分钟，300秒)
            if (storageValue == null) {
                cache.expire(key, 60 * 5);
            }
            return storageValue;
        } else {
            System.out.println("缓存存在，直接返回缓存");
            // 缓存非空
            return cacheValue;
        }
    }

    /**
     * 更新数据
     * 延迟双删
     *
     * @param key
     * @return
     */
    public static void update(String key) throws InterruptedException {
        Jedis cache = getAuthJedis();
        cache.del(key);
        BusinessUtil.updateTable();
        // 睡眠时间可以根据get方法的时长进行设置
        Thread.sleep(1000);
        cache.del(key);
    }

    /**
     * 使用gzip压缩字符串
     * GZip压缩 256字节以上才有压缩效果
     *
     * @param str 要压缩的字符串
     * @return 压缩后的字符串
     */
    public static String compress(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(str.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            logger.error("字符串压缩失败str:{}，错误信息:{}", str, e.getMessage());
            throw new RuntimeException("字符串压缩失败");
        }
        return Base64.encodeBase64String(out.toByteArray());
    }

    /**
     * 使用gzip解压缩
     *
     * @param compressedStr 压缩字符串
     * @return 解压后的字符串
     */
    public static String uncompress(String compressedStr) {
        if (compressedStr == null || compressedStr.isEmpty()) {
            return compressedStr;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in;
        GZIPInputStream gzip = null;
        byte[] compressed;
        String decompressed;
        try {
            compressed = Base64.decodeBase64(compressedStr);
            in = new ByteArrayInputStream(compressed);
            gzip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int offset;
            while ((offset = gzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            logger.error("字符串解压失败compressedStr:{}，错误信息:{}", compressedStr, e.getMessage());
            throw new RuntimeException("字符串解压失败");
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException ignored) {
                }
            }
            try {
                out.close();
            } catch (IOException ignored) {
            }
        }
        return decompressed;
    }

}
