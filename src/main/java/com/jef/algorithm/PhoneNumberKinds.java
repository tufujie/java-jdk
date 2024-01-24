package com.jef.algorithm;

import com.google.common.collect.Lists;
import com.jef.util.BitUtil;

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
        int[] arr = new int[numberList.size() / 32 + 1];
        for (String number : numberList) {
            int num = Integer.valueOf(number);
            BitUtil.set(arr, num);
        }
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < 32; j++) {
                // 获取对应bit位的值
                if (BitUtil.read(arr[i], j) == 1) {
                    count++;
                }
            }
        }
        System.out.println("numberCount=" + count);
    }

}