package com.jef.annotation;

import com.jef.util.BusinessUtil;

import javax.annotation.PostConstruct;

/**
 * @author tufujie
 * @date 2024/1/3
 */
public class PostConstructDemo {

    public static void main(String[] args) {
        new PostConstructDemo();
    }

    @PostConstruct
    void init() {
        BusinessUtil.doSomeThing();
    }

}