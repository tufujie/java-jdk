package com.jef.util;

import com.jef.business.BusinessDemo;

import com.google.common.util.concurrent.RateLimiter;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author Jef
 * @date 2023/10/24
 */
public class LimitUtil {

    /**
     * RateLimiter的设计哲学：它允许瞬间的流量波峰超过QPS，但瞬间过后的请求将会等待较长的时间来缓解上次的波峰，
     * 以使得平均的QPS等于预定值。
     * 突发请求情况对比
     */
    public static void compareBurstyRequest() {
        // 每秒生成的令牌数
        RateLimiter burstyLimiter = RateLimiter.create(5);
        RateLimiter warmingUpLimiter = RateLimiter.create(5, 2, TimeUnit.SECONDS);

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

}