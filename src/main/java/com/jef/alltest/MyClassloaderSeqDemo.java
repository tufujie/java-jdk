package com.jef.alltest;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 不同的jar有相同的全量名（包名+类名）加载顺序验证
 *
 * @author tufujie
 * @date 2024/1/23
 */
public class MyClassloaderSeqDemo {

    // 没有该方法
    final static String JAR_PATH_ONE = "E:\\Desktop\\jar\\slf4j-api-1.7.5.jar";

    // 有该方法
    final static String JAR_PATH_TWO = "E:\\Desktop\\jar\\slf4j-api-1.7.25.jar";

    final static String JAR_CLASS_NAME = "org.slf4j.MDC";


    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException {
        URL[] urls = {
                new File(JAR_PATH_ONE).toURL(),
                new File(JAR_PATH_TWO).toURL()
        };
        URLClassLoader urlClassLoader = new URLClassLoader(urls);
        Class<?> clazz = urlClassLoader.loadClass(JAR_CLASS_NAME);
        Method method;
        try {
            method = clazz.getDeclaredMethod("bwCompatibleGetMDCAdapterFromBinder");
        } catch (NoSuchMethodException e) {
            System.out.println("no such method exception");
        }
        System.out.println("change seq");
        urls = new URL[]{
                new File(JAR_PATH_TWO).toURL(),
                new File(JAR_PATH_ONE).toURL()
        };
        urlClassLoader = new URLClassLoader(urls);
        clazz = urlClassLoader.loadClass(JAR_CLASS_NAME);
        try {
            method = clazz.getDeclaredMethod("bwCompatibleGetMDCAdapterFromBinder");
        } catch (NoSuchMethodException e) {
            System.out.println("no such method 2");
        }

    }


}