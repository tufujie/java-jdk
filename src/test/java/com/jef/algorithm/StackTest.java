package com.jef.algorithm;

import com.jef.util.Stack;

import org.junit.jupiter.api.Test;

/**
 * @author Jef
 * @date 2023/10/27
 */
public class StackTest {

    @Test
    public void testStack() {
        Stack<String> stack = new Stack<String>();
        stack.push("1");
        stack.push("2");
        stack.push("3");
        // 获取栈顶：3
        System.out.println("peek()=" + stack.peek());
        // 获取栈顶：3，并移除
        System.out.println("pop()=" + stack.pop());
        // 重新获取栈顶：2
        System.out.println("peek()=" + stack.peek());
    }

}