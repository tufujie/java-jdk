package com.jef.algorithm;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueTest {
    @Test
    public void testQueue() {
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
        queue.add("1");
        queue.add("2");
        queue.add("3");
        // 获取对头：1
        System.out.println("peek()=" + queue.peek());
        System.out.println(queue);
        // 获取对头：1，并移除
        System.out.println("poll()=" + queue.poll());
        System.out.println(queue);
        // 重新获取对头：2
        System.out.println("peek()=" + queue.peek());
        // 队尾添加元素
        queue.offer("4");
        // 重新获取对头：2
        System.out.println("peek()=" + queue.peek());
    }
}
