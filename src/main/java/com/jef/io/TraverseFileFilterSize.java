package com.jef.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 遍历文件夹文件获取空文件或者是待完善的文件
 *
 * @author tufujie
 * @date 2023/12/7
 */
public class TraverseFileFilterSize {
    private static final String DIRECTORY_PATH = "";

    // md文件只写标题的大小
    private static final int BIT_SIZE = 70;

    private static List<String> FILE_LIST = new ArrayList<String>();

    /**
     * 真正递归的方法
     *
     * @param path 根目录的抽象路径
     */
    public void traverseFileLessThanSize(String path, List<String> filterPath) {
        File file = new File(path);
        if (file.exists()) {
            File[] fileArray = file.listFiles();
            for (File f : fileArray) {
                if (f.isDirectory()) {
                    traverseFileLessThanSize(f.getAbsolutePath(), filterPath);
                } else {
                    if (f.length() <= BIT_SIZE) {
                        boolean filterFlag = false;
                        for (String filter : filterPath) {
                            if (f.getAbsolutePath().contains(filter)) {
                                filterFlag = true;
                                break;
                            }
                        }
                        if (!filterFlag) {
                            System.out.println(f.getAbsoluteFile() + " size: " + f.length());
                            FILE_LIST.add(f.getAbsolutePath());
                        }
                    }
                }
            }
        } else {
            System.out.println("文件不存在！");
        }
    }

    public static void main(String[] args) throws Exception {
        TraverseFileFilterSize traverseFile = new TraverseFileFilterSize();
        if (args.length == 0) {
            throw new Exception("请输入目录");
        }
        List<String> filterPath = new ArrayList<>();
        if (args.length == 2) {
            filterPath.add(args[1]);
        }
        traverseFile.traverseFileLessThanSize(args[0], filterPath);
        if (!FILE_LIST.isEmpty()) {
            System.out.println("以下文件待完善，请及时完善");
            System.out.println("------------------------");
            for (String filePath : FILE_LIST) {
                System.out.println(filePath);
            }
            System.out.println("------------------------");
        } else {
            System.out.println("所有文件已完善，请继续加油！");
        }
    }
}
