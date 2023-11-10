package com.jef.algorithm;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author Jef
 * @date 2023/11/2
 */
public class DelayQueueTask implements Delayed {
    long currentTime = System.currentTimeMillis();

    String name;
    long runningTime;
    long scheduleTime;


    public DelayQueueTask(String name, long runningTime) {
        this.name = name;
        this.runningTime = runningTime;
        this.scheduleTime = currentTime;
    }

    @Override
    public int compareTo(Delayed other) {
        long td = this.getDelay(TimeUnit.MILLISECONDS);
        long od = other.getDelay(TimeUnit.MILLISECONDS);
        return Long.compare(td, od);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long residue = runningTime - System.currentTimeMillis();
        // 500毫秒触发一次比较，直到<=0触发
        System.out.println("task=" + name + "还剩" + residue + "ms触发");
        return unit.convert(residue, TimeUnit.MILLISECONDS);
    }

    @Override
    public String toString() {
        return name + "-" + runningTime;
    }

    public void execTask() {
        long startTime = System.currentTimeMillis();
        System.out.println("Task " + name + ": 任务添加时间=" + scheduleTime + ",任务开始时间="
                + startTime + ",延迟=" + (startTime - scheduleTime));
    }
}