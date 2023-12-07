package com.jef.cmd;

import java.io.IOException;

/**
 * @author tufujie
 * @date 2023/11/28
 */
public class CmdExecutionExample {

    public static void main(String[] args) throws IOException, InterruptedException {
        // 调用CMD命令
        Runtime.getRuntime().exec("pandoc test.docx --extract-media=. -o test.md");
    }

}