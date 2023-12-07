package com.jef.io;

import java.io.File;

/**
 * 遍历文件夹文件
 * 用递归的方式
 * 【遍历文件夹】
 *
 * @author tufujie
 * @date 2023/12/7
 */
public class TraverseFile {
    /**
     * 真正递归的方法
     *
     * @param path 根目录的抽象路径
     */
    public void traverseFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            File[] fileArray = file.listFiles();
            for (File f : fileArray) {
                if (f.isDirectory()) {
                    System.out.println("【文件夹】-----" + f.getAbsolutePath());
                    // -----递归的方法体
                    traverseFile(f.getAbsolutePath());
                } else {
                    System.out.println("【文件】-----" + f.getAbsolutePath());
                }
            }
        } else {
            System.out.println("文件不存在！");
        }
    }

    public static void main(String[] args) {
        TraverseFile traverseFile = new TraverseFile();
        //参数"path"为：要遍历的文件根目录
        traverseFile.traverseFile("E:\\Desktop\\test\\testSize");
    }
}
