package com.jef.designpattern.structure.proxy.demo;

import com.jef.util.PrintUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 静态代理、动态代理
 *
 * @author Jef
 * @date 2023/10/25
 */
public class ProxyPatternDemo {
    // 定义接口
    interface Image {
        void display();
    }

    // 目标类
    static class RealImage implements Image {
        private String filename;

        public RealImage(String filename) {
            this.filename = filename;
            loadFromDisk();
        }

        private void loadFromDisk() {
            System.out.println("Loading image: " + filename);
        }

        @Override
        public void display() {
            System.out.println("Displaying image: " + filename);
        }
    }

    // 代理类
    static class ImageProxy implements Image {
        private String filename;
        private RealImage realImage;

        public ImageProxy(String filename) {
            this.filename = filename;
        }

        @Override
        public void display() {
            if (realImage == null) {
                realImage = new RealImage(filename);
            }
            realImage.display();
        }
    }

    // InvocationHandler 实现类
    static class ImageInvocationHandler implements InvocationHandler {
        private Object target;

        public ImageInvocationHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("Before method: " + method.getName());
            Object result = method.invoke(target, args);
            System.out.println("After method: " + method.getName());
            return result;
        }
    }

    public static void main(String[] args) {
        // 静态代理
        // 使用代理对象显示图片
        Image image = new ImageProxy("test.jpg");
        image.display();
        // 图片已加载，直接显示，无需重新加载
        image.display();

        PrintUtil.printSplitLine();

        // 动态代理
        // 创建目标对象
        Image realImage = new RealImage("test.jpg");

        // 创建 InvocationHandler 实例
        ImageInvocationHandler handler = new ImageInvocationHandler(realImage);

        // 创建代理对象
        Image imageProxy = (Image) Proxy.newProxyInstance(Image.class.getClassLoader(),
                new Class[]{Image.class}, handler);

        // 使用代理对象显示图片
        imageProxy.display();
    }
}
