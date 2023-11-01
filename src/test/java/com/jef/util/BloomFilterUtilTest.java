package com.jef.util;

import com.jef.redis.RedisJavaUtil;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 布隆过滤器测试
 *
 * @author Jef
 * @date 2023/11/1
 */
public class BloomFilterUtilTest {

    @DisplayName("自定义布隆过滤器")
    @Test
    void testCustomBloom() {
        String key = UUID.randomUUID().toString();
        BloomFilterUtil bloomFilterUtil = new BloomFilterUtil();
        System.out.println("s1是否存在：" + bloomFilterUtil.contains(key));
        bloomFilterUtil.add(key);
        System.out.println("s1是否存在：" + bloomFilterUtil.contains(key));
    }

    @DisplayName("Guava布隆过滤器")
    @Test
    void testGuavaBloom() {
        // 初始化布隆过滤器，设计预计元素数量为100_0000L，误差率为1%
        int n = 100_0000;
        BloomFilter<CharSequence> bloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), n, 0.01);
        for (int i = 0; i < n; i++) {
            bloomFilter.put(String.valueOf(i));
        }
        int count = 0;
        for (int i = 0; i < (n * 2); i++) {
            if (bloomFilter.mightContain(String.valueOf(i))) {
                count++;
            }
        }
        System.out.println("过滤器误判率：" + 1.0 * (count - n) / n);
    }

    @DisplayName("Hutool布隆过滤器")
    @Test
    void testHutoolBloom() {
        int n = 1_0000;
        BitMapBloomFilter bitMapBloomFilter = new BitMapBloomFilter(5);
        for (int i = 0; i < n; i++) {
            bitMapBloomFilter.add(String.valueOf(i));
        }
        int count = 0;
        for (int i = 0; i < (n * 2); i++) {
            if (bitMapBloomFilter.contains(String.valueOf(i))) {
                count++;
            }
        }
        System.out.println("过滤器误判率：" + 1.0 * (count - n) / n);
    }

    @DisplayName("Redisson布隆过滤器")
    @Test
    void testRedissonBloom() {
        RedissonClient redissonClient = RedisJavaUtil.getRedissonClient();
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter("bloomnumber");
        // 初始化布隆过滤器，设计预计元素数量为100_0000L，误差率为1%
        int n = 1_0000;
        bloomFilter.tryInit(1_0000L, 0.01);
        for (int i = 0; i < n; i++) {
            bloomFilter.add(String.valueOf(i));
        }
        int count = 0;
        for (int i = 0; i < (n * 2); i++) {
            if (bloomFilter.contains(String.valueOf(i))) {
                count++;
            }
        }
        System.out.println("过滤器误判率：" + 1.0 * (count - n) / n);
    }


}