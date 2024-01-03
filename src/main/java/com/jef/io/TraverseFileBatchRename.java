package com.jef.io;

import java.io.File;
import java.util.Scanner;

/**
 * 遍历文件夹文件批量修改文件名
 *
 * @author tufujie
 * @date 2023/12/28
 */
public class TraverseFileBatchRename {

    /**
     * 真正递归的方法
     *
     * @param path 根目录的抽象路径
     */
    public void traverseFileBatchRename(String path, String beforeName, String afterName) {
        File file = new File(path);
        if (file.exists()) {
            File[] fileArray = file.listFiles();
            for (File f : fileArray) {
                String fileName = f.getName();
                if (fileName.contains(beforeName)) {
                    f.renameTo(new File(path + "\\" + fileName.replace(beforeName, afterName)));
                }
            }
        } else {
            System.out.println("文件不存在！");
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入目录、修改前、修改后");
        String allValue = scanner.nextLine();
        String[] arr = allValue.split(" ");
        TraverseFileBatchRename traverseFile = new TraverseFileBatchRename();
        if (arr.length < 3) {
            throw new Exception("请输入目录、修改前、修改后");
        }
        traverseFile.traverseFileBatchRename(arr[0], arr[1], arr[2]);
    }
}
