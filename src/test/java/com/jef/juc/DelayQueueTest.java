package com.jef.juc;

import com.jef.algorithm.DelayQueueTask;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

/**
 * 延迟队列
 *
 * @author Jef
 * @date 2023/11/2
 */
public class DelayQueueTest {

    @DisplayName("DelayQueue测试")
    @Test
    void testDelayQueue() throws InterruptedException {
        // 可以用来执行定时任务
        BlockingQueue<DelayQueueTask> task = new DelayQueue<>();
        long now = System.currentTimeMillis();
        DelayQueueTask t1 = new DelayQueueTask("t1", now + 1000);
        DelayQueueTask t2 = new DelayQueueTask("t2", now + 2000);
        DelayQueueTask t3 = new DelayQueueTask("t3", now + 1500);
        DelayQueueTask t4 = new DelayQueueTask("t4", now + 3000);
        DelayQueueTask t5 = new DelayQueueTask("t5", now + 500);
        DelayQueueTask t6 = new DelayQueueTask("t6", now + 2500);

        task.put(t1);
        task.put(t2);
        task.put(t3);
        task.put(t4);
        task.put(t5);
        task.put(t6);

        for (int i = 0; i < 6; i++) {
            task.take().execTask();
        }
    }

}