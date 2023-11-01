package com.jef.thread.lock;

import com.jef.util.BusinessUtil;
import com.jef.util.ExceptionUtil;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock的使用
 *
 * @author Jef
 * @date 2019/1/31
 */
public class LockDemo {
    // 注意这个地方
    private Lock lock = new ReentrantLock();

    public void insert(Thread thread) {
        lock.lock();
        try {
            BusinessUtil.taskHasReturn("Lock");
        } catch (Exception e) {
            ExceptionUtil.getExceptionStackTraceMessage("锁异常", e);
        } finally {
            System.out.println("lock，" + thread.getName() + "释放了锁");
            lock.unlock();
        }
    }

    public void insertTryLock(Thread thread) throws InterruptedException {
        // 设置超时时间，避免死锁
        boolean locked = lock.tryLock(2, TimeUnit.SECONDS);
        if (locked) {
            try {
                BusinessUtil.taskHasReturn("tryLock");
            } catch (Exception e) {
                ExceptionUtil.getExceptionStackTraceMessage("锁异常", e);
            } finally {
                System.out.println("tryLock，" + thread.getName() + "释放了锁");
                lock.unlock();
            }
        } else {
            System.out.println("tryLock，" + thread.getName() + "获取锁失败");
        }
    }

    public static void main(String[] args) {
        final LockDemo test = new LockDemo();

        new Thread() {
            @Override
            public void run() {
                test.insert(Thread.currentThread());
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                test.insert(Thread.currentThread());
            }
        }.start();

        new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                test.insertTryLock(Thread.currentThread());
            }
        }.start();
    }
}