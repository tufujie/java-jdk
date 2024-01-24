package com.jef.util;

/**
 * @author tufujie
 * @date 2024/1/18
 */
public class BitUtil {

    // 将num所在的第index位置的int值进行bit操作
    public static void set(int[] numArr, int num) {
        // 确定使用int数组中的下标
        int index = num / 32;
        // 确定一个int表示32位bit中的哪一位bit
        int position = num % 32;
        numArr[index] = set(numArr[index], position);
    }


    // 读取num值对应的位置，判断是0/1
    public static int read(int[] numArr, int num) {
        // 确定使用int数组中的下标
        int index = num / 32;
        // 确定一个int表示32位bit中的哪一位bit
        int position = num % 32;
        return read(numArr[index], position);
    }

    // 将int值的position位置bit值设置为1，position为0到31
    public static int set(int num, int position) {
        return num |= (1 << position);
    }

    // 判断int值的第position个bit值是否是1，position为0到31
    public static int read(int num, int position) {
        return (num >> position) & 1;
    }

}