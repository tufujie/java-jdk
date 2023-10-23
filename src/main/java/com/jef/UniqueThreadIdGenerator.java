package com.jef;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jef on 2017-08-22.
 */
public class UniqueThreadIdGenerator {
    private static final AtomicInteger uniqueId = new AtomicInteger(0);

    private static final ThreadLocal < Integer > uniqueNum =
            new ThreadLocal < Integer > () {
                @Override
                protected Integer initialValue() {
                    return uniqueId.getAndIncrement();
                }
            };

    public static int getCurrentThreadId() {
        return uniqueId.get();
    }

    public static void main(String[] args) {
        System.out.println(uniqueNum.get());
        System.out.println(getCurrentThreadId());
        System.out.println(uniqueNum.get());
        System.out.println(getCurrentThreadId());
    }

}
