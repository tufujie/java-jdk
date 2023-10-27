package com.jef.util;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

/**
 * @author Jef
 * @date 2023/10/24
 */
public class LimitUtilTest {

    @Test
    void testLimitBy() {
        LimitUtil.compareBurstyRequest();
    }

    @Test
    void testLimitInterfaceTotalReq() {
        for (int i = 0; i < 100; i++) {
            new Thread() {
                @Override
                public void run() {
                    LimitUtil.limitInterfaceTotalReq(2L);
                }
            }.start();
        }
    }

    @Test
    void testLimitInterfaceTimeTotalReq() {
        for (int i = 0; i < 100; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        LimitUtil.limitInterfaceTimeTotalReq(5L);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    @Test
    void testAcquireByRedisAndLua() throws Exception {
        boolean acquire = LimitUtil.acquireByRedisAndLua();
        System.out.println("是否获得令牌：" + acquire);
    }

}