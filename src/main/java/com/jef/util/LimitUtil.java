package com.jef.util;

import com.jef.business.BusinessDemo;
import com.jef.redis.RedisJavaUtil;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.util.concurrent.RateLimiter;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

/**
 * 限流工具
 *
 * @author Jef
 * @date 2023/10/24
 */
public class LimitUtil {

    private static AtomicLong atomic = new AtomicLong(0);

    /**
     * RateLimiter的设计哲学：它允许瞬间的流量波峰超过QPS（允许一定程度的突发），但瞬间过后的请求将会等待较长的时间来缓解上次的波峰，
     * 以使得平均的QPS等于预定值。
     * 突发请求情况对比
     */
    public static void compareBurstyRequest() {
        // 平滑限流某个接口的请求数，5：每秒生成的令牌数，1秒产生5个，每个200ms
        RateLimiter burstyLimiter = RateLimiter.create(5);
        System.out.println("突发耗时=" + burstyLimiter.acquire(10));
        RateLimiter warmingUpLimiter = RateLimiter.create(5, 2, TimeUnit.SECONDS);

        // 突发测试

        DecimalFormat df = new DecimalFormat("0.00");
        // 积攒1秒
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("稳定限流器");
        IntStream.range(1, 11).forEach(a -> {
            double acquire = burstyLimiter.acquire();
            BusinessDemo.doSomeThing();
            System.out.println("第" + a + "次请求等待时间：" + df.format(acquire));
        });

        System.out.println("预热限流器");
        IntStream.range(1, 11).forEach(a -> {
            double acquire = warmingUpLimiter.acquire();
            BusinessDemo.doSomeThing();
            System.out.println("第" + a + "次请求等待时间：" + df.format(acquire));
        });
    }

    /**
     * 限流某个接口的总并发/请求数
     *
     * @param limit 限流数
     */
    public static void limitInterfaceTotalReq(Long limit) {
        try {
            if (atomic.incrementAndGet() > limit) {
                // 拒绝请求
                System.out.println("限流了，拒绝请求");
                return;
            }
            // 处理请求
            BusinessDemo.doSomeThing();
        } finally {
            atomic.decrementAndGet();
        }
    }

    /**
     * 限流某个接口的时间窗请求数
     *
     * @param limit 限流数
     */
    public static void limitInterfaceTimeTotalReq(Long limit) throws ExecutionException {
        LoadingCache<Long, AtomicLong> counter = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .build(new CacheLoader<Long, AtomicLong>() {
                    @Override
                    public AtomicLong load(Long seconds) throws Exception {
                        return new AtomicLong(0);
                    }
                });
        while (true) {
            // 得到当前秒
            long currentSeconds = System.currentTimeMillis() / 1000;
            if (counter.get(currentSeconds).incrementAndGet() > limit) {
                System.out.println("限流了:" + currentSeconds);
                continue;
            }
            //业务处理
            BusinessDemo.doSomeThing();
        }
    }

    /**
     * Redis + LUA限流
     *
     * @return true or false
     * @throws Exception
     */
    public static boolean acquireByRedisAndLua() throws Exception {
        String luaScript = Files.toString(new File("src\\main\\java\\com\\jef\\util\\limit.lua"), Charset.defaultCharset());
        Jedis jedis = RedisJavaUtil.getAuthJedis();
        // 针对IP限流，总流量可能超过阈值，所以更多是针对请求接口限流
        // 此处将当前时间戳取秒数来模拟不同的请求URL
        String key = "url:" + System.currentTimeMillis() / 1000;
        // 限流大小
        String limit = "2";
        return (Long) jedis.eval(luaScript, Lists.newArrayList(key), Lists.newArrayList(limit)) == 1;
    }


}