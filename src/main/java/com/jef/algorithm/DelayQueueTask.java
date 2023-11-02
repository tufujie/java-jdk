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
        scheduleTime = currentTime;
    }

    @Override
    public int compareTo(Delayed other) {
        long td = this.getDelay(TimeUnit.MILLISECONDS);
        long od = other.getDelay(TimeUnit.MILLISECONDS);
        return Long.compare(td, od);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(runningTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
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