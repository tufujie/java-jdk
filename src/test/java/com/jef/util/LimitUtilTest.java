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
        for (int i = 0; i < 5; i++) {
            try {
                LimitUtil.limitInterfaceTimeTotalReq(2L);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void testAcquireByRedisAndLua() throws Exception {
        for (int i = 0; i < 5; i++) {
            boolean acquire = false;
            try {
                acquire = LimitUtil.acquireByRedisAndLua();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("是否获得令牌：" + acquire);
            if (acquire) {
                BusinessUtil.doSomeThing();
            } else {
                System.out.println("进行限流");
            }
        }
    }

}