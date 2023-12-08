package com.jef.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Jef
 * @date 2023/5/14
 */
public class CountDownLatchDemo extends Thread {

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        int taskNum = 4;
        CountDownLatch countDownLatchNew = new CountDownLatch(taskNum);
        for (int i = 0; i < taskNum; i++) {
            new CountDownLatchDemo(countDownLatchNew).start();
        }
        countDownLatch.await(10, TimeUnit.SECONDS);
        System.out.println("所有线程执行完毕，进行汇总、分析等任务");
    }

    private static CountDownLatch countDownLatch;

    public CountDownLatchDemo(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        System.out.println("线程" + Thread.currentThread().getName() + "处理业务完毕，等待其他线程处理完毕");
        countDownLatch.countDown();
    }
}