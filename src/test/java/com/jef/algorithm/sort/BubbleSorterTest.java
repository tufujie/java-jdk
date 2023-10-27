package com.jef.algorithm.sort;

import com.jef.constant.BasicConstant;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

/**
 * @author Jef
 * @date 2023/10/27
 */
public class BubbleSorterTest {

    @Test
    void testSort() {
        Integer[] array = BasicConstant.INTEGER_ARRAY;
        new BubbleSorter().sort(array);
        System.out.println(JSONObject.toJSONString(array));
    }

    @Test
    void testSortV2() {
        Integer[] array = BasicConstant.INTEGER_ARRAY;
        new BubbleSorter().sort(array, ArraySortUtil.getDownSortComparator());
        System.out.println(JSONObject.toJSONString(array));
    }

}