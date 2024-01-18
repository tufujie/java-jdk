package com.jef.algorithm;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 统计不同号码的个数
 *
 * @author tufujie
 * @date 2024/1/5
 */
public class PhoneNumberKinds {

    static List<String> numberList = Lists.newArrayList("00000000", "00000000", "00000001", "00000002", "00000003", "00000100", "99999999");
//    static List<String> numberList = Lists.newArrayList("00000100");

    public static void main(String[] args) {
        // "00000000" 到 "99999999"，共1亿个数字
        int[] arr = new int[100000000 / 32];
        for (String number : numberList) {
            int num = Integer.valueOf(number);
            // 数组的位置
            int index = num / 32;
            // 位图的位置
            int bitIndex = num % 32;
            // 对应bit位设置为1
            arr[index] |= (1 << bitIndex);
        }
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < 32; j++) {
                // 获取对应bit位的值
                int targetBit = (arr[i] >> j) & 1;
                if (targetBit == 1) {
                    count++;
                }
            }
        }
        System.out.println("numberCount=" + count);
    }

}