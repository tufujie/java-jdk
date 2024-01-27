package com.jef.clazz;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tufujie
 * @date 2024/1/24
 */
public class SpringBeanFactory {
    private final static String path = "E:\\Desktop\\MyGet\\java-jdk\\target\\classes\\";
    private static Map<Class, Object> beanList = new HashMap<>();

    public SpringBeanFactory() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        MyClassLoader myClassLoader = new MyClassLoader(path);
                        // 这里就暂且只加载王厨师了，实战是应加载全部类
                        Class<?> aClass = myClassLoader.findClass("com.jef.clazz.Cook_wang");
                        beanList.put(Cook.class, aClass.newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }


    public static <T> T getBean(Class<T> beanClass) {
        Object object = beanList.get(beanClass);
        T cast = beanClass.cast(object);
        return cast;
    }

}