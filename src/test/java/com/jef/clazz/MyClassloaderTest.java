package com.jef.clazz;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

/**
 * @author tufujie
 * @date 2024/1/23
 */
public class MyClassloaderTest {

    /**
     * 类加载路径
     */
    final static String path = "E:\\Desktop\\MyGet\\java-jdk\\target\\classes\\";
    final static String JAR_PATH = "E:\\Desktop\\jar";
    final static String JAR_PATH_ONE = "E:\\Desktop\\jar\\slf4j-api-1.7.5.jar";
    final static String JAR_PATH_TWO = "E:\\Desktop\\jar\\slf4j-api-1.7.25.jar";

    final static String JAR_CLASS_NAME = "org.slf4j.MDC";

    @Test
    public void testFindClass() throws Exception {
        MyClassLoader myClassloader1 = new MyClassLoader(path);
        MyClassLoader myClassloader2 = new MyClassLoader(path);
        // 主要此处不能用loadClass，使用loadClass就会由系统类加载器去加载target下的User.class了
        Class<?> userClass2 = myClassloader1.findClass("com.jef.entity.User");
        Class<?> userClass1 = myClassloader2.findClass("com.jef.entity.User");
        System.out.println(userClass2 == userClass1);
        System.out.println("userClass1的类加载器是" + userClass1.getClassLoader());
        System.out.println("userClass2的类加载器是" + userClass2.getClassLoader());
    }

    @Test
    void testV2() throws Exception {
        Field classes = ClassLoader.class.getDeclaredField("classes");
        classes.setAccessible(true);
        MyClassLoader myClassloader1 = new MyClassLoader(path);
        // 使用loadClass会遵循双亲委派机制最终由系统类加载器加载User类
        Class<?> userClass1 = myClassloader1.loadClass("com.jef.entity.User");
        System.out.println("************打印系统类加载器加载的类******************");
        Vector a = (Vector) classes.get(myClassloader1.getParent());
        for (Object o : a) {
//            System.out.println(o);
        }
        System.out.println("************打印MyClassloader加载器加载的类****************");
        Vector b = (Vector) classes.get(myClassloader1);
        for (Object o : b) {
            System.out.println(o);
        }
        Class<?> userClass2 = myClassloader1.findClass("com.jef.entity.User");
        System.out.println(userClass2 == userClass1);

        System.out.println("userClass1的类加载器是" + userClass1.getClassLoader());

        System.out.println("userClass2的类加载器是" + userClass2.getClassLoader());


        System.out.println("************再次打印系统类加载器加载的类******************");
        Vector aa = (Vector) classes.get(myClassloader1.getParent());
        for (Object o : aa) {
//            System.out.println(o);
        }
        System.out.println("************再次打印MyClassloader加载器加载的类****************");

        Vector bb = (Vector) classes.get(myClassloader1);
        for (Object o : bb) {
            System.out.println(o);
        }
    }

    @Test
    void testV3() throws ClassNotFoundException {
        MyClassLoader myClassloader1 = new MyClassLoader(path);
        // 主要此处不能用loadClass，使用loadClass会遵循双亲委派机制userClass2不会真正加载而是直接被赋予userClass1的引用
        Class<?> userClass1 = myClassloader1.findClass("com.jef.entity.User");
        Class<?> userClass2 = myClassloader1.findClass("com.jef.entity.User");
    }

    @Test
    void testV4() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        MyClassLoader myClassLoader = new MyClassLoader(path);
        Class<?> aClass = myClassLoader.findClass("com.jef.entity.User");
        Object o = aClass.newInstance();
    }

    @Test
    void testVN() throws MalformedURLException, ClassNotFoundException {
        URL[] urls = {
                new File(JAR_PATH_ONE).toURL(),
                new File(JAR_PATH_TWO).toURL()
        };
        URLClassLoader urlClassLoader = new URLClassLoader(urls);
        Class<?> clazz = urlClassLoader.loadClass(JAR_CLASS_NAME);
        Method method = null;
        try {
            method = clazz.getDeclaredMethod("bwCompatibleGetMDCAdapterFromBinder");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        method.setAccessible(true);
    }


}