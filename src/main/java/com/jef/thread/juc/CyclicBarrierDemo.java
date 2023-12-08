package com.jef.thread.juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Jef
 * @date 2023/5/14
 */
public class CyclicBarrierDemo extends Thread {

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        int taskNum = 4;
        int parties = taskNum + 1;
        CyclicBarrier cyclicBarrierNew = new CyclicBarrier(parties);
        for (int i = 0; i < taskNum; i++) {
            new CyclicBarrierDemo(cyclicBarrierNew).start();
        }
        cyclicBarrier.await();
        System.out.println("所有线程执行完毕，进行汇总、分析等任务");
    }

    private static CyclicBarrier cyclicBarrier;

    public CyclicBarrierDemo(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        try {
            System.out.println("线程" + Thread.currentThread().getName() + "处理业务完毕，等待其他线程处理完毕");
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}