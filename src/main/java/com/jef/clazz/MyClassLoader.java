package com.jef.clazz;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 自定义类加载器
 *
 * @author tufujie
 * @date 2024/1/23
 */
public class MyClassLoader extends ClassLoader {
    private String path;

    public MyClassLoader(String path) {
        this.path = path;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes1;
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            // 将.换成/
            String classFilePath = path + name.replace(".", "/") + ".class";
            // 获取.class文件的输入流
            fileInputStream = new FileInputStream(classFilePath);
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = fileInputStream.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, len);
            }
            // 获取.class文件的字节数组
            bytes1 = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new ClassNotFoundException(e.getMessage());
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 调用defineClass加载该类
        return defineClass(name, bytes1, 0, bytes1.length, null);
    }
}
