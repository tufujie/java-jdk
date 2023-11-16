package com.jef.alltest;


import com.jef.entity.User;

/**
 * @author Jef
 * 全部测试
 */
public class AllTest {
    private String a = null;

    public static void main(String[] args) {
        User user = new User();
        String userName = user.getName();
        System.out.println(userName);
        System.out.println(userName);
        System.out.println(userName);
    }

    private void extracted() {
        Integer a = 1;
        int b = a + 1;
        int c = b + 1;
    }

}
